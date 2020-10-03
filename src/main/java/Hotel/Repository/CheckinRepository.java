package Hotel.Repository;

import java.util.HashMap;
import java.util.Map;

import Hotel.Domain.Checkin;
import Hotel.Domain.Room;
import Hotel.Exception.NonExistingEntityException;
import Hotel.Exception.RoomEmptyException;
import Hotel.Exception.RoomUnavailableException;
import org.jetbrains.annotations.NotNull;

public class CheckinRepository {
    private static int lastCheckinId = 0;
    private Map<Integer, Checkin> checkins;
    private HotelRepository hotelRepository;
    
    public CheckinRepository(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
        this.checkins = new HashMap<>();
    }
    
    public Checkin getCheckinById(Integer id) {
        return checkins.get(id);
    }
    
    public void add(@NotNull Checkin checkin) throws RoomUnavailableException, NonExistingEntityException {
        // sanity check: is this really a new checkin?
        if (checkin.getId() != null && checkins.containsKey(checkin.getId())) {
            throw new NonExistingEntityException("The checkin already exists!");
        }

        // sanity check: does the room exist at all?
        Room room = hotelRepository.getRoomByNumber(checkin.getRoomNumber());
        if (room == null) {
            throw new NonExistingEntityException("The room does not exist!");
        }
        
        lastCheckinId++;
        checkin.setId(lastCheckinId);
        checkins.put(checkin.getId(), checkin);
        
        // mark the room as occupied
        hotelRepository.occupy(room);
    } 
    
    public void update(@NotNull Checkin checkin) throws RoomEmptyException, NonExistingEntityException {
        if (checkins.get(checkin.getId()) == null) {
            throw new NonExistingEntityException("The checkin does not exist!");
        }
        
        // sanity check: does the room exist?
        if (hotelRepository.getRoomByNumber(checkin.getRoomNumber()) == null) {
            throw new NonExistingEntityException("The room does not exist!");
        }

        // it will be funny if we can't update the room because it's occupied ... by the same person
        // TODO add proper validation
        
        checkins.put(checkin.getId(), checkin);
    } 
}
