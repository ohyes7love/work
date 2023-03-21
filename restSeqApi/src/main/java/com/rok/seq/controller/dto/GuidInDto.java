package com.rok.seq.controller.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GuidInDto {
	
	@NotNull
	@NotBlank
	@Length(max = 3)
	private String sendChlCd;
	
	@NotNull
	@DecimalMax(value="99") 
	private Integer sendSysNodeNo;
	
	@NotNull
	@DecimalMax(value="99") 
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
