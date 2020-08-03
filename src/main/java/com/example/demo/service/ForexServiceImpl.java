package com.example.demo.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Model.ForexValue;

@Service
public class ForexServiceImpl implements ForexService {

	@Autowired
	RestTemplate restTemplate;

	@Override
	public double getValue(String fromType, String toType, double value) {
		String jsonString = restTemplate.getForObject("https://api.exchangeratesapi.io/latest?base=USD", String.class);
		JSONObject json = new JSONObject(jsonString);
		JSONObject rates = json.getJSONObject("rates");
		double result=0d;
		if (rates.has(fromType) && rates.has(fromType)) {
			result= value * rates.getDouble(toType) / rates.getDouble(fromType);
		}
		System.out.print(result);
		return result;
	}

}
