package com.medecision.batch.writer;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;


public class BeanSqlParameterMapper extends BeanPropertyItemSqlParameterSourceProvider {
	private static final Logger log = LoggerFactory.getLogger(BeanSqlParameterMapper.class);
	@Override
    public SqlParameterSource createSqlParameterSource(Object item) {
		
		HashMap<String, Object> castedItem = (LinkedHashMap<String, Object>)item;
		
		log.info("## rakesh castedItem size" + castedItem.size());
		
		castedItem.forEach((key, value) -> log.info("Key in sql mapper: "+key.toString() + " : Value in mapper: " + value.toString()));
        return new MapSqlParameterSource()
                .addValue("claim_id", castedItem.get("_id"))
                .addValue("claim_data", castedItem.get("admissionDate"));               
    }

}
