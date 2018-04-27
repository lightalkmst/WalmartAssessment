package ticketing;

public class BetterTicketService extends AbstractTicketService {
    // TODO: left as an exercise to the examiner
    // the other parts of the application are designed to support/streamline additional implementations of TicketService

    @Override
    public int numSeatsAvailable() {
        throw new NotImplementedException();
    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        throw new NotImplementedException();
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        throw new NotImplementedException();
    }
}
