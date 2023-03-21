package com.rok.seq.service;

import java.io.IOException;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.rok.seq.controller.dto.GuidInDto;
import com.rok.seq.utils.DateUtils;

@Service
public class GenGuidService {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final Random RANDOM = new Random();

	/**
	 * guid 생성 요청 시스템코드와 해당 시스템의 노드번호, 인스턴스번호를 받아 guid를 채번한다. 규칙:
	 * yyyyMMddHHmmssSSS(17) + 시스템코드(3) + 노드번호(2) + 인스턴스번호(2) + 랜덤String(6) = 30자리
	 * 
	 * @param in String sendChlCd : guid채번요청 시스템코드 Integer sendSysNodeNo : guid채번요청
	 *           시스템의 노드번호 Integer sendSysInstNo : guid채번요청 시스템의 인스턴스번호
	 * @return String guid : 생성된 guid
	 */
	public String generateGuid(GuidInDto in) {

		StringBuffer guidBuffer = new StringBuffer(30);
		append(guidBuffer, DateUtils.getCurrentDataTimeMillis());
		append(guidBuffer, in.getSendChlCd());
		append(guidBuffer, String.format("%02d", in.getSendSysNodeNo()));
		append(guidBuffer, String.format("%02d", in.getSendSysInstNo()));
		append(guidBuffer, generateRandomString(6));

		return guidBuffer.toString();
	}

	private static Appendable append(Appendable appendable, CharSequence sequence) {
		return append(appendable, sequence, sequence.length());
	}

	private static Appendable append(Appendable appendable, CharSequence sequence, int length) {
		try {
			for (int i = 0; i < length; i++)
				appendable.append(sequence.charAt(i));
		} catch (IOException e) {
		}
		return appendable;
	}
	
	private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
