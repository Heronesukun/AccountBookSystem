<template>
  <div class="home">
    <!-- 头部区域 -->
    <el-row class="header">
      <el-col :span="24">
        <div class="header-content">
          <h2>欢迎, {{ realName }}!</h2>
          <el-button type="primary" @click="logout">退出登录</el-button>
        </div>
      </el-col>
    </el-row>

    <!-- 账本区域 -->
    <el-row class="section">
      <el-col :span="24">
        <el-card shadow="hover" class="notebook-card">
          <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane label="全部" name="all"></el-tab-pane>
            <el-tab-pane label="我创建" name="created"></el-tab-pane>
            <el-tab-pane label="我参与" name="participated"></el-tab-pane>
          </el-tabs>

          <el-row :gutter="20" class="notebook-list">
            <el-col :span="6" v-for="notebook in displayedNotebooks" :key="notebook.bookId">
              <el-card shadow="hover" class="notebook">
                <div class="notebook-content" @click="viewNotebook(notebook.bookId)">
                  <h4>{{ notebook.bookName }}</h4>
                  <p>创建时间: {{ formatDate(notebook.createTime) }}</p>
                </div>
                <div class="notebook-actions">
                  <el-button type="text" @click.stop="editNotebook(notebook)">修改</el-button>
                  <el-button
                      type="text"
                      @click.stop="deleteNotebook(notebook.bookId)"
                      v-if="notebook.type === 'created'"
                  >删除</el-button>
                </div>
              </el-card>
            </el-col>
            <!-- 仅在“全部”和“我创建”选项卡显示新增账本 -->
            <el-col :span="6" v-if="activeTab !== 'participated'">
              <el-card shadow="hover" class="new-notebook" @click="createNotebook">
                <el-icon><Plus /></el-icon>
                <p>新建账本</p>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 修改账本对话框 -->
    <el-dialog title="修改账本名称" v-model="editDialogVisible" width="30%">
      <el-form :model="editForm">
        <el-form-item label="账本名称">
          <el-input v-model="editForm.bookName"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">确认</el-button>
      </template>
    </el-dialog>

    <!-- 新建账本对话框 -->
    <el-dialog title="新建账本" v-model="createDialogVisible" width="30%">
      <el-form :model="createForm">
        <el-form-item label="账本名称">
          <el-input v-model="createForm.bookName"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCreate">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import mainPageBookRequest from '@/api/mainPageBook.js';

// 路由
const router = useRouter();

// 从 localStorage 获取用户信息
const realName = ref(localStorage.getItem('real_name') || '用户');
const username = ref(localStorage.getItem('username') || '');

// 页签和账本数据
const activeTab = ref('all');
const createdNotebooks = ref([]);
const participatedNotebooks = ref([]);

// 根据页签动态显示账本
const displayedNotebooks = computed(() => {
  if (activeTab.value === 'all') {
    return [...createdNotebooks.value, ...participatedNotebooks.value];
  } else if (activeTab.value === 'created') {
    return createdNotebooks.value;
  } else {
    return participatedNotebooks.value;
  }
});

// 对话框状态
const editDialogVisible = ref(false);
const createDialogVisible = ref(false);
const editForm = ref({ bookId: null, bookName: '' });
const createForm = ref({ bookName: '' });

// 页面加载时获取账本数据
onMounted(() => {
  fetchNotebooks();
});

// 获取账本数据
function fetchNotebooks() {
  const bookMaster = username.value;

  // 获取用户创建的账本
  mainPageBookRequest.getMainPageBooks(bookMaster).then(res => {
    if (res.data.code === 200) {
      createdNotebooks.value = res.data.data.map(book => ({ ...book, type: 'created' }));
    } else {
      ElMessage.error('获取我创建的账本失败');
    }
  });

  // 获取用户参与的账本
  mainPageBookRequest.getMainPageBooksWhenParticipants(bookMaster).then(res => {
    if (res.data.code === 200) {
      participatedNotebooks.value = res.data.data.map(book => ({ ...book, type: 'participated' }));
    } else {
      ElMessage.error('获取我参与的账本失败');
    }
  });
}

// 页签切换
function handleTabClick() {
  // 可选：每次切换时重新获取数据
  fetchNotebooks();
}

// 格式化日期
function formatDate(date) {
  return new Date(date).toLocaleDateString();
}

// 查看账本
function viewNotebook(bookId) {
  router.push(`/LedgerDetail?bookId=${bookId}`);
}

// 修改账本
function editNotebook(notebook) {
  editForm.value.bookId = notebook.bookId;
  editForm.value.bookName = notebook.bookName;
  editDialogVisible.value = true;
}

function confirmEdit() {
  if (!editForm.value.bookName.trim()) {
    ElMessage.error('账本名称不能为空');
    return;
  }
  const accountBook = { bookId: editForm.value.bookId, bookName: editForm.value.bookName };
  mainPageBookRequest.updateBookName(accountBook).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('修改成功');
      fetchNotebooks();
      editDialogVisible.value = false;
    } else {
      ElMessage.error('修改失败');
    }
  });
}

// 删除账本
function deleteNotebook(bookId) {
  ElMessageBox.confirm('确定要删除这个账本吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const accountBook = { bookId };
    mainPageBookRequest.deleteBook(accountBook).then(res => {
      if (res.data.code === 200) {
        ElMessage.success('删除成功');
        fetchNotebooks();
      } else {
        ElMessage.error('删除失败');
      }
    });
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
}

// 新建账本
function createNotebook() {
  createDialogVisible.value = true;
}

function confirmCreate() {
  if (!createForm.value.bookName.trim()) {
    ElMessage.error('账本名称不能为空');
    return;
  }
  const accountBook = { bookMaster: username.value, bookName: createForm.value.bookName };
  mainPageBookRequest.createNewBook(accountBook).then(res => {
    if (res.data.code === 200) {
      ElMessage.success('创建成功');
      fetchNotebooks();
      createDialogVisible.value = false;
      createForm.value.bookName = ''; // 清空输入框
    } else {
      ElMessage.error('创建失败');
    }
  });
}

// 退出登录
function logout() {
  ElMessage.warning('已退出登录');
  localStorage.removeItem('real_name');
  localStorage.removeItem('username');
  localStorage.removeItem('token');
  router.push('/login');
}
</script>

<style scoped>
.home {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.section {
  margin-top: 20px;
}

.notebook-card {
  border-radius: 12px;
  background-color: #fff;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.notebook-list {
  margin-top: 20px;
}

.notebook {
  position: relative;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  border-radius: 8px;
  overflow: hidden;
}

.notebook:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.notebook-content {
  padding: 15px;
  text-align: center;
}

.notebook-actions {
  position: absolute;
  top: 10px;
  right: 10px;
}

.new-notebook {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 120px;
  cursor: pointer;
  background-color: #e6f7ff;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.new-notebook:hover {
  background-color: #bae7ff;
}

.el-button--text {
  color: #409eff;
}
</style>