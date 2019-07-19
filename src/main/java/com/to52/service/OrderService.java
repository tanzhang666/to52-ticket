package com.to52.service;
import com.to52.entity.Order;
import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/21 {16:34}
 */
public interface OrderService {
    Order getOrderByOid(Integer oid);//la funtion pour saisir les infos d'order selon le id
    List<Order> getOrderByBid(Integer bid);//la funtion pour saisir les infos d'order selon le id de acheteur
    Order getOrderByTid(Integer tid);//la funtion pour saisir les infos d'order selon le id de billet
    void updateOrderState(Integer oid,Integer ostate);//la function pour faire mise a jour le status d'order
    void deleteOrderByOid(Integer oid);//la funtion pour suppression les infos d'order selon le id
    void createOrder(Order order);//la function pour creer l'order
}
