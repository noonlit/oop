package Hotel.Repository;

import Hotel.Domain.Room;
import Hotel.Exception.ExistingEntityException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HotelRepository {
    public static final int ROOMS = 20;
    private static int lastRoomId = 0;
    private Map<Integer, Room> rooms;
    private Map<Integer, Room> occupiedRooms;

    public HotelRepository() {
        rooms = new HashMap<Integer, Room>(ROOMS);
        occupiedRooms = new HashMap<Integer, Room>(ROOMS);
    }

    /**
     * Retrieves all rooms present in the hotel.
     *
     * @return A Collection of rooms
     */
    public Collection<Room> getRooms() {
        return rooms.values();
    }
    
    public Room getRoomByNumber(Integer number) {
        return rooms.get(number);
    }

    /**
     * Adds a room to the hotel.
     *
     * @param room The room to add
     */
    public void add(Room room) {
        // sanity check: is this really a new room?
        if (room.getId() != null && rooms.containsKey(room.getNumber())) {
            throw new ExistingEntityException("The room already exists!");
        }
        
        lastRoomId++;
        room.setId(lastRoomId);
        rooms.put(room.getNumber(), room);
    }

    public void occupy(Room room) {
        occupiedRooms.put(room.getNumber(), room);
    }
    
    public boolean isRoomOccupied(int roomNumber) {
        return occupiedRooms.containsKey(roomNumber);
    }

    public void freeRoom(int roomNumber) {
        occupiedRooms.remove(roomNumber);
    }
}
