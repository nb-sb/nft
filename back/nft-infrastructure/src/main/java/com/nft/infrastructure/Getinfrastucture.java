package com.nft.infrastructure;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Getinfrastucture {
    @Value("${test.qqq}")
    String qqq;

    public String get() {
        System.err.println("qqq: "+qqq);
        return "infrastucture";

    }
}
