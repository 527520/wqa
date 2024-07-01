<template>
  <div class="chat-list">
    <div
      class="chat-item"
      v-for="chat in messageList"
      :key="chat.messageVO.id"
      @click="
        goChat(
          (chat.messageVO.formUserId !== loginUser.id
            ? chat.messageVO.formUserId
            : chat.messageVO.toUserId) as number
        )
      "
    >
      <a-badge :max-count="100" :count="chat.unReadMessageNum">
        <img :src="chat.messageVO.formAvatar" alt="avatar" class="avatar" />
      </a-badge>
      <div class="chat-info">
        <div class="chat-name">{{ chat.messageVO.counterpartName }}</div>
        <div class="chat-last-message">{{ chat.messageVO.content }}</div>
      </div>
      <div class="chat-time">{{ chat.messageVO.time }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { MessageControllerService } from "../../generated/services/MessageControllerService";
import message from "@arco-design/web-vue/es/message";
import store from "@/store";
import { useRouter } from "vue-router";

let messageList = ref([
  {
    messageVO: {
      content: "内容",
      counterpartName: "对方名字",
      formAvatar: "发送者头像",
      formUserId: "发送者id",
      id: "消息id",
      isRead: "是否已读",
      time: "时间",
      toAvatar: "接受者头像",
      toUserId: "接受者id",
    },
    unReadMessageNum: 0,
  },
]);

const fetchData = async () => {
  const res = await MessageControllerService.getChatListUsingGet();
  console.log(res.data);
  if (res.code === 0) {
    messageList.value = res.data as any;
    console.log(messageList.value);
  } else {
    message.error("获取消息列表发送错误，" + res.message);
  }
};
onMounted(() => {
  fetchData();
});
const loginUser = store.state.user.loginUser;
const router = useRouter();
const goChat = (userId: number) => {
  router.push({
    path: "/chat",
    query: {
      toUserId: userId,
    },
  });
};
</script>

<style scoped>
.chat-list {
  width: 100%;
  margin: 0 auto;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
  transition: background-color 0.3s;
}

.chat-item:hover {
  background-color: #f5f5f5;
  cursor: pointer;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.chat-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-left: 20px;
}

.chat-name {
  font-weight: bold;
}

.chat-last-message {
  color: #888;
  font-size: 14px;
}

.chat-time {
  font-size: 12px;
  color: #aaa;
  margin-left: 10px;
}
</style>
