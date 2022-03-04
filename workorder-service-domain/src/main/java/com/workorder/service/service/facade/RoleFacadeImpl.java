package com.workorder.service.service.facade;

import com.google.common.collect.Lists;
import com.workorder.service.facade.RoleFacade;
import com.workorder.service.service.domain.RoleEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guo.li
 */
@Service
public class RoleFacadeImpl implements RoleFacade {
    @Override
    public List<String> allRoles() {

        return Lists.newArrayList(RoleEnum.DEVELOP.name(), RoleEnum.APPROVE.name()
                , RoleEnum.ADMINISTRATOR.name(), RoleEnum.EXECUTE.name());
    }
}
