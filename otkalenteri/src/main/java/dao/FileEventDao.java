package dao;

import domain.Event;
import domain.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Class for dealing with a list of events.
 */
public class FileEventDao implements EventDao{
    /**
    * List of events
    */
    public List<Event> events;
    private String file;

    /**
    * Constructor for FileEventDao initializes list of events to an empty ArrayList.
    * 
    */
    public FileEventDao(String file, FileUserDao users) throws IOException {
        this.events = new ArrayList<>();
        this.file = file;
        try {
            Scanner reader = new Scanner(new File(file));
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(";");
                int id = Integer.parseInt(parts[0]);
                boolean prive = Boolean.parseBoolean(parts[3]);
                User user = users.getAll().stream().filter(u->u.getUsername().equals(parts[4])).findFirst().orElse(null); 
                Event ev = new Event(id, parts[1], parts[2],prive, user);
                events.add(ev);
            }
        } catch (Exception e) {
            FileWriter writer = new FileWriter(new File(file));
            writer.close();
        }
    }
    
    private void save() throws Exception{
        try (FileWriter writer = new FileWriter(new File(file))) {
            for (Event ev : events) {
                writer.write(ev.getId() + ";" + ev.getName() + ";" + ev.getDateAsString() + ";"+ ev.isPrivate() + ";" + ev.getUser().getUsername() + "\n");
            }
        }
    }    


    private int generateId(){
        return events.size()+1;
    }
    @Override
    public boolean create(Event event) throws Exception {
        if(event.getName().length()>24 || event.getName().length()<3) return false;
        event.setId(generateId());
        events.add(event);
        save();
        return true;
    }

    @Override
    public void delete(Event event) throws Exception{
        events.remove(event);
        save();
    }

    @Override
    public List<Event> getAllPrivate(User user) {
        List<Event> ans = new ArrayList<>();
        if(user == null) return ans;
        for (Event e2 : events) {
            if(e2.getUser().getUsername().equals(user.getUsername()) && e2.isPrivate()) ans.add(e2);
        }
        return ans;
    }

    @Override
    public List<Event> getAllPublic() {
        List<Event> ans = new ArrayList<>();
        for (Event e2 : events) {
            if(!e2.isPrivate()) ans.add(e2);
        }
        return ans;
    }

}
