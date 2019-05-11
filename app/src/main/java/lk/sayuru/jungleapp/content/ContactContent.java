package lk.sayuru.jungleapp.content;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.sayuru.jungleapp.db.entity.Contact;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ContactContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ContactItem> ITEMS = new ArrayList<ContactItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ContactItem> ITEM_MAP = new HashMap<String, ContactItem>();


    private static void addItem(ContactItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ContactItem createItem(Contact contact) {
        return new ContactItem(String.valueOf(contact.getId()),contact.getFname(),contact.getPhoneNo());
    }

    public static void makeDetails(List<Contact> contacts) {
        ITEM_MAP.clear();
        ITEMS.clear();
        for (Contact contact : contacts) {
            addItem(createItem(contact));
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ContactItem {
        public final String id;
        public final String name;
        public final String phoneNumber;

        public ContactItem(String id, String name, String phoneNumber) {
            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
