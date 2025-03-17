<template>
  <div class="ledger-detail-container">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header>
        <div class="top-nav">
          <div class="left-nav">
            <el-button @click="goToHome">Home</el-button>
          </div>
          <div class="center-nav">
            <h1>{{ bookName }}</h1>
          </div>
          <div class="right-nav">
            <el-button @click="goToMemberManagement">成员管理</el-button>
          </div>
        </div>
      </el-header>

      <!-- 主要内容区域 -->
      <el-container class="main-content">
        <!-- 侧边栏 -->
        <el-aside width="200px">
          <Sidebar :activePage="activePage" @change-page="changePage" @open-record-drawer="openRecordDrawer" />
        </el-aside>

        <!-- 内容显示区域 -->
        <el-main class="content-area">
          <component :is="currentComponent" :bookId="bookId" ref="currentComponent"></component>
        </el-main>
      </el-container>

      <!-- 记账抽屉 -->
      <RecordDrawer
          v-if="showRecordDrawer"
          @close="closeRecordDrawer"
          @save="saveRecord"
          @save-and-continue="saveAndContinue"
          :bookId="bookId"
      />
    </el-container>
  </div>
</template>

<script>
import Sidebar from '../components/Sidebar.vue';
import RecordDrawer from '../components/RecordDrawer.vue';
import HomePage from '../components/pages/HomePage.vue';
import TransactionsPage from '../components/pages/TransactionsPage.vue';
import ReportsPage from '../components/pages/ReportsPage.vue';
import CategoryManagementPage from '../components/pages/CategoryManagementPage.vue';
import AccountManagementPage from '../components/pages/AccountManagementPage.vue';
import MemberManagementPage from '../components/pages/MemberManagementPage.vue';
import MerchantManagementPage from '../components/pages/MerchantManagementPage.vue';
import ProjectManagementPage from '../components/pages/ProjectManagementPage.vue';
import SettingsPage from '../components/pages/SettingsPage.vue';

import mainPageBookRequest from '@/api/mainPageBook.js';

export default {
  name: 'LedgerDetail',
  components: {
    Sidebar,
    RecordDrawer,
    HomePage,
    TransactionsPage,
    ReportsPage,
    CategoryManagementPage,
    AccountManagementPage,
    MemberManagementPage,
    MerchantManagementPage,
    ProjectManagementPage,
    SettingsPage
  },
  data() {
    return {
      bookId: this.$route.params.id || this.$route.query.bookId,
      bookName: '我的账本', // 初始值，会被API返回的数据覆盖
      activePage: 'home',
      showRecordDrawer: false,
      bookMaster: localStorage.getItem('username') || '',
      // 页面组件映射
      pageComponents: {
        home: HomePage,
        transactions: TransactionsPage,
        reports: ReportsPage,
        categories: CategoryManagementPage,
        accounts: AccountManagementPage,
        members: MemberManagementPage,
        merchants: MerchantManagementPage,
        projects: ProjectManagementPage,
        settings: SettingsPage
      }
    };
  },
  computed: {
    currentComponent() {
      return this.pageComponents[this.activePage];
    }
  },
  mounted() {
    // 获取账本信息
    this.fetchBookDetails();
  },
  methods: {
    goToHome() {
      this.$router.push({ path: '/home' });
    },
    goToMemberManagement() {
      this.activePage = 'members';
    },
    changePage(page) {
      this.activePage = page;
    },
    openRecordDrawer() {
      this.showRecordDrawer = true;
    },
    closeRecordDrawer() {
      this.showRecordDrawer = false;
    },
    saveRecord(recordData) {
      // 保存记录的逻辑，后续连接API
      console.log('Saving record:', recordData);
      // 模拟API调用
      setTimeout(() => {
        this.closeRecordDrawer();
        // 刷新数据
        this.refreshData();
      }, 500);
    },
    saveAndContinue(recordData) {
      // 保存并继续记账
      console.log('Saving record and continuing:', recordData);
      // 模拟API调用
      setTimeout(() => {
        // 不关闭抽屉，但重置抽屉内表单
        this.$refs.recordDrawer && this.$refs.recordDrawer.resetForm();
        // 刷新数据
        this.refreshData();
      }, 500);
    },
    fetchBookDetails() {
      // 获取账本详情
      // 这里使用现有的API，后续根据实际情况调整
      mainPageBookRequest.getMainPageBooks(this.bookMaster).then(res => {
        if (res.data.code === 200) {
          const books = res.data.data;
          const currentBook = books.find(book => book.bookId == this.bookId);
          if (currentBook) {
            this.bookName = currentBook.bookName;
          }
        }
      }).catch(err => {
        console.error('获取账本详情失败:', err);
      });
    },
    refreshData() {
      // 刷新当前页面数据
      if (this.currentComponent && this.$refs.currentComponent && this.$refs.currentComponent.fetchData) {
        this.$refs.currentComponent.fetchData();
      }
    }
  }
};
</script>

<style scoped>
.ledger-detail-container {
  height: 100vh;
  width: 100%;
}

.el-header {
  padding: 0;
  background-color: #f5f7fa;
}

.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 100%;
}

.el-aside {
  background-color: #f5f7fa;
  color: #333;
  border-right: 1px solid #e6e6e6;
}

.el-main {
  padding: 20px;
  background-color: #fff;
}

.main-content {
  height: calc(100vh - 60px);
}
</style>
