package com.nft.infrastructure.fisco.service;

import com.nft.common.Utils.IOUtil;
import com.nft.infrastructure.fisco.model.bo.*;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class DetailStorageService {
  public static final String ABI = IOUtil.readResourceAsString("abi/DetailStorage.abi");

  public static final String BINARY = IOUtil.readResourceAsString("bin/ecc/DetailStorage.bin");

  public static final String SM_BINARY = IOUtil.readResourceAsString("bin/sm/DetailStorage.bin");

  @Value("${system.contract.detailStorageAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse selectByUserTranfer(DetailStorageSelectByUserTranferInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "selectByUserTranfer", input.toArgs());
  }

  public CallResponse selectByHash(DetailStorageSelectByHashInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "selectByHash", input.toArgs());
  }

  public CallResponse selectByUserAccept(DetailStorageSelectByUserAcceptInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "selectByUserAccept", input.toArgs());
  }

  public TransactionResponse addDetail(DetailStorageAddDetailInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addDetail", input.toArgs());
  }
}
