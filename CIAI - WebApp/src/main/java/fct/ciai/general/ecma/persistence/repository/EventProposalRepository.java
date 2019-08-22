package fct.ciai.general.ecma.persistence.repository;

import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventProposalRepository extends JpaRepository<EventProposal, Long> {
    Optional<EventProposal> findByTitle(String title);

    List<EventProposal> findByIdIn(List<Long> eventIds);

    List<EventProposal> findByAuthor(User author);

    Page<EventProposal> findByInvolvedCompany(Company company, Pageable pageable);

    Page<EventProposal> findByInvolvedCompanyAndApproved(Company company, boolean approved, Pageable pageable);

    @Query(value =  "SELECT p.* FROM EVENT_PROPOSALS p " +
                    "LEFT JOIN EVENT_PROPOSAL_REVIEWS r ON r.EVENT_PROPOSAL_ID = p.ID " +
                    "WHERE r.Id is null AND p.ASSIGNED_REVIEWER_ID = ?1", nativeQuery = true)
    Page<EventProposal> findByReviewerIdAndReviewIsNull(Long reviewerId, Pageable pageable);

    Page<EventProposal> findByApproved(boolean approved, Pageable pageable);
}
