package service.impl;

import bean.Config;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import service.DomainResolveService;
import util.DateFormat;
import util.ElasticsearchUtil;
import util.FileUtils;
import util.LogUtil;

import java.io.File;
import java.util.*;

/**
 * @ClassName 解析数据 实现类
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
@Service
public class DomainResolveServiceImpl implements DomainResolveService {

    /**
     * 获取文件数据 也许存在同步情况 考虑可以加同步方法
     *
     * @param file
     */
    @Override
    public void insert(File file) {
        try {
            synchronized (DomainResolveServiceImpl.class) {
                //索 引存在 直接录人 不存在创建索引并分片
                if (!ElasticsearchUtil.isExitIndex(Config.INDEX_NAME, ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT)))) {
                    ElasticsearchUtil.createIndex(Config.INDEX_NAME);
                }

                //获取文件数据
                Map <String, List <String>> data = FileUtils.readFileAndMap(file);

                //解析数据 提取关键字段 并分类 子域名信息
                TreeMap <String, TreeMap <String, List <String>>> modelData = getModelData(data);

                //整合数据 提取成插入形式数据
                List <TreeMap <String, Object>> saveObject = getSaveObject(modelData, file);

                //插入数据  域名存在更新 否则插入
                ElasticsearchUtil.insertDomain(saveObject);

                //处理完成
                LogUtil.info("任务结束！");
                LogUtil.info("第二次测试提交远程");

            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * 提供关键字段 存放至map
     *
     * @param data
     * @return
     */
    public TreeMap <String, TreeMap <String, List <String>>> getModelData(Map <String, List <String>> data) {
        TreeMap <String, TreeMap <String, List <String>>> resultMap = new TreeMap <>();
        try {
            if (!data.isEmpty()) {
                Set <Map.Entry <String, List <String>>> entries = data.entrySet();
                for (Map.Entry <String, List <String>> entry : entries) {
                    TreeMap <String, List <String>> subClassMap = new TreeMap <>();
                    List <String> subClassList = new LinkedList <>();
                    int subClassCount = 0;
                    String domainName = entry.getKey();

                    //循环 域名对应 文本内容 进行提取关键字段
                    List <String> values = entry.getValue();
                    for (int i = 0; i < values.size(); i++) {
                        if (StringUtils.isNotBlank(values.get(i))) {
                            if (values.get(i).contains("<<>>") || values.get(i).contains("+cmd")) {
                                continue;
                            }
                            if (!values.get(i).contains(Config.RECEIVED) && !values.get(i).contains(Config.START)) {
                                subClassList.add(values.get(i));
                            }
                            if (values.get(i).contains(Config.RECEIVED)) {
                                subClassMap.put(Config.DOMAIN_SUBCLASS_PREFIX + subClassCount, subClassList);
                                subClassList = new ArrayList <>();
                                subClassCount++;
                            }
                        }
                    }
                    if (!subClassMap.isEmpty()) {
                        resultMap.put(domainName, subClassMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 整合数据 提取成插入形式数据
     *
     * @param data
     * @return
     */
    public List <TreeMap <String, Object>> getSaveObject(TreeMap <String, TreeMap <String, List <String>>> data, File file) {
        int domainInfoCountFlag = 0;
        List <TreeMap <String, Object>> resultList = new ArrayList <>();
        //获取设备id  与传输时间
        Map <String, Object> fileInfoMap = FileUtils.getDerviceIdByFile(file);
        Long time = (fileInfoMap.containsKey(Config.TIMESTAMP)) ? Long.valueOf(fileInfoMap.get(Config.TIMESTAMP).toString()) : DateFormat.YYYY_MM_DD_HH_MM_SS.format2Long(DateFormat.YYYY_MM_DD_HH_MM_SS.format(new Date()));
        String deviceId = (fileInfoMap.containsKey(Config.IP_DEVICE_NO)) ? fileInfoMap.get(Config.IP_DEVICE_NO).toString() : "";
        Set <Map.Entry <String, TreeMap <String, List <String>>>> entries = data.entrySet();
        try {
            for (Map.Entry <String, TreeMap <String, List <String>>> entry : entries) {
                TreeMap <String, Object> domainObjMap = new TreeMap <>();
                Map <String, Object> subClassMap = new TreeMap <>();
                Map <String, List <String>> value = entry.getValue();
                Set <Map.Entry <String, List <String>>> valueSet = value.entrySet();
                for (Map.Entry <String, List <String>> stringListEntry : valueSet) {
                    List <String> subClassInfo = stringListEntry.getValue();
                    ArrayList <Object> subClassInfoList = new ArrayList <>();
                    for (String str : subClassInfo) {
                        Map <Object, Object> subClassInfoMap = new TreeMap <>();
                        if (str.contains("|")) {
                            Object[] strArray = Arrays.stream(str.split("\\|")).filter(string -> StringUtils.isNotBlank(string)).toArray();
                            if (domainInfoCountFlag == 0) {
                                domainInfoCountFlag = strArray.length;
                            }
                            if (strArray.length == domainInfoCountFlag) {
                                subClassInfoMap.put(Config.DOMAIN_NODE_NAME, strArray[0].toString());
                                subClassInfoMap.put(Config.DOMAIN_TTL, strArray[1].toString());
                                subClassInfoMap.put(Config.DOMAIN_IP_PROTOCOL, strArray[2].toString());
                                subClassInfoMap.put(Config.DOMAIN_SERVER_NAME, strArray[3].toString());
                                subClassInfoMap.put(Config.DOMAIN_SUB_CLASS_NAME, strArray[4].toString());
                            }
                        }
                        if(!subClassInfoMap.isEmpty()){
                            subClassInfoList.add(subClassInfoMap);
                        }
                    }
                    subClassMap.put(stringListEntry.getKey(), subClassInfoList);
                }
                domainObjMap.put(Config.IP_DEVICE_NO, deviceId);
                domainObjMap.put(Config.IP_TYPE_DOMAIN, entry.getKey());
                domainObjMap.put(Config.TIMESTAMP, time);
                domainObjMap.put(Config.DOMAIN_SUB_CLASS_NAME, subClassMap);
                resultList.add(domainObjMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("解析域名数据出错：" + e.getMessage());
            return resultList;
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
        return resultList;
    }

}
