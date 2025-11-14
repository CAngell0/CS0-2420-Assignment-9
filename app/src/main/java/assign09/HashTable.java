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

    /** {@inheritDoc} */
    @Override
    public void clear() {
        // Reinitializes the backing and deletion arrays, along with the size.
        array = new Object[capacity];
        deleted = new boolean[capacity];
        size = 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsKey(K key) {
        int i = 0;

        // if we increments capacity times then its not in the list
        while (i < capacity) {
            // Gets the index of where the key is suppoed to be
            int j = calcIndex(key, i);
            Object curr = array[j];

            // If the loop hits an completely empty spot, the key was not found.
            if (curr == null && !deleted[j]) {
                return false;
            }

            // If the current item is not empty or deleted...
            if (curr != null && !deleted[j]) {
                // If the keys are equal, return true
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    return true;
                }
            }
            i++;
        }

        // If no key was found, return false
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsValue(V value) {
        // Iterates through the backing array...
        for (int i = 0; i < array.length; i++) {
            // If the current item is not deleted...
            if (!deleted[i] && array[i] != null) {
                // Return true of the values match
                if (toEntry(array[i]).getValue().equals(value))
                    return true;
            }
        }

        // If no value was found, return false.
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public List<MapEntry<K, V>> entries() {
        // Creates an empty list to store the entries
        List<MapEntry<K, V>> list = new ArrayList<>();

        // Iterates through the backing array...
        for (int i = 0; i < array.length; i++) {
            // If the current item is not deleted, add it to the list of entries
            if (!deleted[i] && array[i] != null) {
                list.add(toEntry(array[i]));
            }
        }
        return list;
    }

    /** {@inheritDoc} */
    @Override
    public V get(K key) {
        int i = 0;

        while (i < capacity) {
            // Gets the item at the calculated index based on quadratic probing and hashing
            int j = calcIndex(key, i);
            Object curr = array[j];

            // If the key was not found
            if (curr == null && !deleted[j]) {
                return null;
            }

            // If the current item is not deleted or empty...
            if (curr != null && !deleted[j]) {
                // If the keys match, return the value
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
            i++;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** {@inheritDoc} */
    @Override
    public V put(K key, V value) {
        // If the load factor is too big, resize the backing arrays
        if (calculateLoadFactor() >= loadThreshold) {
            resizeBackingArray();
        }

        // Create a new map entry from the key and value.
        MapEntry<K, V> toBePut = new MapEntry<>(key, value);

        // stores first tombstone(deleted is true)
        int firstTombstone = -1;
        int i = 0;

        while (i < capacity) {
            // Calculates the next index to check
            int j = calcIndex(key, i);
            Object curr = array[j];

            // If there is an empty spot at the current item
            if (curr == null) {
                // Insert at j or at first tombstone found
                int insertIndex = (firstTombstone != -1) ? firstTombstone : j;

                array[insertIndex] = toBePut;
                deleted[insertIndex] = false;
                size++;
                return null;
            }

            // If the current item is not empty...
            if (!deleted[j]) {
                // If the keys are equal, overwrite it and return the value. If not, keep
                // probing
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    V toReturn = entry.getValue();
                    array[j] = toBePut;
                    return toReturn;
                }

                // If the current item was soft deleted
            } else {
                // Remember the index of the first tombstone, if there hasn't been one already
                if (firstTombstone == -1) {
                    firstTombstone = j;
                }
            }
            i++;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public V remove(K key) {
        int i = 0;

        while (i < capacity) {
            // Calculate the index to test for the key
            int j = calcIndex(key, i);
            Object curr = array[j];

            // If the current item is empty but not a tombstone, end the search
            if (curr == null && !deleted[j]) {
                return null;
            }

            // If the item is not empty and not a tombstone...
            if (curr != null && !deleted[j]) {
                // If the keys are equal, soft delete the item and return the value
                MapEntry<K, V> entry = toEntry(curr);
                if (entry.getKey().equals(key)) {
                    deleted[j] = true;
                    size--;
                    return entry.getValue();
                }
            }

            // If the current item is a tombstone, continue to the next item
            i++;
        }

        // If not key was found
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return size;
    }

    /**
     * A helper method that casts an Object from the backing array to a MapEntry.
     * 
     * @param entry The object to cast
     * @return
     */
    @SuppressWarnings("unchecked")
    private MapEntry<K, V> toEntry(Object entry) {
        return (MapEntry<K, V>) entry;
    }

    /**
     * A helper method that calculates the load factor of the data structure with
     * its durrent data.
     * 
     * @return The load factor: size / capacity
     */
    private double calculateLoadFactor() {
        return (double) size / capacity;
    }

    /**
     * A helper method that takes a key and calculates a corresponding index for the
     * backing array.
     * 
     * It first hashes it with the built in hashCode() method. Then it modulos by
     * the backing array capacity.
     * This method also implements quadratic probing. By putting in a step, it will
     * calculate it to the index at that step
     * using this paradigm.
     * 
     * @param key  The key to create the index from
     * @param step How many steps to take along the quadratic probing. Put 0 for the
     *             starting index.
     * @return
     */
    private int calcIndex(K key, int step) {
        int start = Math.abs(key.hashCode()) % capacity;
        return (start + (step * step)) % capacity;
    }

    /**
     * A helper method that calculates what the next capacity of the backing array
     * should be.
     * This method is used in resizing to decide how big the array should be resized
     * to.
     * 
     * @return
     */
    private int calculateNextCapacity() {
        int number = capacity * 2;
        boolean found = false;

        // Keep testing capacity numbers until you find one that's prime
        while (!found) {
            number++;
            if (isPrime(number))
                found = true;
        }

        return number;
    }

    /**
     * A simple helper method that checks whether a inputted integer is a prime
     * number.
     * 
     * @param num The number to check.
     * @return Whether or not the number is prime.
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
     * A helper method that resizes the backing array. It will resize the capacity
     * to the next
     * prime number after it's doubled previous capacity.
     */
    private void resizeBackingArray() {
        // Calculates the next capacity
        int newCapacity = calculateNextCapacity();
        capacity = newCapacity;

        // Grabs references to the old backing data
        Object[] oldArr = array;
        boolean[] oldDeleted = deleted;

        // Resets the backing arrays and size
        clear();

        // Iterates through the old backing array...
        for (int i = 0; i < oldArr.length; i++) {
            // If the current item is not empty or deleted, rehash it to the new backing
            // array
            if (oldArr[i] != null && !oldDeleted[i]) {
                MapEntry<K, V> entry = toEntry(oldArr[i]);
                put(entry.getKey(), entry.getValue());
            }
        }
    }
}
