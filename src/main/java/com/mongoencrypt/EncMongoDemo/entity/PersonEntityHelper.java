
package com.mongoencrypt.EncMongoDemo.entity;

import com.mongodb.client.model.vault.EncryptOptions;
import com.mongoencrypt.EncMongoDemo.keymgmt.KMSHandler;
import com.mongoencrypt.EncMongoDemo.repo.DcUsersRepo;
import org.bson.BsonBinary;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Uday Shankar
 */

@Component
public class PersonEntityHelper {

    @Autowired
    Environment environment;

    @Autowired
    DcUsersRepo dcUsersRepo;


    @Autowired
    protected KMSHandler kmsHandler;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";


    public EncryptedPerson getEncrypedPerson(Person p) throws IOException {


        EncryptedPerson ep = new EncryptedPerson(p.getFirstName(), p.getLastName());
        ep.setSsn(kmsHandler.getClientEncryption().encrypt(new BsonInt32(p.getSsn()), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE)));
        ep.setPhone(kmsHandler.getClientEncryption().encrypt(new BsonString(p.getPhone()), getEncryptOptions(RANDOM_ENCRYPTION_TYPE)));
        ep.setBloodType(kmsHandler.getClientEncryption().encrypt(new BsonString(p.getBloodType()), getEncryptOptions(RANDOM_ENCRYPTION_TYPE)));
        return ep;
    }

    public Person getPerson(EncryptedPerson ep, String uname) throws IOException {
        DcUsers dcuser = dcUsersRepo.findByUser(uname);
        Person p = new Person(ep.getFirstName(), ep.getLastName());
        if (dcuser.getUser().equalsIgnoreCase("admin@test.com") && dcuser.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            String[] dcperms = dcuser.getDecryptPermissions();
            for (String pe : dcperms) {
                if (pe.equalsIgnoreCase("ssn")) {
                    p.setSsn(kmsHandler.getClientEncryption().decrypt(ep.getSsn()).asNumber().intValue());
                } else if (pe.equalsIgnoreCase("phone")) {
                    p.setPhone(kmsHandler.getClientEncryption().decrypt(ep.getPhone()).asString().getValue());
                } else if (pe.equalsIgnoreCase("bloodType")) {
                    p.setBloodType(kmsHandler.getClientEncryption().decrypt(ep.getBloodType()).asString().getValue());
                }
            }

        } else if (dcuser.getUser().equalsIgnoreCase("user@test.com") && dcuser.getRole().equalsIgnoreCase("ROLE_USER")) {
            String[] dcperms = dcuser.getDecryptPermissions();
            for (String pe : dcperms) {
                if (pe.equalsIgnoreCase("ssn")) {
                    p.setSsn(kmsHandler.getClientEncryption().decrypt(ep.getSsn()).asNumber().intValue());
                } else if (pe.equalsIgnoreCase("phone")) {
                    p.setPhone(kmsHandler.getClientEncryption().decrypt(ep.getPhone()).asString().getValue());
                } else if (pe.equalsIgnoreCase("bloodType")) {
                    p.setBloodType(kmsHandler.getClientEncryption().decrypt(ep.getBloodType()).asString().getValue());
                } else {
                    if (p.getFirstName() == null) {
                        p.setFirstName("<secured>");
                    } else if (p.getLastName() == null) {
                        p.setLastName("<secured>");
                    } else if (p.getPhone() == null) {
                        p.setPhone("<secured>");
                    } else if (p.getBloodType() == null) {
                        p.setBloodType("<secured>");
                    }
                }
            }
        }
        return p;

    }



    public Person getPersonWithoutRbac(EncryptedPerson ep) throws IOException {

        Person p = new Person(ep.getFirstName(), ep.getLastName());
        p.setSsn(kmsHandler.getClientEncryption().decrypt(ep.getSsn()).asNumber().intValue());
        p.setPhone(kmsHandler.getClientEncryption().decrypt(ep.getPhone()).asString().getValue());
        p.setBloodType(kmsHandler.getClientEncryption().decrypt(ep.getBloodType()).asString().getValue());
        return p;

    }


    public BsonBinary getEncryptedSsn(int ssn) throws IOException {
        return kmsHandler.getClientEncryption().encrypt(new BsonInt32(ssn), getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE));
    }

    private EncryptOptions getEncryptOptions(String algorithm) {
        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;

    }

}
