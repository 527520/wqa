<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user/')">
      <router-view />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<style>
#app {
}
</style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { onMounted } from "vue";
import { useRoute } from "vue-router";
import { connectWebSocket } from "@/access/websocket";

const route = useRoute();

/**
 * 全局初始化函数，有全局单次调用的代码，都可以写到这里
 */
const doInit = () => {
  console.log("哈哈哈哈哈哈哈哈哈哈，这是吴奇安的项目！");
  connectWebSocket();
};

onMounted(() => {
  doInit();
});
</script>
