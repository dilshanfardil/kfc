package lk.sayuru.jungleapp.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<PlaceItem> ITEMS = new ArrayList<PlaceItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, PlaceItem> ITEM_MAP = new HashMap<String, PlaceItem>();

    private static void addItem(PlaceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void createItem(String s) {
        addItem(new PlaceItem(s, s,s));
    }

    public static void makeDetails(List<String> places) {
        for (String place : places) {
            createItem(place);
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class PlaceItem {
        public final String id;
        public final String place;
        public final String details;

        public PlaceItem(String id, String place, String details) {
            this.id = id;
            this.place = place;
            this.details = details;
        }

        @Override
        public String toString() {
            return place;
        }
    }
}
