package com.fu.skincare.response.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BlogResponse {
    private int id;
    private String title;
    private String content;
    private String image;
    private String createdAt;
    private String status;
    private String authorName;
}
