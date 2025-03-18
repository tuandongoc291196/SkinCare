package com.fu.skincare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.RolePreAuthorize;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.question.QuestionSuccessMessage;
import com.fu.skincare.request.question.CreateQuestionRequest;
import com.fu.skincare.request.question.UpdateQuestionRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.question.QuestionResponse;
import com.fu.skincare.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/question")
@CrossOrigin("*")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> create(@Validated @RequestBody CreateQuestionRequest request) {
        ResponseDTO<QuestionResponse> responseDTO = new ResponseDTO<QuestionResponse>();
        QuestionResponse data = questionService.createQuestion(request);
        responseDTO.setData(data);
        responseDTO.setMessage(QuestionSuccessMessage.CREATE_QUESTION_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/edit/")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> edit(@Validated @RequestBody UpdateQuestionRequest request) {
        ResponseDTO<QuestionResponse> responseDTO = new ResponseDTO<QuestionResponse>();
        QuestionResponse data = questionService.updateQuestion(request);
        responseDTO.setData(data);
        responseDTO.setMessage(QuestionSuccessMessage.UPDATE_QUESTION_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/")
    public ResponseEntity<?> getById(@RequestParam int id) {
        ResponseDTO<QuestionResponse> responseDTO = new ResponseDTO<QuestionResponse>();
        QuestionResponse data = questionService.getQuestionById(id);
        responseDTO.setData(data);
        responseDTO.setMessage(QuestionSuccessMessage.GET_QUESTION_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> activeQuestion(@RequestParam int id) {
        ResponseDTO<QuestionResponse> responseDTO = new ResponseDTO<QuestionResponse>();
        QuestionResponse data = questionService.updateStatus(id, Status.ACTIVATED);
        responseDTO.setData(data);
        responseDTO.setMessage(QuestionSuccessMessage.ACTIVE_QUESTION_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> deleteQuestion(@RequestParam int id) {
        ResponseDTO<QuestionResponse> responseDTO = new ResponseDTO<QuestionResponse>();
        QuestionResponse data = questionService.updateStatus(id, Status.DISABLED);
        responseDTO.setData(data);
        responseDTO.setMessage(QuestionSuccessMessage.DISABLE_QUESTION_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        ListResponseDTO<QuestionResponse> responseDTO = new ListResponseDTO<QuestionResponse>();
        List<QuestionResponse> data = questionService.getAllQuestions(pageNo, pageSize, sortBy, isAscending);
        responseDTO.setData(data);
        responseDTO.setMessage(QuestionSuccessMessage.GET_ALL_QUESTION_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
