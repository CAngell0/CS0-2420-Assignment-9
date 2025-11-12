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
        int startBucket = key.hashCode()%capacity;
        int j = quadraticProbe(startBucket, i);
        K maybeK = ((MapEntry<K,V>) array[j]).getKey();
        
        while(!maybeK.equals(key)){
            i++;
            j = quadraticProbe(startBucket, i);
            maybeK = ((MapEntry<K,V>) array[j]).getKey();
        }
        if(!deleted[i]){return ((MapEntry<K,V>) array[j]).getValue();}
        else return null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V put(K key, V value) {
        MapEntry<K, V> entry = new MapEntry<K, V>(key, value);
        V returnedValue = null;
        int steps = 0;
        int index = quadraticProbe(key.hashCode(), steps);

        while (array[index] != null && !deleted[index]){
            MapEntry<K, V> checkingEntry = (MapEntry<K, V>) array[index];
            if (checkingEntry != null && checkingEntry.getKey().equals(key)){
                returnedValue = checkingEntry.getValue();
                break;
            }

            steps++;
            index = quadraticProbe(key.hashCode(), steps);
        }

        array[index] = entry;
        deleted[index] = false;
        if (returnedValue == null) size++;

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

    private double calculateLoadFactor(){
        return size / capacity;
    }

    private void resizeBackingArray(){
        int newCapacity = calculateNextCapacity();
        Object[] newArray = new Object[newCapacity];
        Object[] currentArray = array;

        array = newArray;
        for (Object entryObject : currentArray){
            MapEntry<K, V> entry = (MapEntry<K, V>) entryObject;
            put(entry.getKey(), entry.getValue());
        }
    }

    private int calculateNextCapacity(){
        int number = size * 2;
        boolean found = false;

        while (!found){
            number++;
            if (isPrime(number)) found = true;
        }

        return number;
    }

    private boolean isPrime(int num){
        if (num % 2 == 0 || num % 3 == 0) return false;

        for (int i = 5; i * i <= num; i = i + 6) 
            if (num % i == 0 || num % (i + 2) == 0) return false; 
        
        return true; 
    }
}
