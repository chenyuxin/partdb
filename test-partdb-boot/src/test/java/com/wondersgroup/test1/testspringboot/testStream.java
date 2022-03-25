package com.wondersgroup.test1.testspringboot;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class testStream {
	
	@Test
    public void asyncThread3()throws Exception{
        CompletableFuture<String> a = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> b = CompletableFuture.supplyAsync(() -> "youth");
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> "!");

        CompletableFuture<?> all = CompletableFuture.allOf(a,b,c);
        all.get();

        String result = Stream.of(a, b,c)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));

        System.out.println(result);
    }

}
