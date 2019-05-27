package com.to52;
import com.to52.controller.ClientController;
import com.to52.service.ClientService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages={"com.to52"})
@RestController
@MapperScan("com.to52.dao.daoMapper")
public class App 
{
    @Autowired
    private ClientController clientController;
    @RequestMapping("/")
    public String home() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return clientController.login("tan.zhang@utbm.fr","123456").getData().toString();
//        try {
//        return clientController.register("tan","zhang",1,"tan.zhang@utbm.fr","0650227521","15 faubourg du lyon,belfort","123456",1).getCode();
//
//        }catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        }catch (NoSuchAlgorithmException e2){
//            e2.printStackTrace();
//        }
//        return "error";
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class,args);

    }
}
