pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;
/**
 * @title 用户表
 * @author 戏人看戏
 * @notice 存贮所有用户信息，理应和mysql 表一 一对应
 */

/**
 *  1. 需要可以使用用户id查询到对应的用户地址
 *  2. 至少有一个count记录用户总数和最后一名用户的id
 *  备注： 用最后一名用户的id可用于循环查询到所有用户的地址【不推荐这样做，应该有一个mysql表与之对应进行查询】
 */

/**
 * 主键 id （此用户id和mysql中数据进行对应）
 * 字段    用户地址 address
 */
import "./AbstractBean.sol";
import "./typeUtils.sol";
contract UserStorage  is AbstractBean ,typeUtils{
    string privKey;
    string uniKey;
    string[] mFields;
    uint256 count = 0;
    uint256 last_count = 0;
    constructor() public AbstractBean(
        "UserStorage", 
        "id", 
        "un_id", 
        "address") {
        privKey = "id";
        uniKey = "un_id";
        mFields = splitString("address");
    }
    
    function addUser(string memory un_id, address _address) public {
        (int res, ) = select(un_id);
        require(res != 0, "key exits!");
        string[] memory fields = new string[](1);
        fields[0] = addressToString(_address);
        insert(un_id, un_id, fields);
    }
    
    function readMap(string memory un_id) public view returns (int, string memory) {
        (int res, string memory result) = select(un_id);
        return (res, result);
    }
    
    function existMap(string memory un_id) public view returns(int) {
        int res = exist(un_id);
        return res;
    }
    
    function updateMap(string memory un_id, string memory _address) public {
        (int res, ) = select(un_id);
        require(res == 0, "key not exits!");
        string[] memory values = new string[](1);
        values[0] = _address;
        update(un_id, mFields, values);
        
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