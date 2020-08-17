package com.lq.pwdmanage.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * XML文件操作
 * @author LQ
 * @date 2020/6/10 10:48
 */
public class XMLUtils {

    /**
     * xml文件名
     */
    public static final String XML_NAME = "pwdManage.xml";

    /**
     * 默认读取的文件路径
     */
    public static final String XML_PATH = "classpath:" + XML_NAME;

    /**
     * 获取XML文档
     * @param xmlPath
     * @return
     */
    public static Document getDocument(String xmlPath) {
        if (xmlPath == null) {
            xmlPath = XML_PATH;
        } else if(!xmlPath.endsWith(".xml")) {
            xmlPath += File.separator + XML_NAME;
        }
        FileExists(xmlPath);

        Document document = null;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(ResourceUtils.getURL(xmlPath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return document;
    }

    /**
     * 文件如果不存在，则创建一个文档
     * @param xmlPath
     */
    private static void FileExists(String xmlPath){
        try {
            File file = ResourceUtils.getFile(xmlPath);
            if(!file.exists()){
                Document doc = DocumentHelper.createDocument();
                //增加根节点
                Element fileRoot = doc.addElement("manage");
                //写入xml文件
                writeToXml(doc, xmlPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写出到XML文件
     * @param doc     Document对象
     * @param xmlPath 保存的xml文件路径
     */
    public static void writeToXml(Document doc, String xmlPath) throws Exception {
        if (xmlPath == null) {
            xmlPath = XML_PATH;
        } else if(!xmlPath.endsWith(".xml")) {
            xmlPath += File.separator + XML_NAME;
        }

        XMLWriter writer = null;
        try {
            File file = ResourceUtils.getFile(xmlPath);
            OutputFormat format = OutputFormat.createPrettyPrint();
            //format.setTrimText(false);
            format.setEncoding("UTF-8");

            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

            writer = new XMLWriter(out, format);
            writer.write(doc);
            writer.flush();
        } catch (Exception e) {
            throw  e;
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 去除XML文件的头部描述
     * @param xml xml文件内容
     * @return
     */
    public static String stripXMLHeader(String xml) {
        xml = xml.replaceAll("^<\\?xml.*?\\?>\r?\n?", "");
        return xml;
    }

}

