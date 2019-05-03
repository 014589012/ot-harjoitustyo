
import dao.FileEventDao;
import dao.FileUserDao;
import domain.Event;
import domain.User;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class EventTest {
    FileEventDao fed;
    FileUserDao fud;
    User kalle;
    User tuomas;
    Event party;
    Event crafts;
    Event sports;

    @Before
    public void setUp() throws ParseException, IOException {
        fud = new FileUserDao("");
        fed = new FileEventDao("",fud);
        kalle = new User("kalle","salasana");
        tuomas = new User("tuomas", "password");
        party = new Event("party", "2019-04-27", false, kalle);
        crafts = new Event("crafts", "2019-06-10", true, tuomas);
        sports = new Event("urheilua", "2018-05-05", false, tuomas);
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
    public void cannotCreateEventWithNameOver24Characters() throws ParseException, Exception{
        assertFalse(fed.create(new Event("abcdefghijklmnopqrstuvwxyzåäö", "2019-04-28",true,kalle)));
    }

    @Test
    public void cannotCreateEventWithNameUnder3Characters() throws ParseException, Exception{
        assertFalse(fed.create(new Event("a_", "2019-02-02",false,tuomas)));
    }
    
    @Test
    public void findByUsernameWorks() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        assertEquals(fud.findByUsername("tuomas"),tuomas);
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

    @Test
    public void genarateUserIdWorks() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        assertTrue(kalle.getId()==1 && tuomas.getId()==2);
    }

    @Test
    public void getAllPublicWorks() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        fed.create(party);
        fed.create(crafts);
        fed.create(sports);
        List<Event> list = new ArrayList<Event>();
        list.add(party);
        list.add(sports);
        assertEquals(list,fed.getAllPublic());
    }

    @Test
    public void getAllPublicWorksEmpty() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        fed.create(crafts);
        assertTrue(fed.getAllPublic().isEmpty());
    }

    @Test
    public void getAllPrivateWorks() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        fed.create(party);
        fed.create(crafts);
        fed.create(sports);
        List<Event> list = new ArrayList<>();
        list.add(crafts);
        assertEquals(list,fed.getAllPrivate(tuomas));
    }

    @Test
    public void getAllPrivateWorksEmpty() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        fed.create(party);
        fed.create(crafts);
        fed.create(sports);
        assertTrue(fed.getAllPrivate(kalle).isEmpty());
    }

    @Test
    public void genarateEventIdWorks() throws Exception{
        fud.create(kalle);
        fud.create(tuomas);
        fed.create(party);
        fed.create(crafts);
        fed.create(sports);
        assertEquals("1,2,3",party.getId()+","+crafts.getId()+","+sports.getId());
    }





}
