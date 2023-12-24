package com.nft.infrastructure.fisco.raw;

import org.fisco.bcos.sdk.abi.FunctionEncoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Int256;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class AbstractBean extends Contract {
    public static final String[] BINARY_ARRAY = {"60806040523480156200001157600080fd5b506040516200137538038062001375833981018060405262000037919081019062000ca0565b60006110019050606060026040519080825280602002602001820160405280156200007f57816020015b6200006b620009b2565b815260200190600190039081620000615790505b509050620000a184620003236401000000000262000009176401000000009004565b816000815181101515620000b157fe5b90602001906020020181905250620000dd83620003236401000000000262000009176401000000009004565b816001815181101515620000ed57fe5b906020019060200201819052508173ffffffffffffffffffffffffffffffffffffffff166356004b6a87876200018e85620001716040805190810160405280600181526020017f2c00000000000000000000000000000000000000000000000000000000000000815250620003236401000000000262000009176401000000009004565b620003536401000000000262000037179091906401000000009004565b6040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401620001ca9392919062000dac565b602060405180830381600087803b158015620001e557600080fd5b505af1158015620001fa573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525062000220919081019062000c74565b5060806040519081016040528087815260200186815260200185815260200160006040519080825280602002602001820160405280156200027657816020015b6060815260200190600190039081620002605790505b5081525060008082015181600001908051906020019062000299929190620009cc565b506020820151816001019080519060200190620002b8929190620009cc565b506040820151816002019080519060200190620002d7929190620009cc565b506060820151816003019080519060200190620002f692919062000a53565b509050506200031760008585620004e5640100000000026401000000009004565b50505050505062000eb5565b6200032d62000aba565b600060208301905060408051908101604052808451815260200182815250915050919050565b6060600082511415620003795760206040519081016040528060008152509050620004df565b60006001835103846000015102905060008090505b8351811015620003c7578381815181101515620003a757fe5b90602001906020020151600001518201915080806001019150506200038e565b506060816040519080825280601f01601f191660200182016040528015620003fe5781602001600182028038833980820191505090505b509050600060208201905060008090505b8551811015620004d7576200046d8287838151811015156200042d57fe5b906020019060200201516020015188848151811015156200044a57fe5b906020019060200201516000015162000686640100000000026401000000009004565b85818151811015156200047c57fe5b9060200190602002015160000151820191506001865103811015620004c957620004bf828860200151896000015162000686640100000000026401000000009004565b8660000151820191505b80806001019150506200040f565b508193505050505b92915050565b826003018290806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906200052692919062000ad4565b50506200053262000aba565b6200055182620003236401000000000262000009176401000000009004565b90506200055d62000aba565b620005b16040805190810160405280600181526020017f2c00000000000000000000000000000000000000000000000000000000000000815250620003236401000000000262000009176401000000009004565b905060006001620005da8385620006d3640100000000026200019b179091906401000000009004565b01905060008090505b818110156200067d5786600301620006316200061785876200076e6401000000000262000212179091906401000000009004565b6200079b640100000000026200022c176401000000009004565b90806001815401808255809150509060018203906000526020600020016000909192909190915090805190602001906200066d92919062000ad4565b50508080600101915050620005e3565b50505050505050565b5b602081101515620006ae578151835260208301925060208201915060208103905062000687565b60006001826020036101000a0390508019835116818551168181178652505050505050565b60008082600001516200070885600001518660200151866000015187602001516200080f640100000000026401000000009004565b0190505b8360000151846020015101811115156200076757818060010192505082600001516200075e8560200151830386600001510383866000015187602001516200080f640100000000026401000000009004565b0190506200070c565b5092915050565b6200077862000aba565b62000794838383620008ff640100000000026401000000009004565b5092915050565b60608082600001516040519080825280601f01601f191660200182016040528015620007d65781602001600182028038833980820191505090505b509050600060208201905062000805818560200151866000015162000686640100000000026401000000009004565b8192505050919050565b60008084905060008685111515620008ef57602085111515620008a357600060018660200360080260020a0319600102905060008186511690506000878a8a0103905060008386511690505b82811415156200089457818610151562000880578a8a019650505050505050620008f7565b85806001019650508386511690506200085b565b859650505050505050620008f7565b60008585209050600091505b85880382111515620008ed576000868420905080821415620008d85783945050505050620008f7565b600184019350508180600101925050620008af565b505b868601925050505b949350505050565b6200090962000aba565b60006200093885600001518660200151866000015187602001516200080f640100000000026401000000009004565b905084602001518360200181815250508460200151810383600001818152505084600001518560200151018114156200097c576000856000018181525050620009a7565b8360000151836000015101856000018181510391508181525050836000015181018560200181815250505b829150509392505050565b604080519081016040528060008152602001600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1062000a0f57805160ff191683800117855562000a40565b8280016001018555821562000a40579182015b8281111562000a3f57825182559160200191906001019062000a22565b5b50905062000a4f919062000b5b565b5090565b82805482825590600052602060002090810192821562000aa7579160200282015b8281111562000aa657825182908051906020019062000a95929190620009cc565b509160200191906001019062000a74565b5b50905062000ab6919062000b83565b5090565b604080519081016040528060008152602001600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1062000b1757805160ff191683800117855562000b48565b8280016001018555821562000b48579182015b8281111562000b4757825182559160200191906001019062000b2a565b5b50905062000b57919062000b5b565b5090565b62000b8091905b8082111562000b7c57600081600090555060010162000b62565b5090565b90565b62000bb191905b8082111562000bad576000818162000ba3919062000bb4565b5060010162000b8a565b5090565b90565b50805460018160011615610100020316600290046000825580601f1062000bdc575062000bfd565b601f01602090049060005260206000209081019062000bfc919062000b5b565b5b50565b600062000c0e825162000e64565b905092915050565b600082601f830112151562000c2a57600080fd5b815162000c4162000c3b8262000e2c565b62000dfe565b9150808252602083016020830185838301111562000c5e57600080fd5b62000c6b83828462000e6e565b50505092915050565b60006020828403121562000c8757600080fd5b600062000c978482850162000c00565b91505092915050565b6000806000806080858703121562000cb757600080fd5b600085015167ffffffffffffffff81111562000cd257600080fd5b62000ce08782880162000c16565b945050602085015167ffffffffffffffff81111562000cfe57600080fd5b62000d0c8782880162000c16565b935050604085015167ffffffffffffffff81111562000d2a57600080fd5b62000d388782880162000c16565b925050606085015167ffffffffffffffff81111562000d5657600080fd5b62000d648782880162000c16565b91505092959194509250565b600062000d7d8262000e59565b80845262000d9381602086016020860162000e6e565b62000d9e8162000ea4565b602085010191505092915050565b6000606082019050818103600083015262000dc8818662000d70565b9050818103602083015262000dde818562000d70565b9050818103604083015262000df4818462000d70565b9050949350505050565b6000604051905081810181811067ffffffffffffffff8211171562000e2257600080fd5b8060405250919050565b600067ffffffffffffffff82111562000e4457600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b6000819050919050565b60005b8381101562000e8e57808201518184015260208101905062000e71565b8381111562000e9e576000848401525b50505050565b6000601f19601f8301169050919050565b6104b08062000ec56000396000f3fe6080604052600080fd5b61001161045c565b600060208301905060408051908101604052808451815260200182815250915050919050565b606060008251141561005b5760206040519081016040528060008152509050610195565b60006001835103846000015102905060008090505b83518110156100a657838181518110151561008757fe5b9060200190602002015160000151820191508080600101915050610070565b506060816040519080825280601f01601f1916602001820160405280156100dc5781602001600182028038833980820191505090505b509050600060208201905060008090505b855181101561018d5761013782878381518110151561010857fe5b9060200190602002015160200151888481518110151561012457fe5b906020019060200201516000015161028e565b858181","518110151561014557fe5b906020019060200201516000015182019150600186510381101561018057610176828860200151896000015161028e565b8660000151820191505b80806001019150506100ed565b508193505050505b92915050565b60008082600001516101bf85600001518660200151866000015187602001516102d9565b0190505b83600001518460200151018111151561020b57818060010192505082600001516102038560200151830386600001510383866000015187602001516102d9565b0190506101c3565b5092915050565b61021a61045c565b6102258383836103be565b5092915050565b60608082600001516040519080825280601f01601f1916602001820160405280156102665781602001600182028038833980820191505090505b5090506000602082019050610284818560200151866000015161028e565b8192505050919050565b5b6020811015156102b4578151835260208301925060208201915060208103905061028f565b60006001826020036101000a0390508019835116818551168181178652505050505050565b600080849050600086851115156103ae5760208511151561036657600060018660200360080260020a0319600102905060008186511690506000878a8a0103905060008386511690505b8281141515610358578186101515610345578a8a0196505050505050506103b6565b8580600101965050838651169050610323565b8596505050505050506103b6565b60008585209050600091505b858803821115156103ac57600086842090508082141561039857839450505050506103b6565b600184019350508180600101925050610372565b505b868601925050505b949350505050565b6103c661045c565b60006103e485600001518660200151866000015187602001516102d9565b90508460200151836020018181525050846020015181038360000181815250508460000151856020015101811415610426576000856000018181525050610451565b8360000151836000015101856000018181510391508181525050836000015181018560200181815250505b829150509392505050565b60408051908101604052806000815260200160008152509056fea265627a7a723058202815113944245abab1fb6f13aef44a6f72db99ec216d582ff276e2678513ccbf6c6578706572696d656e74616cf50037"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[{\"name\":\"tableName\",\"type\":\"string\"},{\"name\":\"primaryKey\",\"type\":\"string\"},{\"name\":\"uniqueKey\",\"type\":\"string\"},{\"name\":\"fields\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"retCode\",\"type\":\"int256\"},{\"indexed\":false,\"name\":\"primaryKey\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"uniqueKey\",\"type\":\"string\"}],\"name\":\"AddEvent\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final Event ADDEVENT_EVENT = new Event("AddEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected AbstractBean(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<AddEventEventResponse> getAddEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEVENT_EVENT, transactionReceipt);
        ArrayList<AddEventEventResponse> responses = new ArrayList<AddEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddEventEventResponse typedResponse = new AddEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.retCode = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.primaryKey = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.uniqueKey = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeAddEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeAddEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ADDEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static AbstractBean load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AbstractBean(contractAddress, client, credential);
    }

    public static AbstractBean deploy(Client client, CryptoKeyPair credential, String tableName, String primaryKey, String uniqueKey, String fields) throws ContractException {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(tableName),
                new Utf8String(primaryKey),
                new Utf8String(uniqueKey),
                new Utf8String(fields)));
        return deploy(AbstractBean.class, client, credential, getBinary(client.getCryptoSuite()), encodedConstructor);
    }

    public static class AddEventEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger retCode;

        public String primaryKey;

        public String uniqueKey;
    }
}
