<template>
  <div class="record-drawer-container">
    <div class="drawer-overlay" @click="close"></div>
    <div class="drawer-content">
      <div class="drawer-header">
        <h2>记一笔</h2>
        <button class="close-button" @click="close">×</button>
      </div>

      <div class="drawer-body">
        <!-- 标签页切换 -->
        <div class="tabs">
          <div
              v-for="tab in tabs"
              :key="tab.id"
              :class="['tab', { active: activeTab === tab.id }]"
              @click="activeTab = tab.id"
          >
            {{ tab.name }}
          </div>
        </div>

        <!-- 标签页内容 -->
        <div class="tab-content">
          <!-- 支出表单 -->
          <div v-if="activeTab === 'expense'" class="tab-pane">
            <form @submit.prevent>
              <div class="form-group">
                <label>金额</label>
                <input type="number" v-model="form.expense.amount" placeholder="请输入金额" />
              </div>
              <div class="form-group">
                <label>分类</label>
                <select v-model="form.expense.category">
                  <option v-for="cat in expenseCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>账户</label>
                <select v-model="form.expense.account">
                  <option v-for="account in accounts" :key="account.id" :value="account.id">{{ account.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>日期</label>
                <input type="date" v-model="form.expense.date" />
              </div>
              <div class="form-group">
                <label>备注</label>
                <textarea v-model="form.expense.notes" placeholder="添加备注..."></textarea>
              </div>
            </form>
          </div>

          <!-- 收入表单 -->
          <div v-if="activeTab === 'income'" class="tab-pane">
            <form @submit.prevent>
              <div class="form-group">
                <label>金额</label>
                <input type="number" v-model="form.income.amount" placeholder="请输入金额" />
              </div>
              <div class="form-group">
                <label>分类</label>
                <select v-model="form.income.category">
                  <option v-for="cat in incomeCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>账户</label>
                <select v-model="form.income.account">
                  <option v-for="account in accounts" :key="account.id" :value="account.id">{{ account.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>日期</label>
                <input type="date" v-model="form.income.date" />
              </div>
              <div class="form-group">
                <label>备注</label>
                <textarea v-model="form.income.notes" placeholder="添加备注..."></textarea>
              </div>
            </form>
          </div>

          <!-- 转账表单 -->
          <div v-if="activeTab === 'transfer'" class="tab-pane">
            <form @submit.prevent>
              <div class="form-group">
                <label>金额</label>
                <input type="number" v-model="form.transfer.amount" placeholder="请输入金额" />
              </div>
              <div class="form-group">
                <label>从账户</label>
                <select v-model="form.transfer.fromAccount">
                  <option v-for="account in accounts" :key="account.id" :value="account.id">{{ account.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>到账户</label>
                <select v-model="form.transfer.toAccount">
                  <option v-for="account in accounts" :key="account.id" :value="account.id">{{ account.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>日期</label>
                <input type="date" v-model="form.transfer.date" />
              </div>
              <div class="form-group">
                <label>备注</label>
                <textarea v-model="form.transfer.notes" placeholder="添加备注..."></textarea>
              </div>
            </form>
          </div>

          <!-- 余额调整表单 -->
          <div v-if="activeTab === 'balance'" class="tab-pane">
            <form @submit.prevent>
              <div class="form-group">
                <label>账户</label>
                <select v-model="form.balance.account">
                  <option v-for="account in accounts" :key="account.id" :value="account.id">{{ account.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>新余额</label>
                <input type="number" v-model="form.balance.newBalance" placeholder="请输入新余额" />
              </div>
              <div class="form-group">
                <label>日期</label>
                <input type="date" v-model="form.balance.date" />
              </div>
              <div class="form-group">
                <label>备注</label>
                <textarea v-model="form.balance.notes" placeholder="添加备注..."></textarea>
              </div>
            </form>
          </div>

          <!-- 退款表单 -->
          <div v-if="activeTab === 'refund'" class="tab-pane">
            <form @submit.prevent>
              <div class="form-group">
                <label>金额</label>
                <input type="number" v-model="form.refund.amount" placeholder="请输入金额" />
              </div>
              <div class="form-group">
                <label>原支出分类</label>
                <select v-model="form.refund.originalCategory">
                  <option v-for="cat in expenseCategories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>退款账户</label>
                <select v-model="form.refund.account">
                  <option v-for="account in accounts" :key="account.id" :value="account.id">{{ account.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>日期</label>
                <input type="date" v-model="form.refund.date" />
              </div>
              <div class="form-group">
                <label>备注</label>
                <textarea v-model="form.refund.notes" placeholder="添加备注..."></textarea>
              </div>
            </form>
          </div>
        </div>
      </div>

      <div class="drawer-footer">
        <button class="save-button" @click="save">保存</button>
        <button class="save-continue-button" @click="saveAndContinue">保存并再记一笔</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RecordDrawer',
  props: {
    bookId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      activeTab: 'expense',
      tabs: [
        { id: 'expense', name: '支出' },
        { id: 'income', name: '收入' },
        { id: 'transfer', name: '转账' },
        { id: 'balance', name: '余额' },
        { id: 'refund', name: '退款' }
      ],
      // 模拟数据
      accounts: [
        { id: 1, name: '现金' },
        { id: 2, name: '银行卡' },
        { id: 3, name: '支付宝' },
        { id: 4, name: '微信' }
      ],
      expenseCategories: [
        { id: 1, name: '餐饮' },
        { id: 2, name: '交通' },
        { id: 3, name: '购物' },
        { id: 4, name: '娱乐' }
      ],
      incomeCategories: [
        { id: 1, name: '工资' },
        { id: 2, name: '奖金' },
        { id: 3, name: '投资收益' },
        { id: 4, name: '其他' }
      ],
      // 表单数据
      form: {
        expense: {
          amount: null,
          category: null,
          account: null,
          date: new Date().toISOString().substr(0, 10),
          notes: ''
        },
        income: {
          amount: null,
          category: null,
          account: null,
          date: new Date().toISOString().substr(0, 10),
          notes: ''
        },
        transfer: {
          amount: null,
          fromAccount: null,
          toAccount: null,
          date: new Date().toISOString().substr(0, 10),
          notes: ''
        },
        balance: {
          account: null,
          newBalance: null,
          date: new Date().toISOString().substr(0, 10),
          notes: ''
        },
        refund: {
          amount: null,
          originalCategory: null,
          account: null,
          date: new Date().toISOString().substr(0, 10),
          notes: ''
        }
      }
    };
  },
  methods: {
    close() {
      this.$emit('close');
    },
    save() {
      // 根据当前标签页获取对应的表单数据
      const formData = {
        type: this.activeTab,
        bookId: this.bookId,
        data: this.form[this.activeTab]
      };

      this.$emit('save', formData);
    },
    saveAndContinue() {
      // 根据当前标签页获取对应的表单数据
      const formData = {
        type: this.activeTab,
        bookId: this.bookId,
        data: this.form[this.activeTab]
      };

      this.$emit('save-and-continue', formData);
      this.resetForm();
    },
    resetForm() {
      // 重置当前标签页的表单
      this.form[this.activeTab] = {
        ...this.getDefaultFormValues(this.activeTab)
      };
    },
    getDefaultFormValues(tabType) {
      const today = new Date().toISOString().substr(0, 10);

      const defaults = {
        expense: { amount: null, category: null, account: null, date: today, notes: '' },
        income: { amount: null, category: null, account: null, date: today, notes: '' },
        transfer: { amount: null, fromAccount: null, toAccount: null, date: today, notes: '' },
        balance: { account: null, newBalance: null, date: today, notes: '' },
        refund: { amount: null, originalCategory: null, account: null, date: today, notes: '' }
      };

      return defaults[tabType];
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
  padding: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}

.drawer-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 8px;
  line-height: 1;
}

.close-button:hover {
  color: #666;
}

.drawer-body {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

.tabs {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}

.tab {
  cursor: pointer;
  padding: 8px 12px;
  color: #666;
  position: relative;
  transition: color 0.2s;
}

.tab.active {
  color: #409eff;
  font-weight: 500;
}

.tab.active::after {
  content: '';
  position: absolute;
  bottom: -9px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #409eff;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.form-group textarea {
  height: 80px;
  resize: vertical;
}

.drawer-footer {
  padding: 16px 24px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 12px;
  background: #fff;
}

.save-button,
.save-continue-button {
  flex: 1;
  padding: 12px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.save-button {
  background-color: #409eff;
  color: white;
}

.save-button:hover {
  background-color: #66b1ff;
}

.save-continue-button {
  background-color: #f0f2f5;
  color: #606266;
}

.save-continue-button:hover {
  background-color: #e4e7ed;
}

/* 移动端适配 */
@media (max-width: 480px) {
  .drawer-content {
    width: 100%;
  }
}
</style>