import ticketing.AbstractTicketService;
import ticketing.BetterTicketService;
import ticketing.NaiveTicketService;
import ticketing.NotImplementedException;
import ticketing.SeatHold;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String INVALID_INPUT_TEXT = "Invalid input provided. Please try again.";
    private static final String EMAIL_REGEX = "";

    private final Scanner scanner = new Scanner(System.in);

    private void printLines(String... lines) {
        Arrays.stream(lines)
            .forEach(System.out::println);
    }

    private String readLine() {
        return scanner.nextLine();
    }

    private NaiveTicketService configureNaiveTicketService() {
        // other languages have multiple native ways to prevent duplication like this without resorting to collections
        // even across multiple functions
        Integer seats = null;
        while (seats == null) {
            printLines("How many seats are in the venue?");
            String input = readLine();
            if (input.equals(""))
                System.exit(0);
            try {
                seats = Integer.parseInt(input);
            }
            catch (Exception e) {
                printLines(INVALID_INPUT_TEXT);
            }
        }
        Integer expirationTime = null;
        while (expirationTime == null) {
            printLines("How many seconds is the seat holding timeout?");
            String input = readLine();
            if (input.equals(""))
                System.exit(0);
            try {
                expirationTime = Integer.parseInt(input);
            }
            catch (Exception e) {
                printLines(INVALID_INPUT_TEXT);
            }
        }
        return new NaiveTicketService(seats, expirationTime);
    }

    // TODO: left as an exercise to the examiner
    private BetterTicketService configureBetterTicketService() {
        throw new NotImplementedException();
    }

    private AbstractTicketService pickTicketService() {
        while (true) {
            printLines(
                "Which Ticketing service would you like to use?",
                "1) Naive",
                "2) Better"
            );
            switch (readLine()) {
                case "1":
                    return configureNaiveTicketService();
                case "2":
                    return configureBetterTicketService();
                case "":
                    System.exit(0);
                default:
                    printLines(INVALID_INPUT_TEXT);
                    break;
            }
        }
    }

    private void checkHowManySeatsAreAvailable(AbstractTicketService service) {
        printLines("There are " + service.numSeatsAvailable() + " seats available");
    }

    private void findAndHoldSeating(AbstractTicketService service) {
        Integer seats = null;
        while (seats == null) {
            printLines("How many seats will you hold?");
            String input = readLine();
            if (input.equals(""))
                System.exit(0);
            try {
                seats = Integer.parseInt(input);
            }
            catch (Exception e) {
                printLines(INVALID_INPUT_TEXT);
            }
        }
        String email = null;
        while (email == null) {
            printLines("What is your email address?");
            String input = readLine();
            switch (input) {
                case "":
                    System.exit(0);
                default:
                    if (input.matches(EMAIL_REGEX))
                        email = input;
                    else
                        printLines(INVALID_INPUT_TEXT);
                    break;
            }
        }
        SeatHold hold = service.findAndHoldSeats(seats, email);
        printLines("Your holding id is " + hold.getId());
    }

    private void confirmAReservation(AbstractTicketService service) {
        Integer id = null;
        while (id == null) {
            printLines("What is the holding id?");
            String input = readLine();
            if (input.equals(""))
                System.exit(0);
            try {
                id = Integer.parseInt(input);
            }
            catch (Exception e) {
                printLines(INVALID_INPUT_TEXT);
            }
        }
        String email = null;
        while (email == null) {
            printLines("What is the holding email address?");
            String input = readLine();
            switch (input) {
                case "":
                    System.exit(0);
                default:
                    if (input.matches(EMAIL_REGEX))
                        email = input;
                    else
                        printLines(INVALID_INPUT_TEXT);
                    break;
            }
        }
        String confirmationId = service.reserveSeats(id, email);
        printLines("Your reservation id is " + confirmationId);
    }

    private void runTicketService(AbstractTicketService service) {
        while (true) {
            printLines(
                "Which action would you like to do?",
                "1) Check how many seats are available",
                "2) Find and hold seating",
                "3) Confirm a reservation"
            );
            switch (readLine()) {
                case "1":
                    checkHowManySeatsAreAvailable(service);
                    break;
                case "2":
                    findAndHoldSeating(service);
                    break;
                case "3":
                    confirmAReservation(service);
                    break;
                case "":
                    System.exit(0);
                default:
                    printLines(INVALID_INPUT_TEXT);
                    break;
            }
        }
    }

    public void main() {
        printLines(
            "|-----------------------------------|",
            "|                                   |",
            "|  Ticket Service Coding Challenge  |",
            "|                                   |",
            "|-----------------------------------|",
            "Empty inputs will terminate the program"
        );
        runTicketService(pickTicketService());
    }

    public static void main(String[] argv) {
        new Main().main();
    }
}
