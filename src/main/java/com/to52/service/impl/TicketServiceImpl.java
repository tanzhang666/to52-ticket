package com.to52.service.impl;
import com.to52.dao.daoMapper.ClientDOMapper;
import com.to52.dao.daoMapper.TicketDOMapper;
import com.to52.dao.dataObject.ClientDO;
import com.to52.dao.dataObject.TicketDO;
import com.to52.entity.Client;
import com.to52.entity.Ticket;
import com.to52.service.TicketService;
import org.springframework.beans.BeanUtils;//Importer la classe avec les méthodes de commodité statiques pour JavaBeans: pour instancier des beans, vérifier les types de propriété de bean, copier les propriétés de bean, etc.
import org.springframework.beans.factory.annotation.Autowired;//il va crée l’instance de la classe, et il va faire l’injection de dépendance (injecter l’objet créeé de la classe)
import org.springframework.stereotype.Service;//Indiquer la classe est un "Service"
import java.text.ParseException;//Signale qu'une erreur a été atteinte de manière inattendue lors de l'analyse.
import java.text.SimpleDateFormat;//Importer la classe pour le formatage (date -> texte), l'analyse (texte -> date) et la normalisation.
import java.util.ArrayList;//ArrayList est une matrice de files d'attente , ce qui correspond à un tableau dynamique .
import java.util.Date;//Importer la classe Date
import java.util.List;//Importer la classe collection ordonnée

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
@Service//Indiquer la classe est un "Service"
public class TicketServiceImpl implements TicketService {
    //Injecter l'objet de type TicketDOMapper
    @Autowired
    private TicketDOMapper ticketDOMapper;
    //Injecter l'objet de type ClientDOMapper
    @Autowired
    private ClientDOMapper clientDOMapper;
    @Override
    /**
     * la function pour creer le billet
     */
    public void createTicket(Ticket ticket) {
        //initialiser l'objet TicketDO
        TicketDO ticketDO= new TicketDO();
        try {
            //transferer l'objet ticket a ticketDO
            ticketDO = this.convertTicketToTicketDO(ticket);
        } catch (ParseException e) {
            //si il y a exception print l'eureur
            e.printStackTrace();
        }
        //definir la valeur d'attribute de sid de l'objet ticketDO
        ticketDO.setSid(ticket.getSeller().getCid());
        //faire la insertion dans la base
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
    /**
     * la funtion pour saisir les infos de billet selon le id de billet
     */
    public Ticket getTicketById(Integer tid) {
        //saisir le billet utilisant la methode de TicketDOMapper selon le id de ticket
        TicketDO ticketDO=ticketDOMapper.selectByPrimaryKey(tid);
        //si le ticket existe dans la base
        if(ticketDO!=null){
            //retourner l'objet ticket apres tranferer depuis ticketDO
            return convertTicketDOToTicket(ticketDO);
        }
        //si le ticket n'existe
        else{
            //retourner null
            return null;
        }
    }
    /**
     * la funtion pour faire mise a jour les infos de billet
     */
    @Override
    public void updateTicketInfo(Ticket ticket) {
        //initialiser l'objet ticketDO
        TicketDO ticketDO=new TicketDO();
        try {
            //transferer l'objet ticket a ticketDO
            ticketDO = this.convertTicketToTicketDO(ticket);
        } catch (ParseException e) {
            //si il y a exception print l'eureur
            e.printStackTrace();
        }
        //faire la mise a jour les infos de billet dans la base
        ticketDOMapper.updateByPrimaryKeySelective(ticketDO);
    }
    /**
     * la function pour faire mise a jour le status de billet
     */
    @Override
    public void updateTicketState(Integer tid, Integer tstate) {
        //initialiser l'objet ticket
        Ticket ticket=new Ticket();
        //definir le valeur d'attribute tid du l'objet ticket
        ticket.setTid(tid);
        //definir le valeur d'attribute tstate du l'objet ticket
        ticket.setTstate(tstate);
        //faire la mise a jour le status dans la base
        updateTicketInfo(ticket);
    }
    /**
     * la funtion pour saisir les infos de billet selon le id de vendeur
     */
    @Override
    public List<Ticket> getTicketBySid(Integer sid) {
        //saisir les billets utilisant la methode de TicketDOMapper selon le id de vendeurs
        List<TicketDO> ticketDOList=ticketDOMapper.selectBySid(sid);
        //si il y a des tickets
        if(ticketDOList!=null){
            //initialise la liste pour recevoir les objets billet
            List<Ticket> ticketList=new ArrayList<Ticket>();
            //pour tout les objets dans la liste de ticketDO,tranferer a l'objet ticket et inserer dans la liste de ticket
            for(int i=0;i<ticketDOList.size();i++){
                ticketList.add(convertTicketDOToTicket(ticketDOList.get(i)));
            }
            //retourner la liste ticket
            return ticketList;
        }
        //si il n'y pas de ticket retourner null
        else {
            return null;
        }
    }
    /**
     * la funtion pour saisir les infos des billets
     */
    @Override
    public List<Ticket> getAllTicket() {
        //saisir les billets utilisant la methode de TicketDOMapper
        List<TicketDO> ticketDOList=ticketDOMapper.selectAll();
        //si il y a des billets
        if(ticketDOList!=null){
            //initialiser la liste pour recevoir les billtes apres consultation depuis la base
            List<Ticket> ticketList=new ArrayList<Ticket>();
            //pour tout les objets dans la liste de ticketDO,tranferer a l'objet ticket et inserer dans la liste de ticket
            for(int i=0;i<ticketDOList.size();i++){
                ticketList.add(convertTicketDOToTicket(ticketDOList.get(i)));
            }
            //retourner la liste ticket
            return ticketList;
        }
        //si il n'y pas de ticket retourner null
        else {
            return null;
        }
    }

    @Override
    /**
     * la funtion pour saisir les infos des billets selon les conditions
     */
    public List<Ticket> getTicketByCondition(String destination, String departure, String dateDepart, String dateDepartTo)throws ParseException {
        //initialiser l'objet DateFormatter pour transferer la string a l'objet date en format dd/MM/yyyy
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd/MM/yyyy");
        Date dateDe=null;
        Date dateDeTo=null;
        //transferer la dateDepart de type String a type Date
        if(dateDepart!=null&&dateDepart!=""){
            dateDe=simpleDateFormat1.parse(dateDepart);
        }
        //transferer la dateDepartTo de type String a type Date
        if(dateDepartTo!=null&&dateDepartTo!=""){
            dateDeTo=simpleDateFormat1.parse(dateDepartTo);
        }
        //saisir les billets utilisant la methode de TicketDOMapper selon les conditions
        List<TicketDO> ticketDOList=ticketDOMapper.selectByCondition(destination,departure,dateDe,dateDeTo);
        //si il y a des billets
        if(ticketDOList!=null){
            //initialiser la liste pour recevoir les billtes apres consultation depuis la base
            List<Ticket> ticketList=new ArrayList<Ticket>();
            //pour tout les objets dans la liste de ticketDO,tranferer a l'objet ticket et inserer dans la liste de ticket
            for(int i=0;i<ticketDOList.size();i++){
                ticketList.add(convertTicketDOToTicket(ticketDOList.get(i)));
            }
            //retourner la liste ticket
            return ticketList;
        }
        //si il n'y pas de ticket retourner null
        else {
            return null;
        }
    }

    /**
     * la function pour transferer l'objet TicketDO a l'objet Ticket
     * @param ticketDO
     * @return
     */
    private Ticket convertTicketDOToTicket(TicketDO ticketDO){
        //initialiser l'objet DateFormatter pour transferer l'objet date en format dd/MM/yyyya la string
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd/MM/yyyy");
        //initialiser l'objet DateFormatter pour transferer l'objet date en format HH:mm a la string
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("HH:mm");
        //initialiser un objet ticket
        Ticket ticket=new Ticket();
        //copier tout les valeurs des attributes d'objet ticketDO a ticket
        BeanUtils.copyProperties(ticketDO,ticket);
        //definir la valeur de attribute dateDepart apres former
        ticket.setDateDepart(simpleDateFormat1.format(ticketDO.getDateDepart()));
        //definir la valeur de attribute dateArrival apres former
        ticket.setDateArrival(simpleDateFormat1.format(ticketDO.getDateArrival()));
        //definir la valeur de attribute timeDepart apres former
        ticket.setTimeDepart(simpleDateFormat2.format(ticketDO.getTimeDepart()));
        //definir la valeur de attribute timeArrival apres former
        ticket.setTimeArrival(simpleDateFormat2.format(ticketDO.getTimeArrival()));
        //saisir le client utilisant la methode de ClientDOMapper selon le id de vendeur de ticket
        ClientDO clientDO=clientDOMapper.selectByPrimaryKey(ticketDO.getSid());
        Client client=new Client();
        //si le client exist
        if(clientDO!=null){
            //copier tout les valeurs des attributes de clientDO a client
            BeanUtils.copyProperties(clientDO,client);
        }
        //definir la valeur d'attribute seller de ticket
        ticket.setSeller(client);
        //retourner l'objet apres assemblage
        return ticket;
    }

    /**
     * la function pour transferer l'objet Ticket a l'objet TicketDO
     * @param ticket
     * @return
     * @throws ParseException
     */
    private TicketDO convertTicketToTicketDO(Ticket ticket) throws ParseException {
        //initialiser l'objet DateFormatter pour transferer la string a l'objet date en format dd/MM/yyyy
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd/MM/yyyy");
        //initialiser l'objet DateFormatter pour transferer la string a l'objet date en format HH:mm
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //initialiser un objet ticketDO
        TicketDO ticketDO=new TicketDO();
        //copier tout les valeurs des attributes d'objet ticket a ticketDO
        BeanUtils.copyProperties(ticket,ticketDO);
        //definir la valeur de attribute dateDepart apres former
        if(ticket.getDateDepart()!=null){
            ticketDO.setDateDepart(simpleDateFormat1.parse(ticket.getDateDepart()));
        }
        //definir la valeur de attribute dateArrival apres former
        if(ticket.getDateArrival()!=null){
            ticketDO.setDateArrival(simpleDateFormat1.parse(ticket.getDateArrival()));
        }
        //definir la valeur de attribute timeDepart apres former
        if(ticket.getTimeDepart()!=null){
            ticketDO.setTimeDepart(simpleDateFormat2.parse(ticket.getDateDepart()+" "+ticket.getTimeDepart()));
        }
        //definir la valeur de attribute timeArrival apres former
        if(ticket.getTimeArrival()!=null){
            ticketDO.setTimeArrival(simpleDateFormat2.parse(ticket.getDateArrival()+" "+ticket.getTimeArrival()));
        }
        //definir la valeur d'attribute sid de ticket
        if(ticket.getSeller()!=null){
            ticketDO.setSid(ticket.getSeller().getCid());
        }
        //retourner l'objet ticketDO apres assemblage
        return ticketDO;
    }
}
