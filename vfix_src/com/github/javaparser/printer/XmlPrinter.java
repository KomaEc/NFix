package com.github.javaparser.printer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class XmlPrinter {
   private final boolean outputNodeType;

   public XmlPrinter(boolean outputNodeType) {
      this.outputNodeType = outputNodeType;
   }

   public String output(Node node) {
      StringBuilder output = new StringBuilder();
      this.output(node, "root", 0, output);
      return output.toString();
   }

   public void output(Node node, String name, int level, StringBuilder builder) {
      Utils.assertNotNull(node);
      NodeMetaModel metaModel = node.getMetaModel();
      List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
      List<PropertyMetaModel> attributes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subNodes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subLists = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList).collect(Collectors.toList());
      builder.append("<").append(name);
      if (this.outputNodeType) {
         builder.append(attribute("type", metaModel.getTypeName()));
      }

      Iterator var10 = attributes.iterator();

      PropertyMetaModel subListMetaModel;
      while(var10.hasNext()) {
         subListMetaModel = (PropertyMetaModel)var10.next();
         builder.append(attribute(subListMetaModel.getName(), subListMetaModel.getValue(node).toString()));
      }

      builder.append(">");
      var10 = subNodes.iterator();

      while(var10.hasNext()) {
         subListMetaModel = (PropertyMetaModel)var10.next();
         Node value = (Node)subListMetaModel.getValue(node);
         if (value != null) {
            this.output(value, subListMetaModel.getName(), level + 1, builder);
         }
      }

      var10 = subLists.iterator();

      while(true) {
         NodeList subList;
         do {
            do {
               if (!var10.hasNext()) {
                  builder.append(close(name));
                  return;
               }

               subListMetaModel = (PropertyMetaModel)var10.next();
               subList = (NodeList)subListMetaModel.getValue(node);
            } while(subList == null);
         } while(subList.isEmpty());

         String listName = subListMetaModel.getName();
         builder.append("<").append(listName).append(">");
         String singular = listName.substring(0, listName.length() - 1);
         Iterator var15 = subList.iterator();

         while(var15.hasNext()) {
            Node subListNode = (Node)var15.next();
            this.output(subListNode, singular, level + 1, builder);
         }

         builder.append(close(listName));
      }
   }

   private static String close(String name) {
      return "</" + name + ">";
   }

   private static String attribute(String name, String value) {
      return " " + name + "='" + value + "'";
   }
}
