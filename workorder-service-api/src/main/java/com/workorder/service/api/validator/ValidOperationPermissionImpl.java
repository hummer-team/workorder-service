package com.workorder.service.api.validator;

import com.hummer.user.auth.plugin.annotation.PermissionAuthorityConditionEnum;
import com.hummer.user.auth.plugin.validator.ValidOperationPermission;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.facade.BaseWorkOrderFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author guo.li
 */
@Component
@Slf4j
public class ValidOperationPermissionImpl implements ValidOperationPermission {
    @Autowired
    @Qualifier("templateFacadeImpl")
    private BaseWorkOrderFacade baseWorkOrderFacade;

    @Override
    public void validate(String[] authorityCodes, PermissionAuthorityConditionEnum condition) {
        OpEnum[] opEnumList = Arrays.stream(authorityCodes).map(OpEnum::getByName).collect(Collectors.toList())
                .toArray(OpEnum[]::new);
        baseWorkOrderFacade.checkIsAllowOp(opEnumList);
    }
}
