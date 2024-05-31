<template>
  <a-table
    stripe
    :columns="columns"
    :data="data"
    :filter-icon-align-left="alignLeft"
  >
    <template #optional="{ record }">
      <span>
        <a-button type="primary" @click="takeOrder(record.id)"
          >接取订单</a-button
        >
      </span>
    </template>
    <template #publishTime="{ record }">
      <span>{{ formatTime(record.publishTime) }}</span>
    </template>
    <template #estimatedCompletionTime="{ record }">
      <span>{{ formatTime(record.estimatedCompletionTime) }}</span>
    </template>
    <template #fetchAddress="{ record }">
      <span>{{ formatAddress(record.fetchAddress) }}</span>
    </template>
    <template #shippingAddress="{ record }">
      <span>{{ formatAddress(record.shippingAddress) }}</span>
    </template>
  </a-table>
</template>
<script lang="ts">
import { onMounted, reactive, ref } from "vue";
import {
  Task,
  TaskControllerService,
  TaskReceiveRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

export default {
  setup() {
    const columns = [
      {
        title: "服务类型",
        dataIndex: "serviceType",
      },
      {
        title: "物品名称",
        dataIndex: "itemName",
      },
      {
        title: "物品重量",
        dataIndex: "itemWeight",
      },
      {
        title: "发布时间",
        dataIndex: "publishTime",
        sortable: {
          sortDirections: ["ascend", "descend"],
        },
        slots: { customRender: "publishTime" },
        slotName: "publishTime",
      },
      {
        title: "期望完成时间",
        dataIndex: "estimatedCompletionTime",
        sortable: {
          sortDirections: ["ascend", "descend"],
        },
        slots: { customRender: "estimatedCompletionTime" },
        slotName: "estimatedCompletionTime",
      },
      {
        title: "价格",
        dataIndex: "price",
        sortable: {
          sortDirections: ["ascend", "descend"],
        },
      },
      {
        title: "取件地址",
        dataIndex: "fetchAddress",
        slots: { customRender: "fetchAddress" },
        slotName: "fetchAddress",
      },
      {
        title: "收货地址",
        dataIndex: "shippingAddress",
        slots: { customRender: "shippingAddress" },
        slotName: "shippingAddress",
      },
      {
        title: "描述信息",
        dataIndex: "description",
      },
      {
        title: "操作",
        slots: { customRender: "operation" },
        slotName: "optional",
        fixed: "right",
        width: 100,
      },
    ];
    let data = ref([
      {
        description: "",
        estimatedCompletionTime: "",
        fetchAddress: "",
        id: 0,
        itemName: "",
        itemWeight: "",
        price: 0,
        publishTime: "",
        serviceType: "",
        shippingAddress: "",
        status: "",
      } as Task,
    ]);
    const fetchData = async () => {
      const res = await TaskControllerService.getTaskByDeliverymanUsingGet();
      if (res.code === 0) {
        data.value = res.data as any;
      } else {
        message.error("错误，" + res.message);
      }
    };
    onMounted(() => {
      fetchData();
    });

    const router = useRouter();
    const takeOrder = async (id: number) => {
      const taskReceiveRequest = reactive({
        id: id,
      } as TaskReceiveRequest);
      const res = await TaskControllerService.receiveTaskUsingPost(
        taskReceiveRequest
      );
      if (res.code === 0) {
        message.success("订单接取成功");
        await router.push("/dOManagement");
      } else {
        message.error("接取失败，" + res.message);
      }
    };

    const alignLeft = ref(false);

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

    return {
      columns,
      data,
      takeOrder,
      alignLeft,
      formatTime,
      formatAddress,
    };
  },
};
</script>
<style scoped></style>
