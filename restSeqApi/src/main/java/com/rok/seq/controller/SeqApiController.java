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

@RestController
@RequestMapping({ "/seqApi" })
public class SeqApiController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GenGuidService guidService;

	@RequestMapping({ "/getGuid" })
	public GuidOutDto getGuid(@RequestBody GuidInDto in) {
		logger.info("TEST!!! : {}", in);
		GuidOutDto out = new GuidOutDto();
		out.setGuid(this.guidService.generateGuid(in));
		return out;
	}
}
