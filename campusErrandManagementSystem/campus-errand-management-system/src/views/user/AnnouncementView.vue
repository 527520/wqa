<template>
  <div class="announcementBox">
    <h1 style="text-align: center">系统公告</h1>
    <a-card title="系统公告">
      <a-card
        :style="{ marginBottom: '20px' }"
        v-for="data in announcementList"
        :key="data"
        :title="data.title"
      >
        <MdViewer :value="data.content || ''" />
      </a-card>
    </a-card>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import {
  Announcement,
  AnnouncementControllerService,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import MdViewer from "@/components/MdViewer.vue";

onMounted(() => {
  fetchAnnouncement();
});
let announcementList = ref([{} as Announcement]);
const fetchAnnouncement = async () => {
  const res = await AnnouncementControllerService.getLatestFiveUsingGet();
  if (res.code === 0) {
    announcementList.value = res.data as any;
  } else {
    message.error("错误，" + res.message);
  }
};
</script>
<style scoped></style>
