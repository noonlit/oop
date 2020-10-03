package Cinema.Domain.Validator;

import Cinema.Domain.AbstractEntity;
import Cinema.Domain.Booking;
import Cinema.Exception.EntityNotFoundException;
import Cinema.Exception.LogicException;
import java.time.LocalDateTime;

public class BookingValidator implements ValidatorInterface {
    /**
     * Validates a booking.
     *
     * A booking must be for an existing movie.
     * The movie must be running.
     * The booking must not be in the past.
     *
     * @param entity The booking entry
     */
    public void validate(AbstractEntity entity) {
        Booking booking = (Booking) entity;

        if (booking.getMovie() == null) {
            throw new EntityNotFoundException("The booking doesn't correspond to any movie!");
        }

        if (!booking.getMovie().isRunning()) {
            throw new LogicException("The movie isn't running!");
        }

        if (booking.getDateTime().isBefore(LocalDateTime.now())) {
            throw new LogicException("The booking is in the past!");
        }
    }
}
