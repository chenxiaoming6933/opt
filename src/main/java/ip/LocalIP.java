package ip;

import java.util.*;

public class LocalIP {
	
	private ArrayList<String> ip_list = new ArrayList<String>();
	
	public LocalIP(String ip_conf_file)
	{
		loadIPConfFile(ip_conf_file);
	}
	
	private boolean loadIPConfFile(String ip_conf_file)
	{
		String file_content = FileOpr.read_file(ip_conf_file);	
		
		file_content = file_content.replace('\r', ' ');
		file_content = file_content.replace('\t', ' ');
		
		String[] line = file_content.split("\n");
		
		String tmp = "";
		for(int i=0; i<line.length; i++)
		{
			tmp = line[i].trim();			
			if(tmp.length() > 0)
			{				
				tmp = tmp.replaceAll("\\.", "\\\\.");
				tmp = tmp.replaceAll("\\*", "(.*)"); // [0-9]{1,3}+
				ip_list.add(tmp);
			}
		}
		
		return true;
	}

	private boolean isInnerIP(String ipAddress) {
		
		if(! checkIP(ipAddress))
		{
			return false;
		}
		
		boolean isInnerIp = false;
		long ipNum = getIpNum(ipAddress);
		
		/**
		 * 私有IP：
		 * A类 10.0.0.0-10.255.255.255 
		 * B类 172.16.0.0-172.31.255.255 
		 * C类 192.168.0.0-192.168.255.255 
		 * 当然，还有127这个网段是环回地址
		 */
		long aBegin = getIpNum("10.0.0.0");
		long aEnd = getIpNum("10.255.255.255");
		long bBegin = getIpNum("172.16.0.0");
		long bEnd = getIpNum("172.31.255.255");
		long cBegin = getIpNum("192.168.0.0");
		long cEnd = getIpNum("192.168.255.255");
		
		isInnerIp = isInner(ipNum, aBegin, aEnd)
				|| isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
				|| ipAddress.equals("127.0.0.1");
		
		return isInnerIp;
	}	

	private long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}
	
	public boolean checkIP(String ip) {		
		return ip.matches("[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+");		
	}

	public boolean isLocalIP(String ip) {		
		
		if(isInnerIP(ip))
		{
			return true;
		}
		
		for(int i=0; i<ip_list.size(); i++)		
		{
			if(ip.matches(ip_list.get(i)))
			{
				return true;
			}			
		}
		
		return false;
	}

	/*public static void main(String[] args) {
		LocalIP li = new LocalIP("D:/work/zhongxun_work/data_analyzer/data_analyzer/conf/inner_ip.conf");
		
		
		String ip = "";	
		String match = "202\\.108\\.(.*)\\.(.*)";
		
		ip = "127.0.0.1";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));	
			
		
		
		ip = "10.0.0.0";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));		
		 ip = "9.0.0.0";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "10.255.255.255";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "10.255.255.256";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "172.16.0.0";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "172.15.0.0";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "172.31.255.255";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "172.31.255.256";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "192.168.0.0";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "192.167.0.0";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "192.168.255.255";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
		 ip = "192.168.255.256";		
		System.out.println(ip + ": " + (li.isLocalIP(ip) ? "true":"false"));
			
		
	}*/
}
