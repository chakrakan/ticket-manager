package ticket_crud;

import ticket_crud.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketQueries {
   private static final String URL = "jdbc:derby:lib/ticket_crud;";
   private static final String USERNAME = "user1";
   private static final String PASSWORD = "pass123";

   private Connection connection; // manages connection
   private PreparedStatement selectAllTickets;
   private PreparedStatement getHighestId;
   private PreparedStatement countRecords;
   private PreparedStatement populateDb;
   private PreparedStatement selectTicketByDestinationName;
   private PreparedStatement insertNewTicket;
   private PreparedStatement deleteTicket;
   private PreparedStatement updateTicket;
    
   // constructor
   public TicketQueries() {
      try {
         connection = 
            DriverManager.getConnection(URL, USERNAME, PASSWORD);

         //count all records in db
         countRecords = connection.prepareStatement(
                 "SELECT COUNT(*) FROM tickets"
         );

         // create query that selects all tickets in the database
         selectAllTickets = connection.prepareStatement(
            "SELECT * FROM tickets ORDER BY ticket_id");

         //get the max id of items in tickets table
         getHighestId = connection.prepareStatement(
                 "SELECT MAX(ticket_id) FROM tickets"
         );
         
         // create query that selects entries with destination/ticket number
         // that begin with the specified characters
         selectTicketByDestinationName = connection.prepareStatement(
            "SELECT * FROM tickets WHERE destination LIKE ? OR ticket_num LIKE ?");
         
         // create insert that adds a new ticket into the database
         insertNewTicket = connection.prepareStatement(
            "INSERT INTO tickets " +
            "(ticket_id, destination, dep_time, arr_time, ticket_num, seat_num) " +
            "VALUES (?, ?, ?, ?, ?, ?)");

         //delete ticket from database
         deleteTicket = connection.prepareStatement(
                  "DELETE FROM tickets WHERE ticket_id=?"
          );

          //update ticket in database
         updateTicket = connection.prepareStatement(
                 "UPDATE tickets SET destination=?, dep_time=?, " +
                         "arr_time=?, ticket_num=?, seat_num=? WHERE ticket_id=?"
         );
      } 
      catch (SQLException sqlException)
      {
         sqlException.printStackTrace();
         System.exit(1);
      }
   }
   // select all of the tickets in the database
   public List<Ticket> getAllTickets() {
      // executeQuery returns ResultSet containing matching entries
      try (ResultSet resultSet = selectAllTickets.executeQuery()) {
         List<Ticket> results = new ArrayList<Ticket>();
         
         while (resultSet.next()) {
            results.add(new Ticket(
               resultSet.getInt("ticket_id"),
               resultSet.getString("destination"),
               resultSet.getString("dep_time"),
               resultSet.getString("arr_time"),
               resultSet.getString("ticket_num"),
               resultSet.getInt("seat_num")));
         }

         return results;
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();         
      }
      
      return null;
   }

   //get highest id number
   public int getHighestId() {
      try {
         ResultSet highestId = getHighestId.executeQuery();
         highestId.next();
         int id = highestId.getInt("1");
         highestId.close();
         return id;
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
         return 0;
      }
   }

   //check for records and populate db
   public void populateDb() {
      try {
         int recordCount = countRecords();
         int maxId = getHighestId();
         if (recordCount < 5) {
            //populate db if count of records less than 5
            populateDb = connection.prepareStatement(
                    "INSERT INTO tickets (ticket_id, destination, dep_time, arr_time, ticket_num, seat_num) VALUES" +
                            "(" + ++maxId + ", 'Kolkata, India', '12:55', '17:30', 'KO42', 12)," +
                            "(" + ++maxId + ", 'Delhi, India', '10:00', '12:30', 'DD10', 24)," +
                            "(" + ++maxId + ", 'Durban, South Africa', '9:00', '10:30', 'SA89', 72)," +
                            "(" + ++maxId + ", 'Havana, Cuba', '19:25', '6:30', 'HV65', 26)," +
                            "(" + ++maxId + ", 'Rio de Janerio, Brazil', '7:30', '14:50', 'RI52', 3)," +
                            "(" + ++maxId + ", 'Kathmandu, Nepal', '15:10', '2:30', 'KA24', 89)," +
                            "(" + ++maxId + ", 'Auckland, New Zealand', '20:55', '11:30', 'AU24', 15)," +
                            "(" + ++maxId + ", 'Warsaw, Poland', '4:35', '19:30', 'WA90', 22)"
            );
         }
         populateDb.executeUpdate();
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
      }
   }

   public int countRecords() {
      try {
         ResultSet countQuery = countRecords.executeQuery();
         countQuery.next();
         int recordCount = countQuery.getInt("1");
         countQuery.close();
         return recordCount;
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
         return 0;
      }
   }

   // update a ticket
   public int updateTicket(String destName, String depTime, String arrTime,
                        String ticketNum, int seatNum, int ticketId) {

      // update the ticket
      try {
         // set parameters
         updateTicket.setString(1, destName);
         updateTicket.setString(2, depTime);
         updateTicket.setString(3, arrTime);
         updateTicket.setString(4, ticketNum);
         updateTicket.setInt(5, seatNum);
         updateTicket.setInt(6, ticketId);

         return updateTicket.executeUpdate();
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
         return 0;
      }
   }

   public int deleteTicket(int ticketId) {
      try {
         deleteTicket.setInt(1, ticketId);
         return deleteTicket.executeUpdate();
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
         return 0;
      }
   }
   
   // select ticket by Destination Name or Ticket Number
   public List<Ticket> getTicketByDestinationNameOrNum(String destinationOrNum) {
      try {
         selectTicketByDestinationName.setString(1, destinationOrNum);
         selectTicketByDestinationName.setString(2, destinationOrNum);
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
         return null;
      }

      // executeQuery returns ResultSet containing matching entries
      try (ResultSet resultSet = selectTicketByDestinationName.executeQuery()) {
         List<Ticket> results = new ArrayList<Ticket>();

         while (resultSet.next()) {
            results.add(new Ticket(
                    resultSet.getInt("ticket_id"),
                    resultSet.getString("destination"),
                    resultSet.getString("dep_time"),
                    resultSet.getString("arr_time"),
                    resultSet.getString("ticket_num"),
                    resultSet.getInt("seat_num")));
         } 

         return results;
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
         return null;
      } 
   }
   
   // add an entry
   public int addTicket(int ticketId, String destName, String depTime, String arrTime,
      String ticketNum, int seatNum) {
      
      // insert the new entry; returns # of rows updated
      try {
         // set parameters
         insertNewTicket.setInt(1, ticketId);
         insertNewTicket.setString(2, destName);
         insertNewTicket.setString(3, depTime);
         insertNewTicket.setString(4, arrTime);
         insertNewTicket.setString(5, ticketNum);
         insertNewTicket.setInt(6, seatNum);

         return insertNewTicket.executeUpdate();
      }
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
         return 0;
      }
   }
   
   // close the database connection
   public void close() {
      try {
         connection.close();
      } 
      catch (SQLException sqlException) {
         sqlException.printStackTrace();
      } 
   }
}
 