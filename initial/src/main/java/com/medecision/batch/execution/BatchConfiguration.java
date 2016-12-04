package com.medecision.batch.execution;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.JsonLineMapper;
import org.springframework.batch.item.file.separator.JsonRecordSeparatorPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.medecision.batch.processor.ClaimJsonProcessor;
import com.medecision.batch.reader.JsonFlatFileItemReader;
import com.medecision.batch.writer.BeanSqlParameterMapper;
import com.medecision.batch.writer.ClaimsDatabaseWriter;
import com.medecision.batch.writer.ClaimsLineAggegator;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	private static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    //@Autowired
    //public DataSource dataSource;
    public DataSource dataSource() {
		// no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
 /*   	EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.HSQL) //.H2 or .DERBY
			.addScript("schema-all.sql")
			//.addScript("db/sql/insert-data.sql")
			.build(); */
		//return db;
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	    dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    	    dataSource.setUrl("jdbc:oracle:thin:@50.21.182.140:1521:ORCL");
    	    dataSource.setUsername("system");
    	    dataSource.setPassword("oracle");
    	
    	  return dataSource; 
	}
    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<String> reader() {
        JsonFlatFileItemReader<String> reader = new JsonFlatFileItemReader<String>();
        //reader.setResource(new FileSystemResource("c:\\temp\\data\\instClaimData_2563861094248939323.json"));
        reader.setResource(new ClassPathResource("med_data_small.txt"));
        
        //new FileSystemResource("/Users/neerajtiwari/Documents/Technical/JavaWorkspace/SpringBatch/initial/src/main/resources/spring-processed-med-data-small.txt"));
        reader.setRecordSeparatorPolicy(new JsonRecordSeparatorPolicy());
        //reader.setLineMapper(new DefaultLineMapper());
        try {
			log.info("reader: ==== "+ reader.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        reader.setLineMapper(new JsonLineMapper());
        /*{{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName" });
            }
        	});
		/*            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});*/
       // }});
        return reader;
    }

    @Bean
    public ItemProcessor<Object, Object>  processor() {  	
        return new ClaimJsonProcessor();
    }

    @Bean
    public ItemWriter<? super Object> writer() {
    	FlatFileItemWriter<Object> writer = new FlatFileItemWriter<Object>();
    	//writer.setResource(new ClassPathResource("spring-processed-med-data-small.txt"));
    	writer.setResource(new FileSystemResource("/Users/neerajtiwari/Documents/Technical/JavaWorkspace/SpringBatch/initial/src/main/resources/spring-processed-med-data-small.txt"));  	
    	ClaimsLineAggegator<Object>  passAgg = new ClaimsLineAggegator<Object>();
    	writer.setLineAggregator(passAgg);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public ItemWriter<? super Object> databaseWriter() {
    	ClaimsDatabaseWriter<Object> writer = new ClaimsDatabaseWriter<Object>();
    	BeanSqlParameterMapper beanSqlParameterMapper = new BeanSqlParameterMapper(); 
    	writer.setItemSqlParameterSourceProvider(beanSqlParameterMapper);
    	writer.setSql("insert into JSON_CLAIM (json_doc) "+
		              "values (:json_doc)");
    	writer.setDataSource(dataSource());  	
        return writer;
    }
    /*    
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }
    */

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
        		.<Object, Object> chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(databaseWriter())
                .build();
        //ItemProcessor<String, String> 
    }
    // end::jobstep[]
}
