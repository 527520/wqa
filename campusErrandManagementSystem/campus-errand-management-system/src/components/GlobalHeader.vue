<template>
  <div>
    <a-row id="globalHeader" align="center" :wrap="false">
      <a-col flex="auto">
        <a-menu
          mode="horizontal"
          :selected-keys="selectedKeys"
          @menu-item-click="doMenuClick"
        >
          <a-menu-item
            key="0"
            :style="{ padding: 0, marginRight: '38px' }"
            disabled
          >
            <div class="title-bar"><!--此处放logo-->校园跑腿</div>
          </a-menu-item>
          <a-menu-item v-for="item in visibleRoutes" :key="item.path">
            {{ item.name }}
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <div>
          <a-dropdown trigger="hover">
            <a-button @click="goToProfile"
              >{{ store.state.user?.loginUser?.username ?? "未登录" }}
            </a-button>
            <template #content>
              <a-doption
                @click="logout"
                v-if="
                  (store.state.user?.loginUser?.role ??
                    ACCESS_ENUM.NOT_LOGIN) !== ACCESS_ENUM.NOT_LOGIN
                "
                >退出登录
              </a-doption>
            </template>
          </a-dropdown>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import { UserControllerService } from "../../generated";
import message from "@arco-design/web-vue/es/message";
import ACCESS_ENUM from "@/access/accessEnum";

const router = useRouter();
const store = useStore();

// 展示在菜单的路由数据组
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item?.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    return checkAccess(
      store.state.user.loginUser,
      item?.meta?.access as string
    );
  });
});

// 默认主页
const selectedKeys = ref(["/"]);

//路由跳转后，更新选中的菜单项
router.afterEach((to) => {
  selectedKeys.value = [to.path];
});
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
const logout = async () => {
  // 在这里执行退出登录的逻辑，例如清除用户登录状态等
  const res = await UserControllerService.userLogoutUsingPost();
  if (res.code === 0) {
    // 退出登录成功
    await store.dispatch("user/getLoginUser");
    await router.push({
      path: "/",
      replace: true,
    });
    console.log("退出登录");
    location.reload();
  } else {
    message.error("登录失败," + res.message);
  }
};
const goToProfile = () => {
  // 如果用户已登录，则跳转到用户中心页，否则跳转到登录页
  if (
    (store.state.user?.loginUser?.role ?? ACCESS_ENUM.NOT_LOGIN) ===
    ACCESS_ENUM.NOT_LOGIN
  ) {
    router.push("/user/login");
  } else {
    router.push("/user/userCenter");
  }
};
</script>

<style scoped></style>
