package org.yqj.es.demo.doc;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by yaoqijun on 2017-08-05.
 */
public class EsClientFiveTest {

    private static Client client;

    public static void main(String[] args) throws Exception {
        buildClient();

//        createIndex();

//        testSourcePut();

        testDelete();

        client.close();
    }

    public static void testDelete() throws Exception{
        client.admin().indices().delete(new DeleteIndexRequest("twitter")).actionGet();
    }

    public static void testSourcePut() throws Exception{
        IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
                .get();
    }

    public static void createIndex(){
//        XContentBuilder mapping = null;
//        try {
//            mapping = XContentFactory.jsonBuilder();
//            mapping.startObject().startObject("test_index_type");
//            mapping.startObject("properties");
//            mapping.startObject("teacher_id")
//                    .field("type", "long")
//                    .field("store", "no")
//                    .field("index", "not_analyzed")
//                    .endObject();
//            mapping.endObject();
//            mapping.endObject().endObject();
//        } catch (IOException e) {
//            System.out.println("error");
//        }
//
//        client.admin().indices()
//                .prepareCreate("test_index")
//                .addMapping("test_index_type",mapping)
//                .get(TimeValue.MINUS_ONE);
    }

    private static void buildClient(){
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
                .put("client.transport.sniff", true).build();
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.20.12.156"), 9300));
        }catch (Exception e){
            System.out.println("error");
        }
    }
}
