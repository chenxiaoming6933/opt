package bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * @ClassName dns探测 服务类型详细信息 基础类  提取公共属性
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class DnsServerTypeBasisBean implements Serializable {

    /**
     * 服务器唯一标识
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String serverUnique;

    /**
     * 服务器名称
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String serverName;

    /**
     * 服务器IP地址
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String serverIp;

    /**
     * 服务器主机名称
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String serverHostName;

    /**
     * 开放端口
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String openPort;

    /**
     * 存活状态
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String survivalStatus;

    /**
     * 访问平均时延
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero})
    private Long averageAccessDelay;

    /**
     * 服务器版本
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String serverVersion;

    /**
     * 是否支持DNSSEC
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String isSupportDnssec;

    /**
     * 是否支持IPV6
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String isSupportIpSix;

    /**
     * 是否支持TCP传输
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String isSupportTcp;

    /**
     * 操作系统名称
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String ovServerName;

    public String getServerUnique() {
        return serverUnique;
    }

    public void setServerUnique(String serverUnique) {
        this.serverUnique = serverUnique;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerHostName() {
        return serverHostName;
    }

    public void setServerHostName(String serverHostName) {
        this.serverHostName = serverHostName;
    }

    public String getOpenPort() {
        return openPort;
    }

    public void setOpenPort(String openPort) {
        this.openPort = openPort;
    }

    public String getSurvivalStatus() {
        return survivalStatus;
    }

    public void setSurvivalStatus(String survivalStatus) {
        this.survivalStatus = survivalStatus;
    }

    public Long getAverageAccessDelay() {
        return averageAccessDelay;
    }

    public void setAverageAccessDelay(Long averageAccessDelay) {
        this.averageAccessDelay = averageAccessDelay;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getIsSupportDnssec() {
        return isSupportDnssec;
    }

    public void setIsSupportDnssec(String isSupportDnssec) {
        this.isSupportDnssec = isSupportDnssec;
    }

    public String getIsSupportIpSix() {
        return isSupportIpSix;
    }

    public void setIsSupportIpSix(String isSupportIpSix) {
        this.isSupportIpSix = isSupportIpSix;
    }

    public String getIsSupportTcp() {
        return isSupportTcp;
    }

    public void setIsSupportTcp(String isSupportTcp) {
        this.isSupportTcp = isSupportTcp;
    }

    public String getOvServerName() {
        return ovServerName;
    }

    public void setOvServerName(String ovServerName) {
        this.ovServerName = ovServerName;
    }
}
