<template>
  <div class="record-drawer-container">
    <div class="drawer-overlay" @click="close"></div>
    <div class="drawer-content">
      <div class="drawer-header">
        <h2>记一笔</h2>
        <el-button class="close-button" type="text" @click="close">
          <el-icon><Close /></el-icon>
        </el-button>
      </div>

      <div class="drawer-body">
        <!-- 标签页切换 -->
        <el-tabs v-model="activeTab" class="drawer-tabs">
          <el-tab-pane v-for="tab in tabs" :key="tab.id" :label="tab.name" :name="tab.id">
            <el-form :model="form[activeTab]" label-width="80px" label-position="top">
              <!-- 金额 -->
              <el-form-item label="金额" v-if="activeTab !== 'balance'">
                <el-input-number v-model="form[activeTab].amount" :min="0" :precision="2" style="width: 100%"></el-input-number>
              </el-form-item>

              <!-- 新余额 (仅适用于balance) -->
              <el-form-item label="新余额" v-if="activeTab === 'balance'">
                <el-input-number v-model="form[activeTab].newBalance" :precision="2" style="width: 100%"></el-input-number>
              </el-form-item>

              <!-- 分类 (适用于expense, income, refund) -->
              <el-form-item label="分类" v-if="['expense', 'income', 'refund'].includes(activeTab)">
                <el-select v-model="form[activeTab].category" placeholder="请选择分类" style="width: 100%">
                  <el-option
                      v-for="category in activeTab === 'income' ? incomeCategories : expenseCategories"
                      :key="category.id"
                      :label="category.name"
                      :value="category.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <!-- 原支出分类 (仅适用于refund) -->
              <el-form-item label="原支出分类" v-if="activeTab === 'refund'">
                <el-select v-model="form[activeTab].originalCategory" placeholder="请选择原支出分类" style="width: 100%">
                  <el-option
                      v-for="category in expenseCategories"
                      :key="category.id"
                      :label="category.name"
                      :value="category.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <!-- 账户 -->
              <el-form-item label="账户" v-if="!['transfer'].includes(activeTab)">
                <el-select v-model="form[activeTab].account" placeholder="请选择账户" style="width: 100%">
                  <el-option
                      v-for="account in accounts"
                      :key="account.id"
                      :label="account.name"
                      :value="account.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <!-- 从账户 (仅适用于transfer) -->
              <el-form-item label="从账户" v-if="activeTab === 'transfer'">
                <el-select v-model="form[activeTab].fromAccount" placeholder="请选择转出账户" style="width: 100%">
                  <el-option
                      v-for="account in accounts"
                      :key="account.id"
                      :label="account.name"
                      :value="account.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <!-- 到账户 (仅适用于transfer) -->
              <el-form-item label="到账户" v-if="activeTab === 'transfer'">
                <el-select v-model="form[activeTab].toAccount" placeholder="请选择转入账户" style="width: 100%">
                  <el-option
                      v-for="account in accounts"
                      :key="account.id"
                      :label="account.name"
                      :value="account.id"
                      :disabled="account.id === form[activeTab].fromAccount"
                  ></el-option>
                </el-select>
              </el-form-item>

              <!-- 商家 (适用于expense, refund) -->
              <el-form-item label="商家" v-if="['expense', 'refund'].includes(activeTab)">
                <el-select v-model="form[activeTab].merchant" placeholder="请选择商家（可选）" style="width: 100%" filterable clearable>
                  <el-option
                      v-for="merchant in merchants"
                      :key="merchant.id"
                      :label="merchant.name"
                      :value="merchant.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <!-- 日期 -->
              <el-form-item label="日期">
                <el-date-picker
                    v-model="form[activeTab].date"
                    type="date"
                    placeholder="选择日期"
                    style="width: 100%"
                ></el-date-picker>
              </el-form-item>

              <!-- 备注 -->
              <el-form-item label="备注">
                <el-input
                    v-model="form[activeTab].notes"
                    type="textarea"
                    :rows="3"
                    placeholder="添加备注..."
                ></el-input>
              </el-form-item>
            </el-form>

          </el-tab-pane>
        </el-tabs>
      </div>

      <div class="drawer-footer">
        <el-button type="primary" @click="save" :loading="loading">保存</el-button>
        <el-button @click="saveAndContinue" :loading="loading">保存并再记一笔</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { Close } from '@element-plus/icons-vue';
// 导入API模块
import accountApi from '@/api/account';
import categoryApi from '@/api/category';
import merchantApi from '@/api/merchant';
import generalTableApi from '@/api/generalTable'; // 导入新创建的API

export default {
  name: 'RecordDrawer',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    bookId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    const today = new Date().toISOString().substr(0, 10);
    return {
      drawerVisible: this.visible,
      activeTab: 'expense',
      loading: false,
      tabs: [
        { id: 'expense', name: '支出' },
        { id: 'income', name: '收入' },
        { id: 'transfer', name: '转账' },
        { id: 'balance', name: '余额' },
        { id: 'refund', name: '退款' }
      ],
      accounts: [],
      expenseCategories: [],
      incomeCategories: [],
      merchants: [],
      form: {
        expense: {
          amount: null,
          category: null,
          account: null,
          merchant: null,
          date: today,
          notes: ''
        },
        income: {
          amount: null,
          category: null,
          account: null,
          date: today,
          notes: ''
        },
        transfer: {
          amount: null,
          fromAccount: null,
          toAccount: null,
          date: today,
          notes: ''
        },
        balance: {
          account: null,
          newBalance: null,
          date: today,
          notes: ''
        },
        refund: {
          amount: null,
          originalCategory: null,
          account: null,
          merchant: null,
          date: today,
          notes: ''
        }
      }
    };
  },
  watch: {
    visible(newVal) {
      this.drawerVisible = newVal;
    },
    drawerVisible(newVal) {
      if (!newVal) {
        this.$emit('update:visible', false);
        this.$emit('close');
      }
    }
  },
  created() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        const userId = localStorage.getItem('userId');

        // 获取账户列表
        const accountResponse = await accountApi.getAccountList(this.bookId, userId);
        this.accounts = accountResponse.data.data || [];

        // 获取支出分类列表
        const expenseCategoriesResponse = await categoryApi.getParentCategories(this.bookId, userId, 'expense');
        this.expenseCategories = expenseCategoriesResponse.data.data || [];

        // 获取收入分类列表
        const incomeCategoriesResponse = await categoryApi.getParentCategories(this.bookId, userId, 'income');
        this.incomeCategories = incomeCategoriesResponse.data.data || [];

        // 获取商家列表
        const merchantResponse = await merchantApi.getMerchantList(this.bookId, userId);
        // 检查返回的数据结构，确保正确提取商家列表
        if (merchantResponse.data && merchantResponse.data.code === 200) {
          // 如果返回的是分页数据，则提取records或list字段
          if (merchantResponse.data.data && Array.isArray(merchantResponse.data.data)) {
            this.merchants = merchantResponse.data.data;
          } else if (merchantResponse.data.data && merchantResponse.data.data.records) {
            this.merchants = merchantResponse.data.data.records;
          } else if (merchantResponse.data.data && merchantResponse.data.data.list) {
            this.merchants = merchantResponse.data.data.list;
          } else {
            this.merchants = [];
            console.error('商家数据格式不正确:', merchantResponse.data);
          }
        } else {
          this.merchants = [];
          console.error('获取商家列表失败:', merchantResponse);
        }
        
        // 调试输出
        console.log('获取到的商家列表:', this.merchants);
      } catch (error) {
        console.error('获取数据失败:', error);
        this.$message.error('获取数据失败，请稍后重试');
      }
    },
    close() {
      this.drawerVisible = false;
    },
    async save() {
  this.loading = true;
  
  // 构建通用记录对象
  const generalTable = this.buildGeneralTableObject();
  
  // 添加type字段，使用当前活动的标签页作为类型
  generalTable.type = this.getTypeFromActiveTab();
  
  // 调用API保存记录
  generalTableApi.addGeneralTable(generalTable)
    .then(response => {
      if (response.data.code === 200) {
        ElMessage.success('记录添加成功');
        this.$emit('save', response.data.data);
        this.close();
      } else {
        ElMessage.error(response.data.msg || '记录添加失败');
      }
    })
    .catch(error => {
      console.error('添加记录出错:', error);
      ElMessage.error('添加记录失败，请稍后重试');
    })
    .finally(() => {
      this.loading = false;
    });
},
    async saveAndContinue() {
  this.loading = true;
  
  // 构建通用记录对象
  const generalTable = this.buildGeneralTableObject();
  
  // 添加type字段，使用当前活动的标签页作为类型
  generalTable.type = this.getTypeFromActiveTab();
  
  // 调用API保存记录
  generalTableApi.addGeneralTable(generalTable)
    .then(response => {
      if (response.data.code === 200) {
        ElMessage.success('记录添加成功');
        this.$emit('save-and-continue', response.data.data);
        // 重置表单，但保持抽屉打开状态
        this.resetForm();
      } else {
        ElMessage.error(response.data.msg || '记录添加失败');
      }
    })
    .catch(error => {
      console.error('添加记录出错:', error);
      ElMessage.error('添加记录失败，请稍后重试');
    })
    .finally(() => {
      this.loading = false;
    });
},
// 根据activeTab获取对应的type值
getTypeFromActiveTab() {
  const typeMapping = {
    'expense': '支出',
    'income': '收入',
    'transfer': '转账',
    'balance': '余额',
    'refund': '退款'
  };
  return typeMapping[this.activeTab] || this.activeTab;
},
// 构建通用记录对象
buildGeneralTableObject() {
  const formData = this.form[this.activeTab];
  
  // 处理日期格式，确保包含时分秒
  let formattedDate;
  if (formData.date) {
    // 如果是字符串格式的日期（yyyy-MM-dd）
    if (typeof formData.date === 'string') {
      formattedDate = `${formData.date} 00:00:00`;
    } 
    // 如果是Date对象
    else if (formData.date instanceof Date) {
      const year = formData.date.getFullYear();
      const month = String(formData.date.getMonth() + 1).padStart(2, '0');
      const day = String(formData.date.getDate()).padStart(2, '0');
      formattedDate = `${year}-${month}-${day} 00:00:00`;
    }
    // 其他情况保持原值
    else {
      formattedDate = formData.date;
    }
  } else {
    // 如果没有日期，使用当前日期时间
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    formattedDate = `${year}-${month}-${day} 00:00:00`;
  }
  
  const generalTable = {
    bid: this.bookId,
    userId: parseInt(localStorage.getItem('userId')),
    date: formattedDate,
    desc: formData.notes // 保持原有的desc字段
  };
  
  // 根据不同的记录类型设置不同的字段
  switch (this.activeTab) {
    case 'expense':
      generalTable.amount = -Math.abs(formData.amount); // 支出为负数
      generalTable.accountCategoryId = formData.account;
      generalTable.categoryId = formData.category;
      generalTable.merchantId = formData.merchant || null;
      break;
    case 'income':
      generalTable.amount = Math.abs(formData.amount); // 收入为正数
      generalTable.accountCategoryId = formData.account;
      generalTable.categoryId = formData.category;
      break;
    case 'transfer':
      generalTable.amount = Math.abs(formData.amount);
      generalTable.accountCategoryId = formData.fromAccount; // 转出账户
      generalTable.transferAccountId = formData.toAccount; // 转入账户
      break;
    case 'balance':
      generalTable.amount = formData.newBalance;
      generalTable.accountCategoryId = formData.account;
      break;
    case 'refund':
      generalTable.amount = Math.abs(formData.amount);
      generalTable.accountCategoryId = formData.account;
      generalTable.categoryId = formData.originalCategory;
      generalTable.merchantId = formData.merchant || null;
      break;
  }
  
  return generalTable;
},
    resetForm() {
      // 重置当前标签页的表单
      const today = new Date().toISOString().substr(0, 10);

      // 定义默认值
      const defaults = {
        expense: { amount: null, category: null, account: null, merchant: null, date: today, notes: '' },
        income: { amount: null, category: null, account: null, date: today, notes: '' },
        transfer: { amount: null, fromAccount: null, toAccount: null, date: today, notes: '' },
        balance: { account: null, newBalance: null, date: today, notes: '' },
        refund: { amount: null, originalCategory: null, account: null, merchant: null, date: today, notes: '' }
      };

      // 应用默认值
      this.form[this.activeTab] = { ...defaults[this.activeTab] };
    }
  }
};
</script>

<style scoped>
.record-drawer-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
}

.drawer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(2px);
  transition: opacity 0.3s;
}

.drawer-content {
  position: fixed;
  top: 0;
  right: 0;
  width: 480px;
  height: 100%;
  background: #ffffff;
  box-shadow: -4px 0 16px rgba(0, 0, 0, 0.1);
  transform: translateX(0);
  transition: transform 0.3s ease-out;
  display: flex;
  flex-direction: column;
}

.drawer-header {
  padding: 16px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}

.drawer-header h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-button {
  padding: 8px;
  line-height: 1;
}

.drawer-body {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.drawer-footer {
  padding: 16px 24px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 12px;
  background: #fff;
}

/* 移动端适配 */
@media (max-width: 480px) {
  .drawer-content {
    width: 100%;
  }
}
</style>