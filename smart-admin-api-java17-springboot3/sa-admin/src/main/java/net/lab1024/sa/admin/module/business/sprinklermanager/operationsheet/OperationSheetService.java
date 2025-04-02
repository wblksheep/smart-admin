package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.SprinklerOperationHandler;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.registry.OperationHandlerRegistry;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.LocalDateParseUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OperationSheetService {

    @Autowired
    private OperationHandlerRegistry operationHandlerRegistry;


    /**
     * 新建记录表
     *
     */
    public ResponseDTO<String> batchCreateOperationSheet(@Valid MultipartFile file, RequestUser requestUser){


        SprinklerOperationHandler handler = operationHandlerRegistry.getHandler("stock_in");
//        handler.validateSprinkler();//校验的逻辑是一致的，看存不存在喷头
//        //下面的逻辑都是不一样的。
//        handler.validateOperationSheet();
//        handler.insert();
//        handler.updateStatus();
//        handler.addTrace();
        handler.handle();
        return ResponseDTO.ok();
    }
}
