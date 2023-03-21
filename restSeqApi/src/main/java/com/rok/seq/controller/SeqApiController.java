package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rok.seq.controller.dto.GuidInDto;
import com.rok.seq.controller.dto.GuidOutDto;
import com.rok.seq.controller.dto.SeqInDto;
import com.rok.seq.controller.dto.SeqOutDto;
import com.rok.seq.service.GenGuidService;
import com.rok.seq.service.GenSeqService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/seqApi" })
public class SeqApiController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GenGuidService guidService;
	@Autowired
	private GenSeqService seqService;

	@PostMapping("/getGuid")
	public GuidOutDto getGuid(@Valid @RequestBody GuidInDto in) {

		logger.info("Input Data : {}", in);

		String guid = guidService.generateGuid(in);

		logger.info("guidLength: {}", guid.getBytes().length);

		if (guid.getBytes().length != 30) {
			throw new RuntimeException("GUID 생성오류(길이)");
		}

		GuidOutDto out = new GuidOutDto();
		out.setGuid(guid);

		return out;
	}

	@PostMapping("/getSeq")
	public SeqOutDto getSeq(@RequestBody SeqInDto in) {

		SeqOutDto out = new SeqOutDto();

		try {
			logger.info("currentSeq#1: {}", seqService.current());
			out.setSequence(seqService.next(in.getIsDayTest(), in.getIsMaxSeqTest()));
			logger.info("currentSeq#2: {}", seqService.current());
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException("시퀀스 생성 오류");
		}

		return out;
	}
}
