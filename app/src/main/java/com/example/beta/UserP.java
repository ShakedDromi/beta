package com.example.beta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * a user class
 * contains all of the data required for a user.
 */
public class UserP implements Serializable {
    private String pname, ppass, pmail, paddress, pdesc, pimage, puid;
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
     * @param pname
     * @param Ppass
     * @param Pmail
     * @param Paddress
     * @param Pdesc
     * @param Pimage
     * @param Puid
     */
    public UserP(String name, String ppass, String pmail, String paddress, String pdesc, String pimage, String puid){
        this.pname=pname;
        this.ppass =ppass;
        this.pmail =pmail;
        this.paddress =paddress;
        this.pdesc =pdesc;
        this.pimage =pimage;
        this.puid =puid;
    }


    public String getpname(){return pname;}
    public void setpname(String Pname){this.pname =Pname;}

    public String getppass(){return ppass;}
    public void setppass(String phNumber){this.ppass = ppass;}

    public String getpmail(){return pmail;}
    public void setpmail(String Puid){this.pmail = pmail;}

    public String getpaddress() {
        return paddress;
    }
    public void setpaddress(String Paddress) {
        this.paddress = Paddress;
    }

    public String getpdesc() {
        return pdesc;
    }
    public void setpdesc(String desc) { this.pdesc = pdesc; }

    public String getpimage(){return pimage;}
    public void setpimage(String image){this.pimage = pimage;}

    public String getpuid(){return puid;}
    public void setPuid(String image){this.puid = puid;}

    public ArrayList<String> getKidsBday() {
        return kidsBday;
    }

    public void setKidsBday(ArrayList<String> kidsBday) {
        this.kidsBday = kidsBday;
    }
}

