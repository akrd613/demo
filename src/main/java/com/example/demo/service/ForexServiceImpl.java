package com.example.demo.service;

import java.math.BigDecimal;

import javax.naming.ServiceUnavailableException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Exceptions.CustomErrorHandler;
import com.example.demo.Exceptions.KeyNotSupportedException;

@Service
public class ForexServiceImpl implements ForexService {

	@Autowired
	RestTemplate restTemplate;

	private ResponseErrorHandler responseErrorHandler = new CustomErrorHandler();

	@SuppressWarnings("deprecation")
	@Override
	public double getValue(String fromType, String toType, double value)
			throws ServiceUnavailableException, KeyNotSupportedException {
		restTemplate.setErrorHandler(responseErrorHandler);
		ResponseEntity<String> responseEntiry = restTemplate.exchange("https://api.exchangeratesapi.io/latest?base=USD",
				HttpMethod.GET, null, String.class);
		if (responseEntiry.getStatusCode().is2xxSuccessful()) {
			JSONObject json = new JSONObject(responseEntiry.getBody());
			JSONObject rates = json.getJSONObject("rates");
			double result = 0d;
			if (rates.has(fromType) && rates.has(toType)) {
				result = value * rates.getDouble(toType) / rates.getDouble(fromType);
			} else
				throw new KeyNotSupportedException("Currency values are not supported");
			result= new BigDecimal(result).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			return result;
		} else {
			throw new ServiceUnavailableException("Unable to process request,please try later");
		}
	}

}
