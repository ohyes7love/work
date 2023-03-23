package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rok.seq.controller.dto.CurrSeqOutDto;
import com.rok.seq.service.GenSeqService;
import com.rok.seq.service.dto.SequenceStateDto;

/**
 * 시퀀스 데이터를 변경하여 테스트를 위해 생성한 컨트롤러 
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({ "/testApi" })
public class TestApiController {
	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 시퀀스 테스트 로직 처리를 위한 서비스 클래스
	 */
	@Autowired
	private GenSeqService seqService;

	/**
	 * 관리하고 있는 시퀀스 데이터의 날짜 데이터를 전날로 변경한다.
	 * <p>
	 * 시퀀스 날짜데이터 변경 서비스를 호출하여 데이터 변경 후 현재 시퀀스 데이터를 받아 리턴한다.
	 * 날짜가 변경되는 경우를 테스트하기 위해 작성하였다.
	 * 입출력 데이터 포맷은 Json
	 * <p>
	 *
	 * @param - void
	 * @return - CurrSeqOutDto
	 * 		Long sequence - 현재 저장되어 있는 시퀀스번호
	 * 		String date - 현재 저장되어 있는 날짜
	 */
	@RequestMapping("/setPreDate")
	public CurrSeqOutDto setPreDate() {

		CurrSeqOutDto out = new CurrSeqOutDto();

		try {
			seqService.changePreDate();
			SequenceStateDto serviceOut = seqService.getCurrVal();
			out.setSequence(serviceOut.getCurrentSequence());
			out.setDate(serviceOut.getDate());
		} catch (Exception e) {
			logger.error("#####오류내용: ", e);
			throw new RuntimeException("시퀀스 조회 오류");
		}

		return out;
	}

	/**
	 * 관리하고 있는 시퀀스 데이터의 시퀀스값을 최대값-1로 변경한다.
	 * <p>
	 * 시퀀스값 변경 서비스를 호출하여 데이터 변경 후 현재 시퀀스 데이터를 받아 리턴한다.
	 * 시퀀스 최대값 테스트를 위해 작성하였다.
	 * 입출력 데이터 포맷은 Json
	 * <p>
	 *
	 * @param - void
	 * @return - CurrSeqOutDto
	 * 		Long sequence - 현재 저장되어 있는 시퀀스번호
	 * 		String date - 현재 저장되어 있는 날짜
	 */
	@RequestMapping("/setMaxSeq")
	public CurrSeqOutDto setMaxSeq() {

		CurrSeqOutDto out = new CurrSeqOutDto();

		try {
			seqService.changeMaxSeq();
			SequenceStateDto serviceOut = seqService.getCurrVal();
			out.setSequence(serviceOut.getCurrentSequence());
			out.setDate(serviceOut.getDate());
		} catch (Exception e) {
			logger.error("#####오류내용: ", e);
			throw new RuntimeException("시퀀스 조회 오류");
		}

		return out;
	}

	/**
	 * 관리하고 있는 시퀀스 데이터의 시퀀스값과 날짜를 초기화하여 저장한다.
	 * <p>
	 * 시퀀스 초기화 후 테스트를 위해 작성하였다.
	 * 초기화 후 현재 데이터를 조회하여 리턴한다.
	 * 입출력 데이터 포맷은 Json
	 * <p>
	 *
	 * @param - void
	 * @return - CurrSeqOutDto
	 * 		Long sequence - 현재 저장되어 있는 시퀀스번호
	 * 		String date - 현재 저장되어 있는 날짜
	 */
	@RequestMapping("/setInit")
	public CurrSeqOutDto setInit() {
		
		CurrSeqOutDto out = new CurrSeqOutDto();

		try {
			seqService.setInit();
			SequenceStateDto serviceOut = seqService.getCurrVal();
			out.setSequence(serviceOut.getCurrentSequence());
			out.setDate(serviceOut.getDate());
		} catch (Exception e) {
			logger.error("#####오류내용: ", e);
			throw new RuntimeException("시퀀스 조회 오류");
		}
		return out;
	}

}
