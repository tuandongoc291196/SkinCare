package com.fu.skincare.response.userSkinType;

import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.skinType.SkinTypeResponse;

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
public class UserSkinTypeResponse {
    private int id;
    private SkinTypeResponse skinTypeResponse;
    private AccountResponse user;
}
