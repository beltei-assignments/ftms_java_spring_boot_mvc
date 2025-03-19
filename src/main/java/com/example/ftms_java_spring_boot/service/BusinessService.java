package com.example.ftms_java_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.BusinessRepository;

@Service
public class BusinessService {
  @Autowired
  private BusinessRepository businessRepository;

  public List<Business> getAll(User user) {
    return businessRepository.findAllBusinesses(user);
  }

  public Optional<Business> getById(Long id) {
    return businessRepository.findById(id);
  }

  public boolean create(Business business) {
    businessRepository.save(business);

    return true;
  }

  public void update(Business business) {
    businessRepository.save(business);
  }

  public void deleteById(Long id) {
    Business business = getById(id).get();
    business.setDisabled(true);
    update(business);
  }
}
