<template>
  <div class="announceBox">
    <h1 style="text-align: center">发布公告</h1>
    <a-form :model="form" @submit="handleSubmit">
      <a-row :gutter="16">
        <a-col :span="16">
          <a-form-item field="title" tooltip="请输入公告标题" label="标题">
            <a-input v-model="form.title" placeholder="请输入公告标题" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="16">
          <a-form-item field="content" label="内容">
            <MdEditor
              :value="form.content as any"
              :handle-change="onContentChange"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="16">
          <a-form-item>
            <a-button type="primary" html-type="submit" @click="doSubmit"
              >发布
            </a-button>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import {
  AnnouncementAddRequest,
  AnnouncementControllerService,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";

const form = ref({
  title: "",
  content: "",
} as AnnouncementAddRequest);
const handleSubmit = () => {
  console.log(form);
};
const onContentChange = (v: string) => {
  form.value.content = v;
};
const doSubmit = async () => {
  const res = await AnnouncementControllerService.addAnnouncementUsingPost(
    form.value
  );
  if (res.code === 0) {
    message.success("发布成功");
  } else {
    message.error("发布失败，" + res.message);
  }
};
</script>
<style scoped></style>
