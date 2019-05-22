package com.github.javaparser.printer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class YamlPrinter {
   private static final int NUM_SPACES_FOR_INDENT = 4;
   private final boolean outputNodeType;

   public YamlPrinter(boolean outputNodeType) {
      this.outputNodeType = outputNodeType;
   }

   public String output(Node node) {
      StringBuilder output = new StringBuilder();
      output.append("---");
      this.output(node, "root", 0, output);
      output.append(System.lineSeparator() + "...");
      return output.toString();
   }

   public void output(Node node, String name, int level, StringBuilder builder) {
      Utils.assertNotNull(node);
      NodeMetaModel metaModel = node.getMetaModel();
      List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
      List<PropertyMetaModel> attributes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subNodes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subLists = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList).collect(Collectors.toList());
      if (this.outputNodeType) {
         builder.append(System.lineSeparator() + this.indent(level) + name + "(Type=" + metaModel.getTypeName() + "): ");
      } else {
         builder.append(System.lineSeparator() + this.indent(level) + name + ": ");
      }

      ++level;
      Iterator var10 = attributes.iterator();

      PropertyMetaModel sl;
      while(var10.hasNext()) {
         sl = (PropertyMetaModel)var10.next();
         builder.append(System.lineSeparator() + this.indent(level) + sl.getName() + ": " + this.escapeValue(sl.getValue(node).toString()));
      }

      var10 = subNodes.iterator();

      while(var10.hasNext()) {
         sl = (PropertyMetaModel)var10.next();
         Node nd = (Node)sl.getValue(node);
         if (nd != null) {
            this.output(nd, sl.getName(), level, builder);
         }
      }

      var10 = subLists.iterator();

      while(true) {
         NodeList nl;
         do {
            do {
               if (!var10.hasNext()) {
                  return;
               }

               sl = (PropertyMetaModel)var10.next();
               nl = (NodeList)sl.getValue(node);
            } while(nl == null);
         } while(!nl.isNonEmpty());

         builder.append(System.lineSeparator() + this.indent(level) + sl.getName() + ": ");
         String slName = sl.getName();
         slName = slName.endsWith("s") ? slName.substring(0, sl.getName().length() - 1) : slName;
         Iterator var14 = nl.iterator();

         while(var14.hasNext()) {
            Node nd = (Node)var14.next();
            this.output(nd, "- " + slName, level + 1, builder);
         }
      }
   }

   private String indent(int level) {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < level; ++i) {
         for(int j = 0; j < 4; ++j) {
            sb.append(" ");
         }
      }

      return sb.toString();
   }

   private String escapeValue(String value) {
      return "\"" + value.replace("\\", "\\\\").replaceAll("\"", "\\\\\"").replace("\n", "\\n").replace("\t", "\\t") + "\"";
   }
}
