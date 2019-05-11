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
public class PathContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<PathItem> ITEMS = new ArrayList<PathItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, PathItem> ITEM_MAP = new HashMap<String, PathItem>();



    private static void addItem(PathItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PathItem createItem(String s) {
        return new PathItem(s,s,s);
    }

    public static void makeDetails(List<String> paths) {
        ITEM_MAP.clear();
        ITEMS.clear();
        for (String path : paths) {
            addItem(createItem(path));
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class PathItem {
        public final String id;
        public final String content;
        public final String details;

        public PathItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
