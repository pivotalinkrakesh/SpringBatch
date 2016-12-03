/**
 * 
 */
package com.medecision.batch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.JsonLineMapper;

/**
 * @author cho922
 *
 */
public class JsonFlatFileItemReader<T> extends FlatFileItemReader<T> {
	public void setLineMapper(JsonLineMapper jsonLineMapper) {
		super.setLineMapper((LineMapper<T>) jsonLineMapper);	
	}

}
