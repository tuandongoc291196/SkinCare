package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.constants.message.answer.AnswerErrorMessage;
import com.fu.skincare.constants.message.question.QuestionErrorMessage;
import com.fu.skincare.constants.message.skinType.SkinTypeErrorMessage;
import com.fu.skincare.constants.message.userTestResult.UserTestResultErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Answer;
import com.fu.skincare.entity.ProductSkinType;
import com.fu.skincare.entity.Question;
import com.fu.skincare.entity.SkinType;
import com.fu.skincare.entity.UserSkinType;
import com.fu.skincare.entity.UserTest;
import com.fu.skincare.entity.UserTestResult;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.AnswerRepository;
import com.fu.skincare.repository.ProductSkinTypeRepository;
import com.fu.skincare.repository.QuestionRepository;
import com.fu.skincare.repository.SkinTypeRepository;
import com.fu.skincare.repository.UserTestRepository;
import com.fu.skincare.repository.UserTestResultRepository;
import com.fu.skincare.request.userTest.CreateUserTestRequest;
import com.fu.skincare.request.userTestResult.CreateUserTestResultRequest;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.product.ProductResponse;
import com.fu.skincare.response.skinType.SkinTypeResponse;
import com.fu.skincare.response.userTest.UserTestResponse;
import com.fu.skincare.response.userTestResult.UserTestHistoryResponse;
import com.fu.skincare.response.userTestResult.UserTestResultResponse;
import com.fu.skincare.service.UserSkinTypeService;
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
    private final UserSkinTypeService userSkinTypeService;
    private final SkinTypeRepository skinTypeRepository;
    private final ProductSkinTypeRepository productSkinTypeRepository;

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
        int maxPoint = 0;
        for (CreateUserTestRequest createUserTestRequest : request.getUserTests()) {

            Answer answer = answerRepository.findById(createUserTestRequest.getAnswerId()).orElseThrow(
                    () -> new ErrorException(AnswerErrorMessage.ANSWER_NOT_FOUND));

            Question question = questionRepository.findById(createUserTestRequest.getQuestionId()).orElseThrow(
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
            maxPoint += question.getAnswers().stream()
                    .filter(a -> Status.ACTIVATED.equals(a.getStatus())) // filter active answers
                    .mapToInt(Answer::getPoint)
                    .max()
                    .orElse(0);
            userTestResponses.add(userTestResponse);
            totalPoint += answer.getPoint();
        }
        userTestResultSaved.setMaxPoint(maxPoint);
        userTestResultSaved.setTotalPoint(totalPoint);

        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
        accountResponse.setRoleName(account.getRole().getName());
        UserTestResultResponse response = modelMapper.map(userTestResultSaved, UserTestResultResponse.class);
        response.setUser(accountResponse);
        response.setUserTestResponse(userTestResponses);
        SkinTypeResponse skinTypeResponse = userSkinTypeService.createUserSkinType(userTestResultSaved);
        SkinType skinType = skinTypeRepository.findById(skinTypeResponse.getId()).orElseThrow(
                () -> new ErrorException(SkinTypeErrorMessage.NOT_FOUND));

        List<ProductSkinType> productSkinTypes = productSkinTypeRepository.findBySkinType(skinType);
        List<ProductResponse> suitableProducts = new ArrayList<>();
        for (ProductSkinType productSkinType : productSkinTypes) {
            ProductResponse productResponse = Utils.convertProduct(productSkinType.getProduct());
            suitableProducts.add(productResponse);
        }
        response.setSuitableProducts(suitableProducts);
        response.setSkinType(skinTypeResponse);
        return response;
    }

    @Override
    public UserTestResultResponse getById(int id) {

        UserTestResult userTestResult = userTestResultRepository.findById(id).orElseThrow(
                () -> new ErrorException(UserTestResultErrorMessage.NOT_FOUND));

        UserTestResultResponse response = modelMapper.map(userTestResult, UserTestResultResponse.class);
        AccountResponse accountResponse = modelMapper.map(userTestResult.getAccount(), AccountResponse.class);
        List<UserTestResponse> userTestResponses = new ArrayList<>();

        for (UserTest userTest : userTestResult.getUserTests()) {
            UserTestResponse userTestResponse = modelMapper.map(userTest, UserTestResponse.class);
            userTestResponses.add(userTestResponse);
        }

        UserSkinType userSkinType = userTestResult.getUserSkinTypes().stream().findFirst().orElseThrow(
                () -> new ErrorException(UserTestResultErrorMessage.NOT_FOUND));
        SkinType skinType = userSkinType.getSkinType();
        List<ProductSkinType> productSkinTypes = productSkinTypeRepository.findBySkinType(skinType);
        List<ProductResponse> suitableProducts = new ArrayList<>();
        for (ProductSkinType productSkinType : productSkinTypes) {
            ProductResponse productResponse = Utils.convertProduct(productSkinType.getProduct());
            suitableProducts.add(productResponse);
        }
        response.setSuitableProducts(suitableProducts);
        SkinTypeResponse skinTypeResponse = modelMapper.map(skinType, SkinTypeResponse.class);
        response.setSkinType(skinTypeResponse);
        response.setUser(accountResponse);
        response.setUserTestResponse(userTestResponses);
        return response;
    }

    @Override
    public List<UserTestHistoryResponse> getAll() {
        List<UserTestResult> userTestResults = userTestResultRepository.findAllByOrderByIdDesc();

        if (userTestResults.isEmpty()) {
            throw new ErrorException(UserTestResultErrorMessage.EMPTY);
        }

        List<UserTestHistoryResponse> userTestHistoryResponses = new ArrayList<>();
        for (UserTestResult userTestResult : userTestResults) {
            AccountResponse accountResponse = modelMapper.map(userTestResult.getAccount(), AccountResponse.class);
            accountResponse.setRoleName(userTestResult.getAccount().getRole().getName());
            UserTestHistoryResponse userTestHistoryResponse = modelMapper.map(userTestResult,
                    UserTestHistoryResponse.class);

            SkinType skinType = userTestResult.getUserSkinTypes().stream().findFirst().get().getSkinType();
            SkinTypeResponse skinTypeResponse = modelMapper.map(skinType, SkinTypeResponse.class);
            userTestHistoryResponse.setSkinType(skinTypeResponse);
            userTestHistoryResponse.setUser(accountResponse);
            userTestHistoryResponses.add(userTestHistoryResponse);
        }

        return userTestHistoryResponses;
    }

    @Override
    public List<UserTestHistoryResponse> getAllByUser(int accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

        List<UserTestResult> userTestResults = userTestResultRepository.findByAccountOrderByIdDesc(account);

        if (userTestResults.isEmpty()) {
            throw new ErrorException(UserTestResultErrorMessage.EMPTY);
        }

        List<UserTestHistoryResponse> userTestHistoryResponses = new ArrayList<>();
        for (UserTestResult userTestResult : userTestResults) {
            UserTestHistoryResponse userTestHistoryResponse = modelMapper.map(userTestResult,
                    UserTestHistoryResponse.class);
            AccountResponse accountResponse = modelMapper.map(userTestResult.getAccount(), AccountResponse.class);
            accountResponse.setRoleName(userTestResult.getAccount().getRole().getName());
            SkinType skinType = userTestResult.getUserSkinTypes().stream().findFirst().get().getSkinType();
            SkinTypeResponse skinTypeResponse = modelMapper.map(skinType, SkinTypeResponse.class);
            userTestHistoryResponse.setUser(accountResponse);
            userTestHistoryResponse.setSkinType(skinTypeResponse);
            userTestHistoryResponses.add(userTestHistoryResponse);
        }

        return userTestHistoryResponses;
    }

}
