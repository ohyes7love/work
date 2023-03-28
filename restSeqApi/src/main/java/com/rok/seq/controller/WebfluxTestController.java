package com.rok.seq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping("/getGuid")
    public Mono<GuidOutDto> getGuid(@Valid @RequestBody Mono<GuidInDto> in) {
        return in.map(guidService::generateGuid)
                .doOnNext(guid -> {
                    if (guid.getBytes().length != 30) {
                        throw new RuntimeException("GUID 생성오류(길이)");
                    }
                    logger.info("GUID: {}", guid);
                })
                .map(guid -> {
                    GuidOutDto out = new GuidOutDto();
                    out.setGuid(guid);
                    return out;
                });
    }

}
