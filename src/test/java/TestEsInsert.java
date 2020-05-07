/**
 * @title: TestEsInsert
 * @projectName resolve
 * @description: TODO
 * @author ChenXiaoMing
 * @date 2019/9/615:37
 */

import bean.Config;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import util.ElasticsearchUtil;
import org.elasticsearch.action.search.SearchRequestBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName es测试类
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class TestEsInsert {

    @Test
    public void queryByLimit() {

        TransportClient client = ElasticsearchUtil.getClient("192.168.1.35", 9301);

        SearchResponse searchResponse = client.prepareSearch(Config.INDEX_NAME,Config.IP_TYPE_NAME)
                .setQuery(QueryBuilders.matchAllQuery()) //查询所有
                //.setQuery(QueryBuilders.matchQuery("port", 53).operator(Operator.AND)) //根据tom分词查询name,默认or
                //.setQuery(QueryBuilders.multiMatchQuery("tom", "name", "age")) //指定查询的字段
                //.setQuery(QueryBuilders.queryString("name:to* AND age:[0 TO 19]")) //根据条件查询,支持通配符大于等于0小于等于19
                //.setQuery(QueryBuilders.termQuery("name", "tom"))//查询时不分词
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                //.addSort(SortBuilders.fieldSort("timestamp").unmappedType("date").order(SortOrder.DESC))//排序
                .setFrom(10).setSize(10)//分页
                .get();

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println(total);
        SearchHit[] searchHits = hits.hits();
        for (SearchHit s : searchHits) {
            System.out.println("=========");
            System.out.println(s.getSourceAsString() + "====");
        }
    }


    @Test
    public void testQuery() {

        TransportClient client = ElasticsearchUtil.getClient("192.168.1.35", 9301);

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(Config.INDEX_NAME).setTypes("ip");

        SearchResponse searchResponse = searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();

        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits()+"======数量");

        SearchHit[] hits1 = hits.getHits();
        for (SearchHit searchHitFields : hits1) {
            System.out.println(searchHitFields.getId());
            System.out.println(searchHitFields.getSourceAsString());
        }
    }

}
