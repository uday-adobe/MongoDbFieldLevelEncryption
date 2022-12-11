package com.mongoencrypt.EncMongoDemo.repo;

import com.mongoencrypt.EncMongoDemo.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * @author Uday Shankar
 */
public interface PersonRepo extends MongoRepository<Person,String> {
}
