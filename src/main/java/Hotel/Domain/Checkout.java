package Hotel.Domain;

public class Checkout {
    private Integer id;
    private int roomNumber;
    private String feedback;
    private int rating;

    public Checkout(int roomNumber, String feedback, int rating) {
        this.roomNumber = roomNumber;
        this.feedback = feedback;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (this.id != null) {
            return;
        }

        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Checkout --- ID = %d, with feedback %s, room %d, rating %d.", id, feedback, roomNumber, rating);
    }
}
