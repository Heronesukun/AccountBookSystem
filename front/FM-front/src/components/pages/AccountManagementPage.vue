<template>
    <div class="account-management-container">
      <div class="page-header">
        <h2>账户管理</h2>
        <el-button type="primary" @click="showAddAccountDialog">添加账户</el-button>
      </div>
  
      <!-- 账户标签页 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="资产账户" name="ASSET">
          <div class="account-list">
            <!-- 资产账户列表 -->
            <div v-for="parent in assetAccounts" :key="parent.id" class="account-group">
              <div class="parent-account">
                <div class="account-info">
                  <span class="account-icon">
                    <font-awesome-icon v-if="getFontAwesomeIcon(parent.icon)" :icon="getFontAwesomeIcon(parent.icon)" />
                    <span v-else>💰</span>
                  </span>
                  <span class="account-name">{{ parent.name }}</span>
                </div>
                <div class="account-actions">
                  <el-button size="small" @click="editAccount(parent)">编辑</el-button>
                  <el-button size="small" type="danger" @click="confirmDelete(parent)" :disabled="parent.isSystem === 1">删除</el-button>
                  <el-button size="small" type="success" @click="showAddChildDialog(parent)">添加子账户</el-button>
                </div>
              </div>
              <div class="child-accounts">
                <div v-for="child in getChildAccounts(parent.id)" :key="child.id" class="child-account">
                  <div class="account-info">
                    <span class="account-icon">
                      <font-awesome-icon v-if="getFontAwesomeIcon(child.icon)" :icon="getFontAwesomeIcon(child.icon)" />
                      <span v-else>💳</span>
                    </span>
                    <span class="account-name">{{ child.name }}</span>
                    <span class="account-amount">{{ formatAmount(child.assetAmount) }}</span>
                  </div>
                  <div class="account-actions">
                    <el-button size="small" @click="editAccount(child)">编辑</el-button>
                    <el-button size="small" type="danger" @click="confirmDelete(child)" :disabled="child.isSystem === 1">删除</el-button>
                  </div>
                </div>
                <div v-if="getChildAccounts(parent.id).length === 0" class="no-child">
                  <span class="empty-text">暂无子账户</span>
                </div>
              </div>
            </div>
            <div v-if="assetAccounts.length === 0" class="empty-account">
              <el-empty description="暂无资产账户" />
            </div>
          </div>
        </el-tab-pane>
  
        <el-tab-pane label="负债账户" name="LIABILITY">
          <div class="account-list">
            <!-- 负债账户列表 -->
            <div v-for="parent in liabilityAccounts" :key="parent.id" class="account-group">
              <div class="parent-account">
                <div class="account-info">
                  <span class="account-icon">
                    <font-awesome-icon v-if="getFontAwesomeIcon(parent.icon)" :icon="getFontAwesomeIcon(parent.icon)" />
                    <span v-else>💸</span>
                  </span>
                  <span class="account-name">{{ parent.name }}</span>
                </div>
                <div class="account-actions">
                  <el-button size="small" @click="editAccount(parent)">编辑</el-button>
                  <el-button size="small" type="danger" @click="confirmDelete(parent)" :disabled="parent.isSystem === 1">删除</el-button>
                  <el-button size="small" type="success" @click="showAddChildDialog(parent)">添加子账户</el-button>
                </div>
              </div>
              <div class="child-accounts">
                <div v-for="child in getChildAccounts(parent.id)" :key="child.id" class="child-account">
                  <div class="account-info">
                    <span class="account-icon">
                      <font-awesome-icon v-if="getFontAwesomeIcon(child.icon)" :icon="getFontAwesomeIcon(child.icon)" />
                      <span v-else>💳</span>
                    </span>
                    <span class="account-name">{{ child.name }}</span>
                    <span class="account-amount">{{ formatAmount(child.assetAmount) }}</span>
                  </div>
                  <div class="account-actions">
                    <el-button size="small" @click="editAccount(child)">编辑</el-button>
                    <el-button size="small" type="danger" @click="confirmDelete(child)" :disabled="child.isSystem === 1">删除</el-button>
                  </div>
                </div>
                <div v-if="getChildAccounts(parent.id).length === 0" class="no-child">
                  <span class="empty-text">暂无子账户</span>
                </div>
              </div>
            </div>
            <div v-if="liabilityAccounts.length === 0" class="empty-account">
              <el-empty description="暂无负债账户" />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
  
      <!-- 净资产卡片 -->
      <div class="net-worth-card">
        <el-card>
          <div class="net-worth-content">
            <div class="net-worth-item">
              <div class="net-worth-label">总资产</div>
              <div class="net-worth-value asset-value">{{ formatAmount(netWorth.totalAssets) }}</div>
            </div>
            <div class="net-worth-item">
              <div class="net-worth-label">总负债</div>
              <div class="net-worth-value liability-value">{{ formatAmount(netWorth.totalLiabilities) }}</div>
            </div>
            <div class="net-worth-item">
              <div class="net-worth-label">净资产</div>
              <div class="net-worth-value net-value">{{ formatAmount(netWorth.netWorth) }}</div>
            </div>
          </div>
        </el-card>
      </div>
  
      <!-- 添加/编辑账户对话框 -->
      <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="800px"
      >
        <el-form :model="accountForm" :rules="rules" ref="accountFormRef" label-width="100px">
          <el-form-item label="账户名称" prop="name">
            <el-input v-model="accountForm.name" placeholder="请输入账户名称"></el-input>
          </el-form-item>
          <el-form-item label="账户图标" prop="icon">
            <div class="icon-selector">
              <el-input v-model="accountForm.icon" placeholder="请输入账户图标"></el-input>
              <div class="icon-preview" v-if="accountForm.icon">
                <span>预览：</span>
                <font-awesome-icon v-if="getFontAwesomeIcon(accountForm.icon)" :icon="getFontAwesomeIcon(accountForm.icon)" />
                <span v-else>无效图标</span>
              </div>
              <div class="icon-list">
                <div class="icon-list-title">选择图标：</div>
                <div class="icon-grid">
                  <div 
                    v-for="(icon, name) in allIcons" 
                    :key="name" 
                    class="icon-item" 
                    :class="{ 'icon-selected': accountForm.icon === name }"
                    @click="selectIcon(name)"
                  >
                    <font-awesome-icon :icon="icon" />
                    <div class="icon-name">{{ name.replace('_icon', '') }}</div>
                  </div>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="账户类型" prop="type" v-if="!isEditMode && !isAddChildMode">
            <el-select v-model="accountForm.type" placeholder="请选择账户类型">
              <el-option label="资产" value="ASSET"></el-option>
              <el-option label="负债" value="LIABILITY"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="账户金额" prop="assetAmount">
            <el-input-number 
              v-model="accountForm.assetAmount" 
              :precision="2" 
              :step="100" 
              :min="accountForm.type === 'LIABILITY' ? -1000000000 : 0"
              :max="accountForm.type === 'ASSET' ? 1000000000 : 0"
            ></el-input-number>
          </el-form-item>
          <el-form-item label="计入净资产" prop="includeInNetWorth">
            <el-switch v-model="includeInNetWorthSwitch"></el-switch>
          </el-form-item>
          <el-form-item label="排序顺序" prop="sortOrder">
            <el-input-number v-model="accountForm.sortOrder" :min="0" :max="100"></el-input-number>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveAccount">确定</el-button>
          </span>
        </template>
      </el-dialog>
  
      <!-- 删除确认对话框 -->
      <el-dialog
        title="确认删除"
        v-model="deleteDialogVisible"
        width="400px"
      >
        <p>确定要删除账户 "{{ accountToDelete?.name }}" 吗？</p>
        <p v-if="accountToDelete?.level === 1" class="warning-text">注意：删除一级账户将同时删除其下所有二级账户！</p>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="deleteDialogVisible = false">取消</el-button>
            <el-button type="danger" @click="deleteAccount">确定删除</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted, computed, watch } from 'vue';
  import { ElMessage } from 'element-plus';
  import accountApi from '@/api/account.js'; // 使用新创建的API接口
  import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'; // 导入FontAwesomeIcon组件
  
  const props = defineProps({
    bookId: {
      type: [String, Number],
      required: true
    }
  });
  
  // 状态定义
  const activeTab = ref('ASSET');
  const allAccounts = ref([]);
  const dialogVisible = ref(false);
  const deleteDialogVisible = ref(false);
  const accountFormRef = ref(null);
  const isEditMode = ref(false);
  const isAddChildMode = ref(false);
  const parentAccount = ref(null);
  const accountToDelete = ref(null);
  const includeInNetWorthSwitch = ref(true);
  const netWorth = reactive({
    totalAssets: 0,
    totalLiabilities: 0,
    netWorth: 0
  });
  
  // 图标映射函数
  const getFontAwesomeIcon = (iconName) => {
    if (!iconName) return null;
    
    // 图标名称到Font Awesome图标的映射
    const iconMap = {
      'cash_icon': ['fas', 'money-bill-wave'],
      'savings_icon': ['fas', 'piggy-bank'],
      'wallet_icon': ['fas', 'wallet'],
      'icbc_icon': ['fas', 'university'],
      'ccb_icon': ['fas', 'university'],
      'abc_icon': ['fas', 'university'],
      'stock_icon': ['fas', 'chart-line'],
      'fund_icon': ['fas', 'chart-pie'],
      'finance_icon': ['fas', 'money-bill-wave'],
      'alipay_icon': ['fab', 'alipay'],
      'wechat_icon': ['fab', 'weixin'],
      'credit_icon': ['far', 'credit-card'],
      'loan_icon': ['fas', 'hand-holding-usd'],
      'other_liability_icon': ['fas', 'ellipsis-h'],
      'boc_icon': ['fas', 'university'],
      'icbc_credit_icon': ['far', 'credit-card'],
      'cmb_icon': ['fas', 'university'],
      'house_loan_icon': ['fas', 'home'],
      'car_loan_icon': ['fas', 'car'],
      'consumer_loan_icon': ['fas', 'tags'],
      'shopping_icon': ['fas', 'shopping-bag'],
      'food_icon': ['fas', 'utensils'],
      'transport_icon': ['fas', 'car'],
      'home_icon': ['fas', 'home'],
      'daily_icon': ['fas', 'box'],
      'supermarket_icon': ['fas', 'shopping-cart'],
      'toiletry_icon': ['fas', 'toilet-paper'],
      'digital_icon': ['fas', 'mobile-alt'],
      'salary_icon': ['fas', 'money-bill-wave'],
      'investment_icon': ['fas', 'chart-line'],
      'other_income_icon': ['fas', 'ellipsis-h'],
      'bonus_icon': ['fas', 'wallet'],
      'performance_icon': ['fas', 'medal']
    };
    
    return iconMap[iconName] || null;
  };
  
  // 所有图标合并到一个对象中
  const allIcons = {
    'cash_icon': ['fas', 'money-bill-wave'],
    'savings_icon': ['fas', 'piggy-bank'],
    'wallet_icon': ['fas', 'wallet'],
    'icbc_icon': ['fas', 'university'],
    'ccb_icon': ['fas', 'university'],
    'abc_icon': ['fas', 'university'],
    'stock_icon': ['fas', 'chart-line'],
    'fund_icon': ['fas', 'chart-pie'],
    'finance_icon': ['fas', 'money-bill-wave'],
    'alipay_icon': ['fab', 'alipay'],
    'wechat_icon': ['fab', 'weixin'],
    'credit_icon': ['far', 'credit-card'],
    'loan_icon': ['fas', 'hand-holding-usd'],
    'other_liability_icon': ['fas', 'ellipsis-h'],
    'boc_icon': ['fas', 'university'],
    'icbc_credit_icon': ['far', 'credit-card'],
    'cmb_icon': ['fas', 'university'],
    'house_loan_icon': ['fas', 'home'],
    'car_loan_icon': ['fas', 'car'],
    'consumer_loan_icon': ['fas', 'tags']
  };
  
  // 选择图标
  const selectIcon = (iconName) => {
    accountForm.icon = iconName;
  };
  
  // 表单数据
  const accountForm = reactive({
    id: null,
    name: '',
    icon: '',
    type: 'ASSET',
    parentId: null,
    level: 1,
    sortOrder: 0,
    bookId: props.bookId,
    userId: localStorage.getItem('userId') || '',
    assetAmount: 0,
    includeInNetWorth: 1
  });
  
  // 监听includeInNetWorthSwitch变化，更新accountForm
  watch(includeInNetWorthSwitch, (newVal) => {
    accountForm.includeInNetWorth = newVal ? 1 : 0;
  });
  
  // 表单验证规则
  const rules = {
    name: [
      { required: true, message: '请输入账户名称', trigger: 'blur' },
      { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
    ],
    type: [
      { required: true, message: '请选择账户类型', trigger: 'change' }
    ]
  };
  
  // 计算属性：对话框标题
  const dialogTitle = computed(() => {
    if (isEditMode.value) {
      return '编辑账户';
    } else if (isAddChildMode.value) {
      return '添加子账户';
    } else {
      return '添加账户';
    }
  });
  
  // 计算属性：资产账户列表（一级）
  const assetAccounts = computed(() => {
    return allAccounts.value.filter(item => item.type === 'ASSET' && item.level === 1);
  });
  
  // 计算属性：负债账户列表（一级）
  const liabilityAccounts = computed(() => {
    return allAccounts.value.filter(item => item.type === 'LIABILITY' && item.level === 1);
  });
  
  // 获取子账户列表
  const getChildAccounts = (parentId) => {
    return allAccounts.value.filter(item => item.parentId === parentId);
  };
  
  // 格式化金额
  const formatAmount = (amount) => {
    if (amount === undefined || amount === null) return '¥0.00';
    return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY' }).format(amount);
  };
  
  // 加载账户数据
  const loadAccounts = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        ElMessage.error('用户未登录');
        return;
      }
      
      const response = await accountApi.getAccountList(props.bookId, userId);
      
      if (response.data.code === 200) {
        allAccounts.value = response.data.data || [];
      } else {
        ElMessage.error(response.data.msg || '加载账户失败');
      }
    } catch (error) {
      console.error('加载账户异常:', error);
      ElMessage.error('加载账户异常');
    }
  };
  
  // 加载净资产数据
  const loadNetWorth = async () => {
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        ElMessage.error('用户未登录');
        return;
      }
      
      const response = await accountApi.getNetWorth(props.bookId, userId);
      
      if (response.data.code === 200) {
        Object.assign(netWorth, response.data.data || {
          totalAssets: 0,
          totalLiabilities: 0,
          netWorth: 0
        });
      } else {
        ElMessage.error(response.data.msg || '加载净资产失败');
      }
    } catch (error) {
      console.error('加载净资产异常:', error);
      ElMessage.error('加载净资产异常');
    }
  };
  
  // 切换标签页
  const handleTabClick = () => {
    // 切换标签页时，如果要添加新账户，更新表单的类型
    if (!isEditMode.value && !isAddChildMode.value) {
      accountForm.type = activeTab.value;
    }
  };
  
  // 显示添加账户对话框
  const showAddAccountDialog = () => {
    isEditMode.value = false;
    isAddChildMode.value = false;
    parentAccount.value = null;
    includeInNetWorthSwitch.value = true;
    
    // 重置表单
    Object.assign(accountForm, {
      id: null,
      name: '',
      icon: '',
      type: activeTab.value,
      parentId: null,
      level: 1,
      sortOrder: 0,
      bookId: props.bookId,
      userId: localStorage.getItem('userId') || '',
      assetAmount: 0,
      includeInNetWorth: 1
    });
    
    dialogVisible.value = true;
  };
  
  // 显示添加子账户对话框
  const showAddChildDialog = (parent) => {
    isEditMode.value = false;
    isAddChildMode.value = true;
    parentAccount.value = parent;
    includeInNetWorthSwitch.value = true;
    
    // 设置子账户表单
    Object.assign(accountForm, {
      id: null,
      name: '',
      icon: '',
      type: parent.type,
      parentId: parent.id,
      level: 2,
      sortOrder: 0,
      bookId: props.bookId,
      userId: localStorage.getItem('userId') || '',
      assetAmount: 0,
      includeInNetWorth: 1
    });
    
    dialogVisible.value = true;
  };
  
  // 编辑账户
  const editAccount = (account) => {
    isEditMode.value = true;
    isAddChildMode.value = false;
    
    // 复制账户数据到表单
    Object.assign(accountForm, {
      id: account.id,
      name: account.name,
      icon: account.icon,
      type: account.type,
      parentId: account.parentId,
      level: account.level,
      sortOrder: account.sortOrder || 0,
      bookId: account.bookId,
      userId: account.userId,
      assetAmount: account.assetAmount || 0,
      includeInNetWorth: account.includeInNetWorth || 1
    });
    
    // 设置开关状态
    includeInNetWorthSwitch.value = account.includeInNetWorth === 1;
    
    dialogVisible.value = true;
  };
  
  // 保存账户
  const saveAccount = async () => {
    if (!accountFormRef.value) return;
    
    await accountFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          let response;
          
          if (isEditMode.value) {
            response = await accountApi.updateAccount(accountForm);
          } else {
            response = await accountApi.addAccount(accountForm);
          }
          
          if (response.data.code === 200) {
            ElMessage.success(isEditMode.value ? '账户更新成功' : '账户添加成功');
            dialogVisible.value = false;
            // 重新加载数据
            loadAccounts();
            loadNetWorth();
          } else {
            ElMessage.error(response.data.msg || (isEditMode.value ? '账户更新失败' : '账户添加失败'));
          }
        } catch (error) {
          console.error(isEditMode.value ? '更新账户异常:' : '添加账户异常:', error);
          ElMessage.error(isEditMode.value ? '更新账户异常' : '添加账户异常');
        }
      }
    });
  };
  
  // 确认删除
  const confirmDelete = (account) => {
    accountToDelete.value = account;
    deleteDialogVisible.value = true;
  };
  
  // 删除账户
  const deleteAccount = async () => {
    if (!accountToDelete.value) return;
    
    try {
      const response = await accountApi.deleteAccount(
        accountToDelete.value.id, 
        localStorage.getItem('userId')
      );
      
      if (response.data.code === 200) {
        ElMessage.success('账户删除成功');
        deleteDialogVisible.value = false;
        // 重新加载数据
        loadAccounts();
        loadNetWorth();
      } else {
        ElMessage.error(response.data.msg || '账户删除失败');
      }
    } catch (error) {
      console.error('删除账户异常:', error);
      ElMessage.error('删除账户异常');
    }
  };
  
  // 批量删除账户
  const batchDeleteAccounts = async (ids) => {
    try {
      const response = await accountApi.batchDeleteAccounts(
        ids,
        localStorage.getItem('userId')
      );
      
      if (response.data.code === 200) {
        ElMessage.success('批量删除成功');
        // 重新加载数据
        loadAccounts();
        loadNetWorth();
      } else {
        ElMessage.error(response.data.msg || '批量删除失败');
      }
    } catch (error) {
      console.error('批量删除异常:', error);
      ElMessage.error('批量删除异常');
    }
  };
  
  // 更新账户金额
  const updateAccountAmount = async (id, amount) => {
    try {
      const response = await accountApi.updateAccountAmount(
        id,
        localStorage.getItem('userId'),
        amount
      );
      
      if (response.data.code === 200) {
        ElMessage.success('账户金额更新成功');
        // 重新加载数据
        loadAccounts();
        loadNetWorth();
      } else {
        ElMessage.error(response.data.msg || '账户金额更新失败');
      }
    } catch (error) {
      console.error('更新账户金额异常:', error);
      ElMessage.error('更新账户金额异常');
    }
  };
  
  // 添加 fetchData 方法，以便 LedgerDetail.vue 可以调用刷新数据
  const fetchData = () => {
    loadAccounts();
    loadNetWorth();
  };
  
  // 暴露方法给父组件
  defineExpose({
    fetchData
  });
  
  // 页面加载时获取数据
  onMounted(() => {
    loadAccounts();
    loadNetWorth();
  });
</script>

<style scoped>
.account-management-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.account-list {
  margin-top: 20px;
}

.account-group {
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.parent-account {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.child-accounts {
  padding: 10px 20px;
}

.child-account {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px dashed #ebeef5;
}

.child-account:last-child {
  border-bottom: none;
}

.account-info {
  display: flex;
  align-items: center;
}

.account-icon {
  margin-right: 10px;
  font-size: 18px;
}

.account-name {
  font-size: 16px;
  margin-right: 15px;
}

.account-amount {
  color: #409EFF;
  font-weight: bold;
}

.account-actions {
  display: flex;
  gap: 8px;
}

.no-child {
  padding: 15px 0;
  text-align: center;
}

.empty-text {
  color: #909399;
  font-size: 14px;
}

.warning-text {
  color: #E6A23C;
  font-weight: bold;
}

.net-worth-card {
  margin-top: 20px;
}

.net-worth-content {
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
}

.net-worth-item {
  text-align: center;
}

.net-worth-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.net-worth-value {
  font-size: 18px;
  font-weight: bold;
}

.asset-value {
  color: #67C23A;
}

.liability-value {
  color: #F56C6C;
}

.net-value {
  color: #409EFF;
}

/* 添加图标选择器样式 */
.icon-selector {
  margin-top: 10px;
}

.icon-preview {
  margin: 10px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.icon-list {
  margin-top: 15px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
}

.icon-list-title {
  font-weight: bold;
  margin-bottom: 10px;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 10px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 8px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.icon-item:hover {
  background-color: #f5f7fa;
}

.icon-selected {
  background-color: #ecf5ff;
  border-color: #409eff;
  color: #409eff;
}

.icon-name {
  margin-top: 5px;
  font-size: 12px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}

.account-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  margin-right: 8px;
}
</style>