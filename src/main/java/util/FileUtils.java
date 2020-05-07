package util;

import bean.*;
import com.alibaba.fastjson.JSONObject;
import ip.GeoIpBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class FileUtils {


    /**
     * 读取csv文件 忽略头部并存至集合
     *
     * @return
     */
    public static List <Map <String, Object>> readCsvAndAddList(File file) {
        List <Map <String, Object>> resultList = new ArrayList <>();
        String jsonStr = "";
        Map <String, Object> fileInfoMap = FileUtils.getDerviceIdByFile(file);
        Long time = (fileInfoMap.containsKey(Config.TIMESTAMP)) ? Long.valueOf(fileInfoMap.get(Config.TIMESTAMP).toString()) : DateFormat.YYYY_MM_DD_HH_MM_SS.format2Long(DateFormat.YYYY_MM_DD_HH_MM_SS.format(new Date()));
        String deviceId = (fileInfoMap.containsKey(Config.IP_DEVICE_NO)) ? fileInfoMap.get(Config.IP_DEVICE_NO).toString() : "";
        FileReader fileReader = null;
        BufferedReader bReader = null;
        try {
            fileReader = new FileReader(file.getAbsolutePath());
            bReader = new BufferedReader(fileReader);
            //读取内容
            while ((jsonStr = bReader.readLine()) != null) {
                if (StringUtils.isBlank(jsonStr)) {
                    continue;
                }
                JSONObject obj = JSONObject.parseObject(jsonStr);
                HashMap <String, Object> value = new HashMap <>();
                GeoIpBean bean = IpDbToll.queryCountryName(obj.getString(Config.IP_ADDR));
                value.put(Config.IP_ADDR, obj.getString(Config.IP_ADDR));
                value.put(Config.IP_PORT, obj.getInteger(Config.IP_PORT));
                value.put(Config.IP_TTL, obj.getInteger(Config.IP_TTL));
                value.put(Config.SERVER_TYPE, obj.getString(Config.SERVER_TYPE));
                value.put(Config.IP_COUNTRY_NAME, bean.getCountry());
                value.put(Config.IP_COUNTRY_CODE, bean.getCountryCode());
                value.put(Config.IP_PROVINCE_CODE, (StringUtils.isNotBlank(bean.getProvince())) ? bean.getProvince() : "");
                value.put(Config.IP_PROVINCE_NAME, (StringUtils.isNotBlank(bean.getProvinceCode())) ? bean.getProvinceCode() : "");
                String longitudeAndLatitude = (StringUtils.isNotBlank(bean.getLongitude()) && StringUtils.isNotBlank(bean.getLatitude())) ? bean.getLongitude() + "/" + bean.getLatitude() : "未知";
                value.put(Config.LONGITUDE_AND_LATITUDE, longitudeAndLatitude);
                String operator = (StringUtils.isNotBlank(bean.getIpIsp())) ? bean.getIpIsp() : "未知";
                value.put(Config.OPERATOR, operator);
                value.put(Config.SECURITY_STATUS, "");
                value.put(Config.LEAKAGE_DETAILS, "");

                //服务器相关信息
                Map <String, Object> serverTypeInfo = getServerTypeInfo(obj.getString(Config.SERVER_TYPE), obj.getString(Config.IP_ADDR), obj);
                value.put(Config.SERVER_TYPE, serverTypeInfo.get(Config.SERVER_TYPE));
                value.put(Config.SERVER_TYPE_INFO, serverTypeInfo.get(Config.SERVER_TYPE_INFO));

                value.put(Config.IP_DEVICE_NO, deviceId);
                value.put(Config.TIMESTAMP, time);
                resultList.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error("读取csv文件错误：" + e.getMessage());
            return resultList;
        } finally {
            if (null != IpDbToll.opr) {
                try {
                    IpDbToll.opr.closeDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (null != bReader) {
                    bReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != fileReader) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file.exists()) {
            file.delete();
        }
        return resultList;
    }


    /**
     * 根据配置文件中 key值  获取配置参数
     *
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        String value = null;
        FileInputStream fileInputStream = null;
        try {
            Properties props = new Properties();
            fileInputStream = new FileInputStream(Config.ETC_CONFIG_PATH);
            props.load(fileInputStream);
            if (props.containsKey(key)) {
                value = props.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return value;
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 加载dns服务器IP 地址 并存入list
     */
    public static void getDnsRootServerIp() {
        BufferedReader bufferedReader = null;
        File file = new File(Config.DNS_SERVER_IP_FILE_PATH);
        FileInputStream fileInputStream = null;
        InputStreamReader read = null;
        try {
            fileInputStream = new FileInputStream(file);
            read = new InputStreamReader(fileInputStream, "UTF-8");
            if (file.isFile() && file.exists()) {
                bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    Config.dnsServerIpList.add(lineTxt);
                }
            }
            LogUtil.info("初始化加载DNS服务器IP成功！");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("初始化加载DNS服务器IP失败！" + e.getMessage());
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != read) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * dig域名解析生成文件后  读取文件内容 并存放至map  key:domain  value:相关字符串
     *
     * @param file
     * @return
     */
    public static Map <String, List <String>> readFileAndMap(File file) {
        Map <String, List <String>> resultMap = new HashMap <>();
        String lineStr = "";
        String domainName = "";
        List <String> strList = new ArrayList <>();
        FileReader fileReader = null;
        BufferedReader bReader = null;
        try {
            fileReader = new FileReader(file.getAbsolutePath());
            bReader = new BufferedReader(fileReader);
            while ((lineStr = bReader.readLine()) != null) {
                if (StringUtils.isBlank(lineStr)) {
                    continue;
                }

                //解析文件域名头 并存放map
                if (lineStr.contains(Config.START)) {
                    if (!resultMap.containsKey(domainName)) {
                        domainName = bReader.readLine();
                        resultMap.put(domainName, strList);
                    } else {
                        if (resultMap.containsKey(domainName) && !strList.isEmpty()) {
                            resultMap.put(domainName, strList);
                            strList = new ArrayList <>();
                        }
                        domainName = bReader.readLine();
                        resultMap.put(domainName, strList);
                    }
                }
                strList.add(lineStr.replaceAll(" ", "|").replaceAll("\t", "|"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap;
        } finally {
            if (null != bReader) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fileReader) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultMap;
    }

    /**
     * 根据文件 获取设备id
     *
     * @param file
     * @return
     */
    public static Map <String, Object> getDerviceIdByFile(File file) {
        Map <String, Object> resultMap = new HashMap <>();
        try {
            if (!file.exists()) {
                return resultMap;
            }
            String fileName = file.getName();
            int flag = fileName.indexOf('.');
            if (flag != -1 && flag < fileName.length()) {
                String fileNameNotPostfix = fileName.substring(0, flag);
                if (fileNameNotPostfix.contains("_")) {
                    String[] strs = fileNameNotPostfix.split("_");
                    resultMap.put(Config.IP_DEVICE_NO, strs[1]);
                    resultMap.put(Config.TIMESTAMP, strs[strs.length - 1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 根据不同服务类别 返回对应类别对象与类型名称
     *
     * @return
     */
    public static Map <String, Object> getServerTypeInfo(String serverType, String addr, JSONObject jsonObject) {
        Map <String, Object> resultMap = new HashMap <>();
        //递归
        if (StringUtils.isNotBlank(serverType) && Config.SERVER_TYPE_RECURSIVE.equals(serverType)) {
            resultMap.put(Config.SERVER_TYPE, Config.SERVER_TYPE_RECURSIVE);

            DnsServerTypeRecursiveDetaiBean bean = new DnsServerTypeRecursiveDetaiBean();
            bean.setIsSupportIpSix(jsonObject.getString(Config.IS_IP_SIX));
            bean.setIsSupportTcp(jsonObject.getString(Config.TCP));
            bean.setSurvivalStatus("true");
            bean.setAverageAccessDelay(jsonObject.getLong(Config.SERVER_TIMEDELAY));
            String objStr = JSONObject.toJSONString(bean);
            resultMap.put(Config.SERVER_TYPE_INFO, JSONObject.parseObject(objStr, Map.class));
        }
        //权威类型
        if (StringUtils.isNotBlank(serverType) && Config.SERVER_TYPE_AUTHORITY.equals(serverType)) {
            resultMap.put(Config.SERVER_TYPE, Config.SERVER_TYPE_AUTHORITY);

            DnsServerTypeAuthorityDetaiBean bean = new DnsServerTypeAuthorityDetaiBean();
            bean.setIsSupportIpSix(jsonObject.getString(Config.IS_IP_SIX));
            bean.setIsSupportTcp(jsonObject.getString(Config.TCP));
            bean.setSurvivalStatus("true");
            bean.setAverageAccessDelay(jsonObject.getLong(Config.SERVER_TIMEDELAY));
            bean.setIsAuthorityServer(jsonObject.getString(Config.RECURSION));
            String objStr = JSONObject.toJSONString(bean);
            resultMap.put(Config.SERVER_TYPE_INFO, JSONObject.parseObject(objStr, Map.class));
        }
        //其他类型
        if (StringUtils.isNotBlank(serverType) && !Config.SERVER_TYPE_AUTHORITY.equals(serverType) && !Config.SERVER_TYPE_RECURSIVE.equals(serverType)) {
            if (Config.dnsServerIpList.contains(addr)) {
                resultMap.put(Config.SERVER_TYPE, Config.SERVER_TYPE_ROOT);

                DnsServerTypeRootDetaiBean bean = new DnsServerTypeRootDetaiBean();
                bean.setIsSupportIpSix(jsonObject.getString(Config.IS_IP_SIX));
                bean.setIsSupportTcp(jsonObject.getString(Config.TCP));
                bean.setSurvivalStatus("true");
                bean.setAverageAccessDelay(jsonObject.getLong(Config.SERVER_TIMEDELAY));
                String objStr = JSONObject.toJSONString(bean);
                resultMap.put(Config.SERVER_TYPE_INFO, JSONObject.parseObject(objStr, Map.class));
            } else {
                resultMap.put(Config.SERVER_TYPE, Config.SERVER_TYPE_UNKNOWN);

                DnsServerTypeBasisBean bean = new DnsServerTypeBasisBean();
                bean.setIsSupportIpSix(jsonObject.getString(Config.IS_IP_SIX));
                bean.setIsSupportTcp(jsonObject.getString(Config.TCP));
                bean.setSurvivalStatus("true");
                String objStr = JSONObject.toJSONString(bean);
                resultMap.put(Config.SERVER_TYPE_INFO, JSONObject.parseObject(objStr, Map.class));
            }
        }
        return resultMap;
    }

    /**
     * 解压缩tar.gz文件
     *
     * @param file       压缩包文件
     * @param targetPath 目标文件夹
     */
    public static void decompressTarGz(File file, String targetPath) {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        GZIPInputStream gzipIn = null;
        TarInputStream tarIn = null;
        OutputStream out = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            gzipIn = new GZIPInputStream(bufferedInputStream);
            tarIn = new TarInputStream(gzipIn, 1024 * 2);

            // 创建输出目录
            createDirectory(targetPath, null);

            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                } else {
                    String fileName = entry.getName();
                    if (fileName.contains("\r")) {
                        fileName = fileName.replaceAll("\r", "");
                    }
                    File tempFIle = new File(targetPath + File.separator +fileName);
                    createDirectory(tempFIle.getParent() + File.separator, null);
                    out = new FileOutputStream(tempFIle);
                    int len = 0;
                    byte[] b = new byte[2048];

                    while ((len = tarIn.read(b)) != -1) {
                        out.write(b, 0, len);
                    }
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.error(file.getName() + "文件解压失败！" + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (tarIn != null) {
                    tarIn.close();
                }
                if (gzipIn != null) {
                    gzipIn.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建目录
     *
     * @param outputDir 输出目录
     * @param subDir    子目录
     */
    private static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {
            file = new File(outputDir + File.separator + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }
}
