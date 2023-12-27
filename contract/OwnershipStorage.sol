pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;
import "./AbstractBean.sol";
import "./typeUtils.sol";
import "./SellStroage.sol";
import "./DetailStorage.sol";
/**
 * @title 存贮当前用户的藏品所属权，这里存贮的是拥有者
 * @author 戏人看戏
 * @notice 存贮合约中 数字hash 和 用户 address 对应的表
 */

/**
 *  1. 需要可以使用用户地址查询到对应的用户所有的 数字hash
 *  2. 需要可以使用数字hash查询到对应的用户的地址
 *  用户可以有多个数字藏品
 */
/**
 * 主键   表名称 （因为需要反向查询，使用字段反向查询）
 * 字段 用户地址 address
 * 字段 数字hash hash
 * 字段 获得时间
 * 字段 获得类型 type ||  0 表示转增，1表示购买
 * 字段 数字藏品id digital_collection_id 例如 1#5000 或 51#5000 等也就是id和总数进行拼接
 *  */
 contract OwnershipStorage is AbstractBean ,typeUtils{
    string privKey;
    string uniKey;
    string[] mFields;
    SellStroage sellStroage;
    DetailStorage detailStorage;
    constructor() public 
    AbstractBean("OwnershipStorage", "primaryKey", "address","hash,time,type,digital_collection_id")
    {
        privKey = "primaryKey";
        uniKey = "address";
        mFields = splitString("hash,time,type,digital_collection_id");
        sellStroage = new SellStroage();
        detailStorage = new DetailStorage();
    }
    string constant TRANSFORM = "0"; //转赠藏品
    string constant PURCHASE = "1"; //购买藏品
    string constant primaryKey = "ownership";

    // 购买藏品 || 同一个用户可以有多个数字藏品 || 保证业务原子性
    function addOwnership(
        address _address,
        string memory _hash
    ) public returns (bool _res,string memory _msg) {
        //  digital_collection_id 应该是获取到剩余的id数 然后进行拼接 "{当前数id}#{总数id}"
        int256 remain =  sellStroage.catRemain(_hash);
        if(remain <= 0){
            return (false,"There's no more goods.");
        }
        int256 total =  sellStroage.catTotal(_hash);
        string memory digital_collection_id = LibStringUtil.strConcat3(uintToString(uint256(total-remain+1)), "#",uintToString(uint256(total)));
        string[] memory fields = new string[](4);
        fields[0] = _hash;
        fields[1] = uintToString(now);
        fields[2] = PURCHASE;
        fields[3] = digital_collection_id;
        if(many_insert(primaryKey, addressToString(_address), fields) ==0){
            require(sellStroage.decrRemain(_hash));
            detailStorage.addDetail("", addressToString(_address),PURCHASE,_hash,digital_collection_id);
            _res = true;
            _msg = "success";
        }
    }

    // 使用用户地址查询到所有的所属权
    function selectByUserAddr(address _address) public view returns (int , string memory ) {
        return select(primaryKey,addressToString(_address));
    }

    // 使用数字藏品hash可以查询到对应的所有的拥有者
    function selectByHash(string memory hash) public view returns(int , string memory ) {
        return  select(primaryKey,mFields[0],hash);
    }
    //使用用户地址和藏品hash查询内容
    function selectInfo(address _address , string memory _hash )public view returns(int,string memory,string memory,string memory) {
        string[] memory a  = new string[](2);
        string[] memory b  = new string[](2);
        a[0] = uniKey;
        a[1] = mFields[0];
        b[0] = addressToString(_address);
        b[1] = _hash;
        Entries entries =   select(primaryKey,a,b);
        if(entries.size()<=0){
            return(0,"not found","","");
        }
        return(1,entries.get(0).getString("time"),entries.get(0).getString("type"),entries.get(0).getString("digital_collection_id"));
    }
    // 转赠 -- 只能所属人进行调用  || 保证业务原子性
    function transfer(address target_address, string memory _hash) public returns (bool , string memory ) {
        Table table = openTable();
        Condition condition =  table.newCondition();
        condition.EQ(uniKey,addressToString(msg.sender));
        Entries entries = table.select(primaryKey, condition);
        // 如果查询不到 或 该藏品不属于你则直接返回这个错误
        if(entries.size() == 0){
            return (false,"Collection does not belong to you");
        }
        // 获取这个hash对应的 id
        string memory _digital_collection_id= entries.get(0).getString(mFields[2]);
        // 在进行修改 -- 修改转赠者
        int switcher = table.remove(primaryKey,condition);
        if(switcher == 0){
            return (false,"Deletion of subgrantee error");
        }
        // 修改接收者 -- 直接添加即可 
        (bool code ,string memory _msg )= transOwnership(target_address, _hash,_digital_collection_id);
        require(code,_msg); 
        detailStorage.addDetail(addressToString(msg.sender), addressToString(target_address),TRANSFORM,_hash,_digital_collection_id);
        return(true,"Successful transfer");
    }

    // 转增藏品 添加
    function transOwnership(
        address _target_address,
        string memory _hash,
        string memory _digital_collection_id
    ) private returns (bool _res,string memory _msg) {
        string[] memory fields = new string[](4);
        fields[0] = _hash;
        fields[1] = uintToString(now);
        fields[2] = TRANSFORM;
        fields[2] = _digital_collection_id;
        if(many_insert(primaryKey, addressToString(_target_address), fields) ==0){
            _res = true;
            _msg = "success";
        }
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
