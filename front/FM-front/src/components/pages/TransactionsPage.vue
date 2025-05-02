<template>
    <div class="transactions-container">
      <!-- 顶部统计信息 -->
      <div class="summary-header">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover" class="balance-card">
              <div class="card-title">结余</div>
              <div class="card-amount">{{ formatAmount(balance) }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" class="income-card">
              <div class="card-title">收入</div>
              <div class="card-amount">{{ formatAmount(income) }}</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover" class="expense-card">
              <div class="card-title">支出</div>
              <div class="card-amount">{{ formatAmount(expense) }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
  
      <!-- 搜索和筛选区域 -->
      <div class="search-filter-area">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索交易记录"
              prefix-icon="el-icon-search"
              @keyup.enter.native="searchRecords"
            >
              <template #append>
                <el-button @click="searchRecords">搜索</el-button>
              </template>
            </el-input>
          </el-col>
          <el-col :span="8" class="filter-buttons">
            <el-button @click="toggleFilterPanel">
              筛选
              <i :class="showFilterPanel ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
            </el-button>
            <el-button @click="resetFilters">重置</el-button>
            <el-button type="primary" @click="openAddRecordDrawer">添加记录</el-button>
          </el-col>
        </el-row>
  
        <!-- 筛选面板 -->
        <el-collapse-transition>
          <div v-show="showFilterPanel" class="filter-panel">
            <el-form :model="filterForm" label-width="80px" size="small">
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="日期范围">
                    <el-date-picker
                      v-model="filterForm.dateRange"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      format="YYYY-MM-DD"
                      value-format="YYYY-MM-DD"
                      style="width: 100%"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="类型">
                    <el-select v-model="filterForm.type" placeholder="选择类型" style="width: 100%">
                      <el-option label="全部" value=""></el-option>
                      <el-option label="收入" value="income"></el-option>
                      <el-option label="支出" value="expense"></el-option>
                      <el-option label="转账" value="transfer"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="账户">
                    <el-select v-model="filterForm.accountId" placeholder="选择账户" style="width: 100%">
                      <el-option label="全部" value=""></el-option>
                      <el-option
                        v-for="account in accounts"
                        :key="account.id"
                        :label="account.name"
                        :value="account.id"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="分类">
                    <el-select v-model="filterForm.categoryId" placeholder="选择分类" style="width: 100%">
                      <el-option label="全部" value=""></el-option>
                      <el-option
                        v-for="category in categories"
                        :key="category.id"
                        :label="category.name"
                        :value="category.id"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="成员">
                    <el-select v-model="filterForm.memberId" placeholder="选择成员" style="width: 100%">
                      <el-option label="全部" value=""></el-option>
                      <el-option
                        v-for="member in members"
                        :key="member.id"
                        :label="member.name"
                        :value="member.id"
                      ></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="金额范围">
                    <el-row :gutter="10">
                      <el-col :span="11">
                        <el-input v-model="filterForm.minAmount" placeholder="最小值"></el-input>
                      </el-col>
                      <el-col :span="2" style="text-align: center">-</el-col>
                      <el-col :span="11">
                        <el-input v-model="filterForm.maxAmount" placeholder="最大值"></el-input>
                      </el-col>
                    </el-row>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24" style="text-align: right">
                  <el-button type="primary" @click="applyFilters">应用筛选</el-button>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-collapse-transition>
      </div>
  
      <!-- 主要内容区域 -->
      <div class="main-content">
        <el-row :gutter="20">
          <!-- 左侧汇总区域 -->
          <el-col :span="6">
            <div class="summary-section">
              <div class="summary-header">
                <h3>汇总方式</h3>
                <el-radio-group v-model="summaryType" @change="changeSummaryType">
                  <el-radio-button label="year">年</el-radio-button>
                  <el-radio-button label="month">月</el-radio-button>
                  <el-radio-button label="week">周</el-radio-button>
                  <el-radio-button label="day">日</el-radio-button>
                </el-radio-group>
              </div>
              
              <div class="summary-content">
                <el-date-picker
                  v-if="summaryType === 'year'"
                  v-model="summaryDateRange"
                  type="year"
                  placeholder="选择年份"
                  @change="fetchSummaryData"
                  style="width: 100%; margin-bottom: 15px"
                ></el-date-picker>
                
                <el-date-picker
                  v-if="summaryType === 'month'"
                  v-model="summaryDateRange"
                  type="month"
                  placeholder="选择月份"
                  @change="fetchSummaryData"
                  style="width: 100%; margin-bottom: 15px"
                ></el-date-picker>
                
                <el-date-picker
                  v-if="summaryType === 'week' || summaryType === 'day'"
                  v-model="summaryDateRange"
                  :type="summaryType === 'week' ? 'week' : 'date'"
                  :placeholder="summaryType === 'week' ? '选择周' : '选择日期'"
                  @change="fetchSummaryData"
                  style="width: 100%; margin-bottom: 15px"
                ></el-date-picker>
                
                <!-- 汇总数据展示 -->
                <div v-if="summaryData.length > 0" class="summary-chart">
                  <h4>{{ getSummaryTitle() }}</h4>
                  <el-table :data="summaryData" style="width: 100%" :max-height="400">
                    <el-table-column :prop="summaryLabelField" :label="getSummaryLabelTitle()"></el-table-column>
                    <el-table-column prop="income" label="收入">
                      <template #default="scope">
                        <span class="income-text">{{ formatAmount(scope.row.income) }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="expense" label="支出">
                      <template #default="scope">
                        <span class="expense-text">{{ formatAmount(scope.row.expense) }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column prop="balance" label="结余">
                      <template #default="scope">
                        <span :class="scope.row.balance >= 0 ? 'income-text' : 'expense-text'">
                          {{ formatAmount(scope.row.balance) }}
                        </span>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
                <el-empty v-else description="暂无汇总数据"></el-empty>
              </div>
            </div>
          </el-col>
          
          <!-- 右侧流水记录区域 -->
          <el-col :span="18">
            <div class="transactions-section">
              <h3>流水记录</h3>
              <el-table
                :data="transactions"
                style="width: 100%"
                :max-height="600"
                v-loading="loading"
              >
                <el-table-column prop="date" label="日期" width="100">
                  <template #default="scope">
                    {{ formatDate(scope.row.date) }}
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="类型" width="80">
                  <template #default="scope">
                    <el-tag
                      :type="scope.row.type === 'income' ? 'success' : scope.row.type === 'expense' ? 'danger' : 'info'"
                      size="small"
                    >
                      {{ getTypeText(scope.row.type) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="categoryName" label="分类" width="120"></el-table-column>
                <el-table-column prop="accountName" label="账户" width="120"></el-table-column>
                <el-table-column prop="amount" label="金额" width="120">
                  <template #default="scope">
                    <span :class="scope.row.type === 'income' ? 'income-text' : 'expense-text'">
                      {{ formatAmount(scope.row.amount) }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="memberName" label="成员" width="100"></el-table-column>
                <el-table-column prop="merchantName" label="商家" width="120"></el-table-column>
                <el-table-column prop="projectName" label="项目" width="120"></el-table-column>
                <el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column fixed="right" label="操作" width="120">
                  <template #default="scope">
                    <el-button type="text" size="small" @click="editRecord(scope.row)">编辑</el-button>
                    <el-button type="text" size="small" @click="deleteRecord(scope.row)">删除</el-button>
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
                  :total="total"
                ></el-pagination>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- 编辑记录对话框 -->
      <el-dialog
        :title="editingRecord.id ? '编辑记录' : '添加记录'"
        v-model="showEditDialog"
        width="50%"
      >
        <el-form :model="editingRecord" label-width="80px" :rules="rules" ref="recordForm">
          <el-form-item label="类型" prop="type">
            <el-select v-model="editingRecord.type" placeholder="选择类型" style="width: 100%">
              <el-option label="收入" value="income"></el-option>
              <el-option label="支出" value="expense"></el-option>
              <el-option label="转账" value="transfer"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日期" prop="date">
            <el-date-picker
              v-model="editingRecord.date"
              type="datetime"
              placeholder="选择日期时间"
              style="width: 100%"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="金额" prop="amount">
            <el-input v-model.number="editingRecord.amount" type="number" placeholder="输入金额"></el-input>
          </el-form-item>
          <el-form-item label="分类" prop="categoryId">
            <el-select v-model="editingRecord.categoryId" placeholder="选择分类" style="width: 100%">
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="账户" prop="accountId">
            <el-select v-model="editingRecord.accountId" placeholder="选择账户" style="width: 100%">
              <el-option
                v-for="account in accounts"
                :key="account.id"
                :label="account.name"
                :value="account.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="成员" prop="memberId">
            <el-select v-model="editingRecord.memberId" placeholder="选择成员" style="width: 100%">
              <el-option
                v-for="member in members"
                :key="member.id"
                :label="member.name"
                :value="member.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商家" prop="merchantId">
            <el-select v-model="editingRecord.merchantId" placeholder="选择商家" style="width: 100%">
              <el-option
                v-for="merchant in merchants"
                :key="merchant.id"
                :label="merchant.name"
                :value="merchant.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="项目" prop="projectId">
            <el-select v-model="editingRecord.projectId" placeholder="选择项目" style="width: 100%">
              <el-option
                v-for="project in projects"
                :key="project.id"
                :label="project.name"
                :value="project.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="editingRecord.remark" type="textarea" :rows="3" placeholder="输入备注"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="showEditDialog = false">取消</el-button>
            <el-button type="primary" @click="saveRecord">保存</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import { ref, reactive, onMounted, computed, watch } from 'vue';
  import generalTableApi from '@/api/generalTable.js';
  import accountApi from '@/api/account.js';
  import categoryApi from '@/api/category.js';
  import bookMemberRequest from '@/api/bookMember.js';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import dayjs from 'dayjs';
  
  export default {
    name: 'TransactionsPage',
    props: {
      bookId: {
        type: [String, Number],
        required: true
      }
    },
    setup(props) {
      // 数据状态
      const loading = ref(false);
      const transactions = ref([]);
      const accounts = ref([]);
      const categories = ref([]);
      const members = ref([]);
      const merchants = ref([]);
      const projects = ref([]);
      const currentPage = ref(1);
      const pageSize = ref(20);
      const total = ref(0);
      const income = ref(0);
      const expense = ref(0);
      const balance = ref(0);
      
      // 搜索和筛选
      const searchKeyword = ref('');
      const showFilterPanel = ref(false);
      const filterForm = reactive({
        dateRange: [],
        type: '',
        accountId: '',
        categoryId: '',
        memberId: '',
        minAmount: '',
        maxAmount: ''
      });
      
      // 汇总相关
      const summaryType = ref('month');
      const summaryDateRange = ref(dayjs().format('YYYY-MM'));
      const summaryData = ref([]);
      const summaryLabelField = computed(() => {
        switch (summaryType.value) {
          case 'year': return 'year';
          case 'month': return 'month';
          case 'week': return 'week';
          case 'day': return 'day';
          default: return 'month';
        }
      });
      
      // 编辑记录
      const showEditDialog = ref(false);
      const editingRecord = reactive({
        id: null,
        bid: props.bookId,
        userId: localStorage.getItem('userId'),
        type: 'expense',
        date: dayjs().format('YYYY-MM-DD HH:mm:ss'),
        amount: '',
        categoryId: '',
        accountId: '',
        memberId: '',
        merchantId: '',
        projectId: '',
        remark: ''
      });
      
      // 表单验证规则
      const rules = {
        type: [{ required: true, message: '请选择类型', trigger: 'change' }],
        date: [{ required: true, message: '请选择日期', trigger: 'change' }],
        amount: [
          { required: true, message: '请输入金额', trigger: 'blur' },
          { type: 'number', message: '金额必须为数字', trigger: 'blur' }
        ],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        accountId: [{ required: true, message: '请选择账户', trigger: 'change' }]
      };
      
      // 初始化数据
      onMounted(() => {
        fetchData();
        fetchRelatedData();
        fetchSummaryData();
      });
      
      // 监听bookId变化
      watch(() => props.bookId, (newVal) => {
        if (newVal) {
          fetchData();
          fetchRelatedData();
          fetchSummaryData();
        }
      });
      
      // 获取相关数据（账户、分类、成员等）
      const fetchRelatedData = async () => {
        if (!props.bookId) return;
        
        try {
          const userId = localStorage.getItem('userId');
          
          // 获取账户列表
          const accountRes = await accountApi.getAccountList(props.bookId, userId);
          if (accountRes.data && accountRes.data.data) {
            accounts.value = accountRes.data.data;
          }
          
          // 获取分类列表
          const categoryRes = await categoryApi.getAllCategories(props.bookId, userId);
          if (categoryRes.data && categoryRes.data.data) {
            categories.value = categoryRes.data.data;
          }
          
          // 获取成员列表
          const memberRes = await bookMemberRequest.getBookMembers(props.bookId, userId);
          if (memberRes.data && memberRes.data.data) {
            members.value = memberRes.data.data;
          }
          
          // 获取商家列表 (如果有merchant API)
          try {
            const merchantRes = await axios.get(`${config.mdBaseUrl}/merchant/list`, {
              params: { bookId: props.bookId, userId },
              headers: { token: localStorage.getItem('token') }
            });
            if (merchantRes.data && merchantRes.data.data) {
              merchants.value = merchantRes.data.data;
            }
          } catch (error) {
            console.error('获取商家列表失败:', error);
          }
          
          // 获取项目列表 (如果有project API)
          try {
            const projectRes = await axios.get(`${config.mdBaseUrl}/project/list`, {
              params: { bookId: props.bookId, userId },
              headers: { token: localStorage.getItem('token') }
            });
            if (projectRes.data && projectRes.data.data) {
              projects.value = projectRes.data.data;
            }
          } catch (error) {
            console.error('获取项目列表失败:', error);
          }
          
        } catch (error) {
          console.error('获取相关数据失败:', error);
          ElMessage.error('获取相关数据失败，请重试');
        }
      };
      
      // 获取流水记录数据
      const fetchData = async () => {
        if (!props.bookId) return;
        
        loading.value = true;
        try {
          const userId = localStorage.getItem('userId');
          
          // 构建查询参数
          const params = {
            bid: props.bookId,
            userId: userId,
            page: currentPage.value,
            size: pageSize.value
          };
          
          // 添加筛选条件
          if (filterForm.dateRange && filterForm.dateRange.length === 2) {
            params.startDate = filterForm.dateRange[0];
            params.endDate = filterForm.dateRange[1];
          }
          
          if (filterForm.type) params.type = filterForm.type;
          if (filterForm.accountId) params.accountId = filterForm.accountId;
          if (filterForm.categoryId) params.categoryId = filterForm.categoryId;
          if (filterForm.memberId) params.memberId = filterForm.memberId;
          if (filterForm.minAmount) params.minAmount = filterForm.minAmount;
          if (filterForm.maxAmount) params.maxAmount = filterForm.maxAmount;
          if (searchKeyword.value) params.keyword = searchKeyword.value;
          
          const response = await generalTableApi.searchGeneralTables(params);
          
          if (response.data.code === 200) {
            const data = response.data.data;
            transactions.value = data.records || [];
            total.value = data.total || 0;
            
            // 处理数据，确保所有字段都存在
            processTransactionData();
            
            // 计算总收入和支出
            calculateSummary();
          } else {
            ElMessage.error(response.data.message || '获取流水记录失败');
          }
        } catch (error) {
          console.error('获取流水记录出错:', error);
          ElMessage.error('获取流水记录失败');
        } finally {
          loading.value = false;
        }
      };
      
      // 处理流水数据，确保所有需要的字段都存在
      const processTransactionData = () => {
        if (!transactions.value || transactions.value.length === 0) return;
        
        // 创建ID到名称的映射
        const accountMap = {};
        const categoryMap = {};
        const memberMap = {};
        const merchantMap = {};
        const projectMap = {};
        
        accounts.value.forEach(item => { accountMap[item.id] = item.name; });
        categories.value.forEach(item => { categoryMap[item.id] = item.name; });
        members.value.forEach(item => { memberMap[item.id] = item.name; });
        merchants.value.forEach(item => { merchantMap[item.id] = item.name; });
        projects.value.forEach(item => { projectMap[item.id] = item.name; });
        
        // 确保每条记录都有必要的字段
        transactions.value = transactions.value.map(item => {
          // 如果后端已经返回了名称字段，则直接使用
          // 如果没有，则使用ID映射获取名称
          return {
            ...item,
            accountName: item.accountName || accountMap[item.accountId] || '未知账户',
            categoryName: item.categoryName || categoryMap[item.categoryId] || '未知分类',
            memberName: item.memberName || memberMap[item.memberId] || '未知成员',
            merchantName: item.merchantName || merchantMap[item.merchantId] || '',
            projectName: item.projectName || projectMap[item.projectId] || ''
          };
        });
      };
      
      // 计算总收入和支出
      const calculateSummary = () => {
        let totalIncome = 0;
        let totalExpense = 0;
        
        transactions.value.forEach(item => {
          if (item.type === 'income') {
            totalIncome += parseFloat(item.amount);
          } else if (item.type === 'expense') {
            totalExpense += parseFloat(item.amount);
          }
        });
        
        income.value = totalIncome;
        expense.value = totalExpense;
        balance.value = totalIncome - totalExpense;
      };
      
      // 获取汇总数据
      const fetchSummaryData = async () => {
        if (!props.bookId) return;
        
        try {
          const userId = localStorage.getItem('userId');
          let startDate, endDate;
          
          // 根据汇总类型和选择的日期范围确定开始和结束日期
          if (summaryDateRange.value) {
            const date = dayjs(summaryDateRange.value);
            
            switch (summaryType.value) {
              case 'year':
                startDate = date.startOf('year').format('YYYY-MM-DD');
                endDate = date.endOf('year').format('YYYY-MM-DD');
                break;
              case 'month':
                startDate = date.startOf('month').format('YYYY-MM-DD');
                endDate = date.endOf('month').format('YYYY-MM-DD');
                break;
              case 'week':
                startDate = date.startOf('week').format('YYYY-MM-DD');
                endDate = date.endOf('week').format('YYYY-MM-DD');
                break;
              case 'day':
                startDate = date.format('YYYY-MM-DD');
                endDate = date.format('YYYY-MM-DD');
                break;
            }
          } else {
            // 默认为当前月
            const date = dayjs();
            startDate = date.startOf('month').format('YYYY-MM-DD');
            endDate = date.endOf('month').format('YYYY-MM-DD');
          }
          
          const response = await generalTableApi.getSummaryByDate(
            props.bookId,
            userId,
            summaryType.value,
            startDate,
            endDate
          );
          
          if (response.data.code === 200) {
            summaryData.value = response.data.data;
          } else {
            ElMessage.error(response.data.message || '获取汇总数据失败');
          }
        } catch (error) {
          console.error('获取汇总数据出错:', error);
          ElMessage.error('获取汇总数据失败');
        }
      };
      
      // 切换筛选面板
      const toggleFilterPanel = () => {
        showFilterPanel.value = !showFilterPanel.value;
      };
      
      // 重置筛选条件
      const resetFilters = () => {
        Object.keys(filterForm).forEach(key => {
          if (key === 'dateRange') {
            filterForm[key] = [];
          } else {
            filterForm[key] = '';
          }
        });
        searchKeyword.value = '';
        fetchData();
      };
      
      // 应用筛选条件
      const applyFilters = () => {
        currentPage.value = 1;
        fetchData();
      };
      
      // 搜索记录
      const searchRecords = () => {
        currentPage.value = 1;
        fetchData();
      };
      
      // 分页处理
      const handleSizeChange = (size) => {
        pageSize.value = size;
        fetchData();
      };
      
      const handleCurrentChange = (page) => {
        currentPage.value = page;
        fetchData();
      };
      
      // 编辑记录
      const editRecord = (record) => {
        Object.keys(editingRecord).forEach(key => {
          if (key in record) {
            editingRecord[key] = record[key];
          }
        });
        showEditDialog.value = true;
      };
      
      // 打开添加记录抽屉
      const openAddRecordDrawer = () => {
        // 重置编辑记录
        Object.keys(editingRecord).forEach(key => {
          if (key === 'id') {
            editingRecord[key] = null;
          } else if (key === 'bid') {
            editingRecord[key] = props.bookId;
          } else if (key === 'userId') {
            editingRecord[key] = localStorage.getItem('userId');
          } else if (key === 'type') {
            editingRecord[key] = 'expense';
          } else if (key === 'date') {
            editingRecord[key] = dayjs().format('YYYY-MM-DD HH:mm:ss');
          } else {
            editingRecord[key] = '';
          }
        });
        showEditDialog.value = true;
      };
      
      // 保存记录
      const saveRecord = async () => {
        try {
          if (editingRecord.id) {
            // 更新记录
            const response = await generalTableApi.updateGeneralTable(editingRecord);
            if (response.data.code === 200) {
              ElMessage.success('更新记录成功');
              showEditDialog.value = false;
              fetchData();
            } else {
              ElMessage.error(response.data.message || '更新记录失败');
            }
          } else {
            // 添加记录
            const response = await generalTableApi.addGeneralTable(editingRecord);
            if (response.data.code === 200) {
              ElMessage.success('添加记录成功');
              showEditDialog.value = false;
              fetchData();
            } else {
              ElMessage.error(response.data.message || '添加记录失败');
            }
          }
        } catch (error) {
          console.error('保存记录出错:', error);
          ElMessage.error('保存记录失败');
        }
      };
      
      // 删除记录
      const deleteRecord = (record) => {
        ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          try {
            const userId = localStorage.getItem('userId');
            const response = await generalTableApi.deleteGeneralTable(record.id, userId);
            if (response.data.code === 200) {
              ElMessage.success('删除记录成功');
              fetchData();
            } else {
              ElMessage.error(response.data.message || '删除记录失败');
            }
          } catch (error) {
            console.error('删除记录出错:', error);
            ElMessage.error('删除记录失败');
          }
        }).catch(() => {
          // 取消删除
        });
      };
      
      // 更改汇总类型
      const changeSummaryType = () => {
        // 根据汇总类型重置日期选择器的值
        switch (summaryType.value) {
          case 'year':
            summaryDateRange.value = dayjs().format('YYYY');
            break;
          case 'month':
            summaryDateRange.value = dayjs().format('YYYY-MM');
            break;
          case 'week':
          case 'day':
            summaryDateRange.value = dayjs().format('YYYY-MM-DD');
            break;
        }
        fetchSummaryData();
      };
      
      // 获取汇总标题
      const getSummaryTitle = () => {
        switch (summaryType.value) {
          case 'year': return '年度汇总';
          case 'month': return '月度汇总';
          case 'week': return '周汇总';
          case 'day': return '日汇总';
          default: return '汇总数据';
        }
      };
      
      // 获取汇总标签标题
      const getSummaryLabelTitle = () => {
        switch (summaryType.value) {
          case 'year': return '年份';
          case 'month': return '月份';
          case 'week': return '周';
          case 'day': return '日期';
          default: return '时间';
        }
      };
      
      // 格式化金额
      const formatAmount = (amount) => {
        return parseFloat(amount).toFixed(2);
      };
      
      // 格式化日期
      const formatDate = (date) => {
        return dayjs(date).format('YYYY-MM-DD');
      };
      
      // 获取类型文本
      const getTypeText = (type) => {
        switch (type) {
          case 'income': return '收入';
          case 'expense': return '支出';
          case 'transfer': return '转账';
          default: return '未知';
        }
      };
      
      return {
        loading,
        transactions,
        accounts,
        categories,
        members,
        merchants,
        projects,
        currentPage,
        pageSize,
        total,
        income,
        expense,
        balance,
        searchKeyword,
        showFilterPanel,
        filterForm,
        summaryType,
        summaryDateRange,
        summaryData,
        summaryLabelField,
        showEditDialog,
        editingRecord,
        rules,
        toggleFilterPanel,
        resetFilters,
        applyFilters,
        searchRecords,
        handleSizeChange,
        handleCurrentChange,
        editRecord,
        openAddRecordDrawer,
        saveRecord,
        deleteRecord,
        changeSummaryType,
        getSummaryTitle,
        getSummaryLabelTitle,
        formatAmount,
        formatDate,
        getTypeText
      };
    }
  };
  </script>
  
  <style scoped>
  .transactions-container {
    padding: 20px;
  }
  
  .summary-header {
    margin-bottom: 20px;
  }
  
  .balance-card {
    background-color: #f0f9ff;
  }
  
  .income-card {
    background-color: #f0fff0;
  }
  
  .expense-card {
    background-color: #fff0f0;
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
  
  .search-filter-area {
    margin-bottom: 20px;
  }
  
  .filter-buttons {
    display: flex;
    justify-content: flex-end;
  }
  
  .filter-panel {
    margin-top: 15px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;
  }
  
  .main-content {
    margin-top: 20px;
  }
  
  .summary-section {
    background-color: #fff;
    border-radius: 4px;
    padding: 15px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    height: 100%;
  }
  
  .summary-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }
  
  .summary-content {
    margin-top: 15px;
  }
  
  .summary-chart {
    margin-top: 15px;
  }
  
  .transactions-section {
    background-color: #fff;
    border-radius: 4px;
    padding: 15px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .income-text {
    color: #67c23a;
  }
  
  .expense-text {
    color: #f56c6c;
  }
  </style>