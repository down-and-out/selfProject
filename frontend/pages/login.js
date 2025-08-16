// 登录页面组件
const LoginPage = {
    template: `
    <div class="container">
      <div class="decoration">
        <h1>欢迎回来</h1>
        <p>高效、安全、易用的系统登录体验，助您快速进入工作状态</p>
        <div class="illustration">
          <svg viewBox="0 0 500 300" xmlns="http://www.w3.org/2000/svg">
            <rect x="50" y="100" width="400" height="150" rx="20" fill="#5D34AB" opacity="0.8"/>
            <rect x="80" y="130" width="340" height="40" rx="10" fill="#fff"/>
            <rect x="80" y="190" width="340" height="40" rx="10" fill="#fff"/>
            <circle cx="400" cy="150" r="30" fill="#FFD166"/>
            <circle cx="400" cy="210" r="30" fill="#FFD166"/>
            <circle cx="100" cy="150" r="20" fill="#EF476F"/>
            <circle cx="100" cy="210" r="20" fill="#06D6A0"/>
          </svg>
        </div>
      </div>

      <div class="content">
        <div class="logo">
          <div class="logo-icon">V</div>
          <div class="logo-text">VueSystem</div>
        </div>

        <h2>账号登录</h2>
        <p class="subtitle">请输入您的账号和密码</p>

        <form @submit.prevent="login">
          <div class="form-group">
            <label for="userName">用户名</label>
            <input
              type="text"
              id="userName"
              v-model="userName"
              placeholder="请输入用户名"
              :class="{'error-input': errors.userName}"
            >
            <div class="error">{{ errors.userName }}</div>
          </div>

          <div class="form-group">
            <label for="password">密码</label>
            <input
              type="password"
              id="password"
              v-model="password"
              placeholder="请输入密码"
              :class="{'error-input': errors.password}"
            >
            <div class="error">{{ errors.password }}</div>
          </div>

          <button type="submit" class="btn">登录</button>
        </form>

        <div class="footer">
          <p>还没有账号? <a href="#">立即注册</a></p>
          <p><a href="#">忘记密码?</a></p>
        </div>
      </div>
    </div>
  `,
    data() {
        return {
            userName: '',
            password: '',
            errors: {
                userName: '',
                password: ''
            }
        }
    },
    methods: {
        validateForm() {
            let isValid = true;
            this.errors = { userName: '', password: '' };

            if (!this.userName) {
                this.errors.userName = '用户名不能为空';
                isValid = false;
            } else if (this.userName.length < 4) {
                this.errors.userName = '用户名长度至少4个字符';
                isValid = false;
            }

            if (!this.password) {
                this.errors.password = '密码不能为空';
                isValid = false;
            } else if (this.password.length < 6) {
                this.errors.password = '密码长度至少6个字符';
                isValid = false;
            }

            return isValid;
        },
        // 修改login方法
        login() {
            if (this.validateForm()) {
                // 准备发送到后端的登录数据
                const loginData = {
                    userName: this.userName,
                    password: this.password
                };

                // 发送登录请求到后端接口
                fetch('http://localhost:8080/AqTest/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(loginData)
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('网络响应异常');
                    }
                    return response.json();
                })
                .then(data => {
                    // 检查返回的status是否为200000（与后端保持一致）
                    if (data.status === 200000) {
                        // 登录成功，保存用户信息
                        localStorage.setItem('userInfo', JSON.stringify({ userName: this.userName }));
                        // 跳转到dashboard页面
                        this.$router.push('/dashboard');
                    } else {
                        // 登录失败，显示错误信息
                        alert(data.message || '登录失败，请重试');
                    }
                })
                .catch(error => {
                    console.error('登录请求失败:', error);
                    alert('登录请求失败，请检查网络连接或稍后再试');
                });
            }
        }
    }
};