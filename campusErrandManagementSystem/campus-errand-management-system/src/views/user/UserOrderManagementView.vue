<template>
  <a-layout style="margin-top: 32px">
    <a-layout>
      <a-layout-sider>
        <div>
          <a-collapse
            :accordion="true"
            :active-key="activeKey as any"
            :show-expand-icon="false"
            style="max-width: 200px; text-align: center"
            @change="changeActiveKey"
          >
            <a-collapse-item
              :header="activeHeaders.all"
              key="1"
            ></a-collapse-item>
            <a-collapse-item
              :header="activeHeaders.waitingOrder"
              key="2"
            ></a-collapse-item>
            <a-collapse-item
              :header="activeHeaders.toBeDelivered"
              key="3"
            ></a-collapse-item>
            <a-collapse-item
              :header="activeHeaders.toBeReceived"
              key="4"
            ></a-collapse-item>
            <a-collapse-item
              :header="activeHeaders.toBeComment"
              key="5"
            ></a-collapse-item>
            <a-collapse-item
              :header="activeHeaders.cancelled"
              key="6"
            ></a-collapse-item>
          </a-collapse>
        </div>
      </a-layout-sider>
      <a-layout-content>
        <div
          :style="{
            display: 'flex',
            boxSizing: 'border-box',
            padding: '40px',
            backgroundColor: 'var(--color-fill-2)',
          }"
        >
          <a-row v-if="Object.keys(taskFormData).length > 0" align="">
            <a-col v-for="data in taskFormData" :key="data">
              <a-card
                :style="{ minWidth: '360px', margin: '0 auto 16px' }"
                :title="activeHeader"
                :bordered="false"
              >
                <template #extra>
                  <span style="display: flex; align-items: center">
                    <router-link
                      class="orderOperate"
                      :to="{ path: '/orderDetail', query: { taskId: data.id } }"
                      >查看详情</router-link
                    >
                    <a-popconfirm
                      content="确定要删除吗?"
                      type="warning"
                      @ok="deleteOK(data.id, data.status)"
                    >
                      <a-button style="background-color: white; color: red"
                        ><icon-delete size="15px" />删除</a-button
                      >
                    </a-popconfirm>
                    <a-link
                      v-if="activeHeader === '待评论'"
                      style="color: darkorange"
                      >去评价</a-link
                    >
                  </span>
                </template>
                <div class="cardContent">
                  <span style="display: flex; align-items: center">
                    <a-tag
                      style="
                        margin-right: 5px;
                        background-color: blanchedalmond;
                      "
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
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import { onMounted, reactive, ref, UnwrapRef } from "vue";
import { DeleteRequest, Task, TaskControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";

export default {
  setup() {
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
    let activeKey = ref("1");
    let activeHeaders = reactive({
      all: "全部订单",
      waitingOrder: "待接单",
      toBeDelivered: "待送达",
      toBeReceived: "待收货",
      toBeComment: "待评论",
      cancelled: "已取消",
    });
    let activeHeader = ref("全部订单");

    const changeActiveKey = (key: any, ev: Event) => {
      localStorage.setItem("selectedKey", key);
      activeKey.value = key;
      if (key[0] === "1") {
        activeHeader.value = "全部订单";
      }
      if (key[0] === "2") {
        activeHeader.value = "待接单";
      }
      if (key[0] === "3") {
        activeHeader.value = "待送达";
      }
      if (key[0] === "4") {
        activeHeader.value = "待收货";
      }
      if (key[0] === "5") {
        activeHeader.value = "待评论";
      }
      if (key[0] === "6") {
        activeHeader.value = "已取消";
      }
      localStorage.setItem("activeHeader", activeHeader.value);
      fetchData();
    };

    onMounted(() => {
      const storedKey = localStorage.getItem("selectedKey");
      const storedHeader = localStorage.getItem("activeHeader");
      if (storedKey && storedHeader) {
        activeKey.value = storedKey;
        activeHeader.value = storedHeader;
      } else {
        activeKey.value = "1"; // 默认为第一个选项
        activeHeader.value = "全部订单";
      }
      fetchData();
    });

    const fetchData = async () => {
      console.log(activeHeader.value);
      const res = await TaskControllerService.getTaskUsingPost(
        activeHeader.value
      );
      if (res.code === 0) {
        taskFormData.value = res.data as any;
      } else {
        message.error("请求失败，" + res.message);
      }
    };

    const deleteOK = async (
      id: UnwrapRef<Task["id"]> | undefined,
      status: UnwrapRef<Task["status"]> | undefined
    ) => {
      if (status !== "已取消" && status !== "已完成" && status !== "已评论") {
        message.error("正在进行中的订单不能删除哟！");
      } else {
        console.log(id);
        const deleteForm = {
          id: id,
        } as DeleteRequest;
        const res = await TaskControllerService.deleteUsingPost(deleteForm);
        if (res.code === 0) {
          await fetchData();
          message.success("删除成功！");
        } else {
          message.error("删除失败!" + res.message);
        }
      }
    };

    return {
      activeKey,
      changeActiveKey,
      activeHeaders,
      activeHeader,
      taskFormData,
      formatTime,
      deleteOK,
    };
  },
};
</script>

<style>
.arco-collapse-item-header-left {
  padding-right: 0 !important;
  padding-left: 0 !important;
}

.arco-collapse-item-header-title {
  margin: 0 auto !important;
}

#basicLayout > section > main > section > section > main > div > div {
  margin: 0 auto;
  width: 77%;
}

.orderOperate {
  text-decoration: none;
  color: inherit;
  padding: 3px 4px;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.orderOperate:hover {
  background-color: rgba(100, 100, 100, 0.1);
}
</style>
