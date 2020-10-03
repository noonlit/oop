package Hotel.Domain;

public class Room {
    private Integer id;
    private Integer number;

    public Room(int number) {
        this.number = number;
    }

    public void setId(Integer id) {
        // ensure ID can only be set once
        if (this.id != null) {
            return;
        }
        
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public Integer getNumber() {
        return this.number;
    }
}
