package com.workorder.service.api.main;


import com.hummer.core.starter.HummerApplicationStart;
import com.hummer.rest.webserver.UndertowServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
* application enter
* @author liguo
**/
@SpringBootApplication(scanBasePackages = "com.workorder.service",exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@UndertowServer
@EnableTransactionManagement
public class ApplicationStart {

    public static void main(String[] args) {
        HummerApplicationStart.start(ApplicationStart.class,args);
    }

}
