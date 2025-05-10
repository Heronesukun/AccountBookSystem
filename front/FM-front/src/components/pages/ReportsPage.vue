<template>
    <div class="reports-container">
      <el-card class="reports-card">
        <div class="reports-header">
          <h2>报表管理</h2>

        </div>
  
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <!-- 基础统计页签 -->
          <el-tab-pane label="基础统计" name="basic">
            <div class="statistics-container">
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-card class="stat-card">
                    <div class="stat-title">记账总笔数</div>
                    <div class="stat-value">{{ totalTransactions }}</div>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card class="stat-card">
                    <div class="stat-title">总收入</div>
                    <div class="stat-value income">¥{{ totalIncome.toFixed(2) }}</div>
                  </el-card>
                </el-col>
                <el-col :span="8">
                  <el-card class="stat-card">
                    <div class="stat-title">总支出</div>
                    <div class="stat-value expense">¥{{ totalExpense.toFixed(2) }}</div>
                  </el-card>
                </el-col>
              </el-row>
  
              <el-row :gutter="20" class="chart-row">
                <el-col :span="12">
                  <el-card class="chart-card">
                    <div class="chart-title">收入分布</div>
                    <div ref="incomeChart" class="chart"></div>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card class="chart-card">
                    <div class="chart-title">支出分布</div>
                    <div ref="expenseChart" class="chart"></div>
                  </el-card>
                </el-col>
              </el-row>
  
              <el-row :gutter="20" class="chart-row">
                <el-col :span="12">
                  <el-card class="chart-card">
                    <div class="chart-title">资产账户统计</div>
                    <div ref="assetChart" class="chart"></div>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card class="chart-card">
                    <div class="chart-title">负债账户统计</div>
                    <div ref="liabilityChart" class="chart"></div>
                  </el-card>
                </el-col>
              </el-row>
  

            </div>
          </el-tab-pane>
  
          <!-- 分类页签 -->
          <el-tab-pane label="分类" name="category">
            <div class="category-container">
              <el-row :gutter="20" class="chart-row">
                <el-col :span="12">
                  <el-card class="chart-card">
                    <div class="chart-title">收入分类统计 ({{ selectedYear }}年{{ selectedMonthLabel }})</div>
                    <div ref="incomeCategoryChart" class="chart"></div>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card class="chart-card">
                    <div class="chart-title">支出分类统计 ({{ selectedYear }}年{{ selectedMonthLabel }})</div>
                    <div ref="expenseCategoryChart" class="chart"></div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
  
          <!-- 账户页签 -->
          <el-tab-pane label="账户" name="account">
            <div class="account-container">

  
              <el-row :gutter="20" class="account-details-row">
                <el-col :span="12">
                  <el-card class="account-card">
                    <div class="account-title">资产账户</div>
                    <el-table :data="assetAccounts" style="width: 100%">
                      <el-table-column prop="name" label="账户名称" />
                      <el-table-column prop="assetAmount" label="余额">
                        <template #default="scope">
                          <span class="income">¥{{ (scope.row.assetAmount !== undefined && scope.row.assetAmount !== null) ? scope.row.assetAmount.toFixed(2) : '0.00' }}</span>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-card>
                </el-col>
                <el-col :span="12">
                  <el-card class="account-card">
                    <div class="account-title">负债账户</div>
                    <el-table :data="liabilityAccounts" style="width: 100%">
                      <el-table-column prop="name" label="账户名称" />
                      <el-table-column prop="assetAmount" label="余额">
                        <template #default="scope">
                          <span class="expense">¥{{ (scope.row.assetAmount !== undefined && scope.row.assetAmount !== null) ? scope.row.assetAmount.toFixed(2) : '0.00' }}</span>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, computed, watch } from 'vue';
  import * as echarts from 'echarts/core';
  import { PieChart, LineChart, BarChart } from 'echarts/charts';
  import {
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent
  } from 'echarts/components';
  import { CanvasRenderer } from 'echarts/renderers';
  import generalTableApi from '@/api/generalTable.js';
  import accountApi from '@/api/account.js';
  import categoryApi from '@/api/category.js';
  
  // 注册ECharts组件
  echarts.use([
    PieChart,
    LineChart,
    BarChart,
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent,
    CanvasRenderer
  ]);
  
  // 状态变量
  const bookId = ref(localStorage.getItem('currentBookId'));
  const userId = ref(localStorage.getItem('userId'));
  const activeTab = ref('basic');
  const selectedYear = ref(new Date().getFullYear());
  const selectedMonth = ref(null);
  const selectedMonthLabel = computed(() => {
    if (!selectedMonth.value) return '';
    return selectedMonth.value.split('-')[1] + '月';
  });
  
  // 图表引用
  const incomeChart = ref(null);
  const expenseChart = ref(null);
  const assetChart = ref(null);
  const liabilityChart = ref(null);
  const monthlyTrendChart = ref(null);
  const incomeCategoryChart = ref(null);
  const expenseCategoryChart = ref(null);
  const netWorthChart = ref(null);
  
  // 数据变量
  const totalTransactions = ref(0);
  const totalIncome = ref(0);
  const totalExpense = ref(0);
  const incomeData = ref([]);
  const expenseData = ref([]);
  const assetAccounts = ref([]);
  const liabilityAccounts = ref([]);
  const monthlyIncomeData = ref(Array(12).fill(0));
  const monthlyExpenseData = ref(Array(12).fill(0));
  const incomeCategoryData = ref([]);
  const expenseCategoryData = ref([]);
  const netWorthData = ref([]);
  const netWorthDates = ref([]);
  
  // 图表实例
  let incomeChartInstance = null;
  let expenseChartInstance = null;
  let assetChartInstance = null;
  let liabilityChartInstance = null;
  let monthlyTrendChartInstance = null;
  let incomeCategoryChartInstance = null;
  let expenseCategoryChartInstance = null;
  let netWorthChartInstance = null;
  
  // 生命周期钩子
  onMounted(() => {
    fetchData();
  });
  
  // 监听年份和月份变化
  watch([selectedYear, selectedMonth], () => {
    fetchData();
  });
  
// 处理标签页切换
const handleTabClick = (tab) => {
  if (tab.props.name === 'basic') {
    fetchBasicData();
  } else if (tab.props.name === 'category') {
    fetchCategoryData();
    // 添加延迟以确保DOM已渲染
    setTimeout(() => {
      initIncomeCategoryChart();
      initExpenseCategoryChart();
    }, 100);
  } else if (tab.props.name === 'account') {
    fetchAccountData();
  }
};
  
  // 处理年份变化
  const handleYearChange = () => {
    fetchData();
  };
  
  // 处理月份变化
  const handleMonthChange = () => {
    fetchData();
  };
  
  // 获取所有数据
  const fetchData = () => {
    fetchBasicData();
    fetchCategoryData();
    fetchAccountData();
  };
  
  // 获取基础统计数据
  const fetchBasicData = async () => {
    try {
      // 设置日期范围
      const startDate = `${selectedYear.value}-01-01`;
      const endDate = `${selectedYear.value}-12-31`;
      
      // 获取流水记录
      const response = await generalTableApi.searchGeneralTables({
        bid: bookId.value,
        userId: userId.value,
        startDate: startDate,
        endDate: endDate
      });
      
      if (response.data.code === 200) {
        const transactions = response.data.data || [];
        
        // 计算总笔数
        totalTransactions.value = transactions.length;
        
        // 计算总收入和总支出
        let income = 0;
        let expense = 0;
        
        // 收入和支出分类数据
        const incomeByCategory = {};
        const expenseByCategory = {};
        
        // 月度收入和支出数据
        const monthlyIncome = Array(12).fill(0);
        const monthlyExpense = Array(12).fill(0);
        
        // 处理每条交易记录
        transactions.forEach(transaction => {
          const amount = parseFloat(transaction.amount);
          const date = new Date(transaction.transactionDate);
          const month = date.getMonth(); // 0-11
          
          if (transaction.type === '收入') {
            income += amount;
            monthlyIncome[month] += amount;
            
            // 按分类统计收入
            const category = transaction.categoryName || '未分类';
            incomeByCategory[category] = (incomeByCategory[category] || 0) + amount;
          } else if (transaction.type === '支出') {
            expense += amount;
            monthlyExpense[month] += amount;
            
            // 按分类统计支出
            const category = transaction.categoryName || '未分类';
            expenseByCategory[category] = (expenseByCategory[category] || 0) + amount;
          }
        });
        
        totalIncome.value = income;
        totalExpense.value = expense;
        monthlyIncomeData.value = monthlyIncome;
        monthlyExpenseData.value = monthlyExpense;
        
// 转换为图表数据格式
incomeData.value = Object.entries(incomeByCategory)
  .filter(([_, value]) => value > 0)  // 过滤掉数值为0的数据
  .map(([name, value]) => ({
    name,
    value
  }));
        
// 转换为图表数据格式
expenseData.value = Object.entries(expenseByCategory)
  .filter(([_, value]) => value !== 0)  // 过滤掉数值为0的数据
  .map(([name, value]) => ({
    name,
    value: Math.abs(value)  // 确保支出值为正数
  }));
        
        // 初始化图表
        initIncomeChart();
        initExpenseChart();
        initMonthlyTrendChart();
      }
      
      // 获取账户信息
      const accountResponse = await accountApi.getAccountList(bookId.value, userId.value);
      
      if (accountResponse.data.code === 200) {
        const accounts = accountResponse.data.data || [];
        
        // 分离资产和负债账户
        const assets = accounts.filter(account => account.type === 'ASSET');
        const liabilities = accounts.filter(account => account.type === 'LIABILITY');
        
        assetAccounts.value = assets;
        liabilityAccounts.value = liabilities;
        
// 转换为图表数据格式
const assetData = assets
  .filter(account => parseFloat(account.assetAmount || 0) !== 0)  // 过滤掉余额为0的账户
  .map(account => ({
    name: account.name,
    value: parseFloat(account.assetAmount || 0)
  }));
        
// 转换为图表数据格式
const liabilityData = liabilities
  .filter(account => parseFloat(account.assetAmount || 0) !== 0)  // 过滤掉余额为0的账户
  .map(account => ({
    name: account.name,
    value: Math.abs(parseFloat(account.assetAmount || 0))  // 确保负债值为正数
  }));
        
        // 初始化图表
        initAssetChart(assetData);
        initLiabilityChart(liabilityData);
      }
    } catch (error) {
      console.error('获取基础统计数据失败:', error);
      ElMessage.error('获取基础统计数据失败');
    }
  };
  
  // 获取分类数据
  const fetchCategoryData = async () => {
    try {
      // 设置日期范围
      let startDate, endDate;
      
      if (selectedMonth.value) {
        // 如果选择了月份，则设置为该月的起止日期
        const [year, month] = selectedMonth.value.split('-');
        const lastDay = new Date(parseInt(year), parseInt(month), 0).getDate();
        startDate = `${year}-${month}-01`;
        endDate = `${year}-${month}-${lastDay}`;
      } else {
        // 否则设置为选择年份的起止日期
        startDate = `${selectedYear.value}-01-01`;
        endDate = `${selectedYear.value}-12-31`;
      }
      
      // 获取流水记录
      const response = await generalTableApi.searchGeneralTables({
        bid: bookId.value,
        userId: userId.value,
        startDate: startDate,
        endDate: endDate
      });
      
      if (response.data.code === 200) {
        const transactions = response.data.data || [];
        
        // 收入和支出分类数据
        const incomeByCategory = {};
        const expenseByCategory = {};
        
        // 处理每条交易记录
        transactions.forEach(transaction => {
          const amount = parseFloat(transaction.amount);
          
          if (transaction.type === '收入') {
            // 按分类统计收入
            const category = transaction.categoryName || '未分类';
            incomeByCategory[category] = (incomeByCategory[category] || 0) + amount;
          } else if (transaction.type === '支出') {
            // 按分类统计支出
            const category = transaction.categoryName || '未分类';
            expenseByCategory[category] = (expenseByCategory[category] || 0) + amount;
          }
        });
        
// 转换为图表数据格式
incomeCategoryData.value = Object.entries(incomeByCategory)
  .filter(([_, value]) => value > 0)  // 过滤掉数值为0的数据
  .map(([name, value]) => ({
    name,
    value
  }));

expenseCategoryData.value = Object.entries(expenseByCategory)
  .filter(([_, value]) => value !== 0)  // 过滤掉数值为0的数据
  .map(([name, value]) => ({
    name,
    value: Math.abs(value)  // 确保支出值为正数
  }));
        
        // 初始化图表
        initIncomeCategoryChart();
        initExpenseCategoryChart();
      }
    } catch (error) {
      console.error('获取分类数据失败:', error);
      ElMessage.error('获取分类数据失败');
    }
  };
  
  // 获取账户数据
  const fetchAccountData = async () => {
    try {
      // 获取净资产信息
      const netWorthResponse = await accountApi.getNetWorth(bookId.value, userId.value);
      
      if (netWorthResponse.data.code === 200) {
        const netWorthHistory = netWorthResponse.data.data || [];
        
        // 转换为图表数据格式 - 添加类型检查
        netWorthDates.value = Array.isArray(netWorthHistory) 
          ? netWorthHistory.map(item => item.date)
          : [];
        netWorthData.value = Array.isArray(netWorthHistory)
          ? netWorthHistory.map(item => item.netWorth)
          : [];
        
        // 初始化图表
        initNetWorthChart();
      }
      
      // 获取账户列表
      const accountResponse = await accountApi.getAccountList(bookId.value, userId.value);
      
      if (accountResponse.data.code === 200) {
        const accounts = accountResponse.data.data || [];
        
        // 分离资产和负债账户
        assetAccounts.value = accounts.filter(account => account.type === 'ASSET').map(account => ({
          ...account,
          assetAmount: parseFloat(account.assetAmount || 0)
        }));
        
        liabilityAccounts.value = accounts.filter(account => account.type === 'LIABILITY').map(account => ({
          ...account,
          assetAmount: parseFloat(account.assetAmount || 0)
        }));
      }
    } catch (error) {
      console.error('获取账户数据失败:', error);
      ElMessage.error('获取账户数据失败');
    }
  };
  
  // 初始化收入分布图表
  const initIncomeChart = () => {
    if (!incomeChart.value) return;
    
    if (!incomeChartInstance) {
      incomeChartInstance = echarts.init(incomeChart.value);
    }
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 10,
        data: incomeData.value.map(item => item.name)
      },
      series: [
        {
          name: '收入分布',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: incomeData.value
        }
      ],
      color: ['#91cc75', '#5470c6', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4']
    };
    
    incomeChartInstance.setOption(option);
  };
  
  // 初始化支出分布图表
  const initExpenseChart = () => {
    if (!expenseChart.value) return;
    
    if (!expenseChartInstance) {
      expenseChartInstance = echarts.init(expenseChart.value);
    }
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 10,
        data: expenseData.value.map(item => item.name)
      },
      series: [
        {
          name: '支出分布',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: expenseData.value
        }
      ],
      color: ['#ee6666', '#fac858', '#5470c6', '#91cc75', '#73c0de', '#3ba272', '#fc8452', '#9a60b4']
    };
    
    expenseChartInstance.setOption(option);
  };
  
  // 初始化资产账户图表
  const initAssetChart = (data) => {
    if (!assetChart.value) return;
    
    if (!assetChartInstance) {
      assetChartInstance = echarts.init(assetChart.value);
    }
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 10,
        data: data.map(item => item.name)
      },
      series: [
        {
          name: '资产账户',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: data
        }
      ],
      color: ['#91cc75', '#5470c6', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4']
    };
    
    assetChartInstance.setOption(option);
  };
  
  // 初始化负债账户图表
  const initLiabilityChart = (data) => {
    if (!liabilityChart.value) return;
    
    if (!liabilityChartInstance) {
      liabilityChartInstance = echarts.init(liabilityChart.value);
    }
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 10,
        data: data.map(item => item.name)
      },
      series: [
        {
          name: '负债账户',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '18',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: data
        }
      ],
      color: ['#ee6666', '#fac858', '#5470c6', '#91cc75', '#73c0de', '#3ba272', '#fc8452', '#9a60b4']
    };
    
    liabilityChartInstance.setOption(option);
  };
  
  // 初始化月度收支趋势图表
  const initMonthlyTrendChart = () => {
    if (!monthlyTrendChart.value) return;
    
    if (!monthlyTrendChartInstance) {
      monthlyTrendChartInstance = echarts.init(monthlyTrendChart.value);
    }
    
    const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
    
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['收入', '支出']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: months
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '收入',
          type: 'line',
          stack: 'Total',
          data: monthlyIncomeData.value,
          itemStyle: {
            color: '#91cc75'
          }
        },
        {
          name: '支出',
          type: 'line',
          stack: 'Total',
          data: monthlyExpenseData.value,
          itemStyle: {
            color: '#ee6666'
          }
        }
      ]
    };
    
    monthlyTrendChartInstance.setOption(option);
  };
  
// 初始化收入分类图表
const initIncomeCategoryChart = () => {
  if (!incomeCategoryChart.value) return;
  
  // 如果图表实例已存在，先销毁
  if (incomeCategoryChartInstance) {
    incomeCategoryChartInstance.dispose();
  }
  
  // 创建新的图表实例
  incomeCategoryChartInstance = echarts.init(incomeCategoryChart.value);
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: incomeCategoryData.value.map(item => item.name)
    },
    series: [
      {
        name: '收入分类',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: incomeCategoryData.value
      }
    ],
    color: ['#91cc75', '#5470c6', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4']
  };
  
  incomeCategoryChartInstance.setOption(option);
  
  // 添加窗口大小变化的监听
  window.addEventListener('resize', () => {
    incomeCategoryChartInstance && incomeCategoryChartInstance.resize();
  });
};
  
// 初始化支出分类图表
const initExpenseCategoryChart = () => {
  if (!expenseCategoryChart.value) return;
  
  // 如果图表实例已存在，先销毁
  if (expenseCategoryChartInstance) {
    expenseCategoryChartInstance.dispose();
  }
  
  // 创建新的图表实例
  expenseCategoryChartInstance = echarts.init(expenseCategoryChart.value);
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: expenseCategoryData.value.map(item => item.name)
    },
    series: [
      {
        name: '支出分类',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: expenseCategoryData.value
      }
    ],
    color: ['#ee6666', '#fac858', '#5470c6', '#91cc75', '#73c0de', '#3ba272', '#fc8452', '#9a60b4']
  };
  
  expenseCategoryChartInstance.setOption(option);
  
  // 添加窗口大小变化的监听
  window.addEventListener('resize', () => {
    expenseCategoryChartInstance && expenseCategoryChartInstance.resize();
  });
};
  
// 初始化净资产趋势图表
const initNetWorthChart = () => {
  if (!netWorthChart.value) return;
  
  if (!netWorthChartInstance) {
    netWorthChartInstance = echarts.init(netWorthChart.value);
  }
  
  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br />{a}: {c}'
    },
    xAxis: {
      type: 'category',
      data: netWorthDates.value,
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [{
      name: '净资产',
      type: 'line',
      data: netWorthData.value,
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      itemStyle: {
        color: '#5470c6'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          {
            offset: 0,
            color: 'rgba(84, 112, 198, 0.5)'
          },
          {
            offset: 1,
            color: 'rgba(84, 112, 198, 0.1)'
          }
        ])
      }
    }],
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    }
  };
  
  netWorthChartInstance.setOption(option);
  
  // 添加窗口大小变化时的自适应
  window.addEventListener('resize', () => {
    netWorthChartInstance.resize();
  });
};
  
  // 窗口大小变化时重新调整图表大小
  window.addEventListener('resize', () => {
    incomeChartInstance?.resize();
    expenseChartInstance?.resize();
    assetChartInstance?.resize();
    liabilityChartInstance?.resize();
    monthlyTrendChartInstance?.resize();
    incomeCategoryChartInstance?.resize();
    expenseCategoryChartInstance?.resize();
    netWorthChartInstance?.resize();
  });
</script>

<style scoped>
.reports-container {
  padding: 20px;
}

.reports-card {
  margin-bottom: 20px;
}

.reports-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-container {
  display: flex;
  gap: 10px;
}

.statistics-container,
.category-container,
.account-container {
  margin-top: 20px;
}

.chart-row {
  margin-top: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.income {
  color: #67c23a;
}

.expense {
  color: #f56c6c;
}

.chart-card {
  padding: 20px;
}

.chart-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 15px;
  text-align: center;
}

.chart {
  height: 300px;
}

.trend-chart {
  height: 400px;
}

.account-details-row {
  margin-top: 20px;
}

.account-card {
  padding: 20px;
}

.account-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 15px;
  font-weight: bold;
}
</style>