package com.rok.seq.controller.dto;

/**
 * 현재 시퀀스데이터를 담아 리턴하기위한 DTO 
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class CurrSeqOutDto {

	/**
	 *  시퀀스값
	 */
	private Long sequence;
	/**
	 *  날짜 값
	 */
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

}
