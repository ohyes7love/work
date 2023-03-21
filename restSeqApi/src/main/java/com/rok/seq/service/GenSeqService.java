package com.rok.seq.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rok.seq.service.dto.SequenceStateDto;

@Service
public class GenSeqService {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	private static final String FILENAME = "sequence.dat";
	private LocalDate date = LocalDate.now();
	private long currentSequence = 0L;
	private Instant lastUpdate = Instant.EPOCH;
	private static final long MAX_SEQUENCE_NUMBER = 9999999999L;

	public synchronized long next(boolean isDayTest, boolean isMaxSeqTest) throws IOException, ClassNotFoundException {
		File file = new File(FILENAME);
		if (file.exists()) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
				SequenceStateDto state = (SequenceStateDto) in.readObject();
				if(isDayTest) {
					date = state.getDate().minusDays(1);
				} else {
					date = state.getDate();
				}
				currentSequence = state.getCurrentSequence();
				lastUpdate = state.getLastUpdate();
			}
		} else {
			date = LocalDate.now();
			currentSequence = 0L;
			lastUpdate = Instant.EPOCH;
			saveState();
		}
		
		if(isMaxSeqTest) {
			currentSequence = MAX_SEQUENCE_NUMBER-1 ;
		}
		if(MAX_SEQUENCE_NUMBER == currentSequence) {
			throw new RuntimeException("시퀀스 제한 수를 초과하였습니다.") ;
		}
		
		LocalDate now = LocalDate.now();
		if (!date.equals(now)) {
			date = now;
			currentSequence = 0L;
		}
		currentSequence++;
		lastUpdate = Instant.now();
		saveState();
		
		logger.info("seq: {}", currentSequence);
		
		return currentSequence;
	}

	public synchronized long current() throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File(FILENAME);
		if (file.exists()) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
				SequenceStateDto state = (SequenceStateDto) in.readObject();
				date = state.getDate(); 
				currentSequence = state.getCurrentSequence();
				lastUpdate = state.getLastUpdate();
			}
		}
		return currentSequence;
	}

	private void saveState() throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
			out.writeObject(new SequenceStateDto(date, currentSequence, lastUpdate));
		}
	}

}
