package domain;


import domain.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kwkalle
 */
public class Event {
    private String name;
    private String date;
    private User user;
    private boolean prive;

    public Event(String name, String date, boolean prive, User user) {
        this.name = name;
        this.date = date;
        this.prive = prive;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    
}
