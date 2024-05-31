<template>
  <a-table
    stripe
    :columns="columns"
    :data="data"
    :filter-icon-align-left="alignLeft"
    :scroll="scroll"
    :pagination="false"
  >
    <template #optional="{ record }">
      <span>
        <a-button type="primary" @click="assignDeliveryman(record.id)"
          >选择配送员</a-button
        >
        <!--toto 此处没有实现-->
        <a-button
          type="outline"
          style="color: red; margin-left: 5px"
          @click="assignDeliveryman(record.id)"
          >取消订单</a-button
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
  <a-modal v-model:visible="visible1" @ok="handleOk1" @cancel="handleCancel1">
    <template #title>选择配送员</template>
    <div>
      <a-space direction="vertical" size="large">
        <a-select
          :style="{ width: '320px' }"
          placeholder="请选择配送员"
          allow-search
          v-model="form.deliverymanId"
        >
          <a-option
            v-for="deliveryman in deliverymanList"
            :key="deliveryman"
            :value="deliveryman.id as number"
            >{{ deliveryman.name }}
          </a-option>
        </a-select>
      </a-space>
    </div>
  </a-modal>
</template>
<script lang="ts">
import { onMounted, reactive, ref } from "vue";
import {
  AssignTaskRequest,
  DeliverymanControllerService,
  DeliverymanVOResponse,
  Task,
  TaskControllerService,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";

export default {
  setup() {
    const columns = [
      {
        title: "服务类型",
        dataIndex: "serviceType",
        width: 150,
      },
      {
        title: "物品名称",
        dataIndex: "itemName",
        width: 150,
      },
      {
        title: "物品重量",
        dataIndex: "itemWeight",
        width: 150,
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
        width: 150,
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
        width: 230,
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

    const assignDeliveryman = async (id: number) => {
      // 分配订单给配送员
      console.log(id);
      form.taskId = id;
      const res = await DeliverymanControllerService.getAllInlineUsingGet();
      if (res.code === 0) {
        deliverymanList.value = res.data as any;
      } else {
        message.error("请求失败," + res.message);
      }
      visible1.value = true;
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

    let form = reactive({} as AssignTaskRequest);

    let deliverymanList = ref([
      {} as DeliverymanVOResponse,
    ] as Array<DeliverymanVOResponse>);

    const visible1 = ref(false);

    const handleCancel1 = () => {
      visible1.value = false;
    };

    const handleOk1 = async () => {
      const res = await TaskControllerService.assignTaskUsingPost(form);
      if (res.code === 0) {
        message.success("订单分配成功");
        await fetchData();
      } else {
        message.error("订单分配失败，" + res.message);
      }
      console.log(form);
    };

    const scroll = {
      x: 2000,
      y: 1000,
    };

    return {
      columns,
      data,
      alignLeft,
      formatTime,
      formatAddress,
      assignDeliveryman,
      form,
      visible1,
      handleCancel1,
      handleOk1,
      deliverymanList,
      scroll,
    };
  },
};
</script>
<style scoped></style>
