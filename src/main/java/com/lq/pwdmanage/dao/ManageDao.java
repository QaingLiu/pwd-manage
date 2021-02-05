package com.lq.pwdmanage.dao;

import com.lq.pwdmanage.bean.PwdManage;
import com.lq.pwdmanage.bean.SecurityQuestion;
import com.lq.pwdmanage.cache.CacheCliet;
import com.lq.pwdmanage.cache.CacheKeys;
import com.lq.pwdmanage.util.CommonUtils;
import com.lq.pwdmanage.util.DESUtil;
import com.lq.pwdmanage.util.RSAUtils;
import com.lq.pwdmanage.util.XMLUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 管理数据操作
 *
 * @author LQ
 * @date 2020/6/10 10:05
 */
@Repository
public class ManageDao {

    @Autowired
    private CacheCliet cacheCliet;

    @Value("${xml-path}")
    private String xmlPath;

    /**
     * 不处理字段的白名单
     */
    public static final List<String> WHITE_LIST = new ArrayList<>();

    /**
     * 需要进行DES加/解密处理的字段
     */
    public static final List<String> DES_LIST = new ArrayList<>();

    static {
        WHITE_LIST.add("serialVersionUID");
        WHITE_LIST.add("id");
        WHITE_LIST.add("securityQuestions");

        DES_LIST.add("pwd");
        DES_LIST.add("email");
        DES_LIST.add("mobileNumber");
    }

    /**
     * 获取全部
     *
     * @return
     */
    public List<PwdManage> findAll() {
        List<PwdManage> list = new ArrayList<>();

        Document document = XMLUtils.getDocument(xmlPath);
        //根节点
        Element rootElement = document.getRootElement();
        Iterator<Element> passwordIt = rootElement.elementIterator("password");
        while (passwordIt.hasNext()) {
            Element passwordEle = passwordIt.next();
            if (passwordEle == null) {
                continue;
            }
            PwdManage pwdManage = ConvertToBean(passwordEle);

            list.add(pwdManage);
        }

        return list;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public PwdManage findById(String id) {
        if (id == null) {
            return null;
        }
        PwdManage pwdManage = null;

        Document document = XMLUtils.getDocument(xmlPath);
        //根据属性id查询
        String xpathExpression = "//password[@id='" + id + "']";
        Element passwordEle = (Element) document.selectSingleNode(xpathExpression);
        if (passwordEle != null) {
            pwdManage = ConvertToBean(passwordEle);
        }

        return pwdManage;
    }

    /**
     * Element节点转为PwdManage对象
     *
     * @param passwordEle
     * @return
     */
    private PwdManage ConvertToBean(Element passwordEle) {
        PwdManage pwdManage = new PwdManage();

        Class<? extends PwdManage> pwdManageClass = pwdManage.getClass();
        Field[] fields = pwdManageClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if (WHITE_LIST.contains(name)) {
                continue;
            }

            //set方法获取值
            String methodName = "set" + (name.substring(0, 1).toUpperCase() + name.substring(1));
            try {
                Method method = pwdManageClass.getMethod(methodName, field.getType());

                //获取节点值
                String value = passwordEle.elementText(name);

                //需要解密的
                if (DES_LIST.contains(name) && value != null) {
                    //先解密
                    value = DESUtil.decrypt(value);

                    //再加密
                    value = RSAUtils.encryptByPrivateKey(value);
                }

                method.invoke(pwdManage, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //id
        String id = passwordEle.attributeValue("id");
        pwdManage.setId(id);

        //密保问题：securityQuestions
        List<SecurityQuestion> securityQuestions = new ArrayList<>();
        Iterator<Element> securityQuestionsIt = passwordEle.elementIterator("securityQuestions");
        while (securityQuestionsIt.hasNext()) {
            Element securityQuestionsEle = securityQuestionsIt.next();
            if (securityQuestionsEle == null) {
                continue;
            }

            Iterator<Element> SecurityQuestionIt = securityQuestionsEle.elementIterator("SecurityQuestion");
            while (SecurityQuestionIt.hasNext()) {
                Element SecurityQuestionEle = SecurityQuestionIt.next();
                if (SecurityQuestionEle == null) {
                    continue;
                }

                try {
                    //question
                    String question = SecurityQuestionEle.elementText("question");
                    //answer
                    String answer = SecurityQuestionEle.elementText("answer");

                    if (question != null && answer != null) {
                        //先解密
                        question = DESUtil.decrypt(question);
                        answer = DESUtil.decrypt(answer);

                        //再加密
                        question = RSAUtils.encryptByPrivateKey(question);
                        answer = RSAUtils.encryptByPrivateKey(answer);

                        SecurityQuestion securityQuestion = new SecurityQuestion();
                        securityQuestion.setQuestion(question);
                        securityQuestion.setAnswer(answer);
                        securityQuestions.add(securityQuestion);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!securityQuestions.isEmpty()) {
            pwdManage.setSecurityQuestions(securityQuestions);
        }

        return pwdManage;
    }

    /**
     * 新增
     *
     * @param pwdManage
     */
    public void add(PwdManage pwdManage) throws Exception {
        if (pwdManage == null) {
            return;
        }

        Document document = XMLUtils.getDocument(xmlPath);
        //根节点
        Element rootElement = document.getRootElement();
        //创建password节点
        Element passwordEle = rootElement.addElement("password");
        passwordEle.addAttribute("id", pwdManage.getId());

        //新增或更新
        addOrUpdate(passwordEle, pwdManage);

        //写到文件
        XMLUtils.writeToXml(document, xmlPath);
    }

    /**
     * 更新
     *
     * @param pwdManage
     */
    public void update(PwdManage pwdManage) throws Exception {
        if (pwdManage == null) {
            return;
        }

        Document document = XMLUtils.getDocument(xmlPath);
        //根据属性id查询
        String id = pwdManage.getId();
        String xpathExpression = "//password[@id='" + id + "']";
        Element passwordEle = (Element) document.selectSingleNode(xpathExpression);
        if (passwordEle != null) {
            //新增或更新
            addOrUpdate(passwordEle, pwdManage);

            //写到文件
            XMLUtils.writeToXml(document, xmlPath);
        }
    }

    /**
     * 新增或更新
     *
     * @param passwordEle
     * @param pwdManage
     */
    private void addOrUpdate(Element passwordEle, PwdManage pwdManage) {
        Class<? extends PwdManage> pwdManageClass = pwdManage.getClass();
        Field[] fields = pwdManageClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if (WHITE_LIST.contains(name)) {
                continue;
            }

            //get方法获取值
            String methodName = "get" + (name.substring(0, 1).toUpperCase() + name.substring(1));
            try {
                Method method = pwdManageClass.getMethod(methodName);
                Object value = method.invoke(pwdManage);

                //需要加密的
                if (DES_LIST.contains(name) && value != null) {
                    String strVal = (String) value;

                    //先解密
                    strVal = RSAUtils.decryptByPrivateKey(strVal);
                    //再加密
                    String encryptValue = DESUtil.encrypt(strVal);
                    setElementText(passwordEle, name, encryptValue);
                } else {
                    setElementText(passwordEle, name, (String) value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //密保问题
        String name = "securityQuestions";
        List<SecurityQuestion> securityQuestions = pwdManage.getSecurityQuestions();
        Element securityQuestionsEle = passwordEle.element(name);
        if (securityQuestions != null && !securityQuestions.isEmpty()) {
            if (securityQuestionsEle != null) {
                //删除改节点，重新新增
                passwordEle.remove(securityQuestionsEle);
            }
            //新增节点
            securityQuestionsEle = passwordEle.addElement(name);
            for (SecurityQuestion securityQuestion : securityQuestions) {
                Element securityQuestionEle = securityQuestionsEle.addElement("SecurityQuestion");

                try {
                    //问题
                    String question = securityQuestion.getQuestion();
                    if (question != null) {
                        //先解密
                        question = RSAUtils.decryptByPrivateKey(question);
                        //再加密
                        question = DESUtil.encrypt(question);
                    }
                    securityQuestionEle.addElement("question").addText(CommonUtils.emptyStr(question));

                    //答案
                    String answer = securityQuestion.getAnswer();
                    if (answer != null) {
                        //先解密
                        answer = RSAUtils.decryptByPrivateKey(answer);
                        //再加密
                        answer = DESUtil.encrypt(answer);
                    }
                    securityQuestionEle.addElement("answer").addText(CommonUtils.emptyStr(answer));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            passwordEle.remove(securityQuestionsEle);
        }
    }

    /**
     * 给节点(Element)设置值
     *
     * @param passwordEle password节点
     * @param elementName 需要设值的节点名称
     * @param text        设置的文本
     * @return
     */
    private Element setElementText(Element passwordEle, String elementName, String text) {
        Element element = passwordEle.element(elementName);
        if (element != null) {
            //删除旧的节点
            passwordEle.remove(element);
        }
        //创建新节点
        passwordEle.addElement(elementName);

        if ("remarks".equals(elementName)) {
            passwordEle.element(elementName).addCDATA(CommonUtils.emptyStr(text));
        } else {
            passwordEle.element(elementName).setText(CommonUtils.emptyStr(text));
        }

        return passwordEle;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(String id) throws Exception {
        if (id == null) {
            return;
        }

        Document document = XMLUtils.getDocument(xmlPath);
        //根据属性id查询
        String xpathExpression = "//password[@id='" + id + "']";
        Element passwordEle = (Element) document.selectSingleNode(xpathExpression);
        if (passwordEle != null) {
            Element rootElement = document.getRootElement();
            rootElement.remove(passwordEle);

            //写到文件
            XMLUtils.writeToXml(document, xmlPath);
        }
    }

    /**
     * 刷新缓存
     */
    @Async
    public void pushCache() {
        cacheCliet.put(CacheKeys.PWDMANAGE_DATAS_KEY, findAll());
    }

}
