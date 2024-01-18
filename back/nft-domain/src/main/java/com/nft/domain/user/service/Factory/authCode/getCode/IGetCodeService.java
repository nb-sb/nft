package com.nft.domain.user.service.Factory.authCode.getCode;

import com.nft.domain.user.model.entity.AuthCodeActionEntity;

import javax.mail.MessagingException;

public interface IGetCodeService<T extends AuthCodeActionEntity.AuthEntity>  {
    AuthCodeActionEntity<T> getCode(String target) throws MessagingException;
}
