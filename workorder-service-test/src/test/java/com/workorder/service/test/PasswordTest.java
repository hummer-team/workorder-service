package com.workorder.service.test;

import com.hummer.common.security.Aes;
import com.hummer.common.security.Md5;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PasswordTest {
    @Test
    public void password() {
        List<String> usersPass = new ArrayList<>();
        usersPass.add("H.S");
        usersPass.add("zhuoxing.yan");
        usersPass.add("xiaowu.zhang");
        usersPass.add("shenggen.li");
        usersPass.add("jinhao");
        usersPass.add("jiqing.ren");
        usersPass.add("tiantian");
        usersPass.add("guo.li.0");
        usersPass.add("xinda.guo");
        usersPass.add("heqiang");
        usersPass.add("C.S.H");

        for (String u : usersPass) {
            String pwd = Md5.encryptMd5(Aes.encryptToStringByDefaultKeyIv(u + "@123"));
            System.out.println("u=" + u + "  p=" + pwd);
        }
    }

    @Test
    public void exist() {
        List<String> lst = new ArrayList<>();
        lst.add("1");
        lst.add("2");

        Assert.assertEquals(true, lst.contains(String.valueOf(1)));
        Assert.assertEquals(false, lst.contains("01"));

    }
}
