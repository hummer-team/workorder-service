package com.workorder.service.facade.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author guo.li
 */
@Data
public class TemplateCreatedReqDto {
    @NotEmpty(message = "template can't empty.")
    @Length(max = 100, message = "template name max value 100")
    private String name;
    @NotNull(message = "approve user can't null")
    private List<Integer> approveUserIds;
    @NotNull(message = "execute user can't null")
    private Integer executeUserId;
    @NotEmpty(message = "project code can't null")
    private String projectCode;
    @Length(max = 1000, message = "template describe max length 1000.")
    private String describe;
    private List<String> envList;
}
