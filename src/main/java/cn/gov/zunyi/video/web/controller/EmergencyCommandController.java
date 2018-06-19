package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.EmergencyCommand;
import cn.gov.zunyi.video.service.EmergencyCommandService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api("应急指挥中心接口")
@RequestMapping("/emergencyCommand")
@RestController
public class EmergencyCommandController extends BaseController {

    @Autowired
    private EmergencyCommandService emergencyCommandService;

    /**
     * 查询应急指挥中心电话列表
     */
    @RequestMapping(value = "/getTelephone",method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> getTelephoneList(){
        Map<String,Object> map = new HashMap<>();
        EntityWrapper<EmergencyCommand> ew = new EntityWrapper<>();
        Page<EmergencyCommand> page = this.getPage();
        try {
            ew.eq("is_deleted","0");
            map.put("telephones",emergencyCommandService.selectPage(page,ew));
            return ResponseEntity.ok(map);
        }catch (Exception e){
            logger.error("查询电话列表出错");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增应急指挥中心
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> addEmergencyCommand(EmergencyCommand emergencyCommand){
        Map<String,Object> map = new HashMap<>();
        EntityWrapper<EmergencyCommand> ew = new EntityWrapper<>();
        try {
            boolean rel = false;
            emergencyCommand.setCreateTime(new Date(System.currentTimeMillis()));
            if(StringUtils.isNoneBlank(emergencyCommand.getTelephone())){
                emergencyCommand.setTelephone(emergencyCommand.getTelephone().trim());
                ew.eq("telephone",emergencyCommand.getTelephone());
                EmergencyCommand em = emergencyCommandService.selectOne(ew);
                if(em != null){
                    map.put("status","400");
                    map.put("message","固定电话已存在！");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                }
            }
            rel = emergencyCommandService.insert(emergencyCommand);
            if(!rel){
                map.put("status","500");
                map.put("message","服务器忙！");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
            }
                map.put("status","201");
                map.put("message","添加成功！");
                return ResponseEntity.status(HttpStatus.CREATED).body(map);
        }catch (Exception e){
            logger.error(e.toString());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 编辑应急指挥中心
     */
    @RequestMapping(value = "/edit",method = RequestMethod.PUT)
    public ResponseEntity<Map<String,Object>> editEmergencyCommand(EmergencyCommand emergencyCommand){
        Map<String,Object> map = new HashMap<>();
        EntityWrapper<EmergencyCommand> ew = new EntityWrapper<>();
        try {
            if(StringUtils.isNoneBlank(emergencyCommand.getId())){
                emergencyCommand.setUpdateTime(new Date(System.currentTimeMillis()));
                if(StringUtils.isNoneBlank(emergencyCommand.getTelephone())){
                    emergencyCommand.setTelephone(emergencyCommand.getTelephone().trim());
                    ew.eq("telephone",emergencyCommand.getTelephone());
                    if(emergencyCommandService.selectOne(ew) != null){
                        map.put("status","400");
                        map.put("message","手机号码已存在！");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                    }
                }
                boolean rel = emergencyCommandService.updateById(emergencyCommand);
                if(!rel){
                    map.put("status","500");
                    map.put("message","服务器忙！");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
                }
                map.put("status","201");
                map.put("message","修改成功！");
                return ResponseEntity.status(HttpStatus.CREATED).body(map);
            }
        }catch (Exception e){
            logger.error("编辑应急指挥中心出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除应急指挥中心
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ResponseEntity<Map<String,Object>> deleteEmergencyCommand(@RequestParam("id") String id){
        Map<String,Object> map = new HashMap<>();
        EmergencyCommand emergencyCommand = new EmergencyCommand();
        try {
            emergencyCommand.setId(id);
            emergencyCommand.setIsDeleted("1");
            boolean rel = false;
            rel = emergencyCommandService.updateById(emergencyCommand);
            if(!rel){
                map.put("message","服务器忙！");
                map.put("status","500");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
            }
            map.put("status","201");
            map.put("message","删除成功！");
            return ResponseEntity.ok(map);
        }catch (Exception e){
            logger.error("删除应急指挥中心出错！");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
