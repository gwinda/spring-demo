package com.example.demo.core.service;

public interface CustomerService {
  void createCustomer(String username, String password);

  boolean login(String username, String password);

  void changePassword(String username, String password);

  void deleteUser(String username);
}
