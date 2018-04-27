package ticketing;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTicketService implements TicketService {
    protected Integer expirationTime = 0;
    protected List<SeatHold> seatHolds = new LinkedList<>();

    protected synchronized void clearExpiredSeatHolds() {
        seatHolds =
            seatHolds.stream()
                .filter(SeatHold::isNotExpired)
                .collect(Collectors.toList());
    }

    public Integer getExpirationTime() {
        return expirationTime * 1000;
    }

    public String reserveSeats(int seatHoldId, String customerEmail) {
        clearExpiredSeatHolds();
        SeatHold hold =
            seatHolds.stream()
                // java appears to be the only language that doesn't have .find
                // so weird!
                .filter(x -> x.getId() == seatHoldId && x.getEmail().equals(customerEmail))
                .findAny()
                .orElse(null);
        if (hold == null)
            return null;
        return hold.reserve();
    }
}
