package service.impl;

import bean.Config;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import service.IpResolveService;
import util.ElasticsearchUtil;
import util.FileUtils;
import util.IpDbToll;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @ClassName 解析数据 实现类
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
@Service
public class IpResolveServiceImpl implements IpResolveService {

    @Override
    public void insert(File file) {
        //获取文件数据 也许存在同步情况 考虑可以加同步方法
        try {
            synchronized (IpResolveServiceImpl.class) {
                //索引存在 直接录人 不存在创建索引并分片
                if (!ElasticsearchUtil.isExitIndex(Config.INDEX_NAME, ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT)))) {
                    ElasticsearchUtil.createIndex(Config.INDEX_NAME);
                }
                //获取数据
                List <Map <String, Object>> data = FileUtils.readCsvAndAddList(file);

                //录入数据
                if (!data.isEmpty()) {
                    ElasticsearchUtil.insertIp(data);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
