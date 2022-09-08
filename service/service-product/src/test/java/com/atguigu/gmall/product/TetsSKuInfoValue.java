package com.atguigu.gmall.product;

import com.atguigu.gmall.common.util.Jsons;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * Author：张世平
 * Date：2022/8/31 10:42
 */
public class TetsSKuInfoValue {
    @Test
    public void testValue() {
        List<SkuIDVale> list=new ArrayList<>();
        list.add(new SkuIDVale(49L,118L));
        list.add(new SkuIDVale(49L,121L));
        list.add(new SkuIDVale(50L,120L));
        list.add(new SkuIDVale(50L,122L));

        Map<Long, List<SkuIDVale>> map = list.stream().
                collect(Collectors.groupingBy(SkuIDVale::getId));
        Map<Long,List<Long>> longMap=new HashMap<>();
        map.forEach((key,value)->{
          value.forEach(e->{
              if (longMap.get(key)==null){
                  ArrayList<Long> longs = new ArrayList<>();
                  longs.add(e.getSaleAttrValueId());
                  longMap.put(key,longs);
              }else {
                  List<Long> list1 = longMap.get(key);
                  list1.add(e.getSaleAttrValueId());
                  longMap.put(key,list1);
              }
          });
        });
        System.out.println(longMap);
        String longMapJson = Jsons.toStr(longMap);
        System.out.println("longMapJson = " + longMapJson);

        Map<String,Long> json=new HashMap<>();

        longMap.forEach((key,value)->{
            String endString = value.toString().replace(",", "|")
                    .replace(" ", "").replace("[", "")
                    .replace("]", "");
            json.put(endString,key);
        });
        String endJson = Jsons.toStr(json);
        System.out.println("endJson = " + endJson);
    }


    @Test
    public void testListLong() {
        List<Integer> list=new ArrayList<>();
        list.add(118);
        list.add(121);
        String listLong = list.toString();
        System.out.println("listLong = " + listLong);
    }


}
