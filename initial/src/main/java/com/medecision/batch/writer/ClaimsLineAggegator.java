package com.medecision.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;

import com.medecision.batch.processor.ClaimProcessor;

public class ClaimsLineAggegator<String> extends PassThroughLineAggregator<String> {
	private static final Logger log = LoggerFactory.getLogger(ClaimProcessor.class);
	public java.lang.String aggregate(String item){
		log.info(" #################   Line Aggregator object" + item.toString()  );	
		return item.toString();
	}
}
