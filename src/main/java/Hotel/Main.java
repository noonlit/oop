package Hotel;

import Hotel.Domain.Room;
import Hotel.Domain.Validator;
import Hotel.Repository.CheckinRepository;
import Hotel.Repository.CheckoutRepository;
import Hotel.Repository.HotelRepository;
import Hotel.Service.HotelService;
import Hotel.Util.InputOutput;
import Hotel.UserInterface.Console;

public class Main {

    /**
     * Driver code.
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        HotelRepository hotelRepository;
        try {
            hotelRepository = initHotel();
        } catch (Exception e) {
            System.out.println("Cannot start application!");
            System.out.println(e.getMessage());
            return;
        }
        
        Validator validator = new Validator();
        CheckinRepository checkinRepository = new CheckinRepository(hotelRepository);
        CheckoutRepository checkoutRepository = new CheckoutRepository(hotelRepository);
        HotelService hotelService = new HotelService(validator, hotelRepository, checkinRepository, checkoutRepository);

        // TODO remove test data.
        addTestData(hotelService);

        Console console = new Console(hotelService, new InputOutput());
        console.run(true);
    }
    
    /**
     * Adds rooms to the hotel, to enable us to check into them.
     */
    private static HotelRepository initHotel() {
        HotelRepository hotel = new HotelRepository();
        hotel.add(new Room(1));
        hotel.add(new Room(2));
        hotel.add(new Room(3));
        hotel.add(new Room(4));
        hotel.add(new Room(5));
        hotel.add(new Room(6));
        hotel.add(new Room(7));
        
        return hotel;
    }

    /**
     * Creates checkin/checkouts for testing purposes.
     *
     * @param hotelService
     */
    private static void addTestData(HotelService hotelService) {
        System.out.println(hotelService.createCheckin(1, 1, 1));
        System.out.println(hotelService.createCheckin(2, 1, 2));
        System.out.println(hotelService.createCheckin(3, 1, 3));
        System.out.println(hotelService.createCheckin(4, 1, 4));

        // checkout from room 1
        System.out.println(hotelService.createCheckout(1, "It was great!", 5));

        // checkout from room 4
        System.out.println(hotelService.createCheckout(4, "It was ... okay!", 4));

        // add new checkin-checkout for room 1
        System.out.println(hotelService.createCheckin(1, 1, 1));
        System.out.println(hotelService.createCheckout(1, "It was meh!", 3));

        // add new checkin-checkout for room 1
        System.out.println(hotelService.createCheckin(1, 1, 1));
        System.out.println(hotelService.createCheckout(1, "It was ok!", 1));

        // add new checkin-checkout for room 2
        System.out.println(hotelService.createCheckout(2, "Eh", 1));
        System.out.println(hotelService.createCheckin(1, 1, 2));
        System.out.println(hotelService.createCheckout(2, "Eh", 1));
    }
}
