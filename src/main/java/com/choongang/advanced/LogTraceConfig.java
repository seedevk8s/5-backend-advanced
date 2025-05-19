package com.choongang.advanced;

import com.choongang.advanced.trace.logtrace.FieldLogTrace;
import com.choongang.advanced.trace.logtrace.LogTrace;
import com.choongang.advanced.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

     @Bean
     public LogTrace logTrace() {
         //return new FieldLogTrace();
            return new ThreadLocalLogTrace();
     }
}
