package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.ForexValue;

@Repository
public interface ForexValueRepository extends MongoRepository<ForexValue, String> {
	public ForexValue findByFromTypeAndToTypeAndDate(String fromType, String toType, String date);

}
