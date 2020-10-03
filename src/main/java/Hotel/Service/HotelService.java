package Hotel.Service;

import Hotel.Domain.Checkin;
import Hotel.Domain.Checkout;
import Hotel.Domain.Validator;
import Hotel.Exception.NonExistingEntityException;
import Hotel.Exception.RoomEmptyException;
import Hotel.Exception.RoomUnavailableException;
import Hotel.Repository.CheckinRepository;
import Hotel.Repository.CheckoutRepository;
import Hotel.Repository.HotelRepository;
import Hotel.ViewModel.CheckoutDetailsViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class HotelService {
    private Validator validator;
    private HotelRepository hotelRepository;
    private CheckinRepository checkinRepository;
    private CheckoutRepository checkoutRepository;

    public HotelService(
            Validator validator, 
            HotelRepository hotelRepository, 
            CheckinRepository checkinRepository, 
            CheckoutRepository checkoutRepository
    ) {
        this.validator = validator;
        this.hotelRepository = hotelRepository;
        this.checkinRepository = checkinRepository;
        this.checkoutRepository = checkoutRepository;
    }
    
    /**
     * Creates a new checkin based on the given parameters.
     *
     * @param personsNumber The number of persons involved in the checkin
     * @param days The number of days for the checkin
     * @param room The room number
     * @return The created checkin
     */
    public Checkin createCheckin(int personsNumber, int days, int room) {
        Checkin checkin = new Checkin(personsNumber, days, room);

        validator.validateCheckin(checkin);

        // sanity check: is the room taken?
        if (hotelRepository.isRoomOccupied(checkin.getRoomNumber())) {
            throw new RoomUnavailableException("The room is occupied!");
        }

        checkinRepository.add(checkin);

        return checkin;
    }

    /**
     * Creates a checkin.
     *
     * @param id The checkin id
     * @param personsNumber The number of persons involved in the checkin
     * @param days The number of days for the checkin
     * @param roomNumber The room number
     * @return The updated checkin
     */
    public Checkin updateCheckin(int id, int personsNumber, int days, int roomNumber) {
        Checkin existingCheckin = checkinRepository.getCheckinById(id);
        
        if (existingCheckin == null) {
            throw new NonExistingEntityException("Cannot update non-existing checkin!");
        }
        
        existingCheckin.setPersonsNumber(personsNumber);
        existingCheckin.setDaysNumber(days);
        existingCheckin.setRoomNumber(roomNumber);

        // sanity check: is the checkin data correct?
        validator.validateCheckin(existingCheckin);

        // sanity check: is the room empty?
        if (!hotelRepository.isRoomOccupied(existingCheckin.getRoomNumber())) {
            throw new RoomEmptyException("The room is empty!");
        }

        checkinRepository.update(existingCheckin);
        
        return existingCheckin;   
    }

    /**
     * Creates a checkout.
     *
     * @param roomNumber The room number to check out from
     * @param feedback The feedback for the room
     * @param rating The room rating
     * @return The created checkout.
     */
    public Checkout createCheckout(int roomNumber, String feedback, int rating) {
        Checkout checkout = new Checkout(roomNumber, feedback, rating);

        validator.validateCheckout(checkout);

        checkoutRepository.add(checkout);
        return checkout;
    }

    /**
     * Retrieves a collection of view models representing room ratings.
     * @return The view models
     */
    public Collection<CheckoutDetailsViewModel> getRoomRatingAveragesSortedAscendingly()
    {
        // retrieve all checkouts and sort them by room number
        List<Checkout> checkouts = checkoutRepository.getCheckouts();
        checkouts.sort(Comparator.comparingInt(Checkout::getRoomNumber));

        // create view models for the checkouts, containing room number and average
        ArrayList<CheckoutDetailsViewModel> viewModels = new ArrayList<>();
        int currentRoomNumber = 0;
        int currentSum  = 0;
        int count = 0;
        for (Checkout checkout : checkouts) {
            if (currentRoomNumber == 0) {
                currentRoomNumber = checkout.getRoomNumber();
            }

            if (currentRoomNumber != checkout.getRoomNumber()) {
                viewModels.add(new CheckoutDetailsViewModel(currentRoomNumber, currentSum/count));
                currentRoomNumber = checkout.getRoomNumber();
                currentSum = checkout.getRating();
                count = 1;
                continue;
            }

            currentSum += checkout.getRating();
            count++;
        }

        // add the data for the last view model, which was not covered in the iteration
        viewModels.add(new CheckoutDetailsViewModel(currentRoomNumber, currentSum/count));

        // sort view models by rating
        viewModels.sort((c1, c2) -> c2.rating - c1.rating);

        return viewModels;
    }
}
