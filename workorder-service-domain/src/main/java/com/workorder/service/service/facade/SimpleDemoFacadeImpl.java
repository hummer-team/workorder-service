package com.workorder.service.service.facade;

import com.hummer.common.utils.ObjectCopyUtils;
import com.workorder.service.facade.dto.request.SimpleDemoSaveReqDto;
import com.workorder.service.dao.SimpleDemoDao;
import com.workorder.service.support.model.po.SimpleDemoPo;
import com.workorder.service.facade.SimpleDemoFacade;

import com.hummer.dao.annotation.TargetDataSourceTM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleDemoFacadeImpl implements SimpleDemoFacade {

    @Autowired(required = false)
    private SimpleDemoDao simpleDemoDao;

    @Override
    @TargetDataSourceTM(dbName = "delivery_w"
            , transactionManager = "delivery_w_TM"
            , rollbackFor = Exception.class)
    public void save(SimpleDemoSaveReqDto batchDto) {
        log.debug("this is demo request , info {}",batchDto);
        //ignore
    }

    @Override
    public SimpleDemoSaveReqDto querySingleById(String id){
        SimpleDemoPo po =  simpleDemoDao.querySingleById(id);
        return ObjectCopyUtils.copy(po, SimpleDemoSaveReqDto.class);
    }
}