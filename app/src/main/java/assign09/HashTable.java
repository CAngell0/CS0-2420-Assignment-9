package assign09;

import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> implements Map<K, V> {
    private Object[] array = new Object[11];
    private boolean[] deleted = new boolean[11];
    private int size = 0;
    private int capacity = 11;

    private int getIndexFromKey(K key){
        int hashCode = key.hashCode();
        return Math.abs(hashCode) % capacity;
    }

    @Override
    public void clear() {
        array = new Object[capacity];
        deleted = new boolean[capacity];
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return !deleted[key.hashCode() % capacity];
    }

    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < array.length; i++){
            if (((MapEntry<K, V>) array[i]).getValue().equals(value)) return true;
        }
        return false;
    }

    @Override
    public List<MapEntry<K, V>> entries() {
        List<MapEntry<K, V>> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++){
            if (!deleted[i]) {
                list.add((MapEntry<K, V>) array[i]);
            }
        }
        return list;
    }

    @Override
    public V get(Object key) {
        int i = 0;
        int startBucket = key.hashCode();
        int j = quadraticProbe(startBucket, i);
        K maybeK = ((MapEntry<K,V>) array[j]).getKey();
        
        while(!maybeK.equals(key)){
            i++;
            j = quadraticProbe(startBucket, i);
            maybeK = ((MapEntry<K,V>) array[j]).getKey();
        }
        return ((MapEntry<K,V>) array[j]).getValue();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V put(K key, V value) {
        MapEntry<K, V> entry = new MapEntry<K, V>(key, value);
        V returnedValue = null;
        int index = getIndexFromKey(key);
        int adjustmentCount = 0;

        while (array[index] != null && !deleted[index]){
            MapEntry<K, V> checkingEntry = (MapEntry<K, V>) array[index];
            if (checkingEntry != null && checkingEntry.getKey().equals(key)){
                returnedValue = checkingEntry.getValue();
                break;
            }

            adjustmentCount++;
            index = (getIndexFromKey(key) + (int) Math.pow(adjustmentCount, 2)) % capacity;
        }

        array[index] = entry;
        deleted[index] = false;
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

    private int quadraticProbe(int start, int i){
        return (start + i * i) % capacity;
    }

}
