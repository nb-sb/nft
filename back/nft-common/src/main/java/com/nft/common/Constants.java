package com.nft.common;

import java.text.SimpleDateFormat;

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

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public enum ResponseCode {
        SUCCESS("1", "成功"),
        ERROR("0", "失败"),
        USER_NOT_FOUND("401", "用户不存在"),
        UN_ERROR("0001", "未知失败"),

        ILLEGAL_PARAMETER("0002", "非法参数"),
        INDEX_DUP("0003", "主键冲突"),
        NO_UPDATE("0", "SQL操作无更新"),
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

        private static final String PAY_LOCK = "PAY_LOCK_";//支付锁，方式多用户同时支付同一订单
        private static final String USER_INFO = "user_";//用户信息记录

        private static final String ADMIN_UPDATE_LOCK = "ADMIN_UPDATE_LOCK_";//用于管理员更新商品信息的锁，不管是审核还是修改信息都用这把锁前缀
        //空值，用于空缓存用
        private static final String REDIS_EMPTY_CACHE = "{}";
        public static final Integer MINUTE_5 = 60*5;

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
        public static String ADMIN_UPDATE_LOCK(Integer id) {
            return ADMIN_UPDATE_LOCK + id;
        }
        public static String PAY_LOCK(String id) {
            return PAY_LOCK + id;
        }
        public static String USER_INFO(Integer id) {
            return USER_INFO + id;
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
        public static final Integer ZFB_PAY = 1;
        public static final Integer XV_PAY = 2;
        public static final Integer WEB_BALANCE_PAY = 3;
    }
    /**
     * 订单支付状态
     */
    public static final class payOrderStatus {

        public static final Integer NO_PAY = 1; //未支付
        public static final Integer PAID = 2; //已经支付
        public static final Integer FINISH = 3; //订单完成
        public static final Integer CANCEL = 4; //取消订单
        public static final Integer REFUN = 5; //退款
    }
    /**
     * 用户aop验证权限参数
     */
    public static final class permiss{
        public static final int admin = 1;
        public static final int regularUser = 0;
        public static final int everyone = 11;
    }
    /**
     * 藏品获得类型
     */
    public static final class CollectionOwnerShipType{
        public static final Integer transfer = 0; //转增
        public static final Integer pay = 1; //购买

    }
    /**
     * 实名审核状态
     */
    public static final class realNameAuthStatus{
        public static final Integer AWAIT_AUDIT = 0; //待审核
        public static final Integer SUCCESS = 1; //审核成功
        public static final Integer REFUND = 0; //审核被驳回



    }
}
