package fct.ciai.general.ecma.persistence.repository;

import fct.ciai.general.ecma.persistence.model.Comment;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventProposalOrderByTimestampDesc(EventProposal eventProposal);
    List<Comment> findByEventProposalAndAuthor(EventProposal eventProposal, User author);
    List<Comment> findByAuthor(User author);
    boolean existsByEventProposalAndAuthor(EventProposal eventProposal, User author);
}
