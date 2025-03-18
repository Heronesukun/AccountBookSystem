<template>
    <div class="member-management-container">
      <div class="page-header">
        <h2>成员管理</h2>
        <el-button type="primary" @click="showAddMemberDialog">添加成员</el-button>
      </div>
  
      <!-- 成员列表 -->
      <el-card class="member-list-card">
        <template #header>
          <div class="card-header">
            <span>账本成员列表</span>
          </div>
        </template>
        
        <el-table :data="members" style="width: 100%" v-loading="loading">
          <el-table-column prop="username" label="用户名" width="180" />
          <el-table-column prop="role" label="角色" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.role === 'master' ? 'danger' : 'success'">
                {{ scope.row.role === 'master' ? '创建者' : '参与者' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="position" label="位置" width="120">
            <template #default="scope">
              <span v-if="scope.row.role === 'participant'">{{ scope.row.position }}</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button 
                v-if="scope.row.role === 'participant'" 
                size="small" 
                type="danger" 
                @click="confirmRemoveMember(scope.row)"
                :disabled="!isBookMaster"
              >
                移除
              </el-button>
              <el-button 
                v-if="scope.row.role === 'participant'" 
                size="small" 
                type="primary" 
                @click="showUpdateMemberDialog(scope.row)"
                :disabled="!isBookMaster"
              >
                修改
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div v-if="members.length === 0 && !loading" class="empty-members">
          <el-empty description="暂无成员" />
        </div>
      </el-card>
  
      <!-- 添加成员对话框 -->
      <el-dialog
        title="添加成员"
        v-model="addDialogVisible"
        width="400px"
      >
        <el-form :model="memberForm" :rules="rules" ref="memberFormRef" label-width="100px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="memberForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="位置" prop="position">
            <el-select v-model="memberForm.position" placeholder="请选择位置">
              <el-option 
                v-for="pos in availablePositions" 
                :key="pos" 
                :label="`位置 ${pos}`" 
                :value="pos"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="addDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="addMember">确定</el-button>
          </span>
        </template>
      </el-dialog>
  
      <!-- 更新成员对话框 -->
      <el-dialog
        title="更新成员"
        v-model="updateDialogVisible"
        width="400px"
      >
        <el-form :model="updateForm" :rules="rules" ref="updateFormRef" label-width="100px">
          <el-form-item label="原用户名">
            <el-input v-model="updateForm.oldUsername" disabled></el-input>
          </el-form-item>
          <el-form-item label="新用户名" prop="newUsername">
            <el-input v-model="updateForm.newUsername" placeholder="请输入新用户名"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="updateDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="updateMember">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, computed } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import bookMemberRequest from '@/api/bookMember.js';
  
  // 接收父组件传递的参数
  const props = defineProps({
    bookId: {
      type: [String, Number],
      required: true
    }
  });
  
  // 状态变量
  const loading = ref(false);
  const members = ref([]);
  const addDialogVisible = ref(false);
  const updateDialogVisible = ref(false);
  const memberForm = ref({
    username: '',
    position: ''
  });
  const updateForm = ref({
    oldUsername: '',
    newUsername: '',
    position: null
  });
  const memberFormRef = ref(null);
  const updateFormRef = ref(null);
  
  // 表单验证规则
  const rules = {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    newUsername: [
      { required: true, message: '请输入新用户名', trigger: 'blur' },
      { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    position: [
      { required: true, message: '请选择位置', trigger: 'change' }
    ]
  };
  
  // 计算当前登录用户是否为账本创建者
  const isBookMaster = computed(() => {
    const currentUser = localStorage.getItem('username');
    const master = members.value.find(m => m.role === 'master');
    return master && master.username === currentUser;
  });
  
  // 计算可用的位置
  const availablePositions = computed(() => {
    const positions = [1, 2, 3, 4];
    const usedPositions = members.value
      .filter(m => m.role === 'participant')
      .map(m => m.position);
    
    return positions.filter(p => !usedPositions.includes(p));
  });
  
  // 加载成员数据
  const loadMembers = async () => {
    loading.value = true;
    try {
      console.log('正在加载成员数据，bookId:', props.bookId);
      const response = await bookMemberRequest.getBookMembers(props.bookId);
      console.log('成员数据响应:', response);
      
      if (response.data.code === 200) {
        members.value = response.data.data || [];
      } else {
        ElMessage.error(response.data.msg || '加载成员失败');
      }
    } catch (error) {
      console.error('加载成员异常:', error);
      ElMessage.error('加载成员异常: ' + (error.message || '未知错误'));
    } finally {
      loading.value = false;
    }
  };
  
  // 显示添加成员对话框
  const showAddMemberDialog = () => {
    if (!isBookMaster.value) {
      ElMessage.warning('只有账本创建者可以添加成员');
      return;
    }
    
    if (availablePositions.value.length === 0) {
      ElMessage.warning('已达到最大成员数量限制');
      return;
    }
    
    memberForm.value = {
      username: '',
      position: availablePositions.value[0]
    };
    
    addDialogVisible.value = true;
  };
  
  // 添加成员
  const addMember = async () => {
    if (!memberFormRef.value) return;
    
    await memberFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          console.log('添加成员请求参数:', {
            bookId: props.bookId,
            memberName: memberForm.value.username,
            position: memberForm.value.position
          });
          
          const response = await bookMemberRequest.addBookMember(
            props.bookId,
            memberForm.value.username,
            memberForm.value.position
          );
          
          console.log('添加成员响应:', response);
          
          if (response.data.code === 200) {
            ElMessage.success('成员添加成功');
            addDialogVisible.value = false;
            loadMembers(); // 重新加载成员列表
          } else {
            ElMessage.error(response.data.msg || '添加成员失败');
          }
        } catch (error) {
          console.error('添加成员异常:', error);
          ElMessage.error('添加成员异常: ' + (error.message || '未知错误'));
        }
      }
    });
  };
  
  // 确认移除成员
  const confirmRemoveMember = (member) => {
    if (!isBookMaster.value) {
      ElMessage.warning('只有账本创建者可以移除成员');
      return;
    }
    
    ElMessageBox.confirm(
      `确定要移除成员 ${member.username} 吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(async () => {
      try {
        console.log('移除成员请求参数:', {
          bookId: props.bookId,
          position: member.position
        });
        
        const response = await bookMemberRequest.removeBookMember(
          props.bookId,
          member.position
        );
        
        console.log('移除成员响应:', response);
        
        if (response.data.code === 200) {
          ElMessage.success('成员移除成功');
          loadMembers(); // 重新加载成员列表
        } else {
          ElMessage.error(response.data.msg || '移除成员失败');
        }
      } catch (error) {
        console.error('移除成员异常:', error);
        ElMessage.error('移除成员异常: ' + (error.message || '未知错误'));
      }
    }).catch(() => {
      // 取消操作
    });
  };
  
  // 显示更新成员对话框
  const showUpdateMemberDialog = (member) => {
    if (!isBookMaster.value) {
      ElMessage.warning('只有账本创建者可以修改成员');
      return;
    }
    
    updateForm.value = {
      oldUsername: member.username,
      newUsername: '',
      position: member.position
    };
    
    updateDialogVisible.value = true;
  };
  
  // 更新成员
  const updateMember = async () => {
    if (!updateFormRef.value) return;
    
    await updateFormRef.value.validate(async (valid) => {
      if (valid) {
        try {
          console.log('更新成员请求参数:', {
            bookId: props.bookId,
            oldMemberName: updateForm.value.oldUsername,
            newMemberName: updateForm.value.newUsername
          });
          
          const response = await bookMemberRequest.updateBookMember(
            props.bookId,
            updateForm.value.oldUsername,
            updateForm.value.newUsername
          );
          
          console.log('更新成员响应:', response);
          
          if (response.data.code === 200) {
            ElMessage.success('成员更新成功');
            updateDialogVisible.value = false;
            loadMembers(); // 重新加载成员列表
          } else {
            ElMessage.error(response.data.msg || '更新成员失败');
          }
        } catch (error) {
          console.error('更新成员异常:', error);
          ElMessage.error('更新成员异常: ' + (error.message || '未知错误'));
        }
      }
    });
  };
  
// 添加 fetchData 方法，以便 LedgerDetail.vue 可以调用刷新数据
const fetchData = () => {
  loadMembers();
};

// 暴露方法给父组件
defineExpose({
  fetchData
});

// 页面加载时获取数据
onMounted(() => {
  console.log('MemberManagementPage 组件已挂载，bookId:', props.bookId);
  loadMembers();
});
</script>

<style scoped>
.member-management-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.member-list-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-members {
  margin-top: 20px;
  text-align: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

.account-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.account-actions {
  display: flex;
  gap: 8px;
}
</style>