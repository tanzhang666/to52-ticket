package com.to52.service.impl;

import com.to52.dao.daoMapper.ClientDOMapper;
import com.to52.dao.daoMapper.TicketDOMapper;
import com.to52.dao.dataObject.ClientDO;
import com.to52.dao.dataObject.TicketDO;
import com.to52.entity.Client;
import com.to52.entity.Ticket;
import com.to52.service.TicketService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketDOMapper ticketDOMapper;
    @Autowired
    private ClientDOMapper clientDOMapper;
    @Override
    public void createTicket(Ticket ticket) {
        TicketDO ticketDO=new TicketDO();
        BeanUtils.copyProperties(ticketDO,ticket);
        ticketDO.setSid(ticket.getSeller().getCid());
        ticketDOMapper.insertSelective(ticketDO);
    }

    @Override
    public void deleteTicketById(Integer tid) {
        TicketDO ticketDO=ticketDOMapper.selectByPrimaryKey(tid);
        if(ticketDO!=null){
            ticketDOMapper.deleteByPrimaryKey(tid);
        }
    }

    @Override
    public Ticket getTicketById(Integer tid) {
        TicketDO ticketDO=ticketDOMapper.selectByPrimaryKey(tid);
        if(ticketDO!=null){
            return convertTicketDOToTicket(ticketDO);
        }
        else{
            return null;
        }
    }

    @Override
    public void updateTicketInfo(Ticket ticket) {
        TicketDO ticketDO=new TicketDO();
        BeanUtils.copyProperties(ticket,ticketDO);
        ticketDOMapper.updateByPrimaryKeySelective(ticketDO);
    }

    @Override
    public void updateTicketState(Integer tid, Integer tstate) {
        Ticket ticket=new Ticket();
        ticket.setTid(tid);
        ticket.setTstate(tstate);
        updateTicketInfo(ticket);
    }

    @Override
    public List<Ticket> getTicketBySid(Integer sid) {
        List<Ticket> ticketList=new ArrayList<Ticket>();
        if(ticketList!=null){
            List<TicketDO> ticketDOList=ticketDOMapper.selectBySid(sid);
            for(int i=0;i<ticketDOList.size();i++){
                ticketList.add(convertTicketDOToTicket(ticketDOList.get(i)));
            }
            return ticketList;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Ticket> getAllTicket() {
        List<Ticket> ticketList=new ArrayList<Ticket>();
        if(ticketList!=null){
            List<TicketDO> ticketDOList=ticketDOMapper.selectAll();
            for(int i=0;i<ticketDOList.size();i++){
                ticketList.add(convertTicketDOToTicket(ticketDOList.get(i)));
            }
            return ticketList;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Ticket> getTicketByCondition(String destination, String departure, Date dateDepart, Date timeDepart) {
        List<Ticket> ticketList=new ArrayList<Ticket>();
        if(ticketList!=null){
            List<TicketDO> ticketDOList=ticketDOMapper.selectByCondition(destination,departure,dateDepart,timeDepart);
            for(int i=0;i<ticketDOList.size();i++){
                ticketList.add(convertTicketDOToTicket(ticketDOList.get(i)));
            }
            return ticketList;
        }
        else {
            return null;
        }
    }
    private Ticket convertTicketDOToTicket(TicketDO ticketDO){
        Ticket ticket=new Ticket();
        BeanUtils.copyProperties(ticketDO,ticket);
        ClientDO clientDO=clientDOMapper.selectByPrimaryKey(ticketDO.getSid());
        Client client=new Client();
        if(clientDO!=null){
            BeanUtils.copyProperties(clientDO,client);
        }
        ticket.setSeller(client);
        return ticket;
    }

}
