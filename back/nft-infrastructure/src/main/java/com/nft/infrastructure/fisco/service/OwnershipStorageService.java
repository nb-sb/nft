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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@NoArgsConstructor
@Data
public class OwnershipStorageService {
  public static final String ABI = IOUtil.readResourceAsString("abi/OwnershipStorage.abi");

  public static final String BINARY = IOUtil.readResourceAsString("bin/ecc/OwnershipStorage.bin");

  public static final String SM_BINARY = IOUtil.readResourceAsString("bin/sm/OwnershipStorage.bin");

  @Value("${system.contract.ownershipStorageAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse transfer(OwnershipStorageTransferInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "transfer", input.toArgs());
  }

  public CallResponse selectByHash(OwnershipStorageSelectByHashInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "selectByHash", input.toArgs());
  }

  public CallResponse selectByUserAddr(OwnershipStorageSelectByUserAddrInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "selectByUserAddr", input.toArgs());
  }

  public TransactionResponse addOwnership(OwnershipStorageAddOwnershipInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addOwnership", input.toArgs());
  }
  public TransactionResponse selectInfo(OwnershipStorageSelectInfoInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "selectInfo", input.toArgs());
  }


}
