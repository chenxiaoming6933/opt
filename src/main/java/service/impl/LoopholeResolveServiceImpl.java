package service.impl;

import bean.Config;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import service.IpResolveService;
import service.LoopholeResolveService;
import util.DateFormat;
import util.ElasticsearchUtil;
import util.FileUtils;
import util.LogUtil;

import java.io.File;
import java.util.*;

/**
 * @ClassName 漏洞扫描解析数据 实现类
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
@Service
public class LoopholeResolveServiceImpl implements LoopholeResolveService {

    @Override
    public void insert(File file) {
        //获取文件数据 也许存在同步情况 考虑可以加同步方法
        try {
            synchronized (LoopholeResolveServiceImpl.class) {
                //索引存在 直接录人 不存在创建索引并分片
                if (!ElasticsearchUtil.isExitIndex(Config.INDEX_NAME, ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT)))) {
                    ElasticsearchUtil.createIndex(Config.INDEX_NAME);
                }

                //进行解压
                FileUtils.decompressTarGz(file, Config.LOOPHOLE_TAR_TMP_FILE_PATH);
                File tarTmpFile = new File(Config.LOOPHOLE_TAR_TMP_FILE_PATH);
                if (!tarTmpFile.exists() && !tarTmpFile.isDirectory() && tarTmpFile.listFiles().length == 0) {
                    LogUtil.info(tarTmpFile.getPath() + "文件路径不存在！或压缩文件中无xml文件");
                    return;
                }

                //解析数据并录入
                File[] files = tarTmpFile.listFiles();
                for (File xmlFile : files) {
                    if (!xmlFile.getName().contains(Config.FILT_TYPE_SUFFIX_XML)) {
                        continue;
                    }
                    List <Map <String, Object>> saveData = getSaveData(xmlFile, file);
                    LogUtil.info("xml解析数据条数为:"+saveData.size());
                    if (!saveData.isEmpty()) {
                        ElasticsearchUtil.insertLoophole(saveData);
                    }
                    xmlFile.delete();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 解析xml文件 获取数据
     *
     * @param
     * @return
     */
    private List <Map <String, Object>> getSaveData(File xmlFile, File tarGzFile) {
        List <Map <String, Object>> resultList = new ArrayList <>();
        SAXReader reader = new SAXReader();
        Map <String, Object> fileInfoMap = FileUtils.getDerviceIdByFile(tarGzFile);
        Long time = (fileInfoMap.containsKey(Config.TIMESTAMP)) ? Long.valueOf(fileInfoMap.get(Config.TIMESTAMP).toString()) : DateFormat.YYYY_MM_DD_HH_MM_SS.format2Long(DateFormat.YYYY_MM_DD_HH_MM_SS.format(new Date()));
        String deviceId = (fileInfoMap.containsKey(Config.IP_DEVICE_NO)) ? fileInfoMap.get(Config.IP_DEVICE_NO).toString() : "";
        String fileName = xmlFile.getName().split("\\." + Config.FILT_TYPE_SUFFIX_XML)[0];
        try {
            Document document = reader.read(xmlFile);
            Element root = document.getRootElement();
            //提取基础内容
            List <Element> port = root.selectNodes("//port");
            for (Element element : port) {
                HashMap <String, Object> valueMap = new HashMap <>();
                valueMap.put(Config.IP_TYPE_NAME, fileName);
                valueMap.put(Config.TIMESTAMP, time);
                valueMap.put(Config.IP_DEVICE_NO, deviceId);

                String protocol = element.attribute(Config.LOOPHOLE_PROTOCOL).getValue();
                valueMap.put(Config.LOOPHOLE_PROTOCOL, (null == protocol ? "" : protocol));
                String ports = element.attribute(Config.LOOPHOLE_PORT).getValue();
                valueMap.put(Config.LOOPHOLE_PORT, (null == ports ? "" : ports));

                List <Element> state = element.selectNodes("state");
                String ttl = null;
                for (Element states : state) {
                    String portStatus = states.attribute(Config.LOOPHOLE_PORT_STATUS).getValue();
                    valueMap.put(Config.LOOPHOLE_PORT_STATUS, (null == portStatus ? "" : portStatus));
                    ttl = states.attribute(Config.LOOPHOLE_TTL).getValue();
                }
                valueMap.put(Config.LOOPHOLE_TTL, (null == ttl ? "" : ttl));

                List <Element> service = element.selectNodes("service");
                String serverName = null;
                for (Element server : service) {
                    serverName = server.attribute(Config.LOOPHOLE_SERVER).getValue();
                }
                valueMap.put(Config.LOOPHOLE_SERVER, (null == serverName ? "" : serverName));

                List <Element> script = element.selectNodes("script");
                List <String> infoList = new ArrayList <>();
                for (Element element1 : script) {
                    String value = element1.attribute(Config.LOOPHOLE_VULNERABILITY_INFORMATION).getValue();
                    if (value.contains("\n")) {
                        infoList = Arrays.asList(value.split("\n"));
                    }
                }
                valueMap.put(Config.LOOPHOLE_VULNERABILITY_INFORMATION, infoList);
                resultList.add(valueMap);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return resultList;
        }
        return resultList;
    }
}
