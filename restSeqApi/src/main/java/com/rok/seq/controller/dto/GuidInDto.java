package com.rok.seq.controller.dto;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * GUID 채번을 위한 입력데이터 DTO 
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class GuidInDto {
	
	/**
	 *  GUID 생성을 요청한 시스템코드
	 */
	@NotNull
	@NotBlank
	@Length(max = 3)
	@JsonProperty("sendChlCd")
	private String sendChlCd;
	
	/**
	 *  GUID 생성을 요청한 시스템의 노드번호
	 */
	@NotNull
	@DecimalMax(value="99") 
	@JsonProperty("sendSysNodeNo")
	private Integer sendSysNodeNo;
	
	/**
	 *  GUID 생성을 요청한 시스템의 인스턴스번호
	 */
	@NotNull
	@DecimalMax(value="99") 
	@JsonProperty("sendSysInstNo")
	private Integer sendSysInstNo;

	public String getSendChlCd() {
		return this.sendChlCd;
	}

	public void setSendChlCd(String sendChlCd) {
		this.sendChlCd = sendChlCd;
	}

	public Integer getSendSysNodeNo() {
		return this.sendSysNodeNo;
	}

	public void setSendSysNodeNo(Integer sendSysNodeNo) {
		this.sendSysNodeNo = sendSysNodeNo;
	}

	public Integer getSendSysInstNo() {
		return this.sendSysInstNo;
	}

	public void setSendSysInstNo(Integer sendSysInstNo) {
		this.sendSysInstNo = sendSysInstNo;
	}

	public String toString() {
		return "GuidInDto [sendChlCd=" + this.sendChlCd + ", sendSysNodeNo=" + this.sendSysNodeNo + ", sendSysInstNo="
				+ this.sendSysInstNo + "]";
	}
}
