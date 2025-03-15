package com.fu.skincare.response.userTest;

import com.fu.skincare.entity.Question;
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
public class UserTestResponse {
    private int id;
    private String createdAt;
    private String status;
    private Question question;
    private AnswerResponse userAnswer;
}
