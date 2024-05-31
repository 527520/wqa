<template>
  <div class="home">
    <div class="search-bar">
      <a-input-search
        @search="search"
        @change="searchChange"
        placeholder="请输入搜索内容"
        style="width: 400px; padding: 5px"
      />
    </div>
  </div>
</template>

<script lang="ts" setup></script>
<style>
.search-bar {
  text-align: center;
}
</style>
