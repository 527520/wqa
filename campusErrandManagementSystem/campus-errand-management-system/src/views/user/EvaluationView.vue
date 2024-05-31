<template>
  <div>
    <a-form :model="form" :style="{ width: '800px' }" @submit="handleSubmit">
      <a-form-item field="rating" tooltip="请给此次跑腿小哥打分吧" label="评分">
        <a-space direction="vertical">
          <a-rate :color="color" allow-half grading v-model="form.rating" />
        </a-space>
      </a-form-item>
      <a-form-item
        field="comment"
        tooltip="展开说说对此次跑腿的想法吧..."
        label="评论"
      >
        <a-space direction="vertical" size="large" fill>
          <a-textarea
            placeholder="展开说说对此次跑腿的想法吧..."
            allow-clear
            show-word-limit
            auto-size
            v-model="form.comment"
            :max-length="200"
            style="min-width: 400px"
          />
        </a-space>
      </a-form-item>
      <a-form-item field="file" tooltip="可以上传图片哟..." label="图片">
        <a-upload
          list-type="picture-card"
          image-preview
          :auto-upload="false"
          @change="onChange"
        /><!--这里可能要改，先测试一下-->
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { ReviewAddRequest, ReviewControllerService } from "../../../generated";
import { FileItem } from "@arco-design/web-vue";
import message from "@arco-design/web-vue/es/message";
import { useRoute, useRouter } from "vue-router";

// todo 可以作为组件放到对话框里弹出

let form = ref({} as ReviewAddRequest);
const color = {
  2: "red",
  4: "green",
  5: "blue",
};

const isUploading = ref(false);
const onChange = async (_: any, currentFile: FileItem) => {
  console.log(currentFile);
  if (isUploading.value) {
    return;
  }
  isUploading.value = true;
  const res = await ReviewControllerService.uploadImageUsingPost(
    currentFile?.file
  );
  if (res.code === 0) {
    currentFile.status = "done";
    form.value.image =
      (form.value.image?.trim() ? form.value.image : "") + " " + res.data;
  } else {
    message.error("文件上传失败，" + res.message);
  }
  isUploading.value = false;
};

const route = useRoute();
const router = useRouter();
onMounted(() => {
  form.value.orderId = route.query.orderId as any;
  form.value.reviewedId = route.query.deliverymanId as any;
  form.value.taskId = route.query.taskId as any;
  console.log(form.value);
});

const handleSubmit = async () => {
  form.value.image = form.value.image?.trim();
  console.log(form.value);
  const res = await ReviewControllerService.addReviewUsingPost(form.value);
  if (res.code === 0) {
    message.success("评论成功");
    await router.push({
      path: "/myReviews",
      query: {
        taskId: form.value.taskId,
      },
    });
  } else {
    message.error("评论失败，" + res.message);
  }
};
</script>

<style scoped></style>
