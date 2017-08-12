package org.yqj.es.demo.doc.v171;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by yaoqijun on 2017/8/12.
 */
public class ContentConstant {

    public static final String INDEX_NAME = "twitter";

    public static final String TYPE = "twitter_type";

    public static Integer id = 1;

    /*
    * random generate post data
    * */
    public static XContentBuilder buildPostDataWithRandom(){
        XContentBuilder xContentBuilder = null;
        try {
            xContentBuilder = XContentFactory.jsonBuilder().startObject();
            xContentBuilder.field("id", id);
            xContentBuilder.field("name", "Jim come test condition different condition");
            xContentBuilder.field("max", 100.0D);
            xContentBuilder.field("min", 200.0D);
            xContentBuilder.endObject();
        }catch (IOException e){
            e.printStackTrace();
        }
        return xContentBuilder;
    }

    public static void addId(){
        id = id + 1;
    }

    public static XContentBuilder buildXContent(){
        XContentBuilder xContentBuilder = null;

        try {
            xContentBuilder = XContentFactory.jsonBuilder().startObject()
                    .startObject(TYPE)
                    .startObject("properties");

            buildId(xContentBuilder);
            buildName(xContentBuilder);
            buildMax(xContentBuilder);
            buildMin(xContentBuilder);

            xContentBuilder.endObject().endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xContentBuilder;
    }

    private static void buildId(XContentBuilder contentBuilder) throws IOException{
        contentBuilder.startObject("id")
                .field("type", "long")
                .field("store","no")
                .field("index","not_analyzed")
                .endObject();
    }

    private static void buildName(XContentBuilder contentBuilder) throws IOException{
        contentBuilder.startObject("name")
                .field("type", "string")
                .field("store","no")
                .field("index","not_analyzed")
                .endObject();
    }

    private static void buildMax(XContentBuilder contentBuilder) throws IOException{
        contentBuilder.startObject("max")
                .field("type", "double")
                .field("store","no")
                .field("index","not_analyzed")
                .endObject();
    }

    private static void buildMin(XContentBuilder contentBuilder) throws IOException{
        contentBuilder.startObject("min")
                .field("type", "double")
                .field("store","no")
                .field("index","not_analyzed")
                .endObject();
    }
}
