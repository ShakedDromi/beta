package com.example.beta;

public class propose {

    private String bUid;
    private int bPrice;

    public propose(){

    }

    /**
     * User class builder.
     * this function gets all of the variables that are required in order to assemble a user
     * @param bUid
     * @param bPrice
     */
    public propose(String bUid, int bPrice){
        this.bUid=bUid;
        this.bPrice=bPrice;
    }

    public String getbUid(){return bUid;}
    public void setbUid(String bUid){this.bUid=bUid;}

    public int getbPrice(){return bPrice;}
    public void setbPrice(int bPrice){this.bPrice=bPrice;}
}
