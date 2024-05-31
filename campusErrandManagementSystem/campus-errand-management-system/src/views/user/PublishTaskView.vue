<template>
  <div class="publishTaskDiv">
    <h1 style="text-align: center">发布任务</h1>
    <a-form :model="form" :layout="'vertical'" @submit="handleSubmit">
      <a-form-item field="serviceType" label="服务类型">
        <a-space direction="vertical" size="large">
          <a-select
            :style="{ width: '400px' }"
            placeholder="请选择服务类型"
            allow-search
            v-model="form.serviceType"
          >
            <a-option>帮取餐</a-option>
            <a-option>取送文件</a-option>
            <a-option>取快递</a-option>
            <a-option>送鲜花</a-option>
            <a-option>送蛋糕</a-option>
            <a-option>送礼盒</a-option>
            <a-option>送生鲜</a-option>
            <a-option>取钥匙</a-option>
            <a-option>送数码</a-option>
            <a-option>送服饰</a-option>
            <a-option>取送眼镜</a-option>
            <a-option>送日用品</a-option>
            <a-option>其他</a-option>
          </a-select>
        </a-space>
      </a-form-item>
      <a-form-item field="itemName" label="物品名称">
        <a-space>
          <a-input
            :style="{ width: '400px' }"
            v-model="form.itemName"
            placeholder="请输入物品名称"
            allow-clear
          />
        </a-space>
      </a-form-item>
      <a-form-item field="fetchAddress" label="取件地址">
        <a-space direction="vertical" size="large">
          <a-select
            v-model="form.fetchAddress"
            :style="{ width: '400px' }"
            :options="options"
            :field-names="fieldNames"
            placeholder="请选择取件地址"
          ></a-select>
        </a-space>
      </a-form-item>
      <a-form-item field="shippingAddress" label="收货地址">
        <a-space direction="vertical" size="large">
          <a-select
            v-model="form.shippingAddress"
            :style="{ width: '400px' }"
            :options="options"
            :field-names="fieldNames"
            placeholder="请选择收获地址(请确保能送到收货人手中)"
          >
          </a-select>
        </a-space>
      </a-form-item>
      <a-form-item field="itemWeight" label="物品重量">
        <a-slider
          v-model="form.itemWeight"
          :max="25"
          :format-tooltip="formatter"
          :marks="marks"
        />
      </a-form-item>
      <a-form-item field="description" label="可备注物品描述、送件要求等">
        <a-textarea
          v-model="form.description"
          placeholder="输入物品描述或取件要求等，请勿填写取件码等敏感信息，如有请在配送员接单后再沟通"
          :auto-size="{
            minRows: 2,
            maxRows: 5,
          }"
          style="margin-top: 20px"
        />
      </a-form-item>
      <a-form-item field="description" label="期望完成时间">
        <a-time-picker
          v-model="form.estimatedCompletionTime"
          style="width: 400px"
          format="YYYY-MM-DD HH:mm"
        />
      </a-form-item>
      <a-form-item field="description" label="任务金额">
        <a-space direction="vertical" size="large">
          <a-input-number
            v-model="form.price"
            :style="{ width: '400px' }"
            placeholder="回车确认"
            :step="1"
            :precision="2"
            :min="1"
            :max="9999999"
            class="input-demo"
          />
        </a-space>
      </a-form-item>
      <a-form-item field="isRead" style="display: flex; align-items: center">
        <span style="display: flex; align-items: center">
          <a-checkbox v-model="form.isRead">已阅读并同意</a-checkbox
          ><a-link href="link" disabled icon>《帮送服务协议》</a-link>
        </span>
      </a-form-item>
      <a-form-item>
        <a-button html-type="submit">提交任务</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts">
import { onMounted, reactive, ref } from "vue";
import message from "@arco-design/web-vue/es/message";
import {
  Address,
  AddressControllerService,
  TaskControllerService,
} from "../../../generated";
import { useRouter } from "vue-router";

export default {
  setup() {
    const form = reactive({
      serviceType: "",
      itemName: "",
      fetchAddress: "",
      shippingAddress: "",
      itemWeight: 3,
      description: "",
      estimatedCompletionTime: "",
      price: 1.0,
      isRead: false,
    });
    const marks = {
      5: "5公斤",
      10: "10公斤",
      15: "15公斤",
      20: "20公斤",
    };
    const formatter = (value: any) => {
      if (value < 5) {
        return "小于5公斤";
      }
      if (value > 20) {
        return "大于20公斤";
      }
      return value + "公斤";
    };
    const router = useRouter();
    const handleSubmit = async () => {
      if (!form.isRead) {
        // 不同意
        message.error("请先同意服务协议！");
      } else {
        console.log(form);
        const res = await TaskControllerService.addTaskUsingPost(form as any);
        if (res.code === 0) {
          message.success("发布成功");
          await router.push("/userOrderManagement");
        } else {
          message.error("错误,", res.message);
        }
      }
    };

    const fetchAddress = async () => {
      const res = await AddressControllerService.getAllAddressUsingGet();
      if (res.code === 0) {
        // 清空数组
        options.splice(0, options.length);
        res.data?.forEach((value: Address, key: number) => {
          /**
           * Address{address='具体地址', consignee='收件人姓名', phoneNumber='电话号码'}
           */
          options[key] = reactive({
            format:
              "Address{address='" +
              value.address +
              "', consignee='" +
              value.consignee +
              "', phoneNumber='" +
              value.phoneNumber +
              "'}",
            address:
              value.address +
              " ( " +
              value.consignee +
              " ) " +
              value.phoneNumber,
          });
        });
      } else {
        message.error("地址请求错误," + res.message);
      }
    };

    onMounted(() => {
      fetchAddress();
    });

    const fieldNames = { value: "format", label: "address" };
    const options = reactive([
      {
        format: "",
        address: "",
      },
    ]);

    return {
      form,
      handleSubmit,
      marks,
      formatter,
      fieldNames,
      options,
    };
  },
};
</script>
<style scoped>
#basicLayout > section > main > div {
  max-width: 400px;
  margin: 0 auto;
}
</style>
