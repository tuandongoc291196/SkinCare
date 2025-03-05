package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.position.CreatePositionRequest;
import com.fu.skincare.response.position.PositionResponse;

public interface PositionService {
  public PositionResponse createPostion(CreatePositionRequest createPositionRequest);

  public List<PositionResponse> getListPosition();

  public PositionResponse getPositionById(int id);
}
