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