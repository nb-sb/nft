package com.nft.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Repository
public class SendEmail {
    @Value("${email.pswd}")
    String emailpswd;

    @Value("${email.from}")
    String emailfrom;
    private static final String MAIL_HOST = "smtp.qq.com"; // 发送邮件的主机
//    private static final String FROM = "oksouti@vip.qq.com"; // 发件人邮箱地址

    /**
     * 用qq邮箱发送一个简单邮件
     *
     * @param subject
     * @param text
     * @param toRecipients 接收邮件，逗号分隔
     * @throws AddressException
     * @throws MessagingException
     */
    public boolean sentSimpleMail(String subject, String text, String toRecipients)
             {
        try {
            /*
             * 初始化JavaMail会话
             */
            Properties props = System.getProperties(); // 获得系统属性配置，用于连接邮件服务器的参数配置
            props.setProperty("mail.smtp.host", MAIL_HOST); // 发送邮件的主机
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getInstance(props, null);// 获得Session对象
            session.setDebug(true); // 设置是否显示debug信息,true 会在控制台显示相关信息

            /*
             * 创建邮件消息，发送邮件
             */
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailfrom));

            // To: 收件人
            // message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toRecipient));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toRecipients, false));
            // To: 增加收件人（可选）
            // message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toRecipient);
            // Cc: 抄送（可选）
            // message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(ccRecipient));
            // Bcc: 密送（可选）
            // message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(bccRecipient));

            message.setSubject(subject); // 邮件标题
            message.setText(text); // 邮件内容

            // 简单发送邮件的方式
            Transport.send(message, emailfrom, emailpswd); // 授权码
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }

    }

//    public static void main(String[] args) throws AddressException, MessagingException {
//        new SendEmail().sentSimpleMail("标题test", "邮件内容test", "3500079813@qq.com");
//    }
}
