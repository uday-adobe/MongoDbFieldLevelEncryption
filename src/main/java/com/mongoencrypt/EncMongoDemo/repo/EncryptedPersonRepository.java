package com.mongoencrypt.EncMongoDemo.repo;

import com.mongoencrypt.EncMongoDemo.entity.EncryptedPerson;
import org.bson.BsonBinary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Uday Shankar
 */
@Repository
public interface EncryptedPersonRepository extends MongoRepository<EncryptedPerson, String> {
	public EncryptedPerson findByFirstName(String firstName);
	public EncryptedPerson findBySsn(BsonBinary ssn);
}
