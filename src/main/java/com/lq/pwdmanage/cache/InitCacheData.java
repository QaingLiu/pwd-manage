package com.lq.pwdmanage.cache;

import com.lq.pwdmanage.bean.PwdManage;
import com.lq.pwdmanage.dao.ManageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化缓存数据
 * @author LQ
 * @date 2020/6/10 11:27
 */
@Component
public class InitCacheData implements ApplicationRunner {

    @Autowired
    private ManageDao manageDao;

    @Autowired
    private CacheCliet cacheCliet;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //获取所有数据，放入缓存
        List<PwdManage> list = manageDao.findAll();
        cacheCliet.put(CacheKeys.PWDMANAGE_DATAS_KEY, list);
    }
}
