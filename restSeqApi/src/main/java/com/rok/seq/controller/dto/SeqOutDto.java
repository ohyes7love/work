package com.rok.seq.controller.dto;

/**
 * 시퀀스 채번 결과를 담는 DTO 
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class SeqOutDto {

	/**
	 *  채번된 시퀀스값
	 */
	private Long sequence;

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

}
