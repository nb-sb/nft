package com.nft.infrastructure.fisco.service;

import com.nft.common.Utils.IOUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.nft.infrastructure.fisco.model.bo.*;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
@NoArgsConstructor
@Data
public class UserStorageService {
  public static final String ABI = IOUtil.readResourceAsString("abi/UserStorage.abi");

  public static final String BINARY = IOUtil.readResourceAsString("bin/ecc/UserStorage.bin");

  public static final String SM_BINARY = IOUtil.readResourceAsString("bin/sm/UserStorage.bin");

  @Value("${system.contract.userStorageAddress}")
  private String address;

  @Resource
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse addUser(UserStorageAddUserInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addUser", input.toArgs());
  }

  public CallResponse existMap(UserStorageExistMapInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "existMap", input.toArgs());
  }

  public TransactionResponse updateMap(UserStorageUpdateMapInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "updateMap", input.toArgs());
  }

  public CallResponse readMap(UserStorageReadMapInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "readMap", input.toArgs());
  }

  public TransactionResponse deleteMap(UserStorageDeleteMapInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "deleteMap", input.toArgs());
  }
}
