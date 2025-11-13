package assign09;

import java.util.ArrayList;
import java.util.List;
/**
 * A hashtable data structure that implements the map interface.
 * 
 * @author Joshua Varughese and Carson Angell
 * @version 11/13/25
 */
public class HashTable<K, V> implements Map<K, V> {
    private Object[] array = new Object[11];
    private boolean[] deleted = new boolean[11];
    private int size = 0;
    private int capacity = 11;
    private double loadThreshold = 0.5;

    /**
     * @inheritDoc
     */
    @Override
    public void clear() {
        //creates new arrays of capacity length and resets size to 0
        array = new Object[capacity];
        deleted = new boolean[capacity];
        size = 0;
    }
    /**
     * @inheritDoc
     */
    @Override
    public boolean containsKey(K key) {
        int i = 0;

        //if we increments capacity times then its not in the list
        while (i < capacity) {
            //j is the quadratic probe index
            int j = calcIndex(key, i);
            Object curr = array[j];

            // hit empty slot
            if (curr == null && !deleted[j]) {
                return false;
            }

            //hit a not emtpy index that hasnt been deleted
            if (curr != null && !deleted[j]) {
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean containsValue(V value) {
        //loop over the array
        for (int i = 0; i < array.length; i++) {
            //if the value isnt deleted and isnt null
            if (!deleted[i] && array[i] != null) {
                if (toEntry(array[i]).getValue().equals(value))
                    return true;
            }
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<MapEntry<K, V>> entries() {
        List<MapEntry<K, V>> list = new ArrayList<>();
        //loop over the array and add non deleted non null values to the list
        for (int i = 0; i < array.length; i++) {
            if (!deleted[i] && array[i] != null) {
                list.add(toEntry(array[i]));
            }
        }
        return list;
    }

    /**
     * @inheritDoc
     */
    @Override
    public V get(K key) {
        int i = 0;

        while (i < capacity) {
            //quadratic probe index
            int j = calcIndex(key, i);
            Object curr = array[j];

            // key not in table
            if (curr == null && !deleted[j]) {
                return null;
            }

            //non null non deleted
            if (curr != null && !deleted[j]) {
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
            i++;
        }
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public V put(K key, V value) {
        //resize if load factor is at threshold
        if (calculateLoadFactor() >= loadThreshold) {
            resizeBackingArray();
        }

        MapEntry<K, V> toBePut = new MapEntry<>(key, value);

        //stores first tombstone(deleted is true)
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

    /**
     * @inheritDoc
     */
    @Override
    public V remove(K key) {
        int i = 0;

        while (i < capacity) {
            //quadratic prob index
            int j = calcIndex(key, i);
            Object curr = array[j];

            // empty and not tombstone
            if (curr == null && !deleted[j]) {
                return null;
            }

            //non null and non deleted
            if (curr != null && !deleted[j]) {
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    deleted[j] = true;
                    size--;
                    return entry.getValue();
                }
            }

            // if tombstone keep probing
            i++;
        }

        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int size() {
        return size;
    }

    /****
     * Helper method to remove warnings
     * @param entry
     * @return
     */
    @SuppressWarnings("unchecked")
    private MapEntry<K, V> toEntry(Object entry) {
        return (MapEntry<K, V>) entry;
    }

    /**
     * Calculates current load factor
     * @return
     */
    private double calculateLoadFactor() {
        return (double) size / capacity;
    }

    /**
     * calculates the quadratic probe index based on step count
     * @param key
     * @param step
     * @return
     */
    private int calcIndex(Object key, int step) {
        int start = Math.abs(key.hashCode()) % capacity;
        return (start + (step * step)) % capacity;
    }

    /***
     * Finds the next prime number
     * @return
     */
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
     * Checks if a number is prime
     * Doesnt need to work for numbers smaller than 11 
     * @param num
     * @return
     */
    private boolean isPrime(int num) {
        if (num % 2 == 0 || num % 3 == 0)
            return false;

        for (int i = 5; i * i <= num; i = i + 6)
            if (num % i == 0 || num % (i + 2) == 0)
                return false;

        return true;
    }

     /**
      * Resizes the array to the next prime number
      */
    private void resizeBackingArray() {
        int newCapacity = calculateNextCapacity();
        capacity = newCapacity;

        Object[] oldArr = array;
        boolean[] oldDeleted = deleted;
        
        array = new Object[capacity];
        deleted = new boolean[capacity];
        // put corrects the size;
        size = 0;

        for (int i = 0; i < oldArr.length; i++) {
            if (oldArr[i] != null && !oldDeleted[i]) {
                MapEntry<K, V> entry = toEntry(oldArr[i]);
                put(entry.getKey(), entry.getValue());
            }
        }

    }
}
