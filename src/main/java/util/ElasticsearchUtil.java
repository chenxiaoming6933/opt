package util;

import bean.Config;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.Tree;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

@Service
public class ElasticsearchUtil {


    private static TransportClient client;

    public static TransportClient getClient(String hostIp, int hostPort) {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (client == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (ElasticsearchUtil.class) {
                //未初始化，则初始instance变量
                if (client == null) {
                    Settings settings = Settings.builder().put("client.transport.sniff", false).build();
                    try {
                        client = new PreBuiltTransportClient(settings)
                                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostIp), hostPort));
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        LogUtil.info("es连接获取失败：" + e.getMessage());
                    }
                }
            }
        }
        return client;
    }

    /**
     * 关闭连接
     *
     * @Title: closeEsConnection
     * @Description:closeEsConnection
     * @author :ChenXiaoMing
     * @date :2019年1月8日
     */
    public static void closeEsConnection() {
        if (client != null) {
            client.close();
        }
    }


    /**
     * 判断es 索引是否存在
     *
     * @Title: isExitIndex
     * @Description:isExitIndex
     * @author :ChenXiaoMing
     * @date :2019年1月7日
     */
    public static boolean isExitIndex(String index_name, TransportClient client) {
        IndicesExistsResponse response = client.admin().indices().exists(new IndicesExistsRequest(index_name)).actionGet();
        return response.isExists();
    }

    /**
     * 判断 type是否存在
     *
     * @Title: isExitType
     * @Description:isExitType
     * @author :ChenXiaoMing
     * @date :2019年1月7日
     */
    public static boolean isExitType(String index_name, String doc_type, TransportClient client) {
        TypesExistsResponse response = client.admin().indices()
                .typesExists(new TypesExistsRequest(new String[]{index_name}, doc_type)).actionGet();
        return response.isExists();
    }

    /**
     * 创建索引 并指定分片数量与副本数
     *
     * @param indexName
     * @return
     */
    public static boolean createIndex(String indexName) {
        Map <String, Integer> map = new HashMap <String, Integer>();
        map.put(Config.NUMBER_OF_SHARDS, 5);
        map.put(Config.NUMBER_OF_REPLICAS, 0);
        CreateIndexResponse response = client.admin().indices().prepareCreate(indexName).setSettings(map).get();
        return response.isAcknowledged();
    }


    /**
     * ip type 中根据ip 查询是否存在
     *
     * @return
     */
    public static String queryDataByIp(String indexName, String typeName, TransportClient client, String hostIp, Integer port) {
        String ids = null;
        SearchResponse res = null;
        try {
            BoolQueryBuilder qb = new BoolQueryBuilder();
            if (StringUtils.isNotBlank(hostIp)) {
                qb.must(QueryBuilders.matchPhraseQuery(Config.IP_ADDR, hostIp));
            }
            if (null != port) {
                qb.must(QueryBuilders.matchPhraseQuery(Config.IP_PORT, port));
            }
            res = client.prepareSearch(indexName).setTypes(typeName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).execute().actionGet();
            if (null != res) {
                SearchHit[] searchHits = res.getHits().getHits();
                for (SearchHit searchHit : searchHits) {
                    ids = searchHit.getId();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ids;
        }
        return ids;
    }

    /**
     * ip type 中根据ip 查询是否存在
     *
     * @return
     */
    public static String queryDataByDomain(String indexName, String typeName, TransportClient client, String domain) {
        String ids = null;
        SearchResponse res = null;
        try {
            QueryBuilder qb = QueryBuilders.matchPhraseQuery(Config.IP_TYPE_DOMAIN, domain);
            res = client.prepareSearch(indexName).setTypes(typeName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).execute().actionGet();
            if (null != res) {
                SearchHit[] searchHits = res.getHits().getHits();
                for (SearchHit searchHit : searchHits) {
                    ids = searchHit.getId();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ids;
        }
        return ids;
    }


    /**
     * loophole type 中根据ip port  查询是否存在
     *
     * @return
     */
    public static String queryDataByIpAndPort(String indexName, String typeName, TransportClient client, String hostIp, String port) {
        String ids = null;
        SearchResponse res = null;
        try {
            QueryBuilder qb = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(Config.IP_TYPE_NAME, hostIp)).must(QueryBuilders.matchQuery(Config.LOOPHOLE_PORT, port));
            res = client.prepareSearch(indexName).setTypes(typeName).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).execute().actionGet();
            if (null != res) {
                SearchHit[] searchHits = res.getHits().getHits();
                for (SearchHit searchHit : searchHits) {
                    ids = searchHit.getId();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ids;
        }
        return ids;
    }

    /**
     * 循环批量 插入   IP存在进行 更新 否则添加
     *
     * @param data
     */
    public static void insertIp(List <Map <String, Object>> data) {
        int count = 0;
        TransportClient client = ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT));
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (Map <String, Object> map : data) {
            //获取安全级别
            addDeviceSecureStatus(map);
            //更新或者插入逻辑
            String ids = ElasticsearchUtil.queryDataByIp(Config.INDEX_NAME, Config.IP_TYPE_NAME, client, map.get(Config.IP_ADDR).toString(), null);
            if (StringUtils.isNotBlank(ids)) {
                update(Config.INDEX_NAME, Config.IP_TYPE_NAME, ids, map, client);
            } else {
                bulkRequest.add(client.prepareIndex(Config.INDEX_NAME, Config.IP_TYPE_NAME).setSource(map, XContentType.JSON));
                // 每一千条提交一次
                if (count % 1000 == 0) {
                    bulkRequest.execute().actionGet();
                    //此处新建一个bulkRequest，类似于重置效果
                    bulkRequest = client.prepareBulk();
                    LogUtil.info("DNS服务器扫描插入" + count + "条数据");
                }
                count++;
            }
        }

        //添加批量请求 才插入数据
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
            LogUtil.info("DNS服务器扫描插入" + count + "条数据");
        }
    }


    /**
     * 循环批量 插入   域名存在进行 更新 否则添加
     *
     * @param data
     */
    public static void insertDomain(List <TreeMap <String, Object>> data) {
        int count = 0;
        TransportClient client = ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT));
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (Map <String, Object> map : data) {
            String ids = ElasticsearchUtil.queryDataByDomain(Config.INDEX_NAME, Config.IP_TYPE_DOMAIN, client, map.get(Config.IP_TYPE_DOMAIN).toString());
            if (StringUtils.isNotBlank(ids)) {
                TreeMap domainDetail = null;
                if (map.containsKey(Config.DOMAIN_SUB_CLASS_NAME)) {
                    domainDetail = (TreeMap) map.get(Config.DOMAIN_SUB_CLASS_NAME);
                }
                if (null != domainDetail && domainDetail.size() > 1) {
                    update(Config.INDEX_NAME, Config.IP_TYPE_DOMAIN, ids, map, client);
                }
            } else {
                bulkRequest.add(client.prepareIndex(Config.INDEX_NAME, Config.IP_TYPE_DOMAIN).setSource(map, XContentType.JSON));
                // 每一千条提交一次
                if (count % 1000 == 0) {
                    bulkRequest.execute().actionGet();
                    //此处新建一个bulkRequest，类似于重置效果
                    bulkRequest = client.prepareBulk();
                    LogUtil.info("域名插入" + count + "条数据");
                }
                count++;
            }
        }

        //添加批量请求 才插入数据
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
            LogUtil.info("域名插入" + count + "条数据");
        }
    }

    /**
     * 漏洞信息 插入
     *
     * @param data
     */
    public static void insertLoophole(List <Map <String, Object>> data) {
        int count = 0;
        TransportClient client = ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT));
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (Map <String, Object> map : data) {
            editIpTypeBySecurity(map);
            String ids = ElasticsearchUtil.queryDataByIpAndPort(Config.INDEX_NAME, Config.IP_TYPE_LOOPHOLE, client, map.get(Config.IP_TYPE_NAME).toString(), map.get(Config.LOOPHOLE_PORT).toString());
            if (StringUtils.isNotBlank(ids)) {
                update(Config.INDEX_NAME, Config.IP_TYPE_LOOPHOLE, ids, map, client);
            } else {
                bulkRequest.add(client.prepareIndex(Config.INDEX_NAME, Config.IP_TYPE_LOOPHOLE).setSource(map, XContentType.JSON));
                // 每一千条提交一次
                if (count % 1000 == 0) {
                    bulkRequest.execute().actionGet();
                    //此处新建一个bulkRequest，类似于重置效果
                    bulkRequest = client.prepareBulk();
                    LogUtil.info("漏洞扫描插入" + count + "条数据");
                }
                count++;
            }
        }

        //添加批量请求 才插入数据
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
            LogUtil.info("漏洞扫描插入" + count + "条数据");
        }
    }


    /**
     * 批量插入数据
     *
     * @param listMap
     */
    public static void batchInsert(List <Map <String, Object>> listMap) {
        BulkProcessor bulkProcessor = null;
        TransportClient client = ElasticsearchUtil.getClient(Config.ELASTICSEARCH_IP, Integer.parseInt(Config.ELASTICSEARCH_PORT));
        try {
            bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {

                @Override
                public void beforeBulk(long executionId, BulkRequest request) {
                }

                @Override
                public void afterBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest, BulkResponse bulkResponse) {
                    LogUtil.info("成功插入" + bulkRequest.numberOfActions() + "条数据");
                }

                //插入失败操作
                @Override
                public void afterBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest, Throwable throwable) {
                    LogUtil.info("插入失败" + bulkRequest.numberOfActions() + "条数据");
                }
            })
                    // 1w次请求执行一次bulk
                    .setBulkActions(5000)
                    // 1gb的数据刷新一次bulk
                    .setBulkSize(new ByteSizeValue(300, ByteSizeUnit.MB))
                    // 固定5s必须刷新一次
                    .setFlushInterval(TimeValue.timeValueSeconds(5))
                    // 并发请求数量, 0不并发, 1并发允许执行
                    .setConcurrentRequests(1).build();
                /*// 设置退避, 100ms后执行, 最大请求3次
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)).build();*/

            for (Map <String, Object> map : listMap) {
                bulkProcessor.add(new IndexRequest(Config.INDEX_NAME, Config.IP_TYPE_NAME).source(map));
            }
            bulkProcessor.flush();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("录入失败：" + e.getMessage());
        } finally {
            if (null != bulkProcessor) {
                bulkProcessor.close();
            }
        }
    }


    /**
     * 根据id修改数据
     *
     * @param id
     */
    public static void update(String idnexName, String typeName, String id, Map <String, Object> data, TransportClient client) {
        try {
            client.prepareUpdate(idnexName, typeName, id).setDoc(data).get();
            LogUtil.info(typeName + "修改成功：id为" + id);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info(typeName + "修改失败：" + e.getMessage());
        }
    }


    /**
     * 根据ip端口查询 设备安全级别
     */
    public static void addDeviceSecureStatus(Map <String, Object> data) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        List <String> deviceLoopholeInfo = null;
        try {
            if (data.containsKey(Config.IP_ADDR)) {
                queryBuilder.must(QueryBuilders.matchPhraseQuery(Config.IP_ADDR, data.get(Config.IP_ADDR)));
            }
            if (data.containsKey(Config.IP_PORT)) {
                queryBuilder.must(QueryBuilders.matchPhraseQuery(Config.IP_PORT, Integer.parseInt(data.get(Config.IP_PORT).toString())));
            }

            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(Config.INDEX_NAME).setTypes(Config.IP_TYPE_LOOPHOLE)
                    .setQuery(queryBuilder)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
            SearchResponse response = searchRequestBuilder.get();
            SearchHits hits = response.getHits();
            SearchHit[] hitsHits = hits.getHits();
            for (SearchHit hitsHit : hitsHits) {
                deviceLoopholeInfo = (List <String>) hitsHit.getSource().get(Config.LOOPHOLE_VULNERABILITY_INFORMATION);
            }

            int securityLevl = findSecurity((List <String>) data.get(Config.LOOPHOLE_VULNERABILITY_INFORMATION));
            data.put(Config.SECURITY_STATUS, securityLevl);
            LogUtil.info("根据Ip查询漏洞信息结果为！" + securityLevl);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("根据Ip查询漏洞信息失败！" + e.getMessage());
        }
    }


    /**
     * 根据ip+port 修改type=ip 中的安全级别字段
     *
     * @param data
     */
    public static void editIpTypeBySecurity(Map <String, Object> data) {
        List <String> deviceLoophole = null;
        try {
            String hostIp = data.get(Config.IP_TYPE_NAME).toString();
            Integer port = Integer.parseInt(data.get(Config.LOOPHOLE_PORT).toString());
            String id = ElasticsearchUtil.queryDataByIp(Config.INDEX_NAME, Config.IP_TYPE_NAME, client, hostIp, port);

            //当数据存在时进行更新
            if (StringUtils.isNotBlank(id)) {
                deviceLoophole = (List <String>) data.get(Config.LOOPHOLE_VULNERABILITY_INFORMATION);
                UpdateRequest updateRequest = new UpdateRequest(Config.INDEX_NAME, Config.IP_TYPE_NAME, id);
                updateRequest.doc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field(Config.SECURITY_STATUS, findSecurity(deviceLoophole))
                        .endObject())
                        .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

                UpdateResponse response = client.update(updateRequest).get();
                String isSuccess = (response.getShardInfo().getSuccessful() > 0) ? "成功" : "失败";
                LogUtil.info("修改安全级别！" + isSuccess);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("修改安全级别失败！" + e.getMessage());
        }
    }

    /**
     * 根据漏洞信息 判断安全级别
     *
     * @param deviceLoophole
     */
    public static int findSecurity(List <String> deviceLoophole) {
        int secureLevl;
        if (null == deviceLoophole || deviceLoophole.isEmpty()) {
            secureLevl = 1;
        } else if (deviceLoophole.size() > 5 && deviceLoophole.size() <= 10) {
            secureLevl = 2;
        } else {
            secureLevl = 3;
        }
        return secureLevl;
    }
}
