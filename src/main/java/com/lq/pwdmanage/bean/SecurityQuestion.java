package com.lq.pwdmanage.bean;

import java.io.Serializable;

/**
 * 密码问题
 * @author LQ
 * @date 2020/6/10 10:30
 */
public class SecurityQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String answer;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "SecurityQuestion{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

}
