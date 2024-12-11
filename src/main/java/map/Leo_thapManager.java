package map;

import io.Session;

import java.util.ArrayList;
import java.util.List;

public class Leo_thapManager {
    private static final List<Leo_thap> list = new ArrayList<>();

    public static synchronized void add_list(Leo_thap d) {
        Leo_thapManager.list.add(d);
    }

    public static synchronized void remove_list(Leo_thap d) {
        Leo_thapManager.list.remove(d);
    }

    public static synchronized Leo_thap get_list(String name) {
        for (Leo_thap leo_thap : Leo_thapManager.list) {
            if (leo_thap.name_party.equals(name)) {
                return leo_thap;
            }
        }
        return null;
    }
}
