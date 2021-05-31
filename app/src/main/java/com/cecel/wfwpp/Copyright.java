package com.cecel.wfwpp;

public class Copyright {

    private static final Copyright instance = new Copyright();

    private final static String[] GROUP_MEMBERS = {"陈超林","孙熙哲"};

    private final static String APPNAME = "微服务Plus";

    private final static String CONTACT = "1021203086,786864534";

    public static Copyright getInstance() {
        return instance;
    }

    public String getAPPNAME() {
        return APPNAME;
    }

    public String[] getGroupMembers() {
        return GROUP_MEMBERS;
    }

    public String getCONTACT() {
        return CONTACT;
    }
}
