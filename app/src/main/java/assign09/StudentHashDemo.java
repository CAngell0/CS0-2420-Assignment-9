package assign09;

/**
 * Demonstratation of how to use a hash table to store key-value pairs.
 *
 * @author CS 2420 course staff
 * @version 2025-11-06
 */
public class StudentHashDemo {

  public static void main(String[] args) {
    StudentBadHash alan = new StudentBadHash(1019999, "Alan", "Turing");
    StudentBadHash ada = new StudentBadHash(1004203, "Ada", "Lovelace");
    StudentBadHash edsger = new StudentBadHash(1010661, "Edsger", "Dijkstra");
    StudentBadHash grace = new StudentBadHash(1019941, "Grace", "Hopper");

    HashTable<StudentBadHash, Double> gpaTable = new HashTable<StudentBadHash, Double>();
    gpaTable.put(alan, 3.2);
    gpaTable.put(ada, 3.5);
    gpaTable.put(edsger, 3.8);
    gpaTable.put(grace, 4.0);

    for (MapEntry<StudentBadHash, Double> e : gpaTable.entries()) {
      System.out.println("Student " + e.getKey() + " has GPA " + e.getValue() + ".");
    }
  }
}
