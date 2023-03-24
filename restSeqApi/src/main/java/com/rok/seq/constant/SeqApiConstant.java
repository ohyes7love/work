package com.rok.seq.constant;

/**
 * 채번 API 서버에서 사용하는 상수 모음 Class
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
public class SeqApiConstant {
	/**
	 * 랜덤 값을 처리하기위해 정의한 변수
	 */
	public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	/**
	 * 시퀀스 최대값
	 */
	public static final long MAX_SEQUENCE_NUMBER = 9999999999L;
	/**
	 * 동시접근제어를 위한 lock key값
	 */
	public static final String LOCK_KEY = "seqLock";
	/**
	 * redis에 저장하는 시퀀스정보의 key
	 */
	public static final String SEQ_KEY = "seq";
}
