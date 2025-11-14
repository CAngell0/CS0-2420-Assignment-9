package assign09;

import java.text.DecimalFormat;

/**
 * Representation for a University of Utah student.
 *
 * @implNote The hashCode method is overridden with a valid hash function for
 *           this student, but one that does a medium job of distributing
 *           students in a hash
 *           table.
 *
 * @author CS 2420 course staff and Joshua Varughese and Carson Angell
 * @version 11/12/25
 */
public class StudentMediumHash {

  private int uid;
  private String firstName;
  private String lastName;

  /**
   * Create a new student with the given uid, firstName, and lastName.
   *
   * @param uid
   * @param firstName
   * @param lastName
   */
  public StudentMediumHash(int uid, String firstName, String lastName) {
    this.uid = uid;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * @return the UID of this student
   */
  public int getUid() {
    return this.uid;
  }

  /**
   * @return the first name of this student
   */

  public String getFirstName() {
    return this.firstName;
  }

  /**
   * @return the last name of this student
   */
  public String getLastName() {
    return this.lastName;
  }

  /**
   * Determine whether this student is equal to a given object.
   *
   * @return true if other is a student with the same UID, first name, and last
   *         name; otherwise false
   */
  public boolean equals(Object other) {
    // change to StudentMediumHash and StudentGoodHash for two new classes
    if (!(other instanceof StudentMediumHash))
      return false;

    StudentMediumHash rhs = (StudentMediumHash) other;

    return (this.uid == rhs.uid &&
        this.firstName.equals(rhs.firstName) &&
        this.lastName.equals(rhs.lastName));
  }

  /**
   * String representation of this student.
   *
   * @return a textual representation of the form "First Last (u0000000)"
   */
  public String toString() {
    DecimalFormat formatter = new DecimalFormat("0000000");
    return firstName + " " + lastName + " (u" + formatter.format(uid) + ")";
  }

  /**
   * Hashes students first and last name
   */
  public int hashCode() {
    String fullName = this.getFirstName() + this.getLastName();
    return fullName.hashCode();
  }
}
