import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import ACCESS_ENUM from "@/access/accessEnum";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";
import DeliveryView from "@/views/deliveryman/DeliVeryView.vue";
import NoAuthView from "@/views/NoAuthView.vue";
import UserLayout from "@/layouts/UserLayout.vue";
import UserCenterView from "@/views/user/UserCenterView.vue";
import AdminView from "@/views/admin/AdminView.vue";
import UserManagementView from "@/views/admin/UserManagementView.vue";
import BecomeDeliveryManView from "@/views/user/BecomeDeliveryManView.vue";
import PublishTaskView from "@/views/user/PublishTaskView.vue";
import UserOrderManagementView from "@/views/user/UserOrderManagementView.vue";
import TakeOrderView from "@/views/deliveryman/TakeOrderView.vue";
import OrderManagementView from "@/views/admin/OrderManagementView.vue";
import OrderDetailView from "@/views/user/OrderDetailView.vue";
import DeliveryOrderView from "@/views/deliveryman/DeliveryOrderView.vue";
import HistoryDeliveryOrderView from "@/views/deliveryman/HistoryDeliveryOrderView.vue";
import AnnounceView from "@/views/admin/AnnounceView.vue";
import AnnouncementView from "@/views/user/AnnouncementView.vue";
import EvaluationView from "@/views/user/EvaluationView.vue";
import MyReviewView from "@/views/user/MyReviewView.vue";
import ViewReviewsView from "@/views/deliveryman/ViewReviewsView.vue";
import ReviewsManagementView from "@/views/admin/ReviewsManagementView.vue";
import RecruitManagementView from "@/views/admin/RecruitManagementView.vue";
import RecruitInfoView from "@/views/user/RecruitInfoView.vue";
import DeliverymanPortraits from "@/views/deliveryman/DeliverymanPortraits.vue";
import ChatView from "@/views/ChatView.vue";
import MyMessageView from "@/views/MyMessageView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "首页",
    component: HomeView,
  },
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: UserLoginView,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: UserRegisterView,
      },
      {
        path: "/user/userCenter",
        name: "用户中心",
        component: UserCenterView,
      },
    ],
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/announcement",
    name: "系统公告",
    component: AnnouncementView,
  },
  {
    path: "/recruitInfo",
    name: "招聘信息",
    component: RecruitInfoView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/publishInfo",
    name: "发布信息",
    component: PublishTaskView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/userOrderManagement",
    name: "用户订单",
    component: UserOrderManagementView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/becomeDeliveryMan",
    name: "成为配送员",
    component: BecomeDeliveryManView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },

  {
    path: "/takeOrder",
    name: "接取订单",
    component: TakeOrderView,
    meta: {
      access: ACCESS_ENUM.DELIVERYMAN,
    },
  },
  {
    path: "/dOManagement",
    name: "配送订单",
    component: DeliveryOrderView,
    meta: {
      access: ACCESS_ENUM.DELIVERYMAN,
    },
  },
  {
    path: "/myTakeOrder",
    name: "历史订单",
    component: HistoryDeliveryOrderView,
    meta: {
      access: ACCESS_ENUM.DELIVERYMAN,
    },
  },
  {
    path: "/orderDetail",
    name: "订单详情",
    component: OrderDetailView,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/reviews",
    name: "评价",
    component: EvaluationView,
    meta: {
      hideInMenu: true,
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/viewReviews",
    name: "查看评价",
    component: ViewReviewsView,
    meta: {
      access: ACCESS_ENUM.DELIVERYMAN,
    },
  },
  {
    path: "/myReviews",
    name: "我的评价",
    component: MyReviewView,
    meta: {
      access: ACCESS_ENUM.USER,
    },
  },
  {
    path: "/deliverymanPortraits",
    name: "跑腿员画像",
    component: DeliverymanPortraits,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/announce",
    name: "发布公告",
    component: AnnounceView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/userManagement",
    name: "用户信息管理",
    component: UserManagementView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/OrderManagement",
    name: "订单管理",
    component: OrderManagementView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/reviewsManagement",
    name: "评价信息管理",
    component: ReviewsManagementView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/recruitInfoManagement",
    name: "招聘信息管理",
    component: RecruitManagementView,
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: "/chat",
    name: "聊天",
    component: ChatView,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/myMessage",
    name: "我的消息",
    component: MyMessageView,
  },
  // {
  //   path: "/onlineMessage",
  //   name: "在线留言",
  //   component: HomeView,
  //   meta: {
  //     access: ACCESS_ENUM.USER,
  //   },
  // },
  {
    path: "/about",
    name: "about",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
  {
    path: "/noAuth",
    name: "没有权限",
    component: NoAuthView,
    meta: {
      hideInMenu: true,
    },
  },
];
