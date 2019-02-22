package com.cy.service;

import com.cy.utils.MimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 总业务操作类
 */
@Service
public class Doc2HtmlService {

    /**
     * 输入源代码和要替换的关键词，输出一个处理后的HTML
     *
     * @param code     源代码
     * @param keywords 要替换的关键词
     * @return 处理后的HTML
     */
    public String doc2Html(String code, String keywords) throws IOException {
        // 替换特殊标签，否则Jsoup解析异常
        code = code.replaceAll("v:imagedata", "imagedata");
        // 处理关键词
        code = dealKeyWords(code, keywords);
        // 解析HTML
        Document document = Jsoup.parse(code);
        // 处理IMG标签中的图片
        dealImg(document, ImgTagName.IMG);
        // 处理V:IMAGEDATA标签中的图片
        dealImg(document, ImgTagName.IMAGEDATA);
        // 处理后的结果
        return document.select("body").html();
    }

    /**
     * 处理关键字
     *
     * @param code     源代码
     * @param keywords 关键字文本 格式为 原词=替换词 多个之间使用英文","隔开
     * @return 处理后的文本
     */
    private String dealKeyWords(String code, String keywords) {
        try {
            if (keywords != null && !"".equals(keywords)) {
                String[] split = keywords.split(",");
                for (String s : split) {
                    String[] words = s.split("=");
                    String key = words[0];
                    String value = words[1];
                    code = code.replaceAll(key, value);
                }
            }
            return code;
        } catch (Exception e) {
            throw new IllegalArgumentException("请检查替换词的格式!");
        }
    }

    /**
     * 处理img标签中的图片
     *
     * @param document 整个HTML
     * @throws IOException 如果解析失败或者文件没有找到
     */
    private void dealImg(Document document, ImgTagName imgTagName) throws IOException {
        Elements img = document.select(imgTagName.getKey());
        for (Element element : img) {
            String src = element.attr("src");
            if (src.contains("file:///")) {
                File file = new File(src.substring(8));
                String mimeType = MimeUtil.mimeType(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] encode = Base64.getEncoder().encode(bufferedInputStream.readAllBytes());
                String base64 = "data:" + mimeType + ";base64," + new String(encode);
                element.tagName("img");
                element.attr("src", base64);
                element.attr("style", "max-width: 100%;");
            }
        }
    }
}
