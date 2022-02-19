package com.workorder.service.facade;

import com.workorder.service.facade.dto.request.UserCreatedReqDto;
import com.workorder.service.facade.dto.request.UserLoginReqDto;
import com.workorder.service.facade.dto.response.UserLoginRespDto;

/**
 * @author guo.li
 */
public interface UserFacade {
    /**
     * create user
     *
     * @param req req
     */
    void createUser(UserCreatedReqDto req);

    /**
     * login
     *
     * @param req req
     * @return
     */
    UserLoginRespDto login(UserLoginReqDto req);
}
