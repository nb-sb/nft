//package com.nft.domain.fisco;
//
//
//import com.nft.common.Utils.IOUtil;
//import org.fisco.bcos.sdk.BcosSDK;
//import org.fisco.bcos.sdk.client.Client;
//import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
//import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
//import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
//import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//@Service
//public class Htest {
//    public static final String ABI = IOUtil.readResourceAsString("fiscobcos/abi/HelloWorld.abi");
//
//    public static final String BINARY = IOUtil.readResourceAsString("fiscobcos/bin/HelloWorld.bin");
//
//    public static final String SM_BINARY = IOUtil.readResourceAsString("fiscobcos/bin/HelloWorld.bin");
//
//    @Value("${system.contract.getandSetAddress}")
//    private String address;
//
//    @Autowired
//    private SdkUtils sdkUtils;
//
//    @Autowired
//    private Client client;
//
//    AssembleTransactionProcessor txProcessor;
//
//    @PostConstruct
//    public void init() throws Exception {
//        this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
//    }
//
//    /**
//     * 加载用户账户切换业务
//     */
//    private TransactionResponse getTransactionResponse(String key, String funcName, List<Object> params) throws Exception {
//        client = sdkUtils.sdk.getClient(Integer.valueOf("1"));
//        System.out.println("sdk config path : "+sdkUtils.getconfig());
//        client.getCryptoSuite().createKeyPair(key);
//        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
//        // 过创建和使用AssembleTransactionProcessor对象来调用和查询等操作。如果不部署合约，那么就不需要复制binary文件
//        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory
//                .createAssembleTransactionProcessor(client, cryptoKeyPair, "app/src/main/resources/fiscobcos/abi/", "");
//        // 使用同步方式发送交易
//        TransactionResponse transactionResponse = transactionProcessor
//                .sendTransactionAndGetResponseByContractLoader("HelloWorld", this.address, funcName, params);
//        return transactionResponse;
//    }
//
//    public TransactionResponse set(List<Object> input,String key) throws Exception {
//        return getTransactionResponse(key, "set", input);
//    }
//
//
//    public TransactionResponse get() throws Exception {
//        return getTransactionResponse("86ad08e55fc55eb29fe15fc797f20df60412e187720e0be9260407912cd3a591", "get", Arrays.asList());
//    }
//
//}
