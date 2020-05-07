package main;

import bean.Config;
import job.FileScanningJob;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.impl.DomainFileListenerServiceImpl;
import service.impl.IpFileListenerServiceImpl;
import service.impl.LoopholeFileListenerServiceImpl;
import util.FileUtils;
import util.LogUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName 程序主入口
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class ProcessMain {

    public static void main(String[] args) {

        LogUtil.info("程序开始启动！");

        //初始化加载dns服务器Ip
        FileUtils.getDnsRootServerIp();

        //ip  解析任务开启
        FileScanningJob.startJob(TimeUnit.SECONDS.toMillis(1), Config.DNS_SERVER_FILE_PATH, Config.DNS_SERVER_FILE_TYPE, new IpFileListenerServiceImpl());

        //domain 解析任务开启
        FileScanningJob.startJob(TimeUnit.SECONDS.toMillis(1), Config.DOMAIN_TRACE_FILE_PATH, Config.DOMAIN_TRACE_FILE_TYPE, new DomainFileListenerServiceImpl());

        //漏扫  解析任务开启
        FileScanningJob.startJob(TimeUnit.SECONDS.toMillis(1), Config.LOOPHOLE_FILE_PATH, Config.LOOPHOLE_FILE_TYPE, new LoopholeFileListenerServiceImpl());

    }
}
