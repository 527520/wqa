<template>
  <div style="max-width: 720px; margin: 0 auto">
    <a-row v-if="Object.keys(taskFormData).length > 0" align="">
      <a-col v-for="data in taskFormData" :key="data">
        <a-card
          :style="{ minWidth: '360px', margin: '0 auto 16px' }"
          :bordered="false"
          :title="data.id as string"
        >
          <template #extra>
            <span style="display: flex; align-items: center">
              <router-link
                class="orderOperate"
                :to="{ path: '/dOManagement', query: { taskId: data.id } }"
                >查看详情</router-link
              >
            </span>
          </template>
          <div class="cardContent">
            <span style="display: flex; align-items: center">
              <a-tag
                style="margin-right: 5px; background-color: blanchedalmond"
                >{{ data.serviceType }}</a-tag
              >
              {{ data.itemName }}
            </span>
            <br />
            <div style="position: relative">
              <span>{{ data.status }}</span>
              <span style="position: absolute; right: 0; bottom: 30px"
                ><span>跑腿费</span
                ><span style="color: red; font-size: 18px"
                  >￥{{ data.price }}</span
                ></span
              >
              <span style="position: absolute; bottom: 0; right: 0">
                下单时间 {{ formatTime(data.publishTime) }}</span
              >
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <a-row v-else align="">
      <a-col>
        <a-empty />
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { Task, TaskControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";

const taskFormData = ref([{} as Task]);
const formatTime = (timestamp: string) => {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getUTCHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

onMounted(() => {
  fetchData();
});

const fetchData = async () => {
  const res = await TaskControllerService.getAllOrderForDeliverymanUsingGet();
  if (res.code === 0) {
    taskFormData.value = res.data as any;
    console.log(taskFormData.value);
  } else {
    message.error("错误，" + res.message);
  }
};
</script>

<style scoped></style>
