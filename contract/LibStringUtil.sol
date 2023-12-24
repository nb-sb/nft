pragma solidity ^0.5.0;
pragma experimental ABIEncoderV2;

import "./Table.sol";

/**
    @title 将Bean格式化为json
*/
library LibStringUtil {

    function getEntry(string[] memory fields, Entry entry) internal view returns (string[] memory) {
        string[] memory values = new string[](fields.length);
        for (uint i = 0; i < fields.length; i++) {
            values[i] = entry.getString(fields[i]);
        }
        return values;
    }

    function getJsonString(string[] memory fields, Entries entries) internal view returns (int, string memory) {
        string memory detail;
        if (0 == entries.size()) {
            return (- 1, detail);
        }
        else {
            //            [{"index":"",{"key1":"","key2":""}}]

            detail = "[";

            // 获取Bean的值
            for (uint i = 0; i < uint(entries.size()); i++) {
                string[] memory values = getEntry(fields, entries.get(int(i)));
                for (uint j = 0; j < values.length; j++) {
                    if (j == 0) {
                        detail = strConcat4(detail, "{\"index\":\"", values[0], "\",{");
                    }

                    detail = strConcat6(detail, "\"", fields[j], "\":\"", values[j], "\"");

                    if (j == values.length - 1) {
                        detail = strConcat2(detail, "}}");
                    } else {
                        detail = strConcat2(detail, ",");
                    }
                }

                if (i != uint(entries.size()) - 1) {
                    detail = strConcat2(detail, ",");
                }
            }

            detail = strConcat2(detail, "]");

            return (0, detail);
        }
    }

    function strConcat6(
        string memory str1,
        string memory str2,
        string memory str3,
        string memory str4,
        string memory str5,
        string memory str6
    ) internal pure returns (string memory) {
        string[] memory strings = new string[](6);
        strings[0] = str1;
        strings[1] = str2;
        strings[2] = str3;
        strings[3] = str4;
        strings[4] = str5;
        strings[5] = str6;
        return strConcat(strings);
    }

    function strConcat5(
        string memory str1,
        string memory str2,
        string memory str3,
        string memory str4,
        string memory str5
    ) internal pure returns (string memory) {
        string[] memory strings = new string[](5);
        strings[0] = str1;
        strings[1] = str2;
        strings[2] = str3;
        strings[3] = str4;
        strings[4] = str5;
        return strConcat(strings);
    }

    function strConcat4(
        string memory str1,
        string memory str2,
        string memory str3,
        string memory str4
    ) internal pure returns (string memory) {
        string[] memory strings = new string[](4);
        strings[0] = str1;
        strings[1] = str2;
        strings[2] = str3;
        strings[3] = str4;
        return strConcat(strings);
    }

    function strConcat3(
        string memory str1,
        string memory str2,
        string memory str3
    ) internal pure returns (string memory) {
        string[] memory strings = new string[](3);
        strings[0] = str1;
        strings[1] = str2;
        strings[2] = str3;
        return strConcat(strings);
    }

    function strConcat2(string memory str1, string memory str2) internal pure returns (string memory) {
        string[] memory strings = new string[](2);
        strings[0] = str1;
        strings[1] = str2;
        return strConcat(strings);
    }

    function strConcat(string[] memory strings) internal pure returns (string memory) {
        // 计算字节长度
        uint bLength = 0;
        for (uint i = 0; i < strings.length; i++) {
            bLength += bytes(strings[i]).length;
        }

        // 实例化字符串
        string memory result = new string(bLength);
        bytes memory bResult = bytes(result);

        // 填充字符串
        uint currLength = 0;
        for (uint i = 0; i < strings.length; i++) {
            // 将当前字符串转换为字节数组
            bytes memory bs = bytes(strings[i]);
            for (uint j = 0; j < bs.length; j++) {
                bResult[currLength] = bs[j];
                currLength++;
            }
        }

        return string(bResult);
    }

    //将版本从0.5.2代码转为0.4.25的版本
    
}