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
    FakeEventDao fed;
    FakeUserDao fud;
    EventService es;
    User kalle;
    User tuomas;
    Event party;
    
    @Before
    public void setUp() throws ParseException, Exception{
        fud = new FakeUserDao();
        fed = new FakeEventDao(fud);
        kalle = new User("kalle","salasana");
        tuomas = new User("tuomas", "password");
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
    public void loginWorksTrue(){
        assertTrue(es.login("kalle", "salasana"));
    }
    
    @Test
    public void loginWorksFalse(){
        assertFalse(es.login("kalle", "sa"));
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
    public void createUserWorksWithValidUser() throws Exception{
        boolean c = es.createUser("nelly", "pony");
        assertTrue(c && fud.findByUsername("nelly") != null);
    }
    
    @Test
    public void createUserDoesntWorkWithInvalidUsername() throws Exception{
        boolean c = es.createUser("n", "pass");
        assertFalse(c);
    }
    
    @Test
    public void createUserDoesntWorkWithInvalidPassword() throws Exception{
        boolean c = es.createUser("nnn", "p");
        assertFalse(c);
    }
    
    @Test
    public void createUserDoesntWorkWithTakenUsername() throws Exception{
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
    public void markDoneWorks() throws Exception{
        es.markDone(party);
        assertFalse(es.getUpcomingPublic().contains(party));
    }
    
    @Test
    public void getUpcomingPublicWorks() throws Exception{
        List<Event> ans = new ArrayList<>();
        ans.add(party);
        assertEquals(ans,es.getUpcomingPublic());
    }
    
    @Test
    public void getUpcomingPrivateWorksEmpty() throws Exception{
        assertTrue(es.getUpcomingPrivate(kalle).isEmpty());
    }
    
    @Test
    public void getUpcomingPrivateWorksNonEmpty() throws Exception{
        assertEquals("crafts",es.getUpcomingPrivate(tuomas).get(0).getName());
    }
    
    @Test
    public void addDescriptionWorks() throws Exception{
        es.addDescriptionToEvent("hello", party);
        assertEquals("hello",party.getDescription());
    }

}
