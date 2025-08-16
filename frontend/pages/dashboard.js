// 仪表盘页面组件
const DashboardPage = {
    template: `
    <div class="dashboard">
      <div class="dashboard-header">
        <h1 class="welcome">欢迎回来，{{ username }}！</h1>
        <button class="logout-btn" @click="logout">退出登录</button>
      </div>

      <div class="stats">
        <div class="stat-card">
          <div class="stat-title">今日访问量</div>
          <div class="stat-value">1,248</div>
        </div>
        <div class="stat-card">
          <div class="stat-title">待处理任务</div>
          <div class="stat-value">18</div>
        </div>
        <div class="stat-card">
          <div class="stat-title">新消息</div>
          <div class="stat-value">5</div>
        </div>
        <div class="stat-card">
          <div class="stat-title">项目进度</div>
          <div class="stat-value">78%</div>
        </div>
      </div>

      <div class="recent-activity">
        <h2 class="activity-title">最近活动</h2>
        <div class="activity-list">
          <div class="activity-item">
            <div class="activity-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M12 8V12L15 15" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="activity-content">
              <div class="activity-text">您更新了个人资料信息</div>
              <div class="activity-time">10分钟前</div>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M17 21V19C17 17.9391 16.5786 16.9217 15.8284 16.1716C15.0783 15.4214 14.0609 15 13 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M9 11C11.2091 11 13 9.20914 13 7C13 4.79086 11.2091 3 9 3C6.79086 3 5 4.79086 5 7C5 9.20914 6.79086 11 9 11Z" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M23 21V19C22.9993 18.1137 22.7044 17.2528 22.1614 16.5523C21.6184 15.8519 20.8581 15.3516 20 15.13" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M17 3.13C17.8604 3.3503 18.623 3.8507 19.1676 4.55231C19.7122 5.25392 20.0078 6.11683 20.0078 7.005C20.0078 7.89317 19.7122 8.75608 19.1676 9.45769C18.623 10.1593 17.8604 10.6597 17 10.88" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="activity-content">
              <div class="activity-text">新用户 "john_doe" 已注册</div>
              <div class="activity-time">2小时前</div>
            </div>
          </div>
          <div class="activity-item">
            <div class="activity-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M22 16.92V19.92C22 20.22 21.78 20.44 21.48 20.44H18.48C17.92 20.44 17.48 19.99 17.51 19.43C17.64 17.57 18.84 16.11 20.66 15.8C21.24 15.7 21.8 15.97 22.1 16.48C22.23 16.71 22.27 16.99 22.22 17.25L22 16.92Z" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M20.9599 14.7999C19.8399 14.7999 18.9299 13.8799 18.9299 12.7599C18.9299 11.6399 19.8399 10.7199 20.9599 10.7199C22.0799 10.7199 22.9999 11.6399 22.9999 12.7599C22.9999 13.8799 22.0799 14.7999 20.9599 14.7999Z" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M12.5301 6.51L13.0601 8.53C13.1201 8.78 13.3801 8.94 13.6501 8.94H15.6701C15.7601 8.94 15.7601 8.94 15.8501 8.94C16.0201 8.98 16.1601 9.11 16.2001 9.28C16.2401 9.45 16.1801 9.63 16.0501 9.75L14.4601 11.16C14.2501 11.34 14.1501 11.63 14.2101 11.91L14.6001 13.81C14.6301 13.96 14.5901 14.13 14.4901 14.25C14.4001 14.37 14.2701 14.44 14.1201 14.44C14.0501 14.44 13.9801 14.43 13.9201 14.41L12.0001 13.71C11.7401 13.62 11.4501 13.62 11.1901 13.71L9.27006 14.41C9.12006 14.46 8.95006 14.44 8.82006 14.34C8.70006 14.24 8.64006 14.08 8.65006 13.92L8.79006 11.91C8.83006 11.63 8.73006 11.34 8.52006 11.16L6.93006 9.75C6.80006 9.63 6.74006 9.45 6.78006 9.28C6.82006 9.11 6.96006 8.98 7.13006 8.94H9.33006C9.60006 8.94 9.86006 8.78 9.93006 8.53L10.4601 6.51C10.5201 6.32 10.6901 6.19 10.8901 6.19H12.1001C12.3001 6.19 12.4701 6.32 12.5301 6.51Z" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M7 16.92V19.92C7 20.22 7.22 20.44 7.52 20.44H10.52C11.08 20.44 11.52 19.99 11.49 19.43C11.36 17.57 10.16 16.11 8.34 15.8C7.76 15.7 7.2 15.97 6.9 16.48C6.77 16.71 6.73 16.99 6.78 17.25L7 16.92Z" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M3.04001 14.7999C4.16001 14.7999 5.07001 13.8799 5.07001 12.7599C5.07001 11.6399 4.16001 10.7199 3.04001 10.7199C1.92001 10.7199 1 11.6399 1 12.7599C1 13.8799 1.92001 14.7999 3.04001 14.7999Z" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <div class="activity-content">
              <div class="activity-text">系统升级至 Vue 3.2 版本</div>
              <div class="activity-time">昨天</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
    // 修改data方法
    computed: {
        username() {
            const userInfo = JSON.parse(localStorage.getItem('userInfo'));
            return userInfo ? userInfo.userName : '管理员';
        }
    },
    methods: {
        logout() {
            // 清除登录状态
            localStorage.removeItem('userInfo');
            // 跳转到登录页面
            this.$router.push('/');
        }
    }
};