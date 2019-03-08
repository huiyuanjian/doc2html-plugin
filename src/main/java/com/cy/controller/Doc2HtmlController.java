package com.cy.controller;

import com.cy.bean.Result;
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
    public Result doc2Html(String code, String keywords) {
        try {
            return new Result(true, doc2HtmlService.doc2Html(code, keywords));
        } catch (Exception e) {
            return new Result(false, e.getMessage());
        }
    }
}
