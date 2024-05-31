<template>
  <div>
    <div>
      <h1 style="text-align: center">招聘信息管理</h1>
      <a-input-search
        @search="search"
        @change="searchChange"
        :style="{ width: '320px', marginLeft: '60%' }"
        placeholder="搜索"
      />
    </div>
    <div style="margin-top: 16px">
      <a-card>
        <template #title>
          <p class="boxTitleP">
            <span style="order: 1">招聘信息</span>
            <a-button type="primary" style="order: 2" @click="addView"
              >发布招聘信息
            </a-button>
          </p>
        </template>
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
                <a-button type="outline" @click="edit(data)">修改</a-button>
                <a-popconfirm
                  content="确定停止招聘吗?"
                  @ok="stopRecruit(data.id as number)"
                >
                  <a-button type="outline" style="color: red; margin-left: 5px"
                    >停止招聘</a-button
                  >
                </a-popconfirm>
              </span>
            </p>
          </template>
          <MdViewer :value="data.content || ''" />
        </a-card>
      </a-card>
      <a-modal
        v-model:visible="visible"
        :title="form?.id === -1 ? '添加招聘信息' : '修改招聘信息'"
        @cancel="handleCancel"
        @before-ok="handleBeforeOk"
        fullscreen
      >
        <a-form :model="form">
          <a-form-item field="title" label="标题">
            <a-input v-model="form.title" />
          </a-form-item>
          <a-form-item field="content" label="招聘内容">
            <MdEditor
              :value="form.content as any"
              :handle-change="onContentChange"
              style="width: 100%"
            />
          </a-form-item>
        </a-form>
      </a-modal>
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
  RecruitUpdateRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import MdEditor from "@/components/MdEditor.vue";
import cloneDeep from "lodash/cloneDeep";

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

const visible = ref(false);
let form = ref({
  title: "",
  content: "",
} as Recruit);

const addView = () => {
  form.value.id = -1;
  visible.value = true;
};
const handleBeforeOk = (done: () => void) => {
  console.log(form.value);
  window.setTimeout(async () => {
    done();
    // prevent close
    // done(false)
    if (form.value.id === -1) {
      const res = await RecruitControllerService.addRecruitUsingPost(
        form.value
      );
      if (res.code === 0) {
        message.success("发布成功");
        await fetchRecruit();
      } else {
        message.error("发布失败，" + res.message);
      }
    } else {
      let updateRecruit = {} as RecruitUpdateRequest;
      updateRecruit.id = form.value.id;
      updateRecruit.title = form.value.title;
      updateRecruit.content = form.value.content;
      const res = await RecruitControllerService.updateRecruitUsingPost(
        updateRecruit
      );
      if (res.code === 0) {
        message.success("修改成功");
        await fetchRecruit();
      } else {
        message.error("修改失败，" + res.message);
      }
    }
  }, 3000);
};
const handleCancel = () => {
  visible.value = false;
};
const onContentChange = (v: string) => {
  form.value.content = v;
};

const edit = (data: any) => {
  form.value = cloneDeep(data);
  visible.value = true;
};

const stopRecruit = async (recruitId: number) => {
  const deleteRequest = {
    id: recruitId,
  } as DeleteRequest;
  const res = await RecruitControllerService.updateStatusUsingPost2(
    deleteRequest
  );
  if (res.code === 0) {
    message.success("操作成功");
    await fetchRecruit();
  } else {
    message.error("操作失败，" + res.message);
  }
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

.arco-col-19 {
  flex: 0 0 65% !important;
  width: 65% !important;
}
</style>
