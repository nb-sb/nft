pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;

import "./Table.sol";
import "./LibStrings.sol";
import "./LibStringUtil.sol";

contract AbstractBean {
    using LibStrings for *;

    // 定义添加数据的事件
    event AddEvent(int256 retCode, string primaryKey, string uniqueKey);

    // 定义表结构
    struct BeanInfo {
        // 表名称
        string tableName;
        // 主键
        string primaryKey;
        // 唯一键
        string uniqueKey;
        // 表字段（包括唯一建、不包括主键）
        string[] fields;
    }

    // 保存当前Bean的结构
    BeanInfo info;

    constructor(
        string memory tableName,
        string memory primaryKey,
        string memory uniqueKey,
        string memory fields
    ) public {
        // 创建表
        TableFactory tf = TableFactory(0x1001);
        // 拼接
        LibStrings.slice[] memory parts = new LibStrings.slice[](2);
        parts[0] = uniqueKey.toSlice();
        parts[1] = fields.toSlice();
        tf.createTable(tableName, primaryKey, ",".toSlice().join(parts));

        // 通过构造函数赋值当前BeanInfo
        info = BeanInfo({
            tableName: tableName,
            primaryKey: primaryKey,
            uniqueKey: uniqueKey,
            fields: new string[](0)
        });
        // 填充info中的fields
        setDBFields(info, uniqueKey, fields);
    }

    /**
     * 描述：查询  -- 由于另外两个是用于封装的直接返回的，需要封装一个用于和其他合约进行交互的
     * param _primary  主键
     * param _fieldsKey  参数的名称
     * param _fieldsValue 参数的值
     * return Entries
     */
    function select(string memory _primary, string[] memory _fieldsKey,string[] memory _fieldsValue) internal view returns(Entries entries){
        require(_fieldsKey.length == _fieldsValue.length, "parametric error");
        Table table = openTable();
        if(_fieldsKey.length == 0){
            entries = table.select(_primary, table.newCondition());
        }
        Condition condition =  table.newCondition();     
        for (uint i = 1; i < _fieldsKey.length; i++) {
                condition.EQ(_fieldsKey[i], _fieldsValue[i]);
        }   
        entries = table.select(_primary,condition);
    }

    /**
     * 描述：查询  -- 由于另外两个是用于封装的直接返回的，需要封装一个用于和其他合约进行交互的
     * param primaryKey ：主键的值  主键
     * return 成功返回0，群组不存在返回-1
     * return 第一个参数为0时有效，key->id，value->对象json 的数组
     */
    function select(
        string memory primaryKey
    ) internal view returns (int, string memory) {
        // 打开表
        Table table = openTable();
        // 查询
        Entries entries = table.select(primaryKey, table.newCondition());
        // 将查询结果解析为json字符串
        return LibStringUtil.getJsonString(info.fields, entries);
    }

    

    /**
     * 描述：根据主键查询指定ID的记录
     * param primaryKey ：主键的值  主键
     * param uniqueKey：唯一键值
     * return 成功返回0，群组不存在返回-1
     * return 第一个参数为0时有效，key->id，value->对象json 的数组
     */
    function select(
        string memory primaryKey,
        string memory uniqueKey
    ) internal view returns (int, string memory) {
        // 打开表
        Table table = openTable();
        // 构建查询条件
        Condition condition = table.newCondition();
        condition.EQ(info.uniqueKey, uniqueKey);
        // 查询
        Entries entries = table.select(primaryKey, condition);
        // 将查询结果解析为json字符串
        return LibStringUtil.getJsonString(info.fields, entries);
    }


    /**
     * 描述：根据主键和其中一个字段查询所有记录
     * param primaryKey ：主键的值  主键
     * param fieldKey : 字段名
     * param fieldValue：字段的值
     * return 成功返回0，群组不存在返回-1
     * return 第一个参数为0时有效，key->id，value->对象json 的数组
     */
   function select(string memory primaryKey,string memory fieldKey,string memory fieldValue) internal view returns (int, string memory){
         // 打开表
        Table table = openTable();
        Condition condition =  table.newCondition();
        condition.EQ(fieldKey,fieldValue);
        // 查询
        Entries entries = table.select(primaryKey, condition);
        // 将查询结果解析为json字符串
        return LibStringUtil.getJsonString(info.fields, entries);
   }


    /**
     * 描述：新增记录
     * param primaryKey：主键的值  主键
     * param uniqueKey：唯一键值
     * param fields：其它字段的值
     * return 成功返回0，记录已存在-1，其它失败返回-2（其它错误）
     */
    function insert(
        string memory primaryKey,
        string memory uniqueKey,
        string[] memory fields
    ) internal returns (int) {
        require(fields.length == info.fields.length - 1);

        int retCode;
        // 判断ID记录是否已存在
        int ret;
        string memory retValue;
        (ret, retValue) = select(primaryKey, uniqueKey);
        if (-1 == ret) {
            // 打开表
            Table table = openTable();
            // 创建表记录
            Entry entry = table.newEntry();
            entry.set(info.primaryKey, primaryKey);
            entry.set(info.uniqueKey, uniqueKey);
            for (uint i = 1; i < info.fields.length; i++) {
                entry.set(info.fields[i], fields[i - 1]);
            }
            // 新增表记录
            if (1 == table.insert(primaryKey, entry)) {
                retCode = 0;
            } else {
                retCode = -2;
            }
        } else {
            retCode = -1;
        }

        // 记录新增结果
        emit AddEvent(retCode, primaryKey, uniqueKey);
        // 返回结果
        return retCode;
    }

    /**
     * 描述：新增记录_可多次插入
     * param primaryKey ：主键的值  主键
     * param uniqueKey ：唯一键值
     * param fields：其它字段的值
     * return 成功返回0，记录已存在-1，其它失败返回-2（其它错误）
     */
    //根据业务无判断ID记录是否已存在,因为可以多次插入
    function many_insert(
        string memory primaryKey,
        string memory uniqueKey,
        string[] memory fields
    ) internal returns (int) {
        require(fields.length == info.fields.length - 1, "参数数量不正确");
        int retCode;
        // 打开表
        Table table = openTable();
        // 创建表记录
        Entry entry = table.newEntry();
        entry.set(info.primaryKey, primaryKey);
        entry.set(info.uniqueKey, uniqueKey);
        for (uint i = 1; i < info.fields.length; i++) {
            entry.set(info.fields[i], fields[i - 1]);
        }
        // 新增表记录
        if (1 == table.insert(primaryKey, entry)) {
            retCode = 0;
        } else {
            retCode = -2;
        }
        // 记录新增结果
        emit AddEvent(retCode, primaryKey, uniqueKey);
        // 返回结果
        return retCode;
    }


    function openTable() internal view returns (Table) {
        TableFactory tf = TableFactory(0x1001);
        return tf.openTable(info.tableName);
    }

    function setDBFields(
        BeanInfo storage info,
        string memory uniqueKey,
        string memory _fields
    ) internal {
        info.fields.push(uniqueKey);

        LibStrings.slice memory s = _fields.toSlice();
        LibStrings.slice memory delimiter = ",".toSlice();
        uint total = s.count(delimiter) + 1;
        for (uint i = 0; i < total; i++) {
            info.fields.push(s.split(delimiter).toString());
        }
    }

    function update(
        string memory primaryKey,
        string[] memory fields,
        string[] memory newValues
    ) internal returns (int) {
        require(
            fields.length == newValues.length,
            "Fields and newValues length mismatch"
        );

        // 打开表
        Table table = openTable();
        // 查询
        Entries entries = table.select(primaryKey, table.newCondition());

        // 检查是否存在记录
        if (entries.size() == 0) {
            return -1; // 记录不存在
        }

        Entry entry = entries.get(0);

        // 更新记录的字段值
        for (uint i = 0; i < fields.length; i++) {
            entry.set(fields[i], newValues[i]);
        }

        // 更新表记录
        if (table.update(primaryKey, entry, table.newCondition()) == 1) {
            return 0; // 更新成功
        } else {
            return -2; // 更新失败
        }
    }

    function remove(string memory primaryKey) internal returns (int) {
        // 打开表
        Table table = openTable();
        // 删除记录
        if (table.remove(primaryKey, table.newCondition()) == 1) {
            return 0; // 删除成功
        } else {
            return -1; // 删除失败
        }
    }

    function exist(string memory primaryKey) internal view returns (int) {
        // 打开表
        Table table = openTable();
        // 构建查询条件
        Condition condition = table.newCondition();
        // 查询
        Entries entries = table.select(primaryKey, condition);
        if (entries.size() == 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
