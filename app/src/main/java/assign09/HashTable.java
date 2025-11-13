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

    // TODO: doesnt check the next buckets using probing
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
        }

    }

    // TODO: skip deleted entries
    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < array.length; i++) {
            if (((MapEntry<K, V>) array[i]).getValue().equals(value))
                return true;
        }
        return false;
    }

    @Override
    public List<MapEntry<K, V>> entries() {
        List<MapEntry<K, V>> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (!deleted[i]) {
                list.add((MapEntry<K, V>) array[i]);
            }
        }
        return list;
    }

    // TODO:bail when hitting a empty array slot and handle tombstones(deleted ==
    // true)
    @Override
    public V get(Object key) {
        int i = 0;
        int startBucket = homeIndex(key);
        int j = quadraticProbe(startBucket, i);
        K maybeK = ((MapEntry<K, V>) array[j]).getKey();

        while (!maybeK.equals(key)) {
            i++;
            j = quadraticProbe(startBucket, i);
            maybeK = ((MapEntry<K, V>) array[j]).getKey();
        }
        if (!deleted[j]) {
            return ((MapEntry<K, V>) array[j]).getValue();
        } else
            return null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // TODO:resized if threshold hits loadThreshold fix tombstone handling
    @Override
    public V put(K key, V value) {
        MapEntry<K, V> entry = new MapEntry<K, V>(key, value);
        V returnedValue = null;
        int steps = 0;
        int startIndex = homeIndex(key);
        int index = homeIndex(key);

        while (array[index] != null && !deleted[index]) {
            MapEntry<K, V> checkingEntry = (MapEntry<K, V>) array[index];
            if (checkingEntry != null && checkingEntry.getKey().equals(key)) {
                returnedValue = checkingEntry.getValue();
                break;
            }

            steps++;
            index = quadraticProbe(startIndex, steps);
        }

        array[index] = entry;
        deleted[index] = false;
        if (returnedValue == null)
            size++;

        return returnedValue;
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
