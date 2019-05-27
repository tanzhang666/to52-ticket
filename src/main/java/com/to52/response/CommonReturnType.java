package com.to52.response;

/**
 * @author tzhang
 * @date 2019/5/23 {13:30}
 */
public class CommonReturnType {
    private String code;
    private Object data;
    private String message;
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"200","success");
    }
    public static CommonReturnType create(Object result,String code,String message){
        CommonReturnType type=new CommonReturnType();
        type.setCode(code);
        type.setMessage(message);
        type.setData(result);
        return type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
