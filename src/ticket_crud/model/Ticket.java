package ticket_crud.model;

public class Ticket {
    private int ticketID;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private String ticketNumber;
    private int seatNumber;

    public Ticket() {}

    public Ticket(int ticketID, String destination, String departureTime,
                  String arrivalTime, String ticketNumber, int seatNumber) {
        this.ticketID = ticketID;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.ticketNumber = ticketNumber;
        this.seatNumber = seatNumber;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return getTicketNumber() + ", " + getDestination() + ", " +
                getArrivalTime() + ", " + getDepartureTime() + ", " + getSeatNumber();
    }
}
