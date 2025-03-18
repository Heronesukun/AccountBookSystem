<template>
    <div class="merchant-management">
      <div class="page-header">
        <h2>商家管理</h2>
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon> 添加商家
        </el-button>
      </div>
      
      <!-- 搜索区域 -->
      <div class="search-area">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商家名称"
          class="search-input"
          clearable
          @clear="loadMerchants"
          @keyup.enter="searchMerchants"
        >
          <template #append>
            <el-button @click="searchMerchants">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
        
        <el-select 
          v-model="selectedCategory" 
          placeholder="按分类筛选" 
          clearable 
          @change="filterByCategory"
          class="category-filter"
        >
          <el-option
            v-for="item in categories"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </div>
      
      <!-- 商家列表 -->
      <el-card class="merchant-list-card">
        <el-table
          v-loading="loading"
          :data="merchants"
          border
          style="width: 100%"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="name" label="商家名称" width="150" />
          <el-table-column prop="categoryName" label="分类" width="120" />
          <el-table-column label="Logo" width="100">
            <template #default="scope">
              <el-image
                v-if="scope.row.logo"
                :src="scope.row.logo"
                style="width: 40px; height: 40px"
                fit="cover"
              />
              <span v-else>无</span>
            </template>
          </el-table-column>
          <el-table-column prop="address" label="地址" />
          <el-table-column prop="contact" label="联系方式" width="120" />
          <el-table-column prop="description" label="描述" show-overflow-tooltip />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button 
                size="small" 
                type="primary" 
                @click="showEditDialog(scope.row)"
              >
                编辑
              </el-button>
              <el-button 
                size="small" 
                type="danger" 
                @click="confirmDelete(scope.row)"
                :disabled="scope.row.isSystem === 1"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 空状态 -->
        <div v-if="merchants.length === 0 && !loading" class="empty-merchants">
          <el-empty description="暂无商家数据" />
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
      
      <!-- 添加/编辑商家对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="isEdit ? '编辑商家' : '添加商家'"
        width="500px"
      >
        <el-form
          ref="merchantFormRef"
          :model="merchantForm"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="商家名称" prop="name">
            <el-input v-model="merchantForm.name" />
          </el-form-item>
          
          <el-form-item label="分类" prop="categoryId">
            <el-select v-model="merchantForm.categoryId" placeholder="选择分类">
              <el-option
                v-for="item in categories"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="Logo">
            <el-input v-model="merchantForm.logo" placeholder="Logo URL" />
          </el-form-item>
          
          <el-form-item label="地址">
            <el-input v-model="merchantForm.address" />
          </el-form-item>
          
          <el-form-item label="联系方式">
            <el-input v-model="merchantForm.contact" />
          </el-form-item>
          
          <el-form-item label="描述">
            <el-input
              v-model="merchantForm.description"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          
          <el-form-item label="排序顺序">
            <el-input-number v-model="merchantForm.sortOrder" :min="0" />
          </el-form-item>
          
          <el-form-item label="是否默认">
            <el-switch v-model="merchantForm.isDefault" />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitForm">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted, computed } from 'vue';
  import { useRoute } from 'vue-router';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { Search, Plus } from '@element-plus/icons-vue';
  import merchantApi from '@/api/merchant';
  import categoryApi from '@/api/category';
  
  // 状态定义
  const loading = ref(false);
  const merchants = ref([]);
  const categories = ref([]);
  const total = ref(0);
  const currentPage = ref(1);
  const pageSize = ref(10);
  const searchKeyword = ref('');
  const selectedCategory = ref(null);
  const dialogVisible = ref(false);
  const isEdit = ref(false);
  const merchantFormRef = ref(null);
  
  // 获取当前账本ID和用户ID
  const route = useRoute();
  const currentBookId = ref(null);
  const userId = ref(localStorage.getItem('userId') || '');
  
  // 在onMounted之前添加一个函数来初始化bookId
  const initBookId = () => {
  // 尝试从多个来源获取bookId
  const routeBookId = route.params.bookId;
  const localBookId = localStorage.getItem('currentBookId');
  
  console.log('路由参数bookId:', routeBookId);
  console.log('localStorage中的currentBookId:', localBookId);
  
  if (routeBookId) {
  currentBookId.value = routeBookId;
  // 同时更新localStorage，确保一致性
  localStorage.setItem('currentBookId', routeBookId);
  console.log('使用路由参数中的bookId:', currentBookId.value);
  } else if (localBookId) {
  currentBookId.value = localBookId;
  console.log('使用localStorage中的bookId:', currentBookId.value);
  } else {
  console.error('无法获取账本ID，既没有路由参数也没有localStorage存储');
  }
  };
  
  // 生命周期钩子
  onMounted(async () => {
  // 初始化bookId
  initBookId();
  
  // 确保有账本ID
  if (!currentBookId.value) {
  ElMessage.error('未找到当前账本ID');
  return;
  }
  
  console.log('当前账本ID:', currentBookId.value);
  console.log('当前用户ID:', userId.value);
  
  // 加载分类数据
  await loadCategories();
  
  // 加载商家数据
  await loadMerchants();
  });
  
  // 加载分类数据
  const loadCategories = async () => {
    try {
      // 直接使用 axios 发送请求
      const response = await axios.get(`${config.mdBaseUrl}/category/list`, {
        params: {
          bookId: currentBookId.value,
          userId: userId.value,
          type: 'expense'
        },
        headers: { token: localStorage.getItem('token') }
      });
      
      if (response.data.code === 200) {
        // 只获取一级分类
        categories.value = response.data.data.filter(item => item.level === 1) || [];
      } else {
        ElMessage.error('加载分类失败: ' + response.data.msg);
      }
    } catch (error) {
      console.error('加载分类异常:', error);
      ElMessage.error('加载分类异常: ' + error.message);
    }
  };
  
  // 加载商家数据
  const loadMerchants = async () => {
    loading.value = true;
    try {
      // 再次检查必要参数
      if (!currentBookId.value) {
        ElMessage.error('缺少账本ID，无法加载商家数据');
        loading.value = false;
        return;
      }
      
      if (!userId.value) {
        ElMessage.error('缺少用户ID，无法加载商家数据');
        loading.value = false;
        return;
      }
      
      console.log('发送请求参数:', {
        bookId: currentBookId.value,
        userId: userId.value,
        page: currentPage.value,
        pageSize: pageSize.value,
        keyword: searchKeyword.value
      });
      
      const response = await merchantApi.getMerchantList(
        currentBookId.value,
        userId.value,
        currentPage.value,
        pageSize.value,
        searchKeyword.value
      );
      
      console.log('获取商家列表响应:', response);
      
      // 修改这里的响应处理方式
      if (response.data.code === 200) {
        merchants.value = response.data.data.list || [];
        total.value = response.data.data.total || 0;
      } else {
        ElMessage.error('加载商家失败: ' + (response.data.message || response.data.msg));
      }
    } catch (error) {
      console.error('加载商家异常:', error);
      ElMessage.error('加载商家异常: ' + error.message);
    } finally {
      loading.value = false;
    }
  };
  
  // 按分类筛选商家
  const filterByCategory = async () => {
    if (!selectedCategory.value) {
      // 如果清除了分类筛选，则加载所有商家
      currentPage.value = 1;
      await loadMerchants();
      return;
    }
    
    loading.value = true;
    try {
      const response = await merchantApi.getMerchantsByCategory(
        selectedCategory.value,
        currentBookId.value,
        userId.value
      );
      
      // 修改这里的响应处理方式
      if (response.data.code === 200) {
        merchants.value = response.data.data || [];
        total.value = merchants.value.length;
        // 禁用分页，因为已经获取了所有符合条件的数据
        currentPage.value = 1;
      } else {
        ElMessage.error('按分类筛选商家失败: ' + response.data.message || response.data.msg);
      }
    } catch (error) {
      console.error('按分类筛选商家异常:', error);
      ElMessage.error('按分类筛选商家异常: ' + error.message);
    } finally {
      loading.value = false;
    }
  };
  
  // 搜索商家
  const searchMerchants = () => {
    // 重置分类筛选
    selectedCategory.value = null;
    currentPage.value = 1;
    loadMerchants();
  };
  
  // 分页处理
  const handleSizeChange = (val) => {
    pageSize.value = val;
    loadMerchants();
  };
  
  const handleCurrentChange = (val) => {
    currentPage.value = val;
    loadMerchants();
  };
  
  // 表单数据
  const merchantForm = reactive({
    id: null,
    name: '',
    categoryId: null,
    logo: '',
    address: '',
    contact: '',
    description: '',
    sortOrder: 0,
    bookId: '', // 初始化为空字符串
    userId: '', // 初始化为空字符串
    isDefault: false,
    isSystem: 0
  });
  
  // 在showAddDialog和showEditDialog中正确设置这些值
  const showAddDialog = () => {
    isEdit.value = false;
    resetForm();
    // 确保使用最新的值
    merchantForm.bookId = currentBookId.value;
    merchantForm.userId = userId.value;
    dialogVisible.value = true;
  };
  
  // 显示编辑对话框
  const showEditDialog = (row) => {
    isEdit.value = true;
    resetForm();
    
    // 复制数据到表单
    Object.keys(merchantForm).forEach(key => {
      if (key in row) {
        merchantForm[key] = row[key];
      }
    });
    
    // 转换布尔值
    merchantForm.isDefault = row.isDefault === 1;
    
    dialogVisible.value = true;
  };
  
  // 重置表单
  const resetForm = () => {
    if (merchantFormRef.value) {
      merchantFormRef.value.resetFields();
    }
    
    Object.assign(merchantForm, {
      id: null,
      name: '',
      categoryId: null,
      logo: '',
      address: '',
      contact: '',
      description: '',
      sortOrder: 0,
      bookId: currentBookId.value,
      userId: userId.value,
      isDefault: false,
      isSystem: 0
    });
  };
  
// ... 前面的代码保持不变 ...

// 提交表单
const submitForm = async () => {
  if (!merchantFormRef.value) return;
  
  await merchantFormRef.value.validate(async (valid) => {
    if (valid) {
      // 转换布尔值为数字
      const formData = { ...merchantForm };
      formData.isDefault = formData.isDefault ? 1 : 0;
      
      try {
        let response;
        
        if (isEdit.value) {
          // 更新商家
          response = await merchantApi.updateMerchant(formData);
        } else {
          // 添加商家
          response = await merchantApi.addMerchant(formData);
        }
        
        // 修改这里的响应处理方式
        if (response.data.code === 200) {
          ElMessage.success(isEdit.value ? '商家更新成功' : '商家添加成功');
          dialogVisible.value = false;
          loadMerchants();
        } else {
          ElMessage.error((isEdit.value ? '更新' : '添加') + '商家失败: ' + response.data.message || response.data.msg);
        }
      } catch (error) {
        console.error((isEdit.value ? '更新' : '添加') + '商家异常:', error);
        ElMessage.error((isEdit.value ? '更新' : '添加') + '商家异常: ' + error.message);
      }
    }
  });
};

// 确认删除
const confirmDelete = (row) => {
  if (row.isSystem === 1) {
    ElMessage.warning('系统预设商家不可删除');
    return;
  }
  
  ElMessageBox.confirm(
    `确定要删除商家 "${row.name}" 吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(async () => {
      try {
        const response = await merchantApi.deleteMerchant(row.id, userId.value);
        
        // 修改这里的响应处理方式
        if (response.data.code === 200) {
          ElMessage.success('商家删除成功');
          loadMerchants();
        } else {
          ElMessage.error('删除商家失败: ' + response.data.message || response.data.msg);
        }
      } catch (error) {
        console.error('删除商家异常:', error);
        ElMessage.error('删除商家异常: ' + error.message);
      }
    })
    .catch(() => {
      // 取消删除，不做任何操作
    });
};
</script>

<style scoped>
.merchant-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-area {
  display: flex;
  margin-bottom: 20px;
  gap: 15px;
}

.search-input {
  width: 300px;
}

.category-filter {
  width: 200px;
}

.merchant-list-card {
  margin-bottom: 20px;
}

.empty-merchants {
  margin-top: 20px;
  text-align: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>