package com.test.material.supitsara.materialnavigationtest;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table USER.
 */
public class User {

    private Long id;
    private String userID;
    private String boothID;
    private String search;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String userID, String boothID, String search) {
        this.id = id;
        this.userID = userID;
        this.boothID = boothID;
        this.search = search;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBoothID() {
        return boothID;
    }

    public void setBoothID(String boothID) {
        this.boothID = boothID;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
