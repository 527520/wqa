<template>
  <div class="BecomeDeliveryManDiv" v-if="!flag">
    <h1 style="text-align: center">申请成为配送员</h1>
    <a-form :model="form" :layout="'vertical'" @submit="handleForm">
      <a-form-item field="name" label="姓名">
        <a-input v-model="form.name" placeholder="请输入真实姓名..." />
      </a-form-item>
      <a-form-item field="sex" label="性别">
        <a-space size="large">
          <a-radio value="男" v-model="form.sex" :default-checked="true"
            >男
          </a-radio>
          <a-radio value="女" v-model="form.sex">女</a-radio>
        </a-space>
      </a-form-item>
      <a-form-item field="birthday" label="生日">
        <a-date-picker v-model="form.birthday" />
      </a-form-item>
      <a-form-item field="phoneNumber" tooltip="电话号码" label="电话号码">
        <a-input
          v-model="form.phoneNumber"
          placeholder="请输入正确的电话号码"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="idNumber" label="身份证号">
        <a-input v-model="form.idNumber" placeholder="请输入身份证号..." />
      </a-form-item>
      <a-form-item field="isRead">
        <a-checkbox v-model="form.isRead"
          >是否有时间灵活、能够接单，是否有意愿加入配送员团队
        </a-checkbox>
      </a-form-item>
      <a-form-item field="idCardFrontImageStr" label="身份证正面照">
        <a-space direction="vertical" :style="{ width: '100%' }">
          <a-upload
            :draggable="true"
            v-model="form.idCardFront"
            @change="handleFileUpload1"
            :auto-upload="false"
          />
        </a-space>
      </a-form-item>
      <a-form-item field="idCardFrontImageStr" label="身份证背面照">
        <a-space direction="vertical" :style="{ width: '100%' }">
          <a-upload
            :draggable="true"
            v-model="form.idCardBack"
            @change="handleFileUpload2"
          />
        </a-space>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
  <div class="reviewProgressDiv" v-if="flag">
    <h1 style="text-align: center">提交申请进度</h1>
    <a-steps label-placement="vertical" :current="+current" :status="status1">
      <a-step description="已提交申请，正在通知管理员审核">已提交</a-step>
      <a-step :description="description2" :title="title2"></a-step>
      <a-step description="审核通过，请等待管理员与您联系，下发账号"
        >审核通过
      </a-step>
    </a-steps>
    <a-button
      type="outline"
      v-if="title2 === StatusEnum.FAIL"
      @click="applyAgain"
      style="position: absolute;
             left: 50%;
             transform: translateX(-50%);
             margin-top: 200px;
      }"
      >重新申请
    </a-button>
  </div>
</template>
<script lang="ts">
import { onMounted, reactive, ref } from "vue";
import message from "@arco-design/web-vue/es/message";
import {
  BecomeDeliverymanControllerService,
  BecomeDeliverymanVO,
} from "../../../generated";
import { FileItem } from "@arco-design/web-vue";
import STATUS_ENUM from "@/access/statusEnum";
import StatusEnum from "@/access/statusEnum";

export default {
  computed: {
    StatusEnum() {
      return StatusEnum;
    },
  },
  setup() {
    const form = reactive({
      name: "",
      sex: "",
      birthday: "",
      idNumber: "",
      phoneNumber: "",
      isRead: false,
      idCardFront: "",
      idCardBack: "",
    });
    const handleForm = async () => {
      if (!form.isRead) {
        // 不同意
        message.error("没有意愿不能加入团队哟！");
      } else {
        const res =
          await BecomeDeliverymanControllerService.becomeDeliverymanUsingPost(
            form
          );
        if (res.code === 0) {
          message.success("提交成功，请等待管理员审核");
          location.reload();
        } else {
          message.error("提交失败，" + res.message);
        }
      }
    };

    const isUploading = ref(false);
    const handleFileUpload1 = async (_: any, currentFile: FileItem) => {
      if (isUploading.value) {
        return;
      }
      isUploading.value = true;
      const res =
        await BecomeDeliverymanControllerService.uploadIdCardUsingPost(
          currentFile?.file
        );
      if (res.code === 0) {
        form.idCardFront = res.data;
        message.success("文件上传成功！");
        currentFile.status = "done";
      } else {
        message.error("文件上传失败，" + res.message);
      }
      isUploading.value = false;
    };
    const handleFileUpload2 = async (_: any, currentFile: FileItem) => {
      if (isUploading.value) {
        return;
      }
      isUploading.value = true;
      const res =
        await BecomeDeliverymanControllerService.uploadIdCardUsingPost(
          currentFile?.file
        );
      if (res.code === 0) {
        form.idCardBack = res.data;
        message.success("文件上传成功！");
        currentFile.status = "done";
      } else {
        message.error("文件上传失败，" + res.message);
      }
      isUploading.value = false;
    };
    let flag = ref(false);

    let formData = reactive({
      status: "",
      message: "",
    } as BecomeDeliverymanVO);
    let current = ref(1);
    let status1 = ref("finish");
    let title2 = ref(STATUS_ENUM.UNDER_REVIEW);
    let description2 = ref("管理员正在审核，请耐心等待，保持电话畅通");
    onMounted(async () => {
      const res =
        await BecomeDeliverymanControllerService.getProgressUsingGet();
      console.log(res.data);
      if (res.code === 0) {
        flag.value = true;
        formData = res.data as BecomeDeliverymanVO;
        if (formData.status === STATUS_ENUM.UNDER_REVIEW) {
          current.value = 2;
          status1.value = "process";
        }
        if (formData.status === STATUS_ENUM.FAIL) {
          current.value = 2;
          status1.value = "error";
          title2.value = STATUS_ENUM.FAIL;
          description2.value = formData.message as string;
        }
        if (formData.status === STATUS_ENUM.PASS) {
          current.value = 3;
          status1.value = "finish";
        }
      } else {
        flag.value = false;
      }
    });
    const applyAgain = (ev: MouseEvent) => {
      flag.value = false;
    };
    return {
      form,
      handleForm,
      handleFileUpload1,
      handleFileUpload2,
      flag,
      formData,
      current,
      status1,
      title2,
      description2,
      applyAgain,
    };
  },
};
</script>
<style scoped>
#basicLayout > section > main > div {
  max-width: 400px;
  margin: 0 auto;
}

#basicLayout > section > main > div.reviewProgressDiv {
  max-width: 800px;
}
</style>
