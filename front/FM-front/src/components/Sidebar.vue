<template>
  <div class="sidebar">
    <!-- 记一笔按钮 -->
    <div class="add-record">
      <button class="add-record-btn" @click="openRecordDrawer">记一笔</button>
    </div>

    <!-- 主导航 -->
    <div class="main-nav">
      <div
          v-for="item in mainNavItems"
          :key="item.id"
          :class="['nav-item', { active: activePage === item.id }]"
          @click="changePage(item.id)"
      >
        <span class="nav-icon">{{ item.icon }}</span>
        <span class="nav-text">{{ item.name }}</span>
      </div>
    </div>

    <!-- 分类标签折叠菜单 -->
    <div class="category-menu">
      <div class="category-header" @click="toggleCategoryMenu">
        <span class="nav-icon">🏷️</span>
        <span class="nav-text">分类标签</span>
        <span class="nav-icon-right">{{ categoryMenuOpen ? '▼' : '▶' }}</span>
      </div>

      <div v-if="categoryMenuOpen" class="category-submenu">
        <div
            v-for="item in categoryItems"
            :key="item.id"
            :class="['nav-item', { active: activePage === item.id }]"
            @click="changePage(item.id)"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span class="nav-text">{{ item.name }}</span>
        </div>
      </div>
    </div>

    <!-- 设置选项 -->
    <div class="settings-nav">
      <div
          :class="['nav-item', { active: activePage === 'settings' }]"
          @click="changePage('settings')"
      >
        <span class="nav-icon">⚙️</span>
        <span class="nav-text">设置</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  props: {
    activePage: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      categoryMenuOpen: false,
      mainNavItems: [
        { id: 'home', name: '首页', icon: '🏠' },
        { id: 'transactions', name: '流水', icon: '📝' },
        { id: 'reports', name: '报表', icon: '📊' }
      ],
      categoryItems: [
        { id: 'categories', name: '收支分类', icon: '📋' },
        { id: 'accounts', name: '账户管理', icon: '💳' },
        { id: 'members', name: '成员管理', icon: '👥' },
        { id: 'merchants', name: '商家管理', icon: '🏬' },
        { id: 'projects', name: '项目管理', icon: '📁' }
      ]
    };
  },
  methods: {
    changePage(pageId) {
      this.$emit('change-page', pageId);
    },
    openRecordDrawer() {
      this.$emit('open-record-drawer');
    },
    toggleCategoryMenu() {
      this.categoryMenuOpen = !this.categoryMenuOpen;
    }
  }
};
</script>

<style scoped>
.sidebar {
  width: 220px;
  background-color: #f5f7fa;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 20px 10px;
}

.add-record {
  margin-bottom: 30px;
}

.add-record-btn {
  width: 100%;
  padding: 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-record-btn:hover {
  background-color: #66b1ff;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.nav-item:hover {
  background-color: #e6f1fc;
}

.nav-item.active {
  background-color: #ecf5ff;
  color: #409eff;
}

.nav-icon {
  margin-right: 10px;
  font-size: 18px;
}

.nav-text {
  flex-grow: 1;
}

.nav-icon-right {
  font-size: 12px;
}

.category-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.category-header:hover {
  background-color: #e6f1fc;
}

.category-submenu {
  padding-left: 10px;
}

.category-submenu .nav-item {
  padding: 10px 16px;
}

.settings-nav {
  margin-top: auto;
}
</style>