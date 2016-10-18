package org.yqj.es.demo.doc;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.util.Date;

/**
 * Created by yaoqijun on 2016/10/13.
 * org elasticsearch java client api test
 * 测试 java client 配置方式
 */
public class MainTest {

//    public static void main(String[] args) throws Exception {
//        System.out.println("test client api");
//
//        searchIndexTest();

//        bulkIndexTest();

//        indexDeleteTest();

//        indexCreateTest();

//        indexGetTest();
//    }


    public static void searchIndexTest() throws Exception{

        Client client = buildClient();

        SearchResponse searchResponse = client.prepareSearch("twitter")
                .setTypes("tweet").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("multi", "test"))
                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))
                .setFrom(0).setSize(60).setExplain(true)
                .execute().actionGet();

        for(SearchHit searchHit : searchResponse.getHits().getHits()){
            System.out.println(searchHit.toString());
        }

        client.close();

    }

    public static void bulkIndexTest() throws Exception{

        Client client = buildClient();

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();

        bulkRequestBuilder.add(client.prepareIndex("twitter", "test", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
        );

        bulkRequestBuilder.add(client.prepareIndex("twitter", "test", "2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "another post")
                        .endObject()
                )
        );

        BulkResponse bulkResponse = bulkRequestBuilder.get();

        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item

            bulkResponse.getItems();
        }

        client.close();

    }

    /**
     * index delete
     * @throws Exception
     */
    public static void indexDeleteTest() throws Exception{

        Client client = buildClient();

        DeleteResponse deleteResponse = client.prepareDelete("twitter", "tweet", "kimchy").get();

        System.out.println(deleteResponse.toString());

        client.close();

    }

    /**
     * create index method
     * @throws Exception
     */
    public static void indexCreateTest() throws Exception{

        Client client = buildClient();

        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";

        IndexResponse indexResponse = client.prepareIndex("twitter", "tweet")
                .setSource(json).get();

        System.out.println(indexResponse.toString());

        client.close();

    }


    /**
     * client get method
     */
    public static void indexGetTest() throws Exception {
        Client client = buildClient();

        GetResponse getResponse = client.prepareGet("twitter", "tweet", "2").get();

        System.out.println(getResponse.getSourceAsString());

        client.close();

    }

    /**
     * build client
     * @return
     * @throws Exception
     */
    public static Client buildClient() throws Exception {

        Settings settings = Settings.builder()
                .put("cluster.name","elasticsearch")
                .put("client.transport.sniff",true)
                .build();

        return TransportClient.builder()
                .settings(settings)
                .build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.240.128"), 9300));
    }

}
