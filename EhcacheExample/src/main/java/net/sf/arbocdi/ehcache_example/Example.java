/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.arbocdi.ehcache_example;

import java.io.InputStream;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 *
 * @author root
 */
public class Example {

    public static void main(String[] args) throws Exception {
        //прочитаем конфигурацию CacheManager из xml-файла
        //в котором описан CacheManager и набор его кешей
        InputStream in = null;
        CacheManager manager = null;
        try {
            in = Example.class.getResourceAsStream("testManager.xml");
            manager = CacheManager.newInstance(in);
            in.close();
            //получим кеш из менеджера по имени
            Ehcache cache = manager.getCache("cache1");
            cache.put(new Element("k1","v1"));
            System.out.println(cache.get("k1"));
        } finally {
            in.close();
            manager.shutdown();
        }
    }
}
