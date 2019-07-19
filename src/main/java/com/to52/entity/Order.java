package com.to52.entity;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tzhang
 * @date 2019/5/21 {16:34}
 */
public class Order implements Serializable {
    private Integer oid;
    private Integer ostate;
    private String dateOrder;
    private Ticket ticket;
    private Client buyer;

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }
    public Integer getOstate() {
        return ostate;
    }

    public void setOstate(Integer ostate) {
        this.ostate = ostate;
    }
    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Client getBuyer() {
        return buyer;
    }

    public void setBuyer(Client buyer) {
        this.buyer = buyer;
    }


    @Override
    public String toString() {
        return "Order{" +
                "oid=" + oid +
                ", ostate=" + ostate +
                ", dateOrder=" + dateOrder +
                ", ticket=" + ticket +
                ", buyer=" + buyer +
                '}';
    }
}
