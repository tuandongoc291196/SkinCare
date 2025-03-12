package com.fu.skincare.serviceImp;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.message.skinType.SkinTypeErrorMessage;
import com.fu.skincare.entity.SkinType;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.SkinTypeRepository;
import com.fu.skincare.request.skinType.CreateSkinTypeRequest;
import com.fu.skincare.response.skinType.SkinTypeResponse;
import com.fu.skincare.service.SkinTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkinTypeServiceImp implements SkinTypeService {

    private final SkinTypeRepository skinTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public SkinTypeResponse createSkinType(CreateSkinTypeRequest request) {
        SkinType skinType = SkinType.builder().type(request.getType()).build();
        SkinType skinTypeSaved = skinTypeRepository.save(skinType);
        return modelMapper.map(skinTypeSaved, SkinTypeResponse.class);
    }

    @Override
    public SkinTypeResponse getSkinType(int id) {
        SkinType skinType = skinTypeRepository.findById(id).orElseThrow(
                () -> new ErrorException(SkinTypeErrorMessage.NOT_FOUND));

        return modelMapper.map(skinType, SkinTypeResponse.class);
    }

    @Override
    public List<SkinTypeResponse> getAllSkinTypes() {
        List<SkinType> skinTypes = skinTypeRepository.findAll();
        if (skinTypes.isEmpty()) {
            throw new EmptyException(SkinTypeErrorMessage.NOT_FOUND);
        }

        List<SkinTypeResponse> skinTypeResponses = skinTypes.stream()
                .map(skinType -> modelMapper.map(skinType, SkinTypeResponse.class))
                .toList();
        return skinTypeResponses;
    }

}
