<template>
  <div class="chat-container">
    <div class="messages" ref="messagesContainer">
      <div
        v-for="(message, index) in messages"
        :key="index"
        :class="['message', message.isMine ? 'mine' : 'theirs']"
      >
        <div class="messageBody">
          <img :src="message.avatar" class="avatar" alt="头像" />
          <div class="text-bubble">
            {{ message.text }}
          </div>
        </div>
      </div>
    </div>
    <div class="input-container">
      <a-space direction="vertical" size="large" class="messageInput">
        <a-mention
          @keyup.enter="send"
          v-model="newMessage"
          placeholder="请输入消息..."
        />
      </a-space>
      <a-button @click="send" type="primary" shape="round" class="messageButton"
        >发送
      </a-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, nextTick, watch, onMounted } from "vue";
import store from "@/store";
import { LoginUserVO } from "../../generated";
import sendMessage, { socket } from "@/access/websocket";
import { useRoute } from "vue-router";

interface Message {
  text: string;
  isMine: boolean;
  avatar: string;
}

const newMessage = ref("");
const messages = reactive<Message[]>([]);

const messagesContainer = ref<HTMLElement | null>(null);

let loginUser: LoginUserVO;

const send = () => {
  if (newMessage.value.trim() !== "") {
    messages.push({
      text: newMessage.value,
      isMine: true,
      avatar: loginUser.userAvatar as string,
    });
    sendMessage(
      JSON.stringify({
        to: toUserId,
        form: loginUser.id,
        messageText: newMessage.value,
        counterpartAvatar: loginUser.userAvatar,
        toAvatar: toAvatar,
        topic: "/user/" + loginUser.id + "/topic/messages",
      })
    );
    newMessage.value = "";
  }
};

let toAvatar = "";

socket.onmessage = (event) => {
  const messageText = JSON.parse(event.data);
  toAvatar = messageText.counterpartAvatar;
  messages.push({
    text: messageText.messageText,
    isMine: false,
    avatar: messageText.counterpartAvatar,
  });
};

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};
const route = useRoute();
let toUserId: number;

onMounted(async () => {
  loginUser = store.state.user.loginUser;
  if (!loginUser || !loginUser.role) {
    // 加await是为了等用户登录之后在执行后续的代码
    await store.dispatch("user/getLoginUser");
    loginUser = store.state.user.loginUser;
  }
  toUserId = route.query.toUserId as any;
  scrollToBottom();
});

watch(messages, () => {
  nextTick(() => {
    scrollToBottom();
  });
});
</script>

<style>
.chat-container {
  display: flex;
  flex-direction: column;
  max-width: 100%;
  margin: auto;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.messages {
  height: calc(100vh - 222px);
  overflow-y: auto;
}

.message {
  display: flex;
  align-items: center;
  flex-grow: 1;
  overflow-y: auto;
  padding: 10px;
  word-wrap: break-word;
}

.messageBody {
  display: flex;
  max-width: 70%;
}

.mine {
  flex-direction: row-reverse;
}

.theirs {
  flex-direction: row;
}

.mine .messageBody {
  justify-content: flex-end;
}

.theirs .messageBody {
  justify-content: flex-start;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px;
}

.mine .avatar {
  order: 2;
}

.text-bubble {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 20px;
  font-size: 14px;
  line-height: 1.5;
}

.mine .text-bubble {
  background-color: #daf8e3;
  color: #333;
  order: 1;
}

.theirs .text-bubble {
  background-color: #e8eaf6;
  color: #333;
}

.input-container {
  display: flex;
  padding: 10px;
  border-top: 1px solid #ddd;
  background-color: white;
}

.messageInput {
  flex-grow: 1;
  margin-right: 10px;
}

#basicLayout > section > main {
  margin-bottom: 0 !important;
}
</style>
