package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.mapper.VeEquipmentMapper;
import cn.gov.zunyi.video.model.VeCount;
import cn.gov.zunyi.video.model.VeEquipment;
import cn.gov.zunyi.video.model.VeEquipmentRunstus;
import cn.gov.zunyi.video.model.Video;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ZTY
 * @since 2018-05-24
 */
@Service
public class VeEquipmentService extends ServiceImpl<VeEquipmentMapper,VeEquipment> {

    @Autowired
    private VeEquipmentMapper veEquipmentMapper;

    /**
     * 统计视频设备各类数量
     * @param veEquipments
     * @return
     */
    public VeCount veCount(List<VeEquipment> veEquipments) {
        VeCount veCount = new VeCount();
        try {
            for (VeEquipment ve: veEquipments) {
                if(ve.getVeRunstus()==1){
                    for (VeEquipmentRunstus veEquipmentRunstus: ve.getVeEquipmentRunstus()) {
                        if(veEquipmentRunstus.getRunendTime() == null){
                            Date date = new Date();
                            long strTime = veEquipmentRunstus.getRunstartTime().getTime();
                            long currentTime = date.getTime();
                            ve.setVelong(TimeDifference(strTime,currentTime));
                        }
                    }
                }else {
                    ve.setVelong("该设备未工作!");
                }
                if(ve.getVeSecuritystus() == 0){
                    veCount.setSecurityNum(veCount.getSecurityNum()+1);
                }else if(ve.getVeSecuritystus() == 1){
                    veCount.setExceptionNum(veCount.getExceptionNum()+1);
                }else{
                    veCount.setFaultNum(veCount.getFaultNum()+1);
                }
                if(ve.getVeRunstus() == 0){
                    veCount.setEndNum(veCount.getEndNum()+1);
                }else {
                    veCount.setRunNum(veCount.getRunNum()+1);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return veCount;
    }

    public String TimeDifference(long start, long end) {
        long between = end - start;
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        String veLong = null;
//        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        if(day>0) {
            veLong = day + "天" + hour + "小时" + min + "分";
        }else if(hour>0){
            veLong = hour+"小时"+min+"分";
        }else if(min>0){
            veLong = min+"分";
        }else{
            veLong = "设备开启中";
        }
        return veLong;
    }

    public List<VeEquipment> getVeAll(Page<VeEquipment> page) {
        return veEquipmentMapper.selectVeAll(page);
    }

    public List<VeEquipment> getVeAllByType(Page<VeEquipment> page, Integer veType) {
        return veEquipmentMapper.selectVeAllByType(page,veType);
    }

    public List<VeEquipment> getVeList(Page<VeEquipment> page, String typeid, int addressid,
                                 int veStatus, String beforeDate, String afterDate,String[] veNames) {
        List<String> typeids = new ArrayList<>();
        if(!typeid.equals("0") && typeid != null){
            for(int i = 0;i < typeid.length();i++){
                String type = new String();
                if(i == typeid.length()){
                    type = typeid.substring(i);
                }else {
                    type = typeid.substring(i, i + 1);
                }
                typeids.add(type);
            }
        }else{
            typeids = null;
        }
        return veEquipmentMapper.getVeList(page,typeids,addressid,veStatus,beforeDate,afterDate,veNames);
    }
}
