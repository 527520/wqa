<template>
  <div class="about">
    <a-tabs
      :active-key="activeKey"
      @tab-click="changeTab"
      :position="'left'"
      lazy-load
      :size="'large'"
    >
      <a-tab-pane class="pane" key="1" title="个人信息">
        <a-card
          hoverable
          :style="{
            maxWidth: '800px',
            margin: '0 auto',
            padding: '20px',
          }"
        >
          <template #cover>
            <a-upload
              :show-file-list="false"
              :auto-upload="false"
              @change="handleFileUpload"
              style="
                max-width: 150px;
                position: absolute;
                left: 50%;
                transform: translateX(-50%);
                margin-top: 6px;
              "
            >
              <template #upload-button>
                <div
                  style="
                    color: var(--color-text-1);
                    text-align: center;
                    margin: 0 auto;
                  "
                >
                  <div>
                    <a-avatar :size="150" style="margin: 0 auto">
                      <img alt="dessert" :src="form.userAvatar" />
                    </a-avatar>
                  </div>
                </div>
              </template>
            </a-upload>
          </template>
          <a-card-meta style="margin-top: 160px">
            <template #description>
              <a-form
                :model="form"
                style="max-width: 400px; margin: 0 auto"
                label-align="left"
                auto-label-width
                @submit="handleUpdate"
              >
                <a-form-item
                  field="phoneNumber"
                  tooltip="电话号码"
                  label="电话号码"
                >
                  <a-input
                    v-model="form.phoneNumber"
                    placeholder="请输入正确的电话号码"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item field="username" tooltip="真实姓名" label="姓名">
                  <a-input
                    v-model="form.username"
                    placeholder="请输入真实姓名"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item>
                  <a-button type="primary" html-type="submit">修改</a-button>
                </a-form-item>
              </a-form>
            </template>
          </a-card-meta>
        </a-card>
      </a-tab-pane>
      <a-tab-pane class="pane" key="2" title="地址管理">
        <div>
          <h2 style="text-align: center; margin-bottom: 32px">
            地址管理
            <a-space style="margin-left: 16px">
              <a-button type="primary" @click="addAddress">
                <template #icon>
                  <icon-plus />
                </template>
              </a-button>
            </a-space>
          </h2>
          <a-card
            hoverable
            :style="{
              width: '600px',
              margin: '20px auto 20px',
              minHeight: '100px',
              position: 'relative',
            }"
            v-for="address in formAddress"
            :key="address"
          >
            <div
              :style="{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                flexWrap: 'wrap',
              }"
            >
              <span
                :style="{
                  display: 'flex',
                  alignItems: 'center',
                  color: '#1D2129',
                }"
              >
                <a-typography-text style="font-size: 17px">{{
                  address.address
                }}</a-typography-text>
              </span>
              <span>
                <icon-edit
                  @click="editAddress(address)"
                  style="cursor: pointer"
                />
                <!--toto 此处没有实现-->
                <icon-delete
                  @click="deleteAddress(address.id)"
                  style="cursor: pointer; margin-left: 10px"
                />
              </span>
            </div>
            <span style="margin-top: 10px; position: absolute; bottom: 20px">
              <span>{{ address.consignee }}</span>
              <span style="margin-left: 20px">{{ address.phoneNumber }}</span>
            </span>
          </a-card>
          <a-modal
            v-model:visible="visible"
            @ok="handleOk"
            @cancel="handleCancel"
          >
            <template #title> 修改地址</template>
            <div>
              <a-form :model="form" label-align="left" auto-label-width>
                <a-form-item
                  field="consignee"
                  tooltip="姓名"
                  label="收货人姓名"
                >
                  <a-input
                    v-model="data.consignee"
                    placeholder="请输入收货人姓名"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item
                  field="phoneNumber"
                  tooltip="确保能联系到收货人"
                  label="电话号码"
                >
                  <a-input
                    v-model="data.phoneNumber"
                    placeholder="请输入正确的电话号码"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item
                  field="address"
                  tooltip="详细收货地址，确保能送到您的手中"
                  label="收货地址"
                >
                  <a-input
                    v-model="data.address"
                    placeholder="请输入详细地址"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item>
                  <a-checkbox v-model="data.isDefault">默认地址</a-checkbox>
                </a-form-item>
              </a-form>
            </div>
          </a-modal>
          <a-modal
            v-model:visible="visible1"
            @ok="handleOk1"
            @cancel="handleCancel1"
          >
            <template #title>添加地址</template>
            <div>
              <a-form :model="form" label-align="left" auto-label-width>
                <a-form-item
                  field="consignee"
                  tooltip="姓名"
                  label="收货人姓名"
                >
                  <a-input
                    v-model="data1.consignee"
                    placeholder="请输入收货人姓名"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item
                  field="phoneNumber"
                  tooltip="确保能联系到收货人"
                  label="电话号码"
                >
                  <a-input
                    v-model="data1.phoneNumber"
                    placeholder="请输入正确的电话号码"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item
                  field="address"
                  tooltip="详细收货地址，确保能送到您的手中"
                  label="收货地址"
                >
                  <a-input
                    v-model="data1.address"
                    placeholder="请输入详细地址"
                    allow-clear
                  />
                </a-form-item>
                <a-form-item>
                  <a-checkbox v-model="data1.isDefault">默认地址</a-checkbox>
                </a-form-item>
              </a-form>
            </div>
          </a-modal>
        </div>
      </a-tab-pane>
      <a-tab-pane class="pane" key="3" title="修改密码">
        <h2 style="text-align: center; margin-bottom: 32px">修改密码</h2>
        <a-form
          :model="form1"
          :style="{ width: '600px', margin: '0 auto' }"
          @submit="handleSubmitUpdate"
        >
          <a-form-item
            field="oldPassword"
            tooltip="请输入旧密码..."
            label="旧密码"
          >
            <a-input-password
              v-model="form1.oldPassword"
              placeholder="请输入旧密码..."
            />
          </a-form-item>
          <a-form-item field="userPassword" label="新密码">
            <a-input-password
              v-model="form1.userPassword"
              placeholder="请输入新密码..."
            />
          </a-form-item>
          <a-form-item field="checkPassword" label="确认密码">
            <a-input-password
              v-model="form1.checkPassword"
              placeholder="确认密码..."
            />
          </a-form-item>
          <a-form-item field="isRead">
            <a-checkbox v-model="form1.isRead">改好了吗，不再看看？</a-checkbox>
          </a-form-item>
          <a-form-item>
            <a-button html-type="submit">修改密码</a-button>
          </a-form-item>
        </a-form>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script lang="ts">
import {
  Address,
  AddressControllerService,
  UserControllerService,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import store from "@/store";
import { onMounted, reactive, ref } from "vue";
import { FileItem } from "@arco-design/web-vue";
import { useRouter } from "vue-router";

export default {
  setup() {
    let activeKey = ref("1");
    const changeTab = (key: string) => {
      activeKey.value = key;
      localStorage.setItem("selectedActiveKey", key);
    };
    let loginUser = store.state.user.loginUser;
    const form = ref({
      username: loginUser.username,
      phoneNumber: loginUser.phoneNumber,
      userAvatar: loginUser.userAvatar,
    });
    const form1 = reactive({
      userPassword: "",
      checkPassword: "",
      oldPassword: "",
      isRead: false,
    });
    const fetchData1 = async () => {
      const res = await UserControllerService.updateMyUserUsingPost(form.value);
      if (res.code === 0) {
        message.success("个人信息修改成功");
      } else {
        message.error("操作失败，", res.message);
      }
    };
    const isUploading = ref(false);
    const handleFileUpload = async (_: any, currentFile: FileItem) => {
      if (isUploading.value) {
        return;
      }
      isUploading.value = true;
      const res = await UserControllerService.uploadAvatarUsingPost(
        currentFile?.file
      );
      if (res.code === 0) {
        form.value.userAvatar = res.data;
        message.success("头像修改成功！");
        currentFile.status = "done";
      } else {
        message.error("文件上传失败，" + res.message);
      }
      isUploading.value = false;
    };
    const handleUpdate = () => {
      console.log(form.value);
      fetchData1();
    };

    const fetchData2 = async () => {
      const res = await AddressControllerService.getAllAddressUsingGet();
      if (res.code === 0) {
        formAddress.value = res.data as any;
      } else {
        message.error("请求错误," + res.message);
      }
    };
    const router = useRouter();
    onMounted(() => {
      const loginUser = store.state.user.loginUser;
      if (!loginUser || loginUser.username === "未登录") {
        router.push("/user/login");
      }
      fetchData2();
      activeKey.value = localStorage.getItem("selectedActiveKey") as string;
    });

    let formAddress = ref([
      {
        id: 0,
        address: "",
        consignee: "",
        phoneNumber: "",
        isDefault: 0,
      } as Address,
    ]);
    let data = ref({
      id: 0,
      address: "",
      consignee: "",
      phoneNumber: "",
      isDefault: 0,
    } as Address);
    let data1 = ref({
      id: 0,
      address: "",
      consignee: "",
      phoneNumber: "",
      isDefault: 0,
    } as Address);
    const editAddress = (address: any) => {
      data.value = address;
      visible.value = true;
    };

    const deleteAddress = async (addressId: any) => {
      const res = await AddressControllerService.deleteAddressUsingPost(
        addressId
      );
      if (res.class === 0) {
        message.success("删除成功");
      } else {
        message.error("删除失败，" + res.message);
      }
    };

    const visible = ref(false);
    const visible1 = ref(false);

    const handleOk = async () => {
      console.log(data.value);
      data.value.isDefault = data.value.isDefault ? 1 : 0;
      const res = await AddressControllerService.updateAddressUsingPost(
        data.value
      );
      if (res.code === 0) {
        visible.value = false;
        message.success("修改成功");
        await fetchData2();
      } else {
        message.error("修改失败,", res.message);
      }
    };
    const handleOk1 = async () => {
      console.log(data1.value);
      data1.value.isDefault = data1.value.isDefault ? 1 : 0;
      const res = await AddressControllerService.addAddressUsingPost(
        data1.value
      );
      if (res.code === 0) {
        visible1.value = false;
        await fetchData2();
        message.success("添加成功");
      } else {
        message.error("添加失败，" + res.message);
      }
    };
    const handleCancel = () => {
      visible.value = false;
    };
    const handleCancel1 = () => {
      visible1.value = false;
    };
    const addAddress = () => {
      visible1.value = true;
    };
    const handleSubmitUpdate = async () => {
      if (!form1.isRead) {
        message.error("没有改好不能提交哦！");
      } else {
        const res = await UserControllerService.updatePasswordUsingPost(form1);
        if (res.code === 0) {
          location.reload();
          message.success("密码修改成功！");
        } else {
          message.error("密码修改失败！" + res.message);
        }
        console.log(form1);
      }
    };
    return {
      form,
      form1,
      handleUpdate,
      handleFileUpload,
      formAddress,
      editAddress,
      deleteAddress,
      visible,
      visible1,
      handleOk,
      handleOk1,
      handleCancel,
      handleCancel1,
      data,
      data1,
      addAddress,
      handleSubmitUpdate,
      changeTab,
      activeKey,
    };
  },
};
</script>
<style>
.arco-tabs-nav-vertical.arco-tabs-nav-type-line .arco-tabs-tab {
  padding: 20px !important;
}

#userLayout > section > main > div > div {
  margin-top: 50px;
}
</style>
