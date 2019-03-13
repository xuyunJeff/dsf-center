package com.invech.platform.modules.common.config;


import com.invech.platform.modules.common.dto.ag.AgResponseDto;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;

public class XmlUtil {

	public static AgResponseDto getAginResult(String result)
	{
		Document doc;
		AgResponseDto agResDto=new AgResponseDto();
		try {
			doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			agResDto.setInfo(root.attribute("info").getValue());
			agResDto.setMessage(root.attribute("msg").getValue());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return agResDto;
	}
	   //遍历当前节点下的所有节点  
    public static String listNodes(Element node) {
		//System.out.println("当前节点的名称：" + node.getName());
		//首先获取当前节点的所有属性节点
   /*     List<Attribute> list = node.attributes();  
        //遍历属性节点  
        for(Attribute attribute : list){  
            //System.out.println("属性"+attribute.getName() +":" + attribute.getValue());  
        }  */
		//System.out.println(node.getName());
		//如果当前节点内容不为空，则输出
		if (!(node.getTextTrim().equals(""))) {
			if (node.getName().equals("amount"))
				return node.getText();
		}
		//同时迭代当前节点下面的所有子节点
		//使用递归
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			return listNodes(e);
		}
		return "0.00";
	}
}
