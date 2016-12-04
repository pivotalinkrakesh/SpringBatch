package com.medecision.batch.writer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;


public class BeanSqlParameterMapper extends BeanPropertyItemSqlParameterSourceProvider {
	private static final Logger log = LoggerFactory.getLogger(BeanSqlParameterMapper.class);
	@Override
    public SqlParameterSource createSqlParameterSource(Object item) {
		log.info("##################This is sql param mapper");
		HashMap<String, Object> castedItem = (LinkedHashMap<String, Object>)item;
		
		String json  = null;
		try {
			json = new ObjectMapper().writeValueAsString(castedItem);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("## rakesh castedItem size" + castedItem.size());
		
		castedItem.forEach((key, value) -> log.info("Key in sql mapper: "+key.toString() + " : Value in mapper: " + value.toString()));
        return new MapSqlParameterSource()
                //.addValue("id", castedItem.get("_id"))
                .addValue("json_doc", castedItem.get(json));               
    }

}
