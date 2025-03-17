package com.fu.skincare.service;

import com.fu.skincare.entity.UserTestResult;
import com.fu.skincare.response.skinType.SkinTypeResponse;
import com.fu.skincare.response.userSkinType.UserSkinTypeResponse;

public interface UserSkinTypeService {
    public UserSkinTypeResponse getUserSkinType(int accountId);

    public SkinTypeResponse getSkinType(int id);

    public SkinTypeResponse createUserSkinType(UserTestResult userTestResult);
}
