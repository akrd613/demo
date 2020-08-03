package com.example.demo;

import java.util.Iterator;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Model.ForexValue;
import com.example.demo.Repository.ForexValueRepository;

@EnableMongoRepositories(basePackages = "com.example.demo.repository")
@SpringBootApplication(scanBasePackages = { "com.example.demo" })
public class DemoApplication implements CommandLineRunner {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ForexValueRepository forexValueRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Override
	public void run(String... args) throws Exception {
		String jsonString = restTemplate.getForObject("https://api.exchangeratesapi.io/latest?base=USD", String.class);
		JSONObject json = new JSONObject(jsonString);
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

	}

}
