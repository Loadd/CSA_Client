package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private String username;
    private String password;
    private byte[] profilePicture;
    private String accountType;
    private Boolean ban;
    private Date timeout;
    private Float money;

    public Account() {
        this.username = "";
        this.password = "";
        this.profilePicture = null;
        this.accountType = "";
        this.ban = false;
        this.timeout = null;
        this.money = (float) 0;
    }

    //Default settings when registering
    public Account(String username, String password) {
        ByteArrayOutputStream bos = null;
        File file = new File("src/assets/Default_pfp.jpg");
        try{
            BufferedImage bImage = ImageIO.read(file);
            bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", bos);
            bos.close();
        
        } catch (IOException e){
            e.printStackTrace();
        }

        this.username = username;
        this.password = password;
        this.profilePicture = bos.toByteArray();
        this.accountType = "User";
        this.ban = false;
        this.timeout = null;
        this.money = 100F;
    }

    public Account(String username, String password, byte[] profilePicture, String accountType, Boolean ban, Date timeout, Float money){
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.accountType = accountType;
        this.ban = ban;
        this.timeout = timeout;
        this.money = money;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public byte[] getProfilePicture(){
        return this.profilePicture;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public Boolean getBan() {return ban;}

    public Date getTimeout() {
        return this.timeout;
    }

    public Float getMoney() {
        return this.money;
    }
}
