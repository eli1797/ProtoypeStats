package eli.protoypestats;

/**
 * Created by Elijah Bailey on 6/5/2018.
 */

public class Player {

    //could also do first name and last name as different variables
    public String firstName, lastName;

    //number on their jersey
    public int number;

<<<<<<< HEAD
    public Player(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public Player(String name) {
        this(name, 0);
=======
    //constructors
    public Player(String firstName, String lastName, int number) {
        this(firstName, lastName);
        this.number = number;
    }

    public Player(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Player " + firstName + " " + lastName + " with number " + number;
>>>>>>> 565f3a52de511b2cf0eb837fe78f53bbaa8c53e3
    }
}
