
import dao.FileEventDao;
import dao.FileUserDao;
import domain.Event;
import domain.User;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class EventTest {
    FakeEventDao fed;
    FakeUserDao fud;
    User kalle;
    User tuomas;
    Event party;
    Event crafts;
    Event sports;

    @Before
    public void setUp() throws ParseException, IOException {
        fud = new FakeUserDao();
        fed = new FakeEventDao(fud);
        kalle = new User("kalle","salasana");
        tuomas = new User("tuomas", "password");
        party = new Event("party", "2019-04-27", false, kalle);
        crafts = new Event("crafts", "2019-06-10", true, tuomas);
        sports = new Event(3,"urheilua", "2018-05-05", false, "futista kumpulassa",tuomas);
    }

    
    @Test
    public void eventGetNameWorks(){
        assertEquals("crafts",crafts.getName());
    }
    
    @Test
    public void eventGetDateWorks(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2019-06-10",df.format(crafts.getDate()));
    }
    
    @Test
    public void eventGetDateAsStringWorks(){
        assertEquals("2019-04-27",party.getDateAsString());
    }
    
    @Test
    public void eventGetUserWorks(){
        assertEquals(kalle,party.getUser());
    }
    
    @Test
    public void eventIsPrivateWorksForTrue(){
        assertTrue(crafts.isPrivate());
    }
    
    @Test
    public void eventIsPrivateWorksForFalse(){
        assertFalse(party.isPrivate());
    }
    
    @Test
    public void eventGetIdWorks(){
        assertEquals(3,sports.getId());
    }
    
    @Test
    public void eventSetIdWorks(){
        sports.setId(5);
        assertEquals(5,sports.getId());
    }
    
    @Test
    public void eventGetDescription(){
        assertEquals("futista kumpulassa",sports.getDescription());
    }
    
    @Test
    public void eventSetDescription(){
        sports.setDescription("floorball");
        assertEquals("floorball",sports.getDescription());
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
        assertEquals("1,2",party.getId()+","+crafts.getId());
    }





}
