<template>
    <div class="home-page-container">
      <!-- 年度收支统计卡片 -->
      <el-card class="annual-stats-card">
        <div class="annual-stats">
          <div class="stats-left">
            <h3>本年收支统计</h3>
            <div class="expense-amount">
              <span>总支出</span>
              <div class="amount-value negative">-{{ formatAmount(yearlyExpense) }}</div>
            </div>
            <div class="stats-summary">
              <div>总收入 {{ formatAmount(yearlyIncome) }}</div>
              <div>结余 {{ formatAmount(yearlyBalance) }}</div>
            </div>
          </div>

        </div>
      </el-card>
  
      <div class="stats-row">
        <!-- 本月支出情况 -->
        <el-card class="monthly-expense-card">
          <div class="card-header">
            <h3>本月支出情况</h3>
            <div class="total-expense">总支出 {{ formatAmount(monthlyExpense) }} 支出笔数 {{ monthlyExpenseCount }}</div>
          </div>
          <div class="expense-chart-container">
            <div ref="monthlyExpenseChart" class="expense-chart"></div>
          </div>
        </el-card>
  
        <!-- 收支报告 -->
        <el-card class="income-expense-report">
          <h3>收支报告</h3>
          <div class="report-list">
            <div class="report-item" v-for="(item, index) in reportItems" :key="index">
              <div class="report-icon" :class="item.icon">
                <el-icon><component :is="item.icon"></component></el-icon>
              </div>
              <div class="report-info">
                <div class="report-title">{{ item.title }}</div>
                <div class="report-date">{{ item.date }}</div>
              </div>
              <div class="report-amount">
                <div>总收入 {{ formatAmount(item.income) }}</div>
                <div>总支出 {{ formatAmount(item.expense) }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
  
      <!-- 本月各分类支出排行 -->
      <el-card class="category-ranking-card">
        <div class="card-header">
          <h3>本月各分类支出排行</h3>
          <div class="ranking-count">记账笔数 {{ totalRecords }} 总支出 -{{ formatAmount(monthlyExpense) }}</div>
        </div>
        <div class="ranking-list">
          <div class="ranking-item" v-for="(category, index) in categoryRanking" :key="index">
            <div class="ranking-number">{{ index + 1 }}</div>
            <div class="category-icon">
              <el-icon><component :is="getCategoryIcon(category.categoryName)"></component></el-icon>
            </div>
            <div class="category-name">{{ category.categoryName }}</div>
            <div class="category-amount">-{{ formatAmount(category.amount) }}</div>
          </div>
        </div>
      </el-card>
  
      <!-- 本月各成员收支 -->
      <el-card class="member-stats-card">
        <h3>本月各成员收支</h3>
        <div class="member-list">
          <div class="member-item" v-for="(member, index) in memberStats" :key="index">
            <div class="member-avatar">
              <el-avatar :size="40">{{ member.userName.substring(0, 1) }}</el-avatar>
            </div>
            <div class="member-info">
              <div class="member-name">{{ member.userName }}</div>
              <div class="member-amount">
                <span>总收入 {{ formatAmount(member.income) }}</span>
                <span>总支出 -{{ formatAmount(member.expense) }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </template>
  
  <script>
  import * as echarts from 'echarts';
  import generalTableApi from '@/api/generalTable.js';
  import bookMemberRequest from '@/api/bookMember.js';
  import { Calendar, Money, Food, Goods, Histogram } from '@element-plus/icons-vue';
  
  export default {
    name: 'HomePage',
    components: {
      Calendar,
      Money,
      Food,
      Goods,
      Histogram
    },
    props: {
      bookId: {
        type: [String, Number],
        required: true
      }
    },
    data() {
      return {
        // 年度统计数据
        yearlyIncome: 2700.00,
        yearlyExpense: 74.06,
        yearlyBalance: 2774.09,
        
        // 月度支出数据
        monthlyExpense: 109.06,
        monthlyExpenseCount: 0,
        monthlyExpenseData: [],
        
        // 收支报告项
        reportItems: [
          {
            icon: 'Calendar',
            title: '今天',
            date: this.formatDate(new Date()),
            income: 0.00,
            expense: 0.00
          },
          {
            icon: 'Calendar',
            title: '本周',
            date: this.getWeekRange(),
            income: 2700.00,
            expense: 109.06
          },
          {
            icon: 'Calendar',
            title: '本月',
            date: this.getMonthRange(),
            income: 2700.00,
            expense: 109.06
          }
        ],
        
        // 分类支出排行
        categoryRanking: [],
        totalRecords: 1,
        
        // 成员收支统计
        memberStats: [],
        
        // 图表实例
        expenseChart: null
      };
    },
    mounted() {
      this.fetchData();
      this.initExpenseChart();
    },
    methods: {
      // 获取账本数据
      async fetchData() {
        try {
          const userId = localStorage.getItem('userId');
          if (!this.bookId || !userId) {
            console.error('缺少必要参数：bookId 或 userId');
            return;
          }
          
          // 获取账本交易记录
          const response = await generalTableApi.getGeneralTablesByBid(this.bookId, userId);
          if (response.data.code === 200) {
            const records = response.data.data || [];
            
            // 处理数据
            this.processTransactionData(records);
            
            // 获取成员数据
            this.fetchMemberData();
          } else {
            console.error('获取账本数据失败:', response.data.msg);
          }
        } catch (error) {
          console.error('获取账本数据出错:', error);
        }
      },
      
      // 处理交易数据
      processTransactionData(records) {
        // 重置数据
        this.yearlyIncome = 0;
        this.yearlyExpense = 0;
        this.monthlyExpense = 0;
        this.monthlyExpenseCount = 0;
        this.categoryRanking = [];
        
        // 分类统计
        const categoryMap = new Map();
        
        // 当前日期
        const now = new Date();
        const currentYear = now.getFullYear();
        const currentMonth = now.getMonth();
        
        // 日期数据用于图表
        const dailyExpenseMap = new Map();
        const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
        
        // 初始化每日数据
        for (let i = 1; i <= daysInMonth; i++) {
          const dateKey = `${currentMonth + 1}.${i < 10 ? '0' + i : i}`;
          dailyExpenseMap.set(dateKey, 0);
        }
        
        // 处理每条记录
        records.forEach(record => {
          const recordDate = new Date(record.date);
          const recordYear = recordDate.getFullYear();
          const recordMonth = recordDate.getMonth();
          const recordDay = recordDate.getDate();
          
          // 年度统计
          if (recordYear === currentYear) {
            if (record.type === '收入') {
              this.yearlyIncome += record.amount;
            } else if (record.type === '支出') {
              this.yearlyExpense += record.amount;
            }
          }
          
          // 月度统计
          if (recordYear === currentYear && recordMonth === currentMonth) {
            if (record.type === '支出') {
              this.monthlyExpense += record.amount;
              this.monthlyExpenseCount++;
              
              // 分类统计
              const categoryName = record.categoryName || '未分类';
              if (categoryMap.has(categoryName)) {
                const category = categoryMap.get(categoryName);
                category.amount += record.amount;
                category.count++;
              } else {
                categoryMap.set(categoryName, {
                  categoryName,
                  amount: record.amount,
                  count: 1,
                  icon: record.generalIcon || 'Money'
                });
              }
              
              // 日期统计
              const dateKey = `${recordMonth + 1}.${recordDay < 10 ? '0' + recordDay : recordDay}`;
              if (dailyExpenseMap.has(dateKey)) {
                dailyExpenseMap.set(dateKey, dailyExpenseMap.get(dateKey) + record.amount);
              }
            }
          }
        });
        
        // 计算结余
        this.yearlyBalance = this.yearlyIncome - this.yearlyExpense;
        
        // 转换分类数据为数组并排序
        this.categoryRanking = Array.from(categoryMap.values())
          .sort((a, b) => b.amount - a.amount);
        
        // 设置总记录数
        this.totalRecords = this.monthlyExpenseCount;
        
        // 更新图表数据
        this.updateExpenseChart(dailyExpenseMap);
        
        // 更新报告数据
        this.updateReportData(records);
      },
      
      // 更新报告数据
      updateReportData(records) {
        // 获取今天的日期范围
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        
        // 获取本周的日期范围
        const weekStart = new Date(today);
        weekStart.setDate(today.getDate() - today.getDay());
        const weekEnd = new Date(weekStart);
        weekEnd.setDate(weekStart.getDate() + 7);
        
        // 获取本月的日期范围
        const monthStart = new Date(today.getFullYear(), today.getMonth(), 1);
        const monthEnd = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        
        // 统计数据
        let todayIncome = 0, todayExpense = 0;
        let weekIncome = 0, weekExpense = 0;
        let monthIncome = 0, monthExpense = 0;
        
        records.forEach(record => {
          const recordDate = new Date(record.date);
          
          // 今天的统计
          if (recordDate >= today && recordDate < tomorrow) {
            if (record.type === '收入') {
              todayIncome += record.amount;
            } else if (record.type === '支出') {
              todayExpense += record.amount;
            }
          }
          
          // 本周的统计
          if (recordDate >= weekStart && recordDate < weekEnd) {
            if (record.type === '收入') {
              weekIncome += record.amount;
            } else if (record.type === '支出') {
              weekExpense += record.amount;
            }
          }
          
          // 本月的统计
          if (recordDate >= monthStart && recordDate <= monthEnd) {
            if (record.type === '收入') {
              monthIncome += record.amount;
            } else if (record.type === '支出') {
              monthExpense += record.amount;
            }
          }
        });
        
        // 更新报告数据
        this.reportItems = [
          {
            icon: 'Calendar',
            title: '今天',
            date: this.formatDate(today),
            income: todayIncome,
            expense: todayExpense
          },
          {
            icon: 'Calendar',
            title: '本周',
            date: this.getWeekRange(),
            income: weekIncome,
            expense: weekExpense
          },
          {
            icon: 'Calendar',
            title: '本月',
            date: this.getMonthRange(),
            income: monthIncome,
            expense: monthExpense
          }
        ];
      },
      
      // 获取成员数据
      async fetchMemberData() {
        try {
          const response = await bookMemberRequest.getBookMembers(this.bookId);
          if (response.data.code === 200) {
            const members = response.data.data || [];
            
            // 处理成员数据
            this.memberStats = members.map(member => ({
              userName: member.memberName || '未知用户',
              income: 2700.00, // 示例数据，实际应从交易记录中计算
              expense: 109.06  // 示例数据，实际应从交易记录中计算
            }));
          }
        } catch (error) {
          console.error('获取成员数据出错:', error);
        }
      },
      
      // 初始化支出图表
      initExpenseChart() {
        this.$nextTick(() => {
          if (this.$refs.monthlyExpenseChart) {
            this.expenseChart = echarts.init(this.$refs.monthlyExpenseChart);
            
            // 设置图表配置
            const option = {
              grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                top: '3%',
                containLabel: true
              },
              xAxis: {
                type: 'category',
                data: ['05.01', '05.03', '05.07', '05.10', '05.13', '05.16', '05.19', '05.22', '05.25', '05.28', '05.31'],
                axisLine: {
                  lineStyle: {
                    color: '#E0E0E0'
                  }
                },
                axisLabel: {
                  color: '#999',
                  fontSize: 10
                }
              },
              yAxis: {
                type: 'value',
                axisLine: {
                  show: false
                },
                axisTick: {
                  show: false
                },
                splitLine: {
                  lineStyle: {
                    color: '#E0E0E0',
                    type: 'dashed'
                  }
                },
                axisLabel: {
                  color: '#999',
                  fontSize: 10
                }
              },
              series: [{
                data: [0, -150, -75, 0, 0, 0, 0, 0, 0, 0, 0],
                type: 'line',
                smooth: true,
                symbol: 'circle',
                symbolSize: 6,
                lineStyle: {
                  color: '#4FC08D'
                },
                itemStyle: {
                  color: '#4FC08D'
                },
                areaStyle: {
                  color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                      offset: 0,
                      color: 'rgba(79, 192, 141, 0.3)'
                    }, {
                      offset: 1,
                      color: 'rgba(79, 192, 141, 0.1)'
                    }]
                  }
                }
              }]
            };
            
            this.expenseChart.setOption(option);
            
            // 监听窗口大小变化，调整图表大小
            window.addEventListener('resize', this.resizeChart);
          }
        });
      },
      
      // 更新支出图表数据
      updateExpenseChart(dailyExpenseMap) {
        if (this.expenseChart) {
          const dates = [];
          const values = [];
          
          // 转换Map为数组
          for (const [date, amount] of dailyExpenseMap.entries()) {
            dates.push(date);
            values.push(-amount); // 支出为负值
          }
          
          // 更新图表数据
          this.expenseChart.setOption({
            xAxis: {
              data: dates
            },
            series: [{
              data: values
            }]
          });
        }
      },
      
      // 调整图表大小
      resizeChart() {
        if (this.expenseChart) {
          this.expenseChart.resize();
        }
      },
      
      // 格式化金额
      formatAmount(amount) {
        return amount.toFixed(2);
      },
      
      // 格式化日期
      formatDate(date) {
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${year}年${month}月${day}日`;
      },
      
      // 获取周范围
      getWeekRange() {
        const now = new Date();
        const weekStart = new Date(now);
        weekStart.setDate(now.getDate() - now.getDay());
        const weekEnd = new Date(weekStart);
        weekEnd.setDate(weekStart.getDate() + 6);
        
        return `${weekStart.getMonth() + 1}月${weekStart.getDate()}日-${weekEnd.getMonth() + 1}月${weekEnd.getDate()}日`;
      },
      
      // 获取月范围
      getMonthRange() {
        const now = new Date();
        const year = now.getFullYear();
        const month = now.getMonth() + 1;
        return `${month}月01日-${month}月${new Date(year, month, 0).getDate()}日`;
      },
      
      // 获取分类图标
      getCategoryIcon(categoryName) {
        const iconMap = {
          '食品酒水': 'Food',
          '日用品': 'Goods',
          '交通': 'Van',
          '娱乐': 'VideoPlay',
          '服装': 'Suitcase',
          '医疗': 'FirstAidKit',
          '住房': 'House',
          '通讯': 'Phone',
          '其他': 'More'
        };
        
        return iconMap[categoryName] || 'Money';
      }
    },
    beforeDestroy() {
      // 移除窗口大小变化监听
      window.removeEventListener('resize', this.resizeChart);
      
      // 销毁图表实例
      if (this.expenseChart) {
        this.expenseChart.dispose();
        this.expenseChart = null;
      }
    }
  };
  </script>
  
  <style scoped>
  .home-page-container {
    padding: 16px;
  }
  
  /* 卡片通用样式 */
  .el-card {
    margin-bottom: 16px;
    border-radius: 8px;
  }
  
  .el-card :deep(.el-card__body) {
    padding: 16px;
  }
  
  h3 {
    margin: 0 0 16px 0;
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }
  
  /* 年度统计卡片 */
  .annual-stats-card {
    background: linear-gradient(135deg, #3a8ee6, #53a8ff);
    color: white;
  }
  
  .annual-stats-card :deep(.el-card__body) {
    padding: 20px;
  }
  
  .annual-stats {
    display: flex;
    justify-content: space-between;
  }
  
  .stats-left {
    flex: 1;
  }
  
  .stats-left h3 {
    color: white;
    margin-bottom: 20px;
  }
  
  .expense-amount {
    margin-bottom: 16px;
  }
  
  .expense-amount span {
    font-size: 14px;
  }
  
  .amount-value {
    font-size: 28px;
    font-weight: bold;
    margin-top: 4px;
  }
  
  .amount-value.negative {
    color: #fff;
  }
  
  .stats-summary {
    display: flex;
    justify-content: space-between;
    font-size: 14px;
  }
  
  .stats-right {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .piggy-bank-img {
    width: 80px;
    height: 80px;
    object-fit: contain;
  }
  
  /* 统计行 */
  .stats-row {
    display: flex;
    gap: 16px;
    margin-bottom: 16px;
  }
  
  .monthly-expense-card, .income-expense-report {
    flex: 1;
    min-height: 300px;
  }
  
  /* 月度支出图表 */
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }
  
  .total-expense, .ranking-count {
    font-size: 12px;
    color: #999;
  }
  
  .expense-chart-container {
    height: 250px;
  }
  
  .expense-chart {
    width: 100%;
    height: 100%;
  }
  
  /* 收支报告 */
  .report-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
  
  .report-item {
    display: flex;
    align-items: center;
    padding: 12px;
    background-color: #f9f9f9;
    border-radius: 8px;
  }
  
  .report-icon {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: #e6f7ff;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12px;
    color: #1890ff;
  }
  
  .report-info {
    flex: 1;
  }
  
  .report-title {
    font-weight: 500;
    margin-bottom: 4px;
  }
  
  .report-date {
    font-size: 12px;
    color: #999;
  }
  
  .report-amount {
    text-align: right;
    font-size: 14px;
  }
  
  /* 分类排行 */
  .ranking-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  .ranking-item {
    display: flex;
    align-items: center;
    padding: 12px;
    background-color: #f9f9f9;
    border-radius: 8px;
  }
  
  .ranking-number {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background-color: #f0f0f0;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12px;
    font-size: 14px;
    font-weight: 500;
  }
  
  .category-icon {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background-color: #e6f7ff;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12px;
    color: #1890ff;
  }
  
  .category-name {
    flex: 1;
    font-weight: 500;
  }
  
  .category-amount {
    font-weight: 500;
    color: #ff4d4f;
  }
  
  /* 成员统计 */
  .member-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
  
  .member-item {
    display: flex;
    align-items: center;
    padding: 12px;
    background-color: #f9f9f9;
    border-radius: 8px;
  }
  
  .member-avatar {
    margin-right: 12px;
  }
  
  .member-info {
    flex: 1;
  }
  
  .member-name {
    font-weight: 500;
    margin-bottom: 4px;
  }
  
  .member-amount {
    display: flex;
    justify-content: space-between;
    font-size: 14px;
  }
  
  @media (max-width: 768px) {
    .stats-row {
      flex-direction: column;
    }
    
    .stats-summary {
      flex-direction: column;
      gap: 8px;
    }
    
    .member-amount {
      flex-direction: column;
      gap: 4px;
    }
  }
  </style>