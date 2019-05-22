package soot.xml;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.LabeledUnitPrinter;
import soot.Local;
import soot.Main;
import soot.Modifier;
import soot.NormalUnitPrinter;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.IdentityStmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.LiveLocals;
import soot.toolkits.scalar.SimpleLiveLocals;
import soot.util.Chain;

public class XMLPrinter {
   private static final Logger logger = LoggerFactory.getLogger(XMLPrinter.class);
   public static final String xmlHeader = "<?xml version=\"1.0\" ?>\n";
   public static final String dtdHeader = "<!DOCTYPE jil SYSTEM \"http://www.sable.mcgill.ca/~flynn/jil/jil10.dtd\">\n";
   public XMLRoot root;
   private XMLNode xmlNode = null;

   public String toString() {
      if (this.root != null) {
         return this.root.toString();
      } else {
         throw new RuntimeException("Error generating XML!");
      }
   }

   public XMLNode addElement(String name) {
      return this.addElement(name, "", "", "");
   }

   public XMLNode addElement(String name, String value) {
      return this.addElement(name, value, "", "");
   }

   public XMLNode addElement(String name, String value, String[] attributes) {
      return this.addElement(name, value, (String[])attributes, (String[])null);
   }

   public XMLNode addElement(String name, String value, String attribute, String attributeValue) {
      return this.addElement(name, value, new String[]{attribute}, new String[]{attributeValue});
   }

   public XMLNode addElement(String name, String value, String[] attributes, String[] values) {
      return this.root.addElement(name, value, attributes, values);
   }

   public XMLPrinter(Singletons.Global g) {
   }

   public static XMLPrinter v() {
      return G.v().soot_xml_XMLPrinter();
   }

   public XMLNode setXMLNode(XMLNode node) {
      return this.xmlNode = node;
   }

   private void printStatementsInBody(Body body, PrintWriter out) {
      LabeledUnitPrinter up = new NormalUnitPrinter(body);
      Map<Unit, String> stmtToName = up.labels();
      Chain<Unit> units = body.getUnits();
      ExceptionalUnitGraph exceptionalUnitGraph = new ExceptionalUnitGraph(body);
      LiveLocals sll = new SimpleLiveLocals(exceptionalUnitGraph);
      String cleanMethodName = this.cleanMethod(body.getMethod().getName());
      Iterator<Unit> unitIt = units.iterator();
      Unit currentStmt = null;
      String currentLabel = "default";
      long statementCount = 0L;
      long labelCount = 0L;
      long labelID = 0L;
      Vector<String> useList = new Vector();
      Vector<Vector<Long>> useDataList = new Vector();
      Vector<String> defList = new Vector();
      Vector<Vector<Long>> defDataList = new Vector();
      Vector<Vector<String>> paramData = new Vector();
      Vector<XMLPrinter.XMLLabel> xmlLabelsList = new Vector();
      long maxStmtCount = 0L;
      XMLNode methodNode = this.xmlNode.addChild("method", new String[]{"name", "returntype", "class"}, new String[]{cleanMethodName, body.getMethod().getReturnType().toString(), body.getMethod().getDeclaringClass().getName().toString()});
      String declarationStr = body.getMethod().getDeclaration().toString().trim();
      methodNode.addChild("declaration", this.toCDATA(declarationStr), new String[]{"length"}, new String[]{declarationStr.length() + ""});
      XMLNode parametersNode = methodNode.addChild("parameters", new String[]{"method"}, new String[]{cleanMethodName});
      XMLNode localsNode = methodNode.addChild("locals");
      XMLNode labelsNode = methodNode.addChild("labels");
      XMLNode stmtsNode = methodNode.addChild("statements");
      XMLPrinter.XMLLabel xmlLabel = new XMLPrinter.XMLLabel(labelCount, cleanMethodName, currentLabel);
      labelsNode.addChild("label", new String[]{"id", "name", "method"}, new String[]{labelCount++ + "", currentLabel, cleanMethodName});

      XMLNode sootstmtNode;
      int useCount;
      int paramIndex;
      while(unitIt.hasNext()) {
         currentStmt = (Unit)unitIt.next();
         if (stmtToName.containsKey(currentStmt)) {
            currentLabel = ((String)stmtToName.get(currentStmt)).toString();
            xmlLabel.stmtCount = labelID;
            xmlLabel.stmtPercentage = (new Float((double)(new Float((float)labelID) / (float)(new Float((float)units.size())).intValue()) * 100.0D)).longValue();
            if (xmlLabel.stmtPercentage > maxStmtCount) {
               maxStmtCount = xmlLabel.stmtPercentage;
            }

            xmlLabelsList.addElement(xmlLabel);
            xmlLabel = new XMLPrinter.XMLLabel(labelCount, cleanMethodName, currentLabel);
            labelsNode.addChild("label", new String[]{"id", "name", "method"}, new String[]{labelCount + "", currentLabel, cleanMethodName});
            ++labelCount;
            labelID = 0L;
         }

         XMLNode stmtNode = stmtsNode.addChild("statement", new String[]{"id", "label", "method", "labelid"}, new String[]{statementCount + "", currentLabel, cleanMethodName, labelID + ""});
         sootstmtNode = stmtNode.addChild("soot_statement", new String[]{"branches", "fallsthrough"}, new String[]{this.boolToString(currentStmt.branches()), this.boolToString(currentStmt.fallsThrough())});
         int j = 0;
         Iterator boxIt = currentStmt.getUseBoxes().iterator();

         ValueBox box;
         String local;
         Vector tempVector;
         while(boxIt.hasNext()) {
            box = (ValueBox)boxIt.next();
            if (box.getValue() instanceof Local) {
               local = this.cleanLocal(((Local)box.getValue()).toString());
               sootstmtNode.addChild("uses", new String[]{"id", "local", "method"}, new String[]{j + "", local, cleanMethodName});
               ++j;
               tempVector = null;
               useCount = useList.indexOf(local);
               if (useCount == -1) {
                  useDataList.addElement(tempVector);
                  useList.addElement(local);
                  useCount = useList.indexOf(local);
               }

               if (useDataList.size() > useCount) {
                  tempVector = (Vector)useDataList.elementAt(useCount);
                  if (tempVector == null) {
                     tempVector = new Vector();
                  }

                  tempVector.addElement(new Long(statementCount));
                  useDataList.setElementAt(tempVector, useCount);
               }
            }
         }

         j = 0;
         boxIt = currentStmt.getDefBoxes().iterator();

         while(boxIt.hasNext()) {
            box = (ValueBox)boxIt.next();
            if (box.getValue() instanceof Local) {
               local = this.cleanLocal(((Local)box.getValue()).toString());
               sootstmtNode.addChild("defines", new String[]{"id", "local", "method"}, new String[]{j + "", local, cleanMethodName});
               ++j;
               tempVector = null;
               useCount = defList.indexOf(local);
               if (useCount == -1) {
                  defDataList.addElement(tempVector);
                  defList.addElement(local);
                  useCount = defList.indexOf(local);
               }

               if (defDataList.size() > useCount) {
                  tempVector = (Vector)defDataList.elementAt(useCount);
                  if (tempVector == null) {
                     tempVector = new Vector();
                  }

                  tempVector.addElement(new Long(statementCount));
                  defDataList.setElementAt(tempVector, useCount);
               }
            }
         }

         List<Local> liveLocalsIn = sll.getLiveLocalsBefore(currentStmt);
         List<Local> liveLocalsOut = sll.getLiveLocalsAfter(currentStmt);
         XMLNode livevarsNode = sootstmtNode.addChild("livevariables", new String[]{"incount", "outcount"}, new String[]{liveLocalsIn.size() + "", liveLocalsOut.size() + ""});

         for(useCount = 0; useCount < liveLocalsIn.size(); ++useCount) {
            livevarsNode.addChild("in", new String[]{"id", "local", "method"}, new String[]{useCount + "", this.cleanLocal(((Local)liveLocalsIn.get(useCount)).toString()), cleanMethodName});
         }

         for(useCount = 0; useCount < liveLocalsOut.size(); ++useCount) {
            livevarsNode.addChild("out", new String[]{"id", "local", "method"}, new String[]{useCount + "", this.cleanLocal(((Local)liveLocalsOut.get(useCount)).toString()), cleanMethodName});
         }

         for(useCount = 0; useCount < body.getMethod().getParameterTypes().size(); ++useCount) {
            Vector<String> tempVec = new Vector();
            paramData.addElement(tempVec);
         }

         currentStmt.toString(up);
         String jimpleStr = up.toString().trim();
         if (currentStmt instanceof IdentityStmt && jimpleStr.indexOf("@parameter") != -1) {
            String tempStr = jimpleStr.substring(jimpleStr.indexOf("@parameter") + 10);
            if (tempStr.indexOf(":") != -1) {
               tempStr = tempStr.substring(0, tempStr.indexOf(":")).trim();
            }

            if (tempStr.indexOf(" ") != -1) {
               tempStr = tempStr.substring(0, tempStr.indexOf(" ")).trim();
            }

            paramIndex = new Integer(tempStr);
            Vector<String> tempVec = (Vector)paramData.elementAt(paramIndex);
            if (tempVec != null) {
               tempVec.addElement(Long.toString(statementCount));
            }

            paramData.setElementAt(tempVec, paramIndex);
         }

         sootstmtNode.addChild("jimple", this.toCDATA(jimpleStr), new String[]{"length"}, new String[]{jimpleStr.length() + 1 + ""});
         ++labelID;
         ++statementCount;
      }

      stmtsNode.addAttribute("count", statementCount + "");
      parametersNode.addAttribute("count", body.getMethod().getParameterCount() + "");

      Vector typedLocals;
      for(int i = 0; i < body.getMethod().getParameterTypes().size(); ++i) {
         sootstmtNode = parametersNode.addChild("parameter", new String[]{"id", "type", "method", "name"}, new String[]{i + "", ((Type)body.getMethod().getParameterTypes().get(i)).toString(), cleanMethodName, "_parameter" + i});
         XMLNode sootparamNode = sootstmtNode.addChild("soot_parameter");
         typedLocals = (Vector)paramData.elementAt(i);

         for(int k = 0; k < typedLocals.size(); ++k) {
            sootparamNode.addChild("use", new String[]{"id", "line", "method"}, new String[]{k + "", typedLocals.elementAt(k) + "", cleanMethodName});
         }

         sootparamNode.addAttribute("uses", typedLocals.size() + "");
      }

      xmlLabel.stmtCount = labelID;
      xmlLabel.stmtPercentage = (new Float((double)(new Float((float)labelID) / new Float((float)units.size())) * 100.0D)).longValue();
      if (xmlLabel.stmtPercentage > maxStmtCount) {
         maxStmtCount = xmlLabel.stmtPercentage;
      }

      xmlLabelsList.addElement(xmlLabel);
      Collection<Local> locals = body.getLocals();
      Iterator<Local> localsIterator = locals.iterator();
      Vector<String> localTypes = new Vector();
      typedLocals = new Vector();
      Vector<Integer> typeCounts = new Vector();
      int j = 0;

      XMLNode catchNode;
      int defineCount;
      for(boolean var64 = false; localsIterator.hasNext(); ++j) {
         useCount = 0;
         defineCount = 0;
         Local localData = (Local)localsIterator.next();
         String local = this.cleanLocal(localData.toString());
         String localType = localData.getType().toString();
         if (!localTypes.contains(localType)) {
            localTypes.addElement(localType);
            typedLocals.addElement(new Vector());
            typeCounts.addElement(new Integer(0));
         }

         catchNode = new XMLNode("local", "", new String[]{"id", "method", "name", "type"}, new String[]{j + "", cleanMethodName, local, localType});
         XMLNode sootlocalNode = catchNode.addChild("soot_local");
         int currentType = 0;

         int k;
         for(k = 0; k < localTypes.size(); ++k) {
            if (localType.equalsIgnoreCase((String)localTypes.elementAt(k))) {
               currentType = k;
               Integer tempInt = new Integer((Integer)typeCounts.elementAt(k) + 1);
               typeCounts.setElementAt(tempInt, k);
               break;
            }
         }

         Vector tempVector;
         int i;
         String query;
         for(k = 0; k < useList.size(); ++k) {
            query = (String)useList.elementAt(k);
            if (query.equalsIgnoreCase(local)) {
               tempVector = (Vector)useDataList.elementAt(useList.indexOf(local));

               for(i = 0; i < tempVector.size(); ++i) {
                  sootlocalNode.addChild("use", new String[]{"id", "line", "method"}, new String[]{i + "", ((Long)tempVector.elementAt(i)).toString(), cleanMethodName});
               }

               useCount = tempVector.size();
               break;
            }
         }

         for(k = 0; k < defList.size(); ++k) {
            query = (String)defList.elementAt(k);
            if (query.equalsIgnoreCase(local)) {
               tempVector = (Vector)defDataList.elementAt(defList.indexOf(local));

               for(i = 0; i < tempVector.size(); ++i) {
                  sootlocalNode.addChild("definition", new String[]{"id", "line", "method"}, new String[]{i + "", ((Long)tempVector.elementAt(i)).toString(), cleanMethodName});
               }

               defineCount = tempVector.size();
               break;
            }
         }

         sootlocalNode.addAttribute("uses", useCount + "");
         sootlocalNode.addAttribute("defines", defineCount + "");
         Vector<XMLNode> list = (Vector)typedLocals.elementAt(currentType);
         list.addElement(catchNode);
         typedLocals.setElementAt(list, currentType);
         localsNode.addChild((XMLNode)catchNode.clone());
      }

      localsNode.addAttribute("count", locals.size() + "");
      XMLNode typesNode = localsNode.addChild("types", new String[]{"count"}, new String[]{localTypes.size() + ""});

      for(defineCount = 0; defineCount < localTypes.size(); ++defineCount) {
         String type = (String)localTypes.elementAt(defineCount);
         XMLNode typeNode = typesNode.addChild("type", new String[]{"id", "type", "count"}, new String[]{defineCount + "", type, typeCounts.elementAt(defineCount) + ""});
         Vector<XMLNode> list = (Vector)typedLocals.elementAt(defineCount);

         for(j = 0; j < list.size(); ++j) {
            typeNode.addChild((XMLNode)list.elementAt(j));
         }
      }

      labelsNode.addAttribute("count", labelCount + "");
      XMLNode current = labelsNode.child;

      for(paramIndex = 0; paramIndex < xmlLabelsList.size(); ++paramIndex) {
         XMLPrinter.XMLLabel tempLabel = (XMLPrinter.XMLLabel)xmlLabelsList.elementAt(paramIndex);
         tempLabel.stmtPercentage = (new Float((double)(new Float((float)tempLabel.stmtPercentage) / new Float((float)maxStmtCount)) * 100.0D)).longValue();
         if (current != null) {
            current.addAttribute("stmtcount", tempLabel.stmtCount + "");
            current.addAttribute("stmtpercentage", tempLabel.stmtPercentage + "");
            current = current.next;
         }
      }

      statementCount = 0L;
      XMLNode exceptionsNode = methodNode.addChild("exceptions");
      Iterator<Trap> trapIt = body.getTraps().iterator();
      if (trapIt.hasNext()) {
         while(trapIt.hasNext()) {
            Trap trap = (Trap)trapIt.next();
            catchNode = exceptionsNode.addChild("exception", new String[]{"id", "method", "type"}, new String[]{statementCount++ + "", cleanMethodName, Scene.v().quotedNameOf(trap.getException().getName())});
            catchNode.addChild("begin", new String[]{"label"}, new String[]{((String)stmtToName.get(trap.getBeginUnit())).toString()});
            catchNode.addChild("end", new String[]{"label"}, new String[]{((String)stmtToName.get(trap.getEndUnit())).toString()});
            catchNode.addChild("handler", new String[]{"label"}, new String[]{((String)stmtToName.get(trap.getHandlerUnit())).toString()});
         }
      }

      exceptionsNode.addAttribute("count", exceptionsNode.getNumberOfChildren() + "");
   }

   private String cleanMethod(String str) {
      return str.trim().replace('<', '_').replace('>', '_');
   }

   private String cleanLocal(String str) {
      return str.trim();
   }

   private String toCDATA(String str) {
      str = str.replaceAll("]]>", "]]&gt;");
      return "<![CDATA[" + str + "]]>";
   }

   private String boolToString(boolean bool) {
      return bool ? "true" : "false";
   }

   private void printXMLTo(SootClass cl, PrintWriter out) {
      this.root = new XMLRoot();
      XMLNode xmlRootNode = null;
      XMLNode xmlHistoryNode = null;
      XMLNode xmlClassNode = null;
      XMLNode xmlTempNode = null;
      xmlRootNode = this.root.addElement("jil");
      String cmdlineStr = "";
      String[] var8 = Main.v().cmdLineArgs;
      int var9 = var8.length;

      String name;
      for(int var10 = 0; var10 < var9; ++var10) {
         name = var8[var10];
         cmdlineStr = cmdlineStr + name + " ";
      }

      String dateStr = (new Date()).toString();
      xmlHistoryNode = xmlRootNode.addChild("history");
      xmlHistoryNode.addAttribute("created", dateStr);
      String[] var10002 = new String[]{"version", "command", "timestamp"};
      String[] var10003 = new String[3];
      Main.v();
      var10003[0] = Main.versionString;
      var10003[1] = cmdlineStr.trim();
      var10003[2] = dateStr;
      xmlHistoryNode.addChild("soot", var10002, var10003);
      xmlClassNode = xmlRootNode.addChild("class", new String[]{"name"}, new String[]{Scene.v().quotedNameOf(cl.getName()).toString()});
      if (cl.getPackageName().length() > 0) {
         xmlClassNode.addAttribute("package", cl.getPackageName());
      }

      if (cl.hasSuperclass()) {
         xmlClassNode.addAttribute("extends", Scene.v().quotedNameOf(cl.getSuperclass().getName()).toString());
      }

      xmlTempNode = xmlClassNode.addChild("modifiers");
      StringTokenizer st = new StringTokenizer(Modifier.toString(cl.getModifiers()));

      while(st.hasMoreTokens()) {
         xmlTempNode.addChild("modifier", new String[]{"name"}, new String[]{st.nextToken() + ""});
      }

      xmlTempNode.addAttribute("count", xmlTempNode.getNumberOfChildren() + "");
      xmlTempNode = xmlClassNode.addChild("interfaces", "", new String[]{"count"}, new String[]{cl.getInterfaceCount() + ""});
      Iterator<SootClass> fieldIt = cl.getInterfaces().iterator();
      if (fieldIt.hasNext()) {
         while(fieldIt.hasNext()) {
            xmlTempNode.addChild("implements", "", new String[]{"class"}, new String[]{Scene.v().quotedNameOf(((SootClass)fieldIt.next()).getName()).toString()});
         }
      }

      xmlTempNode = xmlClassNode.addChild("fields", "", new String[]{"count"}, new String[]{cl.getFieldCount() + ""});
      fieldIt = cl.getFields().iterator();
      if (fieldIt.hasNext()) {
         int var17 = 0;

         label63:
         while(true) {
            SootField f;
            do {
               if (!fieldIt.hasNext()) {
                  break label63;
               }

               f = (SootField)fieldIt.next();
            } while(f.isPhantom());

            String type = f.getType().toString();
            name = f.getName().toString();
            XMLNode xmlFieldNode = xmlTempNode.addChild("field", "", new String[]{"id", "name", "type"}, new String[]{var17++ + "", name, type});
            XMLNode xmlModifiersNode = xmlFieldNode.addChild("modifiers");
            StringTokenizer st = new StringTokenizer(Modifier.toString(f.getModifiers()));

            while(st.hasMoreTokens()) {
               xmlModifiersNode.addChild("modifier", new String[]{"name"}, new String[]{st.nextToken() + ""});
            }

            xmlModifiersNode.addAttribute("count", xmlModifiersNode.getNumberOfChildren() + "");
         }
      }

      fieldIt = cl.methodIterator();
      this.setXMLNode(xmlClassNode.addChild("methods", new String[]{"count"}, new String[]{cl.getMethodCount() + ""}));

      while(fieldIt.hasNext()) {
         SootMethod method = (SootMethod)fieldIt.next();
         if (!method.isPhantom() && !Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
            if (!method.hasActiveBody()) {
               throw new RuntimeException("method " + method.getName() + " has no active body!");
            }

            this.printTo(method.getActiveBody(), out);
         }
      }

      out.println(this.toString());
   }

   public void printJimpleStyleTo(SootClass cl, PrintWriter out) {
      this.printXMLTo(cl, out);
   }

   private void printTo(Body b, PrintWriter out) {
      b.validate();
      this.printStatementsInBody(b, out);
   }

   class XMLLabel {
      public long id;
      public String methodName;
      public String label;
      public long stmtCount;
      public long stmtPercentage;

      public XMLLabel(long in_id, String in_methodName, String in_label) {
         this.id = in_id;
         this.methodName = in_methodName;
         this.label = in_label;
      }
   }
}
