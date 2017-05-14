package org.smart4J.memcachetest;

import com.schooner.MemCached.MemcachedItem;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/9.
 */
public class MemcacheTest {
    protected static MemCachedClient memCachedClient;

    static{
        memCachedClient = new MemCachedClient();
        // 设置缓存服务器列表，当使用分布式缓存的时，可以指定多个缓存服务器。
        String[] servers ={"127.0.0.1:11211"};

        // 设置服务器权重
        Integer[] weights = {3, 2};

        // 创建一个Socked连接池实例
        SockIOPool pool = SockIOPool.getInstance();

        pool.setServers(servers);
        pool.setWeights(weights);
        pool.setFailover(true);//容错
        pool.setInitConn(10);  //设置初始连接数
        pool.setMinConn(5); //设置最小连接数
        pool.setMaxConn(100); //设置最大连接数
        pool.setMaintSleep(30); //设置连接池维护线程睡眠时间
        pool.setNagle(false); //设置是否适用Nagle算法
        pool.setSocketTO(3000); //设置Socket的读取等待超时时间
        pool.setAliveCheck(true); //设置连接心跳连接开关
        pool.setHashingAlg(SockIOPool.CONSISTENT_HASH); //设置Hash算法
        // initialize the connection pool
        pool.initialize();
    }

    /**
     * Add方法新增缓存，如果缓存服务器存在同样的key,则新增失败，返回false
     */
    public static boolean add(String key, Object value){
        return memCachedClient.add(key,value);
    }

    /**
     * Set方法将数据保存到缓存服务器，如果缓存服务器存在同样的key,则将其替换
     */
    public static boolean set(String key, Object value){
        return memCachedClient.set(key,value);
    }

    /**
     *  Replace方法用来替换相同key的值，如果key不存在，则返回false
     */
    public static boolean replace (String key,Object value){
        return memCachedClient.replace(key,value);
    }

    /**
     *  从缓存服务器中获取该key对应的数据，如果key不存在,则返回null
     */
    public static Object get(String key){
        return  memCachedClient.get(key);
    }

    /**
     * 一次性从缓存中获取一组数据
     */
    public static Map<String, Object> getMulti(String[] keys){
        return memCachedClient.getMulti(keys);
    }

    /**
     *  在指定的key的值后面增加后缀
     */
    public static boolean append(String key, Object value){
        return memCachedClient.append(key , value);
    }

    /**
     *  在指定的key的值前面面增加前缀
     */
    public static boolean prepend(String key, Object value){
        return memCachedClient.prepend(key , value);
    }

    /**
     *  获取key对应的值和版本号
     */
    public static MemcachedItem gets(String key){
        return  memCachedClient.gets(key);
    }

    /**
     *  将key对应的值进行增量i操作
     */
    public static  long incr(String key, int i){
        return memCachedClient.incr(key,i);
    }

    /**
     *  将key对应的值进行减量i操作
     */
    public static  long decr(String key, int i){
        return memCachedClient.decr(key,i);
    }


    public static void main(String[] args) {
        //Add
        System.out.println("第一次Add缓存car:"+MemcacheTest.add("car","Rubicon"));
        System.out.println("第二次Add缓存car:"+MemcacheTest.add("car","Rubicon & Sahara"));
        System.out.println("Get缓存car的值:"+MemcacheTest.get("car"));
        System.out.println("Get不存在的缓存BIke:"+MemcacheTest.get("BIke"));

        //Set
        System.out.println("第一次Set缓存Moto:"+MemcacheTest.set("Moto","Honda"));
        System.out.println("第二次Set缓存Moto:"+MemcacheTest.set("Moto","Dayang"));
        System.out.println("Get缓存Moto的值:"+MemcacheTest.get("Moto"));

        //Replace
        System.out.println("Replace缓存car的值:"+MemcacheTest.replace("car","Rubicon & Sahara"));
        System.out.println("Replace不存在的缓存Dog:"+MemcacheTest.replace("Dog","Labrado"));
        System.out.println("Get缓存car的值:"+MemcacheTest.get("car"));
        System.out.println("Get不存在的缓存Dog:"+MemcacheTest.get("Dog"));

        //GetMulti
        String[] keys = {"car","Moto"};
        Map<String,Object> result = MemcacheTest.getMulti(keys);
        for (String key : result.keySet()){
            System.out.println("GetMulti方法一次性获取一组数据："+key +"<==>"+result.get(key));
        }


        // Prepend
        System.out.println("Prepend在car的值前面增加前缀:"+MemcacheTest.prepend("car","I want to buy "));
        //Append
        System.out.println("Append在car的值后面增加后缀:"+MemcacheTest.append("car",",But I have no money at all! "));
        System.out.println("Get缓存car的值:"+MemcacheTest.get("car"));

        //Gets
        MemcachedItem item = MemcacheTest.gets("car");
        System.out.println("Gets缓存car的值:"+item.getValue()+"和版本号:"+item.getCasUnique());

        //incr
        System.out.println("添加一个number值:"+MemcacheTest.add("number","1"));
        System.out.println("Incr对number的值进行增量:"+MemcacheTest.incr("number",1));
        System.out.println("Get缓存number的值:"+MemcacheTest.get("number"));

        //decr
        System.out.println("decr对number的值进行减量:"+MemcacheTest.decr("number",1));
        System.out.println("Get缓存number的值:"+MemcacheTest.get("number"));

    }
}
