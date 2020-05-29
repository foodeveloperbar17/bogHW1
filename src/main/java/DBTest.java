import java.sql.*;

public class DBTest {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "araqvs");

            Author author = new Author(2, "luka", "agdg");
            addAuthor(author);
            printTable("authors");

            Book book = new Book(2, "vigacam gadaufrina", 10.5, 2);
            addBook(book);
            printTable("books");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            safeClose(connection);
        }
    }

    private static void safeClose(AutoCloseable c){
        if(c != null){
            try{
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void addAuthor(Author author){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT into authors values(?, ?, ?)");
            preparedStatement.setInt(1, author.id);
            preparedStatement.setString(2, author.firstName);
            preparedStatement.setString(3, author.lastName);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            safeClose(preparedStatement);
        }
    }

    private static void addBook(Book book){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT into books values(?, ?, ?, ?)");
            preparedStatement.setInt(1, book.id);
            preparedStatement.setString(2, book.bookName);
            preparedStatement.setDouble(3, book.price);
            preparedStatement.setInt(4, book.authorId);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private static class Author{
        private int id;
        private String firstName;
        private String lastName;

        public Author(int id, String firstName, String lastName){
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    private static class Book{
        private int id;
        private String bookName;
        private double price;
        private int authorId;

        public Book(int id, String bookName, double price, int authorId) {
            this.id = id;
            this.bookName = bookName;
            this.authorId = authorId;
            this.price = price;
        }
    }

    private static void printTable(String tableName){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + tableName);
            while(resultSet.next()){
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " "
                + resultSet.getString(3));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
