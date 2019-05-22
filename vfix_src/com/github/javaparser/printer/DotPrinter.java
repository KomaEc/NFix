package com.github.javaparser.printer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DotPrinter {
   private int nodeCount;
   private final boolean outputNodeType;

   public DotPrinter(boolean outputNodeType) {
      this.outputNodeType = outputNodeType;
   }

   public String output(Node node) {
      this.nodeCount = 0;
      StringBuilder output = new StringBuilder();
      output.append("digraph {");
      this.output(node, (String)null, "root", output);
      output.append(Utils.EOL + "}");
      return output.toString();
   }

   public void output(Node node, String parentNodeName, String name, StringBuilder builder) {
      Utils.assertNotNull(node);
      NodeMetaModel metaModel = node.getMetaModel();
      List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
      List<PropertyMetaModel> attributes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subNodes = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode).filter(PropertyMetaModel::isSingular).collect(Collectors.toList());
      List<PropertyMetaModel> subLists = (List)allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList).collect(Collectors.toList());
      String ndName = this.nextNodeName();
      if (this.outputNodeType) {
         builder.append(Utils.EOL + ndName + " [label=\"" + escape(name) + " (" + metaModel.getTypeName() + ")\"];");
      } else {
         builder.append(Utils.EOL + ndName + " [label=\"" + escape(name) + "\"];");
      }

      if (parentNodeName != null) {
         builder.append(Utils.EOL + parentNodeName + " -> " + ndName + ";");
      }

      Iterator var11 = attributes.iterator();

      PropertyMetaModel sl;
      while(var11.hasNext()) {
         sl = (PropertyMetaModel)var11.next();
         String attrName = this.nextNodeName();
         builder.append(Utils.EOL + attrName + " [label=\"" + escape(sl.getName()) + "='" + escape(sl.getValue(node).toString()) + "'\"];");
         builder.append(Utils.EOL + ndName + " -> " + attrName + ";");
      }

      var11 = subNodes.iterator();

      while(var11.hasNext()) {
         sl = (PropertyMetaModel)var11.next();
         Node nd = (Node)sl.getValue(node);
         if (nd != null) {
            this.output(nd, ndName, sl.getName(), builder);
         }
      }

      var11 = subLists.iterator();

      while(true) {
         NodeList nl;
         do {
            do {
               if (!var11.hasNext()) {
                  return;
               }

               sl = (PropertyMetaModel)var11.next();
               nl = (NodeList)sl.getValue(node);
            } while(nl == null);
         } while(!nl.isNonEmpty());

         String ndLstName = this.nextNodeName();
         builder.append(Utils.EOL + ndLstName + " [label=\"" + escape(sl.getName()) + "\"];");
         builder.append(Utils.EOL + ndName + " -> " + ndLstName + ";");
         String slName = sl.getName().substring(0, sl.getName().length() - 1);
         Iterator var16 = nl.iterator();

         while(var16.hasNext()) {
            Node nd = (Node)var16.next();
            this.output(nd, ndLstName, slName, builder);
         }
      }
   }

   private String nextNodeName() {
      return "n" + this.nodeCount++;
   }

   private static String escape(String value) {
      return value.replace("\"", "\\\"");
   }
}
