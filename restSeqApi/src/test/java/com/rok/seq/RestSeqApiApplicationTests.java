package com.rok.seq;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rok.seq.service.GenSeqService;

@SpringBootTest
class RestSeqApiApplicationTests {

	@Autowired
	private GenSeqService seqService;
	
	private static final String URL = "http://localhost:9091/seqApi/getSeq"; // 테스트할 API 주소
	private static final int NUM_THREADS = 10; // 동시 접근할 스레드 수
	private static final int NUM_REQUESTS_PER_THREAD = 10; // 각 스레드에서 수행할 요청 수

	@Test
	void seqTest() {
		for (int i = 0; i < NUM_THREADS; i++) {
			Thread thread = new Thread(() -> {
				for (int j = 0; j < NUM_REQUESTS_PER_THREAD; j++) {
					try {
						seqService.next(false, false);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
	}

	private static void sendRequest() {
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST"); // HTTP 요청 메소드 설정
			int responseCode = conn.getResponseCode(); // HTTP 응답 코드 받기
			System.out.println(Thread.currentThread().getName() + ": " + responseCode);
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
