package com.rok.seq.service.dto;

import java.io.Serializable;

public class SequenceStateDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String date;
	private long currentSequence;

	public SequenceStateDto() {
	}
	
	public SequenceStateDto(String date, long currentSequence) {
		this.date = date;
		this.currentSequence = currentSequence;
	}

	public String getDate() {
		return date;
	}

	public long getCurrentSequence() {
		return currentSequence;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setCurrentSequence(long currentSequence) {
		this.currentSequence = currentSequence;
	}

	@Override
	public String toString() {
		return "SequenceStateDto [date=" + date + ", currentSequence=" + currentSequence + "]";
	}

}
