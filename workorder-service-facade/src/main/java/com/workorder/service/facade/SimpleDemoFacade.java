package com.workorder.service.facade;


import com.workorder.service.facade.dto.request.SimpleDemoSaveReqDto;

/**
 * @Author: lee
 * @version:1.0.0
 * @Date: 2019/6/28 15:19
 **/
public interface SimpleDemoFacade {
    void save(SimpleDemoSaveReqDto demoDto);
    SimpleDemoSaveReqDto querySingleById(String id);
}
