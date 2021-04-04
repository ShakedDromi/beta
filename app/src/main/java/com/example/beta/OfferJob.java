package com.example.beta;

/**
 * a user's job offer class
 * contains all of the data required for a job offer.
 */
public class OfferJob {
    private String date, time, description, note, uidJP, uidJB;
    private int payment;

    /**
     * an empty builder.
     * this function is not used, but is required in order to use Firebase.
     */
    public OfferJob(){
    }

    /**
     * User class builder.
     * this function gets all of the variables that are required in order to assemble a user
     * @param date
     * @param time
     * @param description
     * @param note
     * @param uidJP
     * @param uidJB
     * @param payment
     */
    public OfferJob(String date, String time, String description, String note, String uidJP, String uidJB, int payment){
        this.date=date;
        this.time=time;
        this.description=description;
        this.note=note;
        this.uidJP=uidJP;
        this.uidJB=uidJB;
        this.payment=payment;
    }

    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getTime(){return time;}
    public void setTime(String time){this.time=time;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public String getNote(){return note;}
    public void setNote(String note){this.note=note;}

    public String getUidJP(){return uidJP;}
    public void setUidJP(String uidJP){this.uidJP=uidJP;}

    public String getUidJB(){return uidJB;}
    public void setUidJB(String uidJB){this.uidJB=uidJB;}

    public int getPayment(){return payment;}
    public void setPayment(int payment){this.payment=payment;}

}
