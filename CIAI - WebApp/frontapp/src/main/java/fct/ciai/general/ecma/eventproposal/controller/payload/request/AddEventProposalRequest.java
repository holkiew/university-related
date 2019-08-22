package fct.ciai.general.ecma.eventproposal.controller.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AddEventProposalRequest", description = "Adds event")
public class AddEventProposalRequest {

    @ApiModelProperty(value = "event title")
    @NotBlank
    @Size(max = 40)
    private String title;

    @ApiModelProperty(value = "event description")
    @Size(max = 500)
    private String description;

    @ApiModelProperty(value = "event goals")
    @Size(max = 500)
    private String goals;

    @ApiModelProperty(value = "event materials")
    @Size(max = 250)
    private String neededMaterials;

    @ApiModelProperty(value = "event budget")
    @Min(0)
    private Double budget;

    @ApiModelProperty(value = "event working plan")
    @NotBlank
    @Size(max = 500)
    private String workPlan;
}

