package com.lq.pwdmanage;

import com.lq.pwdmanage.util.CommonUtils;
import com.lq.pwdmanage.util.LocalHostUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@EnableAsync
@SpringBootApplication
public class PwdManageApplication implements ApplicationListener<WebServerInitializedEvent> {

    protected static final Logger log = LoggerFactory.getLogger(PwdManageApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PwdManageApplication.class, args);
        log.info("服务启动成功！");
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        WebServer server = event.getWebServer();
        WebServerApplicationContext context = event.getApplicationContext();
        Environment env = context.getEnvironment();
        int port = server.getPort();
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n---------------------------------------------------------\n");
        sb.append("\tApplication is running! Access address:\n");
        sb.append("\tLocal:\t\thttp://localhost:" + port + contextPath);
        //获取本机所有IP地址
        String[] localIPs = LocalHostUtil.getLocalIPs();
        for(String ip : localIPs){
            sb.append("\n\tExternal:\thttp://" + ip + ":" + port + contextPath);
        }
        sb.append("\n---------------------------------------------------------\n");
        log.info(sb.toString());
    }

}
