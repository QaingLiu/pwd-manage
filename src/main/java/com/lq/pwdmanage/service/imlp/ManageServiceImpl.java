package com.lq.pwdmanage.service.imlp;

import com.lq.pwdmanage.bean.Page;
import com.lq.pwdmanage.bean.PwdManage;
import com.lq.pwdmanage.cache.CacheCliet;
import com.lq.pwdmanage.cache.CacheKeys;
import com.lq.pwdmanage.dao.ManageDao;
import com.lq.pwdmanage.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 管理服务
 * @author LQ
 * @date 2020/6/10 10:04
 */
@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private ManageDao manageDao;

    @Autowired
    private CacheCliet cacheCliet;

    @Override
    public Page<PwdManage> pageList(int pageIndex, int pageSize, String likeAll) {
        Page<PwdManage> page = new Page<>(pageIndex, pageSize);

        List<PwdManage> list = (List<PwdManage>) cacheCliet.get(CacheKeys.PWDMANAGE_DATAS_KEY);
        if (!list.isEmpty() && !StringUtils.isEmpty(likeAll)) {
            Iterator<PwdManage> iterator = list.iterator();
            while (iterator.hasNext()) {
                PwdManage pwdManage = iterator.next();
                if (!filter(likeAll, pwdManage)) {
                    iterator.remove();
                }
            }
        }

        //分页处理
        //暂不分页....

        page.setRecords(list);
        page.setTotal(list.size());
        return page;
    }

    /**
     * 过滤
     * @param likeAll
     * @param pwdManage
     * @return true 包含，false 不包含
     */
    private boolean filter(String likeAll, PwdManage pwdManage) {
        String websiteUrl = pwdManage.getWebsiteUrl();
        if (websiteUrl != null && websiteUrl.contains(likeAll)) {
            return true;
        }

        String websiteName = pwdManage.getWebsiteName();
        if (websiteName != null && websiteName.contains(likeAll)) {
            return true;
        }

        String account = pwdManage.getAccount();
        if (account != null && account.contains(likeAll)) {
            return true;
        }

        String userName = pwdManage.getUserName();
        if (userName != null && userName.contains(likeAll)) {
            return true;
        }

        String qq = pwdManage.getQq();
        if (qq != null && qq.contains(likeAll)) {
            return true;
        }

        String email = pwdManage.getEmail();
        if (email != null && email.contains(likeAll)) {
            return true;
        }

        String mobileNumber = pwdManage.getMobileNumber();
        if (mobileNumber != null && mobileNumber.contains(likeAll)) {
            return true;
        }

        String remarks = pwdManage.getRemarks();
        if (remarks != null && remarks.contains(likeAll)) {
            return true;
        }

        return false;
    }

    @Override
    public PwdManage selectById(String id) {
        return manageDao.findById(id);
    }

    @Override
    public boolean add(PwdManage pwdManage) {
        try {
            //生成id
            pwdManage.setId(UUID.randomUUID().toString().replace("-", ""));
            manageDao.add(pwdManage);

            //刷新缓存
            refreshCache();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(PwdManage pwdManage) {
        try {
            manageDao.update(pwdManage);

            //刷新缓存
            refreshCache();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(String ids) {
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                manageDao.delete(id);
            }

            //刷新缓存
            refreshCache();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void refreshCache() {
        manageDao.pushCache();
    }

}
