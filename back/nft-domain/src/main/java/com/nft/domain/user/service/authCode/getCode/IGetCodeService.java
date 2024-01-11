package com.nft.domain.user.service.authCode.getCode;

import javax.mail.MessagingException;

public interface IGetCodeService {
    String getCode(String target) throws MessagingException;
}
