package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.skinType.CreateSkinTypeRequest;
import com.fu.skincare.response.skinType.SkinTypeResponse;

public interface SkinTypeService {
    public SkinTypeResponse createSkinType(CreateSkinTypeRequest request);

    public SkinTypeResponse getSkinType(int id);

    public List<SkinTypeResponse> getAllSkinTypes();
}
