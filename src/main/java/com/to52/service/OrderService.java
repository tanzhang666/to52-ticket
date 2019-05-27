package com.to52.service;

import com.to52.entity.Client;
import com.to52.entity.Order;
import com.to52.entity.Ticket;

import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/21 {16:34}
 */
public interface OrderService {
    Order getOrderByOid(Integer oid);
    List<Order> getOrderByBid(Integer bid);
    Order getOrderByTid(Integer tid);
    void updateOrderState(Integer oid,Integer ostate);
    void deleteOrderByOid(Integer oid);
    void createOrder(Order order);
}
