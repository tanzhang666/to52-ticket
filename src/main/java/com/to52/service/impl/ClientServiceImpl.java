package com.to52.service.impl;
import com.to52.dao.daoMapper.ClientDOMapper;
import com.to52.dao.daoMapper.OrderDOMapper;
import com.to52.dao.dataObject.ClientDO;
import com.to52.dao.dataObject.OrderDO;
import com.to52.entity.Client;
import com.to52.service.ClientService;
import org.apache.commons.lang3.StringUtils;//Operations sur les chaînes de caractères
import org.mybatis.spring.annotation.MapperScan;//l'annotation pour spécifier le chemin du package de la classe Mapper à analyser
import org.springframework.beans.BeanUtils;//Importer la classe avec les méthodes de commodité statiques pour JavaBeans: pour instancier des beans, vérifier les types de propriété de bean, copier les propriétés de bean, etc.
import org.springframework.beans.factory.annotation.Autowired;//il va crée l’instance de la classe, et il va faire l’injection de dépendance (injecter l’objet créeé de la classe)
import org.springframework.stereotype.Service;//Indiquer la classe est un "Service"
import org.springframework.transaction.annotation.Transactional;//L'annotation javax.transaction.Transactional offre à l'application la possibilité de contrôler de manière déclarative les limites des transactions sur les beans gérés CDI
import java.util.List;//Importer la classe collection ordonnée

/**
 * @author tzhang
 * @date 2019/5/23 {1:38}
 */
@Service//Indiquer la classe est un "Service"
@MapperScan("com.to52.dao.daoMapper")//spécifier le chemin du package de la classe Mapper à analyser
public class ClientServiceImpl implements ClientService {
    //Injecter l'objet de type ClientDOMapper
    @Autowired
    private ClientDOMapper clientDOMapper;
    //Injecter l'objet de type OrderDOMapper
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Override
    /**
     * la funtion pour saisir les infos de client selon le id
     */
    public Client getClientById(Integer id) {
        //saisir la dao client selon le id utilisant la methode de classe ClientDOMapper
        ClientDO clientDO=clientDOMapper.selectByPrimaryKey(id);
        //si le client n'exist
        if(clientDO==null){
            //retourner null
            return null;
        }
        //si le client exist
        else{
            //initialiser un objet de client
            Client client=new Client();
            //copier tout les valeur des attributes de clientDO a client
            BeanUtils.copyProperties(clientDO,client);
            //retourner l'objet client
            return client;
        }
    }

    @Override
    /**
     * la function pour s'inscrire et stocker dans la base de donne
     */
    public void register(Client client) {
        //initialiser un objet de type ClientDO
        ClientDO clientDO=new ClientDO();
        //si le client n'est pas null
        if(client!=null){
            //copier tout les valeur des attributes de client a clientDO
            BeanUtils.copyProperties(client,clientDO);
            //faire insertion dans la base utilisant la methode de classe ClientDOMapper
            clientDOMapper.insertSelective(clientDO);
        }
    }

    @Override
    /**
     * la function pour se conneter et  faire validation dans la base de donne selon le mot de passe et mail
     */
    public Client login(String email, String encrptPassword) {
        //saisir le client depuis la base selon le mail
        ClientDO clientDO=clientDOMapper.selectByEmail(email);
        //si le client exist
        if(clientDO!=null){
            //si le mot de passe est correct
            if(StringUtils.equals(encrptPassword,clientDO.getPassword())){
                //initialiser un objet de client
               Client client=new Client();
               //copier tout les valeurs des attributes de clientDO a client
               BeanUtils.copyProperties(clientDO,client);
               //retourner l'objet client
               return client;
            }
            //si le mot de passe n'est pas correct
            else{
                //retourner null
                return null;
            }
        }
        //si le client n'exist
        else{
            //retourner null
            return null;
        }
    }

    @Override
    @Transactional//faire la mise a jour dans la base dans un contexte de transaction.
    /**
     * la function pour supprimer le client selon le id
     */
    public void deleteClientById(Integer id) {
        ClientDO clientDO=clientDOMapper.selectByPrimaryKey(id);
        List<OrderDO> orderDOList=orderDOMapper.selectByBid(id);
                if(clientDO!=null){
                    clientDOMapper.deleteByPrimaryKey(id);
                    if(orderDOList!=null){
                        orderDOMapper.deleteByBid(id);
                    }
                }
    }


    @Override
    /**
     * la function pour faire le mise a jour les infos des clients
     * @param client le client avec les informations apres mise a jour
     */
    public void updateClientInfo(Client client) {
        //initialiser l'objet clientDO pour le base
        ClientDO clientDO=new ClientDO();
        //copier tout les valeurs des attributes client a clientDO
        BeanUtils.copyProperties(client,clientDO);
        //faire la mise a jour des informations utilisant la methode de clientDOMapper
        clientDOMapper.updateByPrimaryKeySelective(clientDO);
    }
}
