package com.to52;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;//annotation pour spécifier le chemin du package de la classe Mapper à analyser
import org.springframework.web.bind.annotation.RestController;
/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages={"com.to52"})
@RestController
@MapperScan("com.to52.dao.daoMapper")//spécifier le chemin du package de la classe Mapper à analyser
public class App 
{
    @RequestMapping("/")
    public String home(){
        return "hello world";

    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class,args);

    }
}
