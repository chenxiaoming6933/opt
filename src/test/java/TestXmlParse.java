/**
 * @title: TestXmlParse
 * @projectName resolve
 * @description: TODO
 * @author ChenXiaoMing
 * @date 2019/9/2513:12
 */

import bean.Config;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.junit.Test;
import util.DateFormat;
import util.ElasticsearchUtil;
import util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName 利用DOM4J解析xml
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class TestXmlParse {

    @Test
    public void test() {

        //1.创建Reader对象
        SAXReader reader = new SAXReader();
        //2.加载xml
        try {
            Document document = reader.read(new File("F:\\trojanwall\\dns\\xml\\192.168.1.1.xml"));
            Element root = document.getRootElement();

            List <Element> port = root.selectNodes("//port");
            for (int i = 0; i < port.size(); i++) {
                Element element = port.get(i);

                List <Element> script = element.selectNodes("script");
                for (Element element1 : script) {
                    String value = element1.attribute("output").getValue();
                    if (value.contains("\n")) {
                        String[] split = value.split("&#xa;");
                        for (String s : split) {
                            System.out.println("s");
                            System.out.println("=====");
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testTarGz() {
        FileUtils.decompressTarGz(new File("F:\\trojanwall\\dns\\xml\\VULSCAN_Z1Z7Y4B620191010_1571290741000.tgz"), "F:\\trojanwall\\dns\\xml\\var");
    }

    @Test
    public void testEdid() {
        TransportClient client = ElasticsearchUtil.getClient("192.168.1.242", 9301);
        try {
            UpdateRequest updateRequest = new UpdateRequest(Config.INDEX_NAME, Config.IP_TYPE_NAME, "AW4DIh8DJwPExsdiMh_d");
            updateRequest.doc(XContentFactory.jsonBuilder()
                    .startObject()
                    .field(Config.SECURITY_STATUS, 2)
                    .endObject())
                    .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

            UpdateResponse response = client.update(updateRequest).get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testStr() {
        TransportClient client = ElasticsearchUtil.getClient("192.168.1.242", 9301);
        AggregationBuilder  termsBuilder = AggregationBuilders.terms("security_status").field("security_status");
        //AggregationBuilder  countBuilder=AggregationBuilders.count("count").field("security_status");
        //termsBuilder.subAggregation(countBuilder);

        SearchRequestBuilder sv=client.prepareSearch("dns_portrait").setTypes("ip").addAggregation(termsBuilder);
        SearchResponse response=  sv.get();

        Aggregations terms= response.getAggregations();
        for (Aggregation a:terms){
            LongTerms teamSum= (LongTerms)a;
            List <Terms.Bucket> buckets = teamSum.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                System.out.println(bucket.getKeyAsString()+"===="+bucket.getDocCount());
            }
            System.out.println("group 一次");
        }
    }
}
