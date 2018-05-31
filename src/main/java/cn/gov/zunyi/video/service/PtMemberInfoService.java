package cn.gov.zunyi.video.service;

import cn.gov.zunyi.video.model.PtMemberInfo;
import cn.gov.zunyi.video.mapper.PtMemberInfoMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PtMemberInfoService extends ServiceImpl<PtMemberInfoMapper,PtMemberInfo> {
    @Autowired
    private PtMemberInfoMapper ptMemberInfoMapper;

    public List<PtMemberInfo> getPartyInfo(String beforeYear, String afterYear) {
        return ptMemberInfoMapper.getPartyInfo(beforeYear,afterYear);
    }

    //获取开始时间到结束时间的(yyyy-mm格式）时间
    public List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    //将每个（yyyy-mm格式)的时间插入到已有的数据中
    public List<PtMemberInfo> getPtsSupplement(List<PtMemberInfo> pts, List<String> list) {
        List<PtMemberInfo> listpt = new ArrayList<>();
        for(int i = 0; i <list.size();i++){  //将yyyy-mm时间转为int yyyy,int mm
                String time = list.get(i);
                String years = time.substring(0,4);
                String months = time.substring(5);
                int year = Integer.parseInt(years);
                int month = Integer.parseInt(months);
                PtMemberInfo pt = new PtMemberInfo();
                pt.setMonth(month);
                pt.setYear(year);
                listpt.add(pt);
        }
        for(PtMemberInfo ptm : listpt){
            boolean result = false;
            //判断数据库获取的数据中是否有该年该月的数据，如果没有，返回false，如果有，返回true
            for(PtMemberInfo ptMemberInfo : pts){
                if(ptm.getMonth() == ptMemberInfo.getMonth() && ptm.getYear() == ptMemberInfo.getYear()){
                    result = true;
                }
            }
            //返回false，将该年该月的时间插入，并将数据补充为0
            if(!result){
                PtMemberInfo pt2 = new PtMemberInfo();
                pt2.setYear(ptm.getYear());
                pt2.setMonth(ptm.getMonth());
                pt2.setLearnNum(0);
                pt2.setWorkNum(0);
                pts.add(pt2);
            }
        }
        //先按年份排序，然后按月份排序
        Collections.sort(pts);
        return pts;
    }

    public Boolean getPartyInfoByTime(String countDate) {
        boolean result = false;
        int year = Integer.parseInt(countDate.substring(0,4));
        int month = 0;
        if(countDate.substring(6,7).equals("-")){
            month = Integer.parseInt(countDate.substring(5,6));
        }else{
            month = Integer.parseInt(countDate.substring(5,7));
        }
        PtMemberInfo pt = ptMemberInfoMapper.getPartyInfoByTime(year,month);
        if(pt == null){
            result = true;
        }
        return result;
    }
}
