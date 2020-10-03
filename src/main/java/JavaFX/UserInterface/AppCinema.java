package JavaFX.UserInterface;

import JavaFX.Domain.Movie;
import JavaFX.Service.MovieReservationService;
import JavaFX.Service.MovieService;
import JavaFX.Service.UndoRedoService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;

public class AppCinema implements IUserInterface {
    @FXML
    public Pane display;
    private  MovieReservationService movieReservationService;
    private  MovieService movieService;
    private  UndoRedoService undoRedoService;

    public void setServices(MovieReservationService movieReservationService, MovieService movieService, UndoRedoService undoRedoService)
    {
        this.movieReservationService = movieReservationService;
        this.movieService = movieService;
        this.undoRedoService = undoRedoService;
    }

    @Override
    public void runUserInterface() {
        initialize();
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
        });
    }

    /**
     * Displays a form for adding a new movie.
     *
     * @param actionEvent
     * @return a pane containing the form.
     */
    public Pane handleShowNewMovieForm(ActionEvent actionEvent) {
        VBox vbox = getFrame();

        TextField title = new TextField();
        title.setPromptText("Title: ");
        vbox.getChildren().add(title);

        TextField year = new TextField();
        year.setPromptText("Year of release: ");
        vbox.getChildren().add(year);

        TextField price = new TextField();
        price.setPromptText("Ticket price: ");
        vbox.getChildren().add(price);

        CheckBox isRunning = new CheckBox();
        isRunning.setText("Is this movie currently running?");
        vbox.getChildren().add(isRunning);

        Button submit = new Button("Submit");

        final Text message = new Text();
        submit.setOnAction(e -> {
            try {
                movieService.addMovie(title.getText(), Integer.parseInt(year.getText()), Integer.parseInt(price.getText()));
                message.setText("Add operation successful!");
            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        vbox.getChildren().add(submit);
        vbox.getChildren().add(message);

        return buildDisplay(vbox);
    }

    /**
     * Displays an editable table of movies that currently exist in the application.
     *
     * @param actionEvent
     * @return a pane containing the table.
     */
    public Pane handleShowMovies(ActionEvent actionEvent) {
        VBox vbox = getFrame();

        TableView table = new TableView();

        // add the title column
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Movie, String>>) t -> {
                    Movie movie = t.getRowValue();
                    movie.setTitle(t.getNewValue());
                }
        );

        // add the year of release column
        TableColumn yearCol = new TableColumn("Year of Release");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("yearOfRelease"));
        yearCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // add the ticket price column
        TableColumn ticketPriceCol = new TableColumn("Ticket Price");
        ticketPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        ticketPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // add the 'is currently running' column
        TableColumn isRunningCol = new TableColumn("Is Currently Running");
        isRunningCol.setCellValueFactory(new PropertyValueFactory<>("inTheatre"));
        isRunningCol.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));

        Button delete = new Button("Delete Movie");
        delete.setOnAction(e -> {
            Movie selected = (Movie) table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selected);
            // movieService.delete(selected.getId()); -- not implemented
        });

        Button createReservation = new Button("Create Reservation");
        createReservation.setOnAction(e -> {
            handleShowReservation((Movie) table.getSelectionModel().getSelectedItem());
        });

        // add the columns
        table.getColumns().addAll(titleCol, yearCol, ticketPriceCol, isRunningCol);
        table.setEditable(true);

        // add the actual data
        for (Movie movie : this.movieService.getAll()) {
            table.getItems().add(movie);
        }

        // add table to pane
        vbox.getChildren().add(table);
        HBox hbox = new HBox();
        hbox.getChildren().add(createReservation);
        hbox.getChildren().add(delete);
        vbox.getChildren().add(hbox);
        return buildDisplay(vbox);
    }

    private Pane handleShowReservation(Movie movie) {
        VBox vbox = getFrame();

        TextField movieTitle = new TextField();
        movieTitle.setText(movie.getTitle());
        vbox.getChildren().add(movieTitle);

        TextField clientCardNumber = new TextField();
        clientCardNumber.setPromptText("Your client card: ");
        vbox.getChildren().add(clientCardNumber);

        TextField hour = new TextField();
        hour.setPromptText("The hour: ");
        vbox.getChildren().add(hour);

        TextField day = new TextField();
        day.setPromptText("The day: ");
        vbox.getChildren().add(day);

        TextField month = new TextField();
        month.setPromptText("The month: ");
        vbox.getChildren().add(month);

        TextField year = new TextField();
        year.setPromptText("The year: ");
        vbox.getChildren().add(year);

        Button submit = new Button("Submit");

        final Text message = new Text();
        submit.setOnAction(e -> {
            try {
                LocalDate dateOfReservation = LocalDate.of(
                        Integer.parseInt(year.getText()),
                        Integer.parseInt(month.getText()),
                        Integer.parseInt(day.getText())
                );

                movieReservationService.addReservation(movie.getId(), Integer.parseInt(clientCardNumber.getText()), Integer.parseInt(hour.getText()), dateOfReservation);
                message.setText("Add operation successful!");
            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        vbox.getChildren().add(submit);
        vbox.getChildren().add(message);
        return buildDisplay(vbox);
    }

    private VBox getFrame() {
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        return vbox;
    }

    private Pane buildDisplay(VBox vbox)
    {
        display.getChildren().clear();
        display.getChildren().add(vbox);

        return display;
    }
}
