import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    static Logger log = LoggerFactory.getLogger(DBUtil.class);

    public final static String url = "jdbc:mysql://localhost:3306/amzlist";
    public final static String username = "sa";
    public final static String password = "sa";

    public static Connection getConnection() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return con;
    }

    public static void createTable(Connection con){
        try(Statement stmt = con.createStatement()){
            String tableSql = "CREATE TABLE IF NOT EXISTS amzBookList"
                    + "(id int PRIMARY KEY AUTO_INCREMENT, imagethumb varchar(255),"
                    + "title varchar(255), bookdetails varchar(255), rating varchar(255),"
                    + "source varchar(255), price varchar(255))";
            stmt.execute(tableSql);

            log.info("Table amzBookList created");

        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public static void deleteTable(Connection con){
        try(Statement stmt = con.createStatement()){
            String tableSql = "DROP TABLE IF EXISTS amzBookList";
            stmt.execute(tableSql);
            log.info("Table amzBookList deleted");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public static void insertData(Connection con, AmzBookItem item){
        try(Statement stmt = con.createStatement()){
            String tableSql = "INSERT INTO amzBookList (imagethumb, title, bookdetails, rating, source, price) " +
                    "VALUES (\'" + item.getImageThumb() + "\', " +
                    "\'" + item.getTitle() + "\', " +
                    "\'" + item.getDetails() + "\', " +
                    "\'" + item.getRating() + "\', " +
                    "\'" + item.getSource() + "\', " +
                    "\'" + item.getPrice() + "\');";
            stmt.execute(tableSql);
            log.info("Insert data success! " + item.getTitle());
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

}
