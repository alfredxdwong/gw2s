package info.mornlight.gw2s.android.client;

import info.mornlight.gw2s.android.http.JsonClient;
import info.mornlight.gw2s.android.model.*;
import info.mornlight.gw2s.android.model.event.Event;
import info.mornlight.gw2s.android.model.event.EventDetails;
import info.mornlight.gw2s.android.model.item.ItemDetails;
import info.mornlight.gw2s.android.model.item.RecipeDetails;
import info.mornlight.gw2s.android.model.map.Continent;
import info.mornlight.gw2s.android.model.map.ContinentFloor;
import info.mornlight.gw2s.android.model.wvw.WvwMatch;
import info.mornlight.gw2s.android.model.wvw.WvwMatchDetails;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiClient extends JsonClient {
    static class EventsWrap {
        public List<Event> events;
    }
    static class WvwMatchesWrap {
        public List<WvwMatch> wvwMatches;
    }
    static class ItemsWrap {
        public List<Integer> items;
    }
    static class RecipesWrap {
        public List<Integer> recipes;
    }

    static class EventDetailsWrap {
        public Map<String, EventDetails> events;
    }

    static class ContinentsWrap {
        public Map<String, Continent> continents;
    }

    public List<Event> listEvents(int worldId) throws IOException {
        //https://api.guildwars2.com/v1/events.json?world_id=1001
        String url = "https://api.guildwars2.com/v1/events.json?world_id=" + worldId;

        HttpGet request = newGet(url);
        EventsWrap wrap = (EventsWrap) invoke(request, EventsWrap.class);

        return wrap.events;
    }

    public List<EventDetails> listEventDetails() throws IOException {
        //https://api.guildwars2.com/v1/event_details.json
        String url = "https://api.guildwars2.com/v1/event_details.json";
        HttpGet request = newGet(url);
        EventDetailsWrap wrap = (EventDetailsWrap) invoke(request, EventDetailsWrap.class);
        return new ArrayList<EventDetails>(wrap.events.values());
    }

    public List<IntName> listWorldNames(String lang) throws IOException {
        String url = "https://api.guildwars2.com/v1/world_names.json";
        return listIntNames(url, lang);
    }

    private List<IntName> listIntNames(String url, String lang) throws IOException {
        url = url + "?lang=" + lang;

        HttpGet request = newGet(url);
        List<IntName> items = (List<IntName>) invoke(request, typeFactory.constructCollectionType(List.class, IntName.class));

        return items;
    }

    private List<StringName> listStringNames(String url, String lang) throws IOException {
        url = url + "?lang=" + lang;

        HttpGet request = newGet(url);
        List<StringName> items = (List<StringName>) invoke(request, typeFactory.constructCollectionType(List.class, StringName.class));

        return items;
    }

    public List<StringName> listEventNames(String lang) throws IOException {
        String url = "https://api.guildwars2.com/v1/event_names.json";
        return listStringNames(url, lang);
    }

    public List<IntName> listMapNames(String lang) throws IOException {
        String url = "https://api.guildwars2.com/v1/map_names.json";
        return listIntNames(url, lang);
    }

    public List<IntName> listObjectiveNames(String lang) throws IOException {
        String url = "https://api.guildwars2.com/v1/wvw/objective_names.json";
        return listIntNames(url, lang);
    }

    public List<WvwMatch> listWvwMatches() throws IOException {
        //https://api.guildwars2.com/v1/wvw/matches.json
        String url = "https://api.guildwars2.com/v1/wvw/matches.json";

        HttpGet request = newGet(url);
        WvwMatchesWrap wrap = (WvwMatchesWrap) invoke(request, WvwMatchesWrap.class);

        return wrap.wvwMatches;
    }

    public WvwMatchDetails readWvwMatchDetails(String matchId) throws IOException {
        //https://api.guildwars2.com/v1/wvw/match_details.json
        String url = "https://api.guildwars2.com/v1/wvw/match_details.json?match_id=" + matchId;

        HttpGet request = newGet(url);
        WvwMatchDetails matchDetails = (WvwMatchDetails) invoke(request, WvwMatchDetails.class);

        return matchDetails;
    }

    public List<Integer> listItems() throws IOException {
        //https://api.guildwars2.com/v1/items.json
        String url = "https://api.guildwars2.com/v1/items.json";

        HttpGet request = newGet(url);
        ItemsWrap wrap = (ItemsWrap) invoke(request, ItemsWrap.class);

        return wrap.items;
    }

    public ItemDetails readItemDetails(int itemId, String lang) throws IOException {
        String url = "https://api.guildwars2.com/v1/item_details.json?item_id=" + itemId + "&lang=" + lang;

        HttpGet request = newGet(url);
        ItemDetails details = (ItemDetails) invoke(request, ItemDetails.class);

        return details;
    }

    public List<Integer> listRecipes() throws IOException {
        //https://api.guildwars2.com/v1/items.json
        String url = "https://api.guildwars2.com/v1/recipes.json";

        HttpGet request = newGet(url);
        RecipesWrap wrap = (RecipesWrap) invoke(request, RecipesWrap.class);

        return wrap.recipes;
    }

    public RecipeDetails readRecipeDetails(int itemId, String lang) throws IOException {
        String url = "https://api.guildwars2.com/v1/recipe_details.json?item_id=" + itemId + "&lang=" + lang;

        HttpGet request = newGet(url);
        RecipeDetails details = (RecipeDetails) invoke(request, RecipeDetails.class);

        return details;
    }

    public List<Continent> listContinents() throws IOException {
        String url = "https://api.guildwars2.com/v1/continents.json";

        HttpGet request = newGet(url);
        ContinentsWrap wrap = (ContinentsWrap) invoke(request, ContinentsWrap.class);

        for(Map.Entry<String, Continent> entry : wrap.continents.entrySet()) {
            entry.getValue().setId(entry.getKey());
        }

        return new ArrayList<Continent>(wrap.continents.values());
    }

    public ContinentFloor readContinentFloor(int continentId, int floor) throws IOException {
        String url = String.format("https://api.guildwars2.com/v1/map_floor.json?continent_id=%d&floor=%d",
                continentId, floor);

        HttpGet request = newGet(url);
        ContinentFloor continent = (ContinentFloor) invoke(request, ContinentFloor.class);

        return continent;
    }
}
