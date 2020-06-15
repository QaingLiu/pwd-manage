package com.lq.pwdmanage.util;

import com.lq.pwdmanage.bean.PwdManage;
import com.lq.pwdmanage.bean.SecurityQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 * @author LQ
 * @date 2020/6/10 10:06
 */
public class CommonUtils {

    /**
     * 密保问题数量
     */
    public static final int SECURITY_QUESTION_SIZE = 4;

    /**
     * 获取为null的字符串返回为空
     * @param str
     * @return
     */
    public static String emptyStr(String str) {
        return str == null ? "" : str;
    }

    /**
     * 字符是否为null或者""
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 初始化密保问题
     * @param size 初始个数
     * @return
     */
    public static List<SecurityQuestion> initSecurityQuestion(int size){
        List<SecurityQuestion> securityQuestions = new ArrayList<>();
        for(int i = 0; i < size; i++){
            securityQuestions.add(new SecurityQuestion());
        }
        return securityQuestions;
    }

}
