package com.example.demo.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Repository.ForexValueRepository;

@RunWith(MockitoJUnitRunner.class)
public class TestForexServiceImpl {

	@InjectMocks
	ForexService forexService;

	@Mock
	ForexValueRepository forexValueRepository;

	@Mock
	RestTemplate restTemplate;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getCurrencyValues() {
		
		/*
		 * String jsonString =
		 * restTemplate.getForObject("https://api.exchangeratesapi.io/latest?base=USD",
		 * String.class); JSONObject json = new JSONObject(jsonString); JSONObject rates
		 * = json.getJSONObject("rates");
		 */
		String fromType="USD";
		String toType="INR";
		double value=1;
		double result=0d;
		result=value*(forexValueRepository.findByFromTypeAndToTypeAndDate(fromType, toType,java.time.LocalDate.now().toString()).getValue());
		double expected=0d;
		try {
			expected = forexService.getValue(fromType, toType, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(result, expected);
	}
}
