package com.workorder.service.service.domain.core.status;

import com.workorder.service.support.model.po.WorkOrderHandlerFlow;
import lombok.Builder;
import lombok.Getter;

/**
 * @author guo.li
 */
@Builder
@Getter
public class NextHandlerFlow {
    private WorkOrderHandlerFlow next;
    private boolean isEnd;
}
