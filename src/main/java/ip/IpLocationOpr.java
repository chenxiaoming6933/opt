package ip;

import java.io.IOException;
import java.util.LinkedHashMap;

import com.wy.tools.ip.IpAddress;
import com.wy.tools.ip.db.IpDB;
import com.wy.tools.ip.db.IpDBFactory;
import org.apache.commons.lang3.StringUtils;

public class IpLocationOpr {
    private IpDB ip_db = null;

    public IpLocationOpr() {
    }

    public void openDB(String db_ipip, String db_geoip, String db_17monip) throws IOException {
        //生成ip库配置map
        //***********************
        //* put的顺序决定查询的顺序  *
        //***********************		
        LinkedHashMap <String, String> conf = new LinkedHashMap <>();

        conf.put("ipip", db_ipip);
        conf.put("geoip", db_geoip);
        conf.put("17monip", db_17monip);

        ip_db = IpDBFactory.connect(conf);
    }

    public void closeDB() throws Exception {
        if (ip_db != null) {
            ip_db.close();
            ip_db = null;
        }
    }

    /*	
    特殊值约定：	
    	无法识别: 国家和省字段为空
    	127.0.0.1: 国家为1, 省字段为空
    	内网: 国家为2, 省字段为空
    */
    public GeoIpBean search(String ip, LocalIP local_ip) {
        GeoIpBean ipaddr = new GeoIpBean();

        if (ip.equals("127.0.0.1")) {
            ipaddr.setCountry("本地");
            ipaddr.setCountryCode("1");
            return ipaddr;
        }

        if (local_ip.isLocalIP(ip)) {
            ipaddr.setCountry("局域网");
            ipaddr.setCountryCode("2");
            return ipaddr;
        }

        if (StringUtils.isBlank(ip)) {
            ipaddr.setCountry("未知");
            ipaddr.setCountryCode("unknown");
        }

        if (!checkIP(ip)) {
            return ipaddr;
        }

        try {
            IpAddress ip_addr = ip_db.search(ip);
            ipaddr.setCountry(ip_addr.getIpCountryCnName());
            ipaddr.setProvince(ip_addr.getIpProvinceCnName());
            ipaddr.setPostcode(ip_addr.getIpPostalCode());//邮编
            ipaddr.setLatitude(ip_addr.getIpLatitude());//纬度
            ipaddr.setLongitude(ip_addr.getIpLongitude());//经度
            ipaddr.setIpIsp(ip_addr.getIpIsp());//运营商
            ipaddr.setTimezonecity(ip_addr.getIpTimeZoneCity());//城市时区
            ipaddr.setCityname(ip_addr.getIpCityCnName());//城市
            ipaddr.setTimezone(ip_addr.getIpTimeZone());
            ipaddr.setCountryCode(ip_addr.getIpCountryIsoCode());
            ipaddr.setProvinceCode(ip_addr.getIpProvinceIsoCode());

        } catch (Exception e) {
            e.printStackTrace();
            return ipaddr;
        }

        return ipaddr;
    }

    /**
     * 判断ip是否合法
     *
     * @param ip
     * @return
     */
    public boolean checkIP(String ip) {
        return ip.matches("[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+");
    }

    /**
     * @param args
     *//*
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		IpLocationOpr opr = new IpLocationOpr();
		try
		{
			LocalIP local = new LocalIP("E:\\trojanwall_v4\\etc\\inner_ip.conf");
			
			opr.openDB("E:\\trojanwall_v4\\etc\\ipip.wydb", "E:\\trojanwall_v4\\etc\\geoip.wydb", "E:\\trojanwall_v4\\etc\\17mon.wydb");
			
			GeoIpBean bean=opr.search("202.111.20.66", local);		
			
			System.out.println("country="+bean.country+", province="+bean.province);
			
			opr.closeDB();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
