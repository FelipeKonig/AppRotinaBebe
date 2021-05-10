package br.com.projetobebe.controllerDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.projetobebe.model.Event;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE id IN (:eventIds)")
    List<Event> loadAllByIds(int[] eventIds);

    @Query("SELECT * FROM event WHERE id = :id")
    Event findById(int id);

    @Query("SELECT * FROM event WHERE name_type LIKE :name_type LIMIT 1")
    Event findByName(String name_type);

    @Insert
    void insertEvent(Event event);

    @Insert
    void insertAll(List<Event> eventList);

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);

    @Query("DELETE FROM event")
    void deleteAll();
}
