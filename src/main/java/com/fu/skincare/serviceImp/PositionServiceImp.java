package com.fu.skincare.serviceImp;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.position.PositionErrorMessage;
import com.fu.skincare.entity.Position;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.PositionRepository;
import com.fu.skincare.request.position.CreatePositionRequest;
import com.fu.skincare.response.position.PositionResponse;
import com.fu.skincare.service.PositionService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImp implements PositionService {

  public final ModelMapper modelMapper;
  public final PositionRepository positionRepository;

  @Override
  public PositionResponse createPostion(CreatePositionRequest createPositionRequest) {

    Position position = Position.builder()
        .name(createPositionRequest.getName())
        .status(Status.ACTIVATED)
        .createdAt(Utils.formatVNDatetimeNow())
        .build();
    Position positionSaved = positionRepository.save(position);

    PositionResponse response = modelMapper.map(positionSaved, PositionResponse.class);

    return response;
  }

  @Override
  public List<PositionResponse> getListPosition() {
    List<Position> positions = positionRepository.findByStatus(Status.ACTIVATED);
    return Utils.mapList(positions, PositionResponse.class);
  }

  @Override
  public PositionResponse getPositionById(int id) {

    Position position = positionRepository.findById(id)
        .orElseThrow(() -> new ErrorException(PositionErrorMessage.POSITION_NOT_EXIST));

    return modelMapper.map(position, PositionResponse.class);

  }

}
