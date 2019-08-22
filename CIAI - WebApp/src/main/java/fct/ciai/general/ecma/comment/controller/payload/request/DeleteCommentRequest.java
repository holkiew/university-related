package fct.ciai.general.ecma.comment.controller.payload.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "DeleteCommentRequest", description = "Deletes proposal comment")
public class DeleteCommentRequest {
    @ApiModelProperty(value = "comment id")
    @NotNull
    private Long commentId;

    @ApiModelProperty(value = "event proposal id")
    @NotNull
    private Long eventProposalId;
}
