<template>
  <div>
    <h1 style="text-align: center">我的评价</h1>
    <div
      :style="{ display: 'flex', marginTop: '20px' }"
      v-for="item in form"
      :key="item"
    >
      <a-card :style="{ width: '800px', margin: '0 auto' }">
        <template #extra>
          <a-link>{{ item.reviewer?.username }} </a-link
          ><!--todo 可以指向个人主页-->
        </template>
        <div>
          <p>
            <span>发布于</span><span>{{ formatTime(item.reviewTime) }}</span>
          </p>
        </div>
        <div>
          <a-rate
            :color="color"
            allow-half
            grading
            readonly
            :default-value="item.rating as number"
          />
        </div>
        <div>
          <p>{{ item.comment }}</p>
        </div>
        <div v-if="item.image">
          <a-image
            v-for="path in item.image?.split(' ')"
            :key="path"
            :src="path"
            width="200"
            style="margin-right: 16px"
          />
        </div>
      </a-card>
    </div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import { ReviewControllerService, ReviewVO } from "../../../generated";
import message from "@arco-design/web-vue/es/message";

let form = ref([{} as ReviewVO]);
const color = {
  2: "red",
  4: "green",
  5: "blue",
};

const fetchData = async () => {
  const res = await ReviewControllerService.viewMyAllCommentsUsingGet();
  if (res.code === 0) {
    form.value = res.data as any;
  } else {
    message.error("请求错误，" + res.message);
  }
};

onMounted(() => {
  fetchData();
});

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

const getRating = (rating: number) => {
  return rating / 2;
};
</script>
<style scoped></style>
