package com.to52.response;
/**
 * @author tzhang
 * @date 2019/5/23 {13:30}
 */

/**
 * la classe pour recevoir le reponse du request http
 */
public class CommonReturnType {
    private String code;//le code du reponse http pour indiquer le status de request http
    private Object data;//l'objet de reponse
    private String message;//le message du reponse http pour indiquer le status de request http

    /**
     * la function pour creer l'objet CommonReturnType quand le request arrive a obtenir les donnee
     * @param result
     * @return
     */
    public static CommonReturnType create(Object result){
        //retourner l'objet avec les donnees et le code 200 et le message 'success'
        return CommonReturnType.create(result,"200","success");
    }
    /**
     * la function pour creer l'objet CommonReturnType quand le request n'arrive pas obtenir les donnee
     * @param result
     * @return
     */
    public static CommonReturnType create(Object result,String code,String message){
        //initializer l'objet de CommonReturnType
        CommonReturnType type=new CommonReturnType();
        //definir la valeur de code
        type.setCode(code);
        //definir la valeur de message
        type.setMessage(message);
        //definir les donnees
        type.setData(result);
        //retourner l'objet de CommonReturnType
        return type;
    }

    /**
     * la function getter pour l'attribut data
     * @return
     */
    public Object getData() {
        return data;
    }
    /**
     * la function setter pour l'attribut data
     * @return
     */
    public void setData(Object data) {
        this.data = data;
    }
    /**
     * la function getter pour l'attribut code
     * @return
     */
    public String getCode() {
        return code;
    }
    /**
     * la function setter pour l'attribut code
     * @return
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * la function getter pour l'attribut message
     * @return
     */
    public String getMessage() {
        return message;
    }
    /**
     * la function setter pour l'attribut message
     * @return
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
