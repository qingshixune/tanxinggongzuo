package cn.gov.zunyi.video.web.controller;

import cn.gov.zunyi.video.model.PtCare;
import cn.gov.zunyi.video.model.Texcel;
import cn.gov.zunyi.video.common.util.ReadExcelUtil;
import cn.gov.zunyi.video.service.PtCareService;
import cn.gov.zunyi.video.service.Tservice;
import cn.gov.zunyi.video.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@Api("党内关怀")
@RestController
@RequestMapping("/care")
@ConfigurationProperties
public class PtCareController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PtCareController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PtCareService ptCareService;
    @Autowired
    private Tservice tservice;

    /**
     * 查询捐款求助信息
     * @param status 接受前台上传状态，0为查询捐款信息，1为查询求助信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "_index", value = "分页起始偏移量", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "_size", value = "返回条数", required = false, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value="/getPartyCare",method = RequestMethod.GET)
    public ResponseEntity<Map> getPartyCare(@RequestParam("status") int status){
        Map<String,Object> map = new HashMap<>();
        try {
            Page<PtCare> page = getPage();
            List<PtCare> ptCares = ptCareService.query(page,status);  //根据状态获取求助者或捐款者信息
            map.put("ptCares",ptCares);
            return ResponseEntity.ok(map);
        }catch (Exception e){
            if(status == 0) {
                LOGGER.error("查询捐款信息出错!", e);
            }else{
                LOGGER.error("查询求助信息出错!", e);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增或修改 党内关怀信息
     * @param ptCare
     * @return
     */
    @RequestMapping(value="/editPartyCare",method = RequestMethod.POST)
    public ResponseEntity<Map> editPartyCare(PtCare ptCare){
        Map<String,Object> map = new HashMap<>();
        try {
            boolean result;
            if(ptCare.getId() != null){
                ptCare.setUpdateTime(new Date(System.currentTimeMillis()));
                result = ptCareService.updateById(ptCare);
                map.put("status","200");
                map.put("message","修改成功!");
                return ResponseEntity.ok(map);
            }else {
                ptCare.setCreateTime(new Date(System.currentTimeMillis()));
                ptCare.setUpdateTime(ptCare.getCreateTime());
                result = ptCareService.insert(ptCare);
                map.put("status","201");
                map.put("message","新增成功!");
                return ResponseEntity.ok(map);
            }
        }catch (Exception e){
            LOGGER.error("增加或修改信息出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除 党内关怀信息
     * @param id
     * @return
     */
    @RequestMapping(value="/deletePtCare",method = RequestMethod.DELETE)
    public ResponseEntity<Map> deletePtCare(int id){
        Map<String,Object> map = new HashMap<>();
        try {
            boolean result;
            PtCare ptCare = ptCareService.selectById(id);
            if (ptCare != null) {
                result = ptCareService.deleteById(id);
                map.put("status","200");
                map.put("message","删除成功！");
                return ResponseEntity.ok(map);
            }else {
                map.put("status","400");
                map.put("message","信息不存在！");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            }
        }catch (Exception e){
            LOGGER.error("删除出错!", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 党支部数据导入测试版
     */
    @RequestMapping(value="/leadExcel",method = RequestMethod.POST)
    public ResponseEntity<Map> leadExcel(@RequestParam(value = "excelFile") MultipartFile excelFile){
        Map<String,Object> map = new HashMap<>();
        try{
            List<Texcel> texcels = new ArrayList<Texcel>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Texcel te = null;
            String excelName = excelFile.getOriginalFilename().substring(0,3);
            List<String[]> horseList = ReadExcelUtil.readExcel(excelFile);

            Wrapper<Texcel> wr = new EntityWrapper<>();
            List<Texcel> tex = tservice.selectList(wr);
            List msgs = new ArrayList();

            for(int i = 0;i<horseList.size();i++){
                boolean result = false;
                String[] excels = horseList.get(i);
                for (Texcel t:tex) {
                    if(excels[1].equals(t.getPbName())){
                        result = true;
                        break;
                    }else if(excels[0].equals(t.getPbName()) && excelName.equals("000")){
                        result = true;
                        break;
                    }
                }
                if(!result) {
                    if (excelName.equals("000")) {
                        Texcel texcel = new Texcel();
                        if (i == 0) {
                            texcel.setId(1);
                            texcel.setPbName(excels[0]);
                            texcels.add(texcel);
                        } else if (i > 1) {
                            if (excels[0].equals("合计")) {
                                break;
                            }
                            texcel.setPbName(excels[1]);
                            texcel.setPbSecretary(excels[2]);
                            texcel.setPbLinkman(excels[3]);
                            texcel.setLinkPhone(excels[4]);
                            texcel.setPbType(excels[5]);
                            texcel.setUnitAddress(excels[6]);
                            texcel.setPcNumber(Integer.parseInt(excels[7]));
                            texcel.setTotalcount(Integer.parseInt(excels[8]));
                            texcel.setBarnchNumber(Integer.parseInt(excels[9]));
                            texcel.setPmNumber(Integer.parseInt(excels[10]));
                            texcel.setNote(excels[11]);
                            texcel.setPid(1);
                            texcel.setCreateTime(new Date(System.currentTimeMillis()));
                            texcels.add(texcel);
                        }
                    }else if(i == 0){
                        Wrapper<Texcel> wp = new EntityWrapper<>();
                        wp.like("pb_name",excels[0]);
                        te = tservice.selectOne(wp);
                    } else if (i > 1) {
                        if (excels[0].equals("合计")) {
                            break;
                        }
                        if(te != null){
                            msgs.add("请先录入该表上级组织表！");
                            break;
                        }else {
                            Texcel texcel = new Texcel();
                            texcel.setPbName(excels[1]);
                            texcel.setPbSecretary(excels[2]);
                            texcel.setPbLinkman(excels[3]);
                            texcel.setLinkPhone(excels[4]);
                            texcel.setPbType(excels[5]);
                            texcel.setUnitType(excels[6]);
                            texcel.setUnitAddress(excels[7]);
                            texcel.setPmNumber(Integer.parseInt(excels[8]));
                            texcel.setNote(excels[9]);
                            texcel.setPid(te.getPid());
                            texcel.setCreateTime(new Date(System.currentTimeMillis()));
                            texcels.add(texcel);
                        }
                    }
                }else {
                    msgs.add("第"+(i+1)+"行数据重复录入！");
                }
            }
            map.put("message",msgs);
            tservice.insertBatch(texcels);

        }catch (Exception e){
            LOGGER.error("查询工作学习统计信息出错!", e);
        }
        return ResponseEntity.ok(map);
    }
}
