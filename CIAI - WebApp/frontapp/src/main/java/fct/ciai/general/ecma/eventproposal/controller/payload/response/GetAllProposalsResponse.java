package fct.ciai.general.ecma.eventproposal.controller.payload.response;

import fct.ciai.general.ecma.eventproposal.model.dto.EventProposalDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "GetAllProposalsResponse", description = "Gets pageable proposals")
public class GetAllProposalsResponse {
    private List<EventProposalDTO> proposalDTOList;
    private int totalPages;
}
