package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.mapper.VeInfoMapper;
import cn.gov.zunyi.video.model.VeCount;
import cn.gov.zunyi.video.model.VeInfo;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.velocity.util.ArrayListWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class VeInfoService extends ServiceImpl<VeInfoMapper,VeInfo> {

    /**
     * 统计视频设备各类数量
     * @param veInfos
     * @return
     */
    public VeCount veCount(List<VeInfo> veInfos) {
        VeCount veCount = new VeCount();
        for (VeInfo ve:veInfos ) {
            if(ve.getVeRunstus()==1){
                Date date = new Date();
                long strTime = ve.getVeStrtime().getTime();
                long currentTime = date.getTime();
                ve.setVelong(TimeDifference(strTime,currentTime));
            }else if(ve.getVeEndtime() != null){
                long strTime = ve.getVeStrtime().getTime();
                long endTime = ve.getVeEndtime().getTime();
                ve.setVelong(TimeDifference(strTime,endTime));
            }else {
                ve.setVelong("该设备暂未运营!");
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

    /**
     * 读取某个文件夹下的所有文件
     */
    public List<Map<String,Object>> readfile(String filepath) throws FileNotFoundException, IOException {
        List<Map<String,Object>> mapList = new ArrayList<>();

        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("path=" + file.getPath());
                System.out.println("absolutepath=" + file.getAbsolutePath());
                System.out.println("name=" + file.getName());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    Map<String,Object> map = new HashMap<>();
                    if (!readfile.isDirectory()) {
                        if(readfile.getName().contains("，")){
                            String[] names = readfile.getName().split("，");
                            map.put("pic","./static/pic/"+names[2].trim());
                            map.put("name",names[0].trim());
                            if(readfile.getName().contains("党")) {
                                map.put("job",names[1].substring(names[1].indexOf("党")));
                            }else{
                                map.put("job",names[1].trim());
                            }
                            map.put("organization","班子成员");
                            map.put("phone",names[2].substring(0,names[2].indexOf(".")));
                            mapList.add(i,map);
                        }else if(readfile.getName().contains("+")){
                            String[] names = readfile.getName().split("\\+");
                            map.put("pic","./static/pic/"+names[2].trim());
                            map.put("name",names[0].trim());
                            if(readfile.getName().contains("党")) {
                                map.put("job", names[1].substring(names[1].indexOf("党")));
                            }else{
                                map.put("job",names[1].trim());
                            }
                            map.put("organization","班子成员");
                            map.put("phone",names[2].substring(0,names[2].indexOf(".")));
                            mapList.add(i,map);
                        }else if(readfile.getName().contains(":") ||readfile.getName().contains("：")){
                            String[] names = null;
                            if(readfile.getName().contains(":")){
                                names = readfile.getName().split(":");
                            }else{
                                names = readfile.getName().split("：");
                            }
                            map.put("pic","./static/pic/"+names[1].substring(names[1].indexOf("1")).trim());
                            map.put("name",names[1].substring(0,names[1].indexOf("1")).trim());
                            map.put("job",names[0].trim());
                            map.put("organization","班子成员");
                            map.put("phone",names[1].substring(names[1].indexOf("1"),names[1].indexOf(".")));
                            mapList.add(i,map);
                        }else if(readfile.getName().contains("蓝底")){
                            String names = readfile.getName();
                            map.put("pic","./static/pic/"+names.trim());
                            map.put("name",names.substring(0,3));
                            map.put("job","");
                            map.put("organization", "班子成员");
                            map.put("phone","");
                            mapList.add(map);
                        }else if(readfile.getName().contains("（")){
                            String names = readfile.getName();
                            map.put("pic","./static/pic/"+names);
                            map.put("name",names.substring(0,names.indexOf("（")).trim());
                            map.put("job",names.substring(names.indexOf("（")+1,names.indexOf("）")).trim());
                            map.put("organization", "班子成员");
                            map.put("phone","");
                            mapList.add(map);
                        }else{
                            String names = readfile.getName();
                            if(names.contains("1")){
                                map.put("pic", "./static/pic/" + names.substring(names.indexOf("1")));
                                map.put("phone", names.substring(names.indexOf("1"), names.indexOf(".")).trim());
                            }else{
                                map.put("pic", names);
                                map.put("phone", "");
                            }
                            map.put("name", names.substring(0, 3).trim());
                            if(names.contains("党")) {
                                map.put("job", names.substring(names.indexOf("党"), names.indexOf("1")));
                            }else if(names.contains("区")){
                                map.put("job",names.substring(names.indexOf("区")+1,names.indexOf("1")));
                            }else if(names.contains("镇")){
                                map.put("job",names.substring(names.indexOf("镇")+1,names.indexOf("1")));
                            }else if(names.contains("村")){
                                map.put("job",names.substring(names.indexOf("村")+1,names.indexOf("1")));
                            }else if(names.contains("居")){
                                map.put("job",names.substring(names.indexOf("居")+1,names.indexOf("1")));
                            }else{
                                if(names.contains("1")) {
                                    map.put("job", names.substring(3, names.indexOf("1")).trim());
                                }else{
                                    map.put("job",names.substring(3,names.indexOf(".")).trim());
                                }
                            }
                            map.put("organization", "班子成员");
                            mapList.add(i, map);
                        }
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }


        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return mapList;
    }
}
