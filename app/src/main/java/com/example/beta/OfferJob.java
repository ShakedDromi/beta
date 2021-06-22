package com.example.beta;


import java.util.ArrayList;

/**
 * a user's job offer class
 * contains all of the data required for a job offer.
 */
public class OfferJob {
    private String date, time, description, uidJP, makom, newId, mail;
    private int knum;
    //private propose proposeB;

    private ArrayList<propose> proposeB;


    /**
     * an empty builder.
     * this function is not used, but is required in order to use Firebase.
     */
    public OfferJob(){}

    /**
     * User class builder.
     * this function gets all of the variables that are required in order to assemble a user
     * @param date
     * @param time
     * @param description
     * @param uidJP
     * @param makom
     * @param newId
     * @param mail
     * @param knum
     * @param proposeB
     */
    public OfferJob(String date, String time, String description, String uidJP, String makom, String newId, String mail, int knum, ArrayList<propose> proposeB){
        this.date=date;
        this.time=time;
        this.description=description;
        this.uidJP=uidJP;
        this.makom=makom;
        this.newId=newId;
        this.mail=mail;
        this.knum=knum;
        this.proposeB=proposeB;
    }



    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getTime(){return time;}
    public void setTime(String time){this.time=time;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public String getUidJP(){return uidJP;}
    public void setUidJP(String uidJP){this.uidJP=uidJP;}

    public String getMakom(){return makom;}
    public void setMakom(String makom){this.makom=makom;}

    public String getNewId(){return newId;}
    public void setNewId(String newId){this.newId=newId;}

    public String getMail(){return mail;}
    public void setMail(String mail){this.mail=mail;}

    public int getKnum(){return knum;}
    public void setKnum(int knum){this.knum=knum;}

    public ArrayList<propose> getProposeB(){return proposeB;}
    public void setProposeB(ArrayList<propose> proposeB){this.proposeB=proposeB;}
}
