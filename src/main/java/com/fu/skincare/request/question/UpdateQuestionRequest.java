package com.fu.skincare.request.question;

import java.util.List;

import com.fu.skincare.request.answer.AnswerUpdateResquest;

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
public class UpdateQuestionRequest {
    private int id;
    private String question;
    private List<AnswerUpdateResquest> answers;
}
