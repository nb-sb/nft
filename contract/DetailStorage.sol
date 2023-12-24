pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;
import "./AbstractBean.sol";
import "./typeUtils.sol";
import "./LibStringUtil.sol";
import "./SellStroage.sol";
// import "./BeanOwnership.sol";
/**
 * @title 存贮转移流水记录表
 * @author 戏人看戏
 * @notice 存贮数字藏品的转移流程，用于追溯该藏品的转移全部流程
 */

/**
 *  1. 需要可以使用用户地址查询到相关的交易订单
 *  2. 使用数字藏品hash可以查询到对应的所有的交易的订单 -- 该藏品的所有的交易信息

/**
 * 主键   表名称 （因为需要反向查询，使用字段查询多条数据）
 * 字段   唯一键 hash
 * 字段   转移方用户地址 transfer_address
 * 字段 接受方用户地址 target_address
 * 字段 数字hash hash
 * 字段 类型 type || 0 表示转增，1表示购买
 * 字段 更新时间  time
 * 字段 数字藏品id digital_collection_id 例如 1#5000 或 51#5000 等也就是id和总数进行拼接
 * TODO: id 发售的唯一id 和 发售个数
 *  */

//  注:  合约调用中 0 是成功 ， -1 是失败
contract DetailStorage is AbstractBean ,typeUtils {
    string privKey;
    string uniKey;
    string[] mFields;
    SellStroage sellStroage;
    constructor() public 
    AbstractBean("DetailStorage_4", "primaryKey", "hash", "transfer_address,target_address,type,time,digital_collection_id")
    {
        privKey = "primaryKey";
        uniKey = "hash";
        mFields = splitString("transfer_address,target_address,type,time,digital_collection_id");
        sellStroage = new SellStroage();
    }

    string constant primaryKey = "detail";

    // 添加购买信息
    // 添加转赠信息
    function addDetail(
        string memory _transfer_address,
        string memory _target_address,
        string memory _type,
        string memory _hash,
        string memory _digital_collection_id
    ) public returns (bool _res) {
        string[] memory fields = new string[](5);
        fields[0] = _transfer_address;
        fields[1] = _target_address;
        fields[2] = _type;
        fields[3] = uintToString(now);
        fields[4] = _digital_collection_id;
        _res = many_insert(primaryKey, _hash, fields) ==0?true:false;
    }

    // 查询用户自己的转移订单
    function selectByUserTranfer(address _address) public view returns (int _code, string memory _res) {
        (_code,  _res) =  select(primaryKey,mFields[0],addressToString(_address));
    }
    // 查询用户自己的接受订单
    function selectByUserAccept(address _address) public view returns (int _code, string memory _res) {
        (_code,  _res) =  select(primaryKey,mFields[1],addressToString(_address));
    }
    // 使用数字藏品hash可以查询到对应的所有的交易的订单
    function selectByHash(string memory hash) public view returns(int _code, string memory _res) {
         (_code,  _res) =  select(primaryKey,hash);
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
