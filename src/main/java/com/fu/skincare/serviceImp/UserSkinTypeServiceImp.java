package com.fu.skincare.serviceImp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.SkinTypeName;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.userTestResult.UserTestResultErrorMessage;
import com.fu.skincare.entity.SkinType;
import com.fu.skincare.entity.UserSkinType;
import com.fu.skincare.entity.UserTestResult;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.SkinTypeRepository;
import com.fu.skincare.repository.UserSkinTypeRepository;
import com.fu.skincare.response.skinType.SkinTypeResponse;
import com.fu.skincare.response.userSkinType.UserSkinTypeResponse;
import com.fu.skincare.service.UserSkinTypeService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSkinTypeServiceImp implements UserSkinTypeService {

    private final SkinTypeRepository skinTypeRepository;
    private final UserSkinTypeRepository userSkinTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserSkinTypeResponse getUserSkinType(int accountId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserSkinType'");
    }

    @Override
    public SkinTypeResponse getSkinType(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSkinType'");
    }

    @Override
    public SkinTypeResponse createUserSkinType(UserTestResult userTestResult) {
        if (userTestResult == null) {
            throw new ErrorException(UserTestResultErrorMessage.NOT_FOUND);
        }

        if (userTestResult.getTotalPoint() < 10) {
            SkinType skinType = skinTypeRepository.findByType(SkinTypeName.NORMALLY);
            UserSkinType userSkinType = UserSkinType.builder()
                    .skinType(skinType)
                    .userTestResult(userTestResult)
                    .createdAt(Utils.formatVNDatetimeNow())
                    .status(Status.ACTIVATED)
                    .account(userTestResult.getAccount()).build();

            userSkinTypeRepository.save(userSkinType);
            return modelMapper.map(skinType, SkinTypeResponse.class);
        } else if (userTestResult.getTotalPoint() < 20) {
            SkinType skinType = skinTypeRepository.findByType(SkinTypeName.COMBINATION);
            UserSkinType userSkinType = UserSkinType.builder()
                    .skinType(skinType)
                    .userTestResult(userTestResult)
                    .createdAt(Utils.formatVNDatetimeNow())
                    .status(Status.ACTIVATED)
                    .account(userTestResult.getAccount()).build();

            userSkinTypeRepository.save(userSkinType);
            return modelMapper.map(skinType, SkinTypeResponse.class);
        } else if (userTestResult.getTotalPoint() < 30) {
            SkinType skinType = skinTypeRepository.findByType(SkinTypeName.OIL);
            UserSkinType userSkinType = UserSkinType.builder()
                    .skinType(skinType)
                    .userTestResult(userTestResult)
                    .createdAt(Utils.formatVNDatetimeNow())
                    .status(Status.ACTIVATED)
                    .account(userTestResult.getAccount()).build();

            userSkinTypeRepository.save(userSkinType);
            return modelMapper.map(skinType, SkinTypeResponse.class);
        } else {
            SkinType skinType = skinTypeRepository.findByType(SkinTypeName.DRY);
            UserSkinType userSkinType = UserSkinType.builder()
                    .skinType(skinType)
                    .userTestResult(userTestResult)
                    .createdAt(Utils.formatVNDatetimeNow())
                    .status(Status.ACTIVATED)
                    .account(userTestResult.getAccount()).build();

            userSkinTypeRepository.save(userSkinType);
            return modelMapper.map(skinType, SkinTypeResponse.class);
        }

    }

}
