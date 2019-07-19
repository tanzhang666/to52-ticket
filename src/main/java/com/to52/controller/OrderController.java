package com.to52.controller;
import com.to52.entity.Client;
import com.to52.entity.Order;
import com.to52.entity.Ticket;
import com.to52.response.CommonReturnType;
import com.to52.service.ClientService;
import com.to52.service.OrderService;
import com.to52.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;//il va crée l’instance de la classe, et il va faire l’injection de dépendance (injecter l’objet créeé de la classe)
import org.springframework.stereotype.Controller;//Indiquer la classe est un "Controller"
import org.springframework.web.bind.annotation.*;//importer les annotations pour lier les requêtes aux contrôleurs et aux méthodes de gestionnaire, ainsi que pour lier les paramètres de requête aux arguments de méthode.
import java.util.ArrayList;
import java.util.List;//Importer la classe collection ordonnée

/**
 * @author tzhang
 * @date 2019/5/23 {1:40}
 */
@Controller("order")//indiquer la classe est un controlleur nomme order
@RequestMapping("/order")//indiquer l'adresse de request http
public class OrderController {
    @Autowired
    private OrderService orderService;//injecter l'objet de classe OrderService
    @Autowired
    private ClientService clientService;//injecter l'objet de classe ClientService
    @Autowired
    private TicketService ticketService;//injecter l'objet de classe TicketService

    /**
     * la function pour saisir les orders selon le id de acheteur
     * @param bid
     * @return
     */
    //indiquer l'adresse de request http d'obtenir les informations de order
    @RequestMapping(value = "/getOrderByBid",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrderByBid(Integer bid){
        //obtenir la liste des objets de order utilisant le service
        List<Order> orderList=orderService.getOrderByBid(bid);
        //si l'order exist
        if(orderList!=null){
            //retourner la liste d'order
            return CommonReturnType.create(orderList);
        }
        //retourner l'objet de CommonReturnType avec l'echec de saisir
        return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
    }
    /**
     * la function pour saisir les orders selon le id de vendeur
     * @param sid
     * @return
     */
    //indiquer l'adresse de request http d'obtenir les informations de order selon le id de vendeur
    @RequestMapping(value = "/getOrderBySid",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrderBySid(Integer sid){
        //obtenir la liste des objets de ticket en utilisant le service
        List<Ticket> ticketList=ticketService.getTicketBySid(sid);
        //si il y a des billets
        if(ticketList!=null) {
            //initiliser la liste pour recevoir les orders
            List<Order> orderList = new ArrayList<Order>();
            //pour chaque billet dans la liste determiner et ajouter dans la liste des orders
            for (Ticket ticket :
                    ticketList) {
                //si le billet est vendu ou en status attent de confirmation
                if (ticket.getTstate() != 0) {
                    //saisir l'objet order selon le id de ticket
                    Order order = orderService.getOrderByTid(ticket.getTid());
                    //ajouter dans la liste des orders
                    orderList.add(order);
                }
            }
            //si l'order exist
            if (orderList != null) {
                //retourner la liste d'order
                return CommonReturnType.create(orderList);
            }
        }
        //retourner l'objet de CommonReturnType avec l'echec de saisir
        return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
    }
    /**
     * la function pour créer le order
     * @param bid
     * @param tid
     * @return
     */
    //indiquer l'adresse de request http de creer l'order
    @RequestMapping(value="/createOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam Integer bid,@RequestParam Integer tid){
        Order order=new Order();
        //saisir le client selon le id
        Client client=clientService.getClientById(bid);
        //saisir le billet selon le id
        Ticket ticket=ticketService.getTicketById(tid);
        //si le client et le billet exist
        if(client!=null&&ticket!=null&&ticket.getTstate()==0){
            //définir  la status du order
            order.setOstate(1);
            //changer la status du billet
            ticketService.updateTicketState(tid,1);
            order.setBuyer(client);
            order.setTicket(ticket);
            //créer le order
            orderService.createOrder(order);
            //retourner la liste d'order
            return CommonReturnType.create(order,"200","buy success");
        }
        //retourner l'objet de CommonReturnType avec l'echec de saisir
        return CommonReturnType.create("null","100","TICKET OR CLIENT NOT FOUND");
    }

    /**
     * la function pour les vendeurs faire validation le order selon le id de acheteur,le id de billet,id de order
     * @param sid
     * @param tid
     * @param oid
     * @return
     */
    //indiquer l'adresse de request http de valider l'order
    @RequestMapping(value="/validateOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType validateOrder(@RequestParam Integer sid,@RequestParam Integer tid,@RequestParam Integer oid){
        //saisir le order selon le id
        Order order=orderService.getOrderByOid(oid);
        //saisir le client selon le id
        Client client=clientService.getClientById(sid);
        //saisir le billet selon le id
        Ticket ticket=ticketService.getTicketById(tid);
        if(order!=null&&client!=null&&ticket!=null&&ticket.getSeller().getCid()==client.getCid()){
            //changer le status du order
            orderService.updateOrderState(oid,2);
            //changer le status du billet
            ticketService.updateTicketState(tid,2);
            order.setOstate(2);
            //retourner l'objet d'order
            return CommonReturnType.create(order);
        }
        //retourner l'objet de CommonReturnType avec l'echec de creation
        return CommonReturnType.create("null","100","TICKET OR CLIENT NOT FOUND");
    }

    /**
     * la function pour les vendeurs refuser les demande d'acheter les billets.
     * @param sid
     * @param tid
     * @param oid
     * @return
     */
    //indiquer l'adresse de request http de refuser la demande d'acheter le billet pour les vendeur
    @RequestMapping(value="/rejectOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType rejectOrder(@RequestParam Integer sid,@RequestParam Integer tid,@RequestParam Integer oid){
        //saisir le order selon le id
        Order order=orderService.getOrderByOid(oid);
        //saisir le client selon le id
        Client client=clientService.getClientById(sid);
        //saisir le billet selon le id
        Ticket ticket=ticketService.getTicketById(tid);
        if(order!=null&&client!=null&&ticket!=null&&ticket.getSeller().getCid()==client.getCid()){
            //changer le status du order
            orderService.updateOrderState(oid,0);
            //changer le status du billet
            ticketService.updateTicketState(tid,0);
            order.setOstate(0);
            orderService.deleteOrderByOid(oid);
            //retourner l'objet d'order
            return CommonReturnType.create(order);
        }
        //retourner l'objet de CommonReturnType avec le status d'echc
        return CommonReturnType.create("null","100","TICKET OR CLIENT NOT FOUND");
    }
}
