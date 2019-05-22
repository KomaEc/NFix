package jasmin;

import jas.AnnotElemValPair;
import jas.AnnotationAttr;
import jas.AnnotationDefaultAttr;
import jas.ArrayElemValPair;
import jas.AsciiCP;
import jas.CP;
import jas.Catchtable;
import jas.ClassCP;
import jas.ClassElemValPair;
import jas.ClassEnv;
import jas.CodeAttr;
import jas.ConstAttr;
import jas.DeprecatedAttr;
import jas.DoubleCP;
import jas.DoubleElemValPair;
import jas.ElemValPair;
import jas.EnclMethAttr;
import jas.EnumElemValPair;
import jas.ExceptAttr;
import jas.FieldCP;
import jas.FloatCP;
import jas.FloatElemValPair;
import jas.GenericAttr;
import jas.IincInsn;
import jas.InnerClassAttr;
import jas.InnerClassSpecAttr;
import jas.Insn;
import jas.IntElemValPair;
import jas.IntegerCP;
import jas.InterfaceCP;
import jas.InvokeDynamicCP;
import jas.InvokeinterfaceInsn;
import jas.Label;
import jas.LineTableAttr;
import jas.LocalVarEntry;
import jas.LocalVarTableAttr;
import jas.LongCP;
import jas.LongElemValPair;
import jas.LookupswitchInsn;
import jas.Method;
import jas.MethodCP;
import jas.MethodHandleCP;
import jas.MultiarrayInsn;
import jas.ParameterVisibilityAnnotationAttr;
import jas.SignatureAttr;
import jas.StringCP;
import jas.StringElemValPair;
import jas.SyntheticAttr;
import jas.TableswitchInsn;
import jas.Var;
import jas.VisibilityAnnotationAttr;
import jas.jasError;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ClassFile {
   String filename;
   ClassEnv class_env;
   String class_name;
   String source_name;
   Scanner scanner;
   String method_name;
   String method_signature;
   short method_access;
   ExceptAttr except_attr;
   Catchtable catch_table;
   LocalVarTableAttr var_table;
   LineTableAttr line_table;
   CodeAttr code;
   InnerClassAttr inner_class_attr;
   Hashtable labels;
   boolean methSynth;
   boolean methDepr;
   String methSigAttr;
   VisibilityAnnotationAttr methAnnotAttrVis;
   VisibilityAnnotationAttr methAnnotAttrInvis;
   ParameterVisibilityAnnotationAttr methParamAnnotAttrVis;
   ParameterVisibilityAnnotationAttr methParamAnnotAttrInvis;
   ElemValPair methAnnotDef;
   int line_label_count;
   int line_num;
   boolean auto_number;
   Vector switch_vec;
   int low_value;
   int high_value;
   int lastInstSize;
   Method currentMethod;
   Var currentField;
   VisibilityAnnotationAttr vis_annot_attr;
   static final String BGN_METHOD = "bgnmethod:";
   static final String END_METHOD = "endmethod:";
   int errors;
   AnnotationAttr currAnn = null;

   public void addSootCodeAttr(String name, String value) {
      this.class_env.addCPItem(new AsciiCP(name));
      this.code.addSootCodeAttr(name, value);
   }

   public void addGenericAttrToMethod(String name, byte[] value) {
      if (this.currentMethod == null) {
         System.err.println("Error: no field in scope to add attribute onto.");
      } else {
         this.class_env.requireJava6();
         this.class_env.addCPItem(new AsciiCP(name));
         this.currentMethod.addGenericAttr(new GenericAttr(name, value));
      }

   }

   public void addGenericAttrToField(String name, byte[] value) {
      if (this.currentField == null) {
         System.err.println("Error: no field in scope to add attribute onto.");
      } else {
         this.class_env.requireJava6();
         this.class_env.addCPItem(new AsciiCP(name));
         this.currentField.addGenericAttr(new GenericAttr(name, value));
      }

   }

   public void addDeprecatedToField() {
      if (this.currentField == null) {
         System.err.println("Error: no field in scope to add deprecated attribute onto");
      } else {
         this.class_env.requireJava6();
         this.currentField.addDeprecatedAttr(new DeprecatedAttr());
      }

   }

   public void addGenericAttrToClass(GenericAttr g) {
      this.class_env.requireJava6();
      this.class_env.addGenericAttr(g);
   }

   void report_error(String msg) {
      System.err.print(this.filename + ":");
      System.err.print(this.scanner.line_num);
      System.err.println(": " + msg + ".");
      if (this.scanner.char_num >= 0) {
         System.err.println(this.scanner.line.toString());

         for(int i = 0; i < this.scanner.char_num; ++i) {
            if (this.scanner.line.charAt(i) == '\t') {
               System.err.print("\t");
            } else {
               System.err.print(" ");
            }
         }

         System.err.println("^");
      }

      ++this.errors;
   }

   void setSource(String name) {
      this.source_name = name;
   }

   void setClass(String name, short acc) {
      this.class_name = name;
      this.class_env.setClass(new ClassCP(name));
      this.class_env.setClassAccess(acc);
   }

   void setSuperClass(String name) {
      this.class_env.setSuperClass(new ClassCP(name));
   }

   void setNoSuperClass() {
      this.class_env.setNoSuperClass();
   }

   void addInterface(String name) {
      this.class_env.addInterface((CP)(new ClassCP(name)));
   }

   void addClassDeprAttr(Object res) {
      if (res != null) {
         this.class_env.requireJava5();
         this.class_env.setClassDepr(new DeprecatedAttr());
      }

   }

   void addClassSigAttr(Object res) {
      if (res != null) {
         String sig = (String)res;
         this.class_env.setClassSigAttr(new SignatureAttr(sig));
         if (sig.contains("<")) {
            this.class_env.requireJava5();
         }
      }

   }

   void addClassAnnotAttrVisible(Object res) {
      if (res != null) {
         this.class_env.requireJava5();
         this.class_env.setClassAnnotAttrVis((VisibilityAnnotationAttr)res);
      }

   }

   void addClassAnnotAttrInvisible(Object res) {
      if (res != null) {
         this.class_env.requireJava5();
         this.class_env.setClassAnnotAttrInvis((VisibilityAnnotationAttr)res);
      }

   }

   void addField(short access, String name, String sig, Object value, Object dep_attr, Object sig_attr, Object vis_annot_attr, Object vis_annot_attr2) {
      this.addField(access, name, sig, value, (String)null, dep_attr, sig_attr, vis_annot_attr, vis_annot_attr2);
   }

   void addField(short access, String name, String sig, Object value, String synth, Object dep_attr, Object sig_attr, Object vis_annot_attr, Object vis_annot_attr2) {
      if (sig.contains("<") || sig_attr != null && ((String)sig_attr).contains("<")) {
         this.class_env.requireJava5();
      }

      if (value == null) {
         if (synth == null) {
            this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), (ConstAttr)null);
         } else {
            this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), (ConstAttr)null, new SyntheticAttr());
            this.class_env.requireJava6();
         }

         if (dep_attr != null) {
            this.class_env.requireJava5();
            this.currentField.addDeprecatedAttr(new DeprecatedAttr());
         }

         if (sig_attr != null) {
            this.currentField.addSignatureAttr(new SignatureAttr((String)sig_attr));
         }

         VisibilityAnnotationAttr attribute;
         if (vis_annot_attr != null) {
            this.class_env.requireJava5();
            attribute = (VisibilityAnnotationAttr)vis_annot_attr;
            if (attribute.getKind().equals("RuntimeVisible")) {
               this.currentField.addVisibilityAnnotationAttrVis(attribute);
            } else {
               this.currentField.addVisibilityAnnotationAttrInvis(attribute);
            }
         }

         if (vis_annot_attr2 != null) {
            this.class_env.requireJava5();
            attribute = (VisibilityAnnotationAttr)vis_annot_attr2;
            if (attribute.getKind().equals("RuntimeVisible")) {
               this.currentField.addVisibilityAnnotationAttrVis(attribute);
            } else {
               this.currentField.addVisibilityAnnotationAttrInvis(attribute);
            }
         }

         this.class_env.addField(this.currentField);
      } else {
         CP cp = null;
         if (!sig.equals("I") && !sig.equals("Z") && !sig.equals("C") && !sig.equals("B") && !sig.equals("S")) {
            if (sig.equals("F")) {
               cp = new FloatCP(((Number)value).floatValue());
            } else if (sig.equals("D")) {
               cp = new DoubleCP(((Number)value).doubleValue());
            } else if (sig.equals("J")) {
               cp = new LongCP(((Number)value).longValue());
            } else if (sig.equals("Ljava/lang/String;")) {
               cp = new StringCP((String)value);
            }
         } else {
            cp = new IntegerCP(((Number)value).intValue());
         }

         if (synth == null) {
            this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), cp == null ? null : new ConstAttr((CP)cp));
         } else {
            this.currentField = new Var(access, new AsciiCP(name), new AsciiCP(sig), cp == null ? null : new ConstAttr((CP)cp), new SyntheticAttr());
            this.class_env.requireJava6();
         }

         if (dep_attr != null) {
            this.class_env.requireJava5();
            this.currentField.addDeprecatedAttr(new DeprecatedAttr());
         }

         if (sig_attr != null) {
            this.currentField.addSignatureAttr(new SignatureAttr((String)sig_attr));
         }

         this.class_env.addField(this.currentField);
      }

   }

   void newMethod(String name, String signature, int access) {
      this.labels = new Hashtable();
      this.method_name = name;
      this.code = null;
      this.except_attr = null;
      this.catch_table = null;
      this.var_table = null;
      this.line_table = null;
      this.line_label_count = 0;
      this.method_signature = signature;
      this.method_access = (short)access;
      this.methSynth = false;
      this.methDepr = false;
      this.methSigAttr = null;
      this.methAnnotAttrVis = null;
      this.methAnnotAttrInvis = null;
      this.methParamAnnotAttrVis = null;
      this.methParamAnnotAttrInvis = null;
      this.methAnnotDef = null;
   }

   void endMethod() throws jasError {
      if (this.code != null) {
         this.plantLabel("endmethod:");
         if (this.catch_table != null) {
            this.code.setCatchtable(this.catch_table);
         }

         if (this.var_table != null) {
            this.code.setLocalVarTable(this.var_table);
         }

         if (this.line_table != null) {
            this.code.setLineTable(this.line_table);
         }

         this.code.setLabelTable(this.labels);
      }

      if (!this.methSynth) {
         this.currentMethod = new Method(this.method_access, new AsciiCP(this.method_name), new AsciiCP(this.method_signature), this.code, this.except_attr);
      } else {
         this.currentMethod = new Method(this.method_access, new AsciiCP(this.method_name), new AsciiCP(this.method_signature), this.code, this.except_attr, new SyntheticAttr());
         this.class_env.requireJava6();
      }

      if (this.methDepr) {
         this.class_env.requireJava5();
         this.currentMethod.addDeprecatedAttr(new DeprecatedAttr());
      }

      if (this.methSigAttr != null) {
         this.currentMethod.addSignatureAttr(new SignatureAttr(this.methSigAttr));
      }

      if (this.methAnnotAttrVis != null) {
         this.class_env.requireJava5();
         this.currentMethod.addVisAnnotationAttr(this.methAnnotAttrVis);
      }

      if (this.methAnnotAttrInvis != null) {
         this.class_env.requireJava5();
         this.currentMethod.addInvisAnnotationAttr(this.methAnnotAttrInvis);
      }

      if (this.methParamAnnotAttrVis != null) {
         this.class_env.requireJava5();
         this.currentMethod.addVisParamAnnotationAttr(this.methParamAnnotAttrVis);
      }

      if (this.methParamAnnotAttrInvis != null) {
         this.class_env.requireJava5();
         this.currentMethod.addInvisParamAnnotationAttr(this.methParamAnnotAttrInvis);
      }

      if (this.methAnnotDef != null) {
         this.class_env.requireJava5();
         this.methAnnotDef.setNoName();
         this.currentMethod.addAnnotationDef(new AnnotationDefaultAttr(this.methAnnotDef));
      }

      this.class_env.addMethod(this.currentMethod);
      this.code = null;
      this.labels = null;
      this.method_name = null;
      this.code = null;
      this.except_attr = null;
      this.catch_table = null;
      this.line_table = null;
      this.var_table = null;
      this.methSynth = false;
      this.methDepr = false;
      this.methSigAttr = null;
      this.methAnnotAttrVis = null;
      this.methParamAnnotAttrVis = null;
      this.methAnnotDef = null;
   }

   void plant(String name) throws jasError {
      InsnInfo insn = InsnInfo.get(name);
      this.autoNumber();
      if (insn.args.equals("")) {
         Insn inst = new Insn(insn.opcode);
         this._getCode().addInsn(inst);
      } else if (!insn.name.equals("wide")) {
         throw new jasError("Missing arguments for instruction " + name);
      }

   }

   void plant(String name, int v1, int v2) throws jasError {
      this.autoNumber();
      if (name.equals("iinc")) {
         Insn inst = new IincInsn(v1, v2);
         this._getCode().addInsn(inst);
      } else {
         throw new jasError("Bad arguments for instruction " + name);
      }
   }

   void plant(String name, int val) throws jasError {
      InsnInfo insn = InsnInfo.get(name);
      CodeAttr code = this._getCode();
      this.autoNumber();
      Insn inst = null;
      if (insn.args.equals("i")) {
         inst = new Insn(insn.opcode, val);
      } else if (insn.args.equals("constant")) {
         inst = new Insn(insn.opcode, new IntegerCP(val));
      } else {
         if (!insn.args.equals("bigconstant")) {
            throw new jasError("Bad arguments for instruction " + name);
         }

         inst = new Insn(insn.opcode, new LongCP((long)val));
      }

      code.addInsn(inst);
   }

   void plant(String name, Number val) throws jasError {
      InsnInfo insn = InsnInfo.get(name);
      CodeAttr code = this._getCode();
      this.autoNumber();
      Insn inst = null;
      if (insn.args.equals("i") && val instanceof Integer) {
         inst = new Insn(insn.opcode, val.intValue());
      } else if (insn.args.equals("constant")) {
         if (!(val instanceof Integer) && !(val instanceof Long)) {
            if (val instanceof Float || val instanceof Double) {
               inst = new Insn(insn.opcode, new FloatCP(val.floatValue()));
            }
         } else {
            inst = new Insn(insn.opcode, new IntegerCP(val.intValue()));
         }
      } else {
         if (!insn.args.equals("bigconstant")) {
            throw new jasError("Bad arguments for instruction " + name);
         }

         if (!(val instanceof Integer) && !(val instanceof Long)) {
            if (val instanceof Float || val instanceof Double) {
               inst = new Insn(insn.opcode, new DoubleCP(val.doubleValue()));
            }
         } else {
            inst = new Insn(insn.opcode, new LongCP(val.longValue()));
         }
      }

      this._getCode().addInsn(inst);
   }

   void plantString(String name, String val) throws jasError {
      InsnInfo insn = InsnInfo.get(name);
      this.autoNumber();
      Insn inst = null;
      if (insn.args.equals("constant")) {
         inst = new Insn(insn.opcode, new StringCP(val));
         this.code.addInsn(inst);
      } else {
         throw new jasError("Bad arguments for instruction " + name);
      }
   }

   void plant(String name, String val, int nargs) throws jasError {
      InsnInfo insn = InsnInfo.get(name);
      CodeAttr code = this._getCode();
      this.autoNumber();
      Insn inst = null;
      if (insn.args.equals("interface")) {
         String[] split = ScannerUtils.splitClassMethodSignature(val);
         inst = new InvokeinterfaceInsn(new InterfaceCP(split[0], split[1], split[2]), nargs);
      } else {
         if (!insn.args.equals("marray")) {
            throw new jasError("Bad arguments for instruction " + name);
         }

         inst = new MultiarrayInsn(new ClassCP(val), nargs);
      }

      code.addInsn((Insn)inst);
   }

   void plant(String name, String val) throws jasError {
      InsnInfo insn = InsnInfo.get(name);
      CodeAttr code = this._getCode();
      this.autoNumber();
      Insn inst = null;
      if (insn.args.equals("method")) {
         String[] split = ScannerUtils.splitClassMethodSignature(val);
         inst = new Insn(insn.opcode, new MethodCP(split[0], split[1], split[2]));
      } else if (insn.args.equals("constant")) {
         inst = new Insn(insn.opcode, new ClassCP(val));
      } else if (insn.args.equals("atype")) {
         int atype = false;
         byte atype;
         if (val.equals("boolean")) {
            atype = 4;
         } else if (val.equals("char")) {
            atype = 5;
         } else if (val.equals("float")) {
            atype = 6;
         } else if (val.equals("double")) {
            atype = 7;
         } else if (val.equals("byte")) {
            atype = 8;
         } else if (val.equals("short")) {
            atype = 9;
         } else if (val.equals("int")) {
            atype = 10;
         } else {
            if (!val.equals("long")) {
               throw new jasError("Bad array type: " + name);
            }

            atype = 11;
         }

         inst = new Insn(insn.opcode, atype);
      } else if (insn.args.equals("label")) {
         inst = new Insn(insn.opcode, this.getLabel(val));
      } else {
         if (!insn.args.equals("class")) {
            throw new jasError("Bad arguments for instruction " + name);
         }

         inst = new Insn(insn.opcode, new ClassCP(val));
      }

      code.addInsn(inst);
   }

   void plant(String name, String v1, String v2) throws jasError {
      InsnInfo info = InsnInfo.get(name);
      CodeAttr code = this._getCode();
      this.autoNumber();
      Insn inst = null;
      if (info.args.equals("field")) {
         String[] split = ScannerUtils.splitClassField(v1);
         inst = new Insn(info.opcode, new FieldCP(split[0], split[1], v2));
         code.addInsn(inst);
      } else {
         throw new jasError("Bad arguments for instruction " + name);
      }
   }

   void plant(String name, String v1, String v2, String v3) throws jasError {
      InsnInfo info = InsnInfo.get(name);
      CodeAttr code = this._getCode();
      this.autoNumber();
      Insn inst = null;
      if (!name.equals("invokedynamic")) {
         throw new jasError("Bad arguments for instruction " + name);
      } else {
         this.class_env.requireJava7();
         String bsmNameAndSig = v3.substring(0, v3.indexOf("Ljava/lang/invoke/CallSite;(") + "Ljava/lang/invoke/CallSite;".length());
         String bsmName = bsmNameAndSig.substring(0, bsmNameAndSig.indexOf("("));
         String bsmClassName = bsmName.substring(0, bsmName.lastIndexOf("/"));
         String bsmMethodName = bsmName.substring(bsmName.lastIndexOf("/") + 1);
         String bsmSig = bsmNameAndSig.substring(bsmNameAndSig.indexOf("("));
         String bsmArgs = v3.substring(v3.indexOf("Ljava/lang/invoke/CallSite;(") + "Ljava/lang/invoke/CallSite;(".length(), v3.length() - 1);
         String[] bsmArgsList = bsmArgs.split(",");
         CP[] argCPs;
         int i;
         if (!bsmArgs.isEmpty()) {
            argCPs = new CP[bsmArgsList.length];

            for(i = 0; i < bsmArgsList.length; ++i) {
               String sig = bsmArgsList[i].substring(1, bsmArgsList[i].indexOf(")"));
               String val = bsmArgsList[i].substring(bsmArgsList[i].indexOf(")") + 1);
               val = this.unescape(val);
               CP cp = null;
               if (!sig.equals("I") && !sig.equals("Z") && !sig.equals("C") && !sig.equals("B") && !sig.equals("S")) {
                  if (sig.equals("F")) {
                     cp = new FloatCP(Float.parseFloat(val));
                  } else if (sig.equals("D")) {
                     cp = new DoubleCP(Double.parseDouble(val));
                  } else if (sig.equals("J")) {
                     cp = new LongCP(Long.parseLong(val));
                  } else if (sig.equals("Ljava/lang/String;")) {
                     cp = new StringCP(val);
                  } else {
                     if (!sig.equals("Ljava/lang/Class;")) {
                        throw new UnsupportedOperationException("static argument type not currently supported: " + sig);
                     }

                     cp = new ClassCP(val);
                  }
               } else {
                  cp = new IntegerCP(Integer.parseInt(val));
               }

               argCPs[i] = (CP)cp;
            }
         } else {
            argCPs = new CP[0];
         }

         i = this.class_env.addBootstrapMethod(new MethodHandleCP(6, bsmClassName, bsmMethodName, bsmSig), argCPs);
         inst = new Insn(info.opcode, new InvokeDynamicCP(bsmClassName, bsmMethodName, bsmSig, v1, v2, i));
         code.addInsn(inst);
      }
   }

   private String unescape(String val) {
      return val.replace("\\comma", ",").replace("\\blank", " ").replace("\\tab", "\t").replace("\\newline", "\n");
   }

   void newLookupswitch() throws jasError {
      this.switch_vec = new Vector();
      this.autoNumber();
   }

   void addLookupswitch(int val, String label) throws jasError {
      this.switch_vec.addElement(new Integer(val));
      this.switch_vec.addElement(this.getLabel(label));
   }

   void endLookupswitch(String deflabel) throws jasError {
      int n = this.switch_vec.size() >> 1;
      int[] offsets = new int[n];
      Label[] labels = new Label[n];
      Enumeration e = this.switch_vec.elements();

      for(int i = 0; e.hasMoreElements(); ++i) {
         offsets[i] = (Integer)e.nextElement();
         labels[i] = (Label)e.nextElement();
      }

      this._getCode().addInsn(new LookupswitchInsn(this.getLabel(deflabel), offsets, labels));
   }

   void newTableswitch(int lowval) throws jasError {
      this.newTableswitch(lowval, -1);
   }

   void newTableswitch(int lowval, int hival) throws jasError {
      this.switch_vec = new Vector();
      this.low_value = lowval;
      this.high_value = hival;
      this.autoNumber();
   }

   void addTableswitch(String label) throws jasError {
      this.switch_vec.addElement(this.getLabel(label));
   }

   void endTableswitch(String deflabel) throws jasError {
      int n = this.switch_vec.size();
      Label[] labels = new Label[n];
      Enumeration e = this.switch_vec.elements();

      for(int i = 0; e.hasMoreElements(); ++i) {
         labels[i] = (Label)e.nextElement();
      }

      if (this.high_value != -1 && this.high_value != this.low_value + n - 1) {
         this.report_error("tableswitch - given incorrect value for <high>");
      }

      this._getCode().addInsn(new TableswitchInsn(this.low_value, this.low_value + n - 1, this.getLabel(deflabel), labels));
   }

   void setLine(int l) {
      this.line_num = l;
   }

   void autoNumber() throws jasError {
      if (this.auto_number) {
         this.addLineInfo(this.line_num);
      }

   }

   Label getLabel(String name) throws jasError {
      if (this.method_name == null) {
         throw new jasError("illegal use of label outside of method definition");
      } else {
         Label lab = (Label)this.labels.get(name);
         if (lab == null) {
            lab = new Label(name);
            this.labels.put(name, lab);
         }

         return lab;
      }
   }

   void plantLabel(String name) throws jasError {
      this._getCode().addInsn(this.getLabel(name));
   }

   void addVar(String startLab, String endLab, String name, String sig, int var_num) throws jasError {
      if (startLab == null) {
         startLab = "bgnmethod:";
      }

      if (endLab == null) {
         endLab = "endmethod:";
      }

      Label slab = this.getLabel(startLab);
      Label elab = this.getLabel(endLab);
      if (this.var_table == null) {
         this.var_table = new LocalVarTableAttr();
      }

      this.var_table.addEntry(new LocalVarEntry(slab, elab, name, sig, var_num));
   }

   void addLineInfo(int line_num) throws jasError {
      String l = "L:" + this.line_label_count++;
      if (this.line_table == null) {
         this.line_table = new LineTableAttr();
      }

      this.plantLabel(l);
      this.line_table.addEntry(this.getLabel(l), line_num);
   }

   void addLine(int line_num) throws jasError {
      if (!this.auto_number) {
         this.addLineInfo(line_num);
      }

   }

   void addThrow(String name) throws jasError {
      if (this.method_name == null) {
         throw new jasError("illegal use of .throw outside of method definition");
      } else {
         if (this.except_attr == null) {
            this.except_attr = new ExceptAttr();
         }

         this.except_attr.addException(new ClassCP(name));
      }
   }

   void addCatch(String name, String start_lab, String end_lab, String branch_lab) throws jasError {
      if (this.method_name == null) {
         throw new jasError("illegal use of .catch outside of method definition");
      } else {
         if (this.catch_table == null) {
            this.catch_table = new Catchtable();
         }

         ClassCP class_cp;
         if (name.equals("all")) {
            class_cp = null;
         } else {
            class_cp = new ClassCP(name);
         }

         this.catch_table.addEntry(this.getLabel(start_lab), this.getLabel(end_lab), this.getLabel(branch_lab), class_cp);
      }
   }

   void setStackSize(short v) throws jasError {
      this._getCode().setStackSize(v);
   }

   void setVarSize(short v) throws jasError {
      this._getCode().setVarSize(v);
   }

   CodeAttr _getCode() throws jasError {
      if (this.method_name == null) {
         throw new jasError("illegal use of instruction outside of method definition");
      } else {
         if (this.code == null) {
            this.code = new CodeAttr();
            this.plantLabel("bgnmethod:");
         }

         return this.code;
      }
   }

   void addInnerClassAttr() {
   }

   void addInnerClassSpec(String inner_class_name, String outer_class_name, String inner_name, short access) {
      if (this.inner_class_attr == null) {
         this.inner_class_attr = new InnerClassAttr();
      }

      this.inner_class_attr.addInnerClassSpec(new InnerClassSpecAttr(inner_class_name, outer_class_name, inner_name, access));
   }

   void endInnerClassAttr() {
      this.class_env.finishInnerClassAttr(this.inner_class_attr);
   }

   void addClassSynthAttr() {
      this.class_env.setClassSynth(true);
   }

   void addMethSynthAttr() {
      this.methSynth = true;
   }

   void addMethDeprAttr() {
      this.methDepr = true;
   }

   void addMethSigAttr(String s) {
      this.methSigAttr = s;
      if (s.contains("<")) {
         this.class_env.requireJava5();
      }

   }

   void addEnclMethAttr(String cls, String meth, String sig) {
      this.class_env.addEnclMethAttr(new EnclMethAttr(cls, meth, sig));
   }

   void addMethAnnotAttrVisible(Object attr) {
      this.methAnnotAttrVis = (VisibilityAnnotationAttr)attr;
   }

   void addMethAnnotAttrInvisible(Object attr) {
      this.methAnnotAttrInvis = (VisibilityAnnotationAttr)attr;
   }

   void addMethParamAnnotAttrVisible(Object attr) {
      this.methParamAnnotAttrVis = (ParameterVisibilityAnnotationAttr)attr;
   }

   void addMethParamAnnotAttrInvisible(Object attr) {
      this.methParamAnnotAttrInvis = (ParameterVisibilityAnnotationAttr)attr;
   }

   void addMethAnnotDefault(Object attr) {
      this.methAnnotDef = (ElemValPair)attr;
   }

   VisibilityAnnotationAttr makeVisibilityAnnotation(Object tval, Object list) {
      return new VisibilityAnnotationAttr((String)tval, (ArrayList)list);
   }

   ParameterVisibilityAnnotationAttr makeParameterVisibilityAnnotation(Object kind, Object list) {
      return new ParameterVisibilityAnnotationAttr((String)kind + "Parameter", (ArrayList)list);
   }

   void endVisibilityAnnotation() {
   }

   ArrayList makeNewAnnotAttrList(Object annot) {
      ArrayList list = new ArrayList();
      list.add(annot);
      return list;
   }

   ArrayList mergeNewAnnotAttr(Object list, Object elem) {
      ((ArrayList)list).add(elem);
      return (ArrayList)list;
   }

   ArrayList makeNewAnnotationList(Object elem) {
      ArrayList list = new ArrayList();
      list.add(elem);
      return list;
   }

   ArrayList mergeNewAnnotation(Object list, Object elem) {
      ((ArrayList)list).add(elem);
      return (ArrayList)list;
   }

   AnnotationAttr makeAnnotation(String type, Object elems) {
      return new AnnotationAttr(type, (ArrayList)elems);
   }

   void endAnnotation() {
      if (this.vis_annot_attr == null) {
         this.vis_annot_attr = new VisibilityAnnotationAttr();
      }

      this.vis_annot_attr.addAnnotation(this.currAnn);
   }

   ArrayList makeNewElemValPairList(Object elem) {
      ArrayList list = new ArrayList();
      list.add(elem);
      return list;
   }

   ArrayList mergeNewElemValPair(Object list, Object elem) {
      ((ArrayList)list).add(elem);
      return (ArrayList)list;
   }

   ElemValPair makeConstantElem(String name, char kind, Object val) {
      ElemValPair result = null;
      switch(kind) {
      case 'B':
      case 'C':
      case 'I':
      case 'S':
      case 'Z':
         result = new IntElemValPair(name, kind, (Integer)val);
         break;
      case 'D':
         result = new DoubleElemValPair(name, kind, ((Number)val).doubleValue());
         break;
      case 'F':
         result = new FloatElemValPair(name, kind, ((Number)val).floatValue());
         break;
      case 'J':
         result = new LongElemValPair(name, kind, ((Number)val).longValue());
         break;
      case 's':
         result = new StringElemValPair(name, kind, (String)val);
      }

      return (ElemValPair)result;
   }

   ElemValPair makeEnumElem(String name, char kind, String tval, String cval) {
      return new EnumElemValPair(name, kind, tval, cval);
   }

   ElemValPair makeClassElem(String name, char kind, String cval) {
      return new ClassElemValPair(name, kind, cval);
   }

   ElemValPair makeAnnotElem(String name, char kind, Object attr) {
      return new AnnotElemValPair(name, kind, (AnnotationAttr)attr);
   }

   ElemValPair makeArrayElem(String name, char kind, Object list) {
      ArrayElemValPair elem = new ArrayElemValPair(name, kind, (ArrayList)list);
      elem.setNoName();
      return elem;
   }

   void endAnnotElem() {
   }

   void endArrayElem() {
   }

   public void readJasmin(InputStream input, String name, boolean numberLines) throws IOException, Exception {
      this.errors = 0;
      this.filename = name;
      this.auto_number = numberLines;
      this.class_env = new ClassEnv();
      this.scanner = new Scanner(input);
      parser parse_obj = new parser(this, this.scanner);
      parse_obj.parse();
   }

   public int errorCount() {
      return this.errors;
   }

   public String getClassName() {
      return this.class_name;
   }

   public void write(OutputStream outp) throws IOException, jasError {
      this.class_env.setSource(this.source_name);
      this.class_env.write(new DataOutputStream(outp));
   }
}
