<template>
  <div>
    <div>
      <h1 style="text-align: center">招聘信息</h1>
      <a-input-search
        @search="search"
        @change="searchChange"
        :style="{ width: '320px', marginLeft: '60%' }"
        placeholder="搜索"
      />
    </div>
    <div style="margin-top: 16px">
      <a-card title="招聘信息">
        <a-card
          :style="{ marginBottom: '20px' }"
          v-for="data in recruitList"
          :key="data"
        >
          <template #title>
            <p class="boxTitleP">
              <span style="order: 1">标题：{{ data.title }}</span>
              <span style="order: 2"
                >发布时间：{{ formatTime(data.publishTime) }}</span
              >
              <span style="order: 3"
                >状态：{{ data.status === 0 ? "招聘中" : "停止招聘" }}</span
              >
              <span style="order: 4">
                <a-popconfirm content="确定去应聘吗?" @ok="apply">
                  <a-button type="primary">应聘</a-button>
                </a-popconfirm>
              </span>
            </p>
          </template>
          <MdViewer :value="data.content || ''" />
        </a-card>
      </a-card>
    </div>
  </div>
</template>
<script setup lang="ts">
import MdViewer from "@/components/MdViewer.vue";
import { onMounted, ref } from "vue";
import {
  DeleteRequest,
  Recruit,
  RecruitControllerService,
  RecruitSearchRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

let recruitList = ref([{} as Recruit]);
onMounted(() => {
  fetchRecruit();
});
const fetchRecruit = async () => {
  const res = await RecruitControllerService.getAllRecruitsUsingGet();
  if (res.code === 0) {
    recruitList.value = res.data as any;
  } else {
    message.error("请求错误，" + res.message);
  }
};

const router = useRouter();

const apply = () => {
  router.push({
    path: "/becomeDeliveryMan",
  });
};

let searchFlag = false; // 搜索标志
const searchValue = ref("");

const search = async (value: string) => {
  if (value && value.trim()) {
    searchFlag = true;
    searchValue.value = value;
    await fetchSearch(searchValue.value);
  } else {
    searchFlag = false;
    await fetchRecruit();
  }
};

const searchChange = async (value: string) => {
  if (value && value.trim()) {
    searchFlag = true;
    searchValue.value = value;
    await fetchSearch(searchValue.value);
  } else {
    searchFlag = false;
    await fetchRecruit();
  }
};

const fetchSearch = async (value: string) => {
  const searchData = {
    searchValue: value,
  } as RecruitSearchRequest;
  const res = await RecruitControllerService.getRecruitsBySearchUsingPost(
    searchData
  );
  if (res.code === 0) {
    recruitList.value = res.data;
  } else {
    message.error("搜索失败，" + res.message);
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
</script>
<style>
.boxTitleP {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
