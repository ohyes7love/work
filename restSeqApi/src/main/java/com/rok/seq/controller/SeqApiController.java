package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rok.seq.controller.dto.GuidInDto;
import com.rok.seq.controller.dto.GuidOutDto;
import com.rok.seq.service.GenGuidService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/seqApi" })
public class SeqApiController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GenGuidService guidService;

	@RequestMapping({ "/getGuid" })
	public GuidOutDto getGuid(@Valid @RequestBody GuidInDto in) {
		logger.info("Input Data : {}", in);
		
		String guid = guidService.generateGuid(in) ;
		
		logger.info("guidLength: {}", guid.getBytes().length) ;
		
		if(guid.getBytes().length != 30) {
			throw new RuntimeException("GUID 생성오류(길이)") ;
		}
		
		GuidOutDto out = new GuidOutDto();
		out.setGuid(guid);
		
		return out;
	}
}
