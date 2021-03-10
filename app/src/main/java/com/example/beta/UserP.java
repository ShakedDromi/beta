package com.example.beta;

import java.util.ArrayList;

/**
 * a user class
 * contains all of the data required for a user.
 */
public class UserP {
    private String Pname, Ppass, Pmail, Paddress, Pdesc,Pimage, Puid;
    private ArrayList<String> kidsBday;
    /**
     * an empty builder.
     * this function is not used, but is required in order to use Firebase.
     */
    public UserP(){

    }

    /**
     * User class builder.
     * this function gets all of the variables that are required in order to assemble a user
     * @param Pname
     * @param Ppass
     * @param Pmail
     * @param Paddress
     * @param Pdesc
     * @param Pimage
     * @param Puid
     */
    public UserP(String Pname, String Ppass, String Pmail, String Paddress, String Pdesc, String Pimage, String Puid){
        this.Pname=Pname;
        this.Ppass=Ppass;
        this.Pmail=Pmail;
        this.Paddress=Paddress;
        this.Pdesc=Pdesc;
        this.Pimage=Pimage;
        this.Puid=Puid;
    }


    public String getPname(){return Pname;}
    public void setPname(String Pname){this.Pname=Pname;}

    public String getPpass(){return Ppass;}
    public void setPpass(String phNumber){this.Ppass=Ppass;}

    public String getPmail(){return Pmail;}
    public void setPmail(String Puid){this.Pmail=Pmail;}

    public String getPaddress() {
        return Paddress;
    }
    public void setPaddress(String Paddress) {
        this.Paddress = Paddress;
    }

    public String getPdesc() {
        return Pdesc;
    }
    public void setPdesc(String desc) { this.Pdesc = Pdesc; }

    public String getPimage(){return Pimage;}
    public void setPimage(String image){this.Pimage=Pimage;}

    public String getPuid(){return Puid;}
    public void setPuid(String image){this.Puid=Puid;}

    public ArrayList<String> getKidsBday() {
        return kidsBday;
    }

    public void setKidsBday(ArrayList<String> kidsBday) {
        this.kidsBday = kidsBday;
    }
}

