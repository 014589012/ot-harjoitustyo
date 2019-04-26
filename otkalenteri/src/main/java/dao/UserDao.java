
package dao;

import java.util.List;
import domain.User;

public interface UserDao {

    /**
    * Method creates a new user.
    *
    * @param   user   User to be created.
    * 
    * @return Boolean value for whether user creation worked.
    */
    boolean create(User user) throws Exception;

    /**
    * Method searches the list of users for a specific one.
    *
    * @param   username   Name of user to be found.
    * 
    * @return User with the given username or null if not found.
    */
    User findByUsername(String username);

    /**
    * Method gets list of users.
    *
    * @return List of all users.
    */
    List<User> getAll();
}
