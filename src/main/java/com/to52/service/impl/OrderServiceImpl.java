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
import com.to52.service.TicketService;
import org.springframework.beans.BeanUtils;//Importer la classe avec les méthodes de commodité statiques pour JavaBeans: pour instancier des beans, vérifier les types de propriété de bean, copier les propriétés de bean, etc.
import org.springframework.beans.factory.annotation.Autowired;//il va crée l’instance de la classe, et il va faire l’injection de dépendance (injecter l’objet créeé de la classe)
import org.springframework.stereotype.Service;//Indiquer la classe est un "Service"
import java.text.SimpleDateFormat;//Importer la classe pour le formatage (date -> texte), l'analyse (texte -> date) et la normalisation.
import java.util.ArrayList;//ArrayList est une matrice de files d'attente , ce qui correspond à un tableau dynamique .
import java.util.Date;//Importer la classe Date
import java.util.List;//Importer la classe collection ordonnée

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
@Service//Indiquer la classe est un "Service"
public class OrderServiceImpl implements OrderService {
    //Injecter l'objet de type OrderDOMapper
    @Autowired
    private OrderDOMapper orderDOMapper;
    //Injecter l'objet de type TicketDOMapper
    @Autowired
    private TicketDOMapper ticketDOMapper;
    //Injecter l'objet de type TicketService
    @Autowired
    private TicketService ticketService;
    //Injecter l'objet de type ClientDOMapper
    @Autowired
    private ClientDOMapper clientDOMapper;
    @Override
    /**
     * la funtion pour saisir les infos d'order selon le id
     */
    public Order getOrderByOid(Integer oid) {
        //saisir l'objet de orderDO selon le id
        OrderDO orderDO=orderDOMapper.selectByPrimaryKey(oid);
        //si l'order exist
        if(orderDO!=null){
            //retourner l'objet d'order apres transferer depuis orderDO
           return convertOrderDOToOrder(orderDO);
        }
        //retourner null
        return null;
    }
    /**
     * la funtion pour saisir les infos d'order selon le id de acheteur
     */
    @Override
    public List<Order> getOrderByBid(Integer bid) {
        //saisir les orders utilisant la methode de OrderDOMapper
        List<OrderDO> orderDOList=orderDOMapper.selectByBid(bid);
        //si il y a des orders
        if(orderDOList!=null){
            //initialiser la liste d'order
            List<Order> orderList=new ArrayList<Order>();
            //pour tout les objet OrderDO dans la liste,copier tout les valeurs des attributes d'objet dao a l'objet order
            for(int i=0;i<orderDOList.size();i++){
                orderList.add(convertOrderDOToOrder(orderDOList.get(i)));
            }
            //retourner la liste des orders
            return orderList;
        }
        //si il n'y pas les orders retourner null
        return null;
    }
    /**
     * la funtion pour saisir les infos d'order selon le id de billet
     */
    @Override
    public Order getOrderByTid(Integer tid) {
        //saisir les orders utilisant la methode de OrderDOMapper selon le id de billet
        OrderDO orderDO=orderDOMapper.selectByTid(tid);
        //si il y a l'order
        if(orderDO!=null){
            //retourner l'objet d'order apres le tranferer depuis orderDO
            return convertOrderDOToOrder(orderDO);
        }
        //si il n'y pas l'order retourner null
        return null;
    }

    @Override
    /**
     * la function pour faire mise a jour le status d'order
     */
    public void updateOrderState(Integer oid, Integer ostate) {
        //initialiser un objet d'orderDO
        OrderDO orderDO=new OrderDO();
        //definir la valeur de id du orderDO
        orderDO.setOid(oid);
        //definir la valeur de status du orderDO
        orderDO.setOstate(ostate);
        //faire la mise a jour le status de'order utilisant la methode de OrderDOMapper
        orderDOMapper.updateByPrimaryKeySelective(orderDO);
    }
    /**
     * la funtion pour suppression les infos d'order selon le id
     */
    @Override
    public void deleteOrderByOid(Integer oid) {
        //saisir les orders utilisant la methode de OrderDOMapper selon le id d'order
        OrderDO orderDO=orderDOMapper.selectByPrimaryKey(oid);
        //si l'order existe
        if(orderDO!=null){
            //faire la suppression dans la base utilisant la methode de OrderMapper
            orderDOMapper.deleteByPrimaryKey(oid);
        }
    }

    @Override
    /**
     * la function pour creer l'order
     */
    public void createOrder(Order order) {
        //initialise un objet d'orderDO
        OrderDO orderDO=new OrderDO();
        //copier tout les valeurs d'order a orderDO
        BeanUtils.copyProperties(order,orderDO);
        //definir le id d'acheteur de orderDO
        orderDO.setBid(order.getBuyer().getCid());
        //definir le id de ticket de orderDO
        orderDO.setTid(order.getTicket().getTid());
        //creer un objet de type date qui a la valeur de temps currente
        Date date=new Date();
        //definir le temps currente comme la date de order
        orderDO.setDateOrder(date);
        //faire insertion dans la base
        orderDOMapper.insert(orderDO);
    }

    /**
     * la function pour transferer l'objet OrderDO a l'objet order
     * @param orderDO
     * @return
     */
    private Order convertOrderDOToOrder(OrderDO orderDO){
        //initialiser un objet order
        Order order=new Order();
        //initialiser un objet ticket
        Ticket ticket=new Ticket();
        //initialiser un objet de client pour vendeur
        Client clientS=new Client();
        //initialiser un objet de client pour acheteur
        Client clientB=new Client();
        //initialiser l'objet DateFormatter pour transferer l'objet date en format dd/MM/yyyya la string
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd/MM/yyyy");
        //saisir l'objet ticketDO depuis la base selon le id
        TicketDO ticketDO=ticketDOMapper.selectByPrimaryKey(orderDO.getTid());
        //saisir l'objet clientDO de vendeur depuis la base selon le id
        ClientDO clientDOS=clientDOMapper.selectByPrimaryKey(ticketDO.getSid());
        //saisir l'objet clientDO d'acheteur depuis la base selon le id
        ClientDO clientDOB=clientDOMapper.selectByPrimaryKey(orderDO.getBid());
        //copier tout les valeurs des attributes de clientDO a client
        BeanUtils.copyProperties(clientDOS,clientS);
        //copier tout les valeurs des attributes de clientDO a client
        BeanUtils.copyProperties(clientDOB,clientB);
        //definir la valeur du attribute seller dans l'objet ticket
        ticket=ticketService.getTicketById(orderDO.getTid());
        //copier tout les valeurs des attributes d'orderDO a order
        BeanUtils.copyProperties(orderDO,order);
        //si la date de orderDO est defini
        if(orderDO.getDateOrder()!=null){
            //definir la chaine de caractere apres transferer par le formatter depuis la dateOrder de orderDO
            order.setDateOrder(simpleDateFormat1.format(orderDO.getDateOrder()));
        }
        //definir la valeur du attribute ticket dans l'objet order
        order.setTicket(ticket);
        //definir la valeur du attribute buyer dans l'objet order
        order.setBuyer(clientB);
        //retourner l'objet order apres assemblage
        return order;
    }
}
