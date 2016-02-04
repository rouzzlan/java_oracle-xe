package DAL;

import Domain.State;
import Domain.Ticket;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rouslankhayaouri on 17/12/15.
 */
public class SqlClient {
    private final String url;
    private final String username;
    private final String password;
    private DateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");


    public SqlClient(String host, String username, String password) {
        this.username = username;
        this.password = password;
        this.url = String.format("jdbc:oracle:thin:@%s:1521:xe", host);
    }

    public void addTicketProcedure(Ticket ticket){
        Connection connection = getConnection();
        String jobquerry = "BEGIN add_ticket(?, ?, ?, ?, ?); END;";
        try {
            CallableStatement callableStatement = connection.prepareCall(jobquerry);
            callableStatement.setInt(1, ticket.getTicketNumber());
            callableStatement.setInt(2, ticket.getAccountId());
            callableStatement.setString(3, ticket.getText());
            callableStatement.setInt(4, stateToInt(ticket.getTicketstate()));
            callableStatement.setString(5, ticket.getDeviceName());
            callableStatement.execute();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public List<Ticket> readTickets() {
        ResultSet set;
        String sql = "SELECT * FROM ticket";
        List<Ticket> ticketList = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            set = statement.executeQuery(sql);
            while (set.next()) {
                ticketList.add(new Ticket(set.getInt(1), set.getInt(2), set.getString(3), set.getDate(4), intToSate(set.getInt(5)), set.getString(6)));
            }
            set.close();
            sluitStatementEnConnectie(statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticketList;
    }

    public boolean createTicket(Ticket ticket) {

        int ticketNum = ticket.getTicketNumber();
        int id = ticket.getAccountId();
        String text = ticket.getText();
        String datum = df.format(ticket.getDateOpened());
        int state = stateToInt(ticket.getTicketstate());
        String device = ticket.getDeviceName();

        String sql = String.format("INSERT INTO TICKET VALUES('%d', '%d', '%s', SYSDATE,%d, '%s')",
                ticketNum, id, text, state, device);
        //String sql = "INSERT INTO TICKET VALUES('5', '5', 'test test', SYSDATE,1, 'pc')";
        executeSql_C_UD(sql);
        return true;
    }

    public Ticket readTicket(int ticketNumber) {
        ResultSet set;
        String sql = String.format("SELECT * FROM TICKET WHERE TC_TICKET_NUMBER='%d'", ticketNumber);
        Ticket ticket = null;
        //Todo readTicket methode die een set retourneert
        try {
            set = createStatement().executeQuery(sql);
            if (set != null) {
                set.next();
                ticket = new Ticket(set.getInt(1), set.getInt(2), set.getString(3), set.getDate(4), intToSate(set.getInt(5)), set.getString(6));
                set.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public boolean UpdateTicket(Ticket ticket) {
        int ticketNum = ticket.getTicketNumber();
        int id = ticket.getAccountId();
        String text = ticket.getText();
        String datum = df.format(ticket.getDateOpened());
        int state = stateToInt(ticket.getTicketstate());
        String device = ticket.getDeviceName();
        String sql = String.format("UPDATE TICKET" +
                " SET" +
                " TC_ACCOUNT_ID = '%d'," +
                " TC_TEXT='%s'," +
                " TC_DATE_OPENED=SYSDATE," +
                " TC_STATE='%d'," +
                " TC_DEVICE_NAME='%s'" +
                " where TC_TICKET_NUMBER = %d", id, text, state, device, ticketNum);
        System.out.println(sql);
        executeSql_C_UD(sql);
        return true;
    }

    public boolean deleteTicket(int ticketNumber) {
        String sql = String.format("DELETE FROM TICKET \n" +
                "WHERE TC_TICKET_NUMBER = %d", ticketNumber);
        executeSql_C_UD(sql);
        return true;
    }

    private void executeSql_C_UD(String sql) {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            sluitStatementEnConnectie(statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Connection getConnection() {
        String driver = "oracle.jdbc.driver.OracleDriver";
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Statement createStatement() {
        Statement statement = null;
        try {
            statement = getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No connection found");
        }
        return statement;
    }

    private int stateToInt(State state) {
        switch (state) {
            case OPEN:
                return 1;
            case ANSWERED:
                return 2;
            case CLIENTANSWER:
                return 3;
            case CLOSED:
                return 4;
            default:
                return 0;
        }
    }

    private State intToSate(int state) {
        switch (state) {
            case 1:
                return State.OPEN;
            case 2:
                return State.ANSWERED;
            case 3:
                return State.CLIENTANSWER;
            case 4:
                return State.CLOSED;
            default:
                throw new IllegalArgumentException("foutief int state");
        }
    }

    private void sluitStatementEnConnectie(Statement statement, Connection connection) {
        try {
            statement.execute("COMMIT");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Cannot execute CHECKPOINT or close statement or close connection");
        }
    }

    public void sluitConnectie() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
