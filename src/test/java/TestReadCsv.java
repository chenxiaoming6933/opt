/**
 * @title: TestReadCsv
 * @projectName resolve
 * @description: TODO
 * @author ChenXiaoMing
 * @date 2019/9/616:50
 */

import bean.Config;
import bean.DnsServerTypeRootDetaiBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ip.GeoIpBean;
import ip.IpLocationOpr;
import ip.LocalIP;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import util.ElasticsearchUtil;
import util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class TestReadCsv {

    @Test
    public void testReadFile() {
        List <Map <String, Object>> maps = FileUtils.readCsvAndAddList(new File("D:\\WeChat Files\\tameyong\\FileStorage\\File\\2019-09\\文件格式\\dns_server_list.txt"));
        for (Map <String, Object> map : maps) {
            Set <Map.Entry <String, Object>> entries = map.entrySet();
            for (Map.Entry <String, Object> entry : entries) {
                System.out.println(entry.getKey() + "=====" + entry.getValue());
            }
        }
    }


    @Test
    public void testLoadProperties() {
        File file = new File(Config.DOMAIN_TRACE_FILE_PATH + "\\DOMAIN_AXCD9912_2019091011001212.txt");
        String fileName = file.getName();
        int indexOf = fileName.indexOf(".");
        if (indexOf != -1 && indexOf != 0) {
            String substring = fileName.substring(0, indexOf);
            System.out.println(substring);
        }



       /* String str = ".|||1229|IN|NS|a.root-servers.net.,";
        String[] split = str.split("\\|");
        for (String s : split) {
            if (StringUtils.isNotBlank(s)) {
                System.out.println(s);
            }
        }*/
    }

    @Test
    public void testInsetEs() {
        int count = 0;
        TransportClient client = ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT));
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        List <Map <String, Object>> maps = FileUtils.readCsvAndAddList(new File(Config.DNS_SERVER_FILE_PATH + "\\dns_server_list.txt"));

        for (Map <String, Object> map : maps) {
            bulkRequest.add(client.prepareIndex(Config.INDEX_NAME, Config.IP_TYPE_NAME).setSource(map));
            // 每一千条提交一次
            if (count % 1000 == 0) {
                bulkRequest.execute().actionGet();
                //此处新建一个bulkRequest，类似于重置效果
                bulkRequest = client.prepareBulk();
                System.out.println("提交了：" + count);
            }
            count++;
        }
        bulkRequest.execute().actionGet();
        System.out.println("插入完毕");
    }


    @Test
    public void testQueryByIp() {
        // String byIp = ElasticsearchUtil.queryDataByIp(Config.INDEX_NAME, Config.IP_TYPE_NAME, "8.8.8.8");
        //System.out.println(byIp);
       /* TransportClient client = ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT));
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(client.prepareIndex(Config.INDEX_NAME, Config.IP_TYPE_NAME).setSource(new HashMap <>()));
        int numberOfActions = bulkRequest.numberOfActions();

        System.out.println(numberOfActions);
        BulkResponse bulkItemResponses = bulkRequest.get();*/

        TransportClient client = ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT));
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(Config.INDEX_NAME).setTypes(Config.IP_TYPE_DOMAIN)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchAllQuery());
        SearchResponse response = searchRequestBuilder.get();
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
    }


    @Test
    public void testIpDb() {
        IpLocationOpr opr = new IpLocationOpr();
        LocalIP local = new LocalIP(Config.INNER_IP_FILE_PATH);
        try {
            opr.openDB(Config.IP_IP_FILE_PATH, Config.GEOIP_FILE_PATH, Config.MON_FILE_PATH);
            GeoIpBean search = opr.search("114.114.114.110", local);
            System.out.println(search.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonMap() {
        HashMap <Object, Object> map = new HashMap <>();
        map.put("isGhoServer", "111");
        map.put("openPort", "3306");
        map.put("servType", "3306");

        DnsServerTypeRootDetaiBean dnsServerTypeRootDetaiBean = JSONObject.parseObject(JSON.toJSONString(map), DnsServerTypeRootDetaiBean.class);
        System.out.println(dnsServerTypeRootDetaiBean.toString());
    }


}
