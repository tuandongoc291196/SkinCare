package com.fu.skincare.response.question;

import java.util.List;

import com.fu.skincare.response.answer.AnswerResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private int id;
    private String question;
    private String createdAt;
    private String status;
    private List<AnswerResponse> listAnswers;
}
