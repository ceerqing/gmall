package com.atguigu.gmall.seacher.bean;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Author：张世平
 * Date：2022/9/9 9:09
 */
@Document(indexName = "person",shards = 1,replicas = 1)
@Data
public class Person {

    @Id
    private Long id;  //主键

    @Field(value = "first",type = FieldType.Keyword) //TEXT【存的时候会分词】，keyword【关键字不分词】：都是字符串
    private String firstName;  // 雷丰阳

    @Field(value = "last",type = FieldType.Keyword)
    private String lastName;

    @Field(value = "age")
    private Integer age;

    @Field(value = "address",type=FieldType.Text ,analyzer = "ik_smart") //自动决定，Java的String默认是ES的TEXT类型
    private String address;
}
