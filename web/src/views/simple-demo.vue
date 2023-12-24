<template>
  <div class="login-div">
    <div class="title">(目前只支持滑动验证码)</div>
    <div class="kuang"><span class="kuang-left">用户名</span>天爱有情</div>
    <div class="kuang"><span class="kuang-left">密码</span>*********</div>
    <div id="login-btn" @click="loginBtn">登录</div>
    <div id="captcha-div"></div>
  </div>
</template>

<script>
import "@/assets/captcha/css/tac.css"; // 验证码css
import "@/assets/captcha/js/jquery.min.js"; // 验证码js
import "@/assets/captcha/js/tac.min.js"; // 验证码js
import CryptoJS from "crypto-js";
import JSEncrypt from "jsencrypt";
export default {
  name: "simple-demo",
  data() {
    return {
      logins: {
        username: "",
        password: "",
        codeId: ""
      }
    }
  },
  mounted() {
    // 绑定全局给captcha用
    window.CryptoJS = CryptoJS;
    window.JSEncrypt = JSEncrypt;
  },
  methods: {
    loginBtn() {
      // rsa公钥
      TAC.enc.rsaPublicKey =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0nf3XQxOCL59bUEno36+d0PoGu82WDaeN6vYzlO5p5JOttGIuw0BLjXjJhpKrwU41tmYVygr+t3+y2GAgavEy2R+RSWCYgLEwpi8xjeV5ZfAWORYILF2YuZsG+276HapgQYxajzUlnHrO+En6LJ9sUglg9VTQNnawwKD6/cy8IQIDAQAB";
      // 样式配置
      const config = {
        requestCaptchaDataUrl: "http://localhost:8081/gen/random",
        validCaptchaUrl: "http://localhost:8081/check3",
        bindEl: "#captcha-div",
        chainString: "cl>json>rsaaes>base64",
        // 验证成功回调函数
        validSuccess: (res, c, tac) => {
          console.log(res)
          if (res.code == 200) {
            // 将验证码id存入data，准备到登录或注册的时候需要和用户名密码一起提交
            console.log(res.data)
            this.logins.codeId = res.data;
          }
          this.login();
          tac.destroyWindow();
        },
      };
      new window.TAC(config).init();
    },
    login() {
      // post 请求后端登录或者注册接口 ，| codeId 传入
      alert("登录成功");
    },
  },
};
</script>
<!--<style src="../assets/css/tianai-captcha.css"></style>-->
<style scoped>
/*@import "../assets/css/tianai-captcha.css";*/
.login-div {
  width: 600px;
  height: 600px;
  margin: 0 auto;
  padding: 50px;
  box-sizing: border-box;
  border-radius: 6px;
  box-shadow: 0 0 11px 0 #999999;
  position: relative;
}

.kuang {
  height: 50px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 6px;
  margin: 20px 0;
  color: #ccc;
  line-height: 50px;
}

.kuang-left {
  float: left;
  border-right: 1px solid #ccc;
  text-align: center;
  width: 100px;
  display: inline-block;
}

#login-btn {
  margin: 0 auto;
  width: 200px;
  height: 50px;
  background-color: #4bc065;
  color: #fff;
  line-height: 50px;
  text-align: center;
  border-radius: 6px;
}

#login-btn:hover {
  cursor: pointer;
}

#captcha {
  width: 100%;
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
}

.title {
  height: 50px;
  color: #2a2929;
  line-height: 50px;
  font-size: 20px;
  text-align: center;
}

@media screen and (max-width: 600px) {
  .login-div {
    width: 100%;
  }
}

#captcha-div {
  position: absolute;
  left: 140px;
  top: 100px;
}
</style>
