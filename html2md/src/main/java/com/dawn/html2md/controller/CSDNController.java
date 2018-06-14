package com.dawn.html2md.controller;

import java.net.URL;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dawn.html2md.csdn2md.CSDN2mdService;
import com.dawn.html2md.util.BaseResp;
import com.dawn.html2md.util.ParamVo;
import com.dawn.html2md.util.ResultStatus;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
@Controller
@RequestMapping("/tool")
public class CSDNController {

    @Resource
    private CSDN2mdService csdn2mdService;

    @RequestMapping(value = "/csdn4url", method = RequestMethod.GET)
    public String csdn4url() {
        return "/csdn4url";
    }

    @RequestMapping(value = "/csdn4url/convert", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<?> csdn4urlConvert(@RequestBody ParamVo paramVo) {
        System.out.println(paramVo);
        try {
            String result = "";
            if (paramVo.getUrl().isEmpty() && paramVo.getHtml().isEmpty()) {
                return new BaseResp(ResultStatus.error_invalid_argument, ResultStatus.error_invalid_argument.getErrorMsg());
            }
            if (!paramVo.getUrl().isEmpty()) {
                result = csdn2mdService.convert(new URL(paramVo.getUrl()));
                return new BaseResp(ResultStatus.SUCCESS, result);
            }
            if(!paramVo.getHtml().isEmpty()){
                result = csdn2mdService.convert(paramVo.getHtml());
                return new BaseResp(ResultStatus.SUCCESS, result);
            }
        } catch (Exception e) {
            return new BaseResp(ResultStatus.http_status_internal_server_error, ResultStatus.http_status_internal_server_error.getErrorMsg());
        }
        return null;
    }
    
    @RequestMapping(value = "/csdn4author", method = RequestMethod.GET)
    public String csdn4author() {
        return "/csdn4author";
    }

    @RequestMapping(value = "/csdn4author/convert", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<?> csdn4authorConvert(@RequestBody ParamVo paramVo) {
        System.out.println(paramVo);
        try {
            String result = "";
            if (paramVo.getUrl().isEmpty() && paramVo.getHtml().isEmpty()) {
                return new BaseResp(ResultStatus.error_invalid_argument, ResultStatus.error_invalid_argument.getErrorMsg());
            }
            if (paramVo.getUrl().isEmpty()) {
                return new BaseResp(ResultStatus.error_invalid_argument, ResultStatus.error_invalid_argument.getErrorMsg());
            }
            if(paramVo.getHtml().isEmpty()){
                return new BaseResp(ResultStatus.error_invalid_argument, ResultStatus.error_invalid_argument.getErrorMsg());
            }
            csdn2mdService.convertAllBlogByUserNames(paramVo.getUrl(), paramVo.getHtml());
            
        } catch (Exception e) {
            return new BaseResp(ResultStatus.http_status_internal_server_error, ResultStatus.http_status_internal_server_error.getErrorMsg());
        }
        return new BaseResp(ResultStatus.SUCCESS, "转换成功，输出地址：" + paramVo.getHtml());
    }
    
}
