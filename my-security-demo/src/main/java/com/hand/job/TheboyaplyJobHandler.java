package com.hand.job;

import com.hand.utils.HttpUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author theboyaply
 * @version V1.0
 * @Date 2019-9-6
 * @description
 */

@JobHandler(value = "theboyaplyJob")
@Component
public class TheboyaplyJobHandler extends IJobHandler {

    private static final Logger log = LoggerFactory.getLogger(TheboyaplyJobHandler.class);

    private static final String MEIZU_WEATHER_URL = "http://aider.meizu.com/app/weather/listWeather";

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("传入参数为：" + param);
        XxlJobLogger.log("传入参数为：" + param);
        String cityStr = param.substring(0, param.indexOf(":"));
        String phoneStr = param.substring(param.indexOf(":") + 1);
        try {
            String data = HttpUtils.sendPost(MEIZU_WEATHER_URL, "cityIds=" + cityStr);
            log.info("向手机号" + phoneStr + "发送信息");
            log.info(data);
        } catch (Exception e) {
            log.error("查询天气失败", e);
            XxlJobLogger.log("查询天气失败：" + e);
        }
        return SUCCESS;
    }
}
