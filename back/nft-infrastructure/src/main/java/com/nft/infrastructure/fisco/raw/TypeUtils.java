package com.nft.infrastructure.fisco.raw;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class TypeUtils extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052348015600f57600080fd5b50604380601d6000396000f3fe6080604052600080fdfea265627a7a72305820a40f04b730bc216ca3eec4491c043c27fc55427a321fff4975a3add924cbd90b6c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    protected TypeUtils(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static TypeUtils load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new TypeUtils(contractAddress, client, credential);
    }

    public static TypeUtils deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(TypeUtils.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
