package com.wildsmith.jarble.utils;

import android.support.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {

    /**
     * Is this list empty. Checks for null as well as size.
     *
     * @return true if empty, false if not
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Is this list empty. Checks for null as well as size.
     *
     * @return true if empty, false if not
     */
    public static boolean isEmpty(Object[] collection) {
        return collection == null || collection.length == 0;
    }

    /**
     * Check to see if the given index is with in the range of collection items
     *
     * @return true if the index is a index in the list, false otherwise
     */
    public static boolean isValidIndex(Collection collection, int index) {
        return !isEmpty(collection) && index < collection.size() && index >= 0;
    }

    /**
     * Clone the passed list and cast to the new type if the items in the list extend or are an instanceof the passed class.
     *
     * @return the cloned list
     */
    public static <T extends Object, P extends Object> List<P> cloneListAndCast(List<T> list, Class<P> clazz) {
        List<P> clone = new ArrayList<>(list.size());
        for (T item : list) {
            if (clazz.isAssignableFrom(item.getClass())) {
                @SuppressWarnings("unchecked")
                P newItem = (P) item;
                clone.add(newItem);
            }
        }
        return clone;
    }

    /**
     * Clone the passed list
     *
     * @return the cloned list
     */
    public static <T extends Object> List<T> cloneList(List<T> list) {
        List<T> clone = new ArrayList<>(list.size());
        for (T item : list) {
            clone.add(item);
        }
        return clone;
    }

    /**
     * @return a new list containing all values in both lists
     */
    public static <T> T[] concatenate(T[] listA, T[] listB) {
        if (isEmpty(listA)) {
            return listB;
        }

        if (isEmpty(listB)) {
            return listA;
        }

        int listALength = listA.length;
        int listBLength = listB.length;

        @SuppressWarnings("unchecked")
        T[] listC = (T[]) Array.newInstance(listA.getClass().getComponentType(), listALength + listBLength);
        System.arraycopy(listA, 0, listC, 0, listALength);
        System.arraycopy(listB, 0, listC, listALength, listBLength);

        return listC;
    }

    /**
     * @return a new list containing all values in both lists
     */
    public static <T> List<T> concatenate(List<T> listA, List<T> listB) {
        if (isEmpty(listA)) {
            return listB;
        }

        if (isEmpty(listB)) {
            return listA;
        }

        List<T> listC = cloneList(listA);
        listC.addAll(listB);

        return listC;
    }

    /**
     * @return a string array of the original list of strings
     */
    @Nullable
    public static String[] toStringArray(List<String> list) {
        if (isEmpty(list)) {
            return null;
        }

        String[] array = new String[list.size()];
        for (int index = 0; index < list.size(); index++) {
            array[index] = list.get(index);
        }

        return array;
    }

    /**
     * @return a list of strings that has duplicates removed
     */
    @Nullable
    public static List<String> removeStringDuplicates(List<String> list) {
        if (isEmpty(list)) {
            return null;
        }

        Set<String> set = new HashSet<>();
        set.addAll(list);
        return new ArrayList<>(set);
    }

    /**
     * @return true if the array contains the given value, false otherwise
     */
    public static <T> boolean contains(T[] array, T value) {
        if (isEmpty(array)) {
            return false;
        }

        for (T item : array) {
            if (item != null && item.equals(value)) {
                return true;
            }
        }

        return false;
    }
}