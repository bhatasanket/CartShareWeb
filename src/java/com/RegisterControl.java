/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author it354f701
 */
@ManagedBean
@SessionScoped
public class RegisterControl implements Serializable {

    private String userEmail;
    private String userpassword;
    private String userREpassword;
    private String userfirstname;
    private String userlastname;
    private String errorMSG;
    private String errorclass;

    private String regclass;
    private String regMSG;
    final private String URL = "http://10.112.4.10:8080/CartShareServ1/webresources/com.logininfo/";

    public String register() {
        if (!userpassword.equals(userREpassword)) {
            errorMSG = "Passwords donot match!!";
            errorclass = "alert alert-warning";
            return null;
        }
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail(userEmail);
        loginInfo.setFirstName(userfirstname);
        loginInfo.setLastName(userlastname);
        loginInfo.setPassword(userpassword);

        Client client = ClientBuilder.newClient();
        try {
            Entity<LoginInfo> abcd = Entity.json(loginInfo);
            String response = client
                    .target(URL)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_PLAIN_TYPE)
                    .post(Entity.json(loginInfo), String.class);
            System.out.println("");
        } catch (Exception e) {
            System.out.println("");

        }
        regMSG = "Registration successfull Please verify your email before login";
        regclass= "alert alert-success";
        return "login?faces-redirect=true";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUserREpassword() {
        return userREpassword;
    }

    public void setUserREpassword(String userREpassword) {
        this.userREpassword = userREpassword;
    }

    public String getUserfirstname() {
        return userfirstname;
    }

    public void setUserfirstname(String userfirstname) {
        this.userfirstname = userfirstname;
    }

    public String getUserlastname() {
        return userlastname;
    }

    public void setUserlastname(String userlastname) {
        this.userlastname = userlastname;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public String getErrorclass() {
        return errorclass;
    }

    public String getRegclass() {
        return regclass;
    }

    public void setRegclass(String regclass) {
        this.regclass = regclass;
    }

    public String getRegMSG() {
        return regMSG;
    }

    public void setRegMSG(String regMSG) {
        this.regMSG = regMSG;
    }

}
