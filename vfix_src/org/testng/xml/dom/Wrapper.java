package org.testng.xml.dom;

import java.lang.annotation.Annotation;
import java.util.List;
import org.testng.collections.Lists;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Wrapper {
   private OnElement m_onElement;
   private OnElementList m_onElementList;
   private Tag m_tag;
   private TagContent m_tagContent;
   private Object m_bean;

   public Wrapper(Annotation a, Object bean) {
      this.m_bean = bean;
      if (a instanceof OnElement) {
         this.m_onElement = (OnElement)a;
      } else if (a instanceof OnElementList) {
         this.m_onElementList = (OnElementList)a;
      } else if (a instanceof Tag) {
         this.m_tag = (Tag)a;
      } else {
         if (!(a instanceof TagContent)) {
            throw new RuntimeException("Illegal annotation " + a);
         }

         this.m_tagContent = (TagContent)a;
      }

   }

   public String getTagName() {
      if (this.m_onElement != null) {
         return this.m_onElement.tag();
      } else {
         return this.m_onElementList != null ? this.m_onElementList.tag() : this.m_tag.name();
      }
   }

   public List<Object[]> getParameters(Element element) {
      List<Object[]> allParameters = Lists.newArrayList();
      List result;
      if (this.m_onElement != null) {
         result = Lists.newArrayList();
         String[] arr$ = this.m_onElement.attributes();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String attributeName = arr$[i$];
            result.add(element.getAttribute(attributeName));
         }

         allParameters.add(result.toArray());
      } else if (this.m_tag != null) {
         result = Lists.newArrayList();
         result.add(this.m_bean);
         allParameters.add(result.toArray());
      } else {
         NodeList childNodes = element.getChildNodes();

         for(int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).hasAttributes()) {
               Element item = (Element)childNodes.item(i);
               List<Object> result = Lists.newArrayList();
               String[] arr$ = this.m_onElementList.attributes();
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  String attributeName = arr$[i$];
                  result.add(item.getAttribute(attributeName));
               }

               allParameters.add(result.toArray());
            }
         }
      }

      return allParameters;
   }
}
