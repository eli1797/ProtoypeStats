package eli.protoypestats.dummy;

/**
 * POJO that represents a set (Volleyball)
 * Not to be confused with Java Sets
 * Created by Elijah Bailey on 7/31/2018.
 */ 

public class Set {

    //set info
    private String teamOne, teamTwo;
    private int teamOneScore, teamTwoScore;

    //stats
    private int receiveWin, receiveLoss;
    private int serveWin, serveLoss;


    @Override
    public String toString() {
        return "Set between " + teamOne + ": " + teamOneScore + " and " + teamTwo + ": " + teamTwoScore;
    }

    // Getters and Setters
    public int getTeamOneScore() {
        return teamOneScore;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

    public int getServeWin() {
        return serveWin;
    }

    public void setServeWin(int serveWin) {
        this.serveWin = serveWin;
    }

    public int getServeLoss() {
        return serveLoss;
    }

    public void setServeLoss(int serveLoss) {
        this.serveLoss = serveLoss;
    }

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
