package com.to52.controller;
import com.to52.entity.Ticket;
import com.to52.response.CommonReturnType;
import com.to52.service.OrderService;
import com.to52.service.TicketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
@Controller("ticket")
@RequestMapping("/ticket")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private OrderService orderService;
    @RequestMapping(value = "/getAllTicket",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getAllTicket(){
        List<Ticket> ticketList=ticketService.getAllTicket();
        if(ticketList!=null){
            return CommonReturnType.create(ticketList);
        }
        else{
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }
    @RequestMapping(value = "/getTicketBySid",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getTicketBySid(Integer sid){
        List<Ticket> ticketList=ticketService.getTicketBySid(sid);
        if(ticketList!=null){
            return CommonReturnType.create(ticketList);
        }
        else{
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }
    @RequestMapping(value = "/getTicketByTid",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getTicketByTid(Integer tid){
        Ticket ticket=ticketService.getTicketById(tid);
        if(ticket!=null){
            return CommonReturnType.create(ticket);
        }
        else{
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }
    @RequestMapping(value = "/getTicketByCondition",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getTicketByCondition(
            @RequestParam(name = "destination") String destination,
            @RequestParam(name = "departure") String departure,
            @RequestParam(name = "dateDepart")Date dateDepart,
            @RequestParam(name = "timeDepart") Date timeDepart){
        List<Ticket> ticketList=ticketService.getTicketByCondition(destination,departure,dateDepart,timeDepart);
        if(ticketList!=null){
            return CommonReturnType.create(ticketList);
        }
        else{
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }
    @RequestMapping(value = "/createTicket",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType createTicket(
            @RequestParam(name = "destination") String destination,
            @RequestParam(name = "departure") String departure,
            @RequestParam(name = "dateDepart")Date dateDepart,
            @RequestParam(name = "dateArrival")Date dateArrival,
            @RequestParam(name = "timeDepart") Date timeDepart,
            @RequestParam(name = "timeArrival")Date timeArrival,
            @RequestParam(name = "price") BigDecimal price,
            @RequestParam(name = "reward") BigDecimal reward,
            @RequestParam(name = "seatNumber") String seatNumber,
            @RequestParam(name = "compartmentNumber") String compartmentNumber ,
            @RequestParam(name = "sid") Integer sid
           ){
        if(StringUtils.isNotEmpty(departure)&&
                StringUtils.isNotEmpty(destination)&&
                String.valueOf(sid)!=null&&
                String.valueOf(dateArrival)!=null&&
                String.valueOf(dateDepart)!=null&&
                String.valueOf(timeArrival)!=null&&
                String.valueOf(timeDepart)!=null&&
                String.valueOf(price)!=null&&
                String.valueOf(reward)!=null
        ){
        Ticket ticket=new Ticket();
        ticket.setDeparture(departure);
        ticket.setDestination(destination);
        ticket.setDateDepart(dateDepart);
        ticket.setDateArrival(dateArrival);
        ticket.setTimeDepart(timeDepart);
        ticket.setTimeArrival(timeArrival);
        ticket.setReward(reward);
        ticket.setPrice(price);
        ticket.setTstate(0);
        if(StringUtils.isNotEmpty(seatNumber)){
            ticket.setSeatNumber(seatNumber);
        }
        if(StringUtils.isNotEmpty(compartmentNumber)){
            ticket.setSeatNumber(compartmentNumber);
        }
        ticketService.createTicket(ticket);
        return CommonReturnType.create(ticket);
    }
        else{
            return CommonReturnType.create("null","101","PARAM_NOT_COMPLET");
        }
    }
    @RequestMapping(value = "/updateTicketInfo",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType updateTicketInfo(
            @RequestParam(name="tid") Integer tid,
            @RequestParam(name = "destination") String destination,
            @RequestParam(name = "departure") String departure,
            @RequestParam(name = "dateDepart")Date dateDepart,
            @RequestParam(name = "dateArrival")Date dateArrival,
            @RequestParam(name = "timeDepart") Date timeDepart,
            @RequestParam(name = "timeArrival")Date timeArrival,
            @RequestParam(name = "price") BigDecimal price,
            @RequestParam(name = "reward") BigDecimal reward,
            @RequestParam(name = "seatNumber") String seatNumber,
            @RequestParam(name = "compartmentNumber") String compartmentNumber
    ) {
        Ticket ticket=ticketService.getTicketById(tid);
        Ticket ticketUpdate=new Ticket();
        if(ticket!=null){
            ticketUpdate.setTid(tid);
            if(StringUtils.isNotEmpty(destination)){
                ticketUpdate.setDestination(destination);
            }
            if(StringUtils.isNotEmpty(departure)){
                ticketUpdate.setDeparture(departure);
            }
            if(String.valueOf(dateDepart)!=null){
                ticketUpdate.setDateDepart(dateDepart);
            }
            if(String.valueOf(dateArrival)!=null){
                ticketUpdate.setDateArrival(dateArrival);
            }
            if(String.valueOf(timeDepart)!=null){
                ticketUpdate.setTimeDepart(timeDepart);
            }
            if(String.valueOf(timeArrival)!=null){
                ticketUpdate.setTimeArrival(timeArrival);
            }
            if(StringUtils.isNotEmpty(seatNumber)){
                ticketUpdate.setSeatNumber(seatNumber);
            }
            if(String.valueOf(reward)!=null){
                ticketUpdate.setReward(reward);
            }
            if(String.valueOf(price)!=null){
                ticketUpdate.setPrice(price);
            }
            if(StringUtils.isNotEmpty(compartmentNumber)){
                ticketUpdate.setCompartmentNumber(compartmentNumber);
            }
            ticketService.updateTicketInfo(ticketUpdate);
            Ticket ticket1=ticketService.getTicketById(tid);
            return CommonReturnType.create(ticket1);
        }
        return CommonReturnType.create(null,"100","TICKET_NOT_FOUND");
    }

}
