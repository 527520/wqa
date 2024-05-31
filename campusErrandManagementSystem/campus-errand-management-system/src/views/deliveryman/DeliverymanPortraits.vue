<template>
  <div>
    <h1 style="text-align: center">{{ deliveryman.name }}跑腿员画像</h1>
    <div class="user-profile">
      <p>
        <span>完成订单数：</span><span>{{ deliveryman.completedOrders }}</span>
      </p>
      <p>
        <span>平均评分：</span><span>{{ deliveryman.averageRating }}</span>
      </p>
      <p>
        <span>账号状态：</span><span>{{ deliveryman.status }}</span>
      </p>
      <p>
        <span>可接单状态：</span><span>{{ deliveryman.acceptingOrders }}</span>
      </p>
    </div>
    <div
      :style="{ display: 'flex', marginTop: '20px' }"
      v-for="item in form"
      :key="item"
    >
      <a-card :style="{ width: '800px', margin: '0 auto' }">
        <template #title>
          <span>用户：</span>
          <a-link>{{ item.reviewer?.username }}</a-link>
          <span> 评价</span>
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
  DeliverymanControllerService,
  DeliverymanVO,
  DeliverymanVOVO,
  ReviewControllerService,
  ReviewVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRoute } from "vue-router";

let form = ref([{} as ReviewVO]);
const color = {
  2: "red",
  4: "green",
  5: "blue",
};

const fetchData1 = async () => {
  const res = await ReviewControllerService.getDeliveryCommentsUsingPost(
    deliverymanReq
  );
  if (res.code === 0) {
    form.value = res.data as any;
  } else {
    message.error("请求错误，" + res.message);
  }
};

let deliveryman = ref({} as DeliverymanVO);

const fetchData2 = async () => {
  const res = await DeliverymanControllerService.getDeliverymanImageUsingPost(
    deliverymanReq
  );
  if (res.code === 0) {
    console.log(res.data);
    deliveryman.value = res.data;
  } else {
    message.error("请求错误，" + res.message);
  }
};

let deliverymanReq = {
  id: 0,
} as DeliverymanVOVO;
const route = useRoute();

onMounted(() => {
  console.log(route.query.deliverymanId);
  deliverymanReq.id = route.query.deliverymanId as any;
  fetchData1();
  fetchData2();
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
<style scoped>
.user-profile {
  text-align: center;
}

.user-profile p {
  margin-bottom: 10px;
  font-size: 16px;
}

.user-profile span {
  font-weight: bold;
  color: #666;
}

.user-profile span.value {
  color: #333;
}
</style>
