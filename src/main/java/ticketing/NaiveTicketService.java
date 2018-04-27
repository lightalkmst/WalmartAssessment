package ticketing;

import java.util.LinkedList;
import java.util.List;

/**
 * For this implementation, we will assume that all of the seats are in a single file, are equally desirable, and the
 * user wants only contiguous seating for the entire party, or none at all
 */
public class NaiveTicketService extends AbstractTicketService {
    private Integer seats;

    public NaiveTicketService(Integer seats, Integer expirationTime) {
        this.seats = seats - 1;
        this.expirationTime = expirationTime;
    }

    @Override
    public int numSeatsAvailable() {
        clearExpiredSeatHolds();
        Integer occupied =
            seatHolds.stream()
                .map(SeatHold::getNumberOfSeats)
                .reduce(0, (a, x) -> a + x);
        return seats + 1 - occupied;
    }

    private class SeatRange {
        private Integer start;
        private Integer end;

        public SeatRange(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        public Integer getStart() {
            return start;
        }

        public Integer getEnd() {
            return end;
        }

        public Integer getNumberOfSeats() {
            return end - start + 1;
        }
    }

    // assumes the user only wants contiguous seating for the entire party
    // not efficient; just simple
    // (i'm reminded of how bulky java is and how poorly the libraries are designed)
    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        clearExpiredSeatHolds();
        if (numSeats > numSeatsAvailable())
            return null;
        List<SeatRange> openings = new LinkedList<>();
        openings.add(new SeatRange(0, seats));
        seatHolds.stream()
            .map(x -> {
                Integer seat = x.getSeats().get(0).getSeat();
                return new SeatRange(seat, seat + x.getNumberOfSeats());
            })
            .forEach(x -> {
                SeatRange toSplit =
                    openings.stream()
                        .filter(y -> y.getStart() <= x.getStart() && y.getEnd() >= x.getEnd())
                        .findAny()
                        .get();
                openings.remove(toSplit);
                openings.add(new SeatRange(toSplit.getStart(), x.getStart()));
                openings.add(new SeatRange(x.getEnd(), toSplit.getEnd()));
            });
        SeatRange seating =
            openings.stream()
                .filter(x -> x.getNumberOfSeats() >= numSeats)
                .findFirst()
                .orElse(null);
        if (seating == null)
            return null;
        List<Seat> seats = new LinkedList<>();
        for (int i = 0; i < numSeats; i++) {
            seats.add(new Seat(0, 0, i + seating.getStart()));
        }
        SeatHold hold = new SeatHold(seats, customerEmail, this);
        seatHolds.add(hold);
        hold.startExpiry();
        return hold;
    }
}
