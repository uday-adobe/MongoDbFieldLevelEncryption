package com.mongoencrypt.EncMongoDemo.repo;

import com.mongoencrypt.EncMongoDemo.entity.DcUsers;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Uday Shankar
 */
public interface DcUsersRepo extends MongoRepository<DcUsers,String> {

    DcUsers findByUser(String user);
}
