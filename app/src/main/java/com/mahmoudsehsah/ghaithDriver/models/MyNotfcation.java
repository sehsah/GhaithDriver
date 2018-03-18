package com.mahmoudsehsah.ghaithDriver.models;

/**
 * Created by mahmoud on 3/13/2018.
 */
/**
 * Created by mahmoud on 3/12/2018.
 */

public class MyNotfcation {

    private String id;
    private String id_user;
    private String id_order;
    private String text;
    private String photo_user;
    private String name_user;

    public String getId() {
        return id;
    }
    public void setId(String id) {this.id = id;}
    public String getid_user() {return id_user;}
    public void setid_user(String id_user) {this.id_user = id_user;}
    public String getid_order() {
        return id_order;
    }
    public void setid_order(String id_order) {this.id_order = id_order;}
    public String gettext() {
        return text;
    }
    public void settext(String text) {this.text = text;}
    public String getphoto_user() {
        return photo_user;
    }
    public void setphoto_user(String photo_user) {this.photo_user = photo_user;}
    public String getname_user() {
        return name_user;
    }
    public void setpname_user(String name_user) {this.name_user = name_user;}
}
