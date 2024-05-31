<template>
  <div>
    <h1 style="text-align: center">我的评价</h1>
    <div v-if="form.length">
      <div
        :style="{ display: 'flex', marginTop: '20px' }"
        v-for="item in form"
        :key="item"
      >
        <a-card :style="{ width: '800px', margin: '0 auto' }">
          <template #title>
            <span>配送员：</span>
            <a-link @click="goToDeliverymanPortraits(item.reviewed?.id)"
              >{{ item.reviewed?.name }}
            </a-link>
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
    <div v-else>
      <a-empty style="margin-top: 40px" />
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
