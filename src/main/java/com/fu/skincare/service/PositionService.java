package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.position.CreatePositionRequest;
import com.fu.skincare.request.position.UpdatePositionRequest;
import com.fu.skincare.response.position.PositionResponse;

public interface PositionService {
  public PositionResponse createPostion(CreatePositionRequest createPositionRequest);

  public PositionResponse updatePostion(UpdatePositionRequest request);

  public List<PositionResponse> getListPosition();

  public PositionResponse getPositionById(int id);
}
