package dao;

import domain.Event;
import domain.User;
import java.util.List;


public interface EventDao {
    void create(Event event) throws Exception;

    void delete(Event event) throws Exception;

    List<Event> getAllPrivate(User user);

    List<Event> getAllPublic();
}
