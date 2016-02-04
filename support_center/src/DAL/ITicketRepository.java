package DAL;

import Domain.Ticket;
import Domain.TicketResponse;

import java.sql.ResultSet;

/**
 * Created by rouslankhayaouri on 17/12/15.
 */
public interface ITicketRepository {
    ResultSet readTickets();
    boolean CreateTicket(Ticket ticket);
    Ticket readTicket(int ticketNumber);
    boolean UpdateTicket(Ticket ticket);
    boolean updateTicketStateToClosed(int ticketNumber);
    boolean DeleteTicket(int ticketNumber);

    ResultSet readTicketResponsesOfTicket(int ticketNumber);
    // CRUD TicketResponse
    TicketResponse createTicketResponse(TicketResponse response);
    TicketResponse readTicketResponse(int id);
    void updateTicketResponse(TicketResponse response);
    void deleteTicketResponse(int id);
}
