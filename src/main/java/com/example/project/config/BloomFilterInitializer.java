package com.example.project.config;

import com.example.project.repository.UserRepository;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 注意：！！！！这个配置仅能够在数据量较少时进行使用，否则会导致系统启动速度缓慢
 * 布隆过滤器初始化器，在应用启动时加载已存在的用户数据到布隆过滤器
 */
@Component
public class BloomFilterInitializer implements  ApplicationRunner {

    @Autowired
    private RBloomFilter<String> userBloomFilter;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        // 预热布隆过滤器：加载所有已存在的用户名
        List<String> allUserNames = userRepository.FindAllUserNames();
        for (String userName : allUserNames) {
            userBloomFilter.add(userName);
        }
        System.out.println("布隆过滤器预热完成，共加载" + allUserNames.size() + "个用户");
    }
}