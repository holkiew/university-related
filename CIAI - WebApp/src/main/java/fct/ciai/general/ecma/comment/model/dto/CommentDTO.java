package fct.ciai.general.ecma.comment.model.dto;

import fct.ciai.general.ecma.persistence.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private User author;
    private Date timestamp;
}
