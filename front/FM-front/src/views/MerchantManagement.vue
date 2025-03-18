<template>
  <div class="merchant-management-view">
    <el-card class="header-card">
      <div class="header-content">
        <h2>商家管理</h2>
        <el-button @click="goBack">返回账本</el-button>
      </div>
    </el-card>
    
    <MerchantManagementPage />
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import MerchantManagementPage from '@/components/pages/MerchantManagementPage.vue';

const route = useRoute();
const router = useRouter();
const bookId = route.params.bookId;

// 确保将账本ID存储到localStorage，以便组件可以访问
if (bookId) {
  localStorage.setItem('currentBookId', bookId);
}

const goBack = () => {
  router.push(`/notebook/${localStorage.getItem('username')}/${bookId}`);
};
</script>

<style scoped>
.merchant-management-view {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>