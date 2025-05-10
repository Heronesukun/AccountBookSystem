<template>
  <div class="transactions-page">
    <!-- 顶部统计卡片 -->
    <div class="summary-cards">
      <el-card class="summary-card balance">
        <div class="card-title">结余</div>
        <div class="card-amount">{{ formatCurrency(totalBalance) }}</div>
      </el-card>
      <el-card class="summary-card income">
        <div class="card-title">收入</div>
        <div class="card-amount">{{ formatCurrency(totalIncome) }}</div>
      </el-card>
      <el-card class="summary-card expense">
        <div class="card-title">支出</div>
        <div class="card-amount">{{ formatCurrency(totalExpense) }}</div>
      </el-card>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="left-actions">
        <!-- 新增记录按钮 -->
        <el-button type="primary" @click="openAddRecordDrawer">
          <i class="el-icon-plus"></i> 新增记录
        </el-button>
        <!-- 批量删除按钮 -->
        <el-button type="danger" :disabled="selectedRecords.length === 0" @click="confirmBatchDelete">
          <i class="el-icon-delete"></i> 批量删除
        </el-button>
      </div>

      <div class="right-actions">
        <!-- 搜索框 -->
        <el-input
          placeholder="搜索记录..."
          v-model="searchKeyword"
          class="search-input"
          prefix-icon="el-icon-search"
          clearable
          @clear="handleSearch"
          @keyup.enter.native="handleSearch"
        >
          <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
        </el-input>
        
        <!-- 筛选按钮 -->
        <el-button @click="showFilters = !showFilters">
          <i class="el-icon-s-operation"></i> 筛选
          <i :class="showFilters ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
        </el-button>
      </div>
    </div>

    <!-- 筛选面板 -->
    <el-collapse-transition>
      <div v-show="showFilters" class="filter-panel">
    <el-form :inline="true" class="filter-form">
      <el-form-item label="日期范围">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          :picker-options="{
            disabledDate(time) {
              return time.getTime() > Date.now();
            }
          }"
        ></el-date-picker>
      </el-form-item>
          
          <el-form-item label="类型">
            <el-select v-model="filterType" placeholder="选择类型" clearable>
              <el-option label="收入" value="收入"></el-option>
              <el-option label="支出" value="支出"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="分类">
            <el-cascader
              v-model="filterCategory"
              :options="categoryOptions"
              placeholder="选择分类"
              clearable
            ></el-cascader>
          </el-form-item>
          
          <el-form-item label="商家">
            <el-select v-model="filterMerchant" placeholder="选择商家" filterable clearable>
              <el-option
                v-for="item in merchantOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="账户">
            <el-select v-model="filterAccount" placeholder="选择账户" filterable clearable>
              <el-option
                v-for="item in accountOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        
        <div class="filter-actions">
          <el-button type="primary" @click="applyFilters">应用筛选</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </div>
      </div>
    </el-collapse-transition>

    <!-- 主要内容区域 -->
    <el-container class="main-container">
      <!-- 左侧汇总面板 -->
      <el-aside width="280px" class="summary-panel">
        <div class="summary-header">
          <h3>数据汇总</h3>
          <el-select v-model="summaryType" placeholder="选择汇总类型" @change="fetchSummaryData">
            <el-option label="按日" value="day"></el-option>
            <el-option label="按周" value="week"></el-option>
            <el-option label="按月" value="month"></el-option>
            <el-option label="按年" value="year"></el-option>
          </el-select>
        </div>
        
        <el-scrollbar class="summary-scrollbar">
          <div class="summary-content">
            <!-- 汇总图表 -->
            <div class="summary-chart">
              <div ref="summaryChart" style="width: 100%; height: 200px;"></div>
            </div>
            
            <!-- 汇总列表 -->
            <div class="summary-list">
              <div 
                v-for="(item, index) in summaryData" 
                :key="index" 
                class="summary-item"
                :class="{ active: selectedSummaryItem === index }"
                @click="selectSummaryItem(index, item)"
              >
                <div class="summary-item-date">{{ formatSummaryDate(item.date, summaryType) }}</div>
                <div class="summary-item-amounts">
                  <span class="income-amount">{{ formatCurrency(item.income) }}</span>
                  <span class="expense-amount">{{ formatCurrency(item.expense) }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </el-aside>
      
      <!-- 右侧流水列表 -->
      <el-main class="records-panel">
  <el-table
    v-loading="loading"
    :data="recordsList"
    style="width: 100%"
    @selection-change="handleSelectionChange"
  >
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="date" label="日期" width="100" sortable>
      <template #default="{row}">
        {{ row && row.date ? formatDate(row.date) : '-' }}
      </template>
    </el-table-column>
          <el-table-column prop="type" label="类型" width="80">
            <template #default="{row}">
              <el-tag v-if="row" :type="row.type == '收入' ? 'success' : 'danger'">
                {{ row && row.type == '收入' ? '收入' : '支出' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="categoryName" label="分类" width="120">
            <template #default="{row}">
              <span v-if="row">{{ row.categoryName || '-' }}</span>
              <span v-if="row && row.childCategoryName"> / {{ row.childCategoryName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="120" sortable>
            <template #default="{row}">
              <span v-if="row" :class="row.type == '收入' ? 'income-text' : 'expense-text'">
                {{ formatCurrency(row.amount) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="merchantName" label="商家" width="120">
            <template #default="{row}">
              {{ row && row.merchantName ? row.merchantName : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="accountName" label="账户" width="120">
            <template #default="{row}">
              {{ row && row.accountCategoryName ? row.accountCategoryName : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" min-width="150">
            <template #default="{row}">
              {{ row && row.desc ? row.desc : '-' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="scope">

              <el-button type="text" size="small" class="delete-btn" @click="confirmDeleteRecord(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="totalRecords"
          ></el-pagination>
        </div>
      </el-main>
    </el-container>

    <!-- 记录详情对话框 -->
    <el-dialog
      title="记录详情"
      :visible.sync="recordDetailVisible"
      width="30%"
    >
      <div v-if="selectedRecord" class="record-detail">
        <div class="detail-item">
          <span class="label">日期:</span>
          <span class="value">{{ formatDate(selectedRecord.date) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">类型:</span>
          <span class="value">
            <el-tag :type="selectedRecord.type == 1 ? 'success' : 'danger'">
              {{ selectedRecord.type == 1 ? '收入' : '支出' }}
            </el-tag>
          </span>
        </div>
        <div class="detail-item">
          <span class="label">分类:</span>
          <span class="value">
            {{ selectedRecord.categoryName }}
            <span v-if="selectedRecord.childCategoryName"> / {{ selectedRecord.childCategoryName }}</span>
          </span>
        </div>
        <div class="detail-item">
          <span class="label">金额:</span>
          <span class="value" :class="selectedRecord.type == 1 ? 'income-text' : 'expense-text'">
            {{ formatCurrency(selectedRecord.amount) }}
          </span>
        </div>
        <div class="detail-item">
          <span class="label">商家:</span>
          <span class="value">{{ selectedRecord.merchantName || '-' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">账户:</span>
          <span class="value">{{ selectedRecord.accountName || '-' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">备注:</span>
          <span class="value">{{ selectedRecord.remark || '-' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">创建时间:</span>
          <span class="value">{{ formatDateTime(selectedRecord.createTime) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">更新时间:</span>
          <span class="value">{{ formatDateTime(selectedRecord.updateTime) }}</span>
        </div>
      </div>
    </el-dialog>

    <!-- 记录抽屉 -->
    <RecordDrawer
      v-if="showRecordDrawer"
      :visible="showRecordDrawer"
      :recordData="editingRecord"
      :isEdit="isEditMode"
      :bookId="bookId"
      @close="closeRecordDrawer"
      @save="handleSaveRecord"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts';
import generalTableApi from '@/api/generalTable.js';
import categoryApi from '@/api/category.js';
import merchantApi from '@/api/merchant.js';
import accountApi from '@/api/account.js';
import RecordDrawer from '../RecordDrawer.vue';

export default {
  name: 'TransactionsPage',
  components: {
    RecordDrawer
  },
  props: {
    bookId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      // 顶部统计数据
      totalBalance: 0,
      totalIncome: 0,
      totalExpense: 0,
      
      // 筛选相关
      showFilters: false,
      searchKeyword: '',
      dateRange: [],
      filterType: '',
      filterCategory: [],
      filterMerchant: '',
      filterAccount: '',
      
      // 选项数据
      categoryOptions: [],
      merchantOptions: [],
      accountOptions: [],
      
      // 表格数据
      loading: false,
      recordsList: [],
      selectedRecords: [],
      currentPage: 1,
      pageSize: 10,
      totalRecords: 0,
      
      // 汇总数据
      summaryType: 'month',
      summaryData: [],
      selectedSummaryItem: null,
      summaryChart: null,
      
      // 详情对话框
      recordDetailVisible: false,
      selectedRecord: null,
      
      // 记录抽屉
      showRecordDrawer: false,
      isEditMode: false,
      editingRecord: null
    };
  },
  created() {
    this.fetchInitialData();
  },
  mounted() {
    this.initSummaryChart();
  },
  methods: {
    // 初始化数据
    async fetchInitialData() {
      this.loading = true;
      try {
        // 并行获取多个数据
        await Promise.all([
          this.fetchRecordsData(),
          this.fetchCategoryOptions(),
          this.fetchMerchantOptions(),
          this.fetchAccountOptions(),
          this.fetchSummaryData()
        ]);
      } catch (error) {
        console.error('初始化数据失败:', error);
        this.$message.error('获取数据失败，请稍后重试');
      } finally {
        this.loading = false;
      }
    },
    
    // 刷新数据（供父组件调用）
    refreshData() {
      this.fetchRecordsData();
      this.fetchSummaryData();
    },
    
    // 获取流水记录数据
    async fetchRecordsData() {
      console.log('Fetching records. User ID:', localStorage.getItem('userId'), 'Book ID:', this.bookId); // 添加日志确认 ID
      try {
        const userId = localStorage.getItem('userId');
        if (!userId || !this.bookId) {
          console.error('User ID 或 Book ID 缺失，终止获取流水记录。'); // 添加错误日志
          this.recordsList = []; // 确保列表为空
          this.totalRecords = 0;
          this.calculateTotals(); // 即使失败也计算一次总额（会是0）
          return;

        }
        
        const params = {
          bookId: this.bookId,
          userId: userId,
          page: this.currentPage,
          pageSize: this.pageSize
        };
        
        // 添加筛选条件
        if (this.searchKeyword) {
          params.keyword = this.searchKeyword;
        }
        
        if (this.dateRange && this.dateRange.length === 2) {
          params.startDate = this.dateRange[0];
          params.endDate = this.dateRange[1];
        }
        
        if (this.filterType) {
          params.type = this.filterType;
        }
        
        if (this.filterCategory && this.filterCategory.length > 0) {
          if (this.filterCategory.length === 1) {
            params.categoryId = this.filterCategory[0];
          } else if (this.filterCategory.length === 2) {
            params.categoryId = this.filterCategory[0];
            params.childCategoryId = this.filterCategory[1];
          }
        }
        
        if (this.filterMerchant) {
          params.merchantId = this.filterMerchant;
        }
        
        if (this.filterAccount) {
          params.accountId = this.filterAccount;
        }
        
        console.log('请求参数:', params); // 添加请求参数日志
        const response = await generalTableApi.searchGeneralTables(params);
        console.log('完整响应:', response); // 打印完整响应
        
        if (response.data && response.data.code === 200) {
  // 检查返回的数据结构
  if (Array.isArray(response.data.data)) {
    // 直接使用数组
    this.recordsList = [...response.data.data];
    this.totalRecords = response.data.data.length;
  } else if (response.data.data && response.data.data.records) {
    // 使用分页对象中的records数组
    const records = response.data.data.records || [];
    this.recordsList = Array.isArray(records) ? [...records] : [];
    this.totalRecords = response.data.data.total || 0;
  } else {
    // 处理其他可能的数据结构
    console.error('未预期的数据结构:', response.data.data);
    this.recordsList = [];
    this.totalRecords = 0;
  }
  console.log('处理后的表格数据:', this.recordsList);
  this.calculateTotals();
} else {
      console.error('API返回异常:', response.data);
    }
      } catch (error) {
        console.error('获取流水记录失败:', error);
        this.$message.error('获取流水记录失败，请稍后重试');
      }
    },
    
    // 获取分类选项
    async fetchCategoryOptions() {
      try {
        const userId = localStorage.getItem('userId');
        if (!userId || !this.bookId) {
          return;
        }
        
        const response = await categoryApi.getAllCategories(this.bookId, userId);
        
        if (response.data && response.data.code === 200) {
          this.processCategoryOptions(response.data.data);
        }
      } catch (error) {
        console.error('获取分类选项失败:', error);
      }
    },
    
    // 处理分类选项数据结构
    processCategoryOptions(categories) {
      if (!categories || !Array.isArray(categories)) {
        return;
      }
      
      // 筛选出父分类
      const parentCategories = categories.filter(item => !item.parentId);
      
      this.categoryOptions = parentCategories.map(parent => {
        // 查找当前父分类的所有子分类
        const children = categories.filter(item => item.parentId === parent.id);
        
        return {
          value: parent.id,
          label: parent.name,
          children: children.map(child => ({
            value: child.id,
            label: child.name
          }))
        };
      });
    },
    
    // 获取商家选项
    async fetchMerchantOptions() {
      try {
        const userId = localStorage.getItem('userId');
        if (!userId || !this.bookId) {
          return;
        }
        
        const response = await merchantApi.getMerchantList(this.bookId, userId);
        
        if (response.data && response.data.code === 200) {
          this.merchantOptions = response.data.data.records || [];
        }
      } catch (error) {
        console.error('获取商家选项失败:', error);
      }
    },
    
    // 获取账户选项
    async fetchAccountOptions() {
      try {
        const userId = localStorage.getItem('userId');
        if (!userId || !this.bookId) {
          return;
        }
        
        // 假设有accountApi，如果没有需要实现
        const response = await accountApi.getAccountList(this.bookId, userId);
        
        if (response.data && response.data.code === 200) {
          this.accountOptions = response.data.data || [];
        }
      } catch (error) {
        console.error('获取账户选项失败:', error);
      }
    },
    
    // 获取汇总数据
    async fetchSummaryData() {
      
      try {
        const userId = localStorage.getItem('userId');
        if (!userId || !this.bookId) {
          return;
        }
        
        // 设置默认日期范围为最近一年
        const endDate = new Date();
        const startDate = new Date();
        startDate.setFullYear(endDate.getFullYear() - 1);
        
        const formattedStartDate = this.formatDateForApi(startDate);
        const formattedEndDate = this.formatDateForApi(endDate);
        
        const response = await generalTableApi.getSummaryByDate(
          this.bookId,
          userId,
          this.summaryType,
          formattedStartDate,
          formattedEndDate
        );
        
        if (response.data && response.data.code === 200) {
          this.summaryData = response.data.data || [];
          this.updateSummaryChart();
        }
      } catch (error) {
        console.error('获取汇总数据失败:', error);
      }
    },
    
    // 初始化汇总图表
    initSummaryChart() {
      if (this.$refs.summaryChart) {
        this.summaryChart = echarts.init(this.$refs.summaryChart);
        this.updateSummaryChart();
      }
    },
    
    // 更新汇总图表
    updateSummaryChart() {
      if (!this.summaryChart || !this.summaryData.length) {
        return;
      }
      
      const dates = this.summaryData.map(item => this.formatSummaryDate(item.date, this.summaryType));
      const incomes = this.summaryData.map(item => item.income);
      const expenses = this.summaryData.map(item => item.expense);
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
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
          data: dates,
          axisLabel: {
            interval: 0,
            rotate: 30,
            formatter: function(value) {
              if (value.length > 4) {
                return value.substring(0, 4) + '...';
              }
              return value;
            }
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '收入',
            type: 'bar',
            stack: 'total',
            data: incomes,
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '支出',
            type: 'bar',
            stack: 'total',
            data: expenses,
            itemStyle: {
              color: '#F56C6C'
            }
          }
        ]
      };
      
      this.summaryChart.setOption(option);
    },
    
    // 计算总额
    calculateTotals() {
      // 重置总额
      this.totalIncome = 0;
      this.totalExpense = 0;
      
      // 遍历记录计算总收入和总支出
      this.recordsList.forEach(record => {
        if (record.type == '收入') { // 收入
          this.totalIncome += parseFloat(record.amount || 0);
        } else if (record.type == '支出') { // 支出
          this.totalExpense += parseFloat(record.amount || 0);
        }
      });
      
      // 计算结余
      this.totalBalance = this.totalIncome + this.totalExpense;
    },
    
    // 处理搜索
    handleSearch() {
      this.currentPage = 1; // 重置到第一页
      this.fetchRecordsData();
    },
    
    // 应用筛选
    applyFilters() {
      const params = {
        bid: this.currentBookId,
        userId: this.userId
      };
      
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.formatDate(this.dateRange[0]);
        params.endDate = this.formatDate(this.dateRange[1]);
      }
      
    },
    
    // 重置筛选
    resetFilters() {
      this.dateRange = [];
      this.filterType = '';
      this.filterCategory = [];
      this.filterMerchant = '';
      this.filterAccount = '';
      this.searchKeyword = '';
      this.currentPage = 1;
      this.fetchRecordsData();
    },
    
    // 处理页码变化
    handleCurrentChange(page) {
      this.currentPage = page;
      this.fetchRecordsData();
    },
    
    // 处理每页条数变化
    handleSizeChange(size) {
      this.pageSize = size;
      this.currentPage = 1; // 重置到第一页
      this.fetchRecordsData();
    },
    
    // 处理选择记录变化
    handleSelectionChange(selection) {
      this.selectedRecords = selection;
    },
    
    // 查看记录详情
    viewRecordDetail(record) {
      this.selectedRecord = record;
      this.recordDetailVisible = true;
    },
    
    // 编辑记录
    editRecord(record) {
      this.isEditMode = true;
      this.editingRecord = JSON.parse(JSON.stringify(record)); // 深拷贝避免直接修改
      this.showRecordDrawer = true;
    },
    
    // 打开添加记录抽屉
    openAddRecordDrawer() {
      this.isEditMode = false;
      this.editingRecord = {
        type: '支出', // 默认为支出
        date: this.formatDateForApi(new Date()) // 默认为今天
      };
      this.showRecordDrawer = true;
    },
    
    // 关闭记录抽屉
    closeRecordDrawer() {
      this.showRecordDrawer = false;
      this.editingRecord = null;
    },
    
    
    // 确认删除记录
    confirmDeleteRecord(record) {
      this.$confirm('确定要删除这条记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRecord(record.id);
      }).catch(() => {
        // 取消删除
      });
    },
    
    // 删除记录
    async deleteRecord(id) {
      try {
        const userId = localStorage.getItem('userId');
        const response = await generalTableApi.deleteGeneralTable(id, userId);
        
        if (response.data && response.data.code === 200) {
          this.$message.success('删除成功');
          this.fetchRecordsData();
          this.fetchSummaryData();
        } else {
          this.$message.error(response.data.message || '删除失败');
        }
      } catch (error) {
        console.error('删除记录失败:', error);
        this.$message.error('删除记录失败，请稍后重试');
      }
    },
    
    // 确认批量删除
    confirmBatchDelete() {
      if (this.selectedRecords.length === 0) {
        return;
      }
      
      this.$confirm(`确定要删除选中的 ${this.selectedRecords.length} 条记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.batchDeleteRecords();
      }).catch(() => {
        // 取消删除
      });
    },
    


    
    // 格式化日期为API所需格式 (YYYY-MM-DD)
    formatDateForApi(date) {
  if (!date) return '';
  
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  
  return `${year}-${month}-${day}`;
},
    
    // 格式化日期显示 (YYYY-MM-DD)
    formatDate(dateStr) {
      if (!dateStr) return '-';
      
      // 如果已经是格式化的字符串，直接返回
      if (typeof dateStr === 'string' && dateStr.match(/^\d{4}-\d{2}-\d{2}$/)) {
        return dateStr;
      }
      
      const date = new Date(dateStr);
      return this.formatDateForApi(date);
    },
    
    // 格式化日期时间显示
    formatDateTime(dateTimeStr) {
      if (!dateTimeStr) return '-';
      
      const date = new Date(dateTimeStr);
      const formattedDate = this.formatDateForApi(date);
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      
      return `${formattedDate} ${hours}:${minutes}:${seconds}`;
    },
    
    // 格式化汇总日期显示
    formatSummaryDate(dateStr, type) {
      if (!dateStr) return '-';
      
      // 确保日期字符串格式正确
      let date;
      if (typeof dateStr === 'string') {
        // 处理可能的格式问题
        if (dateStr.includes('T')) {
          // ISO格式日期
          date = new Date(dateStr);
        } else if (dateStr.includes('-')) {
          // YYYY-MM-DD 格式
          const parts = dateStr.split('-');
          if (parts.length >= 3) {
            date = new Date(parts[0], parts[1] - 1, parts[2]);
          } else if (parts.length === 2) {
            date = new Date(parts[0], parts[1] - 1, 1);
          } else {
            date = new Date(parts[0], 0, 1);
          }
        } else {
          // 尝试直接解析
          date = new Date(dateStr);
        }
      } else {
        // 如果不是字符串，尝试直接创建日期对象
        date = new Date(dateStr);
      }
      
      // 检查日期是否有效
      if (isNaN(date.getTime())) {
        console.error('无效的日期:', dateStr);
        return '-';
      }
      
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      
      switch (type) {
        case 'day':
          return `${year}年${month}月${day}日`;
        case 'week':
          return `${year}年第${this.getWeekNumber(date)}周`;
        case 'month':
          return `${year}年${month}月`;
        case 'year':
          return `${year}年`;
        default:
          return `${year}年${month}月${day}日`;
      }
    },
    
    // 格式化日期显示 (yyyy-MM-dd)
    formatDate(date) {
      if (!date) return '-';
      if (typeof date === 'string') {
        // 如果已经是字符串格式，检查是否需要截取
        if (date.includes('T') || date.includes(' ')) {
          // 处理ISO格式或带时间的日期字符串
          return date.split('T')[0].split(' ')[0];
        }
        return date;
      }
      
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    
    // 格式化汇总日期显示
    formatSummaryDate(date, type) {
      if (!date) return '-';
      
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      
      switch (type) {
        case 'day':
          return `${year}-${month}-${day}`;
        case 'week':
          return `${year}年第${this.getWeekNumber(d)}周`;
        case 'month':
          return `${year}-${month}`;
        case 'year':
          return `${year}年`;
        default:
          return `${year}-${month}-${day}`;
      }
    },
    
    // 获取日期是一年中的第几周
    getWeekNumber(date) {
      const d = new Date(date);
      d.setHours(0, 0, 0, 0);
      d.setDate(d.getDate() + 3 - (d.getDay() + 6) % 7);
      const week1 = new Date(d.getFullYear(), 0, 4);
      return 1 + Math.round(((d - week1) / 86400000 - 3 + (week1.getDay() + 6) % 7) / 7);
    },
    
    // 获取周的开始日期
    getWeekStartDate(date) {
      const d = new Date(date);
      const day = d.getDay() || 7; // 如果是周日，getDay()返回0，我们将其视为7
      if (day !== 1) {
        d.setDate(d.getDate() - day + 1);
      }
      return this.formatDateForApi(d);
    },
    
    // 获取周的结束日期
    getWeekEndDate(date) {
      const d = new Date(date);
      const day = d.getDay() || 7;
      if (day !== 7) {
        d.setDate(d.getDate() + (7 - day));
      }
      return this.formatDateForApi(d);
    },
    
    // 获取月的开始日期
    getMonthStartDate(date) {
      const d = new Date(date);
      d.setDate(1);
      return this.formatDateForApi(d);
    },
    
    // 获取月的结束日期
    getMonthEndDate(date) {
      const d = new Date(date);
      d.setMonth(d.getMonth() + 1);
      d.setDate(0);
      return this.formatDateForApi(d);
    },
    
    // 获取年的开始日期
    getYearStartDate(date) {
      const d = new Date(date);
      d.setMonth(0);
      d.setDate(1);
      return this.formatDateForApi(d);
    },
    
    // 获取年的结束日期
    getYearEndDate(date) {
      const d = new Date(date);
      d.setFullYear(d.getFullYear() + 1);
      d.setMonth(0);
      d.setDate(0);
      return this.formatDateForApi(d);
    },
    
    // 格式化货币显示
    formatCurrency(value) {
      if (value === undefined || value === null) return '¥0.00';
      
      const num = parseFloat(value);
      return `¥${num.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')}`;
    },

    // 选择汇总项
    selectSummaryItem(index, item) {
      this.selectedSummaryItem = index;
      
      // 根据所选汇总项筛选数据
      let startDate, endDate;
      
      if (this.summaryType === 'day') {
        startDate = item.date;
        endDate = item.date;
      } else if (this.summaryType === 'week') {
        // 周的开始日期和结束日期
        startDate = item.startDate;
        endDate = item.endDate;
      } else if (this.summaryType === 'month') {
        // 月的开始日期和结束日期
        const date = new Date(item.date);
        startDate = this.formatDateForApi(new Date(date.getFullYear(), date.getMonth(), 1));
        endDate = this.formatDateForApi(new Date(date.getFullYear(), date.getMonth() + 1, 0));
      } else if (this.summaryType === 'year') {
        // 年的开始日期和结束日期
        const year = item.date;
        startDate = `${year}-01-01`;
        endDate = `${year}-12-31`;
      }
      
      this.dateRange = [startDate, endDate];
      this.applyFilters();
    },

    
    // 处理保存记录
    async handleSaveRecord(recordData) {
      try {
        const userId = localStorage.getItem('userId');
        
        if (this.isEditMode) {
          // 更新记录
          recordData.userId = userId;
          const response = await generalTableApi.updateGeneralTable(recordData);
          
          if (response.data.code === 200) {
            this.$message.success('更新成功');
            this.closeRecordDrawer();
            this.refreshData();
          } else {
            this.$message.error(response.data.msg || '更新失败');
          }
        } else {
          // 添加记录
          recordData.bid = this.bookId;
          recordData.userId = userId;
          
          const response = await generalTableApi.addGeneralTable(recordData);
          
          if (response.data.code === 200) {
            this.$message.success('添加成功');
            this.closeRecordDrawer();
            this.refreshData();
          } else {
            this.$message.error(response.data.msg || '添加失败');
          }
        }
      } catch (error) {
        console.error('保存记录失败:', error);
        this.$message.error('保存失败，请稍后重试');
      }
    },




    // 批量删除记录
    async batchDeleteRecords() {
      try {
        const userId = localStorage.getItem('userId');
        if (!userId || !this.bookId) {
          return;
        }

        const ids = this.selectedRecords.map(item => item.id);

        const response = await generalTableApi.batchDeleteGeneralTables(ids, userId);

        if (response.data && response.data.code === 200) {
          this.$message.success(`成功删除 ${ids.length} 条记录`);
          this.refreshData();
        } else {
          this.$message.error(response.data.message || '批量删除失败');
        }
      } catch (error) {
        console.error('批量删除记录失败:', error);
        this.$message.error('批量删除记录失败，请稍后重试');
      }
    },
    

    

  }
};
</script>

<style scoped>
.transactions-page {
  padding: 20px;
}

/* 顶部统计卡片 */
.summary-cards {
  display: flex;
  margin-bottom: 20px;
  gap: 20px;
}

.summary-card {
  flex: 1;
  text-align: center;
  padding: 15px;
}

.summary-card.balance {
  background-color: #f0f9ff;
}

.summary-card.income {
  background-color: #f0f9eb;
}

.summary-card.expense {
  background-color: #fef0f0;
}

.card-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 10px;
}

.card-amount {
  font-size: 24px;
  font-weight: bold;
}

/* 操作栏 */
.action-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.left-actions, .right-actions {
  display: flex;
  gap: 10px;
}

.search-input {
  width: 250px;
}

/* 筛选面板 */
.filter-panel {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.filter-form {
  margin-bottom: 15px;
}

.filter-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 主要内容区域 */
.main-container {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  height: calc(100vh - 300px);
  min-height: 500px;
}

/* 左侧汇总面板 */
.summary-panel {
  border-right: 1px solid #e6e6e6;
  background-color: #f5f7fa;
}

.summary-header {
  padding: 15px;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.summary-header h3 {
  margin: 0;
  font-size: 16px;
}

.summary-scrollbar {
  height: calc(100% - 60px);
}

.summary-content {
  padding: 15px;
}

.summary-chart {
  margin-bottom: 20px;
}

.summary-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.summary-item {
  padding: 10px;
  border-radius: 4px;
  background-color: #fff;
  cursor: pointer;
  transition: all 0.3s;
}

.summary-item:hover {
  background-color: #ecf5ff;
}

.summary-item.active {
  background-color: #ecf5ff;
  border-left: 3px solid #409eff;
}

.summary-item-date {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.summary-item-amounts {
  display: flex;
  justify-content: space-between;
}

.income-amount {
  color: #67c23a;
}

.expense-amount {
  color: #f56c6c;
}

/* 右侧流水列表 */
.records-panel {
  padding: 15px;
  overflow: auto;
}

.income-text {
  color: #67c23a;
}

.expense-text {
  color: #f56c6c;
}

.delete-btn {
  color: #f56c6c;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 记录详情 */
.record-detail {
  padding: 10px;
}

.detail-item {
  margin-bottom: 15px;
  display: flex;
}

.detail-item .label {
  width: 100px;
  color: #606266;
}

.detail-item .value {
  flex: 1;
  color: #303133;
}
</style>