<template>
  <div class="ledger-detail">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <el-button @click="goHome" type="text" class="home-button">
        <el-icon><House /></el-icon>
        Home
      </el-button>
      <el-button @click="goToMemberManagement" type="text" class="member-button">
        <el-icon><User /></el-icon>
        成员管理
      </el-button>
    </el-header>

    <!-- 布局容器 -->
    <el-container>
      <!-- 左侧侧边栏 -->
      <el-aside class="sidebar" width="200px">
        <!-- “记一笔”按钮及抽屉 -->
        <el-button @click="openDrawer" type="primary" class="record-button">记一笔</el-button>
        <el-drawer v-model="drawerVisible" title="记一笔" direction="rtl" size="400px">
          <el-tabs v-model="activeTab" class="record-tabs">
            <el-tab-pane label="支出" name="expense"></el-tab-pane>
            <el-tab-pane label="收入" name="income"></el-tab-pane>
            <el-tab-pane label="转账" name="transfer"></el-tab-pane>
            <el-tab-pane label="余额" name="balance"></el-tab-pane>
            <el-tab-pane label="退款" name="refund"></el-tab-pane>
          </el-tabs>
          <el-form :model="recordForm" ref="recordForm" :rules="rules" class="record-form">
            <el-form-item label="类别" prop="category">
              <el-select v-model="recordForm.category" placeholder="请选择类别">
                <el-option label="购物" value="shopping"></el-option>
                <el-option label="餐饮" value="dining"></el-option>
                <el-option label="交通" value="transport"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="金额" prop="amount">
              <el-input v-model.number="recordForm.amount" placeholder="请输入金额"></el-input>
            </el-form-item>
            <el-form-item label="日期" prop="date">
              <el-date-picker
                  v-model="recordForm.date"
                  type="date"
                  placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="recordForm.remark" placeholder="请输入备注"></el-input>
            </el-form-item>
          </el-form>
          <div class="drawer-footer">
            <el-button @click="saveRecord">保存</el-button>
            <el-button type="primary" @click="saveAndContinue">保存并再记一笔</el-button>
          </div>
        </el-drawer>

        <!-- 导航菜单 -->
        <el-menu default-active="1" class="menu">
          <el-menu-item index="1" @click="goToHome">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="2" @click="goToTransaction">
            <el-icon><List /></el-icon>
            <span>流水</span>
          </el-menu-item>
          <el-menu-item index="3" @click="goToReport">
            <el-icon><Chart /></el-icon>
            <span>报表</span>
          </el-menu-item>
          <el-sub-menu index="4">
            <template #title>
              <el-icon><Folder /></el-icon>
              <span>分类</span>
            </template>
            <el-menu-item index="4-1" @click="goToCategory">收支分类</el-menu-item>
            <el-menu-item index="4-2" @click="goToAccount">账户管理</el-menu-item>
            <el-menu-item index="4-3" @click="goToMember">成员管理</el-menu-item>
            <el-menu-item index="4-4" @click="goToMerchant">商家管理</el-menu-item>
            <el-menu-item index="4-5" @click="goToProject">项目管理</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="5" @click="goToSettings" class="settings">
            <el-icon><Setting /></el-icon>
            <span>设置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区域 -->
      <el-main class="main-content">
        <el-row :gutter="20">
          <!-- 账本详情 -->
          <el-col :span="8">
            <el-card class="detail-card">
              <h3>账本详情</h3>
              <p>时间戳: {{ ledgerDetail.timestamp }}</p>
              <p>成员: {{ ledgerDetail.members.join(', ') || '无' }}</p>
              <p>所有者: {{ ledgerDetail.owner }}</p>
              <p>备注: {{ ledgerDetail.remark || '无' }}</p>
              <p>创建: {{ ledgerDetail.createdAmount || '0.00' }}</p>
            </el-card>
          </el-col>
          <!-- 账本概览 -->
          <el-col :span="16">
            <el-card class="overview-card">
              <h3>账本概览 <span class="date-range">2025年03月02日 - 2025年03月31日</span></h3>
              <div ref="chart" style="width: 100%; height: 300px;"></div>
              <el-row class="balance-list">
                <el-col :span="12">
                  <p class="income">总收入 {{ ledgerDetail.totalIncome || '0.00' }}</p>
                </el-col>
                <el-col :span="12">
                  <p class="expense">总支出 {{ ledgerDetail.totalExpense || '0.00' }}</p>
                </el-col>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';
import {
  House, List, Chart, Folder, Setting, User
} from '@element-plus/icons-vue';

// 路由
const router = useRouter();
const goHome = () => router.push('/home');
const goToMemberManagement = () => router.push('/member-management');
const goToTransaction = () => router.push('/transaction');
const goToReport = () => router.push('/report');
const goToCategory = () => router.push('/category');
const goToAccount = () => router.push('/account');
const goToMember = () => router.push('/member');
const goToMerchant = () => router.push('/merchant');
const goToProject = () => router.push('/project');
const goToSettings = () => router.push('/settings');

// 抽屉和表单
const drawerVisible = ref(false);
const activeTab = ref('expense');
const recordForm = reactive({
  category: '',
  amount: null,
  date: '',
  remark: ''
});
const rules = {
  category: [{ required: true, message: '请选择类别', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  date: [{ required: true, message: '请选择日期', trigger: 'blur' }]
};
const openDrawer = () => (drawerVisible.value = true);
const saveRecord = () => {
  if (recordForm.amount <= 0) {
    ElMessage.error('金额必须大于0');
    return;
  }
  console.log('保存记录:', recordForm);
  drawerVisible.value = false;
  recordForm.category = '';
  recordForm.amount = null;
  recordForm.date = '';
  recordForm.remark = '';
};
const saveAndContinue = () => {
  if (recordForm.amount <= 0) {
    ElMessage.error('金额必须大于0');
    return;
  }
  console.log('保存并继续记录:', recordForm);
  recordForm.category = '';
  recordForm.amount = null;
  recordForm.date = '';
  recordForm.remark = '';
};

// 账本数据（模拟数据，实际可从后端获取）
const ledgerDetail = reactive({
  timestamp: '2025-03-02 14:21:06',
  members: [],
  owner: '185****6336',
  remark: '',
  createdAmount: '0.00',
  totalIncome: '0.00',
  totalExpense: '0.00'
});

// 图表初始化
const chart = ref(null);
onMounted(() => {
  const myChart = echarts.init(chart.value);
  const option = {
    xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月'] },
    yAxis: { type: 'value' },
    series: [
      { name: '支出', type: 'line', data: [120, 200, 150, 80, 70], itemStyle: { color: '#FF0000' } },
      { name: '收入', type: 'line', data: [200, 150, 300, 250, 180], itemStyle: { color: '#000000' } },
    ],
    tooltip: { trigger: 'axis' },
    legend: { data: ['支出', '收入'] }
  };
  myChart.setOption(option);

  // 监听窗口大小变化
  window.addEventListener('resize', () => myChart.resize());
});
</script>

<style scoped>
.ledger-detail {
  height: 100vh;
  background-color: #ffffff;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  border-bottom: 1px solid #e6e6e6;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.home-button, .member-button {
  font-size: 16px;
  color: #409eff;
}

.sidebar {
  background-color: #f5f5f5;
  padding: 20px 0;
  box-shadow: 2px 0 4px rgba(0, 0, 0, 0.05);
}

.record-button {
  width: 100%;
  margin-bottom: 20px;
  background-color: #ff6f00;
  border-color: #ff6f00;
}

.record-tabs {
  padding: 10px;
}

.record-form {
  padding: 10px;
}

.drawer-footer {
  text-align: right;
  padding: 10px;
}

.menu {
  border: none;
  background-color: transparent;
}

.settings {
  position: absolute;
  bottom: 20px;
  width: 100%;
}

.main-content {
  padding: 20px;
}

.detail-card, .overview-card {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.detail-card h3, .overview-card h3 {
  margin-bottom: 16px;
  color: #303133;
}

.date-range {
  font-size: 14px;
  color: #999999;
  margin-left: 10px;
}

.balance-list {
  margin-top: 16px;
}

.income {
  color: #000000;
  font-size: 16px;
  text-align: right;
}

.expense {
  color: #ff0000;
  font-size: 16px;
  text-align: right;
}
</style>