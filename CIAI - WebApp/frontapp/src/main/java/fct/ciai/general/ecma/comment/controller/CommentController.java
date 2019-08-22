package fct.ciai.general.ecma.comment.controller;

import fct.ciai.general.ecma.comment.controller.payload.request.AddCommentRequest;
import fct.ciai.general.ecma.comment.controller.payload.request.DeleteCommentRequest;
import fct.ciai.general.ecma.comment.model.dto.CommentDTO;
import fct.ciai.general.ecma.comment.service.CommentService;
import fct.ciai.general.security.LoggedUser;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "Manages proposal comments")
@Controller
@RequestMapping("api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper mapper;

    @ApiOperation(httpMethod = "PUT",
            value = "Adds new comment to proposal",
            nickname = "addNewCommentToEventProposal")
    @PutMapping("addNewCommentToEventProposal")
    public ResponseEntity addNewCommentToEventProposal(@LoggedUser AuthUser loggedUser, @Valid @RequestBody AddCommentRequest addCommentRequest) {
        try {
            commentService.addNewCommentToEventProposal(loggedUser, addCommentRequest);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Deletes proposal comment",
            nickname = "deleteEventProposalComment")
    @DeleteMapping("deleteEventProposalComment")
    public ResponseEntity deleteEventProposalCommentByAdmin(@LoggedUser AuthUser loggedUser, @Valid @RequestBody DeleteCommentRequest deleteCommentRequest) {
        try {
            commentService.deleteEventProposalComment(loggedUser, deleteCommentRequest);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Returns all proposal comments",
            nickname = "getAllEventProposalComments")
    @GetMapping("getAllEventProposalComments")
    public ResponseEntity<List<CommentDTO>> getAllEventProposalComments(@LoggedUser AuthUser loggedUser, @RequestParam Long proposalId) {
        return new ResponseEntity<>(
                commentService.getAllEventProposalComments(loggedUser, proposalId).stream()
                        .map(eventProposal -> mapper.map(eventProposal, CommentDTO.class))
                        .collect(Collectors.toList()),
                HttpStatus.ACCEPTED);
    }
}
