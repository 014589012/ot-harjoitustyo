package dao;

import domain.User;
import java.util.*;

/**
 * Class for dealing with a list of users.
 */
public class FileUserDao implements UserDao{
    /**
    * List of users.
    */
    public List<User> users;

    /**
    * Constructor for FileUserDao initializes list of users to an empty ArrayList.
    * 
    */
    public FileUserDao() {
        this.users = new ArrayList<>();
    }


    private int generateId() {
        return users.size()+1;
    }

    @Override
    public boolean create(User user){
        if(user.getUsername().length()>24 || user.getUsername().length()<3) return false;
        if(user.getPassword().length()>24 || user.getPassword().length()<3) return false;
        for (User u2 : users) {
            if(u2.getUsername().equals(user.getUsername())) return false;
        }
        user.setId(generateId());
        users.add(user);
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
