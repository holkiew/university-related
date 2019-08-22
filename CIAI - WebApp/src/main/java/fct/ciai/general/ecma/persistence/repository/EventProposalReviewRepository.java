package fct.ciai.general.ecma.persistence.repository;

import fct.ciai.general.ecma.persistence.model.EventProposalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventProposalReviewRepository extends JpaRepository<EventProposalReview, Long> {
    List<EventProposalReview> findByIdIn(List<Long> eventReviewIds);
    Optional<EventProposalReview> findByEventProposalId(Long eventProposalId);
}
