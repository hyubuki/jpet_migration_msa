package org.mybatis.jpetstore.repository;

import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * {@link Account} 엔티티에 대한 데이터 접근을 담당합니다.
 */
@Repository
public class AccountRepository {

    private final AccountMapper mapper;

    @Autowired
    public AccountRepository(AccountMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 사용자 이름으로 계정을 조회합니다.
     *
     * @param username 사용자 이름
     * @return 계정 정보
     */
    public Account findByUsername(String username) {
        return mapper.getAccountByUsername(username);
    }

    /**
     * 사용자 이름과 비밀번호로 계정을 조회합니다.
     *
     * @param username 사용자 이름
     * @param password 비밀번호
     * @return 계정 정보
     */
    public Account findByUsernameAndPassword(String username, String password) {
        return mapper.getAccountByUsernameAndPassword(username, password);
    }

    /**
     * 계정 정보를 저장합니다.
     *
     * @param account 저장할 계정
     */
    public void insert(Account account) {
        mapper.insertAccount(account);
        mapper.insertProfile(account);
        mapper.insertSignon(account);
    }

    /**
     * 계정 정보를 업데이트합니다.
     *
     * @param account 수정할 계정
     */
    public void update(Account account) {
        mapper.updateAccount(account);
        mapper.updateProfile(account);
        Optional.ofNullable(account.getPassword())
                .filter(p -> p.length() > 0)
                .ifPresent(p -> mapper.updateSignon(account));
    }
}
