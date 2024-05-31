<template>
  <a-radio-group @change="changeType" size="large" type="button">
    <a-radio value="1" :class="{ selected: flag === '1' }"
      >用户信息管理
    </a-radio>
    <a-radio value="2" :class="{ selected: flag === '2' }">配送员管理</a-radio>
    <a-radio value="3" :class="{ selected: flag === '3' }">配送员申请</a-radio>
  </a-radio-group>
  <div class="userInfoManagementDiv" v-if="flag === '1'">
    <div>
      <h2 class="userManagementH2">用户信息管理</h2>
      <a-space direction="vertical" size="large">
        <a-input-group>
          <a-input-search
            @search="search"
            @change="searchChange"
            :style="{ width: '320px' }"
            placeholder="搜索"
          />
          <a-select
            :style="{ width: '120px' }"
            v-model="selectValue"
            placeholder="请选择"
            allow-clear
          >
            <a-option>id</a-option>
            <a-option>用户名</a-option>
            <a-option>姓名</a-option>
            <a-option>手机号</a-option>
          </a-select>
        </a-input-group>
      </a-space>
      <a-table
        :pagination="false"
        stripe
        :columns="columns"
        :data="data.list"
        :filter-icon-align-left="alignLeft"
      >
        <template #optional="{ record }">
          <span>
            <a-button
              style="
                margin-right: 5px;
                background-color: #3498db;
                color: #ffffff;
              "
              @click="editUser(record)"
              >编辑</a-button
            >
            <a-button
              style="background-color: #e74c3c; color: #ffffff"
              @click="deleteUser(record.id)"
              >封号</a-button
            >
            <a-modal
              v-model:visible="visible2"
              @ok="handleOk()"
              @cancel="handleCancel2"
            >
              <template #title> 封号 </template>
              <div>确定封[ {{ record }} ]的号吗？</div>
            </a-modal>
          </span>
        </template>
        <template #createTime="{ record }">
          <span>{{ formatTime(record.createTime) }}</span>
        </template>
      </a-table>
      <a-pagination
        :total="data.total as number"
        :current="data.pageNum"
        :page-size="data.pageSize"
        @change="currentPageChange"
      />
    </div>
    <a-drawer
      :width="340"
      :height="380"
      :visible="visible"
      :placement="position"
      :footer="false"
      @cancel="handleCancel"
      unmountOnClose
    >
      <template #title>编辑用户信息</template>
      <div>
        <a-form :form="form" @submit="handleFinish">
          <a-form-item label="ID" :name="'id'">
            <a-input v-model="formData.id" disabled />
          </a-form-item>
          <a-form-item label="用户名" :name="'username'">
            <a-input v-model="formData.username" />
          </a-form-item>
          <a-form-item label="用户账号" :name="'userAccount'">
            <a-input v-model="formData.userAccount" />
          </a-form-item>
          <a-form-item label="手机号" :name="'phoneNumber'">
            <a-input v-model="formData.phoneNumber" />
          </a-form-item>
          <a-form-item label="角色" :name="'role'">
            <a-select v-model="formData.role">
              <a-option v-for="role in roles" :key="role" :value="role">
                {{ role }}
              </a-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-button html-type="submit">提交修改</a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-drawer>
  </div>
  <div class="deliverymanManagementDiv" v-if="flag === '2'">
    <h2 style="text-align: center">配送员管理</h2>
    <a-table
      :pagination="false"
      :columns="columns2"
      :data="data2"
      :scroll="scroll"
    >
      <template #optional="{ record }">
        <span>
          <a-button
            style="margin-right: 5px; background-color: #3498db; color: #ffffff"
            @click="editDelivery(record)"
            >编辑</a-button
          >
          <a-button
            style="background-color: #e74c3c; color: #ffffff"
            @click="deleteUser(record.id)"
            >删除</a-button
          >
          <a-link @click="goToDeliverymanPortraits(record.id)"
            >查看画像
          </a-link>
          <a-modal
            v-model:visible="visible2"
            @ok="handleOk1()"
            @cancel="handleCancel2"
          >
            <template #title> 删除 </template>
            <div>确定删除[ {{ record.name }} ]号吗？</div>
          </a-modal>
        </span>
      </template>
      <template #idCardFrontImage1="{ record }"
        ><img :src="record.idCardFrontImage" style="max-height: 80px" />
      </template>
      <template #idCardBackImage1="{ record }"
        ><img :src="record.idCardBackImage" style="max-height: 80px" />
      </template>
      <template #birthday="{ record }">
        <span>{{ formatTime1(record.birthday) }}</span>
      </template>
      <template #createTime="{ record }">
        <span>{{ formatTime(record.createTime) }}</span>
      </template>
      <template #updateTime="{ record }">
        <span>{{ formatTime(record.updateTime) }}</span>
      </template>
    </a-table>
    <a-drawer
      :width="340"
      :height="420"
      :visible="visible3"
      :placement="position"
      :footer="false"
      @cancel="handleCancel3"
      unmountOnClose
    >
      <template #title>修改跑腿员信息</template>
      <div>
        <a-form :form="form" @submit="handleFinish2">
          <a-form-item label="id" :name="'id'">
            <a-input v-model="formData2.id" disabled />
          </a-form-item>
          <a-form-item label="姓名" :name="'name'">
            <a-input v-model="formData2.name" />
          </a-form-item>
          <a-form-item field="phoneNumber" label="电话号码">
            <a-input v-model="formData2.phoneNumber" />
          </a-form-item>
          <a-form-item label="身份证号(谨慎修改)" :name="'idNumber'">
            <a-input v-model="formData2.idNumber" />
          </a-form-item>
          <a-form-item label="生日" :name="'birthday'">
            <a-date-picker v-model="formData2.birthday" />
          </a-form-item>
          <a-form-item label="状态" :name="'status'">
            <a-select v-model="formData2.status">
              <a-option
                v-for="status in statusList1"
                :key="status"
                :value="status"
              >
                {{ status }}
              </a-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-button html-type="submit">提交修改</a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-drawer>
  </div>
  <div class="deliverymanBecomeDiv" v-if="flag === '3'">
    <div v-if="Object.keys(data1).length > 0">
      <h2 style="text-align: center">配送员申请管理</h2>
      <a-table
        :pagination="false"
        :columns="columns1"
        :data="data1"
        :scroll="scroll"
      >
        <template #optional="{ record }">
          <span>
            <a-button
              style="
                margin-right: 5px;
                background-color: #3498db;
                color: #ffffff;
              "
              @click="editBecome(record)"
              >编辑</a-button
            >
          </span>
        </template>
        <template #idCardFrontImage="{ record }"
          ><img :src="record.idCardFrontImage" style="max-height: 80px" />
        </template>
        <template #idCardBackImage="{ record }"
          ><img :src="record.idCardBackImage" style="max-height: 80px" />
        </template>
        <template #createTime="{ record }">
          <span>{{ formatTime(record.createTime) }}</span>
        </template>
        <template #birthday="{ record }">
          <span>{{ formatTime1(record.birthday) }}</span>
        </template>
      </a-table>
    </div>
    <div v-else>
      <p>暂无配送员申请数据</p>
    </div>
    <a-drawer
      :width="340"
      :height="380"
      :visible="visible1"
      :placement="position"
      :footer="false"
      @cancel="handleCancel1"
      unmountOnClose
    >
      <template #title>跑腿员申请表审核</template>
      <div>
        <a-form :form="form" @submit="handleFinish1">
          <a-form-item label="id" :name="'id'">
            <a-input v-model="formData1.id" disabled />
          </a-form-item>
          <a-form-item label="姓名" :name="'name'">
            <a-input v-model="formData1.name" disabled />
          </a-form-item>
          <a-form-item label="消息反馈(如未通过)" :name="'message'">
            <a-input v-model="formData1.message" />
          </a-form-item>
          <a-form-item label="状态" :name="'status'">
            <a-select v-model="formData1.status">
              <a-option
                v-for="status in statusList"
                :key="status"
                :value="status"
              >
                {{ status }}
              </a-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-button html-type="submit">提交修改</a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-drawer>
  </div>
</template>

<script lang="ts">
import { onMounted, reactive, ref } from "vue";
import {
  BecomeDeliveryman,
  BecomeDeliverymanControllerService,
  DeleteRequest,
  Deliveryman,
  DeliverymanControllerService,
  PageInfo_UserInfo_,
  UserControllerService,
  UserInfo,
  UserSearchRequest,
  UserUpdateRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import ACCESS_ENUM from "@/access/accessEnum";
import STATUS_ENUM from "@/access/statusEnum";
import { useRouter } from "vue-router";

export default {
  setup() {
    const columns = [
      {
        title: "ID",
        dataIndex: "id",
      },
      {
        title: "姓名",
        dataIndex: "username",
      },
      {
        title: "用户账号",
        dataIndex: "userAccount",
      },
      {
        title: "手机号",
        dataIndex: "phoneNumber",
      },
      {
        title: "角色",
        dataIndex: "role",
      },
      {
        title: "创建时间",
        dataIndex: "createTime",
        sortable: {
          sortDirections: ["ascend", "descend"],
        },
        slots: { customRender: "createTime" },
        slotName: "createTime",
      },
      {
        title: "操作",
        slots: { customRender: "operation" },
        slotName: "optional",
      },
    ];
    let data: PageInfo_UserInfo_ = reactive({
      list: reactive([{} as UserInfo] as Array<UserInfo>),
      endRow: 0,
      hasNextPage: true,
      hasPreviousPage: true,
      isFirstPage: true,
      isLastPage: true,
      navigateFirstPage: 0,
      navigateLastPage: 0,
      navigatePages: 0,
      navigatepageNums: [],
      nextPage: 0,
      pageNum: 0,
      pageSize: 0,
      pages: 0,
      prePage: 0,
      size: 0,
      startRow: 0,
      total: 0,
    } as PageInfo_UserInfo_);

    const columns1 = [
      {
        title: "ID",
        dataIndex: "id",
        fixed: "left",
        width: 200,
      },
      {
        title: "用户id",
        dataIndex: "userId",
      },
      {
        title: "身份证号",
        dataIndex: "idNumber",
      },
      {
        title: "手机号码",
        dataIndex: "phoneNumber",
      },
      {
        title: "姓名",
        dataIndex: "name",
      },
      {
        title: "性别",
        dataIndex: "sex",
      },
      {
        title: "生日",
        dataIndex: "birthday",
        slots: { customRender: "birthday" },
        slotName: "birthday",
      },
      {
        title: "身份证正面照",
        dataIndex: "idCardFrontImage",
        slots: { customRender: "idCardFrontImage" },
        slotName: "idCardFrontImage",
      },
      {
        title: "身份证反面照",
        dataIndex: "idCardBackImage",
        slots: { customRender: "idCardBackImage" },
        slotName: "idCardBackImage",
      },
      {
        title: "提交时间",
        dataIndex: "createTime",
        slots: { customRender: "createTime" },
        slotName: "createTime",
      },
      {
        title: "状态",
        dataIndex: "status",
      },
      {
        title: "反馈(如不通过)",
        dataIndex: "message",
      },
      {
        title: "操作",
        slots: { customRender: "operation" },
        slotName: "optional",
        fixed: "right",
        width: 200,
      },
    ];
    const columns2 = [
      {
        title: "ID",
        dataIndex: "id",
        fixed: "left",
        width: 200,
      },
      {
        title: "用户id",
        dataIndex: "userId",
      },
      {
        title: "身份证号",
        dataIndex: "idNumber",
      },
      {
        title: "手机号码",
        dataIndex: "phoneNumber",
      },
      {
        title: "姓名",
        dataIndex: "name",
      },
      {
        title: "性别",
        dataIndex: "sex",
      },
      {
        title: "生日",
        dataIndex: "birthday",
        slots: { customRender: "birthday" },
        slotName: "birthday",
      },
      {
        title: "身份证正面照",
        dataIndex: "idCardFrontImage",
        slots: { customRender: "idCardFrontImage1" },
        slotName: "idCardFrontImage1",
      },
      {
        title: "身份证反面照",
        dataIndex: "idCardBackImage",
        slots: { customRender: "idCardBackImage1" },
        slotName: "idCardBackImage1",
      },
      {
        title: "是否在线",
        dataIndex: "isOnline",
      },
      {
        title: "可接单状态状态",
        dataIndex: "acceptingOrders",
      },
      {
        title: "账号状态",
        dataIndex: "status",
      },
      {
        title: "完成订单数",
        dataIndex: "completedOrders",
      },
      {
        title: "平均评分",
        dataIndex: "averageRating",
      },
      {
        title: "创建时间",
        dataIndex: "createTime",
        slots: { customRender: "createTime" },
        slotName: "createTime",
      },
      {
        title: "更新时间",
        dataIndex: "updateTime",
        slots: { customRender: "updateTime" },
        slotName: "updateTime",
      },
      {
        title: "操作",
        slots: { customRender: "operation" },
        slotName: "optional",
        fixed: "right",
        width: 220,
      },
    ];
    let data1 = ref([
      {
        birthday: "",
        createTime: "",
        id: 0,
        idCardBackImage: "",
        idCardFrontImage: "",
        idNumber: "",
        phoneNumber: "",
        isdelete: 0,
        message: "",
        name: "",
        sex: "",
        status: "",
        updateTime: "",
        userId: 0,
      } as BecomeDeliveryman,
    ]);
    let data2 = ref([
      {
        id: 0,
        userId: 0,
        idCardBackImage: "",
        idCardFrontImage: "",
        idNumber: "",
        phoneNumber: "",
        name: "",
        sex: "",
        birthday: "",
        status: "",
        averageRating: 0,
        completedOrders: 0,
        acceptingOrders: "",
        isOnline: "",
        createTime: "",
        updateTime: "",
      } as Deliveryman,
    ]);
    let current = 1;

    const fetchData = async () => {
      const res = await UserControllerService.listUserByPageUsingPost({
        current,
      });
      console.log(res.data);
      if (res.code === 0) {
        // 更新 data 的属性值，而不是重新分配整个对象
        Object.keys(res.data).forEach((key) => {
          (data as any)[key] = (res.data as any)[key];
        });
      } else {
        message.error("请求失败," + res.message);
      }
    };
    const fetchData1 = async () => {
      const res = await BecomeDeliverymanControllerService.getAllUsingGet();
      if (res.code === 0) {
        data1.value = res.data as any;
      } else {
        message.error("请求失败," + res.message);
      }
      console.log(data1);
    };
    const fetchData2 = async () => {
      const res = await DeliverymanControllerService.getAllUsingPost();
      if (res.code === 0) {
        data2.value = res.data as any;
      } else {
        message.error("请求失败," + res.message);
      }
      console.log(data2);
    };
    onMounted(() => {
      searchFlag = false; // 刷新页面重置搜索标志
      const storedFlag = localStorage.getItem("selectedFlag");
      if (storedFlag) {
        flag.value = storedFlag;
      } else {
        flag.value = "1"; // 默认为第一个选项
      }
      if (flag.value === "1") {
        fetchData();
      }
      if (flag.value === "2") {
        fetchData2();
      }
      if (flag.value === "3") {
        fetchData1();
      }
    });

    const currentPageChange = (page: number) => {
      // 如果选择的是下一页，将当前页数加一，否则直接使用选择的页数
      current = page === (data.pageNum as number) + 1 ? current + 1 : page;
      // 重新向后端请求数据
      if (searchFlag) {
        // 走搜索
        fetchData3(searchValue.value, selectValue.value);
      } else {
        fetchData();
      }
    };
    const visible = ref(false);
    const visible1 = ref(false);
    const visible2 = ref(false);
    const visible3 = ref(false);
    const position = ref("top");
    const form = ref<any>(null);
    let formData: UserInfo = reactive({} as UserInfo);
    let formData1: BecomeDeliveryman = reactive({} as BecomeDeliveryman);
    let formData2: Deliveryman = reactive({} as Deliveryman);
    const roles = [
      ACCESS_ENUM.ADMIN,
      ACCESS_ENUM.USER,
      ACCESS_ENUM.DELIVERYMAN,
    ];
    const statusList = [
      STATUS_ENUM.SUBMITTED,
      STATUS_ENUM.UNDER_REVIEW,
      STATUS_ENUM.FAIL,
      STATUS_ENUM.PASS,
    ];
    const statusList1 = ["正常", "暂停接单", "离职"];
    const editUser = (record: any) => {
      visible.value = true;
      formData.id = record.id;
      formData.username = record.username;
      formData.userAccount = record.userAccount;
      formData.phoneNumber = record.phoneNumber;
      formData.role = record.role;
      // 将记录数据拷贝到formData中
      console.log(formData);
    };
    const editBecome = (record: any) => {
      visible1.value = true;
      formData1.id = record.id;
      formData1.name = record.name;
      formData1.status = record.status;
      formData1.message = record.message;
      // 将记录数据拷贝到formData中
      console.log(formData1);
    };
    const editDelivery = (record: any) => {
      visible3.value = true;
      formData2.id = record.id;
      formData2.idNumber = record.idNumber;
      formData2.phoneNumber = record.phoneNumber;
      formData2.name = record.name;
      formData2.birthday = record.birthday;
      formData2.status = record.status;
      console.log(formData2);
    };

    let deleteId = 0;

    const handleOk = async () => {
      visible2.value = false;
      const deleteForm = {
        id: deleteId,
      } as DeleteRequest;
      const res = await UserControllerService.deleteUserUsingPost(deleteForm);
      if (res === 0) {
        await fetchData();
        message.success("封号成功");
      } else {
        message.error("操作失败，", res.message);
      }
    };
    const handleOk1 = async () => {
      const deleteForm = {
        id: deleteId,
      } as DeleteRequest;
      const res = await DeliverymanControllerService.deleteByIdUsingPost(
        deleteForm
      );
      if (res === 0) {
        await fetchData2();
        message.success("封号成功");
      } else {
        message.error("操作失败，", res.message);
      }
      visible2.value = false;
    };

    const deleteUser = async (id: number) => {
      console.log(id);
      deleteId = id;
      visible2.value = true;
    };

    const handleFinish = async () => {
      console.log("Form values:", formData);
      // 在这里可以处理提交逻辑，如将数据发送到后端
      const res = await UserControllerService.updateUserUsingPost(
        formData as UserUpdateRequest
      );
      if (res.code === 0) {
        visible.value = false;
        await fetchData();
      } else {
        message.error("修改错误，" + res.message);
      }
    };
    const handleFinish1 = async () => {
      console.log("Form values:", formData1);
      // 在这里可以处理提交逻辑，如将数据发送到后端
      const res =
        await BecomeDeliverymanControllerService.updateStatusUsingPost(
          formData1
        );
      if (res.code === 0) {
        visible1.value = false;
        await fetchData1();
      } else {
        message.error("修改错误，" + res.message);
      }
    };

    const handleFinish2 = async () => {
      console.log("Form values:", formData2);
      const res = await DeliverymanControllerService.updateByIdUsingPost(
        formData2
      );
      if (res.code === 0) {
        visible3.value = false;
        await fetchData2();
      } else {
        message.error("修改错误，" + res.message);
      }
    };

    const handleCancel = () => {
      visible.value = false;
    };
    const handleCancel1 = () => {
      visible1.value = false;
    };
    const handleCancel2 = () => {
      visible2.value = false;
    };
    const handleCancel3 = () => {
      visible3.value = false;
    };

    let flag = ref("");

    const changeType = async (value: string, event: Event) => {
      flag.value = value;
      // 将当前选项存储到本地存储中
      localStorage.setItem("selectedFlag", value);
      if (flag.value === "1") {
        if (searchFlag) {
          //走搜索
          await fetchData3(searchValue.value, selectValue.value);
        } else {
          await fetchData();
        }
      }
      if (flag.value === "2") {
        await fetchData2();
      }
      if (value === "3") {
        await fetchData1();
      }
    };

    const scroll = {
      x: 2300,
      y: 1000,
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

    const formatTime1 = (timestamp: string) => {
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");

      return `${year}-${month}-${day}`;
    };

    const selectValue = ref("");
    const searchValue = ref("");

    // 都用分页
    const fetchData3 = async (value: string, selectValue: string) => {
      const data1 = {
        field: selectValue,
        fieldValue: value,
        current: current,
      } as UserSearchRequest;
      const res = await UserControllerService.searchUserUsingPost(data1);
      if (res.code === 0) {
        Object.keys(res.data).forEach((key) => {
          (data as any)[key] = (res.data as any)[key];
        });
      } else {
        message.error("查询错误，", res.message);
      }
    };

    let searchFlag = false; // 搜索标志
    const search = async (value: string) => {
      if (value && value.trim() && selectValue.value) {
        searchFlag = true;
        searchValue.value = value;
        await fetchData3(value, selectValue.value);
      } else if (!value.trim() && !selectValue.value) {
        searchFlag = false;
        // 两个都空，搜全部
        await fetchData();
      }
    };
    const searchChange = async (value: string) => {
      if (value && value.trim() && selectValue.value) {
        searchFlag = true;
        searchValue.value = value;
        await fetchData3(value, selectValue.value);
      } else if (!value.trim() && !selectValue.value) {
        searchFlag = false;
        await fetchData();
      }
    };

    const router = useRouter();

    const goToDeliverymanPortraits = (deliverymanId: any) => {
      router.push({
        path: "/deliverymanPortraits",
        query: {
          deliverymanId: deliverymanId,
        },
      });
    };

    return {
      columns,
      data,
      currentPageChange,
      editUser,
      deleteUser,
      editBecome,
      visible,
      visible1,
      visible2,
      visible3,
      position,
      handleCancel,
      handleCancel1,
      handleCancel2,
      handleCancel3,
      handleOk,
      handleOk1,
      form,
      formData,
      formData1,
      formData2,
      handleFinish,
      handleFinish1,
      handleFinish2,
      roles,
      statusList,
      statusList1,
      flag,
      changeType,
      columns1,
      data1,
      columns2,
      data2,
      scroll,
      editDelivery,
      alignLeft,
      formatTime,
      formatTime1,
      selectValue,
      search,
      searchChange,
      goToDeliverymanPortraits,
    };
  },
};
</script>

<style scoped>
.userManagementH2 {
  text-align: center;
}

#basicLayout
  > section
  > main
  > div
  > div.arco-table.arco-table-size-large.arco-table-border.arco-table-stripe.arco-table-hover
  > div
  > div
  > div
  > div
  > table
  > tbody
  > tr:nth-child(1)
  > td:nth-child(7)
  > span
  > span {
  text-align: center;
}

#basicLayout > section > main > div > div > div.arco-space.arco-space-vertical {
  margin-bottom: 16px;
  margin-left: 60%;
}

.selected {
  background-color: #fff;
  color: rgb(var(--primary-6));
}
</style>
