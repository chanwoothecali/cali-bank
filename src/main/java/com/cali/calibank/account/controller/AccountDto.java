package com.cali.calibank.account.controller;

import com.cali.calibank.account.entity.Account;
import com.cali.calibank.user.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRequest {

        @NotBlank(message = "은행명을 입력해주세요.")
        private String bank;

        @NotBlank(message = "계좌번호를 입력해주세요.")
        @Size(min = 10, max = 18, message = "올바른 계좌번호를 입력해주세요.")
        private String accountNumber;

        @PositiveOrZero(message = "계좌 금액은 음수가 될 수 없습니다.")
        private BigDecimal balance;

        @Builder
        public CreateRequest(String bank, String accountNumber, BigDecimal balance) {
            this.bank = bank;
            this.accountNumber = accountNumber;
            this.balance = balance;
        }

        public Account toEntity(User user) {
            return Account.builder()
                .bank(bank)
                .accountNumber(accountNumber)
                .balance(balance)
                .user(user)
                .build();
        }
    }

    @Getter
    public static class CreateResponse {

        private String bank;

        private String accountNumber;

        private BigDecimal balance;

        @Builder
        public CreateResponse(String bank, String accountNumber, BigDecimal balance) {
            this.bank = bank;
            this.accountNumber = accountNumber;
            this.balance = balance;
        }

        public static CreateResponse fromEntity(Account account) {
            return CreateResponse.builder()
                .bank(account.getBank())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
        }
    }
}
