package bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName 常量字典相关实体
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class Config {

    /**
     * ip类型  相关常量
     */

    //ip地址
    public static final String IP_ADDR = "server_address";

    //端口
    public static final String IP_PORT = "server_port";

    //ttl
    public static final String IP_TTL = "ttl";

    //classification
    public static final String IP_CLASS_IFICATION = "classification";

    //成功状态
    public static final String IP_SUCCESS = "success";

    //国家名称
    public static final String IP_COUNTRY_NAME = "country_name";

    //国家代码
    public static final String IP_COUNTRY_CODE = "country_code";

    //省份名称
    public static final String IP_PROVINCE_CODE = "province_name";

    //省份代码
    public static final String IP_PROVINCE_NAME = "province_code";

    //设备编号
    public static final String IP_DEVICE_NO = "device_no";

    //时间字段
    public static final String TIMESTAMP = "timestamp";

    //经度/纬度
    public static final String LONGITUDE_AND_LATITUDE = "longitude_and_latitude";

    //运营商
    public static final String OPERATOR = "operator";

    //服务器类型
    public static final String SERVER_TYPE = "server_type";

    //服务器类型 相关信息
    public static final String SERVER_TYPE_INFO = "server_type_info";

    //安全状态
    public static final String SECURITY_STATUS = "security_status";

    //漏洞详情
    public static final String LEAKAGE_DETAILS = "leakage_details";

    //ipv6
    public static final String IS_IP_SIX = "ipv6";

    //tcp
    public static final String TCP = "tcp";

    //服务器访问平均时延(ms)
    public static final String SERVER_TIMEDELAY = "server_timedelay";

    //是否开启递归服务
    public static final String RECURSION = "recursion";

    /**
     * 服务器类型标识符
     */
    public static final String SERVER_TYPE_ROOT = "NA";

    public static final String SERVER_TYPE_RECURSIVE = "RA";

    public static final String SERVER_TYPE_AUTHORITY = "AA";

    public static final String SERVER_TYPE_UNKNOWN = "M";


    /**
     * domain 域名相关 常量
     */

    //域名dns 级别名称前缀
    public static final String DOMAIN_SUBCLASS_PREFIX = "subclass";

    //域名dns 文件逻辑区分关键字
    public static final String RECEIVED = "Received";

    //域名dns 文件头部区分关键字
    public static final String START = "-----start-----";

    //域名dns 域名节点
    public static final String DOMAIN_NODE_NAME = "node_name";

    //域名dns TTL
    public static final String DOMAIN_TTL = "ttl";

    //域名dns IP协议
    public static final String DOMAIN_IP_PROTOCOL = "protocol";

    //域名dns type  A AAAA
    public static final String DOMAIN_SERVER_NAME = "server_name";

    //域名dns 子域名
    public static final String DOMAIN_SUB_CLASS_NAME = "sub_class_name";

    //域名dns  子域名相关信息
    public static final String DOMAIN_SUB_CLASS_INFO = "sub_class_info";


    /**
     * 漏扫相关字段常量
     */

    //协议
    public static final String LOOPHOLE_PROTOCOL = "protocol";

    //端口
    public static final String LOOPHOLE_PORT = "portid";

    //端口状态
    public static final String LOOPHOLE_PORT_STATUS = "state";

    //TTL
    public static final String LOOPHOLE_TTL = "reason_ttl";

    //服务名称
    public static final String LOOPHOLE_SERVER = "name";

    //漏洞信息
    public static final String LOOPHOLE_VULNERABILITY_INFORMATION = "output";

    //安全状态
    public static final String LOOPHOLE_SECURITY = "security_status";

    /**
     * es  相关常量
     */

    //索引名称
    public static final String INDEX_NAME = "dns_portrait";

    //ip  type 名称
    public static final String IP_TYPE_DOMAIN = "domain";

    //domain type 名称
    public static final String IP_TYPE_NAME = "ip";

    //loophole type 名称
    public static final String IP_TYPE_LOOPHOLE = "loophole";

    //分片
    public static final String NUMBER_OF_SHARDS = "number_of_shards";

    //副本
    public static final String NUMBER_OF_REPLICAS = "number_of_replicas";


    /**
     * 系统配置相关
     */

    //配置文件存放路径
    public static final String ETC_CONFIG_PATH = "F:\\trojanwall\\dns\\etc\\config.properties";

    //文件后缀
    public static final String FILT_TYPE_SUFFIX_XML = "xml";

    //es ip
    public static String ELASTICSEARCH_IP = FileUtils.getConfig("elasticsearch_ip");

    //es 端口
    public static String ELASTICSEARCH_PORT = FileUtils.getConfig("elasticsearch_port");

    //ip 文件存放路径
    public static String DNS_SERVER_FILE_PATH = FileUtils.getConfig("dns_server_file_path");

    //ip 文件类型
    public static String DNS_SERVER_FILE_TYPE = FileUtils.getConfig("dns_server_file_type");

    //domain 文件存放路径
    public static String DOMAIN_TRACE_FILE_PATH = FileUtils.getConfig("domain_trace_file_path");

    //domain  文件类型
    public static String DOMAIN_TRACE_FILE_TYPE = FileUtils.getConfig("domain_trace_file_type");

    //漏洞扫描 文件存放路径
    public static String LOOPHOLE_FILE_PATH = FileUtils.getConfig("loophole_file_path");

    //漏洞扫描  文件类型
    public static String LOOPHOLE_FILE_TYPE = FileUtils.getConfig("loophole_file_type");


    public static String LOOPHOLE_TAR_TMP_FILE_PATH = FileUtils.getConfig("loophole_tar_tmp_file_path");


    //inner_ip.conf
    public static String INNER_IP_FILE_PATH = FileUtils.getConfig("inner_ip_file_path");

    //ipip_conf
    public static String IP_IP_FILE_PATH = FileUtils.getConfig("ipip_file_path");

    //geoip.wydb
    public static String GEOIP_FILE_PATH = FileUtils.getConfig("geoip_file_path");

    //17mon.wydb
    public static String MON_FILE_PATH = FileUtils.getConfig("mon_file_path");

    //dns服务Ip 文件路径
    public static String DNS_SERVER_IP_FILE_PATH = FileUtils.getConfig("dns_server_ip_file_path");

    //dns服务Ip列表
    public static List <String> dnsServerIpList = new ArrayList <>();
}
