<template>
    <div class="home-container">
      <!-- 欢迎区域 -->
      <div class="welcome-section">
        <h2>欢迎使用财务管理系统</h2>
        <p>当前账本: {{ bookName }}</p>
      </div>
  
      <!-- 财务概览卡片 -->
      <el-row :gutter="20" class="dashboard-cards">
        <el-col :span="8">
          <el-card shadow="hover" class="dashboard-card income-card">
            <div class="card-header">
              <i class="el-icon-top"></i>
              <span>本月收入</span>
            </div>
            <div class="card-amount">{{ formatAmount(monthlyIncome) }}</div>
            <div class="card-trend">
              <span :class="incomeTrend >= 0 ? 'trend-up' : 'trend-down'">
                {{ incomeTrend >= 0 ? '+' : '' }}{{ incomeTrend }}%
              </span>
              <span class="trend-text">较上月</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="dashboard-card expense-card">
            <div class="card-header">
              <i class="el-icon-bottom"></i>
              <span>本月支出</span>
            </div>
            <div class="card-amount">{{ formatAmount(monthlyExpense) }}</div>
            <div class="card-trend">
              <span :class="expenseTrend >= 0 ? 'trend-up' : 'trend-down'">
                {{ expenseTrend >= 0 ? '+' : '' }}{{ expenseTrend }}%
              </span>
              <span class="trend-text">较上月</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="dashboard-card balance-card">
            <div class="card-header">
              <i class="el-icon-wallet"></i>
              <span>账户余额</span>
            </div>
            <div class="card-amount">{{ formatAmount(totalBalance) }}</div>
            <div class="card-trend">
              <span class="account-count">{{ accountCount }}个账户</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
  
      <!-- 快速操作区域 -->
      <div class="quick-actions">
        <h3>快速操作</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-button type="primary" icon="el-icon-plus" @click="openRecordDrawer">记一笔</el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="success" icon="el-icon-document" @click="goToTransactions">查看流水</el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="warning" icon="el-icon-pie-chart" @click="goToReports">查看报表</el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="info" icon="el-icon-setting" @click="goToSettings">账本设置</el-button>
          </el-col>
        </el-row>
      </div>
  
      <!-- 最近交易记录 -->
      <div class="recent-transactions">
        <div class="section-header">
          <h3>最近交易记录</h3>
          <el-button type="text" @click="goToTransactions">查看全部</el-button>
        </div>
        <el-table :data="recentTransactions" style="width: 100%" v-loading="loading">
          <el-table-column prop="transactionDate" label="日期" width="120">
            <template #default="scope">
              {{ formatDate(scope.row.transactionDate) }}
            </template>
          </el-table-column>
          <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
          <el-table-column prop="description" label="描述"></el-table-column>
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="scope">
              <span :class="scope.row.type === 'income' ? 'income-amount' : 'expense-amount'">
                {{ scope.row.type === 'income' ? '+' : '-' }}{{ formatAmount(scope.row.amount) }}
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
  
      <!-- 账户概览 -->
      <div class="accounts-overview">
        <div class="section-header">
          <h3>账户概览</h3>
          <el-button type="text" @click="goToAccounts">管理账户</el-button>
        </div>
        <el-row :gutter="20" class="account-cards">
          <el-col :span="8" v-for="account in topAccounts" :key="account.id">
            <el-card shadow="hover" class="account-card">
              <div class="account-name">{{ account.accountName }}</div>
              <div class="account-balance">{{ formatAmount(account.balance) }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, computed } from 'vue';
  import { useRouter } from 'vue-router';
  import generalTableApi from '@/api/generalTable.js';
  import accountApi from '@/api/account.js';
  import mainPageBookRequest from '@/api/mainPageBook.js';
  
  // 路由
  const router = useRouter();
  
  // 数据
  const bookId = ref(localStorage.getItem('currentBookId') || '');
  const bookName = ref('我的账本');
  const loading = ref(true);
  const recentTransactions = ref([]);
  const accounts = ref([]);
  const monthlyIncome = ref(0);
  const monthlyExpense = ref(0);
  const totalBalance = ref(0);
  const incomeTrend = ref(0);
  const expenseTrend = ref(0);
  const accountCount = ref(0);
  
  // 计算属性
  const topAccounts = computed(() => {
    return accounts.value.slice(0, 3);
  });
  
  // 方法
  const fetchData = async () => {
    loading.value = true;
    try {
      await Promise.all([
        fetchBookDetails(),
        fetchRecentTransactions(),
        fetchAccountsData(),
        fetchMonthlySummary()
      ]);
    } catch (error) {
      console.error('获取首页数据失败:', error);
    } finally {
      loading.value = false;
    }
  };
  
  const fetchBookDetails = async () => {
    try {
      const username = localStorage.getItem('username');
      if (username && bookId.value) {
        const res = await mainPageBookRequest.getMainPageBooks(username);
        if (res.data.code === 200) {
          const books = res.data.data;
          const currentBook = books.find(book => book.bookId == bookId.value);
          if (currentBook) {
            bookName.value = currentBook.bookName;
          }
        }
      }
    } catch (error) {
      console.error('获取账本详情失败:', error);
    }
  };
  
  const fetchRecentTransactions = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (bookId.value && userId) {
        const res = await generalTableApi.getGeneralTablesByBid(bookId.value, userId);
        if (res.data.code === 200) {
          // 获取最近5条交易记录
          recentTransactions.value = res.data.data.slice(0, 5).map(item => ({
            id: item.id,
            transactionDate: item.transactionDate,
            categoryName: item.categoryName,
            description: item.description,
            amount: item.amount,
            type: item.type
          }));
        }
      }
    } catch (error) {
      console.error('获取最近交易记录失败:', error);
    }
  };
  
  const fetchAccountsData = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (bookId.value && userId) {
        const res = await accountApi.getAccountList(bookId.value, userId);
        if (res.data.code === 200) {
          accounts.value = res.data.data;
          accountCount.value = accounts.value.length;
          
          // 计算总余额
          totalBalance.value = accounts.value.reduce((sum, account) => sum + account.balance, 0);
        }
      }
    } catch (error) {
      console.error('获取账户数据失败:', error);
    }
  };
  
  const fetchMonthlySummary = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (bookId.value && userId) {
        // 获取当前月份的开始和结束日期
        const now = new Date();
        const currentMonthStart = new Date(now.getFullYear(), now.getMonth(), 1);
        const currentMonthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 0);
        
        // 获取上个月的开始和结束日期
        const lastMonthStart = new Date(now.getFullYear(), now.getMonth() - 1, 1);
        const lastMonthEnd = new Date(now.getFullYear(), now.getMonth(), 0);
        
        // 格式化日期为字符串 YYYY-MM-DD
        const formatDateToString = (date) => {
          return date.toISOString().split('T')[0];
        };
        
        // 获取当月汇总
        const currentMonthRes = await generalTableApi.getSummaryByDate(
          bookId.value,
          userId,
          'month',
          formatDateToString(currentMonthStart),
          formatDateToString(currentMonthEnd)
        );
        
        // 获取上月汇总
        const lastMonthRes = await generalTableApi.getSummaryByDate(
          bookId.value,
          userId,
          'month',
          formatDateToString(lastMonthStart),
          formatDateToString(lastMonthEnd)
        );
        
        if (currentMonthRes.data.code === 200) {
          const currentData = currentMonthRes.data.data;
          monthlyIncome.value = currentData.income || 0;
          monthlyExpense.value = currentData.expense || 0;
          
          if (lastMonthRes.data.code === 200) {
            const lastData = lastMonthRes.data.data;
            const lastIncome = lastData.income || 0;
            const lastExpense = lastData.expense || 0;
            
            // 计算环比增长率
            incomeTrend.value = lastIncome === 0 ? 100 : Math.round((monthlyIncome.value - lastIncome) / lastIncome * 100);
            expenseTrend.value = lastExpense === 0 ? 100 : Math.round((monthlyExpense.value - lastExpense) / lastExpense * 100);
          }
        }
      }
    } catch (error) {
      console.error('获取月度汇总数据失败:', error);
    }
  };
  
  // 格式化金额
  const formatAmount = (amount) => {
    return amount.toFixed(2);
  };
  
  // 格式化日期
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return `${date.getMonth() + 1}/${date.getDate()}`;
  };
  
  // 页面跳转
  const openRecordDrawer = () => {
    // 通知父组件打开记账抽屉
    const event = new CustomEvent('open-record-drawer');
    window.dispatchEvent(event);
  };
  
  const goToTransactions = () => {
    // 通知父组件切换到交易记录页面
    const event = new CustomEvent('change-page', { detail: { pageId: 'transactions' } });
    window.dispatchEvent(event);
  };
  
  const goToReports = () => {
    // 通知父组件切换到报表页面
    const event = new CustomEvent('change-page', { detail: { pageId: 'reports' } });
    window.dispatchEvent(event);
  };
  
  const goToAccounts = () => {
    // 通知父组件切换到账户管理页面
    const event = new CustomEvent('change-page', { detail: { pageId: 'accounts' } });
    window.dispatchEvent(event);
  };
  
  const goToSettings = () => {
    // 通知父组件切换到设置页面
    const event = new CustomEvent('change-page', { detail: { pageId: 'settings' } });
    window.dispatchEvent(event);
  };
  
  // 生命周期钩子
  onMounted(() => {
    // 监听事件
    window.addEventListener('refresh-home-data', fetchData);
    
    // 获取数据
    fetchData();
    
    // 组件卸载时移除事件监听
    return () => {
      window.removeEventListener('refresh-home-data', fetchData);
    };
  });
  </script>
  
  <style scoped>
  .home-container {
    padding: 20px;
  }
  
  .welcome-section {
    margin-bottom: 30px;
  }
  
  .welcome-section h2 {
    margin-bottom: 10px;
    color: #303133;
  }
  
  .dashboard-cards {
    margin-bottom: 30px;
  }
  
  .dashboard-card {
    height: 120px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  
  .income-card {
    background-color: #f0f9eb;
  }
  
  .expense-card {
    background-color: #fef0f0;
  }
  
  .balance-card {
    background-color: #f5f7fa;
  }
  
  .card-header {
    display: flex;
    align-items: center;
    font-size: 16px;
    color: #606266;
  }
  
  .card-header i {
    margin-right: 8px;
  }
  
  .card-amount {
    font-size: 24px;
    font-weight: bold;
    color: #303133;
  }
  
  .card-trend {
    font-size: 14px;
    color: #909399;
  }
  
  .trend-up {
    color: #67c23a;
  }
  
  .trend-down {
    color: #f56c6c;
  }
  
  .trend-text {
    margin-left: 5px;
  }
  
  .quick-actions {
    margin-bottom: 30px;
  }
  
  .quick-actions h3 {
    margin-bottom: 15px;
    color: #303133;
  }
  
  .quick-actions .el-button {
    width: 100%;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }
  
  .section-header h3 {
    margin: 0;
    color: #303133;
  }
  
  .recent-transactions {
    margin-bottom: 30px;
  }
  
  .income-amount {
    color: #67c23a;
  }
  
  .expense-amount {
    color: #f56c6c;
  }
  
  .accounts-overview {
    margin-bottom: 30px;
  }
  
  .account-cards {
    display: flex;
    flex-wrap: wrap;
  }
  
  .account-card {
    margin-bottom: 15px;
    height: 100px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  
  .account-name {
    font-size: 16px;
    color: #606266;
    margin-bottom: 10px;
  }
  
  .account-balance {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
  }
  </style>