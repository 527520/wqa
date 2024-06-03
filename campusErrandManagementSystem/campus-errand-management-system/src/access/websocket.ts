import { LoginUserVO } from "../../generated";
import { onMounted } from "vue";
import store from "@/store";
import message from "@arco-design/web-vue/es/message";

let loginUser = store.state.user.loginUser;
if (!loginUser || !loginUser.role) {
  // 加await是为了等用户登录之后在执行后续的代码
  await store.dispatch("user/getLoginUser");
  loginUser = store.state.user.loginUser;
}
export let socket: WebSocket;

export const connectWebSocket = () => {
  socket = new WebSocket(
    "ws://localhost:8101/api/websocket?userId=" + loginUser.id
  );
  socket.onopen = () => {
    console.log("WebSocket 连接。");
    // sendMessage(
    //   JSON.stringify({
    //     to: loginUser.id,
    //     form: loginUser.id,
    //     messageText: "测试",
    //     topic: "/user/" + loginUser.id + "/topic/messages",
    //   })
    // );
  };
  socket.onmessage = (event) => {
    console.log("接收到消息：" + event.data);
    const messageText = JSON.parse(event.data);
    alert(messageText.messageText);
  };
};

const sendMessage = (messageJson: any) => {
  socket.send(messageJson);
};

export default sendMessage;
