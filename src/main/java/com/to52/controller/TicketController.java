package com.to52.controller;
import com.to52.entity.Ticket;
import com.to52.response.CommonReturnType;
import com.to52.service.TicketService;
import org.apache.commons.lang3.StringUtils;//Operations sur les chaînes de caractères
import org.springframework.beans.factory.annotation.Autowired;//il va crée l’instance de la classe, et il va faire l’injection de dépendance (injecter l’objet créeé de la classe)
import org.springframework.stereotype.Controller;//Indiquer la classe est un "Controller"
import org.springframework.web.bind.annotation.*;//importer les annotations pour lier les requêtes aux contrôleurs et aux méthodes de gestionnaire, ainsi que pour lier les paramètres de requête aux arguments de méthode.
import javax.servlet.http.HttpServletRequest;//importer la classe pour fournir des informations de requête aux servlets HTTP.
import java.text.ParseException;//Signale qu'une erreur a été atteinte de manière inattendue lors de l'analyse.
import java.util.List;//Importer la classe collection ordonnée

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
//indiquer la classe est un controlleur nomme ticket
@Controller("ticket")
@RequestMapping("/ticket")//indiquer l'adresse de request http
//Il permet au navigateur de faire des XMLHttpRequestdemandes aux serveurs d’origine croisée, en surmontant les limites de la seule utilisation homologue d’AJAX .
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class TicketController {
    //injecter l'objet de classe TicketService
    @Autowired
    private TicketService ticketService;
    /**
     * la function pour saisir les ticket n'a pas été achete
     * @return
     */
    //indiquer l'adresse de request http d'obtenir les informations de ticket
    @RequestMapping(value = "/getAllTicket",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getAllTicket(){
        //obtenir la liste des objets de ticket utilisant le service
        List<Ticket> ticketList=ticketService.getAllTicket();
        //si il y a des billets
        if(ticketList!=null){
            //retourner la liste des billets
            return CommonReturnType.create(ticketList);
        }
        //si il n'y a pas de billet
        else{
            //retourner l'objet de CommonReturnType avec l'info de echec
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }

    /**
     * saisir le billet selon le id de vendeur
     * @param sid
     * @return
     */
    @RequestMapping(value = "/getTicketBySid",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getTicketBySid(Integer sid){
        //obtenir la liste des objets de ticket en utilisant le service
        List<Ticket> ticketList=ticketService.getTicketBySid(sid);
        //si il y a des billets
        if(ticketList!=null){
            //retourner la liste des billets vendu
            return CommonReturnType.create(ticketList);
        }
        else{
            //retourner l'objet de CommonReturnType avec l'info d'echec
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }

    /**
     * la function pour saisir le billet  selon le id
     * @param tid
     * @return
     */
    //indiquer l'adresse de request http
    @RequestMapping(value = "/getTicketByTid",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getTicketByTid(@RequestParam Integer tid){
        Ticket ticket=ticketService.getTicketById(tid);
        if(ticket!=null){
            return CommonReturnType.create(ticket);
        }
        else{
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }

    /**
     * la function pour saisir les billes selon plusieurs conditions
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTicketByCondition",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getTicketByCondition(
            HttpServletRequest request) throws ParseException {
        //utiliser le service pour saisir les tickets selon les conditions
        List<Ticket> ticketList=ticketService.getTicketByCondition(
                request.getParameter("destination"),request.getParameter("departure"),
                request.getParameter("dateDeparts"),request.getParameter("dateDepartTos"));
        //si on trouve les billets
        if(ticketList!=null){
            //retourner la liste des billets qui corespondant les conditions
            return CommonReturnType.create(ticketList);
        }
        //si on trouve pas les billets
        else{
            //retourner l'objet de commonReturnType avec le message d'echec
            return CommonReturnType.create("null","100","TICKET_NOT_FOUND");
        }
    }
    /**
     * la function pour créer les billets
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/createTicket",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType createTicket(
            @RequestBody Ticket ticket
           ){
        //si les valeurs sont donne par les utilisateurs
        if(StringUtils.isNotEmpty(ticket.getDeparture())&&
                StringUtils.isNotEmpty(ticket.getDestination())&&
                String.valueOf(ticket.getSeller())!=null&&
                String.valueOf(ticket.getDateDepart())!=null&&
                String.valueOf(ticket.getDateArrival())!=null&&
                String.valueOf(ticket.getTimeDepart())!=null&&
                String.valueOf(ticket.getTimeArrival())!=null&&
                String.valueOf(ticket.getPrice())!=null&&
                String.valueOf(ticket.getReward())!=null
        ){
            //definir le status de billet de non vendu
            ticket.setTstate(0);
            //créer le billet
            ticketService.createTicket(ticket);
            //retourner l'objet de billet
            return CommonReturnType.create(ticket);
    }
        //si les valeurs de billet n'est pas complet
        else{
            //retourner l'objet de type CommonreturnType avec l'info d'echec
            return CommonReturnType.create("null","101","PARAM_NOT_COMPLET");
        }
    }

    /**
     * la function pour faire mise à jour lesinformations les billets.
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/updateTicketInfo",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType updateTicketInfo(
            @RequestBody Ticket ticket
    ) {
        //faire mise a jour les infos utilisant le service
            ticketService.updateTicketInfo(ticket);
            //saisir le billet apres le mise a jour
            Ticket ticket1=ticketService.getTicketById(ticket.getTid());
            //retourner l'objet billet
            return CommonReturnType.create(ticket1);
    }

}
