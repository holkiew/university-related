package fct.ciai.general.ecma.comment.service;

import fct.ciai.general.ecma.comment.controller.payload.request.AddCommentRequest;
import fct.ciai.general.ecma.comment.controller.payload.request.DeleteCommentRequest;
import fct.ciai.general.ecma.eventproposal.service.EventProposalService;
import fct.ciai.general.ecma.persistence.model.Comment;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.repository.CommentRepository;
import fct.ciai.general.ecma.persistence.repository.EventProposalRepository;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.exception.BadRequestException;
import fct.ciai.general.utils.UserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventProposalService eventProposalService;
    private final EventProposalRepository eventProposalRepository;
    private final MailSender mailSender;

    public void addNewCommentToEventProposal(AuthUser loggedUser, AddCommentRequest request) {
        EventProposal eventProposal = eventProposalRepository.findById(request.getEventProposalId())
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        if(eventProposalService.userIsInvolvedInEventProposal(loggedUser, eventProposal)) {
            Comment comment = createComment(loggedUser, request);
            commentRepository.save(comment);
            try {
                sendEmailNotification(loggedUser, comment, eventProposal);
            } catch (Exception e) {
                log.warn("Email problem: ", e.getMessage());
            }
        } else {
            throw new BadRequestException("User has no permission to add comment to this discussion.");
        }
    }

    @Transactional
    public void deleteEventProposalComment(AuthUser loggedUser, DeleteCommentRequest request) {
        EventProposal eventProposal = eventProposalRepository.findById(request.getEventProposalId())
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new BadRequestException("Non existing comment"));

        if (!UserUtils.isEcmaAdmin(loggedUser) || !UserUtils.isPartnerAdmin(loggedUser)) {
            if (!comment.getAuthor().getId().equals(loggedUser.getId())) {
                throw new BadRequestException("User has no permission to delete this comment.");
            }
        }
        if(comment.getEventProposal().equals(eventProposal)) {
            commentRepository.deleteById(request.getCommentId());
        } else {
            throw new BadRequestException("Comment is not from this Event proposal discussion");
        }
    }

    public List<Comment> getAllEventProposalComments(AuthUser loggedUser, Long eventProposalId) {
        EventProposal eventProposal = eventProposalRepository.findById(eventProposalId)
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        if(eventProposalService.userIsInvolvedInEventProposal(loggedUser, eventProposal)) {
            return commentRepository.findByEventProposalOrderByTimestampDesc(eventProposal);
        }else {
            throw new BadRequestException("User has no permission to see this discussion.");
        }
    }

    private Comment createComment(AuthUser loggedUser, AddCommentRequest request) {
        EventProposal proposal = eventProposalRepository.findById(request.getEventProposalId())
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        return Comment.builder()
                .eventProposal(proposal)
                .author(loggedUser.getUser())
                .text(request.getText())
                .timestamp(new Date())
                .url(request.getUrl())
                .build();
    }

    private void sendEmailNotification(AuthUser loggedUser, Comment comment, EventProposal eventProposal) {
        String[] emails = eventProposalService.getAllInvolvedUsers(eventProposal)
                .stream().filter(user -> !user.equals(loggedUser)).map(AuthUser::getEmail).toArray(String[]::new);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emails);
        message.setSubject("ECMA comment notification");
        message.setText(
                String.format("New comment in discussion for event proposal: \"%s\". URL: %s",
                        eventProposal.getTitle(),
                        comment.getUrl()));
        mailSender.send(message);
    }
}
