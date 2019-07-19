package com.to52.service;
import com.to52.entity.Client;

/**
 * @author tzhang
 * @date 2019/5/21 {16:33}
 */
public interface ClientService {
    Client getClientById(Integer id);//la funtion pour saisir les infos de client selon le id
    void register(Client client);//la function pour s'inscrire et stocker dans la base de donne
    Client login(String email,String encrptPassword);//la function pour se conneter et  faire validation dans la base de donne selon le mot de passe et mail
    void deleteClientById(Integer id);//la function pour supprimer le client selon le id
    void updateClientInfo(Client client);//la function pour faire le mise a jour les infos des clients
}
