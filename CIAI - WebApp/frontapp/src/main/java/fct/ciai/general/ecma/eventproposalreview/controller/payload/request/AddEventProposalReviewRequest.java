package fct.ciai.general.ecma.eventproposalreview.controller.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AddEventProposalReviewRequest", description = "Adds event review")
public class AddEventProposalReviewRequest {
    @ApiModelProperty(value = "event review proposalId")
    @NotNull
    private Long eventProposalId;

    @ApiModelProperty(value = "event review text")
    private String reviewText;

    @ApiModelProperty(value = "event review approved")
    @NotNull
    private boolean approved;
}
