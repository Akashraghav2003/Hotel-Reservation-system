import java.util.*;
import java.sql.*;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Akash@2003";

    private static void reserveRoom(Connection connection, Scanner scanner, Statement statement) {
        System.out.print("Enter the guest name: ");
        String guest_name = scanner.next();
        scanner.nextLine(); // Consume the newline character after the guest name

        System.out.print("Enter the room number: ");
        int room_no;
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid room number:");
            scanner.next(); // Discard invalid input
        }
        room_no = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter the contact number: ");
        String contact_no = scanner.nextLine();

        String sql = "INSERT INTO reservation (guest_name, room_no, contact_no) VALUES ('" + guest_name + "', '" + room_no + "', '" + contact_no + "')";

        try {
            int affectRow = statement.executeUpdate(sql);
            if (affectRow > 0) {
                System.out.println("Reservation successful.");
            } else {
                System.out.println("Reservation failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection connection,Scanner scanner ,Statement statement) {
        
    	
    	String sql = "SELECT * FROM reservation ;";

        try {
            ResultSet rSet = statement.executeQuery(sql);
            System.out.println("Current Reservations:");
            System.out.println("+----------------+--------------+------------+----------------+----------------+----------+");
            System.out.println("| Reservation ID | Guest        | Room Number | Contact Number | Reservation Date           |");
            System.out.println("+----------------+--------------+------------+----------------+----------------+----------+");

            while (rSet.next()) {
                int reservation_id = rSet.getInt("reservation_id");
                String guest_name = rSet.getString("guest_name");
                int room_no = rSet.getInt("room_no");
                String contact_no = rSet.getString("contact_no");
                String reservation_date = rSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-12s | %-11d | %-15s | %-25s |\n",
                        reservation_id, guest_name, room_no, contact_no, reservation_date);
            }
            System.out.println("+----------------+--------------+------------+----------------+----------------+----------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getRoomNo(Connection connection, Statement statement, Scanner scanner) {
        System.out.print("Enter the reservation ID: ");
        int reservation_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter the guest name: ");
        String guest_name = scanner.nextLine();

        String sql = "SELECT room_no FROM reservation WHERE reservation_id = " + reservation_id + " AND guest_name = '" + guest_name + "'";

        try {
            ResultSet rSet = statement.executeQuery(sql);
            if (rSet.next()) {
                System.out.println("Your room number is " + rSet.getInt("room_no"));
            } else {
                System.out.println("Reservation not found. Please check the details.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReservation(Connection connection, Scanner scanner, Statement statement) {
        System.out.print("Enter the reservation ID to update: ");
        int reservation_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (!reservationExists(connection, reservation_id)) {
            System.out.println("Reservation not found for the given ID.");
            return;
        }

        System.out.print("Enter the new guest name: ");
        String newGuestName = scanner.nextLine();

        System.out.print("Enter the new room number: ");
        int newRoomNo;
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid room number:");
            scanner.next(); // Discard invalid input
        }
        newRoomNo = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter the new contact number: ");
        String newContactNo = scanner.nextLine();

        String sql = "UPDATE reservation SET guest_name = '" + newGuestName + "', room_no = " + newRoomNo + ", contact_no = '" + newContactNo + "' WHERE reservation_id = " + reservation_id;

        try {
            int affectRow = statement.executeUpdate(sql);
            if (affectRow > 0) {
                System.out.println("Reservation updated successfully.");
            } else {
                System.out.println("Reservation update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteReservation(Connection connection, Scanner scanner, Statement statement) {
        System.out.print("Enter the reservation ID: ");
        int reservation_id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (!reservationExists(connection, reservation_id)) {
            System.out.println("Reservation not found.");
            return;
        }

        String sql = "DELETE FROM reservation WHERE reservation_id = " + reservation_id;

        try {
            int affectedRows = statement.executeUpdate(sql);
            if (affectedRows > 0) {
                System.out.println("Reservation deleted successfully!");
            } else {
                System.out.println("Reservation deletion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = " + reservationId;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while (i != 0) {
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Reservation System!!!");
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reserve a room.");
                System.out.println("2. Get room number.");
                System.out.println("3. Update reservation.");
                System.out.println("4. Delete reservation.");
                System.out.println("5. View reservation.");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner, statement);
                        break;
                    case 2:
                        getRoomNo(connection, statement, scanner);
                        break;
                    case 3:
                        updateReservation(connection, scanner, statement);
                        break;
                    case 4:
                        deleteReservation(connection, scanner, statement);
                        break;
                    case 5:
                        viewReservation(connection, scanner,statement);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
