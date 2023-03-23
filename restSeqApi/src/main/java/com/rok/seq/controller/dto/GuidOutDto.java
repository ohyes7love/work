package com.rok.seq.controller.dto;

/**
 * GUID 채번 결과 DTO 
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class GuidOutDto {
	
	/**
	 *  채번된 GUID
	 */
	private String guid;

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
}
