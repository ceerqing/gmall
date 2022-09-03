package com.atguigu.gmall.item;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

/**
 * Author：张世平
 * Date：2022/9/3 17:19
 */
public class BloomFilterTest {
    @Test
    public void test() {
        BloomFilter<Long> bloomFilter = BloomFilter.create(Funnels.longFunnel(),
                200000,
                0.0001
        );

        bloomFilter.put(2L);
        bloomFilter.put(3L);
        bloomFilter.put(4L);

        System.out.println(bloomFilter.mightContain(2L));

    }
}
