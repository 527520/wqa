<template>
  <div class="userRegister">
    <h3 style="text-align: center; padding-top: 10px">用户注册</h3>
    <a-form
      :model="form"
      style="max-width: 280px; margin: 0 auto"
      label-align="left"
      auto-label-width
      @submit="handleSubmitRegister"
    >
      <a-form-item field="userAccount" tooltip="手机号" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" tooltip="密码不少于8位" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="checkPassword" tooltip="确认密码" label="密码">
        <a-input-password
          v-model="form.checkPassword"
          placeholder="确认密码"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="phoneNumber" tooltip="电话号码" label="电话号码">
        <a-input
          v-model="form.phoneNumber"
          placeholder="请输入正确的电话号码"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="username" tooltip="真实姓名" label="姓名">
        <a-input
          v-model="form.username"
          placeholder="请输入真实姓名"
          allow-clear
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { UserControllerService, UserRegisterRequest } from "../../../generated";
import { useRouter } from "vue-router";
import message from "@arco-design/web-vue/es/message";

const form = reactive({
  checkPassword: "",
  userAccount: "",
  userPassword: "",
  phoneNumber: "",
  username: "",
} as UserRegisterRequest);

const router = useRouter();

const handleSubmitRegister = async () => {
  const res = await UserControllerService.userRegisterUsingPost(form);
  //注册成功，跳到登录页
  if (res.code === 0) {
    await router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    message.error("注册失败," + res.message);
  }
};
</script>
