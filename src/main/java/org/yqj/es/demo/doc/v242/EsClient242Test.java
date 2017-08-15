//package org.yqj.es.demo.doc.v242;
//
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.bulk.BulkItemResponse;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.update.UpdateRequestBuilder;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.IndicesAdminClient;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.yqj.es.demo.doc.v171.ContentConstant;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by yaoqijun on 2017/8/12.
// */
//public class EsClient242Test {
//
//    private static Client client = null;
//
//    private static Long totalPostCount = 0L;
//
//    public static void main(String[] args) throws Exception {
//        System.out.println("start to test 242");
//        buildClient();
//        cleanIndex(ContentConstant.INDEX_NAME);
//
//        // add execute thread
//        final SinglePostJob singlePostJob = new SinglePostJob();
//        final ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.submit(singlePostJob);
//
//        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.schedule(new Runnable() {
//            public void run() {
//                System.out.println("current post count " + totalPostCount);
//                singlePostJob.stop();
//                try {
//                    Thread.sleep(1000L);
//                }catch (Exception e){
//                    System.out.println("error sleep content");
//                }
//                executorService.shutdownNow();
//                scheduledExecutorService.shutdown();
//                close();
//            }
//        }, 60, TimeUnit.SECONDS);
//        System.out.println("start in protected");
//    }
//
//    public static class SinglePostJob implements Runnable{
//
//        private boolean isRunning = true;
//
//        public void run() {
//            while (isRunning){
//                try {
////                    postSingleBulkData();
//
////                    indexSingleData();
//
//                    upsertDocument();
//                }catch (Exception e){
//                    e.printStackTrace();
//                    System.out.println("!! ERROR post content");
//                    continue;
//                }
//
//                totalPostCount ++ ;
//            }
//        }
//
//        public void stop(){
//            isRunning = false;
//        }
//    }
//
//    private static boolean upsertDocument(){
//        ContentConstant.addId();
//        XContentBuilder xContentBuilder = ContentConstant.buildPostDataWithRandom();
//        UpdateRequestBuilder builder = client.prepareUpdate(ContentConstant.INDEX_NAME, ContentConstant.TYPE, ContentConstant.id.toString()).setDoc(xContentBuilder)
//                .setDocAsUpsert(true);
//        UpdateResponse updateResponse = builder.get();
//        if (!updateResponse.isCreated()){
//            System.out.println("not created");
//        }
//        return true;
//    }
//
//    // index single data
//    private static boolean indexSingleData(){
//        ContentConstant.addId();
//        XContentBuilder xContentBuilder =  ContentConstant.buildPostDataWithRandom();
//        IndexResponse indexResponse = client.prepareIndex(ContentConstant.INDEX_NAME, ContentConstant.TYPE, ContentConstant.id.toString())
//                .setSource(xContentBuilder)
//                .get();
//        if (!indexResponse.isCreated()){
//            System.out.println("print not create index single");
//        }
//        return true;
//    }
//
//    // post single by bulk
//    private static boolean postSingleBulkData(){
//        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
//
//        ContentConstant.addId();
//        XContentBuilder xContentBuilder =  ContentConstant.buildPostDataWithRandom();
//
//        bulkRequestBuilder.add(client
//                .prepareIndex(ContentConstant.INDEX_NAME, ContentConstant.TYPE, ContentConstant.id.toString())
//                .setSource(xContentBuilder));
//
//        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
//        if (bulkResponse.hasFailures()){
//            for (BulkItemResponse bulkItemResponse : bulkResponse.getItems()) {
//                System.out.println(bulkItemResponse.getFailureMessage());
//            }
//        }
//        return true;
//    }
//
//    private static void cleanIndex(String indexName){
//        // delete index
//        IndicesAdminClient indicesAdminClient = client.admin().indices();
//        Boolean isExitIndex = indicesAdminClient.prepareExists(indexName).execute().actionGet().isExists();
//        if (isExitIndex){
//            indicesAdminClient.delete(new DeleteIndexRequest(indexName)).actionGet();
//        }
//
//        // create index
//        client.admin().indices()
//                .prepareCreate(indexName)
//                .addMapping(ContentConstant.TYPE, ContentConstant.buildXContent())
//                .get(TimeValue.timeValueSeconds(10));
//    }
//
//    private static void buildClient() throws UnknownHostException{
////        Settings settings = Settings.settingsBuilder()
////                .put("cluster.name", "elasticsearch")
////                .put("client.transport.sniff",true)
////                .build();
//        client = TransportClient.builder()
////                .settings(settings)
//                .build()
//                .addTransportAddress(
//                        new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
//    }
//
//    private static void close(){
//        if (client != null){
//            client.close();
//        }
//    }
//}
