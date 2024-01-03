package com.nft.infrastructure.fisco.service;

import com.nft.common.FISCO.Utils.SdkUtils;
import com.nft.common.Utils.IOUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.nft.infrastructure.fisco.model.bo.*;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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
  @Autowired
  private SdkUtils sdkUtils;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  /**
   * 加载用户账户切换业务
   */
  private TransactionResponse getTransactionResponse(String key, String funcName, List<Object> params) throws Exception {
    String configFile = sdkUtils.configFile;
    System.out.println("configFile: "+configFile);
    client = sdkUtils.sdk.getClient(Integer.valueOf("1"));
    client.getCryptoSuite().createKeyPair(key);
    CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
    // 过创建和使用AssembleTransactionProcessor对象来调用和查询等操作。不部署合约，那么就不需要复制binary文件
    AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory
            .createAssembleTransactionProcessor(client, cryptoKeyPair);

    TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponse(this.address, ABI, funcName, params);
//    // 使用同步方式发送交易
//    TransactionResponse transactionResponse = transactionProcessor
//            .sendTransactionAndGetResponseByContractLoader("OwnershipStorage", this.address, funcName, params);
    return transactionResponse;
  }

  public TransactionResponse transfer(OwnershipStorageTransferInputBO input,String privatekey) throws Exception {
    return getTransactionResponse(privatekey, "transfer", input.toArgs());
//    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "transfer", input.toArgs());
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
