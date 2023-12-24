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
public class SellStroageService {
  public static final String ABI = IOUtil.readResourceAsString("abi/SellStroage.abi");

  public static final String BINARY = IOUtil.readResourceAsString("bin/ecc/SellStroage.bin");

  public static final String SM_BINARY = IOUtil.readResourceAsString("bin/sm/SellStroage.bin");

  @Value("${system.contract.sellStroageAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse createSell(SellStroageCreateSellInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "createSell", input.toArgs());
  }

  public TransactionResponse stopSell(SellStroageStopSellInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "stopSell", input.toArgs());
  }

  public CallResponse existSell(SellStroageExistSellInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "existSell", input.toArgs());
  }

  public CallResponse catRemain(SellStroageCatRemainInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "catRemain", input.toArgs());
  }

  public CallResponse catAuthor(SellStroageCatAuthorInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "catAuthor", input.toArgs());
  }

  public TransactionResponse decrRemain(SellStroageDecrRemainInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "decrRemain", input.toArgs());
  }

  public CallResponse catTotal(SellStroageCatTotalInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "catTotal", input.toArgs());
  }
}
