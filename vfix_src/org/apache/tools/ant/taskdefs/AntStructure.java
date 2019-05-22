package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;

public class AntStructure extends Task {
   private static final String LINE_SEP = System.getProperty("line.separator");
   private File output;
   private AntStructure.StructurePrinter printer = new AntStructure.DTDPrinter();
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$Reference;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$TaskContainer;
   // $FF: synthetic field
   static Class class$java$lang$Boolean;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$EnumeratedAttribute;

   public void setOutput(File output) {
      this.output = output;
   }

   public void add(AntStructure.StructurePrinter p) {
      this.printer = p;
   }

   public void execute() throws BuildException {
      if (this.output == null) {
         throw new BuildException("output attribute is required", this.getLocation());
      } else {
         PrintWriter out = null;

         try {
            try {
               out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.output), "UTF8"));
            } catch (UnsupportedEncodingException var9) {
               out = new PrintWriter(new FileWriter(this.output));
            }

            this.printer.printHead(out, this.getProject(), this.getProject().getTaskDefinitions(), this.getProject().getDataTypeDefinitions());
            this.printer.printTargetDecl(out);
            Enumeration dataTypes = this.getProject().getDataTypeDefinitions().keys();

            while(dataTypes.hasMoreElements()) {
               String typeName = (String)dataTypes.nextElement();
               this.printer.printElementDecl(out, this.getProject(), typeName, (Class)this.getProject().getDataTypeDefinitions().get(typeName));
            }

            Enumeration tasks = this.getProject().getTaskDefinitions().keys();

            while(tasks.hasMoreElements()) {
               String tName = (String)tasks.nextElement();
               this.printer.printElementDecl(out, this.getProject(), tName, (Class)this.getProject().getTaskDefinitions().get(tName));
            }

            this.printer.printTail(out);
         } catch (IOException var10) {
            throw new BuildException("Error writing " + this.output.getAbsolutePath(), var10, this.getLocation());
         } finally {
            if (out != null) {
               out.close();
            }

         }
      }
   }

   protected boolean isNmtoken(String s) {
      return AntStructure.DTDPrinter.isNmtoken(s);
   }

   protected boolean areNmtokens(String[] s) {
      return AntStructure.DTDPrinter.areNmtokens(s);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private static class DTDPrinter implements AntStructure.StructurePrinter {
      private static final String BOOLEAN = "%boolean;";
      private static final String TASKS = "%tasks;";
      private static final String TYPES = "%types;";
      private Hashtable visited;

      private DTDPrinter() {
         this.visited = new Hashtable();
      }

      public void printTail(PrintWriter out) {
         this.visited.clear();
      }

      public void printHead(PrintWriter out, Project p, Hashtable tasks, Hashtable types) {
         this.printHead(out, tasks.keys(), types.keys());
      }

      private void printHead(PrintWriter out, Enumeration tasks, Enumeration types) {
         out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
         out.println("<!ENTITY % boolean \"(true|false|on|off|yes|no)\">");
         out.print("<!ENTITY % tasks \"");

         boolean first;
         String typeName;
         for(first = true; tasks.hasMoreElements(); out.print(typeName)) {
            typeName = (String)tasks.nextElement();
            if (!first) {
               out.print(" | ");
            } else {
               first = false;
            }
         }

         out.println("\">");
         out.print("<!ENTITY % types \"");

         for(first = true; types.hasMoreElements(); out.print(typeName)) {
            typeName = (String)types.nextElement();
            if (!first) {
               out.print(" | ");
            } else {
               first = false;
            }
         }

         out.println("\">");
         out.println("");
         out.print("<!ELEMENT project (target | ");
         out.print("%tasks;");
         out.print(" | ");
         out.print("%types;");
         out.println(")*>");
         out.println("<!ATTLIST project");
         out.println("          name    CDATA #IMPLIED");
         out.println("          default CDATA #IMPLIED");
         out.println("          basedir CDATA #IMPLIED>");
         out.println("");
      }

      public void printTargetDecl(PrintWriter out) {
         out.print("<!ELEMENT target (");
         out.print("%tasks;");
         out.print(" | ");
         out.print("%types;");
         out.println(")*>");
         out.println("");
         out.println("<!ATTLIST target");
         out.println("          id          ID    #IMPLIED");
         out.println("          name        CDATA #REQUIRED");
         out.println("          if          CDATA #IMPLIED");
         out.println("          unless      CDATA #IMPLIED");
         out.println("          depends     CDATA #IMPLIED");
         out.println("          description CDATA #IMPLIED>");
         out.println("");
      }

      public void printElementDecl(PrintWriter out, Project p, String name, Class element) {
         if (!this.visited.containsKey(name)) {
            this.visited.put(name, "");
            IntrospectionHelper ih = null;

            try {
               ih = IntrospectionHelper.getHelper(p, element);
            } catch (Throwable var14) {
               return;
            }

            StringBuffer sb = new StringBuffer("<!ELEMENT ");
            sb.append(name).append(" ");
            if ((AntStructure.class$org$apache$tools$ant$types$Reference == null ? (AntStructure.class$org$apache$tools$ant$types$Reference = AntStructure.class$("org.apache.tools.ant.types.Reference")) : AntStructure.class$org$apache$tools$ant$types$Reference).equals(element)) {
               sb.append("EMPTY>").append(AntStructure.LINE_SEP);
               sb.append("<!ATTLIST ").append(name);
               sb.append(AntStructure.LINE_SEP).append("          id ID #IMPLIED");
               sb.append(AntStructure.LINE_SEP).append("          refid IDREF #IMPLIED");
               sb.append(">").append(AntStructure.LINE_SEP);
               out.println(sb);
            } else {
               Vector v = new Vector();
               if (ih.supportsCharacters()) {
                  v.addElement("#PCDATA");
               }

               if ((AntStructure.class$org$apache$tools$ant$TaskContainer == null ? (AntStructure.class$org$apache$tools$ant$TaskContainer = AntStructure.class$("org.apache.tools.ant.TaskContainer")) : AntStructure.class$org$apache$tools$ant$TaskContainer).isAssignableFrom(element)) {
                  v.addElement("%tasks;");
               }

               Enumeration e = ih.getNestedElements();

               while(e.hasMoreElements()) {
                  v.addElement(e.nextElement());
               }

               int count;
               int i;
               if (v.isEmpty()) {
                  sb.append("EMPTY");
               } else {
                  sb.append("(");
                  count = v.size();

                  for(i = 0; i < count; ++i) {
                     if (i != 0) {
                        sb.append(" | ");
                     }

                     sb.append(v.elementAt(i));
                  }

                  sb.append(")");
                  if (count > 1 || !v.elementAt(0).equals("#PCDATA")) {
                     sb.append("*");
                  }
               }

               sb.append(">");
               out.println(sb);
               sb = new StringBuffer("<!ATTLIST ");
               sb.append(name);
               sb.append(AntStructure.LINE_SEP).append("          id ID #IMPLIED");
               e = ih.getAttributes();

               while(true) {
                  String attrName;
                  do {
                     if (!e.hasMoreElements()) {
                        sb.append(">").append(AntStructure.LINE_SEP);
                        out.println(sb);
                        count = v.size();

                        for(i = 0; i < count; ++i) {
                           String nestedName = (String)v.elementAt(i);
                           if (!"#PCDATA".equals(nestedName) && !"%tasks;".equals(nestedName) && !"%types;".equals(nestedName)) {
                              this.printElementDecl(out, p, nestedName, ih.getElementType(nestedName));
                           }
                        }

                        return;
                     }

                     attrName = (String)e.nextElement();
                  } while("id".equals(attrName));

                  sb.append(AntStructure.LINE_SEP).append("          ").append(attrName).append(" ");
                  Class type = ih.getAttributeType(attrName);
                  if (!type.equals(AntStructure.class$java$lang$Boolean == null ? (AntStructure.class$java$lang$Boolean = AntStructure.class$("java.lang.Boolean")) : AntStructure.class$java$lang$Boolean) && !type.equals(Boolean.TYPE)) {
                     if ((AntStructure.class$org$apache$tools$ant$types$Reference == null ? (AntStructure.class$org$apache$tools$ant$types$Reference = AntStructure.class$("org.apache.tools.ant.types.Reference")) : AntStructure.class$org$apache$tools$ant$types$Reference).isAssignableFrom(type)) {
                        sb.append("IDREF ");
                     } else if ((AntStructure.class$org$apache$tools$ant$types$EnumeratedAttribute == null ? (AntStructure.class$org$apache$tools$ant$types$EnumeratedAttribute = AntStructure.class$("org.apache.tools.ant.types.EnumeratedAttribute")) : AntStructure.class$org$apache$tools$ant$types$EnumeratedAttribute).isAssignableFrom(type)) {
                        try {
                           EnumeratedAttribute ea = (EnumeratedAttribute)type.newInstance();
                           String[] values = ea.getValues();
                           if (values != null && values.length != 0 && areNmtokens(values)) {
                              sb.append("(");

                              for(int i = 0; i < values.length; ++i) {
                                 if (i != 0) {
                                    sb.append(" | ");
                                 }

                                 sb.append(values[i]);
                              }

                              sb.append(") ");
                           } else {
                              sb.append("CDATA ");
                           }
                        } catch (InstantiationException var15) {
                           sb.append("CDATA ");
                        } catch (IllegalAccessException var16) {
                           sb.append("CDATA ");
                        }
                     } else if (type.getSuperclass() != null && type.getSuperclass().getName().equals("java.lang.Enum")) {
                        try {
                           Object[] values = (Object[])((Object[])type.getMethod("values", (Class[])null).invoke((Object)null, (Object[])null));
                           if (values.length == 0) {
                              sb.append("CDATA ");
                           } else {
                              sb.append('(');

                              for(int i = 0; i < values.length; ++i) {
                                 if (i != 0) {
                                    sb.append(" | ");
                                 }

                                 sb.append(type.getMethod("name", (Class[])null).invoke(values[i], (Object[])null));
                              }

                              sb.append(") ");
                           }
                        } catch (Exception var17) {
                           sb.append("CDATA ");
                        }
                     } else {
                        sb.append("CDATA ");
                     }
                  } else {
                     sb.append("%boolean;").append(" ");
                  }

                  sb.append("#IMPLIED");
               }
            }
         }
      }

      public static final boolean isNmtoken(String s) {
         int length = s.length();

         for(int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '.' && c != '-' && c != '_' && c != ':') {
               return false;
            }
         }

         return true;
      }

      public static final boolean areNmtokens(String[] s) {
         for(int i = 0; i < s.length; ++i) {
            if (!isNmtoken(s[i])) {
               return false;
            }
         }

         return true;
      }

      // $FF: synthetic method
      DTDPrinter(Object x0) {
         this();
      }
   }

   public interface StructurePrinter {
      void printHead(PrintWriter var1, Project var2, Hashtable var3, Hashtable var4);

      void printTargetDecl(PrintWriter var1);

      void printElementDecl(PrintWriter var1, Project var2, String var3, Class var4);

      void printTail(PrintWriter var1);
   }
}
