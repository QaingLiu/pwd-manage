package com.lq.pwdmanage.controller;

import com.lq.pwdmanage.bean.Page;
import com.lq.pwdmanage.bean.PwdManage;
import com.lq.pwdmanage.bean.SecurityQuestion;
import com.lq.pwdmanage.service.ManageService;
import com.lq.pwdmanage.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页 控制器
 * @author LQ
 * @date 2020/6/10 9:32
 */
@Controller
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ManageService manageService;

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping(value = {"/", "/index", "/index.html"})
    public String index(Model model) {
        Page<PwdManage> page = manageService.pageList(1, 10, null);
        model.addAttribute("page", page);
        return "index";
    }

    /**
     * 列表
     * @param pageIndex
     * @param pageSize
     * @param likeAll
     * @return
     */
    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "10") int pageSize,
                       String likeAll, Model model) {
        Page<PwdManage> page = manageService.pageList(pageIndex, pageSize, likeAll);
        model.addAttribute("page", page);
        model.addAttribute("likeAll", likeAll);
        return "index";
    }

    /**
     * 新增 预览页面
     * @return
     */
    @GetMapping("/add-view")
    public String addView(Model model) {
        PwdManage pwdManage = new PwdManage();
        pwdManage.setSecurityQuestions(CommonUtils.initSecurityQuestion(CommonUtils.SECURITY_QUESTION_SIZE));
        model.addAttribute("pwdManage", pwdManage);
        model.addAttribute("type", "add");
        return "form";
    }

    /**
     * 新增
     * @param model
     * @param pwdManage
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public Object add(Model model, @RequestBody PwdManage pwdManage) {
        log.info("新增: {}", pwdManage.getWebsiteName());
        manageService.add(pwdManage);
        return true;
    }

    /**
     * 编辑 预览页面
     * @return
     */
    @GetMapping("/edit-view/{id}")
    public String editView(Model model, @PathVariable String id) {
        PwdManage pwdManage = manageService.selectById(id);
        if (pwdManage == null) {
            throw new RuntimeException("id: " + id + " 不存在!");
        }

        List<SecurityQuestion> securityQuestions = pwdManage.getSecurityQuestions();
        if(securityQuestions == null || securityQuestions.isEmpty()){
            pwdManage.setSecurityQuestions(CommonUtils.initSecurityQuestion(CommonUtils.SECURITY_QUESTION_SIZE));
        }else{
            int size = securityQuestions.size();
            securityQuestions.addAll(CommonUtils.initSecurityQuestion(CommonUtils.SECURITY_QUESTION_SIZE - size));
        }

        model.addAttribute("pwdManage", pwdManage);
        model.addAttribute("type", "edit");
        return "form";
    }

    /**
     * 更新
     * @param model
     * @param pwdManage
     * @return
     */
    @ResponseBody
    @PostMapping("/update")
    public Object update(Model model, @RequestBody PwdManage pwdManage) {
        log.info("更新: {}", pwdManage.getWebsiteName());
        manageService.update(pwdManage);
        return true;
    }

    /**
     * 删除
     * @param model
     * @param ids
     * @return
     */
    @ResponseBody
    @DeleteMapping("/delete")
    public Object delete(Model model, @RequestParam String ids) {
        log.info("删除: {}", ids);
        manageService.delete(ids);
        return true;
    }

    /**
     * 刷新
     * @return
     */
    @ResponseBody
    @GetMapping("/refresh")
    public Object refresh() {
        manageService.refreshCache();
        return true;
    }

    /**
     * 查看预览页面
     * @return
     */
    @GetMapping("/preview/{id}")
    public String preview(Model model, @PathVariable String id) {
        PwdManage pwdManage = manageService.selectById(id);
        if (pwdManage == null) {
            throw new RuntimeException("id: " + id + " 不存在!");
        }
        model.addAttribute("pwdManage", pwdManage);
        return "preview";
    }

    /**
     * 随机生成密码 预览页面
     * @return
     */
    @GetMapping("/ram-pwd-view")
    public String ramPwdView(Model model) {
        return "ram-password";
    }

}


