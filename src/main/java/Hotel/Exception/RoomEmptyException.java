package Hotel.Exception;

public class RoomEmptyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public RoomEmptyException(String string) {
        super(string);
    }
}
