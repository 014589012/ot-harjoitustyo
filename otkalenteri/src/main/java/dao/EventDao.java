package dao;

import domain.Event;
import domain.User;
import java.util.List;


public interface EventDao {
    /**
    * Method creates an event and adds it to the list of events.
    *
    * @param   event   Event to be created.
    * 
    * @return Boolean value for whether event creation worked.
    */
    boolean create(Event event) throws Exception;

    /**
    * Method deletes an event from the list of events.
    *
    * @param   event   Event to be deleted.
    * 
    */
    void delete(Event event) throws Exception;

    /**
    * Method lists upcoming private events.
    * 
    * @param   user   User whose private events are listed.
    * 
    * @return List of all upcoming private events for specific user.
    */
    List<Event> getAllPrivate(User user);

    /**
    * Method lists upcoming public events.
    * 
    * @return List of all upcoming public events.
    */
    List<Event> getAllPublic();
}
