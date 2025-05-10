<template>
    <div class="settings-page">
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <h2>账本基本信息</h2>
          </div>
        </template>
        <div class="book-info">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="账本名称">
              <div class="editable-field">
                <span v-if="!isEditingName">{{ bookInfo.bookName }}</span>
                <el-input v-else v-model="editingName" size="small" />
                <div class="actions">
                  <el-button v-if="!isEditingName" type="primary" size="small" @click="startEditName">
                    修改
                  </el-button>
                  <template v-else>
                    <el-button type="success" size="small" @click="saveBookName">保存</el-button>
                    <el-button size="small" @click="cancelEditName">取消</el-button>
                  </template>
                </div>
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDate(bookInfo.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="账本ID">{{ bookId }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
  
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <h2>数据导入导出</h2>
          </div>
        </template>
        <div class="import-export">
          <div class="export-section">
            <h3>导出数据</h3>
            <p>将账本数据导出为文件，便于备份或迁移。</p>
            <el-button type="primary" @click="exportData">导出账本数据</el-button>
          </div>
          <el-divider />
          <div class="import-section">
            <h3>导入数据</h3>
            <p>从文件导入账本数据，请确保文件格式正确。</p>
            <el-upload
              class="upload-demo"
              action="#"
              :auto-upload="false"
              :on-change="handleFileChange"
              :limit="1"
            >
              <template #trigger>
                <el-button type="primary">选择文件</el-button>
              </template>
              <el-button
                class="ml-3"
                type="success"
                @click="importData"
                :disabled="!selectedFile"
              >
                导入数据
              </el-button>
            </el-upload>
          </div>
        </div>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, computed } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import mainPageBookRequest from '@/api/mainPageBook.js';
  import bookExportImportRequest from '@/api/bookExportImport.js';
  
  // 获取账本ID
  const props = defineProps({
    bookId: {
      type: [String, Number],
      required: true
    }
  });
  
  // 账本信息
  const bookInfo = ref({
    bookName: '',
    createTime: '',
    bookId: props.bookId
  });
  
  // 编辑状态
  const isEditingName = ref(false);
  const editingName = ref('');
  const selectedFile = ref(null);
  
  // 获取账本详情
  const fetchBookDetails = async () => {
    try {
      const bookMaster = localStorage.getItem('username');
      if (!bookMaster) {
        ElMessage.error('未获取到用户信息');
        return;
      }
  
      const response = await mainPageBookRequest.getMainPageBooks(bookMaster);
      if (response.data.code === 200) {
        const books = response.data.data;
        const currentBook = books.find(book => book.bookId == props.bookId);
        if (currentBook) {
          bookInfo.value = currentBook;
        } else {
          ElMessage.warning('未找到当前账本信息');
        }
      } else {
        ElMessage.error(response.data.msg || '获取账本信息失败');
      }
    } catch (error) {
      console.error('获取账本详情失败:', error);
      ElMessage.error('获取账本详情失败');
    }
  };
  
  // 格式化日期
  const formatDate = (dateString) => {
    if (!dateString) return '未知';
    const date = new Date(dateString);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  };
  
  // 开始编辑名称
  const startEditName = () => {
    editingName.value = bookInfo.value.bookName;
    isEditingName.value = true;
  };
  
  // 取消编辑名称
  const cancelEditName = () => {
    isEditingName.value = false;
  };
  
  // 保存账本名称
  const saveBookName = async () => {
    if (!editingName.value.trim()) {
      ElMessage.warning('账本名称不能为空');
      return;
    }
  
    try {
      const accountBook = {
        bookId: props.bookId,
        bookName: editingName.value.trim(),
        bookMaster: localStorage.getItem('username')
      };
  
      const response = await mainPageBookRequest.updateBookName(accountBook);
      if (response.data.code === 200) {
        ElMessage.success('账本名称更新成功');
        bookInfo.value.bookName = editingName.value.trim();
        isEditingName.value = false;
      } else {
        ElMessage.error(response.data.msg || '更新账本名称失败');
      }
    } catch (error) {
      console.error('更新账本名称失败:', error);
      ElMessage.error('更新账本名称失败');
    }
  };
  
  // 处理文件选择
  const handleFileChange = (file) => {
    selectedFile.value = file;
  };
  
  // 导出数据
  const exportData = async () => {
    try {
        ElMessage.info('正在准备导出数据...');
        
        const exportTime = new Date().toISOString();
        const mockData = {
            exportTime: exportTime,
            transactions: [
                { id: 24, amount: -104, time: "2025-04-01 08:00:00", remark: "超市测试1", type: "支出", account: "支付宝", merchant: "沃尔玛超市", category: "购物消费", icon: "shopping_icon" },
                { id: 25, amount: -802, time: "2025-05-03 08:00:00", remark: "打车出行吗?有点意思", type: "支出", account: "支付宝", merchant: "滴滴出行", category: "交通出行", icon: "transport_icon" },
                { id: 26, amount: 3200, time: "2025-04-15 08:00:00", remark: "发工资", type: "收入", account: "支付宝", merchant: "", category: "职业收入", icon: "salary_icon" },
                { id: 27, amount: -123, time: "2025-05-01 08:00:00", remark: "疯狂星期四", type: "支出", account: "支付宝", merchant: "肯德基", category: "餐饮美食", icon: "food_icon" },
                { id: 28, amount: 605, time: "2025-05-03 08:00:00", remark: "刮刮乐", type: "收入", account: "支付宝", merchant: "", category: "投资理财", icon: "investment_icon" },
                { id: 29, amount: -88, time: "2025-05-03 08:00:00", remark: "联动套餐", type: "支出", account: "支付宝", merchant: "麦当劳", category: "餐饮美食", icon: "food_icon" },
                { id: 30, amount: -50.5, time: "2025-05-06 08:00:00", remark: "", type: "支出", account: "支付宝", merchant: "12306", category: "交通出行", icon: "transport_icon" },
                { id: 31, amount: -10, time: "2025-05-07 08:00:00", remark: "", type: "支出", account: "支付宝", merchant: "地铁出行", category: "交通出行", icon: "transport_icon" },
                { id: 32, amount: -113, time: "2025-05-07 08:00:00", remark: "", type: "支出", account: "支付宝", merchant: "沃尔玛超市", category: "购物消费", icon: "shopping_icon" },
                { id: 33, amount: -28, time: "2025-04-28 08:00:00", remark: "看孤独摇滚", type: "支出", account: "支付宝", merchant: "", category: "休闲娱乐", icon: "digital_icon" }
            ],
            accounts: [
                { id: 1, name: "现金账户", type: "ASSET", icon: "cash_icon", balance: 0.00 },
                { id: 2, name: "储蓄账户", type: "ASSET", icon: "savings_icon", balance: 0.00 },
                { id: 3, name: "投资账户", type: "ASSET", icon: "investment_icon", balance: 0.00 },
                { id: 4, name: "数字账户", type: "ASSET", icon: "digital_icon", balance: 0.00 },
                { id: 5, name: "工资卡", type: "ASSET", icon: "salary_card_icon", balance: 5000.00 },
                { id: 6, name: "现金钱包", type: "ASSET", icon: "wallet_icon", balance: 1000.00 },
                { id: 7, name: "工行储蓄卡", type: "ASSET", icon: "icbc_icon", balance: 10000.00 },
                { id: 8, name: "建行储蓄卡", type: "ASSET", icon: "ccb_icon", balance: 8000.00 },
                { id: 9, name: "农行储蓄卡", type: "ASSET", icon: "abc_icon", balance: 5000.00 },
                { id: 10, name: "股票账户", type: "ASSET", icon: "stock_icon", balance: 20000.00 },
                { id: 11, name: "基金账户", type: "ASSET", icon: "fund_icon", balance: 15000.00 },
                { id: 12, name: "理财产品", type: "ASSET", icon: "finance_icon", balance: 30000.00 },
                { id: 13, name: "支付宝", type: "ASSET", icon: "alipay_icon", balance: 1726.00 },
                { id: 14, name: "微信钱包", type: "ASSET", icon: "wechat_icon", balance: 2000.00 },
                { id: 15, name: "信用账户", type: "LIABILITY", icon: "credit_icon", balance: 0.00 },
                { id: 16, name: "贷款账户", type: "LIABILITY", icon: "loan_icon", balance: 0.00 },
                { id: 17, name: "其他负债", type: "LIABILITY", icon: "other_liability_icon", balance: 0.00 },
                { id: 18, name: "中行信用卡", type: "LIABILITY", icon: "boc_icon", balance: -2000.00 },
                { id: 19, name: "工行信用卡", type: "LIABILITY", icon: "icbc_credit_icon", balance: -1500.00 },
                { id: 20, name: "招行信用卡", type: "LIABILITY", icon: "cmb_icon", balance: -3000.00 },
                { id: 21, name: "房贷", type: "LIABILITY", icon: "house_loan_icon", balance: -500000.00 },
                { id: 22, name: "车贷", type: "LIABILITY", icon: "car_loan_icon", balance: -80000.00 },
                { id: 23, name: "消费贷", type: "LIABILITY", icon: "consumer_loan_icon", balance: -20000.00 },
                { id: 24, name: "花呗", type: "LIABILITY", icon: "", balance: 200.00 }
            ]
        };
  
        // 将对象转换为JSON字符串
        const jsonData = JSON.stringify(mockData, null, 2);
        
        // 创建Blob对象
        const blob = new Blob([jsonData], { type: 'application/json' });
        
        // 创建下载链接
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `账本_${bookInfo.value.bookName}_${new Date().getTime()}.json`;
        
        // 触发下载
        document.body.appendChild(link);
        link.click();
        
        // 清理
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
        
        ElMessage.success('数据导出成功');
    } catch (error) {
        console.error('导出数据失败:', error);
        ElMessage.error(`导出数据失败: ${error.message || '未知错误'}`);
    }
};
  
  // 导入数据
  const importData = async () => {
    if (!selectedFile.value) {
      ElMessage.warning('请先选择要导入的文件');
      return;
    }
  
    try {
      ElMessageBox.confirm(
        '导入数据将覆盖当前账本的部分或全部数据，确定要继续吗？',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        ElMessage.info('正在导入数据...');
        
        const reader = new FileReader();
        reader.onload = async (e) => {
          try {
            // 解析JSON数据
            const importedData = e.target.result;
            
            const userId = localStorage.getItem('username');
            if (!userId) {
              ElMessage.error('未获取到用户信息');
              return;
            }
            
            // 调用导入API
            const response = await bookExportImportRequest.importBookData(
              props.bookId, 
              userId, 
              importedData
            );
            
            if (response.data.code === 200) {
              ElMessage.success('数据导入成功');
              // 刷新账本信息
              fetchBookDetails();
            } else {
              ElMessage.error(response.data.msg || '导入数据失败');
            }
          } catch (error) {
            console.error('解析导入文件失败:', error);
            ElMessage.error('导入失败：文件格式不正确');
          }
        };
        
        reader.onerror = () => {
          ElMessage.error('读取文件失败');
        };
        
        reader.readAsText(selectedFile.value.raw);
      }).catch(() => {
        ElMessage.info('已取消导入操作');
      });
    } catch (error) {
      console.error('导入数据失败:', error);
      ElMessage.error('导入数据失败');
    }
  };
  
  // 组件挂载时获取账本信息
  onMounted(() => {
    fetchBookDetails();
  });
  </script>
  
  <style scoped>
  .settings-page {
    padding: 20px;
  }
  
  .settings-card {
    margin-bottom: 20px;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .book-info {
    margin-bottom: 20px;
  }
  
  .editable-field {
    display: flex;
    align-items: center;
  }
  
  .editable-field .el-input {
    width: 200px;
    margin-right: 10px;
  }
  
  .actions {
    margin-left: 10px;
  }
  
  .import-export {
    padding: 10px 0;
  }
  
  .export-section, .import-section {
    margin-bottom: 20px;
  }
  
  .ml-3 {
    margin-left: 12px;
  }
  </style>