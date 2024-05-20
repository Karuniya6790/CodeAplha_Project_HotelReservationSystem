import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Room {
    private int roomNumber;
    private String category;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double pricePerNight, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", category='" + category + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

class Booking {
    private int bookingId;
    private Room room;
    private String customerName;
    private Date startDate;
    private Date endDate;
    private double totalCost;

    public Booking(int bookingId, Room room, String customerName, Date startDate, Date endDate, double totalCost) {
        this.bookingId = bookingId;
        this.room = room;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Room getRoom() {
        return room;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", room=" + room +
                ", customerName='" + customerName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalCost=" + totalCost +
                '}';
    }
}

class Payment {
    public static boolean processPayment(double amount) {
        // Simulate payment processing
        System.out.println("Processing payment of $" + amount);
        return true; // Assume payment is always successful for simplicity
    }
}

public class HotelReservationSystem {
    private List<Room> rooms;
    private List<Booking> bookings;
    private int bookingCounter;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public HotelReservationSystem() {
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        bookingCounter = 1;

        // Initialize some rooms
        rooms.add(new Room(1001, "Single", 100.0, true));
        rooms.add(new Room(1002, "Double", 150.0, true));
        rooms.add(new Room(1003, "Suite", 250.0, true));
        rooms.add(new Room(1004, "Single", 100.0, false));
    }

    public void searchAvailableRooms(String category) {
        for (Room room : rooms) {
            if (room.isAvailable() && room.getCategory().equalsIgnoreCase(category)) {
                System.out.println(room);
            }
        }
    }

    public void makeReservation(String customerName, int roomNumber, Date startDate, Date endDate) {
        Room roomToBook = null;
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber && room.isAvailable()) {
                roomToBook = room;
                break;
            }
        }

        if (roomToBook != null) {
            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            long diff = diffInMillies / (1000 * 60 * 60 * 24);
            double totalCost = diff * roomToBook.getPricePerNight();

            if (Payment.processPayment(totalCost)) {
                Booking booking = new Booking(bookingCounter++, roomToBook, customerName, startDate, endDate, totalCost);
                bookings.add(booking);
                roomToBook.setAvailable(false);
                System.out.println("Reservation successful! " + booking);
            } else {
                System.out.println("Payment failed. Reservation not completed.");
            }
        } else {
            System.out.println("Room is not available or does not exist.");
        }
    }

    public void viewBookings() {
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }

    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Search available rooms");
            System.out.println("2. Make a reservation");
            System.out.println("3. View bookings");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter room category to search (Single, Double, Suite): ");
                    String category = scanner.nextLine();
                    system.searchAvailableRooms(category);
                    break;
                case 2:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter room number to book: ");
                    int roomNumber = scanner.nextInt();
                    System.out.print("Enter start date (yyyy-mm-dd): ");
                    String startDateStr = scanner.next();
                    System.out.print("Enter end date (yyyy-mm-dd): ");
                    String endDateStr = scanner.next();
                    try {
                        Date startDate = dateFormat.parse(startDateStr);
                        Date endDate = dateFormat.parse(endDateStr);
                        system.makeReservation(customerName, roomNumber, startDate, endDate);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format.");
                    }
                    break;
                case 3:
                    system.viewBookings();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again later.");
            }
        }
    }
}
