import java.sql.*;

public class StudentDAO {

    public static void addStudent(String name, String email) {
        String query = "INSERT INTO students(name, email) VALUES (?, ?)";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
            System.out.println("Student Added Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void viewStudents() {
        String query = "SELECT * FROM students";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n--- Student List ---");
            while (rs.next()) {
                System.out.println(rs.getInt("student_id") + " | " +
                                   rs.getString("name") + " | " +
                                   rs.getString("email"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
