package assign09;

/**
 * Representation of an entry in a map; i.e., a key-value pair.
 *
 * @author CS 2420 course staff
 * @version 2025-11-06
 *
 * @param <K> - type variable for key
 * @param <V> - type variable for value
 */
public class MapEntry<K, V> {

  private K key;
  private V value;

  /**
   * Create a new MapEntry with the given key and value.
   *
   * @param key - the key of the new map entry
   * @param value - the value of the new map entry
   */
  public MapEntry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Retrieve the key of this map entry.
   *
   * @return the key
   */
  public K getKey() {
    return this.key;
  }

  /**
   * Retrieve the value of this map entry.
   *
   * @return the value
   */
  public V getValue() {
    return this.value;
  }

  /**
   * Update the value of this map entry.
   *
   * @param value - the new value
   */
  public void setValue(V value) {
    this.value = value;
  }

  /**
   * Determine whether this map entry is equal to another map entry.
   *
   * @param other - the object being compared to this map entry
   * @return true if the key and value are equal; false otherwise
   */
  public boolean equals(Object other) {
    if (!(other instanceof MapEntry<?, ?>)) return false;

    MapEntry<?, ?> rhs = (MapEntry<?, ?>) other;

    return key.equals(rhs.key) && value.equals(rhs.value);
  }

  /**
   * String representation of this map entry.
   *
   * @return a textual representation of the form `(key, value)`
   */
  public String toString() {
    return "(" + key + ", " + value + ")";
  }
}
