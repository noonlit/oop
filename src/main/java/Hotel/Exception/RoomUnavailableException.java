package Hotel.Exception;

public class RoomUnavailableException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public RoomUnavailableException(String string) {
        super(string);
    }
}
