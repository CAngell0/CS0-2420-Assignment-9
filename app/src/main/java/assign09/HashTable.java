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
        int startIndex = homeIndex(key);
        int i = 0;

        while (true) {
            int j = quadraticProbe(startIndex, i);
            Object curr = array[j];

            // hit empty slot
            if (curr == null && !deleted[j]) {
                return false;
            }

            if (curr != null && !deleted[j]) {
                MapEntry<K, V> entry = (MapEntry<K, V>) curr;
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
                if (((MapEntry<K, V>) array[i]).getValue().equals(value))
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
                list.add((MapEntry<K, V>) array[i]);
            }
        }
        return list;
    }

    // TODO:bail when hitting a empty array slot and handle tombstones(deleted ==
    // true)
    @Override
    public V get(Object key) {
        int startIndex = homeIndex(key);
        int i = 0;

        while (true) {
            int j = quadraticProbe(startIndex, i);
            Object curr = array[j];

            // key not in table
            if (curr == null && !deleted[j]) {
                return null;
            }

            if (curr != null && !deleted[j]) {
                MapEntry<K, V> entry = (MapEntry<K, V>) curr;
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

        int startIndex = homeIndex(key);
        int firstTombstone = -1;
        int i = 0;

        while (i < capacity) {
            int j = quadraticProbe(startIndex, i);
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
                MapEntry<K, V> entry = (MapEntry<K, V>) curr;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public int size() {
        return size;
    }

    private int quadraticProbe(int start, int i) {
        return (start + i * i) % capacity;
    }

    private double calculateLoadFactor() {
        return (double) size / capacity;
    }

    // TODO: update capacity and deleted array reset size before reinserting + skip
    // null entries
    private void resizeBackingArray() {
        int newCapacity = calculateNextCapacity();
        Object[] newArray = new Object[newCapacity];
        Object[] currentArray = array;

        array = newArray;
        for (Object entryObject : currentArray) {
            MapEntry<K, V> entry = (MapEntry<K, V>) entryObject;
            put(entry.getKey(), entry.getValue());
        }
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

    /**
     * 
     * if (num <= 1) return false;
     * if (num <= 3) return true;
     * if (num % 2 == 0 || num % 3 == 0) return false;
     */
    private boolean isPrime(int num) {
        if (num % 2 == 0 || num % 3 == 0)
            return false;

        for (int i = 5; i * i <= num; i = i + 6)
            if (num % i == 0 || num % (i + 2) == 0)
                return false;

        return true;
    }

    private int homeIndex(Object key) {
        int hash = key.hashCode();
        if (hash < 0) {
            hash = -hash;
        }
        return hash % capacity;
    }
}
