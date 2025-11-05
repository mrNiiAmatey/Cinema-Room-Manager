import java.util.Scanner;

/**
 * Cinema Seat Booking Management System
 * 
 * A console-based application for managing cinema seat reservations and tracking revenue.
 * Features include seat layout visualization, ticket purchasing with dynamic pricing,
 * and comprehensive statistical reporting.
 * 
 * @author Nii Amatey Tagoe
 * @version 1.0
 * @since 2024
 */
public class Cinema {

    /**
     * Main entry point for the cinema booking management system.
     * Initializes the cinema seating configuration and presents an interactive menu
     * for seat management operations.
     * 
     * The application supports:
     * - Dynamic cinema sizing based on user input
     * - Real-time seat availability tracking
     * - Revenue calculation and reporting
     * - User-friendly menu-driven interface
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        // Initialize cinema configuration
        System.out.println("Enter the number of rows:");
        int numCinemaRows = scn.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int numSeatsPerRow = scn.nextInt();
        
        // Create seating matrix: 0 = available, 1 = booked
        int[][] cinemaSeating = new int[numCinemaRows][numSeatsPerRow];

        boolean programRunning = true;

        // Main application loop
        while (programRunning) {
            displayMenu();
            int choice = scn.nextInt();

            switch (choice) {
                case 1:
                    // Display current seating arrangement
                    cinemaLayout(cinemaSeating);
                    break;

                case 2:
                    // Process ticket purchase
                    buyCinemaTicket(scn, cinemaSeating, numCinemaRows, numSeatsPerRow);
                    break;

                case 3:
                    // Generate sales and occupancy report
                    cinemaStatistics(cinemaSeating, numCinemaRows, numSeatsPerRow);
                    break;

                case 0:
                    // Terminate application
                    programRunning = false;
                    break;

                default:
                    System.out.println("Enter a valid number");
            }
        }

        scn.close();
    }

    /**
     * Displays the main navigation menu.
     * Provides users with available system operations.
     */
    private static void displayMenu() {
        System.out.println("\n1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    /**
     * Renders the cinema seating layout in a visual grid format.
     * Displays column headers (seat numbers), row headers (row numbers),
     * and seat status indicators.
     * 
     * Legend:
     * - 'S' represents an available seat
     * - 'B' represents a booked seat
     *
     * @param myArray 2D integer array representing the seating grid
     *                where 0 = available and 1 = booked
     */
    public static void cinemaLayout(int[][] myArray) {
        System.out.println("Cinema:");
        
        // Render column headers (seat numbers)
        System.out.print("  ");
        for (int j = 1; j <= myArray[0].length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();

        // Render each row with row number and seat status
        for (int i = 0; i < myArray.length; i++) {
            // Print row identifier
            System.out.print((i + 1) + " ");
            
            // Print seat status for current row
            for (int j = 0; j < myArray[i].length; j++) {
                System.out.print(myArray[i][j] == 1 ? "B " : "S ");
            }
            System.out.println();
        }
    }

    /**
     * Calculates the maximum potential revenue if all seats are sold.
     * Implements tiered pricing strategy based on cinema size:
     * 
     * - Small venues (≤60 seats): Uniform $10 pricing
     * - Large venues (>60 seats): $10 for front half, $8 for back half
     * 
     * For odd row counts, the front half receives fewer rows
     * (e.g., 9 rows → 4 front rows at $10, 5 back rows at $8)
     *
     * @param numRows total number of rows in the cinema
     * @param numSeats number of seats per row
     * @return total potential revenue in dollars
     */
    public static int totalIncome(int numRows, int numSeats) {
        int totalSeats = numRows * numSeats;
        
        // Small cinema: uniform pricing model
        if (totalSeats <= 60) {
            return totalSeats * 10;
        }
        // Large cinema: tiered pricing model
        else {
            int frontRows = numRows / 2;
            int backRows = numRows - frontRows;
            return (frontRows * numSeats * 10) + (backRows * numSeats * 8);
        }
    }

    /**
     * Determines the ticket price for a specific seat based on cinema size
     * and row location.
     * 
     * Pricing Strategy:
     * - Cinemas ≤60 seats: $10 per ticket (all seats)
     * - Cinemas >60 seats: $10 (front half rows), $8 (back half rows)
     * 
     * @param rows total number of rows in the cinema
     * @param seats number of seats per row
     * @param currentRow the selected row number (1-indexed)
     * @return ticket price in dollars (10 or 8)
     */
    public static int ticketPrice(int rows, int seats, int currentRow) {
        int totalNumSeats = rows * seats;

        // Small cinema: uniform pricing
        if (totalNumSeats <= 60) {
            return 10;
        }
        // Large cinema: location-based pricing
        else {
            int frontHalfRows = rows / 2;
            return (currentRow <= frontHalfRows) ? 10 : 8;
        }
    }

    /**
     * Processes a ticket purchase transaction with comprehensive validation.
     * 
     * Validation includes:
     * - Boundary checking for row and seat numbers
     * - Duplicate booking prevention
     * - Exception handling for invalid input
     * 
     * Upon successful purchase, updates the seating matrix and displays
     * the calculated ticket price.
     *
     * @param scn Scanner object for user input
     * @param cinemaSeating 2D array tracking seat availability
     * @param numRows total number of rows in cinema
     * @param numSeats total number of seats per row
     */
    public static void buyCinemaTicket(Scanner scn, int[][] cinemaSeating, int numRows, int numSeats) {
        System.out.println("Enter a row number:");
        int rowNumber = scn.nextInt();
        System.out.println("Enter a seat number in that row:");
        int seatNumber = scn.nextInt();

        try {
            // Validate seat availability
            if (cinemaSeating[rowNumber - 1][seatNumber - 1] == 1) {
                System.out.println("That ticket has already been purchased!");
                return;
            }

            // Process successful purchase
            int price = ticketPrice(numRows, numSeats, rowNumber);
            System.out.println("Ticket price: $" + price);
            
            // Update seating matrix (convert from 1-indexed to 0-indexed)
            cinemaSeating[rowNumber - 1][seatNumber - 1] = 1;
            
        } catch (ArrayIndexOutOfBoundsException e) {
            // Handle invalid seat selection
            System.out.println("Wrong input!");
        }
    }

    /**
     * Generates comprehensive cinema statistics and revenue report.
     * 
     * Metrics calculated:
     * - Total number of tickets sold
     * - Occupancy rate (percentage of seats filled)
     * - Current revenue from sold tickets
     * - Maximum potential revenue at full capacity
     * 
     * Uses dynamic pricing calculation to ensure accurate revenue reporting
     * based on seat location.
     *
     * @param cinemaSeating 2D array representing current seat bookings
     * @param numRows total number of rows in cinema
     * @param numSeats total number of seats per row
     */
    public static void cinemaStatistics(int[][] cinemaSeating, int numRows, int numSeats) {
        int ticketsPurchased = 0;
        int currIncome = 0;
        int totalSeats = numRows * numSeats;

        // Iterate through seating matrix to calculate metrics
        for (int i = 0; i < cinemaSeating.length; i++) {
            for (int j = 0; j < cinemaSeating[i].length; j++) {
                if (cinemaSeating[i][j] == 1) {
                    ticketsPurchased++;
                    // Calculate revenue based on row-specific pricing
                    currIncome += ticketPrice(numRows, numSeats, i + 1);
                }
            }
        }

        // Calculate occupancy percentage
        double purTicketPercentage = (ticketsPurchased * 100.0) / totalSeats;

        // Display comprehensive statistics report
        System.out.println("Number of purchased tickets: " + ticketsPurchased);
        System.out.printf("Percentage: %.2f%%\n", purTicketPercentage);
        System.out.println("Current income: $" + currIncome);
        System.out.println("Total income: $" + totalIncome(numRows, numSeats));
    }
}
