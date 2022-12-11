package com.mongoencrypt.EncMongoDemo.controller;

import com.mongoencrypt.EncMongoDemo.entity.EncryptedPerson;
import com.mongoencrypt.EncMongoDemo.entity.Person;
import com.mongoencrypt.EncMongoDemo.entity.PersonEntityHelper;
import com.mongoencrypt.EncMongoDemo.handler.PersonHandler;
import com.mongoencrypt.EncMongoDemo.repo.EncryptedPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Uday Shankar
 */
@RestController
public class TestController {

    @Autowired
    private PersonHandler personHandler;

    @Autowired
    private PersonEntityHelper personEntityHelper;


    @Autowired
    private EncryptedPersonRepository encryptedPersonRepository;

    @GetMapping("/inlineMigration")
    public String testApp() throws IOException {
        personHandler.runApplication();
        return "Inline Migration Completed Succesfully";
    }

    @GetMapping("/getMigratedFieldsDecrypted")
    public List<Person> getListOfDecryptedFields(@RequestParam("user") String user) throws IOException {
        List<Person> list = new ArrayList<>();
        for (EncryptedPerson ep : encryptedPersonRepository.findAll()) {
            Person person = personEntityHelper.getPerson(ep, user);
            list.add(person);
        }
        return list;


//        return encryptedPersonRepository.findAll()
//                .stream().map(ep -> personEntityHelper.getPerson(ep,user))
//                .collect(Collectors.toList());

    }

    @GetMapping(value = "/getMigratedFieldsDecryptedWithoutRbac")
    public List<Person> getListOfDecryptedFieldsAll() throws IOException {
        List<Person> list = new ArrayList<>();
        for (EncryptedPerson ep : encryptedPersonRepository.findAll()) {
            Person person = personEntityHelper.getPersonWithoutRbac(ep);
            list.add(person);
        }
        return list;


//        return encryptedPersonRepository.findAll()
//                .stream().map(ep -> personEntityHelper.getPerson(ep,user))
//                .collect(Collectors.toList());

    }

    @GetMapping("/getEnc")
    public List<EncryptedPerson> getEncPersons() {
        return encryptedPersonRepository.findAll();
    }
}
