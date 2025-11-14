package assign09;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class HashTableTest {
    private HashTable<String, Integer> table;
    
    @BeforeEach
    public void setUp() {
        table = new HashTable<>();
    }

    // Tests for get()
    @Test
    public void testGetExistingKey() {
        table.put("key1", 42);
        assertEquals(42, table.get("key1"));
    }
    
    @Test
    public void testGetNonExistentKey() {
        assertNull(table.get("nonexistent"));
    }
    
    @Test
    public void testGetAfterUpdate() {
        table.put("key1", 100);
        table.put("key1", 200);
        assertEquals(200, table.get("key1"));
    }



    // Tests for containsValue()
    @Test
    public void testContainsValueExists() {
        table.put("key1", 100);
        assertTrue(table.containsValue(100));
    }
    
    @Test
    public void testContainsValueDoesNotExist() {
        assertFalse(table.containsValue(999));
    }
    


    // Tests for isEmpty() and size()
    @Test
    public void testIsEmptyOnNewTable() {
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }
    
    @Test
    public void testIsEmptyAfterAddingElements() {
        table.put("key1", 1);

        assertFalse(table.isEmpty());
        assertEquals(1, table.size());
    }
    
    @Test
    public void testSizeAfterMultipleInserts() {
        table.put("key1", 1);
        table.put("key2", 8);
        table.put("key3", 13);

        assertEquals(3, table.size());
    }
    


    // Tests for put()
    @Test
    public void testPutNewEntry() {
        assertNull(table.put("key1", 100));
        assertEquals(1, table.size());
        assertEquals(100, table.get("key1"));
    }
    
    @Test
    public void testPutUpdatesExistingKey() {
        table.put("key1", 100);
        Integer oldValue = table.put("key1", 200);
        
        assertEquals(100, oldValue);
        assertEquals(200, table.get("key1"));
        assertEquals(1, table.size());
    }
    
    @Test
    public void testPutMultipleEntries() {
        table.put("a", 10);
        table.put("b", 3);
        table.put("c", 28);

        assertEquals(10, table.get("a"));
        assertEquals(3, table.get("b"));
        assertEquals(28, table.get("c"));
    }
    
    @Test
    public void testPutTriggersResize() {
        // This adds enough elements to exceed the load factor threshold.
        for (int i = 0; i < 10; i++) 
            table.put("key" + i, i);

        assertEquals(10, table.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i, table.get("key" + i));
        }
    }

    @Test
    public void testLargeNumberOfEntries() {
        for (int i = 0; i < 100; i++) {
            table.put("key" + i, i);
        }
        
        assertEquals(100, table.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.get("key" + i));
        }
    }

    @Test
    public void testPutNullValue() {
        table.put("key1", null);

        assertEquals(1, table.size());
        assertNull(table.get("key1"));
        assertTrue(table.containsKey("key1"));
    }


    
    // Tests for containsKey()
    @Test
    public void testContainsKeyExists() {
        table.put("key1", 1);

        assertTrue(table.containsKey("key1"));
    }
    
    @Test
    public void testContainsKeyDoesNotExist() {
        assertFalse(table.containsKey("nonexistent"));
    }
    
    @Test
    public void testContainsKeyAfterRemoval() {
        table.put("key1", 1);
        table.remove("key1");

        assertFalse(table.containsKey("key1"));
    }
    
    @Test
    public void testContainsValueAfterUpdate() {
        table.put("key1", 100);
        table.put("key1", 200);

        assertFalse(table.containsValue(100));
        assertTrue(table.containsValue(200));
    }
    
    @Test
    public void testContainsValueAfterRemoval() {
        table.put("key1", 100);
        table.remove("key1");

        assertFalse(table.containsValue(100));
    }
    


    // Tests for remove()
    @Test
    public void testRemoveExistingKey() {
        table.put("key1", 42);

        assertEquals(42, table.remove("key1"));
        assertEquals(0, table.size());
        assertNull(table.get("key1"));
    }
    
    @Test
    public void testRemoveNonExistentKey() {
        assertNull(table.remove("nonexistent"));
        assertEquals(0, table.size());
    }
    
    @Test
    public void testRemoveMultipleKeys() {
        table.put("key1", 1);
        table.put("key2", 2);
        table.put("key3", 3);

        assertEquals(2, table.remove("key2"));
        assertEquals(2, table.size());
        assertNull(table.get("key2"));

        assertTrue(table.containsKey("key1"));
        assertTrue(table.containsKey("key3"));
    }

    @Test
    public void testUpdateAfterRemoval() {
        table.put("key1", 1);
        table.remove("key1");
        table.put("key1", 2);

        assertEquals(2, table.get("key1"));
        assertTrue(table.containsKey("key1"));
    }
    


    // Tests for clear()
    @Test
    public void testClear() {
        table.put("key1", 1);
        table.put("key2", 2);
        table.clear();

        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("key1"));
    }
    
    @Test
    public void testClearEmptyTable() {
        table.clear();

        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }
    
    @Test
    public void testPutAfterClear() {
        table.put("key1", 1);
        table.clear();
        table.put("key2", 2);

        assertEquals(1, table.size());
        assertEquals(2, table.get("key2"));
    }
    


    // Tests for entries()
    @Test
    public void testEntriesEmptyTable() {
        List<MapEntry<String, Integer>> entries = table.entries();
        assertTrue(entries.isEmpty());
    }
    
    @Test
    public void testEntriesWithElements() {
        table.put("key1", 1);
        table.put("key2", 2);
        table.put("key3", 3);

        List<MapEntry<String, Integer>> entries = table.entries();
        assertEquals(3, entries.size());
        
        // Check that all entries are present
        boolean found1 = false, found2 = false, found3 = false;
        for (MapEntry<String, Integer> entry : entries) {
            if (entry.getKey().equals("key1") && entry.getValue().equals(1)) found1 = true;
            if (entry.getKey().equals("key2") && entry.getValue().equals(2)) found2 = true;
            if (entry.getKey().equals("key3") && entry.getValue().equals(3)) found3 = true;
        }
        assertTrue(found1 && found2 && found3);
    }
    
    @Test
    public void testEntriesAfterRemoval() {
        table.put("key1", 1);
        table.put("key2", 2);
        table.remove("key1");

        List<MapEntry<String, Integer>> entries = table.entries();

        assertEquals(1, entries.size());
        assertEquals("key2", entries.get(0).getKey());
    }
    


    // Other tests
    @Test
    public void testAlternatingPutAndRemove() {
        table.put("key1", 1);
        table.remove("key1");
        table.put("key1", 2);

        assertEquals(2, table.get("key1"));
        assertEquals(1, table.size());
    }
}
