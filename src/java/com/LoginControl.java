/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author it354f701
 */
@ManagedBean
@SessionScoped
public class LoginControl implements Serializable {

    private String userEmail;
    private String userpassword;
    private String userREpassword;
    private String userfirstname;
    private String userlastname;
    private String selectedItem;
    private String errorMSG;
    private String errorclass;

    public LoginControl() {

    }

    public void refresh() {
        errorMSG = "";
        errorclass = "";
    }

    final private String URL = "http://10.112.4.10:8080/CartShareServ1/webresources/com.logininfo/";
    private boolean loggedin = false;

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String loggedin() {
        if (loggedin) {
            return null;
        }
        return "login?faces-redirect=true";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public String authenticate() {

        if (userEmail.equals("")) {
        } else {
            Client client = ClientBuilder.newClient();
            LoginInfo info = client
                    .target(URL)
                    .path("{userID}")
                    .resolveTemplate("userID", userEmail)
                    .request(MediaType.APPLICATION_JSON)
                    .get(LoginInfo.class);

            client.close();

            if (info == null) {
                errorMSG = "No user found with this id";
                errorclass = "alert alert-warning";
                return null;
            }
            if (userpassword.equals(info.getPassword())) {

                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                HttpSession httpSession = request.getSession(false);
                httpSession.setAttribute("userSessionObj", info);
                loggedin = true;
                errorMSG = "";
                errorclass = "";
                return "shoppingList?faces-redirect=true";

            }
            errorMSG = "userid or password incorrect";
            errorclass = "alert alert-warning";
        }
        return null;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
}
