import DAL.SqlClient;
import Domain.State;
import Domain.Ticket;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by rouslankhayaouri on 03/01/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSqlClientConnection {

    private SqlClient client = new SqlClient("192.168.0.185", "support_center", "support_center");

    @Test
    public void A_InitialNumOfTickets(){
        client.deleteTicket(5);
        client.deleteTicket(6);
        long count = client.readTickets().stream().count();
        assertEquals("aantal tickets moet <3> zijn", 4, count, 0);

    }
    @Test
    public void B_VoegEenTicketToe(){
        Ticket ticket = new Ticket(5, 5, "test test", new Date(1990, 10, 12), State.OPEN, "pc");
        client.createTicket(ticket);
        boolean containsNum5 = client.readTickets().stream().anyMatch(t -> t.getTicketNumber() == 5);
        assertTrue("De database moet ticket nummer 5 bevatten",containsNum5);
    }
    @Test
    public void C_VerwijderTicket(){
        client.deleteTicket(5);
        boolean containsNum5 = client.readTickets().stream().anyMatch(t -> t.getTicketNumber() == 5);
        assertFalse("De database mag de ticket nummer 5 niet bevatten",containsNum5);
    }
    @Test
    public void D_ReadTicket(){
        Ticket ticket1 = client.readTicket(1);
        int accId = ticket1.getAccountId();
        String text = "Cannot login on webmail";
        State state = ticket1.getTicketstate();
        boolean Iscorrect = (accId == 1 && text.equalsIgnoreCase("Cannot login on webmail") && state == State.CLOSED);
        assertTrue("Ticket moet de correcte waarden bevatten", Iscorrect);
    }
    @Test
    public void E_UpdateTicket(){
        Ticket ticket = new Ticket(6, 6, "test update", new Date(), State.OPEN, "pc");
        client.createTicket(ticket);
        boolean containsNum6 = client.readTickets().stream().anyMatch(t -> t.getTicketNumber() == 6);
        assertTrue("De database moet ticket nummer 6 bevatten",containsNum6);
        ticket = new Ticket(6, 6, "update is tested", new Date(), State.CLOSED, "pc");
        client.UpdateTicket(ticket);
        Ticket ticket1 = client.readTicket(6);
        boolean isUpdated = ticket1.getText().equals("update is tested") && ticket1.getTicketstate() == State.CLOSED;
        assertTrue("De ticket moet geupdated zijn", isUpdated);
        client.deleteTicket(6);
    }
    @Test
    public void F_CreateTicketUsingPorcedure(){
        Ticket ticket = new Ticket(8, 6, "test procedure", new Date(), State.OPEN, "mac");
        client.addTicketProcedure(ticket);
        Ticket ticket1 = client.readTicket(8);
        boolean isUpdated = ticket1.getText().equals("test procedure");
        assertTrue("De ticket moet geupdated zijn", isUpdated);
        client.deleteTicket(8);
    }
}
