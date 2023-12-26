package com.nft.common;

import cloud.tianai.captcha.generator.impl.StandardIconClickImageCaptchaGenerator;
import jdk.internal.org.objectweb.asm.commons.StaticInitMerger;

/**
 * @description: 枚举信息定义
 * @author：戏人看戏，微信：whn3500079813
 * @date: 2023/12/7
 * @Copyright：网站：https://nb.sb || git开源地址：https://gitee.com/nb-sb/nft/tree/master
 */
public class Constants {
    public static Integer  Use_password_modification = 1;

    //使用验证码修改
    public static Integer Use_Verification_code_modify = 2;

    public static Integer Get_Code_iphone = 3;
    public static Integer Get_Code_email = 4;

    public static Integer ADMIN = 1; //管理员权限
    public static Integer USER = 0; //普通用户权限



    public enum ResponseCode {
        SUCCESS("1", "成功"),
        UN_ERROR("0001", "未知失败"),

        ILLEGAL_PARAMETER("0002", "非法参数"),
        INDEX_DUP("0003", "主键冲突"),
        NO_UPDATE("0004", "SQL操作无更新"),
        USER_EXIST("0","用户名已存在"),
        LOSING_DRAW("D001", "售空无库存"),
        RULE_ERR("D002", "量化人群规则执行失败"),
        NOT_CONSUMED_TAKE("D003", "未消费活动领取记录"),
        OUT_OF_STOCK("D004", "活动无库存"),
        ERR_TOKEN("D005", "分布式锁失败");


        private String code;
        private String info;

        ResponseCode(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

    }

    /**
     * 全局属性
     */
    public static final class Global {
        /**
         * 空节点值
         */
        public static final Long TREE_NULL_NODE = 0L;
    }

    /**
     * 缓存 Key
     */
    public static final class RedisKey {


        // redis锁 Key
        private static final String LOCK_TOKEN = "lock_token_";

        private static final String REDIS_COLLECTION = "Conllection_cahe";
        private static final String LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX = "LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX_";

        private static final String REMAINING_STOCK = "REMAINING_STOCK_"; //剩余数量

        private static final String ADD_ORDER = "ADD_ORDER_";//抢购锁 - 添加到订单表中

        private static final String READ_WRITE_LOCK = "READ_WRITE_LOCK_"; //商品的读写锁
        private static final String ADD_ORDER_BYUSER = "ADD_ORDER_BYUSER_"; //用户锁防止重复提交

        //空值，用于空缓存用
        private static final String REDIS_EMPTY_CACHE = "{}";


        public static String REDIS_COLLECTION(Integer id) {
            return REDIS_COLLECTION + id;
        }
        public static String LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX(Integer id) {
            return LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX + id;
        }
        public static String REDIS_EMPTY_CACHE() {
            return REDIS_EMPTY_CACHE;
        }
        public static String LOCK_TOKEN(Integer stockUsedCount) {
            return LOCK_TOKEN + stockUsedCount;
        }
        public static String REMAINING_STOCK(Integer stockId) {
            return REMAINING_STOCK + stockId;
        }
        public static String ADD_ORDER(Integer id) {
            return ADD_ORDER + id;
        }
        public static String READ_WRITE_LOCK(Integer id) {
            return READ_WRITE_LOCK + id;
        }
        public static String ADD_ORDER_BYUSER(Integer userid) {
            return ADD_ORDER_BYUSER + userid;
        }


    }

    /**
     * 决策树节点
     */
    public static final class NodeType {
        /**
         * 树茎
         */
        public static final Integer STEM = 1;
        /**
         * 果实
         */
        public static final Integer FRUIT = 2;
    }

    /**
     * 规则限定类型
     */
    public static final class RuleLimitType {
        /**
         * 等于
         */
        public static final int EQUAL = 1;
        /**
         * 大于
         */
        public static final int GT = 2;
        /**
         * 小于
         */
        public static final int LT = 3;
        /**
         * 大于&等于
         */
        public static final int GE = 4;
        /**
         * 小于&等于
         */
        public static final int LE = 5;
        /**
         * 枚举
         */
        public static final int ENUM = 6;
    }

    /**
     * 藏品审核状态： 0 为待审核 1 为不通过 2为通过
     */
    public enum SellState {

        /**
         * 0：待审核
         */
        DOING(0, "待审核"),
        /**
         * 1：通过
         */
        PASS(1, "通过"),
        /**
         * 2：拒绝
         */
        REFUSE(2, "拒绝"),

        /**
         * 3: 参数有误
         */
        ERROR(3, "参数有误"),
        NOTDOING(0, "该商品已经审核完成，无需再次审核啦！"),

        ERRORFISCO(0,"上传至区块链数据失败，请联系管理员");



        private Integer code;
        private String info;

        SellState(Integer code, String info) {
            this.code = code;
            this.info = info;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }




    /**
     * 消息发送状态（0未发送、1发送成功、2发送失败）
     */
    public enum MQState {
        INIT(0, "初始"),
        COMPLETE(1, "完成"),
        FAIL(2, "失败");

        private Integer code;
        private String info;

        MQState(Integer code, String info) {
            this.code = code;
            this.info = info;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
    /**
     * 订单支付类型
     */
    public static final class payType {
        public static final int ZFB_PAY = 1;
        public static final int XV_PAY = 2;
        public static final int WEB_BALANCE_PAY = 3;
    }
    /**
     * 用户aop验证权限参数
     */
    public static final class permiss{
        public static final int admin = 1;
        public static final int regularUser = 0;
        public static final int everyone = 11;
    }
}
