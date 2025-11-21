import java.sql.*;

public class BookDAO {

    public static void addBook(String title, String author, int qty) {
        String query = "INSERT INTO books(title, author, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, qty);
            ps.executeUpdate();
            System.out.println("Book Added Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void viewBooks() {
        String query = "SELECT * FROM books";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n--- Books List ---");
            while (rs.next()) {
                System.out.println(rs.getInt("book_id") + " | " +
                                   rs.getString("title") + " | " +
                                   rs.getString("author") + " | Qty: " +
                                   rs.getInt("quantity"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
