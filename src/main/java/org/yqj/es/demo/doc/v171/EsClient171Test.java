//package org.yqj.es.demo.doc.v171;
//
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.bulk.BulkItemResponse;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.IndicesAdminClient;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by yaoqijun on 2017/8/12.
// */
//public class EsClient171Test {
//
//    private static Client client = null;
//
//    private static Long totalPostCount = 0L;
//
//    public static void main(String[] args) {
//        System.out.println("start to test 171");
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
//                    postSingleBulkData();
//                }catch (Exception e){
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
//
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
//    private static void buildClient(){
//        Settings settings = ImmutableSettings.settingsBuilder()
//                .put("cluster.name", "elasticsearch")
//                .put("client.transport.sniff",true)
//                .build();
//        client = new TransportClient(settings)
//                .addTransportAddress(
//                        new InetSocketTransportAddress("localhost", 9300));
//    }
//
//    private static void close(){
//        if (client != null){
//            client.close();
//        }
//    }
//}
