package com.to52.controller;
import com.to52.entity.Client;
import com.to52.response.CommonReturnType;
import com.to52.service.ClientService;
import com.to52.service.TicketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
@Controller("client")
@RequestMapping("/client")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private TicketService ticketService;
    @RequestMapping(value = "/login",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType login(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
            if(StringUtils.isNotEmpty(email)&&StringUtils.isNotEmpty(password)){
                String passwordEncrpt=this.EncodedByMD5(password);
                Client client=clientService.login(email,passwordEncrpt);
                if(client!=null){
                    return CommonReturnType.create(client);
                }
                else{
                    return CommonReturnType.create("null","100","CLIENT_NOT_FOUND");
                }
            }
        return CommonReturnType.create("null","101","PARAM_NOT_COMPLET");
    }
    @RequestMapping(value = "/register",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType register(
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName") String lastName,
            @RequestParam(name = "sex") Integer sex,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "privilege") Integer privilege
            ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(StringUtils.isNotEmpty(email)&&
            StringUtils.isNotEmpty(password)&&
            StringUtils.isNotEmpty(firstName)&&
            StringUtils.isNotEmpty(password)&&
            String.valueOf(sex)!=null&&
            String.valueOf(privilege)!=null
        ){
            String passwordEncrpt=this.EncodedByMD5(password);
            Client client=new Client();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setSex(sex);
            client.setEmail(email);
            client.setPassword(passwordEncrpt);
            client.setPrivilege(privilege);
            if(StringUtils.isNotEmpty(phone)){
                client.setPhone(phone);
            }
            if(StringUtils.isNotEmpty(address)){
                client.setAddress(address);
            }
            clientService.register(client);
            return CommonReturnType.create(client);
        }
        else{
            return CommonReturnType.create("null","101","PARAM_NOT_COMPLET");
        }
    }
    @RequestMapping(value = "/updateClientInfo",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType updateClientInfo(
            @RequestParam(name="cid") Integer cid,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "password") String password
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Client client=clientService.getClientById(cid);
        Client clientUpdate=new Client();
        if(client!=null){
            clientUpdate.setCid(cid);
            if(StringUtils.isNotEmpty(email)){
                clientUpdate.setEmail(email);
            }
            if(StringUtils.isNotEmpty(phone)){
                clientUpdate.setPhone(phone);
            }
            if(StringUtils.isNotEmpty(address)){
                clientUpdate.setAddress(address);
            }
            if(StringUtils.isNotEmpty(password)){
                clientUpdate.setPassword(EncodedByMD5(password));
            }
            clientService.updateClientInfo(clientUpdate);
            Client client1=clientService.getClientById(cid);
            return CommonReturnType.create(client1);
        }
        return CommonReturnType.create(null,"100","CLIENT_NOT_FOUND");
       }
    private String EncodedByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();
        String newstr=base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

}