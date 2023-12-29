package com.nft.common.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @desc   使用token验证用户是否登录
 * @author zm
 **/
@Repository
@Slf4j
public class TokenUtils {
    //设置过期时间
    private static final long EXPIRE_DATE=30*60*100000;
    //token秘钥
    private static final String TOKEN_SECRET = "nbsb20231108";

    public static String token (String username,String password){

        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
//            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password).withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){

            e.printStackTrace();
            return  null;
        }
        return token;
    }

    public static boolean verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @params [token]需要校验的串
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
//            System.out.println(e);
            log.error(String.valueOf(e));
//            e.printStackTrace();
            return  false;
        }
    }
    public static Map<String, String> decodeToken(String token) {
        Optional<String> tokenOpt = Optional.ofNullable(token);
        try {
            if (tokenOpt.isPresent()) {
                try {
                    Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT jwt = verifier.verify(token);

                    Map<String, String> claims = new HashMap<>();
                    claims.put("username", jwt.getClaim("username").asString());
                    claims.put("password", jwt.getClaim("password").asString());

                    return claims;
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                    return null;
                }
            } else {
                log.error("传入token为空");
                return null;
            }
        } catch (JWTDecodeException e) {
            log.error(e.getMessage());
            return null;
        }

    }

//    public static void main(String[] args) {
//        String username ="zhangsan";
//        String password = "123";
//        String token = token(username,password);
//        System.out.println(token);
//        boolean b = verify(token);
//        System.out.println(b);
//    }
}
