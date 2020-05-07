package bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * @ClassName dns探测 详情实体 服务器类别为 root根
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 * @description
 */
public class DnsServerTypeRootDetaiBean extends  DnsServerTypeBasisBean{


    /**
     * 是否为镜像服务器
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty})
    private String isGhoServer;

    public String getIsGhoServer() {
        return isGhoServer;
    }

    public void setIsGhoServer(String isGhoServer) {
        this.isGhoServer = isGhoServer;
    }

    @Override
    public String toString() {
        return "DnsServerTypeRootDetaiBean{" +
                "isGhoServer='" + isGhoServer + '\'' +
                '}';
    }
}
