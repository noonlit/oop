package JavaFX.Domain.Operations;

import JavaFX.Domain.MovieReservation;
import JavaFX.Repository.IRepository;

import java.time.LocalDate;
import java.util.List;

public class AddRemoveReservations extends UndoRedoOperation<MovieReservation> {
    private LocalDate start;
    private LocalDate stop;
    private List<MovieReservation> movieReservations;

    public AddRemoveReservations(IRepository<MovieReservation> iRepository, LocalDate start, LocalDate stop,
                                 List<MovieReservation> movieReservations) {
        super(iRepository);
        this.start = start;
        this.stop = stop;
        this.movieReservations=movieReservations;
    }


    @Override
    public void undo() {
      for (MovieReservation movieReservation:this.movieReservations){
          this.iRepository.recreate(movieReservation);
      }

    }

    @Override
    public void redo() {
        for (MovieReservation movieReservation : this.iRepository.read()) {
            if(movieReservation.getReservationDate().isAfter(start)
                    && movieReservation.getReservationDate().isBefore(stop)){
                this.iRepository.delete(movieReservation.getId());
            }
        }
    }
}
