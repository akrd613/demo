package com.example.demo.Controller;

import javax.naming.ServiceUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Exceptions.KeyNotSupportedException;
import com.example.demo.service.ForexService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/forex")
public class ForexController {

	@Autowired
	ForexService forexService;

	@GetMapping(value = "/getForexInfo/{fromType}/{toType}/{value:.+}")
	@ApiOperation(value = "get local forex values")
	public double getForexValues(@PathVariable String fromType, @PathVariable String toType, @PathVariable double value)
			throws ServiceUnavailableException,KeyNotSupportedException {
		return forexService.getValue(fromType, toType, value);

	}
}
