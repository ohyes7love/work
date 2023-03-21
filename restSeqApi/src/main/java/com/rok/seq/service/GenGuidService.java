package com.rok.seq.service;

import org.springframework.stereotype.Service;

import com.rok.seq.controller.dto.GuidInDto;

@Service
public class GenGuidService {
  public String generateGuid(GuidInDto in) {
    return "guid!!";
  }
}
