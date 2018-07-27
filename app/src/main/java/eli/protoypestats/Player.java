package eli.protoypestats;

/**
 * Created by Elijah Bailey on 6/5/2018.
 */

public class Player {

    //could also do first name and last name as different variables
    public String name;

    //number on their jersey
    public int number;

    public Player(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public Player(String name) {
        this(name, 0);
    }
}
