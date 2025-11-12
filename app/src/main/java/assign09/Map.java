package assign09;

import java.util.List;

/**
 * Interface represention of a map of keys to values. Duplicate keys
 * are not permitted and each has an associated value.
 *
 * @implNote All time-complexity expectations are for a quadratic-probing hash table.
 *
 * @author CS 2420 course staff
 * @version 2025-11-06
 *
 * @param <K> - type variable for keys
 * @param <V> - type variable for values
 */
public interface Map<K, V> {
  /**
   * Remove all entries from this map.
   *
   * @implSpec Expected time-complexity: O(1)
   */
  public void clear();

  /**
   * Determine whether this map contains the given key.
   *
   * @implSpec Expected time-complexity: O(1)
   *
   * @param key - the key being searched for
   * @return true if this map contains the key; false otherwise
   */
  public boolean containsKey(K key);

  /**
   * Determine whether this map contains the given value.
   *
   * @implSpec Expected time-complexity: O(table capacity)
   *
   * @param value - the value being searched for
   * @return true if this map contains one or more keys to the specified value,
   *         false otherwise
   */
  public boolean containsValue(V value);

  /**
   * Get the entries contained in this map as a list.
   *
   * @implNote The ordering of map entries in the list is insignificant.
   * @implSpec Expected time-complexity: O(table capacity)
   *
   * @return a List object containing all entries in this map
   */
  public List<MapEntry<K, V>> entries();

  /**
   * Retrieve the value associated to a given key.
   *
   * @implSpec Expected time-complexity: O(1)
   *
   * @param key - the key being searched for
   * @return the associated value if key is found; otherwise null
   */
  public V get(K key);

  /**
   * Determine whether this map contains any entries.
   *
   * @implSpec Expected time-complexity: O(1)
   *
   * @return true if this map contains no entries; false otherwise
   */
  public boolean isEmpty();

  /**
   * Associate the given value with the given key in this map.
   *
   * @implNote If the key already exists, the associated value is updated;
   *           otherwise, a new entry is added to the map.
   *
   * @implSpec Expected time-complexity: O(1)
   *
   * @param key - the key for the map entry
   * @param value - the value for the map entry
   * @return the previous associated value if key is found; otherwise null
   */
  public V put(K key, V value);

  /**
   * Remove the entry for a key from this map if it is present.
   *
   * @implSpec Expected time-complexity: O(1)
   *
   * @param key - the key of the entry to be removed
   * @return the associated value if the key is found; otherwise null
   */
  public V remove(K key);

  /**
   * Determine the number of entries in this map.
   *
   * @implSpec Expected time-complexity: O(1)
   *
   * @return the number of entries contained in this map
   */
  public int size();
}
