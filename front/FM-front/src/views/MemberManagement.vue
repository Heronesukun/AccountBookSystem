<template>
  <div class="member-management-view">
    <el-card class="header-card">
      <div class="header-content">
        <h2>成员管理</h2>
        <el-button @click="goBack">返回账本</el-button>
      </div>
    </el-card>
    
    <MemberManagementPage :bookId="bookId" />
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import MemberManagementPage from '@/components/pages/MemberManagementPage.vue';

const route = useRoute();
const router = useRouter();
const bookId = route.params.bookId;

const goBack = () => {
  router.push(`/notebook/${localStorage.getItem('username')}/${bookId}`);
};
</script>

<style scoped>
.member-management-view {
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