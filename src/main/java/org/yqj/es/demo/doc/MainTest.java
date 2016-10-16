package org.yqj.es.demo.doc;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasOrIndex;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.net.InetAddress;
import java.util.Date;

/**
 * Created by yaoqijun.
 * Date:2016-10-12
 * Email:yaoqijunmail@gmail.io
 * Descirbe: 测试对应的 ES 单点效果
 */
public class MainTest {

    public static void main(String[] args) throws Exception{
        System.out.println("this is test es java api doc content");
//        createIndex();
        getIndexInfo();
    }


    public static void getIndexInfo() throws Exception{
        Client client = buildESClient();
        GetResponse getFields = client.prepareGet("twitter", "tweet", "1").get();
        System.out.println(getFields.getIndex());
    }

    /**
     * 创建对应的 Index信息内容
     */
    public static void createIndex() throws Exception{

        Client client = buildESClient();

//        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject()
//                .field("user", "yaoqijun")
//                .field("postDate", new Date())
//                .field("message", "null this client test message")
//                .endObject();

//        IndexResponse response = client.prepareIndex("terminus", "person", "1").setSource(xContentBuilder).get();

        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";

        IndexResponse response = client.prepareIndex("twitter", "tweet")
                .setSource(json)
                .get();

        // output result

        System.out.println(response.getId());
        System.out.println(response.getIndex());
        System.out.println(response.getType());

        // close
        client.close();

    }

    /**
     * get client to do
     * @return
     * @throws Exception
     */
    public static Client buildESClient() throws Exception{
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "lee")
                .put("client.transport.sniff", true).build();
        return TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }
}
