package com.rok.seq.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

public class SequenceStateDto implements Serializable {
	private final LocalDate date;
	private final long currentSequence;
	private final Instant lastUpdate;

	public SequenceStateDto(LocalDate date, long currentSequence, Instant lastUpdate) {
		this.date = date;
		this.currentSequence = currentSequence;
		this.lastUpdate = lastUpdate;
	}

	public LocalDate getDate() {
		return date;
	}

	public long getCurrentSequence() {
		return currentSequence;
	}

	public Instant getLastUpdate() {
		return lastUpdate;
	}
}
