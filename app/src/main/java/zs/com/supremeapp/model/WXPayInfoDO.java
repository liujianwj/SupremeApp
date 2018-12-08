package zs.com.supremeapp.model;


public class WXPayInfoDO extends BaseDO{
    /***
     *
     *  PayReq req = new PayReq();
     *         IWXAPPID=payResultInfoVo.getAppId();
     *         req.appId = payResultInfoVo.getAppId();
     *         req.partnerId = payResultInfoVo.getPartnerId();
     *         req.prepayId = payResultInfoVo.getPrePayId();
     *         req.packageValue = payResultInfoVo.getPackageStr();
     *         req.nonceStr = payResultInfoVo.getNonceStr();
     *         req.timeStamp = payResultInfoVo.getTimeStamp();
     *         req.sign = payResultInfoVo.getPaySign();
     *         api.registerApp(payResultInfoVo.getAppId());
     *
     *
     */
    private String prepayid;
    private String appid;
    private String partnerid;
    private String packagestr;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackagestr() {
        return packagestr;
    }

    public void setPackagestr(String packagestr) {
        this.packagestr = packagestr;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
