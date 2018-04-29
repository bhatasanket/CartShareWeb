/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
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

/**
 *
 * @author it354f701
 */
@ManagedBean
@ViewScoped
public class ShoppingListControl implements Serializable {

    private LoginInfo userlogininfo;

    final private String groupURL = "http://10.112.4.10:8080/CartShareServ1/webresources/com.associations/";
    final private String shoppingURL = "http://10.112.4.10:8080/CartShareServ1/webresources/com.shoppinglist/";
    final private String invitationURL = "http://10.112.4.10:8080/CartShareServ1/webresources/com.invitations/";

    private List<Associations> groupList;
    private List<ShoppingList> shoppingList;
    private String groupListselected;
    private String shoppingListselected;
    private String itemtobeadded;
    private String listtobeadded;
    private String emailtosendinvite;
    private Boolean rendershoppingList = false;
    private Boolean rendershoppingListempty = false;
    private List<String> autocompletelist= new ArrayList<>();

    public ShoppingListControl() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        userlogininfo = (LoginInfo) httpSession.getAttribute("userSessionObj");
    }

    public String resetAll() {
        loadGrouplist();
        rendershoppingList = false;
        groupListselected = "Select a shopping list";
        return "shoppingList?faces-redirect=true";

    }

    public void loadGrouplist() {
        Client client = ClientBuilder.newClient();
        groupList = client
                .target(groupURL)
                .path("")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Associations>>() {
                });
        //                                .resolveTemplate("userID", userEmail)
        client.close();

    }

    public void addnewshoppinglist() {
        if (listtobeadded != null) {
            Associations list = new Associations();
            list.setId(1);
            list.setGroupname(listtobeadded);
            list.setEmail(userlogininfo.getEmail());
            Client client = ClientBuilder.newClient();
            try {
//            Entity<ShoppingList> abcd = Entity.json(loginInfo);
                String response = client
                        .target(groupURL)
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN_TYPE)
                        .post(Entity.json(list), String.class);
                System.out.println("");
                loadGrouplist();
                listtobeadded = null;

            } catch (Exception e) {
                System.out.println("");

            }
            client.close();
        }
    }

    public void addtoshoppinglist() {
        if (!itemtobeadded.equals("")) {
            ShoppingList item = new ShoppingList();
            item.setId(1);
            item.setGroupname(groupListselected);
            item.setListitems(itemtobeadded + "    => " + userlogininfo.getFirstName());
            Client client = ClientBuilder.newClient();
            try {
                String response = client
                        .target(shoppingURL)
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN_TYPE)
                        .post(Entity.json(item), String.class);
                System.out.println("");
                loadShoppinglist();
                itemtobeadded = null;
            } catch (Exception e) {
                System.out.println("");
            }
            client.close();
        }
    }

    public void deletefromlist() {
        if (shoppingListselected != null) {
            Client client = ClientBuilder.newClient();
            try {
                int b = client
                        .target(shoppingURL)
                        .path("{id}")
                        .resolveTemplate("id", shoppingListselected)
                        .request()
                        .delete(Integer.class);
                loadShoppinglist();
                itemtobeadded = null;
            } catch (Exception e) {
                System.out.println("");
                loadShoppinglist();

            }
            client.close();
            loadShoppinglist();
        }
    }

    public void exitfromgroup() {
        int idtobedeleted = -1;
        for (Associations ass : groupList) {
            if (ass.getGroupname().equals(groupListselected)) {
                idtobedeleted = ass.getId();
                groupListselected = null;
                break;
            }
        }
        Client client = ClientBuilder.newClient();
        try {
            int b = client
                    .target(groupURL)
                    .path("{id}")
                    .resolveTemplate("id", idtobedeleted)
                    .request()
                    .delete(Integer.class);
            resetAll();
        } catch (Exception e) {
            System.out.println("");
            resetAll();
        }
        client.close();
//        loadShoppinglist();
    }

    public void loadShoppinglist() {
        if (groupListselected != null) {

            Client client = ClientBuilder.newClient();
            shoppingList = client
                    .target(shoppingURL)
                    .path("/groupname/{groupname}")
                    .resolveTemplate("groupname", groupListselected)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ShoppingList>>() {
                    });
            //                                .resolveTemplate("userID", userEmail)
            if (shoppingList.isEmpty()) {
                rendershoppingListempty = true;
            } else {
                rendershoppingListempty = false;
            }
            autocompletelist = new ArrayList<>();

            for (ShoppingList shop : shoppingList) {
                autocompletelist.add(shop.getListitems().split("   ")[0]);
            }

            client.close();
            rendershoppingList = true;
        }
    }

    public List autocompletelistmethod() {
        return autocompletelist;
    }

    public void sendinvitation() {
        if (!emailtosendinvite.equals("")) {
            Invitations i = new Invitations();
            i.setId(1);
            i.setGroupname(groupListselected);
            i.setEmail(emailtosendinvite);
            Client client = ClientBuilder.newClient();
            try {
                String response = client
                        .target(invitationURL)
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_PLAIN_TYPE)
                        .post(Entity.json(i), String.class);
                System.out.println("");
                emailtosendinvite = null;
            } catch (Exception e) {
                System.out.println("");
            }
            client.close();
        }
    }

    public List<ShoppingList> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShoppingList> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<Associations> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Associations> groupList) {
        this.groupList = groupList;
    }

    public Boolean getRendershoppingList() {
        return rendershoppingList;
    }

    public void setRendershoppingList(Boolean rendershoppingList) {
        this.rendershoppingList = rendershoppingList;
    }

    public String getGroupListselected() {
        return groupListselected;
    }

    public void setGroupListselected(String groupListselected) {
        this.groupListselected = groupListselected;
    }

    public String getItemtobeadded() {
        return itemtobeadded;
    }

    public void setItemtobeadded(String itemtobeadded) {
        this.itemtobeadded = itemtobeadded;
    }

    public String getListtobeadded() {
        return listtobeadded;
    }

    public void setListtobeadded(String listtobeadded) {
        this.listtobeadded = listtobeadded;
    }

    public Boolean getRendershoppingListempty() {
        return rendershoppingListempty;
    }

    public void setRendershoppingListempty(Boolean rendershoppingListempty) {
        this.rendershoppingListempty = rendershoppingListempty;
    }

    public String getShoppingListselected() {
        return shoppingListselected;
    }

    public void setShoppingListselected(String shoppingListselected) {
        this.shoppingListselected = shoppingListselected;
    }

    public String getEmailtosendinvite() {
        return emailtosendinvite;
    }

    public void setEmailtosendinvite(String emailtosendinvite) {
        this.emailtosendinvite = emailtosendinvite;
    }

    public List<String> getAutocompletelist() {
        return autocompletelist;
    }

    public void setAutocompletelist(List<String> autocompletelist) {
        this.autocompletelist = autocompletelist;
    }

}
