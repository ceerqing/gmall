package com.atguigu.gmall.seacher;

import com.atguigu.gmall.seacher.bean.Person;
import com.atguigu.gmall.seacher.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Author：张世平
 * Date：2022/9/9 10:22
 */
@SpringBootTest
public class SeacherTest {
    @Autowired
    PersonRepository personRepository;

    @Test
    public void testSave() {
        Person person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("三555");
        person1.setLastName("张");
        person1.setAge(19);
        person1.setAddress("北京市昌平区");

        Person person = new Person();
        person.setId(2L);
        person.setFirstName("三");
        person.setLastName("张");
        person.setAge(19);
        person.setAddress("北京市朝阳区");

        Person person2 = new Person();
        person2.setId(3L);
        person2.setFirstName("四");
        person2.setLastName("力");
        person2.setAge(19);
        person2.setAddress("上海市松江区");


        Person person3 = new Person();
        person3.setId(4L);
        person3.setFirstName("三2");
        person3.setLastName("张");
        person3.setAge(20);
        person3.setAddress("北京市天安门");
        personRepository.save(person1);
        personRepository.save(person);
        personRepository.save(person2);
        personRepository.save(person3);

        System.out.println("完成...");

    }
}
