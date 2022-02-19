package com.workorder.service.facade;

import com.workorder.service.facade.dto.request.TemplateCreatedReqDto;

public interface TemplateFacade {
    void createTemplate(TemplateCreatedReqDto req);
}
