package com.nft.infrastructure.fisco.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import com.nft.common.FISCO.Config.SystemConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Slf4j
public class ServiceManager {
	@Autowired
	private SystemConfig config;

	@Autowired
	private Client client;

	List<String> hexPrivateKeyList;

	@PostConstruct
	public void init() {
		hexPrivateKeyList = Arrays.asList(this.config.getHexPrivateKey().split(","));
	}

	/**
	 * @notice: must use @Qualifier("UserStorageService") with @Autowired to get this Bean
	 */
	@Bean("UserStorageService")
	public Map<String, UserStorageService> initUserStorageServiceManager() throws Exception {
		Map<String, UserStorageService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
		for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
			String privateKey = this.hexPrivateKeyList.get(i);
			if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
				privateKey = privateKey.substring(2);
			}
			if (privateKey.isEmpty()) {
				continue;
			}
			org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
			org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
			String userAddress = cryptoKeyPair.getAddress();
			log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
			UserStorageService userStorageService = new UserStorageService();
			userStorageService.setAddress(this.config.getContract().getUserStorageAddress());
			userStorageService.setClient(this.client);
			org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor =
					org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
			userStorageService.setTxProcessor(txProcessor);
			serviceMap.put(userAddress, userStorageService);
		}
		log.info("++++++++UserStorageService map:{}", serviceMap);
		return serviceMap;
	}

	/**
	 * @notice: must use @Qualifier("SellStroageService") with @Autowired to get this Bean
	 */
	@Bean("SellStroageService")
	public Map<String, SellStroageService> initSellStroageServiceManager() throws Exception {
		Map<String, SellStroageService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
		for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
			String privateKey = this.hexPrivateKeyList.get(i);
			if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
				privateKey = privateKey.substring(2);
			}
			if (privateKey.isEmpty()) {
				continue;
			}
			org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
			org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
			String userAddress = cryptoKeyPair.getAddress();
			log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
			SellStroageService sellStroageService = new SellStroageService();
			sellStroageService.setAddress(this.config.getContract().getSellStroageAddress());
			sellStroageService.setClient(this.client);
			org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor =
					org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
			sellStroageService.setTxProcessor(txProcessor);
			serviceMap.put(userAddress, sellStroageService);
		}
		log.info("++++++++SellStroageService map:{}", serviceMap);
		return serviceMap;
	}

	/**
	 * @notice: must use @Qualifier("OwnershipStorageService") with @Autowired to get this Bean
	 */
	@Bean("OwnershipStorageService")
	public Map<String, OwnershipStorageService> initOwnershipStorageServiceManager() throws Exception {
		Map<String, OwnershipStorageService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
		for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
			String privateKey = this.hexPrivateKeyList.get(i);
			if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
				privateKey = privateKey.substring(2);
			}
			if (privateKey.isEmpty()) {
				continue;
			}
			org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
			org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
			String userAddress = cryptoKeyPair.getAddress();
			log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
			OwnershipStorageService ownershipStorageService = new OwnershipStorageService();
			ownershipStorageService.setAddress(this.config.getContract().getOwnershipStorageAddress());
			ownershipStorageService.setClient(this.client);
			org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor =
					org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
			ownershipStorageService.setTxProcessor(txProcessor);
			serviceMap.put(userAddress, ownershipStorageService);
		}
		log.info("++++++++OwnershipStorageService map:{}", serviceMap);
		return serviceMap;
	}

	/**
	 * @notice: must use @Qualifier("DetailStorageService") with @Autowired to get this Bean
	 */
	@Bean("DetailStorageService")
	public Map<String, DetailStorageService> initDetailStorageServiceManager() throws Exception {
		Map<String, DetailStorageService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
		for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
			String privateKey = this.hexPrivateKeyList.get(i);
			if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
				privateKey = privateKey.substring(2);
			}
			if (privateKey.isEmpty()) {
				continue;
			}
			org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
			org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
			String userAddress = cryptoKeyPair.getAddress();
			log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
			DetailStorageService detailStorageService = new DetailStorageService();
			detailStorageService.setAddress(this.config.getContract().getDetailStorageAddress());
			detailStorageService.setClient(this.client);
			org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor =
					org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
			detailStorageService.setTxProcessor(txProcessor);
			serviceMap.put(userAddress, detailStorageService);
		}
		log.info("++++++++DetailStorageService map:{}", serviceMap);
		return serviceMap;
	}
}
