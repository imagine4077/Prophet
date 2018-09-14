package com.hackathon.prophet.utils;

import java.util.*;
import java.util.stream.Stream;

public class CollectionUtils
{
    public static <K, V extends Comparable<? super V>> Map<K, V> mapSortByValueAsc(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    /**
     * 对map集合进行降序排序
     * @param oldhMap
     * @return
     */
    /* public static Map<String, Integer> mapSortByValueDesc(Map<String, Integer> oldhMap) {

        *
         *   在 Collections 有个排序的方法  sort(List<T> list, Comparator<? super T> comparator)
         *   第一个参数为List map无法使用 所以要想办法把map转化为List
         *

        //把map转成Set集合
        Set<Map.Entry<String, Integer>> set = oldhMap.entrySet();

        //通过set 创建一个 ArrayList 集合
        List<Map.Entry<String, Integer>> arrayList = new ArrayList<>(set);

        //对arraylist进行倒序排序
        Collections.sort(arrayList, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> arg0,
                               Map.Entry<String, Integer> arg1) {
                //逆序 就用后面的参数 - 前面的参数
                return arg1.getValue() - arg0.getValue();
            }
        });


        //创建一个map
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        for (int i = 0; i < arrayList.size(); i++) {
            Map.Entry<String, Integer> entry = arrayList.get(i);
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }*/

    //降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> mapSortByValueDesc(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
            {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
