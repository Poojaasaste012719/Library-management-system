import java.sql.*;
import java.time.LocalDate;

public class IssueDAO {

    public static void issueBook(int studentId, int bookId) {
        try {
            Connection con = DBConnection.getConnection();

            // Check if book available
            String check = "SELECT quantity FROM books WHERE book_id=?";
            PreparedStatement ps = con.prepareStatement(check);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next() || rs.getInt("quantity") <= 0) {
                System.out.println("Book Not Available!");
                return;
            }

            // Reduce quantity
            String updateQty = "UPDATE books SET quantity = quantity - 1 WHERE book_id=?";
            PreparedStatement update = con.prepareStatement(updateQty);
            update.setInt(1, bookId);
            update.executeUpdate();

            // Insert in issued table
            String query = "INSERT INTO issued_books(student_id, book_id, issue_date) VALUES (?, ?, ?)";
            PreparedStatement issue = con.prepareStatement(query);
            issue.setInt(1, studentId);
            issue.setInt(2, bookId);
            issue.setDate(3, Date.valueOf(LocalDate.now()));
            issue.executeUpdate();

            System.out.println("Book Issued Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void returnBook(int issueId) {
        try {
            Connection con = DBConnection.getConnection();

            // Find book id from issue id
            String find = "SELECT book_id FROM issued_books WHERE issue_id=?";
            PreparedStatement ps = con.prepareStatement(find);
            ps.setInt(1, issueId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid Issue ID!");
                return;
            }

            int bookId = rs.getInt("book_id");

            // Update return date
            String updateReturn = "UPDATE issued_books SET return_date=? WHERE issue_id=?";
            PreparedStatement ret = con.prepareStatement(updateReturn);
            ret.setDate(1, Date.valueOf(LocalDate.now()));
            ret.setInt(2, issueId);
            ret.executeUpdate();

            // Increase book quantity
            String updateQty = "UPDATE books SET quantity = quantity + 1 WHERE book_id=?";
            PreparedStatement update = con.prepareStatement(updateQty);
            update.setInt(1, bookId);
            update.executeUpdate();

            System.out.println("Book Returned Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void viewIssuedBooks() {
        String query = "SELECT * FROM issued_books";

        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n--- Issued Books ---");
            while (rs.next()) {
                System.out.println("Issue ID: " + rs.getInt("issue_id") +
                                   ", Student: " + rs.getInt("student_id") +
                                   ", Book: " + rs.getInt("book_id") +
                                   ", Issue Date: " + rs.getDate("issue_date") +
                                   ", Return Date: " + rs.getDate("return_date"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
