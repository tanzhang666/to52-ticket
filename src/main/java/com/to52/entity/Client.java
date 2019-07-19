package com.to52.entity;
import java.io.Serializable;//
/**
 * @author tzhang
 * @date 2019/5/21 {16:31}
 */
public class Client implements Serializable {
    private Integer cid;//l'attribut id
    private String firstName;//l'attribut prenom
    private String lastName;//l'attribut nom
    private String email;//l'attribut mail
    private String password;//l'attribut mot de passe
    private String address;//l'attribut adresse
    private String phone;//l'attribut numero de telephone
    private Integer sex;//l'attribut sexe
    private boolean isShowLogin;
    private boolean isShowStore;
    private boolean isShowUser;
    private String act;
    private Integer privilege;//l'attribut pour limiter les droits des clients
    public Client() {}//l'constructeur de classe client
    public String getFirstName() {
        return firstName;
    }//la function getter d'attribut firstName
    public void setFirstName(String firstName) { this.firstName = firstName; }//la function setter d'attribut firstName
    public String getAct() { return act; }//la function getter d'attribut act
    public void setAct(String act) { this.act = act; }//la function setter d'attribut act
    public String getLastName() {
        return lastName;
    }//la function getter d'attribut lastName
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }//la function setter d'attribut lastName
    public Integer getCid() {
        return cid;
    }//la function getter d'attribut cid
    public void setCid(Integer cid) {
        this.cid = cid;
    }//la function setter d'attribut cid
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Integer getSex() {
        return sex;
    }
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Integer getPrivilege() {
        return privilege;
    }
    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }
    public boolean isShowLogin() { return isShowLogin; }
    public void setShowLogin(boolean showLogin) { isShowLogin = showLogin; }
    public boolean isShowStore() { return isShowStore; }
    public void setShowStore(boolean showStore) { isShowStore = showStore; }
    public boolean isShowUser() { return isShowUser; }
    public void setShowUser(boolean showUser) { isShowUser = showUser; }
    @Override
    public String toString() {
        return "Client{" +
                "cid=" + cid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", privilege=" + privilege +
                '}';
    }

}
