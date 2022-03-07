package com.workorder.service.api.validator;

import com.hummer.common.exceptions.AppException;
import com.hummer.user.auth.plugin.annotation.PermissionAuthorityConditionEnum;
import com.hummer.user.auth.plugin.context.UserContext;
import com.hummer.user.auth.plugin.holder.UserHolder;
import com.hummer.user.auth.plugin.validator.ValidOperationPermission;
import com.workorder.service.service.domain.OpEnum;
import com.workorder.service.service.facade.BaseWorkOrderFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        OpEnum[] opEnumList = Arrays.stream(authorityCodes).map(OpEnum::getByName).toArray(OpEnum[]::new);
        if (condition != PermissionAuthorityConditionEnum.NONE) {
            Map<OpEnum, Boolean> conditionMap = baseWorkOrderFacade.returnIsAllowOp(opEnumList);
            UserContext userContext = UserHolder.getNotNull();
            if (condition == PermissionAuthorityConditionEnum.ALL_OF) {
                List<OpEnum> ops = conditionMap.entrySet().stream().filter(e -> !e.getValue()).map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ops)) {
                    throw new AppException(40000, String.format("user %s not allow operation work order %s"
                            , userContext.getUserName(), ops));
                }
            } else if (condition == PermissionAuthorityConditionEnum.ANY_OF) {
                List<OpEnum> ops = conditionMap.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(ops)) {
                    throw new AppException(40000, String.format("user %s not allow operation work order %s"
                            , userContext.getUserName(), ops));
                }
            }
        } else {
            baseWorkOrderFacade.checkIsAllowOp(opEnumList);
        }
    }
}
