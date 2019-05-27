package com.to52.service.impl;

import com.to52.dao.daoMapper.ClientDOMapper;
import com.to52.dao.daoMapper.OrderDOMapper;
import com.to52.dao.daoMapper.TicketDOMapper;
import com.to52.dao.dataObject.ClientDO;
import com.to52.dao.dataObject.OrderDO;
import com.to52.entity.Client;
import com.to52.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tzhang
 * @date 2019/5/23 {1:38}
 */
@Service
@MapperScan("com.to52.dao.daoMapper")
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientDOMapper clientDOMapper;
    @Autowired
    private TicketDOMapper ticketDOMapper;
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Override
    public Client getClientById(Integer id) {
        ClientDO clientDO=clientDOMapper.selectByPrimaryKey(id);
        if(clientDO==null){
            return null;
        }
        else{
            Client client=new Client();
            BeanUtils.copyProperties(clientDO,client);
            return client;
        }
    }

    @Override
    public void register(Client client) {
        ClientDO clientDO=new ClientDO();
        if(client!=null){
            BeanUtils.copyProperties(client,clientDO);
            clientDOMapper.insertSelective(clientDO);
        }
    }

    @Override
    public Client login(String email, String encrptPassword) {
        ClientDO clientDO=clientDOMapper.selectByEmail(email);
        if(clientDO!=null){
        if(StringUtils.equals(encrptPassword,clientDO.getPassword())){
           Client client=new Client();
           BeanUtils.copyProperties(clientDO,client);
           return client;
        }
        else{
            return null;
        }
        }
        else{
            return null;
        }
    }

    @Override
    @Transactional
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
    public void updateClientInfo(Client client) {
        ClientDO clientDO=new ClientDO();
        BeanUtils.copyProperties(client,clientDO);
        clientDOMapper.updateByPrimaryKeySelective(clientDO);
    }
}
