package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rok.seq.controller.dto.CurrSeqOutDto;
import com.rok.seq.service.GenSeqService;
import com.rok.seq.service.dto.SequenceStateDto;

/**
 * @author root
 *
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({ "/testApi" })
public class TestApiController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GenSeqService seqService;

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
