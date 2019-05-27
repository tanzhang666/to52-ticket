package com.to52.service.impl;
import com.to52.dao.daoMapper.ClientDOMapper;
import com.to52.dao.daoMapper.OrderDOMapper;
import com.to52.dao.daoMapper.TicketDOMapper;
import com.to52.dao.dataObject.ClientDO;
import com.to52.dao.dataObject.OrderDO;
import com.to52.dao.dataObject.TicketDO;
import com.to52.entity.Client;
import com.to52.entity.Order;
import com.to52.entity.Ticket;
import com.to52.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private TicketDOMapper ticketDOMapper;
    @Autowired
    private ClientDOMapper clientDOMapper;
    @Override
    public Order getOrderByOid(Integer oid) {
        OrderDO orderDO=orderDOMapper.selectByPrimaryKey(oid);
        if(orderDO!=null){
           return convertOrderDOToOrder(orderDO);
        }
        return null;
    }

    @Override
    public List<Order> getOrderByBid(Integer bid) {
        List<OrderDO> orderDOList=orderDOMapper.selectByBid(bid);
        if(orderDOList!=null){
            List<Order> orderList=new ArrayList<Order>();
            for(int i=0;i<orderList.size();i++){
                orderList.add(convertOrderDOToOrder(orderDOList.get(i)));
            }
            return orderList;
        }
        return null;
    }

    @Override
    public Order getOrderByTid(Integer tid) {
        OrderDO orderDO=orderDOMapper.selectByTid(tid);
        if(orderDO!=null){
            return convertOrderDOToOrder(orderDO);
        }
        return null;
    }

    @Override
    public void updateOrderState(Integer oid, Integer ostate) {
        Order order=new Order();
        order.setOid(oid);
        order.setOstate(ostate);
        OrderDO orderDO=new OrderDO();
        BeanUtils.copyProperties(order,orderDO);
        orderDOMapper.updateByPrimaryKeySelective(orderDO);
    }

    @Override
    public void deleteOrderByOid(Integer oid) {
        OrderDO orderDO=orderDOMapper.selectByPrimaryKey(oid);
        if(orderDO!=null){
            orderDOMapper.deleteByPrimaryKey(oid);
        }
    }

    @Override
    public void createOrder(Order order) {
        OrderDO orderDO=new OrderDO();
        BeanUtils.copyProperties(order,orderDO);
        orderDO.setBid(order.getBuyer().getCid());
        orderDO.setTid(order.getTicket().getTid());
        orderDOMapper.insert(orderDO);
    }
    private Order convertOrderDOToOrder(OrderDO orderDO){
        Order order=new Order();
        Ticket ticket=new Ticket();
        Client clientS=new Client();
        Client clientB=new Client();
        TicketDO ticketDO=ticketDOMapper.selectByPrimaryKey(orderDO.getTid());
        ClientDO clientDOS=clientDOMapper.selectByPrimaryKey(ticketDO.getSid());
        ClientDO clientDOB=clientDOMapper.selectByPrimaryKey(orderDO.getBid());
        BeanUtils.copyProperties(clientDOS,clientS);
        BeanUtils.copyProperties(clientDOB,clientB);
        BeanUtils.copyProperties(ticketDO,ticket);
        ticket.setSeller(clientS);
        BeanUtils.copyProperties(orderDO,order);
        order.setTicket(ticket);
        order.setBuyer(clientB);
        return order;
    }
}
