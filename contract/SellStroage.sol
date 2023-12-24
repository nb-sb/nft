pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;
/**
 * @title nft 发售合约
 * @author 戏人看戏
 * @notice 存贮合约中 数字hash 和 发行量 amount 对应的map表
 * 这个表中主键和唯一键的都为key 
 * 值为value
 */
/**
 * 
 * 主键 id
 * 字段 数字藏品 hash
 * 字段 发行量 amount
 * 字段 剩余数量 remain
 * 字段 数字藏品作者 auther
 * 字段 发售状态 status  # 1 为正常 ，  0 为闭售
 */
import "./AbstractBean.sol";
import "./typeUtils.sol";
contract SellStroage  is AbstractBean , typeUtils {
    string privKey;
    string uniKey;
    string[] mFields;
    constructor() public AbstractBean(
        "SellStroage", 
        "id", 
        "hash", 
        "amount,remain,auther,status") {
        privKey = "id";
        uniKey = "hash";
        mFields = splitString("amount,remain,auther,status");
    }
    //设置发售
    function createSell(string memory _hash, uint amount) public returns(bool _res) {
        (int res,) = select(_hash);
        require(res != 0, "key exits!");
        string[] memory fields = new string[](4);
        fields[0] = uintToString(amount);
        fields[1] = uintToString(amount);
        fields[2] = addressToString(msg.sender);
        fields[3] = "1";
        _res = insert(_hash, _hash, fields) ==0 ? true : false;
    }
    //停止发售 TODO: 需要判断是否为拥有者 或者 是管理员 [🐕]
    function stopSell(string memory _hash) public returns(bool _res){
        Table table = openTable();
        Entry entry = table.newEntry();
        entry.set(mFields[3], "0"); // 设置为闭售
        _res = table.update(_hash, entry,table.newCondition())==1 ? true : false;
    }
    //减少剩余量
    function decrRemain(string memory _hash) public returns(bool _res) {
        int256 remain = catRemain(_hash);
        Table table = openTable();
        Entry entry = table.newEntry();
        entry.set(mFields[1],uintToString(uint256(remain-1))); // 设置为闭售
        _res = table.update(_hash, entry,table.newCondition())==1 ? true : false;
    }
    // 获取藏品总数
    function catTotal(string memory _hash) public view returns (int256 ) {
        string[] memory fields = new string[](0);
        Entries entries = select(_hash,fields,fields);
        if(entries.size() == 0){
            return 0;
        }
        return entries.get(0).getInt(mFields[0]);
    }

    // 获取藏品剩余数量
    function catRemain(string memory _hash) public view returns (int256 ) {
        string[] memory fields = new string[](0);
        Entries entries = select(_hash,fields,fields);
        if(entries.size() == 0){
            return 0;
        }
        return entries.get(0).getInt(mFields[1]);
    }
    //查询藏品作者address
    function catAuthor(string memory _hash) public view returns (string memory ) {
            string[] memory fields = new string[](0);
        Entries entries = select(_hash,fields,fields);
        if(entries.size() == 0){
            return "";
        }
        return entries.get(0).getString(mFields[2]);
    }
    // 是否存在该藏品
    function existSell(string memory _hash) public view returns(bool) {
        int res = exist(_hash);
        return res==0 ? true : false;
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