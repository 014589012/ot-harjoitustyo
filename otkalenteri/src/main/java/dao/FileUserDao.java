package dao;

import domain.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Class for dealing with a list of users.
 */
public class FileUserDao implements UserDao{
    /**
    * List of users.
    */
    public List<User> users;
    private String file;

    /**
    * Constructor for FileUserDao initializes list of users to an empty ArrayList.
    * 
    */
    public FileUserDao() throws IOException {
        this.users = new ArrayList<>();
        this.file = file;
        try {
            Scanner reader = new Scanner(new File(file));
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split(";");
                User u = new User(parts[0], parts[1]);
                users.add(u);
            }
        } catch (Exception e) {
            FileWriter writer = new FileWriter(new File(file));
            writer.close();
        }
    }

    private void save() throws Exception{
        try (FileWriter writer = new FileWriter(new File(file))) {
            for (User user : users) {
                writer.write(user.getUsername() + ";" + user.getPassword() + "\n");
            }
        } 
    }

    private int generateId() {
        return users.size()+1;
    }

    @Override
    public boolean create(User user) throws Exception{
        if(user.getUsername().length()>24 || user.getUsername().length()<3) return false;
        if(user.getPassword().length()>24 || user.getPassword().length()<3) return false;
        for (User u2 : users) {
            if(u2.getUsername().equals(user.getUsername())) return false;
        }
        user.setId(generateId());
        users.add(user);
        save();
        return true;
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users) {
            if(user.getUsername().equals(username)) return user;
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return users;
    }


}
