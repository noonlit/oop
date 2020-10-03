package Hotel.Tests;

import Hotel.Domain.Checkin;
import Hotel.Domain.Room;
import Hotel.Domain.Validator;
import Hotel.Repository.CheckinRepository;
import Hotel.Repository.CheckoutRepository;
import Hotel.Repository.HotelRepository;
import Hotel.Service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HotelServiceTest {
    HotelService hotelService;
    CheckinRepository checkinRepository;
    CheckoutRepository checkoutRepository;

    @BeforeEach
    void setUp() {
        HotelRepository hotel = new HotelRepository();
        hotel.add(new Room(1));
        hotel.add(new Room(2));
        hotel.add(new Room(3));
        hotel.add(new Room(4));
        hotel.add(new Room(5));
        hotel.add(new Room(6));
        hotel.add(new Room(7));

        Validator validator = new Validator();
        checkinRepository = new CheckinRepository(hotel);
        checkoutRepository = new CheckoutRepository(hotel);
        hotelService = new HotelService(validator, hotel, checkinRepository, checkoutRepository);
    }

    @Test
    void newlyCreatedCheckinShouldHaveANonNullId() {
        Checkin checkin = new Checkin(1, 2, 3);
        checkinRepository.add(checkin);
        assertNotEquals(null, checkin.getId());
    }

    @Test
    void newlyCreatedCheckinShouldBeRetrievableById() {
        Checkin checkin = new Checkin(1, 2, 3);
        checkinRepository.add(checkin);
        assertEquals(checkinRepository.getCheckinById(checkin.getId()).getPersonsNumber(), 1);
        assertEquals(checkinRepository.getCheckinById(checkin.getId()).getDaysNumber(), 2);
        assertEquals(checkinRepository.getCheckinById(checkin.getId()).getRoomNumber(), 3);
    }
}