gw2s (this repository is archived)
====

Guild Wars 2 Scout (GW2S) is a tool to help player view dynamic events, WvW status, game items, and crafting recipes on your android device. This application is based on the Guild Wars 2 official API.

You can use this app to view:
- Dynamic events, see dynamic events status, can set to update every certain time.
- WvW, see WvW battle status, can set to update every certain time.
- Items, supports filter by item type, rarity and required level.
- Recipes, supports filter by item type, rarity and crafting level.
----

Compile
You need to install Android SDK and Google Play Services SDK to compile the app.
First of all you have to launch the Android sdk manager and download and install the following files located under "extras": Android support repository, Google play services, Google repository.

Run "gradle build" in src folder.
----

Generate local database using official API
There is a ruby script to fetch items / recipes from official API to a local sqlite database, it is located in utils/src/crawler.rb.
Before run this script, you need to install required gems.
gem install sqlite3, rest_client, work_queue
