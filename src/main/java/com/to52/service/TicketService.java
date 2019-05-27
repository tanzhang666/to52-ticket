package com.to52.service;

import com.to52.entity.Ticket;

import java.util.Date;
import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/21 {16:34}
 */
public interface TicketService {
    void createTicket(Ticket ticket);
    void deleteTicketById(Integer tid);
    Ticket getTicketById(Integer tid);
    void updateTicketInfo(Ticket ticket);
    void updateTicketState(Integer tid,Integer tstate);
    List<Ticket> getTicketBySid(Integer sid);
    List<Ticket> getAllTicket();
    List<Ticket> getTicketByCondition(String destination,String departure,Date dateDepart,Date timeDepart);
}
