pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;
/**
 * @title nft map 合约
 * @author 戏人看戏
 * @notice 
 */
import "./AbstractBean.sol";

contract MapStorage  is AbstractBean {
    string privKey;
    string uniKey;
    string[] mFields;
    constructor() public AbstractBean(
        "mapStorage", 
        "id", 
        "key", 
        "value") {
        privKey = "id";
        uniKey = "key";
        mFields = splitString("value");
    }
    
    function createMap(string memory key, string memory value) public {
        (int res, string memory result) = select(key);
        require(res != 0, "key exits!");
        string[] memory fields = new string[](1);
        fields[0] = value;
        insert(key, key, fields);
    }
    
    function readMap(string memory key) public view returns (int, string memory) {
        (int res, string memory result) = select(key);
        return (res, result);
    }
    
    function existMap(string memory key) public view returns(int) {
        int res = exist(key);
        return res;
    }
    
    function updateMap(string memory key, string memory value) public {
        (int res, string memory result) = select(key);
        require(res == 0, "key not exits!");
        string[] memory values = new string[](1);
        values[0] = value;
        update(key, mFields, values);
        
    }
    
    function deleteMap(string memory key) public {
        (int res, ) = select(key);
        require(res == 0, "key not found");
        remove(key);
    }
    
    function splitString(string memory _value) internal pure returns (string[] memory) {
        LibStrings.slice memory s = _value.toSlice();
        LibStrings.slice memory delim = ",".toSlice();
        string[] memory values = new string[](s.count(delim) + 1);
    
        for(uint i = 0; i < values.length; i++) {
          values[i] = s.split(delim).toString();
        }
        return values;
    }
}