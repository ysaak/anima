package ysaak.anima.utils;

import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class CollectionUtils {
    private CollectionUtils() {/**/}

    /**
     * Checks if a collection is non null and not empty
     * @param collection Collection to check
     * @param <T> Collection type
     * @return true if the collection is non null and not empty - false otherwise
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * Checks if a map is non null and not empty
     * @param map Map to check
     * @param <K> Map key type
     * @param <V> Map value type
     * @return true if the map is non null and not empty - false otherwise
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Checks if a multimap is non null and not empty
     * @param map Map to check
     * @param <K> Map key type
     * @param <V> Map value type
     * @return true if the collection is non null and not empty - false otherwise
     */
    public static <K, V> boolean isNotEmpty(Multimap<K, V> map) {
        return map != null && !map.isEmpty();
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        else {
            List<T> list = new ArrayList<>();
            iterable.forEach(list::add);
            return list;
        }
    }
}
