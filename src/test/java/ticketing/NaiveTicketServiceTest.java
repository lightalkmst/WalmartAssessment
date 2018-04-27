package ticketing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class NaiveTicketServiceTest {
    @Test
    void numSeatsAvailable() {
        Boolean[] tests = {
            new NaiveTicketService(1, 2).numSeatsAvailable() == 1,
            new NaiveTicketService(10, 2).numSeatsAvailable() == 10,
            new NaiveTicketService(100, 2).numSeatsAvailable() == 100,
            new NaiveTicketService(1000, 2).numSeatsAvailable() == 1000
        };
        Arrays.stream(tests)
            .forEach(Assertions::assertTrue);
    }

    private static final String EMAIL1 = "a@ok.com";

    private void findAndHoldSeatsCase1(Integer seats, Integer seats1, Boolean expectation) {
        NaiveTicketService service = new NaiveTicketService(seats, 2);
        SeatHold hold = service.findAndHoldSeats(seats1, EMAIL1);
        if (expectation) {
            Assertions.assertNotNull(hold);
            Assertions.assertEquals(hold.getEmail(), EMAIL1);
            Assertions.assertEquals(hold.getNumberOfSeats(), seats1);
        }
        else {
            Assertions.assertNull(hold);
        }
    }

    private void findAndHoldSeatsCase2(Integer seats, Integer seats1, Integer seats2, Boolean expectation) {
        NaiveTicketService service = new NaiveTicketService(seats, 2);
        service.findAndHoldSeats(seats1, EMAIL1);
        SeatHold hold = service.findAndHoldSeats(seats2, EMAIL1);
        if (expectation) {
            Assertions.assertEquals(hold.getEmail(), EMAIL1);
            Assertions.assertEquals(hold.getNumberOfSeats(), seats2);
        }
        else {
            Assertions.assertNull(hold);
        }
    }

    @Test
    void findAndHoldSeats() {
        // other languages handle this sort of thing so much better with native tuples and transformations
        findAndHoldSeatsCase1(1, 10, false);
        findAndHoldSeatsCase1(10, 10, true);
        findAndHoldSeatsCase1(100, 10, true);
        findAndHoldSeatsCase1(1000, 10, true);
        findAndHoldSeatsCase2(1, 10, 10, false);
        findAndHoldSeatsCase2(10, 10, 10, false);
        findAndHoldSeatsCase2(100, 10, 10, true);
        findAndHoldSeatsCase2(1000, 10, 10, true);
    }

    private void combinationCase(Integer seats, Integer seats1, Integer expectation) {
        NaiveTicketService service = new NaiveTicketService(seats, 2);
        service.findAndHoldSeats(seats1, EMAIL1);
        Assertions.assertEquals(service.numSeatsAvailable(), (int) expectation);
    }

    @Test
    void combination() {
        combinationCase(1, 10, 1);
        combinationCase(10, 10, 0);
        combinationCase(100, 10, 90);
        combinationCase(1000, 10, 990);
    }

    private void timeoutCase(Integer seats, Integer seats1, Integer expectation) {
        NaiveTicketService service = new NaiveTicketService(seats, 2);
        service.findAndHoldSeats(seats1, EMAIL1);
        try {
            Thread.sleep(3000);
        }
        catch (Exception e) {}
        Assertions.assertEquals(service.numSeatsAvailable(), (int) seats);
    }

    @Test
    void timeout() {
        timeoutCase(1, 10, 1);
        timeoutCase(10, 10, 10);
        timeoutCase(100, 10, 100);
        timeoutCase(1000, 10, 1000);
    }
}