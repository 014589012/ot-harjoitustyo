
package dao;

import java.util.List;
import domain.User;

public interface UserDao {

    boolean create(User user) throws Exception;

    User findByUsername(String username);

    List<User> getAll();
}
