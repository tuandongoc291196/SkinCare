package com.fu.skincare.response.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerResponse {
    private int id;
    private String answer;
    private int point;
    private String createdAt;
    private String status;
}
