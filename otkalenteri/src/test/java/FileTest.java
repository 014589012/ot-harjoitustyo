/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.EventDao;
import dao.FileEventDao;
import dao.FileUserDao;
import dao.UserDao;
import domain.Event;
import domain.User;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Kalle
 */
public class FileTest {
    
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();    
  
    File userFile;  
    EventDao dao;    
    
    @Before
    public void setUp() throws Exception {
        userFile = testFolder.newFile("testfile_users.txt");  
        UserDao userDao = new FakeUserDao();
        userDao.create(new User("tester", "salatest"));
        
        try (FileWriter file = new FileWriter(userFile.getAbsolutePath())) {
            file.write("1;party;2019-08-08;false;tester;kivaaa\n");
        }
        
        dao = new FileEventDao(userFile.getAbsolutePath(), userDao);        
    }
    
    @Test
    public void eventsAreReadCorrectlyFromFile() {
        List<Event> evs = dao.getAllPublic();
        assertEquals(1, evs.size());
        Event ev = evs.get(0);
        assertEquals("party", ev.getName());
        assertEquals("2019-08-08",ev.getDateAsString());
        assertFalse(ev.isPrivate());
        assertEquals("kivaaa", ev.getDescription());
        assertEquals("tester", ev.getUser().getUsername());
    }
    
}
