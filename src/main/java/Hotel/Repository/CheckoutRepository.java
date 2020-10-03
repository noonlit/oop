package Hotel.Repository;

import Hotel.Domain.Checkout;
import Hotel.Domain.Room;
import Hotel.Exception.NonExistingEntityException;

import java.util.*;

public class CheckoutRepository {
    private static int lastCheckoutId = 0;
    private Map<Integer, Checkout> checkouts;
    private HotelRepository hotelRepository;

    public CheckoutRepository (HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
        this.checkouts = new HashMap<>();
    }

    public List<Checkout> getCheckouts() {
        return new ArrayList<>(checkouts.values());
    }

    public void add(Checkout checkout) {
        // sanity check: does the room exist at all?
        Room room = hotelRepository.getRoomByNumber(checkout.getRoomNumber());
        if (room == null) {
            throw new NonExistingEntityException("The room does not exist!");
        }

        lastCheckoutId++;
        checkout.setId(lastCheckoutId);
        checkouts.put(checkout.getId(), checkout);

        hotelRepository.freeRoom(checkout.getRoomNumber());
    }
}
