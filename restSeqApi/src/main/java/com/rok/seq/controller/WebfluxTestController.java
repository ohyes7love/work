package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rok.seq.controller.dto.GuidInDto;
import com.rok.seq.controller.dto.GuidOutDto;
import com.rok.seq.controller.dto.SeqOutDto;
import com.rok.seq.service.GenGuidService;
import com.rok.seq.service.GenSeqService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({ "/webfluxTest" })
public class WebfluxTestController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GenSeqService seqService;
	@Autowired
	private GenGuidService guidService;

	@GetMapping("/hello")
	public Mono<String> hello() {
		logger.info("Hello, World!!!!!!!!!!!!!!!");
		return Mono.just("Hello, World!");
	}

	@GetMapping(value = "/getSeq")
	public Mono<SeqOutDto> getSequence() {
		SeqOutDto out = new SeqOutDto();

		try {
			out.setSequence(seqService.next());
		} catch (Exception e) {
			logger.error("#####오류내용: ", e);
			throw new RuntimeException("시퀀스 생성 오류");
		}

		return Mono.just(out);
	}

	@PostMapping(value = "/getGuid", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<GuidOutDto> getGuid(@Valid @ModelAttribute GuidInDto inDto) {
	    return Mono.just(inDto)
	            .map(guidService::generateGuid)
	            .filter(guid -> guid.getBytes().length == 30)
	            .switchIfEmpty(Mono.error(new RuntimeException("GUID 생성오류(길이)")))
	            .doOnNext(guid -> logger.info("GUID: {}", guid))
	            .map(guid -> {
	                GuidOutDto outDto = new GuidOutDto();
	                outDto.setGuid(guid);
	                return outDto;
	            });
	}
}
