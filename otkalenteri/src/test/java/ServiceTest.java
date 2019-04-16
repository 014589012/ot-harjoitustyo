/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.FileEventDao;
import dao.FileUserDao;
import domain.Event;
import domain.EventService;
import domain.User;
import java.text.ParseException;
import java.util.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kwkalle
 */
public class ServiceTest {
    FileEventDao fed;
    FileUserDao fud;
    EventService es;
    User kalle;
    Event party;
    
    @Before
    public void setUp() throws ParseException, Exception{
        fed = new FileEventDao();
        fud = new FileUserDao();
        kalle = new User("kalle","salasana");
        User tuomas = new User("tuomas", "password");
        fud.create(kalle);
        fud.create(tuomas);
        party = new Event("party", "2022-04-27", false, kalle);
        Event crafts = new Event("crafts", "2022-06-10", true, tuomas);
        Event sports = new Event("urheilua", "2018-05-05", false, tuomas);
        fed.create(sports);
        fed.create(party);
        fed.create(crafts);
        es = new EventService(fed,fud);
    }
    
    @Test
    public void loginWorks(){
        assertTrue(es.login("kalle", "salasana"));
    }
    
    @Test
    public void getloggedUserWorks(){
        es.login("kalle", "salasana");
        assertEquals(fud.findByUsername("kalle"),es.getLoggedUser());
    }
    
    @Test
    public void logoutWorks(){
        es.login("tuomas", "password");
        es.logout();
        assertEquals(null,es.getLoggedUser());
    }
    
    @Test
    public void createUserWorksWithValidUser(){
        boolean c = es.createUser("nelly", "pony");
        assertTrue(c && fud.findByUsername("nelly") != null);
    }
    
    @Test
    public void createUserDoesntWorkWithInvalidUsername(){
        boolean c = es.createUser("n", "pass");
        assertFalse(c);
    }
    
    @Test
    public void createUserDoesntWorkWithInvalidPassword(){
        boolean c = es.createUser("nnn", "p");
        assertFalse(c);
    }
    
    @Test
    public void createUserDoesntWorkWithTakenUsername(){
        boolean c = es.createUser("tuomas", "pony");
        assertFalse(c);
    }
    
    @Test
    public void createEventWorksWithValidEvent() throws ParseException{
        boolean c = es.createEvent("Appro", "2019-06-06",false);
        assertTrue(c);
    }
    
    @Test
    public void createEventDoesntWorkWithInvalidEventName() throws ParseException{
        boolean c = es.createEvent("Ap", "2019-06-06",false);
        assertFalse(c);
    }

    @Test
    public void markDoneWorks(){
        es.markDone(party);
        assertFalse(es.getUpcomingPublic().contains(party));
    }
    
    @Test
    public void getUpcomingPublicWorks(){
        List<Event> ans = new ArrayList<>();
        ans.add(party);
        assertEquals(ans,es.getUpcomingPublic());
    }

}