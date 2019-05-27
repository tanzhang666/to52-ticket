package com.to52.service;
import com.to52.entity.Client;

/**
 * @author tzhang
 * @date 2019/5/21 {16:33}
 */
public interface ClientService {
    Client getClientById(Integer id);
    void register(Client client);
    Client login(String email,String encrptPassword);
    void deleteClientById(Integer id);
    void updateClientInfo(Client client);
}
