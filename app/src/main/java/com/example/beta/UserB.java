package com.example.beta;

/**
 * a user class
 * contains all of the data required for a user.
 */
public class UserB {
    private String name, pass, mail, address, description, birthDate, Buid;
    /**
     * an empty builder.
     * this function is not used, but is required in order to use Firebase.
     */
    public UserB(){

    }

    /**
     * User class builder.
     * this function gets all of the variables that are required in order to assemble a user
     * @param name
     * @param mail
     * @param pass
     * @param description
     * @param address
     * @param uid
     * @param birthDate
     */
    public UserB(String name, String mail, String birthDate, String address, String description, String uid, String pass){
        this.name=name;
        this.pass=pass;
        this.mail=mail;
        this.address=address;
        this.description=description;
        this.birthDate=birthDate;
        this.Buid=uid;
    }

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getPass(){return pass;}
    public void setPass(String pass){this.pass=pass;}

    public String getMail(){return mail;}
    public void setMail(String mail){this.mail=mail;}

    public String getAddress() { return address;}
    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getUid(){return Buid;}
    public void setUid(String uid){this.Buid=uid;}

}





