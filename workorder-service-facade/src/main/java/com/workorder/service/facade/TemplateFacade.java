package com.workorder.service.facade;

import com.workorder.service.facade.dto.request.TemplateCreatedReqDto;
import com.workorder.service.facade.dto.response.TemplateInfoRespDto;

import java.util.List;

public interface TemplateFacade {
    void createTemplate(TemplateCreatedReqDto req);

    List<TemplateInfoRespDto> queryAllTemplates();

    List<String> queryAllProjectCode();
}
