/*
 *         Copyright 2017 Ton Ly (BreadMoirai)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package net.breadmoirai.samurai.util;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArraySearch {

    /**
     * This searches through an array of type T to find an object that matches V value using a value extractor and comparator for values. The array must have been sorted before hand by the same comparator as provided to this function. If there are elements with the same extracted value, a Predicate equals function may be provided to match a single value. Otherwise, null may be passes as equals and the first value found will be returned.
     *
     * @param array      of objects T
     * @param value      value to search for
     * @param extractor  Function that takes in object T and returns value V
     * @param comparator Comparator that compares V
     * @param equals     Predicate that may be null. Used to find a specific object if array is not unique
     * @param <T>
     * @param <V>
     * @return the index of object found
     */
    public static <T, V> int binarySearch(T[] array, V value, Function<T, V> extractor, Comparator<V> comparator, Predicate<T> equals) {
        final int idx = binarySearch(array, value, extractor, comparator);
        return equals == null ? idx : bidirectionalSearch(array, equals, idx, value, extractor, comparator);
    }

    @SuppressWarnings("Duplicates")
    private static <T, V> int binarySearch(T[] array, V value, Function<T, V> extractor, Comparator<V> comparator) {
        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int mid = low + ((high - low) / 2);
            final int compare = comparator.compare(extractor.apply(array[mid]), value);
            if (compare < 0)
                high = mid - 1;
            else if (compare > 0)
                low = mid + 1;
            else
                return mid;
        }
        return -1;
    }
    @SuppressWarnings("Duplicates")
    private static <T, V> int bidirectionalSearch(T[] array, Predicate<T> equals, int idx, V value, Function<T, V> extractor, Comparator<V> comparator) {
        if (equals.test(array[idx])) return idx;
        final int size = array.length;
        boolean left = true, right = true;
        for (int i = 1; i < Math.max(size - idx, idx); i++) {
            if (right) {
                final int iR = idx + i;
                if (iR < size) {
                    if (equals.test(array[iR])) return iR;
                    else if (comparator.compare(value, extractor.apply(array[iR])) != 0) right = false;
                } else right = false;
            }
            if (left) {
                final int iL = idx - i;
                if (iL >= 0) {
                    if (equals.test(array[iL])) return iL;
                    else if (comparator.compare(value, extractor.apply(array[iL])) != 0) left = false;
                } else left = false;
            }
            if (!left && !right) break;
        }
        return -1;
    }
    /**
     * This searches through an array of type T to find an object that matches V value using a value extractor and comparator for values. The array must have been sorted before hand by the same comparator as provided to this function. If there are elements with the same extracted value, a Predicate equals function may be provided to match a single value. Otherwise, null may be passes as equals and the first value found will be returned.
     *
     * @param list       of objects T, best of type ArrayList
     * @param value      value to search for
     * @param extractor  Function that takes in object T and returns value V
     * @param comparator Comparator that compares V
     * @param equals     Predicate that may be null. Used to find a specific object if array is not unique
     * @param <T>
     * @param <V>
     * @return the index of object found
     */
    public static <T, V> int binarySearch(List<T> list, V value, Function<T, V> extractor, Comparator<V> comparator, Predicate<T> equals) {
        final int idx = binarySearch(list, value, extractor, comparator);
        return equals == null ? idx : bidirectionalSearch(list, equals, idx, value, extractor, comparator);
    }

    @SuppressWarnings("Duplicates")
    private static <T, V> int binarySearch(List<T> list, V value, Function<T, V> extractor, Comparator<V> comparator) {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = low + ((high - low) / 2);
            final int compare = comparator.compare(extractor.apply(list.get(mid)), value);
            if (compare < 0)
                high = mid - 1;
            else if (compare > 0)
                low = mid + 1;
            else
                return mid;
        }
        return -1;
    }

    private static <T, V> int bidirectionalSearch(List<T> list, Predicate<T> equals, int idx, V value, Function<T, V> extractor, Comparator<V> comparator) {
        if (equals.test(list.get(idx))) return idx;
        final int size = list.size();
        boolean left = true, right = true;
        for (int i = 1; i < Math.max(size - idx, idx); i++) {
            if (right) {
                final int iR = idx + i;
                if (iR < size) {
                    if (equals.test(list.get(iR))) return iR;
                    else if (comparator.compare(value, extractor.apply(list.get(iR))) != 0) right = false;
                } else right = false;
            }
            if (left) {
                final int iL = idx - i;
                if (iL >= 0) {
                    if (equals.test(list.get(iL))) return iL;
                    else if (comparator.compare(value, extractor.apply(list.get(iL))) != 0) left = false;
                } else left = false;
            }
            if (!left && !right) break;
        }
        return -1;
    }
}
