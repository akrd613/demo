package com.example.demo.service;

import javax.naming.ServiceUnavailableException;

import com.example.demo.Exceptions.KeyNotSupportedException;

public interface ForexService {
	public double getValue(String fromType, String toType, double value) throws ServiceUnavailableException, KeyNotSupportedException;
}
