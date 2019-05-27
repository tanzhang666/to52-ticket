package com.to52.controller;
import com.to52.entity.Client;
import com.to52.entity.Order;
import com.to52.entity.Ticket;
import com.to52.response.CommonReturnType;
import com.to52.service.ClientService;
import com.to52.service.OrderService;
import com.to52.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/23 {1:40}
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TicketService ticketService;
    @RequestMapping(value = "/getOrderByBid",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrderByBid(Integer bid){
        List<Order> orderList=orderService.getOrderByBid(bid);
        if(orderList!=null){
            return CommonReturnType.create(orderList);
        }
        return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
    }
    @RequestMapping(value="/createOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType createOrder(Integer bid,Integer tid){
        Order order=new Order();
        Client client=clientService.getClientById(bid);
        Ticket ticket=ticketService.getTicketById(tid);
        if(client!=null&&ticket!=null){
            order.setOstate(1);
            ticketService.updateTicketState(tid,1);
            order.setBuyer(client);
            order.setTicket(ticket);
            orderService.createOrder(order);
            return CommonReturnType.create(order);
        }
        return CommonReturnType.create("null","100","TICKET OR CLIENT NOT FOUND");
    }
    @RequestMapping(value="/validateOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType validateOrder(Integer sid,Integer tid,Integer oid){
        Order order=orderService.getOrderByOid(oid);
        Client client=clientService.getClientById(sid);
        Ticket ticket=ticketService.getTicketById(tid);
        if(order!=null&&client!=null&&ticket!=null&&ticket.getSeller().getCid()==client.getCid()){
            orderService.updateOrderState(oid,2);
            ticketService.updateTicketState(tid,2);
            order.setOstate(2);
            return CommonReturnType.create(order);
        }
        return CommonReturnType.create("null","100","TICKET OR CLIENT NOT FOUND");
    }
    @RequestMapping(value="/cancelOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType cancelOrder(Integer sid,Integer tid,Integer oid){
        Order order=orderService.getOrderByOid(oid);
        Client client=clientService.getClientById(sid);
        Ticket ticket=ticketService.getTicketById(tid);
        if(order!=null&&client!=null&&ticket!=null&&ticket.getSeller().getCid()==client.getCid()){
            orderService.updateOrderState(oid,0);
            ticketService.updateTicketState(tid,0);
            order.setOstate(0);
            return CommonReturnType.create(order);
        }
        return CommonReturnType.create("null","100","TICKET OR CLIENT NOT FOUND");
    }
}
