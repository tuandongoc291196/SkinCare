package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.constants.message.answer.AnswerErrorMessage;
import com.fu.skincare.constants.message.question.QuestionErrorMessage;
import com.fu.skincare.constants.message.userTestResult.UserTestResultErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Answer;
import com.fu.skincare.entity.Question;
import com.fu.skincare.entity.UserTest;
import com.fu.skincare.entity.UserTestResult;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.AnswerRepository;
import com.fu.skincare.repository.QuestionRepository;
import com.fu.skincare.repository.UserTestRepository;
import com.fu.skincare.repository.UserTestResultRepository;
import com.fu.skincare.request.userTest.CreateUserTestRequest;
import com.fu.skincare.request.userTestResult.CreateUserTestResultRequest;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.userTest.UserTestResponse;
import com.fu.skincare.response.userTestResult.UserTestResultResponse;
import com.fu.skincare.service.UserTestResultService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTestResultServiceImp implements UserTestResultService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final UserTestRepository userTestRepository;
    private final UserTestResultRepository userTestResultRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public UserTestResultResponse createUserTestResult(CreateUserTestResultRequest request) {

        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(
                () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));
        int testTime = userTestResultRepository.countByAccount(account);
        UserTestResult userTestResult = UserTestResult.builder()
                .account(account)
                .testTime(testTime + 1)
                .createdAt(Utils.formatVNDatetimeNow())
                .status(Status.ACTIVATED)
                .build();

        UserTestResult userTestResultSaved = userTestResultRepository.save(userTestResult);
        List<UserTestResponse> userTestResponses = new ArrayList<>();
        int totalPoint = 0;
        for (CreateUserTestRequest createUserTestRequest : request.getUserTests()) {

            Answer answer = answerRepository.findById(createUserTestRequest.getAnswerId()).orElseThrow(
                    () -> new ErrorException(AnswerErrorMessage.ANSWER_NOT_FOUND));

            Question question = questionRepository.findById(createUserTestRequest.getAnswerId()).orElseThrow(
                    () -> new ErrorException(QuestionErrorMessage.QUESTION_NOT_FOUND));
            UserTest userTest = UserTest.builder()
                    .answer(answer)
                    .question(question)
                    .createdAt(Utils.formatVNDatetimeNow())
                    .status(Status.ACTIVATED)
                    .userTestResult(userTestResultSaved)
                    .build();

            UserTest userTestSaved = userTestRepository.save(userTest);
            UserTestResponse userTestResponse = modelMapper.map(userTestSaved, UserTestResponse.class);
            userTestResponses.add(userTestResponse);
            totalPoint += answer.getPoint();
        }

        userTestResultSaved.setTotalPoint(totalPoint);

        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
        accountResponse.setRoleName(account.getRole().getName());
        UserTestResultResponse response = modelMapper.map(userTestResultSaved, UserTestResultResponse.class);
        response.setUser(accountResponse);
        response.setUserTestResponse(userTestResponses);
        return response;
    }

    @Override
    public UserTestResultResponse getById(int id) {

        UserTestResult userTestResult = userTestResultRepository.findById(id).orElseThrow(
                () -> new ErrorException(UserTestResultErrorMessage.NOT_FOUND));

        UserTestResultResponse response = modelMapper.map(userTestResult, UserTestResultResponse.class);
        AccountResponse accountResponse = modelMapper.map(userTestResult.getAccount(), AccountResponse.class);
        List<UserTestResponse> userTestResponses = new ArrayList<>();

        for (UserTest userTest : userTestResult.getUserTests()){
            UserTestResponse userTestResponse = modelMapper.map(userTest, UserTestResponse.class);
            userTestResponses.add(userTestResponse);
        }

        response.setUser(accountResponse);
        response.setUserTestResponse(userTestResponses);
        return response;
    }

    @Override
    public List<UserTestResultResponse> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public List<UserTestResultResponse> getAllByUser(int accountId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllByUser'");
    }

}
