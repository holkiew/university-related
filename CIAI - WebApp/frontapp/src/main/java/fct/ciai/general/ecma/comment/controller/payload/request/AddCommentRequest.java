package fct.ciai.general.ecma.comment.controller.payload.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AddCommentRequest", description = "Adds comment to proposal")
public class AddCommentRequest {

    @ApiModelProperty(value = "comment text")
    @NotBlank
    @Size(max = 1024)
    private String text;

    @ApiModelProperty(value = "comment url")
    private String url;

    @ApiModelProperty(value = "event proposal id")
    private Long eventProposalId;
}
