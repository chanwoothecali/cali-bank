package com.cali.calibank.account.service;

import com.cali.calibank.account.controller.AccountDto;
import com.cali.calibank.account.controller.AccountDto.CreateResponse;
import com.cali.calibank.account.entity.Account;
import com.cali.calibank.account.repository.AccountRepository;
import com.cali.calibank.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountDto.CreateResponse createAccount(AccountDto.CreateRequest request) {
        // TODO: 시큐리티에서 user 받아와야 함
        User user = User.builder().build();
        Account savedAccount = accountRepository.save(request.toEntity(user));
        user.addAccount(savedAccount);
        return CreateResponse.fromEntity(savedAccount);
    }
}
