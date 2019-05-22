package com.github.javaparser.printer;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/** @deprecated */
@Deprecated
public class JsonPrinter {
   private final boolean outputNodeType;

   public JsonPrinter(boolean outputNodeType) {
      this.outputNodeType = outputNodeType;
   }

   public String output(Node node) {
      return this.output(node, (String)null, 0);
   }

   public String output(Node node, String name, int level) {
      Utils.assertNotNull(node);
      NodeMetaModel metaModel = node.getMetaModel();
      List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
      List<PropertyMetaModel> attributes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subNodes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subLists = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList).collect(Collectors.toList());
      List<PropertyMetaModel> subEnumSets = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isEnumSet).collect(Collectors.toList());
      List<String> content = new ArrayList();
      if (this.outputNodeType) {
         content.add(q("_type") + ":" + q(metaModel.getTypeName()));
      }

      Iterator var11 = subEnumSets.iterator();

      while(true) {
         PropertyMetaModel subListMetaModel;
         EnumSet value;
         ArrayList listContent;
         Iterator var15;
         do {
            if (!var11.hasNext()) {
               var11 = attributes.iterator();

               while(var11.hasNext()) {
                  subListMetaModel = (PropertyMetaModel)var11.next();
                  content.add(q(subListMetaModel.getName()) + ":" + q(subListMetaModel.getValue(node).toString()));
               }

               var11 = subNodes.iterator();

               while(var11.hasNext()) {
                  subListMetaModel = (PropertyMetaModel)var11.next();
                  Node value = (Node)subListMetaModel.getValue(node);
                  if (value != null) {
                     content.add(this.output(value, subListMetaModel.getName(), level + 1));
                  }
               }

               var11 = subLists.iterator();

               while(true) {
                  NodeList subList;
                  do {
                     do {
                        if (!var11.hasNext()) {
                           if (name == null) {
                              return (String)content.stream().collect(Collectors.joining(",", "{", "}"));
                           }

                           return (String)content.stream().collect(Collectors.joining(",", q(name) + ":{", "}"));
                        }

                        subListMetaModel = (PropertyMetaModel)var11.next();
                        subList = (NodeList)subListMetaModel.getValue(node);
                     } while(subList == null);
                  } while(subList.isEmpty());

                  listContent = new ArrayList();
                  var15 = subList.iterator();

                  while(var15.hasNext()) {
                     Node subListNode = (Node)var15.next();
                     listContent.add(this.output(subListNode, (String)null, level + 1));
                  }

                  content.add(listContent.stream().collect(Collectors.joining(",", q(subListMetaModel.getName()) + ":[", "]")));
               }
            }

            subListMetaModel = (PropertyMetaModel)var11.next();
            value = (EnumSet)subListMetaModel.getValue(node);
         } while(value.isEmpty());

         listContent = new ArrayList();
         var15 = value.iterator();

         while(var15.hasNext()) {
            Modifier modifier = (Modifier)var15.next();
            listContent.add(q(modifier.asString()));
         }

         content.add(listContent.stream().collect(Collectors.joining(",", q(subListMetaModel.getName()) + ":[", "]")));
      }
   }

   private static String q(String value) {
      return "\"" + value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
   }
}
