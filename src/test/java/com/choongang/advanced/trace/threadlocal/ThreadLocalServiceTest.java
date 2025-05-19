package com.choongang.advanced.trace.threadlocal;

import com.choongang.advanced.trace.threadlocal.code.FieldService;
import com.choongang.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {
     //private FieldService fieldService = new FieldService();
     private ThreadLocalService service = new ThreadLocalService();

     @Test
     void field() {
         log.info("main start");
         Runnable userA = () -> {
             service.logic("userA");
         };

         Runnable userB = () -> {
             service.logic("userB");
         };

         Thread threadA = new Thread(userA);
         threadA.setName("thread-A");

         Thread threadB = new Thread(userB);
         threadB.setName("thread-B");

         threadA.start();
         //sleep(2000);  // 동기화 문제 발생X
         sleep(100);  // 동기화 문제 발생O
         threadB.start();


         sleep(3000);  // 메인 쓰레드 종료 대기
         log.info("main exit");
     }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
