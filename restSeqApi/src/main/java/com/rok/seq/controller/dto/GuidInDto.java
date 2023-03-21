package com.rok.seq.controller.dto;

public class GuidInDto {
  String sendChlCd;
  
  Integer sendSysNodeNo;
  
  Integer sendSysInstNo;
  
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
    return "GuidInDto [sendChlCd=" + this.sendChlCd + ", sendSysNodeNo=" + this.sendSysNodeNo + ", sendSysInstNo=" + this.sendSysInstNo + "]";
  }
}
