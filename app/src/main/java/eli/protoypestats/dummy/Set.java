package eli.protoypestats.dummy;

/**
 * POJO that represents a set (Volleyball)
 * Not to be confused with Java Sets
 * Created by Elijah Bailey on 7/31/2018.
 */ 

public class Set {

    //set info
    private String teamOne, teamTwo;
    private int homeTeamScore, awayTeamScore;

    //stats
    private int receiveWin, receiveLoss;
    private int serveWin, serveLoss;
    private int greatServe, netServe;


    @Override
    public String toString() {
        return "Set between " + teamOne + ": " + homeTeamScore + " and " + teamTwo + ": " + awayTeamScore;
    }


    public int getGreatServe() {
        return greatServe;
    }

    public void setGreatServe(int greatServe) {
        this.greatServe = greatServe;
    }

    public int getNetServe() {
        return netServe;
    }

    public void setNetServe(int netServe) {
        this.netServe = netServe;
    }
    

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
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
