package com.lq.pwdmanage.service;

import com.lq.pwdmanage.bean.Page;
import com.lq.pwdmanage.bean.PwdManage;


/**
 * 管理服务接口
 * @author LQ
 * @date 2020/6/10 10:04
 */
public interface ManageService {

    /**
     * 分页列表
     * @param pageIndex
     * @param pageSize
     * @param likeAll
     * @return
     */
    Page<PwdManage> pageList(int pageIndex, int pageSize, String likeAll);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    PwdManage selectById(String id);

    /**
     * 新增
     * @param pwdManage
     * @return
     */
    boolean add(PwdManage pwdManage);

    /**
     * 更新
     * @param pwdManage
     * @return
     */
    boolean update(PwdManage pwdManage);

    /**
     * 删除
     * @param ids
     * @return
     */
    boolean delete(String ids);

    /**
     * 刷新缓存
     */
    void refreshCache();

}
