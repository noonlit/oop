package Hotel.ViewModel;

public class CheckoutDetailsViewModel {
    public int roomNumber;
    public int rating;

    public CheckoutDetailsViewModel(int roomNumber, int rating) {
        this.roomNumber = roomNumber;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "For room with number " + roomNumber + ", the rating is " + rating;
    }
}
