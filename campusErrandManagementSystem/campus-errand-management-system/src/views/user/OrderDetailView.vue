<template>
  <div>
    <a-descriptions
      style="margin: 20px auto; text-align: center; max-width: 800px"
      :size="size"
      title="订单详情"
      :column="1"
    >
      <a-descriptions-item
        v-for="item in filteredData"
        :label="item.label"
        :key="item.label"
      >
        {{ item.value }}
      </a-descriptions-item>
    </a-descriptions>
    <div class="operate">
      <template
        v-if="
          taskStatus === '已接单' ||
          taskStatus === '配送中' ||
          taskStatus === '已送达'
        "
      >
        <a-button type="primary" @click="contactDelivery">联系跑腿员</a-button>
      </template>
    </div>
    <div class="operate">
      <template v-if="taskStatus === '已取消'">
        <a-button type="outline" @click="rePublish">重新发布</a-button>
      </template>
      <template v-else-if="taskStatus === '待接单'">
        <a-popconfirm content="确定要取消吗?" type="warning" @ok="cancelOrder">
          <a-button type="outline">
            <icon-delete size="15px" />
            取消订单
          </a-button>
        </a-popconfirm>
      </template>
      <template v-else-if="taskStatus !== '待接单' || taskStatus !== '已取消'">
        <a-button type="outline" @click="goToDeliverymanPortraits()"
          >查看跑腿员画像
        </a-button>
      </template>
      <template v-else-if="taskStatus === '已送达'">
        <a-button type="primary" @click="completeTask">任务完成</a-button>
      </template>
      <template v-else-if="taskStatus === '已完成'">
        <a-button type="primary" @click="goToReview">去评价</a-button>
      </template>
      <template v-else-if="taskStatus !== '已评论' && orderNote.trim() === ''">
        <a-form
          :model="orderNoteDate"
          :style="{ width: '450px', margin: '0 auto' }"
          @submit="handleOrderNote"
        >
          <a-form-item
            field="orderNote"
            tooltip="请输入订单备注"
            label="订单备注"
          >
            <a-input
              v-model="orderNoteDate.orderNote"
              placeholder="可添加订单备注"
            />
          </a-form-item>
          <a-form-item>
            <a-button type="primary" html-type="submit">添加</a-button>
          </a-form-item>
        </a-form>
      </template>
    </div>
    <a-modal v-model:visible="visible" @ok="handleOk" @cancel="handleCancel">
      <template #title> 重新发布</template>
      <div>
        <a-form :model="form1">
          <a-form-item field="estimatedCompletionTime" label="期望完成时间">
            <a-time-picker
              v-model="form1.estimatedCompletionTime"
              style="width: 300px"
              format="YYYY-MM-DD HH:mm"
            />
          </a-form-item>
          <a-form-item field="price" label="任务金额">
            <a-space direction="vertical" size="large">
              <a-input-number
                v-model="form1.price"
                :style="{ width: '300px' }"
                placeholder="回车确认"
                :step="1"
                :precision="2"
                :min="1"
                :max="9999999"
                class="input-demo"
              />
            </a-space>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  AddOrderNoteRequest,
  OrderControllerService,
  TaskControllerService,
  TaskUpdateRequest,
  TaskUpdateStatusRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import store from "@/store";

const size = ref("large");
const taskStatus = ref("");
const orderNoteDate = ref({
  id: 0,
  orderNote: "",
} as AddOrderNoteRequest); // 订单备注
let orderNote = ref("");
const handleOrderNote = async () => {
  const res = await OrderControllerService.addOrderNoteUsingPost(
    orderNoteDate.value
  );
  if (res.code === 0) {
    location.reload();
    message.success("订单备注添加成功！");
  } else {
    message.error("订单备注添加失败，" + res.message);
  }
};

let form1 = ref({
  id: 0,
  estimatedCompletionTime: "",
  price: 0,
  status: "待接单",
} as TaskUpdateRequest);

const visible = ref(false);

const handleOk = async () => {
  visible.value = false;
  const res = await TaskControllerService.updateUsingPost(form1.value);
  if (res.code === 0) {
    message.success("重新发布成功");
    location.reload();
  } else {
    message.error("重新发布失败，" + res.message);
  }
};
const handleCancel = () => {
  visible.value = false;
};

const data = reactive([
  {
    label: "订单编号",
    value: "",
  },
  {
    label: "订单状态",
    value: "",
  },
  {
    label: "下单时间",
    value: "",
  },
  {
    label: "取件地址",
    value: "",
  },
  {
    label: "收货地址",
    value: "",
  },
  {
    label: "订单备注",
    value: "",
  },
  {
    label: "预计送达时间",
    value: "",
  },
  {
    label: "实际完成时间",
    value: "",
  },
  {
    label: "配送员编号",
    value: "",
  },
  {
    label: "配送员姓名",
    value: "",
  },
  {
    label: "配送员手机号码",
    value: "",
  },
  {
    label: "配送员平均评分",
    value: "",
  },
  {
    label: "任务编号",
    value: "",
  },
  {
    label: "服务类型",
    value: "",
  },
  {
    label: "物品名称",
    value: "",
  },
  {
    label: "物品重量",
    value: "",
  },
  {
    label: "描述信息",
    value: "",
  },
  {
    label: "任务金额",
    value: "",
  },
  {
    label: "任务状态",
    value: "",
  },
]);
const fetchData = async (taskId: number) => {
  const res = await OrderControllerService.getOrderVoUsingGet(taskId);
  if (res.code === 0) {
    const resData = res.data;
    data.forEach((item) => {
      deliverymanUserId = resData?.deliveryman?.userId as any;
      switch (item.label) {
        case "订单编号":
          item.value = resData?.id ? resData.id.toString() : "";
          orderNoteDate.value.id = resData?.id as number;
          form2.orderId = item.value as any;
          break;
        case "订单状态":
          item.value = resData?.orderStatus || "";
          break;
        case "下单时间":
          if (resData?.orderTime?.trim()) {
            item.value = formatTime(resData?.orderTime || "");
          }
          break;
        case "取件地址":
          item.value = formatAddress(resData?.fetchAddress || "");
          break;
        case "收货地址":
          item.value = formatAddress(resData?.shippingAddress || "");
          break;
        case "订单备注":
          item.value = resData?.orderNote || "";
          orderNote.value = item.value;
          break;
        case "预计送达时间":
          if (resData?.estimatedCompletionTime?.trim()) {
            item.value = formatTime(resData?.estimatedCompletionTime || "");
          }
          break;
        case "实际完成时间":
          if (resData?.completionTime?.trim()) {
            item.value = formatTime(resData?.completionTime || "");
          }
          break;
        case "配送员编号":
          item.value = resData?.deliveryman
            ? resData.deliveryman.id
              ? resData.deliveryman.id.toString()
              : ""
            : "";
          deliverymanId = item.value;
          break;
        case "配送员姓名":
          item.value = resData?.deliveryman
            ? resData.deliveryman.name || ""
            : "";
          break;
        case "配送员手机号码":
          item.value = resData?.deliveryman
            ? resData.deliveryman.phoneNumber || ""
            : "";
          break;
        case "配送员平均评分":
          item.value = resData?.deliveryman
            ? resData.deliveryman.averageRating
              ? resData.deliveryman.averageRating.toString()
              : ""
            : "";
          break;
        case "任务编号":
          item.value = resData?.task
            ? resData.task.id
              ? resData.task.id.toString()
              : ""
            : "";
          form2.id = item.value as any;
          break;
        case "服务类型":
          item.value = resData?.task ? resData.task.serviceType || "" : "";
          break;
        case "物品名称":
          item.value = resData?.task ? resData.task.itemName || "" : "";
          break;
        case "物品重量":
          item.value = resData?.task ? resData.task.itemWeight || "" : "";
          break;
        case "描述信息":
          item.value = resData?.task ? resData.task.description || "" : "";
          break;
        case "任务金额":
          item.value =
            (resData?.task?.price ? resData.task?.price.toString() : "") + "元";
          form1.value.price = resData?.task?.price as any;
          break;
        case "任务状态":
          item.value = resData?.task ? resData.task.status || "" : "";
          taskStatus.value = item.value;
          break;
        default:
          break;
      }
    });
  }
};
const route = useRoute();
const router = useRouter();

onMounted(() => {
  const taskId = route.query.taskId;
  form1.value.id = taskId as any;
  fetchData(taskId as any);
  const loginUser = store.state.user.loginUser;
  if (!loginUser || loginUser.username === "未登录") {
    router.push("/user/login");
  }
});

const formatAddress = (address: string) => {
  // Address{address='新建区黄家湖东路111号', consignee='吴奇安', phoneNumber='18179307809'}
  const matches = address.match(
    /Address{address='([^']*)', consignee='([^']*)', phoneNumber='([^']*)'}/
  );
  if (matches && matches.length === 4) {
    const address = matches[1];
    const consignee = matches[2];
    const phoneNumber = matches[3];
    // 返回格式化后的地址字符串
    return `${address} ( ${consignee} ) ${phoneNumber}`;
  } else {
    // 如果字符串格式不匹配，则返回原始字符串
    return address;
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

const filteredData = computed(() => {
  return data.filter((item) => item.value?.trim() !== "");
});

const rePublish = () => {
  visible.value = true;
};

let deliverymanId = 0 as any;
const form2 = reactive({} as TaskUpdateStatusRequest);
const completeTask = async () => {
  form2.status = "已完成";
  const res = await OrderControllerService.updateStatusUsingPost1(form2);
  if (res.code === 0) {
    await router.push({
      path: "/reviews",
      query: {
        orderId: orderNoteDate.value.id,
        deliverymanId: deliverymanId,
        taskId: route.query.taskId,
      },
    });
  } else {
    message.error("操作错误，" + res.message);
  }
};

const goToReview = () => {
  // 去评价
  router.push({
    path: "/reviews",
    query: {
      orderId: orderNoteDate.value.id,
      deliverymanId: deliverymanId,
      taskId: route.query.taskId,
    },
  });
};

const cancelOrder = async () => {
  const cancelData = {
    id: form1.value.id,
    status: "已取消",
  } as TaskUpdateRequest;
  const res = await TaskControllerService.updateUsingPost(cancelData);
  if (res.code === 0) {
    message.success("订单取消成功");
    // 页面刷新
    location.reload();
  } else {
    message.error("订单取消失败", res.message);
  }
};

const goToDeliverymanPortraits = () => {
  router.push({
    path: "/deliverymanPortraits",
    query: {
      deliverymanId: deliverymanId,
    },
  });
};

let deliverymanUserId: number;

const contactDelivery = () => {
  router.push({
    path: "/chat",
    query: {
      toUserId: deliverymanUserId,
    },
  });
};
</script>

<style>
.operate {
  position: relative;
}

#basicLayout > section > main > div > div.operate > button {
  position: absolute;
  left: 50%;
  margin-top: 20px;
  transform: translateX(-50%);
}
</style>
