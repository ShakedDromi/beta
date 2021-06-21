package com.example.beta;

public class propose {

    private String bUid,bname;
    private int bPrice,bage;
    private boolean picked;

    public propose(){}

    /**
     * User class builder.
     * this function gets all of the variables that are required in order to assemble a user
     * @param bUid
     * @param bname
     * @param bPrice
     * @param bage
     */
    public propose(String bUid, String bname, int bPrice, int bage, boolean picked){
        this.bUid=bUid;
        this.bname=bname;
        this.bPrice=bPrice;
        this.bage=bage;
        this.picked=picked;
    }

    public String getbUid(){return bUid;}
    public void setbUid(String bUid){this.bUid=bUid;}

    public String getBname(){return bname;}
    public void setBname(String bname){this.bname=bname;}

    public int getbPrice(){return bPrice;}
    public void setbPrice(int bPrice){this.bPrice=bPrice;}

    public int getBage(){return bage;}
    public void setBage(int bage){this.bage=bage;}

    public boolean getPicked(){return picked;}
    public void setPicked(boolean picked){this.picked=picked;}
}
