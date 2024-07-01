<template>
  <div>
    <h1 style="text-align: center">配送订单</h1>
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
      <a-button type="primary" @click="contactCustomer">联系顾客</a-button>
    </div>
    <div class="operate">
      <template v-if="taskStatus === '已接单'">
        <a-button type="primary" @click="toDelivery"
          >已取货 去配送
          <icon-double-right size="18px" />
        </a-button>
      </template>
      <template v-else-if="taskStatus === '配送中'">
        <a-popconfirm
          content="确定送到顾客手上并拿到任务金额了吗？"
          @ok="delivered"
          type="success"
        >
          <a-button type="primary">已送达</a-button>
        </a-popconfirm>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import {
  OrderControllerService,
  OrderVO,
  TaskUpdateStatusRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRoute, useRouter } from "vue-router";

const size = ref("large");
const taskStatus = ref("");
let formData = reactive({
  id: 0, //任务id
  orderId: 0,
  status: "",
} as TaskUpdateStatusRequest);

const toDelivery = async () => {
  formData.status = "配送中";
  const res = await OrderControllerService.updateStatusUsingPost1(formData);
  if (res.code === 0) {
    message.success("现在让我们出发尽快送到顾客手中吧！");
    location.reload();
  } else {
    message.error("系统出错，稍后再试，" + res.message);
  }
};

const delivered = async () => {
  formData.status = "已送达";
  const res = await OrderControllerService.updateStatusUsingPost1(formData);
  if (res.code === 0) {
    message.success("送达成功，继续接单吧！");
    location.reload();
  } else {
    message.error("系统出错，稍后再试，" + res.message);
  }
};

const data = reactive([
  {
    label: "取件地址",
    value: "",
  },
  {
    label: "收货地址",
    value: "",
  },
  {
    label: "预计送达时间",
    value: "",
  },
  {
    label: "订单备注",
    value: "",
  },
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
    label: "实际完成时间",
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

let toId: number;

const fetchData1 = async () => {
  // 等待联调
  const res = await OrderControllerService.deliverymanGetOrderVoUsingGet();
  if (res.code === 0) {
    const resData = res.data;
    toId = resData?.task?.userId as number;
    evaluation(resData as OrderVO);
  } else {
    message.error("信息查询失败，" + res.message);
  }
};

const fetchData2 = async (taskId: number) => {
  const res = await OrderControllerService.getOrderVoUsingGet(taskId as any);
  if (res.code === 0) {
    const resData = res.data;
    evaluation(resData as OrderVO);
  } else {
    message.error("信息查询失败，" + res.message);
  }
};

const route = useRoute();
onMounted(() => {
  const taskId = route.query.taskId;
  if (taskId) {
    fetchData2(taskId as any);
  } else {
    fetchData1();
  }
});

const evaluation = (resData: OrderVO) => {
  data.forEach((item) => {
    userId = resData.task?.userId as number;
    switch (item.label) {
      case "订单编号":
        item.value = resData?.id ? resData.id.toString() : "";
        formData.orderId = item.value as any;
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
      case "任务编号":
        item.value = resData?.task
          ? resData.task.id
            ? resData.task.id.toString()
            : ""
          : "";
        formData.id = item.value as any;
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
        break;
      case "任务状态":
        item.value = resData?.task ? resData.task.status || "" : "";
        taskStatus.value = item.value;
        break;
      default:
        break;
    }
  });
};

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

let userId: number;
const router = useRouter();

const contactCustomer = () => {
  router.push({
    path: "/chat",
    query: {
      toUserId: userId,
    },
  });
};
</script>

<style>
.operate {
  position: relative;
  min-height: 70px;
}

#basicLayout > section > main > div > div.operate > button {
  position: absolute;
  left: 50%;
  margin-top: 20px;
  transform: translateX(-50%);
}

#basicLayout
  > section
  > main
  > div
  > div.arco-descriptions.arco-descriptions-layout-horizontal.arco-descriptions-size-large
  > div.arco-descriptions-body
  > table
  > tbody
  > tr:nth-child(1)
  > td.arco-descriptions-item-value.arco-descriptions-item-value-block {
  font-size: 18px;
}

#basicLayout
  > section
  > main
  > div
  > div.arco-descriptions.arco-descriptions-layout-horizontal.arco-descriptions-size-large
  > div.arco-descriptions-body
  > table
  > tbody
  > tr:nth-child(2)
  > td.arco-descriptions-item-value.arco-descriptions-item-value-block {
  font-size: 18px;
}
</style>
