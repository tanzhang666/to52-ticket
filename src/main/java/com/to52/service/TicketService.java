package com.to52.service;

import com.to52.entity.Ticket;
import java.text.ParseException;
import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/21 {16:34}
 */
public interface TicketService {
    void createTicket(Ticket ticket);//la function pour creer le billet
    void deleteTicketById(Integer tid);
    Ticket getTicketById(Integer tid);//la funtion pour saisir les infos de billet selon le id de billet
    void updateTicketInfo(Ticket ticket);//la function pour faire mise a jour les infos de billet
    void updateTicketState(Integer tid,Integer tstate);//la function pour faire mise a jour le status de billet
    List<Ticket> getTicketBySid(Integer sid);//la funtion pour saisir les infos de billet selon le id de vendeur
    List<Ticket> getAllTicket();//la function de saisir la liste des billets
    List<Ticket> getTicketByCondition(String destination,String departure,String dateDepart,String dateDepartTo) throws ParseException;//la funtion pour saisir les infos des billets selon les conditions
}
