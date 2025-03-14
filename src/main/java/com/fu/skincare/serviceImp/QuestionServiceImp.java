package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.answer.AnswerErrorMessage;
import com.fu.skincare.constants.message.question.QuestionErrorMessage;
import com.fu.skincare.entity.Answer;
import com.fu.skincare.entity.Question;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AnswerRepository;
import com.fu.skincare.repository.QuestionRepository;
import com.fu.skincare.request.answer.AnswerUpdateResquest;
import com.fu.skincare.request.answer.CreateAnswerRequestDTO;
import com.fu.skincare.request.question.CreateQuestionRequest;
import com.fu.skincare.request.question.UpdateQuestionRequest;
import com.fu.skincare.response.answer.AnswerResponse;
import com.fu.skincare.response.question.QuestionResponse;
import com.fu.skincare.service.QuestionService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImp implements QuestionService {

    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    public QuestionResponse createQuestion(CreateQuestionRequest request) {

        Question question = Question.builder()
                .question(request.getQuestion())
                .createdAt(Utils.formatVNDatetimeNow())
                .status(Status.ACTIVATED)
                .build();
        Question questionSaved = questionRepository.save(question);
        QuestionResponse response = modelMapper.map(questionSaved, QuestionResponse.class);
        List<AnswerResponse> listAnswerResponses = new ArrayList<>();
        for (CreateAnswerRequestDTO answer : request.getAnswers()) {
            Answer answerEntity = Answer.builder()
                    .answer(answer.getAnswer())
                    .point(answer.getPoint())
                    .createdAt(Utils.formatVNDatetimeNow())
                    .status(Status.ACTIVATED)
                    .question(questionSaved)
                    .build();

            Answer answerSaved = answerRepository.save(answerEntity);
            AnswerResponse answerResponse = modelMapper.map(answerSaved, AnswerResponse.class);
            listAnswerResponses.add(answerResponse);
        }
        response.setListAnswers(listAnswerResponses);
        return response;
    }

    @Override
    public QuestionResponse getQuestionById(int id) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new ErrorException(QuestionErrorMessage.QUESTION_NOT_FOUND));
        QuestionResponse response = modelMapper.map(question, QuestionResponse.class);
        List<AnswerResponse> listAnswerResponses = new ArrayList<>();
        for (Answer answer : question.getAnswers()) {
            AnswerResponse answerResponse = modelMapper.map(answer, AnswerResponse.class);
            listAnswerResponses.add(answerResponse);
        }
        response.setListAnswers(listAnswerResponses);
        return response;
    }

    @Override
    public List<QuestionResponse> getAllQuestions(int pageNo, int pageSize, String sortBy, boolean isAscending) {
        Page<Question> pageResults = null;
        if (isAscending) {
            pageResults = questionRepository.findAllByStatus(Status.ACTIVATED,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            pageResults = questionRepository.findAllByStatus(Status.ACTIVATED,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }

        if (pageResults.isEmpty()) {
            throw new EmptyException(QuestionErrorMessage.EMPTY);
        }

        List<QuestionResponse> response = new ArrayList<>();
        for (Question question : pageResults.getContent()) {
            QuestionResponse questionResponse = modelMapper.map(question, QuestionResponse.class);
            List<AnswerResponse> listAnswerResponses = new ArrayList<>();
            for (Answer answer : question.getAnswers()) {
                AnswerResponse answerResponse = modelMapper.map(answer, AnswerResponse.class);
                listAnswerResponses.add(answerResponse);
            }
            questionResponse.setListAnswers(listAnswerResponses);
            response.add(questionResponse);
        }
        return response;
    }

    @Override
    public QuestionResponse updateQuestion(UpdateQuestionRequest request) {
        Question question = questionRepository.findById(request.getId())
                .orElseThrow(() -> new ErrorException(QuestionErrorMessage.QUESTION_NOT_FOUND));
        
        question.setQuestion(request.getQuestion());
        questionRepository.save(question);
        QuestionResponse response = modelMapper.map(question, QuestionResponse.class);

        List<AnswerResponse> listAnswerResponses = new ArrayList<>();

        for (AnswerUpdateResquest answer: request.getAnswers()){
            Answer answerUpdate = answerRepository.findById(answer.getId()).orElseThrow(
                () -> new ErrorException(AnswerErrorMessage.ANSWER_NOT_FOUND)
            );

            answerUpdate.setAnswer(answer.getAnswer());
            answerUpdate.setPoint(answer.getPoint());

            answerRepository.save(answerUpdate);
            AnswerResponse answerResponse = modelMapper.map(answerUpdate, AnswerResponse.class);
            listAnswerResponses.add(answerResponse);
        }
        response.setListAnswers(listAnswerResponses);
        return response;
    }

}
