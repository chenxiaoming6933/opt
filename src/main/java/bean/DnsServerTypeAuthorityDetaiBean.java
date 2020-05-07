package bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @ClassName dns探测 详情实体 服务器类别为  权威类型
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 * @description
 */
public class DnsServerTypeAuthorityDetaiBean extends DnsServerTypeBasisBean {


    /**
     * 是否开启递归服务
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String isAuthorityServer;

    /**
     * 服务默认ttl
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String serverDefaltTtl;

    /**
     * 默认向主服务器尝试刷新间隔
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero})
    private Long serverDefalutFlushTime;

    /**
     * 尝试刷新失败时重试时间间隔
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero})

    private Long serverDefalutRetryTime;

    /**
     * 从服务器的区域数据到期时间
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero})
    private Long serverRegionalDataTime;


    /**
     * 权威服务器别名
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty})
    private String authoritServerAliasName;

    public String getIsAuthorityServer() {
        return isAuthorityServer;
    }

    public void setIsAuthorityServer(String isAuthorityServer) {
        this.isAuthorityServer = isAuthorityServer;
    }

    public String getServerDefaltTtl() {
        return serverDefaltTtl;
    }

    public void setServerDefaltTtl(String serverDefaltTtl) {
        this.serverDefaltTtl = serverDefaltTtl;
    }

    public Long getServerDefalutFlushTime() {
        return serverDefalutFlushTime;
    }

    public void setServerDefalutFlushTime(Long serverDefalutFlushTime) {
        this.serverDefalutFlushTime = serverDefalutFlushTime;
    }

    public Long getServerDefalutRetryTime() {
        return serverDefalutRetryTime;
    }

    public void setServerDefalutRetryTime(Long serverDefalutRetryTime) {
        this.serverDefalutRetryTime = serverDefalutRetryTime;
    }

    public Long getServerRegionalDataTime() {
        return serverRegionalDataTime;
    }

    public void setServerRegionalDataTime(Long serverRegionalDataTime) {
        this.serverRegionalDataTime = serverRegionalDataTime;
    }

    public String getAuthoritServerAliasName() {
        return authoritServerAliasName;
    }

    public void setAuthoritServerAliasName(String authoritServerAliasName) {
        this.authoritServerAliasName = authoritServerAliasName;
    }
}
