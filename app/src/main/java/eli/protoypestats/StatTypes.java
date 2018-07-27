package eli.protoypestats;

/**
 * Created by Elijah Bailey on 6/5/2018.
 */

public enum StatTypes {
    ERROR,
    KILL,
    BLOCK,
    PASS, //come up with an quick way to rate passes later and add it
    MISS_SERVE,
    GREAT_SERVE,
    COVER,
    HIT_ATTEMPT, //use in coordination will kills to determine efficiency
    OF_NOTE
}
