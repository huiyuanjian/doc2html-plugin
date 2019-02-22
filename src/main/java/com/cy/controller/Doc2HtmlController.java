package com.cy.controller;

import com.cy.bean.InterfaceResult;
import com.cy.service.Doc2HtmlService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 总控制类
 */
@RestController
@RequestMapping(method = RequestMethod.POST)
public class Doc2HtmlController {

    @Resource
    private Doc2HtmlService doc2HtmlService;

    /**
     * Word文档中代码显示到HTML中
     *
     * @param code     Word文档粘贴到富文本编辑器中的代码
     * @param keywords 屏蔽的关键字
     * @return 返回页面
     */
    @RequestMapping("doc2Html")
    public InterfaceResult doc2Html(String code, String keywords) {
        try {
            return new InterfaceResult(true, "处理完成", doc2HtmlService.doc2Html(code, keywords));
        } catch (Exception e) {
            return new InterfaceResult(false, e.getMessage(), null);
        }
    }
}
