import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查权限(判断当前登录用户是否具有某个权限)
 * @param loginUser 当前登录用户
 * @param needAccess 需要的权限
 * @return boolean 有无权限
 */
const checkAccess = (loginUser: any, needAccess = ACCESS_ENUM.NOT_LOGIN) => {
  //获取当前用户具有的权限(如果没有loginUser，则表示未登录)
  const loginUserAccess = loginUser?.role ?? ACCESS_ENUM.NOT_LOGIN;
  console.log(loginUserAccess);
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }
  // 如果需要的权限是user
  if (needAccess === ACCESS_ENUM.USER) {
    // 如果用户没有登录 表示无权限
    if (loginUserAccess !== ACCESS_ENUM.USER) {
      return false;
    }
  }
  // 如果需要跑腿员权限
  if (needAccess === ACCESS_ENUM.DELIVERYMAN) {
    //如果不为跑腿员，表示无权限
    if (loginUserAccess !== ACCESS_ENUM.DELIVERYMAN) {
      return false;
    }
  }
  // 如果需要管理员权限
  if (needAccess === ACCESS_ENUM.ADMIN) {
    //如果不为管理员，表示无权限
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false;
    }
  }
  return true;
};
export default checkAccess;
