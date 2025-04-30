package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.Business.SPRINKLER_MAINTAININGRECORD)
public class MaintainingRecordController {

    @Resource
    private MaintainingRecordService maintainingRecordService;

    @Operation(summary = "批量新建维修记录 @author 芦苇")
    @PostMapping("/sprinklermanager/maintainingrecord/create")
    @SaCheckPermission("sprinklermanager:maintainingrecord:add")
    public ResponseDTO<String> createSprinkler(
            @RequestPart("file") @Valid MultipartFile file
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return maintainingRecordService.batchMaintainingRecordCreate(file, requestUser);
    }
}
