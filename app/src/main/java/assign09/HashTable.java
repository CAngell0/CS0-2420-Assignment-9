package assign09;

import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> implements Map<K, V> {
    private Object[] array = new Object[11];
    private boolean[] deleted = new boolean[11];
    private int size = 0;
    private int capacity = 11;
    private double loadThreshold = 0.5;

    @Override
    public void clear() {
        array = new Object[capacity];
        deleted = new boolean[capacity];
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int i = 0;

        while (true) {
            int j = calcIndex(key, i);
            Object curr = array[j];

            // hit empty slot
            if (curr == null && !deleted[j]) {
                return false;
            }

            if (curr != null && !deleted[j]) {
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    return true;
                }
            }
            i++;
        }

    }

    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < array.length; i++) {
            if (!deleted[i] && array[i] != null) {
                if (toEntry(array[i]).getValue().equals(value))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<MapEntry<K, V>> entries() {
        List<MapEntry<K, V>> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (!deleted[i] && array[i] != null) {
                list.add(toEntry(array[i]));
            }
        }
        return list;
    }

    // TODO:bail when hitting a empty array slot and handle tombstones(deleted ==
    // true)
    @Override
    public V get(Object key) {
        int i = 0;

        while (true) {
            int j = calcIndex(key, i);
            Object curr = array[j];

            // key not in table
            if (curr == null && !deleted[j]) {
                return null;
            }

            if (curr != null && !deleted[j]) {
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
            i++;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // TODO:resized if threshold hits loadThreshold fix tombstone handling
    @Override
    public V put(K key, V value) {
        if (calculateLoadFactor() >= loadThreshold) {
            resizeBackingArray();
        }

        MapEntry<K, V> toBePut = new MapEntry<>(key, value);

        int firstTombstone = -1;
        int i = 0;

        while (i < capacity) {
            int j = calcIndex(key, i);
            Object curr = array[j];

            // empty spot at curr
            if (curr == null) {
                // insert at j or at first tombstone found
                int insertIndex = (firstTombstone != -1) ? firstTombstone : j;

                array[insertIndex] = toBePut;
                deleted[insertIndex] = false;
                size++;
                return null;
            }

            // spot used before
            if (!deleted[j]) {
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    V toReturn = entry.getValue();
                    array[j] = toBePut;
                    return toReturn;
                }
                // else not same key keep probing

            } else {
                // if first tombstone remember for when we hit an empty spot
                if (firstTombstone == -1) {
                    firstTombstone = j;
                }
            }
            i++;
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int step = 0;
        int index = calcIndex(key, step);

        MapEntry<K, V> current = toEntry(array[index]);

        while (current != null && step < capacity){
            if (current.getKey().equals(key)){
                if (deleted[index]) return null;

                deleted[index] = true;
                size--;
                return current.getValue();
            }

            step++;
            index = calcIndex(key, step);
            current = toEntry(array[index]);
        }

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private MapEntry<K, V> toEntry(Object entry){
        return (MapEntry<K, V>) entry;
    }



    private double calculateLoadFactor() {
        return (double) size / capacity;
    }

    private int calcIndex(Object key, int step){
        int start = Math.abs(key.hashCode()) % capacity;
        return (start + (step * step)) % capacity;
    }

    private int calculateNextCapacity() {
        int number = capacity * 2;
        boolean found = false;

        while (!found) {
            number++;
            if (isPrime(number))
                found = true;
        }

        return number;
    }
    private boolean isPrime(int num) {
        if (num % 2 == 0 || num % 3 == 0)
            return false;

        for (int i = 5; i * i <= num; i = i + 6)
            if (num % i == 0 || num % (i + 2) == 0)
                return false;

        return true;
    }

    private void resizeBackingArray() {
        int newCapacity = calculateNextCapacity();
        capacity = newCapacity;
        
        Object[] newArray = new Object[newCapacity];
        Object[] currentArray = array;

        int oldSize = size;
        size = 0;

        array = newArray;
        for (Object entryObject : currentArray) {
            if (entryObject == null) continue;

            MapEntry<K, V> entry = toEntry(entryObject);
            put(entry.getKey(), entry.getValue());
        }

        size = oldSize;
    }
}
