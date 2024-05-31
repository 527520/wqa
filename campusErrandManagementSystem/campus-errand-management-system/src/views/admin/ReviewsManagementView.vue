<template>
  <div>
    <h1 style="text-align: center">评价信息管理</h1>
    <a-space
      direction="vertical"
      size="large"
      style="width: 100%; position: relative; margin-bottom: 30px"
    >
      <a-input-search
        @search="search"
        @change="searchChange"
        :style="{
          width: '400px',
          position: 'absolute',
          left: '50%',
          transform: 'translateX(-50%)',
        }"
        placeholder="搜索"
      />
    </a-space>
    <div
      :style="{ display: 'flex', marginTop: '20px' }"
      v-for="item in form"
      :key="item"
    >
      <a-card :style="{ width: '800px', margin: '0 auto' }">
        <template #title>
          <span>用户：</span>
          <a-link>{{ item.reviewer?.username }}</a-link>
          <span> 评价 配送员：</span>
          <a-link @click="goToDeliverymanPortraits(item.reviewed?.id)"
            >{{ item.reviewed?.name }}
          </a-link>
          <!--todo 可以指向个人主页-->
        </template>
        <template #extra>
          <a-popconfirm
            content="确定删除这条评论吗?"
            type="warning"
            @ok="deleteOK(item.id as number)"
          >
            <a-button type="primary">
              <template #icon>
                <icon-delete />
              </template>
              <template #default>删除</template>
            </a-button>
          </a-popconfirm>
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
import {
  DeleteRequest,
  ReviewControllerService,
  ReviewVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

let form = ref([{} as ReviewVO]);
const color = {
  2: "red",
  4: "green",
  5: "blue",
};

const fetchData = async () => {
  const res = await ReviewControllerService.getAllCommentsUsingGet();
  if (res.code === 0) {
    form.value = res.data as any;
  } else {
    message.error("请求错误，" + res.message);
  }
};

onMounted(() => {
  if (searchFlag) {
    // 走搜索
    // fetchData3(searchValue.value);
    console.log(searchValue.value);
  } else {
    fetchData();
  }
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

const deleteOK = async (reviewId: number) => {
  const deleteForm = {
    id: reviewId,
  } as DeleteRequest;
  const res = await ReviewControllerService.deleteCommentsUsingPost(
    deleteForm as any
  );
  if (res.code === 0) {
    await fetchData();
    message.success("删除成功！");
  } else {
    message.error("删除失败！" + res.message);
  }
};

const searchValue = ref("");
let searchFlag = false; // 搜索标志
const search = async (value: string) => {
  if (value && value.trim()) {
    searchFlag = true;
    searchValue.value = value;
    // await fetchData3(value);
    console.log(searchValue.value);
  } else if (!value.trim()) {
    searchFlag = false;
    // 两个都空，搜全部
    await fetchData();
  }
};
const searchChange = async (value: string) => {
  if (value && value.trim()) {
    searchFlag = true;
    searchValue.value = value;
    // await fetchData3(value);
    console.log(searchValue.value);
  } else if (!value.trim()) {
    searchFlag = false;
    await fetchData();
  }
};

const router = useRouter();

const goToDeliverymanPortraits = (deliverymanId: any) => {
  router.push({
    path: "/deliverymanPortraits",
    query: {
      deliverymanId: deliverymanId,
    },
  });
};

const getRating = (rating: number) => {
  return rating / 2;
};
</script>
<style scoped></style>
