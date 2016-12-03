package com.medecision.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ClaimProcessor implements ItemProcessor<String, String> {

	private static final Logger log = LoggerFactory.getLogger(ClaimProcessor.class);
	
	@Override
	public String process(String item) throws Exception {
		

		log.info(" #################   Json object" + item  );	
		
		
		return item.toUpperCase();
	}

}
