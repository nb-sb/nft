package com.nft;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.mail.MessagingException;


@Log4j2
public class Test123 {
    @Test
    public void ee() {
        log.info("this is info log");
        log.error("this is error log");
        log.debug("this is debug log");
        log.warn("this is warn log");
        log.trace("this is trace log");
        log.fatal("this is fatal log");
    }


    @Test
    public void email() throws MessagingException {
//        boolean b = sendEmail.sentSimpleMail("123", "456", "3500079813@qq.com");
//        System.out.println(b);
    }
}
