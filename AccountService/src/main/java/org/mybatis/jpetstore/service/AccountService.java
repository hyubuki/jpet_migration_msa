/*
 *    Copyright 2010-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.service;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 계정 관련 비즈니스 로직을 담당합니다.
 */
@Service
public class AccountService {

  private final AccountRepository accountRepository;

  @Autowired
  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  /**
   * 사용자 이름으로 계정 정보를 조회합니다.
   *
   * @param username 사용자 이름
   * @return 계정 정보
   */
  public Account getAccount(String username) {
    return accountRepository.findByUsername(username);
  }

  /**
   * 사용자 이름과 비밀번호로 계정 정보를 조회합니다.
   *
   * @param username 사용자 이름
   * @param password 비밀번호
   * @return 계정 정보
   */
  public Account getAccount(String username, String password) {
    return accountRepository.findByUsernameAndPassword(username, password);
  }

  /**
   * 새로운 계정을 등록합니다.
   *
   * @param account 등록할 계정
   */
  @Transactional
  public void insertAccount(Account account) {
    accountRepository.insert(account);
  }

  /**
   * 계정 정보를 수정합니다.
   *
   * @param account 수정할 계정
   */
  @Transactional
  public void updateAccount(Account account) {
    accountRepository.update(account);
  }

}
