package Domain;

import java.util.Date;

/**
 * Created by rouslankhayaouri on 17/12/15.
 */
public class Ticket {
    private int ticketNumber;
    private int accountId;
    private String text;
    private Date dateOpened;
    private State ticketstate;
    private String deviceName;

    public Ticket(int ticketNumber, int accountId, String text, Date dateOpened, State ticketstate, String deviceName) {
        this.ticketNumber = ticketNumber;
        this.accountId = accountId;
        this.text = text;
        this.dateOpened = dateOpened;
        this.ticketstate = ticketstate;
        this.deviceName = deviceName;
    }
    public int getTicketNumber() {
        return ticketNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getText() {
        return text;
    }

    public Date getDateOpened() {
        return dateOpened;
    }

    public State getTicketstate() {
        return ticketstate;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketNumber=" + ticketNumber +
                ", accountId=" + accountId +
                ", text='" + text + '\'' +
                ", dateOpened=" + dateOpened +
                ", ticketstate=" + ticketstate +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }
}
