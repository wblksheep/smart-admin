package net.lab1024.sa.admin.module.business.sprinklermanager.machine;


import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.vo.MachineVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerUpdateForm;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.Business.SPRINKLER_MACHINE)
public class MachineController {

    @Resource
    private MachineService machineService;

    @Operation(summary = "新建机台 @author 芦苇")
    @PostMapping("/sprinklermanager/machine/create")
    @SaCheckPermission("sprinklermanager:machine:add")
    public ResponseDTO<String> create(@RequestBody @Valid MachineCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return machineService.create(createVO);
    }

    @Operation(summary = "编辑机台 @author 芦苇")
    @PostMapping("/sprinklermanager/machine/update")
    @SaCheckPermission("sprinklermanager:machine:update")
    public ResponseDTO<String> updateSprinkler(@RequestBody @Valid MachineUpdateForm updateVO) {
        return machineService.update(updateVO);
    }

    @Operation(summary = "分页查询机台模块 @author 芦苇")
    @PostMapping("/sprinklermanager/machine/page/query")
    @SaCheckPermission("sprinklermanager:machine:query")
    public ResponseDTO<PageResult<MachineVO>> queryByPage(@RequestBody @Valid MachineQueryForm queryForm) {
        return machineService.queryByPage(queryForm);
    }

    @Operation(summary = "查询机台详情 @author 芦苇")
    @GetMapping("/sprinklermanager/machine/get/{machineId}")
    @SaCheckPermission("sprinklermanager:machine:detail")
    public ResponseDTO<MachineVO> getDetail(@PathVariable Long machineId) {
        return ResponseDTO.ok(machineService.getDetail(machineId));
    }

    @Operation(summary = "删除机台 @author 芦苇")
    @GetMapping("/sprinklermanager/machine/delete/{machineId}")
    @SaCheckPermission("sprinklermanager:machine:delete")
    public ResponseDTO<String> delete(@PathVariable Long machineId) {
        return machineService.delete(machineId);
    }
}
