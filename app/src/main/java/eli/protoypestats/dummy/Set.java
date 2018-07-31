package eli.protoypestats.dummy;

/**
 * Created by Elijah Bailey on 7/31/2018.
 */

public class Set {

    private String teamOne, teamTwo;

    private int receiveWin, receiveLoss;

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public int getReceiveWin() {
        return receiveWin;
    }

    public void setReceiveWin(int receiveWin) {
        this.receiveWin = receiveWin;
    }

    public void plusReceiveWin() {
        this.receiveWin = receiveWin + 1;
    }

    public int getReceiveLoss() {
        return receiveLoss;
    }

    public void setReceiveLoss(int receiveLoss) {
        this.receiveLoss = receiveLoss;
    }

    public void plusReceiveLoss() {
        this.receiveLoss = receiveLoss + 1;
    }
}
