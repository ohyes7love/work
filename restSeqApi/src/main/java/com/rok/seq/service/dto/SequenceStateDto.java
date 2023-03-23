package com.rok.seq.service.dto;

import java.io.Serializable;

/**
 * 시퀀스 데이터를 Redis에 저장하기 위한 DTO 객체 클래스 
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class SequenceStateDto implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 *  날짜 값
	 */
	private String date;
	/**
	 *  시퀀스값
	 */
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
