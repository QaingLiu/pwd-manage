package com.lq.pwdmanage.util;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
* 本地主机工具类
* @author LQ
* @date 2020/6/17 10:09
*/
public class LocalHostUtil {

    /**
     * 获取主机名称
     * @return
     * @throws UnknownHostException
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取系统首选IP
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有网卡IP，排除回文地址、虚拟地址
     * @return
     */
    public static String[] getLocalIPs() {
        List<String> list = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface intf = enumeration.nextElement();
                if (intf.isLoopback() || intf.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> inets = intf.getInetAddresses();
                while (inets.hasMoreElements()) {
                    InetAddress addr = inets.nextElement();
                    if (addr.isLoopbackAddress() || !addr.isSiteLocalAddress() || addr.isAnyLocalAddress()) {
                        continue;
                    }
                    list.add(addr.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }

    /**
     * 获取本机所有网卡IP，包括回文地址、虚拟地址
     * @return
     */
    public static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    // IPV4
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    /**
     * 判断操作系统是否是Windows
     * 
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static void main(String[] args) {
        try {
            System.out.println("主机是否为Windows系统：" + LocalHostUtil.isWindowsOS());
            System.out.println("主机名称：" + LocalHostUtil.getHostName());
            System.out.println("系统首选IP：" + LocalHostUtil.getLocalIP());
            System.out.println("系统所有IP：" + String.join(",", LocalHostUtil.getLocalIPs()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}