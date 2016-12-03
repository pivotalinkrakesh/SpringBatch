package com.medecision.batch.processor;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ClaimJsonProcessor implements ItemProcessor<Object, Object>{
	private static final Logger log = LoggerFactory.getLogger(ClaimProcessor.class);
	
	@Override
	public Object process(Object item) throws Exception {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> castedItem = (LinkedHashMap<String, Object>)item;
		castedItem.forEach((key, value) -> log.info("Key: "+key.toString() + " : Value: " + value.toString()));
		return item;
	}
}
