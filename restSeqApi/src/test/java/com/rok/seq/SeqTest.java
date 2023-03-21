package com.rok.seq;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rok.seq.service.GenSeqService;

@SpringBootTest
class SeqTest {

	@Autowired
	private GenSeqService seqService;
	
	@Test
	void test() {
		try {
			ExecutorService executorService = Executors.newFixedThreadPool(10);
		    CountDownLatch countDownLatch = new CountDownLatch(100);
		    for (int i = 1; i <= 100; i++) {
		        executorService.execute(() -> {
		            try {
						System.out.println(seqService.next(false, false));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            countDownLatch.countDown();
		        });
		    }

		    countDownLatch.await();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
