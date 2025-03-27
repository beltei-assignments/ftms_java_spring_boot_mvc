package com.example.ftms_java_spring_boot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.BusinessRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class BusinessService {
  @Autowired
  private BusinessRepository businessRepository;

  public Page<Business> getAllWithPagination(
      Pageable pageable,
      User user) {
    Specification<Business> filters = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Filter by User
      predicates.add(criteriaBuilder.equal(root.get("user"), user));
      predicates.add(criteriaBuilder.equal(root.get("disabled"), false));

      // Filter transaction type
      // if (!type.isEmpty()) {
      // predicates.add(
      // criteriaBuilder.equal(root.get("transactionType"), type.get()));
      // }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    return businessRepository.findAll(filters, pageable);
  }

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
