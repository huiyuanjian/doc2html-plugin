package com.cy.service;

import com.cy.utils.MimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
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
        code = dealHtmlFormat(code);
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
                for (String s : keywords.split(",")) {
                    String[] words = s.split("=");
                    code = code.replaceAll(words[0], words[1]);
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
        Elements img = document.select(imgTagName.getKey());// 获取页面上规定类型的图片
        for (Element element : img) {
            String src = element.attr("src"); // 获取图片的地址
            if (src.contains("file:///")) {
                File file = new File(src.substring(8)); // 裁剪掉前面的file:///
                element.tagName("img"); // 更换标签名字
                element.attr("src", image2Base64(file)); // Base64图片
                element.attr("style", "max-width: 100%;"); // 处理图片的大小
            }
        }
    }

    /**
     * 将图片文件转换成Base64编码
     *
     * @param file 图片文件
     * @return Base64编码后的图片
     */
    private String image2Base64(File file) throws IOException {
        // 判断该文件是否是一张图片
        if (!isImage(file)) throw new IllegalArgumentException(file + " not a picture!");
        // 拼接图片的Base64编码
        return "data:" + MimeUtil.mimeType(file) + ";base64," + new String(Base64.getEncoder().encode(new BufferedInputStream(new FileInputStream(file)).readAllBytes()));
    }

    /**
     * 判断文件是否是一张图片
     *
     * @param file 文件
     * @return 是否是一张图片
     */
    private boolean isImage(File file) {
        try {
            Image image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 处理输入框中HTML的格式
     *
     * @param code HTML
     * @return 处理后的格式
     */
    private String dealHtmlFormat(String code) {
        return code.replaceAll("v:imagedata", "imagedata")
                .replaceAll("text-align:justify;", "text-align:inherit;");
    }
}
