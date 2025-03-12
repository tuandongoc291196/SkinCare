package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.question.CreateQuestionRequest;
import com.fu.skincare.response.question.QuestionResponse;

public interface QuestionService {
    public QuestionResponse createQuestion(CreateQuestionRequest request);
    public QuestionResponse getQuestionById(int id);
    public List<QuestionResponse> getAllQuestions(int pageNo, int pageSize, String sortBy, boolean isAscending);
}
