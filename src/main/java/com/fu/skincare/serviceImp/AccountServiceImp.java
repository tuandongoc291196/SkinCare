package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.request.account.EditProfileRequest;
import com.fu.skincare.request.account.FilterAccountRequest;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public AccountResponse getById(int id) {

        Account account = accountRepository.findById(id).orElseThrow(
                () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

        return modelMapper.map(account, AccountResponse.class);
    }

    @Override
    public List<AccountResponse> getAllAccount() {

        List<Account> listAccounts = accountRepository.findAll();

        if (listAccounts.isEmpty()) {
            throw new EmptyException(AccountErrorMessage.EMPTY_ACCOUNT);
        }

        List<AccountResponse> responses = new ArrayList<>();
        for (Account account : listAccounts) {
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            responses.add(accountResponse);
        }
        return responses;
    }

    @Override
    public List<AccountResponse> filterAccount(FilterAccountRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filterAccount'");
    }

    @Override
    public AccountResponse editProfile(EditProfileRequest request) {

        Account account = accountRepository.findById(request.getId()).orElseThrow(
                () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

        account.setAddress(request.getAddress());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setName(request.getName());

        accountRepository.save(account);
        return modelMapper.map(account, AccountResponse.class);
    }

    @Override
    public AccountResponse updateStatus(int id, String status) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

        account.setStatus(status);
        accountRepository.save(account);
        return modelMapper.map(account, AccountResponse.class);
    }

}
