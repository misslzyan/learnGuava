package com.dwd.test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * Created by bjduanweidong on 2017/4/1.
 */
public class CacheTest {

    private static final Logger logger = Logger.getLogger(CacheTest.class);



    /**
     * 测试插入覆盖
     */
    @Test
    public void test1(){
        Cache<String,String> cache = CacheBuilder.newBuilder().build();
//        cache.asMap().putIfAbsent("key1","value1");
//        cache.asMap().put("key1","value1");
        try {
            String result = cache.get("key1", ()-> {
//                @Override
//                public String call() throws Exception {
                    return "value2";
//                }
            });
            System.out.println(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws ExecutionException {
        LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            @Override
            public String load(String s) throws Exception {
                return s+"value";
            }
        });
        String result = loadingCache.get("m");
        Assert.assertEquals("right",result,"mvalue");
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        LoadingCache<String,String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(1000L)
                .expireAfterWrite(10,TimeUnit.SECONDS)
                .refreshAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        logger.debug("come in load");
                        return s+"value";
                    }

                    @Override
                    public ListenableFuture<String> reload(final String key, String oldValue) throws Exception {
                        System.out.println("come into reload");
                        if(key.equals("a")){
                            return Futures.immediateFuture(oldValue);
                        }else {
                            ListenableFutureTask task = ListenableFutureTask.create(()-> {
                                    return key+"reloadValue";
                            });
                            executor.execute(task);
                            return task;
                        }
                    }
                });
        logger.debug(loadingCache.get("b"));
        Thread.sleep(15009);
        logger.debug(loadingCache.get("b"));
        logger.debug("end");
    }

}
