package Hotel.Domain;

/**
 * Represents a checkin.
 */
public class Checkin {
    private Integer id;
    private int personsNumber;
    private int daysNumber;
    private int roomNumber;

    public Checkin(int id, int personsNumber, int daysNumber, int roomNumber) {
        this.id = id;
        this.personsNumber = personsNumber;
        this.daysNumber = daysNumber;
        this.roomNumber = roomNumber;
    }

    public Checkin(int personsNumber, int daysNumber, int roomNumber) {
        this.personsNumber = personsNumber;
        this.daysNumber = daysNumber;
        this.roomNumber = roomNumber;
    }

    /**
     * Returns the room number for the current checkin.
     * 
     * @return int
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the room number for the current checkin.
     */
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Returns the days number for the current checkin.
     *
     * @return int
     */
    public int getDaysNumber() {
        return daysNumber;
    }

    /**
     * Sets the days number for the current checkin.
     */
    public void setDaysNumber(int daysNumber) {
        this.daysNumber = daysNumber;
    }

    /**
     * Returns the number of persons that wish to check in.
     */
    public int getPersonsNumber() {
        return personsNumber;
    }

    /**
     * Sets the number of persons that wish to check in.
     * 
     * @param int personsNumber
     */
    public void setPersonsNumber(int personsNumber) {
        this.personsNumber = personsNumber;
    }

    /**
     * Sets the checkin ID. 
     * This ID can be set only once and is subsequently immutable.
     * 
     * @param Integer id
     */
    public void setId(Integer id) {
        if (this.id != null) {
            return;
        }
        
        this.id = id;
    }
    
    /**
     * Returns the checkin id.
     * 
     * @return Integer
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Returns a string representation of the checkin.
     */
    public String toString() {
        return String.format("Checkin --- ID = %d, with %d people, for %d days, in room %d.", id, personsNumber, daysNumber, roomNumber);
    }
}
