package ticketing;

import java.util.List;

public class SeatHold {
    private static Integer ID = 0;

    private Integer id;
    private String email;
    private String confirmationCode;
    private List<Seat> seats;
    private Boolean isExpired = false;
    private Boolean isReserved = false;
    private AbstractTicketService service;

    public SeatHold(List<Seat> seats, String email, AbstractTicketService service) {
        this.seats = seats;
        this.email = email;
        this.service = service;
        // yes, i know there's an order counting business security vulnerability with such a naive implementation
        id = ID++;
    }

    // a little bit of future-proofing by making this a distinct call instead of part of the constructor,
    // allowing for recursive solutions without spawning additional threads
    public void startExpiry() {
        new Thread(() -> {
            try {
                Thread.sleep(service.getExpirationTime());
            }
            catch (Exception e) {}
            // this is to make sure that the expiration does not occur during the ticketing.TicketService dirty checks
            // this means that the ticketing.TicketService checks will always operate on the expiry state of all SeatHolds
            // at the time of the beginning of the dirty check
            synchronized (service) {
                if (!isReserved)
                    isExpired = true;
            }
        })
            .start();
    }

    public String reserve() {
        synchronized (service) {
            if (isExpired)
                return null;
            isReserved = true;
            return confirmationCode = "" + id.hashCode() + email.hashCode();
        }
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Integer getNumberOfSeats() {
        return seats.size();
    }

    public Boolean isExpired() {
        return isExpired;
    }

    public Boolean isNotExpired() {
        return !isExpired;
    }
}
