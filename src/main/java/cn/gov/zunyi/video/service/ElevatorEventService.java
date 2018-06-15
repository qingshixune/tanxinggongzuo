package cn.gov.zunyi.video.service;


import cn.gov.zunyi.video.mapper.ElevatorEventMapper;
import cn.gov.zunyi.video.model.ElevatorEvent;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElevatorEventService extends ServiceImpl<ElevatorEventMapper,ElevatorEvent>{

    @Autowired
    private ElevatorEventMapper elevatorEventMapper;

    /**
     * 图片上传
     * @param filePath
     * @param img
     */
    public boolean getImgFile(String filePath,MultipartFile img) {
        InputStream in = null;
        FileOutputStream out = null;
        File newFile = null;
        String resume = null;
        String filename = img.getOriginalFilename();
        boolean result = false;
        try {
            if(!img.isEmpty()){
                in = img.getInputStream();
                newFile = new File(filePath,filename);

                newFile.createNewFile();
                out = new FileOutputStream(newFile);

                byte[] b = new byte[1024];
                int length = 0;
                while((length = in.read(b)) != -1){
                    out.write(b, 0, length);
                }
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Page<ElevatorEvent> selectEvent(Page<ElevatorEvent> page, String id, Integer eventStatus, String typeid, String beforeDate, String afterDate) {
        List<String> typeids = new ArrayList<>();
        if(typeid != null && !typeid.equals("0")){
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
        return page.setRecords(elevatorEventMapper.selectEvent(page,id,eventStatus,typeids,beforeDate,afterDate));
    }
}
