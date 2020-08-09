package com.example.demo;

import java.util.Iterator;

import javax.naming.ServiceUnavailableException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Exceptions.CustomErrorHandler;
import com.example.demo.Exceptions.MalFormedObjectException;
import com.example.demo.Model.ForexValue;
import com.example.demo.Repository.ForexValueRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableMongoRepositories(basePackages = "com.example.demo.repository")
@SpringBootApplication(scanBasePackages = { "com.example.demo" })
@EnableSwagger2
public class DemoApplication implements CommandLineRunner {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ForexValueRepository forexValueRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	private ResponseErrorHandler repsonseErrorHandler = new CustomErrorHandler();

	@Override
	public void run(String... args) throws Exception {
		restTemplate.setErrorHandler(repsonseErrorHandler);
		ResponseEntity<String> jsonString = restTemplate.exchange("https://api.exchangeratesapi.io/latest?base=USD",
				HttpMethod.GET, null, String.class);
		if (jsonString.getStatusCode().is2xxSuccessful()) {
			JSONObject json = new JSONObject(jsonString.getBody());
			if (json.has("rates")) {
				JSONObject rates = json.getJSONObject("rates");
				Iterator<String> keys = rates.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					ForexValue value = new ForexValue();
					value.setBase("USD");
					value.setFromType("USD");
					value.setToType(key);
					value.setDate(json.getString("date"));
					value.setValue(rates.getDouble(key));
					forexValueRepository.save(value);
				}
			} else
				throw new MalFormedObjectException("Unable to fetch converison values from third party api");
		}
		if (jsonString.getStatusCode().is4xxClientError() || jsonString.getStatusCode().is5xxServerError()) {
			throw new ServiceUnavailableException("Unable to Start application due to third party api");
		}
	}
}
