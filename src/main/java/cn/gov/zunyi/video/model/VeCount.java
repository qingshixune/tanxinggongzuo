package cn.gov.zunyi.video.model;

/**
 * <p>
 * 统计视频设备各类数量的辅助表
 * </p>
 * @author ZTY
 * @since 2018-5-24
 */
public class VeCount {

    /**
     * 设备运行数量
     */
    private int runNum = 0;

    /**
     * 设备关闭数量
     */
    private int endNum = 0;

    /**
     * 设备正常数量
     */
    private int securityNum = 0;

    /**
     * 设备异常数量
     */
    private int exceptionNum = 0;

    /**
     * 设备故障数量
     */
    private int faultNum = 0;

    /**
     * 录像数量
     */
    private int videoNum = 0;

    /**
     * 录像总时长
     */
    private int videoLongs = 0;

    /**
     *设备工作总时长
     */
    private int veLongs = 0;

    public int getVeLongs() {
        return veLongs;
    }

    public void setVeLongs(int veLongs) {
        this.veLongs = veLongs;
    }

    public int getVideoNum() {
        return videoNum;
    }


    public void setVideoNum(int videoNum) {
        this.videoNum = videoNum;
    }

    public int getVideoLongs() {
        return videoLongs;
    }

    public void setVideoLongs(int videoLongs) {
        this.videoLongs = videoLongs;
    }

    public int getRunNum() {
        return runNum;
    }

    public void setRunNum(int runNum) {
        this.runNum = runNum;
    }

    public int getEndNum() {
        return endNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    public int getSecurityNum() {
        return securityNum;
    }

    public void setSecurityNum(int securityNum) {
        this.securityNum = securityNum;
    }

    public int getExceptionNum() {
        return exceptionNum;
    }

    public void setExceptionNum(int exceptionNum) {
        this.exceptionNum = exceptionNum;
    }

    public int getFaultNum() {
        return faultNum;
    }

    public void setFaultNum(int faultNum) {
        this.faultNum = faultNum;
    }

    public VeCount() {
    }

    public VeCount(int runNum, int endNum, int securityNum, int exceptionNum, int faultNum, int videoNum, int videoLongs, int veLongs) {
        this.runNum = runNum;
        this.endNum = endNum;
        this.securityNum = securityNum;
        this.exceptionNum = exceptionNum;
        this.faultNum = faultNum;
        this.videoNum = videoNum;
        this.videoLongs = videoLongs;
        this.veLongs = veLongs;
    }

    @Override
    public String toString() {
        return "VeCount{" +
                "runNum=" + runNum +
                ", endNum=" + endNum +
                ", securityNum=" + securityNum +
                ", exceptionNum=" + exceptionNum +
                ", faultNum=" + faultNum +
                ", videoNum=" + videoNum +
                ", videoLongs=" + videoLongs +
                ", veLongs=" + veLongs +
                '}';
    }
}
