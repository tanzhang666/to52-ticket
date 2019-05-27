package com.to52.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author tzhang
 * @date 2019/5/27 {15:30}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilTest {
    @Resource
    public MailTestSender mailTestSender;
    @Test
    public void sendMailTest(){
        mailTestSender.sendMailValidateOrder("tan.zhang@utbm.fr");
    }
}
