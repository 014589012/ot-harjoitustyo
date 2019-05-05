
import dao.UserDao;
import domain.User;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kalle
 */
class FakeUserDao implements UserDao {
    public List<User> users;
    public FakeUserDao() {
        this.users = new ArrayList<>();
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
