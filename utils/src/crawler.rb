require 'sqlite3'
require 'rest_client'
require 'set'
require 'json'
require 'open-uri'
#require 'nokogiri'
require 'work_queue'
require 'openssl'

module OpenSSL
  module SSL
    remove_const :VERIFY_PEER
  end
end
OpenSSL::SSL::VERIFY_PEER = OpenSSL::SSL::VERIFY_NONE

class Crawler
  def initialize
    @gw2sdb = SQLite3::Database.new 'gw2s.db'
    @gw2sdb.results_as_hash = true
    @gw2dbdb = SQLite3::Database.new 'gw2db.db'
    @gw2dbdb.results_as_hash = true

    init_db()
  end

  def close
    @gw2sdb.close
    @gw2dbdb.close
  end


  def init_db
    #items table
    @gw2sdb.execute '''create table if not exists gw2s_items(
            id integer primary key asc,
            name text not null,
            rarity text not null,
            level integer not null,
            type text not null,
            subtype text,
            icon_file_id integer not null,
            icon_file_signature text not null,
            json text);'''

    @gw2sdb.execute('create index if not exists gw2s_items_name on gw2s_items(name asc)')
    @gw2sdb.execute('create index if not exists gw2s_items_rarity on gw2s_items(rarity asc)')
    @gw2sdb.execute('create index if not exists gw2s_items_level on gw2s_items(level asc)')
    @gw2sdb.execute('create index if not exists gw2s_items_type on gw2s_items(type asc)')
    @gw2sdb.execute('create index if not exists gw2s_items_subtype on gw2s_items(subtype asc)')
    #@gw2sdb.execute('create index if not exists gw2s_items_icon_file_id on gw2s_items(icon_file_id asc)')
    #@gw2sdb.execute('create index if not exists gw2s_items_icon_file_signature on gw2s_items(icon_file_signature asc)')

    #recipes table
    @gw2sdb.execute '''create table if not exists gw2s_recipes(
            id integer primary key asc,
            output_item_id integer not null,
            min_rating integer not null,
            type text not null,
            json text not null);'''

    @gw2sdb.execute '''create table if not exists gw2s_recipes_disciplines(
            id integer not null,
            discipline text not null,
            primary key(id, discipline),
            foreign key(id) references gw2s_recipes(id));'''

    @gw2sdb.execute '''create table if not exists gw2s_recipes_ingredients(
            id integer not null,
            item_id integer not null,
            count integer not null,
            primary key(id, item_id),
            foreign key(id) references gw2s_recipes(id),
            foreign key(item_id) references gw2s_items(id));'''

    #@gw2sdb.execute('create index if not exists gw2s_recipes_gw2dbid on gw2s_recipes(gw2dbid asc)')
    @gw2sdb.execute('create index if not exists gw2s_recipes_output_item_id on gw2s_recipes(output_item_id asc)')
    @gw2sdb.execute('create index if not exists gw2s_recipes_min_rating on gw2s_recipes(min_rating asc)')
    @gw2sdb.execute('create index if not exists gw2s_recipes_type on gw2s_recipes(type asc)')

    @gw2sdb.execute('create index if not exists gw2s_recipes_disciplines_id on gw2s_recipes_disciplines(id asc)')
    @gw2sdb.execute('create index if not exists gw2s_recipes_disciplines_discipline on gw2s_recipes_disciplines(discipline asc)')
    @gw2sdb.execute('create index if not exists gw2s_recipes_ingredients_id on gw2s_recipes_ingredients(id asc)')
    @gw2sdb.execute('create index if not exists gw2s_recipes_ingredients_item_id on gw2s_recipes_ingredients(item_id asc)')

    #gw2db table
    @gw2dbdb.execute('''create table if not exists gw2db_items(
            id integer primary key asc,
            data_id integer,
            external_id integer,
            name text,
            image text)''')
    @gw2dbdb.execute('create index if not exists gw2db_items_data_id on gw2db_items(data_id asc)')
    @gw2dbdb.execute('create index if not exists gw2db_items_external_id on gw2db_items(external_id asc)')
    @gw2dbdb.execute('create index if not exists gw2db_items_name on gw2db_items(name asc)')

  end

  def crawl_gw2db
    puts "[#{Time.now}] Start crawl GW2DB"
    #puts "[#{Time.now}] Get item ids from local database"
    #localIds = Set.new
    #result = @gw2dbDb.query('select id from gw2db_items')
    #result.each do |row|
    #  localIds.add(row['id'])
    #end
    #result.close
    #puts "[#{Time.now}] Got #{localIds.size} item ids"

    puts "[#{Time.now}] Start getting items from GW2DB"
    #json = open('http://www.gw2db.com/json-api/items?guid=BDECAF74-E132-4B7B-9952-06C4D6E86866').read
    json = open('gw2db-items.json', 'r:utf-8').read
    items = JSON.parse(json)
    puts "[#{Time.now}] End getting items from GW2DB, #{items.size} items got"

    #proxyUri = URI.parse("http://cn-proxy.jp.oracle.com:80")
    #proxyUri = URI.parse("http://127.0.0.1:8087")

    stmt = @gw2dbdb.prepare('insert or ignore into gw2db_items(id, data_id, external_id, name, image) values(:id, :dataId, :externalId, :name, :image)')
    #partialStmt = @gw2dbDb.prepare('insert or ignore into gw2db_items(id) values(:id)')

    count = 0
    items.each { |item|
      begin
        id = item['ID']
        data_id = item['DataID']
        external_id = item['ExternalID']
        name = item['Name']
        image = item['Icon']
        image = image.gsub('32/32', '56/56')

        stmt.execute(id, data_id, external_id, name, image)

        count += 1
        if count % 100 == 0
          puts("[#{Time.now}] #{count} items completed")
        end
      rescue
        puts("[#{Time.now}] Failed when update item, the error is #{$!}")
      ensure

      end
    }

    stmt.close
    #partialStmt.close

    puts("[#{Time.now}] Complete crawl GW2DB database, #{count} items updated")
  end

  def crawl_gw2api_items
    puts("[#{Time.now}] Start crawl GW2 API items")
    puts("[#{Time.now}] Get item ids from local database")
    local_ids = Set.new
    result = @gw2sdb.query('select id from gw2s_items')
    result.each do |row|
      local_ids.add(row['id'])
    end
    result.close
    puts("[#{Time.now}] Got #{local_ids.size} local item ids")

    puts("[#{Time.now}] Read items ids from api.guildwars2")
    remote_ids = Set.new
    json = open('https://api.guildwars2.com/v1/items.json').read
    doc = JSON.parse(json)
    remote_ids.merge(doc['items'])
    puts("[#{Time.now}] Got #{remote_ids.size} remote items ids")

    ids = remote_ids - local_ids
    puts("#{ids.size} items to be fetched")

    stmt = @gw2sdb.prepare('insert or ignore into gw2s_items(id, name, rarity, level, type, subtype, icon_file_id, icon_file_signature, json) values(?, ?, ?, ?, ?, ?, ?, ?, ?)')
    wq = WorkQueue.new 10, 50

    count = 0
    ids.each { |id|
      wq.enqueue_b(id) do |id|
        begin
          json = open("https://api.guildwars2.com/v1/item_details.json?item_id=#{id}").read
          obj = JSON.parse(json)
          subtype = extract_subtype(obj)

          stmt.execute(obj['item_id'], obj['name'], obj['rarity'], obj['level'], obj['type'], subtype, obj['icon_file_id'], obj['icon_file_signature'], json)

          puts("[#{Time.now}] Item #{id} has been fetched successfully.")
          count += 1
        rescue
          puts("[#{Time.now}] Failed when fetch item #{id}, the error is #{$!}")
        end
      end
    }

    wq.join
    stmt.close

    puts("[#{Time.now}] Complete crawl GW2 API, #{count} items fetched")
  end

  def crawl_gw2api_recipes
    puts("[#{Time.now}] Start crawl GW2 API Recipes")
    puts("[#{Time.now}] Get recipe ids from local database")
    local_ids = Set.new
    result = @gw2sdb.query('select id from gw2s_recipes')
    result.each do |row|
      local_ids.add(row['id'])
    end
    result.close
    puts("[#{Time.now}] Got #{local_ids.size} local recipe ids")

    puts("[#{Time.now}] Read recipe ids from api.guildwars2")
    remote_ids = Set.new
    json = open('https://api.guildwars2.com/v1/recipes.json').read
    doc = JSON.parse(json)
    remote_ids.merge(doc['recipes'])
    puts("[#{Time.now}] Got #{remote_ids.size} remote recipes ids")

    ids = remote_ids - local_ids
    puts("#{ids.size} recipes to be fetched")

    insert_stmt = @gw2sdb.prepare('insert or ignore into gw2s_recipes(id, output_item_id, min_rating, type, json) values(?, ?, ?, ?, ?)')
    ingredient_stmt = @gw2sdb.prepare('insert or ignore into gw2s_recipes_ingredients(id, item_id, count) values(?, ?, ?)')
    discipline_stmt = @gw2sdb.prepare('insert or ignore into gw2s_recipes_disciplines(id, discipline) values(?, ?)')
    wq = WorkQueue.new 10, 50

    count = 0
    ids.each { |id|
      wq.enqueue_b(id) do |id|
        begin
          json = open("https://api.guildwars2.com/v1/recipe_details.json?recipe_id=#{id}").read
          obj = JSON.parse(json)

          @gw2sdb.transaction do
            insert_stmt.execute(id, obj['output_item_id'], obj['min_rating'], obj['type'], json)
            obj['disciplines'].each do |discipline|
              discipline_stmt.execute(id, discipline)
            end
            obj['ingredients'].each do |ingredient|
              ingredient_stmt.execute(id, ingredient['item_id'], ingredient['count'])
            end
          end

          puts("[#{Time.now}] Recipe #{id} has been fetched successfully.")
          count += 1
        rescue
          puts("[#{Time.now}] Failed when fetch recipe #{id}, the error is #{$!}")
        end
      end
    }

    wq.join
    insert_stmt.close
    discipline_stmt.close
    ingredient_stmt.close

    puts("[#{Time.now}] Complete crawl GW2 API, #{count} recipes fetched")
  end

  def correlate
    puts("[#{Time.now}] Start correlate items")

    update_stmt = @gw2sdb.prepare('update gw2s_items set gw2dbid=?, image=? where id=?')

    succeed_count = 0
    failed_count = 0
    result = @gw2sdb.query('select id, name from gw2s_items where image is null')
    result.each do |item|
      begin
        rs = @gw2dbdb.execute('select external_id, image from gw2db_items where data_id=?', item['id'])

        #try another name
        #if rs.size == 0
        #  rs = @gw2dbDb.execute("select id, image from gw2db_items where name=?", item['name'] + '[s]')
        #end

        if rs.size == 0
          puts("No item found for name #{item['name']}, id #{item['id']} in gw2db")

          failed_count += 1
        else
          row = rs[0]

          update_stmt.execute(row['external_id'], row['image'], item['id'])
          succeed_count += 1

          puts("[#{Time.now}] Item #{item['id']} with name #{item['name']} has been correlated successfully.")
        end
      rescue
        puts("[#{Time.now}] Failed when correlate item #{item['id']}, name #{item['name']}, the error is #{$!}")
      end

    end
    result.close

    update_stmt.close
    puts("[#{Time.now}] Complete correlate items, #{succeed_count} items correlated, #{failed_count} items remains not correlated")
  end

  def extract_subtype(item)
    case item['type']
      when 'Armor' then return item['armor']['type']
      when 'Bag' then return nil
      when 'Consumable' then return item['consumable']['type']
      when 'Container' then return item['container']['type']
      when 'CraftingMaterial' then return nil
      when 'Gathering' then return item['gathering']['type']
      when 'Gizmo' then return item['gizmo']['type']
      when 'MiniPet' then return nil
      when 'Tool' then return item['tool']['type']
      when 'Trinket' then return item['trinket']['type']
      when 'Trophy' then return nil
      when 'UpgradeComponent' then return item['upgrade_component']['type']
      when 'Weapon' then return item['weapon']['type']
      else return nil
    end
  end
end

crawler = Crawler.new
#crawler.crawl_gw2db()
crawler.crawl_gw2api_items()
crawler.crawl_gw2api_recipes()
#crawler.correlate()
crawler.close
