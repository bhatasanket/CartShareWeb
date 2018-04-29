/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author it354f701
 */
@ManagedBean
public class ShoppingList {
    private int Id;
    private String Groupname;
    private String Listitems;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getGroupname() {
        return Groupname;
    }

    public void setGroupname(String Groupname) {
        this.Groupname = Groupname;
    }

    public String getListitems() {
        return Listitems;
    }

    public void setListitems(String Listitems) {
        this.Listitems = Listitems;
    }

   
}
