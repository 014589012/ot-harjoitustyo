/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domain.User;
import java.util.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kalle
 */
public class UserTest {
    FakeUserDao fud;
    User kalle;
    User tuomas;
    
    @Before
    public void setUp() {
        fud = new FakeUserDao();
        kalle = new User("kalle","salasana");
        tuomas = new User("tuomas", "password");
    }
    
    @Test
    public void createUserWorks() throws Exception{
        assertTrue(fud.create(kalle) && fud.create(tuomas));
    }

    @Test
    public void cannotCreateUserWithTakenUsername() throws Exception{
        fud.create(kalle);
        assertFalse(fud.create(new User("kalle", "12345")));
    }

    @Test
    public void cannotCreateUserWithUsernameOver24Characters() throws Exception{
        assertFalse(fud.create(new User("abcdefghijklmnopqrstuvwxyzåäö", "12345")));
    }

    @Test
    public void cannotCreateUserWithUsernameUnder3Characters() throws Exception{
        assertFalse(fud.create(new User("a_", "12345")));
    }

    @Test
    public void cannotCreateUserWithPasswordOver24Characters() throws Exception{
        assertFalse(fud.create(new User("12345", "abcdefghijklmnopqrstuvwxyzåäö")));
    }

    @Test
    public void cannotCreateUserWithPasswordUnder3Characters() throws Exception{
        assertFalse(fud.create(new User("12345", "a_")));
    }
    
    @Test
    public void findByUsernameWorks() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        assertEquals(fud.findByUsername("tuomas"),tuomas);
    }
    
    @Test
    public void isAdminReturnsTrueForAdmin(){
        assertTrue(new User("kk5","iamadmin2").isAdmin());
    }
    
    @Test
    public void isAdminReturnsFalseForNonAdmin(){
        assertFalse(tuomas.isAdmin());
    }
    
    @Test
    public void getAllWorks() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        User emma = new User("emma","fffff");
        fud.create(emma);
        List<User> list = new ArrayList<>();
        list.add(kalle);
        list.add(tuomas);
        list.add(emma);
        assertEquals(list,fud.getAll());
    }
}
