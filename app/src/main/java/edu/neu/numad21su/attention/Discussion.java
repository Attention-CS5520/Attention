package edu.neu.numad21su.attention;

public class Discussion {

    public String username;
    public String score;
    public String senders;
    public String scoreTimes;


    public Discussion() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Discussion(String username, String score, String senders) {
        this.username = username;
        this.score = score;

        this.senders = senders;
        this.scoreTimes = "";
    }

}

