<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <a-space>
          <div>校园跑腿</div>
          <div>
            <a-button href="/" type="outline">回到首页</a-button>
          </div>
        </a-space>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="#" target="_blank">吴奇安的毕业设计项目：校园跑腿管理系统</a>
      </a-layout-footer>
    </a-layout>
  </div>
  <!--  <router-view />-->
</template>
<style>
#userLayout {
}

#userLayout .logo {
  height: 48px;
}

#userLayout .header {
  padding-left: 20px;
  padding-top: 14px;
  padding-bottom: 14px;
  //margin-bottom: 16px; box-shadow: #eee 1px 1px 5px;
}

#userLayout .content {
  margin-bottom: 16px;
}

#userLayout .footer {
  //background: #efefef; padding: 16px; position: sticky; bottom: 0; left: 0; right: 0; text-align: center;
}

#userLayout > section > header > div {
  position: relative;
  width: 100%;
}

#userLayout > section > header > div > div:nth-child(2) {
  position: absolute;
  right: 8px;
}
</style>
<script></script>
