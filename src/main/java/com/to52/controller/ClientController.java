package com.to52.controller;
import com.to52.entity.Client;
import com.to52.response.CommonReturnType;
import com.to52.service.ClientService;
import org.apache.commons.lang3.StringUtils;//Operations sur les chaînes de caractères
import org.springframework.beans.factory.annotation.Autowired;//il va crée l’instance de la classe, et il va faire l’injection de dépendance (injecter l’objet créeé de la classe)
import org.springframework.stereotype.Controller;//Indiquer la classe est un "Controller"
import org.springframework.web.bind.annotation.*;//importer les annotations pour lier les requêtes aux contrôleurs et aux méthodes de gestionnaire, ainsi que pour lier les paramètres de requête aux arguments de méthode.
import sun.misc.BASE64Encoder;//Importer la classe pour chiffrer les chaînes de caractères
import javax.servlet.http.HttpServletRequest;//Importer la interface httpservletRequest peut autoriser les informations de requête pour les servlets HTTP. L'objet de HttpServletRequest est créé par le conteneur Servlet, puis transmis à la méthode de service (doGet (), doPost (), etc.) du servlet.
import javax.servlet.http.HttpSession;//Fournit un moyen d'identifier un utilisateur via plusieurs demandes de page ou visites sur un site Web et de stocker des informations sur cet utilisateur
import java.io.UnsupportedEncodingException;//Importer la classe exception pour indiquer le chiffrement des chaînes de caractères n'est pas supported.
import java.security.MessageDigest;//Cette classe MessageDigest fournit aux applications la fonctionnalité d'un algorithme de synthèse de message
import java.security.NoSuchAlgorithmException;//Cette exception est levée lorsqu'un algorithme cryptographique particulier est demandé mais n'est pas disponible dans l'environnement.

/**
 * @author tzhang
 * @date 2019/5/23 {1:39}
 */
@Controller("client")//indiquer la classe est un controlleur nomme client
@RequestMapping("/client")//indiquer l'adresse de request http
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")//Il permet au navigateur de faire des XMLHttpRequestdemandes aux serveurs d’origine croisée, en surmontant les limites de la seule utilisation homologue d’AJAX .

public class ClientController {
    @Autowired
    private ClientService clientService;//injecter l'objet de classe ClientService
    @Autowired
    private HttpServletRequest httpServletRequest;//injecter l'objet de classe HttpServletRequest

    /**
     * la function pour obtenir le id du client de session currente
     * @param session la session currente
     * @return
     */
    //indiquer l'adresse de request http d'obtenir le cliendId de session currente
    @RequestMapping(value = "/getCid", method = RequestMethod.GET)
    @ResponseBody
    public Integer currentUserNameSimple(HttpSession session) {
        //si le client de session currente est en status connexion
        if(session.getAttribute("cid")!=null){
            //retourner le id du client
            return (Integer)session.getAttribute("cid");
        }
        //si le client de session currente est status non connexion
        else return 0;
    }

    /**
     * la function pour se login sur le site
     * @param client
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    //indiquer l'adresse de request http de se connecter
    @RequestMapping(value = "/login",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType login(
            @RequestBody Client client) throws UnsupportedEncodingException, NoSuchAlgorithmException {
            //si le email et le mot de passe n'est pas vide
            if(StringUtils.isNotEmpty(client.getEmail())&&StringUtils.isNotEmpty(client.getPassword())){
                //chiffrer la mot de passe
                String passwordEncrpt=this.EncodedByMD5(client.getPassword());
                //utilise le service login
                client=clientService.login(client.getEmail(),passwordEncrpt);
                if(client!=null){
                    //stocker la status de login dans la session
                    this.httpServletRequest.getSession().setAttribute("cid",client.getCid());
                    //retourner l'objet de client
                    return CommonReturnType.create(client);
                }
                else{
                    //retourner l'objet de CommonReturnType avec l'echec de se conneter
                    return CommonReturnType.create("null","100","CLIENT_NOT_FOUND");
                }
            }
        //retourner l'objet de CommonReturnType avec l'echec de se conneter
        return CommonReturnType.create("null","101","PARAM_NOT_COMPLET");
    }

    /**
     * la function pour s'inscrire l'application
     * @param client
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    //indiquer l'adresse de request http de s'inscrire
    @RequestMapping(value = "/register",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType register(
            @RequestBody Client client
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        client.setPassword(client.getPassword());
        //déterminer la validité  des paramètres nécessaire pour s'inscrire
        if(StringUtils.isNotEmpty(client.getEmail())&&
                StringUtils.isNotEmpty(client.getPassword())&&
                StringUtils.isNotEmpty(client.getFirstName())&&
                StringUtils.isNotEmpty(client.getLastName())&&
                String.valueOf(client.getSex())!=null
        ){
            //chiffrer la mot de passe
            String passwordEncrpt=this.EncodedByMD5(client.getPassword());
            client.setPassword(passwordEncrpt);
            client.setPrivilege(0);
            //faire la inscription
            clientService.register(client);
            //retourner l'objet de client
            return CommonReturnType.create(client);
        }
        else{
            //retourner l'objet de CommonReturnType avec l'echec de s'inscrire
            return CommonReturnType.create("null","101","PARAM_NOT_COMPLET");
        }
    }
    //indiquer l'adresse de request http de saisir l'information de client avec le id
    @RequestMapping(value = "/getClientById",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getClientById(
            @RequestBody Client client
    ) {
        //saisir le client selon le id
        client=clientService.getClientById(client.getCid());
        //si le client exist
       if(client!=null){
           //retourner l'objet de client
           return CommonReturnType.create(client);
        }
        //retourner l'objet de CommonReturnType avec l'echec de saisir
        return CommonReturnType.create(null,"100","CLIENT_NOT_FOUND");
    }
    /**
     * la function pour faire mise à jour les informations individuelles
     * @param client
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    //indiquer l'adresse de request http de mise a jour l'information de client avec le id
    @RequestMapping(value = "/updateClientInfo",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType updateClientInfo(
            @RequestBody Client client
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //saisir le client selon le id
        Client client2=clientService.getClientById(client.getCid());
        Client clientUpdate=new Client();
        //si le client exist
        if(client2!=null){
            //faire mise à jour les informations
            clientUpdate.setCid(client.getCid());
            //si utilisateur donne le valeur de email
            if(StringUtils.isNotEmpty(client.getEmail())){
                clientUpdate.setEmail(client.getEmail());
            }
            //si utilisateur donne le valeur de telephone
            if(StringUtils.isNotEmpty(client.getPhone())){
                clientUpdate.setPhone(client.getPhone());
            }
            //si utilisateur donne le valeur de addresse
            if(StringUtils.isNotEmpty(client.getAddress())){
                clientUpdate.setAddress(client.getAddress());
            }
            //si utilisateur donne le valeur de mot de passe
            if(StringUtils.isNotEmpty(client.getPassword())){
                clientUpdate.setPassword(EncodedByMD5(client.getPassword()));
            }
            //utilise le service updateClientInfo
            clientService.updateClientInfo(clientUpdate);
            Client client1=clientService.getClientById(client.getCid());
            //retouner l'objet de client apres mise a jour
            return CommonReturnType.create(client1);
        }
        //retourner l'objet de CommonReturnType avec l'echec de mise a jour
        return CommonReturnType.create(null,"100","CLIENT_NOT_FOUND");
       }

    /**
     * la function pour chiffrer le mot de passe
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private String EncodedByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();
        String newstr=base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        //retouner le chaine de caractere apres chiffrement
        return newstr;
    }

}