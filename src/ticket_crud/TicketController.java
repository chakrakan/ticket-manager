package ticket_crud;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ticket_crud.model.Ticket;

public class TicketController {
   @FXML private ListView<Ticket> listView; // displays tickets
   @FXML private TextField ticketIDTextField;
   @FXML private TextField destinationTextField;
   @FXML private TextField depTimeTextField;
   @FXML private TextField arrTimeTextField;
   @FXML private TextField ticketNumTextField;
   @FXML private TextField seatNumTextField;
   @FXML private TextField findByDestinationOrIdTextField;

   // interacts with the database
   private final TicketQueries ticketQueries = new TicketQueries();

   // stores list of Ticket objects that results from a database query
   private final ObservableList<Ticket> ticketList = FXCollections.observableArrayList();
   
   // populate listView and set up listener for selection events
   public void initialize() {
      listView.setItems(ticketList); // bind to contactsList
      countRecords();
      getAllEntries(); // populates contactList, which updates listView

      // when ListView selection changes, display selected ticket's data
      listView.getSelectionModel().selectedItemProperty().addListener(
         (observableValue, oldValue, newValue) -> {
            displayTicket(newValue);
         }
      );     
   }

   // get all the entries from the database to populate contactList
   private void getAllEntries() {
      ticketList.setAll(ticketQueries.getAllTickets());
      selectFirstEntry();
   }

   private void countRecords() {
      int records = ticketQueries.countRecords();

      if (records < 5) {
         populateDb();
         getAllEntries();
      }
      else {
         getAllEntries();
      }
   }

   private void populateDb() {
      ticketQueries.populateDb();
   }

   // select first item in listView
   private void selectFirstEntry() {
      listView.getSelectionModel().selectFirst();          
   }

   // display contact information
   private void displayTicket(Ticket ticket) {
      if (ticket != null) {
         ticketIDTextField.setText(Integer.toString(ticket.getTicketID()));
         destinationTextField.setText(ticket.getDestination());
         depTimeTextField.setText(ticket.getDepartureTime());
         arrTimeTextField.setText(ticket.getArrivalTime());
         ticketNumTextField.setText(ticket.getTicketNumber());
         seatNumTextField.setText(Integer.toString(ticket.getSeatNumber()));
      }
      else {
         ticketIDTextField.clear();
         destinationTextField.clear();
         depTimeTextField.clear();
         arrTimeTextField.clear();
         ticketNumTextField.clear();
         seatNumTextField.clear();
      }
   }

   // add a new entry
   @FXML
   void addEntryButtonPressed(ActionEvent event) {
      int result = ticketQueries.addTicket(
              Integer.parseInt(ticketIDTextField.getText()),
              destinationTextField.getText(),
              depTimeTextField.getText(),
              arrTimeTextField.getText(),
              ticketNumTextField.getText(),
              Integer.parseInt(seatNumTextField.getText())
         );
      
      if (result == 1) {
         displayAlert(AlertType.INFORMATION, "Ticket Added",
            "New ticket successfully added!");
      }
      else {
         displayAlert(AlertType.ERROR, "Ticket Not Added",
            "Unable to add ticket.");
      }
      
      getAllEntries();
   }

   @FXML
   void updateEntryButtonPressed(ActionEvent event) {
      int result = ticketQueries.updateTicket(
              destinationTextField.getText(),
              depTimeTextField.getText(),
              arrTimeTextField.getText(),
              ticketNumTextField.getText(),
              Integer.parseInt(seatNumTextField.getText()),
              Integer.parseInt(ticketIDTextField.getText()));

      if (result == 1) {
         displayAlert(AlertType.INFORMATION, "Ticket Updated",
                 "Ticket updated successfully!");
      }
      else {
         displayAlert(AlertType.ERROR, "Ticket Not Updated",
                 "Unable to updated ticket.\nNOTE: Ticket ID is unique and cannot be changed.");
      }

      getAllEntries();
   }

   @FXML
   void removeEntryButtonPressed(ActionEvent event) {
      int result = ticketQueries.deleteTicket(
              Integer.parseInt(ticketIDTextField.getText()));

      if (result == 1) {
         displayAlert(AlertType.INFORMATION, "Ticket Deleted",
                 "Selected ticket successfully deleted!");
      }
      else {
         displayAlert(AlertType.ERROR, "Ticket Not Deleted",
                 "Unable to delete ticket.");
      }

      getAllEntries();
   }

   // find entries with the specified last name
   @FXML
   void findButtonPressed(ActionEvent event) {

      List<Ticket> tickets = ticketQueries.getTicketByDestinationNameOrNum(
         findByDestinationOrIdTextField.getText() + "%");

      if (tickets.size() > 0) { // display all entries
         ticketList.setAll(tickets);
         selectFirstEntry();
      }
      else {
         displayAlert(AlertType.INFORMATION, "Destination Name/Ticket Number Not Found!",
            "There are no entries with the specified destination or ticket number.");
      }
   }

   // browse all the entries
   @FXML
   void browseAllButtonPressed(ActionEvent event) {
      getAllEntries();
   }

   // display an Alert dialog
   private void displayAlert(
      AlertType type, String title, String message) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setContentText(message);
      alert.showAndWait();
   }
}
