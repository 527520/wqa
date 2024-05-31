import ACCESS_ENUM from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";
import router from "@/router";
import store from "@/store";

router.beforeEach(async (to, from, next) => {
  let loginUser = store.state.user.loginUser;
  if (!loginUser || !loginUser.role) {
    // 加await是为了等用户登录之后在执行后续的代码
    await store.dispatch("user/getLoginUser");
    loginUser = store.state.user.loginUser;
  }
  //需要的权限
  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NOT_LOGIN;

  // todo 如果已经登录还要去登录页，不让去

  if (needAccess !== "hideInMenu" && needAccess !== ACCESS_ENUM.NOT_LOGIN) {
    //如果没登录，跳转到登录页面
    if (
      !loginUser ||
      !loginUser.role ||
      loginUser.role === ACCESS_ENUM.NOT_LOGIN
    ) {
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    if (!checkAccess(loginUser, needAccess)) {
      next("/noAuth");
      return;
    }
  }
  next();
});
