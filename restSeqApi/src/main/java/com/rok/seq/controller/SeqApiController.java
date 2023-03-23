package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rok.seq.controller.dto.CurrSeqOutDto;
import com.rok.seq.controller.dto.GuidInDto;
import com.rok.seq.controller.dto.GuidOutDto;
import com.rok.seq.controller.dto.SeqOutDto;
import com.rok.seq.service.GenGuidService;
import com.rok.seq.service.GenSeqService;
import com.rok.seq.service.dto.SequenceStateDto;

import jakarta.validation.Valid;

/**
 * 시퀀스 채번과 GUID 채번을 위한 컨트롤러 클래스
 * 
 * @author     ohyes7love@naver.com
 * @version    1.0.0
 * @since      1.0.0
 */
@RestController
@CrossOrigin(origins="*", allowedHeaders = "*")
@RequestMapping({ "/seqApi" })
public class SeqApiController {
	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * guid채번 로직 처리를 위한 서비스 클래스
	 */
	@Autowired
	private GenGuidService guidService;
	/**
	 * 시퀀스채번 로직 처리를 위한 서비스 클래스
	 */
	@Autowired
	private GenSeqService seqService;

	/**
	 * 요청 데이터를 받아 guid를 생성하여 리턴한다.
	 * GUID생성룰: yyyyMMddHHmmssSSS(17) + 시스템코드(3) + 노드번호(2) + 인스턴스번호(2) + 랜덤String(6) = 30자리
	 * <p>
	 * GUID생성요청시스템의 시스템코드(3자리), 노드번호(2자리숫자), 인스턴스번호(2자리숫자)를 입력받아
	 * guid 생성 서비스를 호출한 후 생성된 guid를 리턴한다.
	 * 입출력 데이터 포맷은 Json이며 입력 데이터에대한 null여부, 길이 Validation 체크를 수행한다.
	 * <p>
	 *
	 * @param - GuidInDto
	 *		String sendChlCd - GUID생성요청시스템의 채널코드(3자리)
	 *		Integer sendSysNodeNo - GUID생성요청시스템의 노드번호(2자리숫자)
	 *		Integer sendSysInstNo - GUID생성요청시스템의 인스턴스번호(2자리숫자)
	 * @return - GuidOutDto
	 * 		String guid - 생성된 GUID(30자리)
	 */
	@RequestMapping("/getGuid")
	public GuidOutDto getGuid(@Valid @RequestBody GuidInDto in) {

		String guid = guidService.generateGuid(in);

		if (guid.getBytes().length != 30) {
			throw new RuntimeException("GUID 생성오류(길이)");
		}
		
		logger.info("GUID: {}", guid) ;

		GuidOutDto out = new GuidOutDto();
		out.setGuid(guid);

		return out;
	}

	/**
	 * 요청을 받아 시퀀스 번호를 채번하여 리턴한다.
	 * <p>
	 * 시퀀스 채번 서비스를 호출한 후 채번된 시퀀스 번호를 리턴한다.
	 * 입출력 데이터 포맷은 Json이다.
	 * <p>
	 *
	 * @param - void
	 * @return - SeqOutDto
	 * 		Long sequence - 생성된 시퀀스번호
	 */
	@GetMapping("/getSeq")
	public SeqOutDto getSeq() {

		SeqOutDto out = new SeqOutDto();

		try {
			out.setSequence(seqService.next());
		} catch (Exception e) {
			logger.error("#####오류내용: ", e);
			throw new RuntimeException("시퀀스 생성 오류");
		}

		return out;
	}
	
	/**
	 * 요청을 받아 현재 시퀀스 번호와 날짜를 리턴한다.
	 * <p>
	 * 현재 시퀀스 조회 서비스를 호출하여 생성된 응답을 리턴한다.
	 * 입출력 데이터 포맷은 Json이다.
	 * <p>
	 *
	 * @param - void
	 * @return - CurrSeqOutDto
	 * 		Long sequence - 현재 저장되어 있는 시퀀스번호
	 * 		String date - 현재 저장되어 있는 날짜
	 */
	@GetMapping("/getCurrentSeq")
	public CurrSeqOutDto getCurrentSeq() {

		CurrSeqOutDto out = new CurrSeqOutDto();

		try {
			SequenceStateDto serviceOut = seqService.getCurrVal() ;
			out.setSequence(serviceOut.getCurrentSequence());
			out.setDate(serviceOut.getDate()) ;
		} catch (Exception e) {
			logger.error("#####오류내용: ", e);
			throw new RuntimeException("시퀀스 조회 오류");
		}

		return out;
	}
}
