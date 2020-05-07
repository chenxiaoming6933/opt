package util;

import bean.Config;
import ip.GeoIpBean;
import ip.IpLocationOpr;
import ip.LocalIP;

/**
 * @ClassName ip查询工具类
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class IpDbToll {

    public static IpLocationOpr opr = null;

    /**
     * 根据ip地址 获取国家名称
     *
     * @param ip
     * @return
     */
    public static GeoIpBean queryCountryName(String ip) {
        GeoIpBean bean = null;
        try {
            opr = new IpLocationOpr();
            LocalIP local = new LocalIP(Config.INNER_IP_FILE_PATH);
            opr.openDB(Config.IP_IP_FILE_PATH, Config.GEOIP_FILE_PATH, Config.MON_FILE_PATH);
            bean = opr.search(ip, local);
        } catch (Exception e) {
            e.printStackTrace();
            return bean;
        }
        return bean;
    }
}
