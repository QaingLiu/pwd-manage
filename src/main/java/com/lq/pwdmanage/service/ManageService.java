package com.lq.pwdmanage.service;

import com.lq.pwdmanage.bean.Page;
import com.lq.pwdmanage.bean.PwdManage;


/**
 * 管理服务接口
 *
 * @author LQ
 * @date 2020/6/10 10:04
 */
public interface ManageService {

    /**
     * 分页列表
     *
     * @param pageIndex 第几页
     * @param pageSize  每页多少条
     * @param likeAll   查询条件
     * @return
     */
    Page<PwdManage> pageList(int pageIndex, int pageSize, String likeAll);

    /**
     * 根据id查询
     *
     * @param id 唯一标识
     * @return
     */
    PwdManage selectById(String id);

    /**
     * 新增
     *
     * @param pwdManage
     * @return
     */
    boolean add(PwdManage pwdManage);

    /**
     * 更新
     *
     * @param pwdManage
     * @return
     */
    boolean update(PwdManage pwdManage);

    /**
     * 删除
     *
     * @param ids 多个标识逗号(,)分隔
     * @return
     */
    boolean delete(String ids);

    /**
     * 刷新缓存
     */
    void refreshCache();

}
