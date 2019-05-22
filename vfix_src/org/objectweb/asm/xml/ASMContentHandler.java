package org.objectweb.asm.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ASMContentHandler extends DefaultHandler implements Opcodes {
   private final ArrayList<Object> stack = new ArrayList();
   String match = "";
   protected ClassVisitor cv;
   protected Map<Object, Label> labels;
   private static final String BASE = "class";
   private final ASMContentHandler.RuleSet RULES = new ASMContentHandler.RuleSet();
   static final HashMap<String, ASMContentHandler.Opcode> OPCODES = new HashMap();
   static final HashMap<String, Integer> TYPES;

   private static void addOpcode(String operStr, int oper, int group) {
      OPCODES.put(operStr, new ASMContentHandler.Opcode(oper, group));
   }

   public ASMContentHandler(ClassVisitor cv) {
      this.RULES.add("class", new ASMContentHandler.ClassRule());
      this.RULES.add("class/interfaces/interface", new ASMContentHandler.InterfaceRule());
      this.RULES.add("class/interfaces", new ASMContentHandler.InterfacesRule());
      this.RULES.add("class/outerclass", new ASMContentHandler.OuterClassRule());
      this.RULES.add("class/innerclass", new ASMContentHandler.InnerClassRule());
      this.RULES.add("class/source", new ASMContentHandler.SourceRule());
      this.RULES.add("class/field", new ASMContentHandler.FieldRule());
      this.RULES.add("class/method", new ASMContentHandler.MethodRule());
      this.RULES.add("class/method/exceptions/exception", new ASMContentHandler.ExceptionRule());
      this.RULES.add("class/method/exceptions", new ASMContentHandler.ExceptionsRule());
      this.RULES.add("class/method/parameter", new ASMContentHandler.MethodParameterRule());
      this.RULES.add("class/method/annotationDefault", new ASMContentHandler.AnnotationDefaultRule());
      this.RULES.add("class/method/code/*", new ASMContentHandler.OpcodesRule());
      this.RULES.add("class/method/code/frame", new ASMContentHandler.FrameRule());
      this.RULES.add("class/method/code/frame/local", new ASMContentHandler.FrameTypeRule());
      this.RULES.add("class/method/code/frame/stack", new ASMContentHandler.FrameTypeRule());
      this.RULES.add("class/method/code/TABLESWITCH", new ASMContentHandler.TableSwitchRule());
      this.RULES.add("class/method/code/TABLESWITCH/label", new ASMContentHandler.TableSwitchLabelRule());
      this.RULES.add("class/method/code/LOOKUPSWITCH", new ASMContentHandler.LookupSwitchRule());
      this.RULES.add("class/method/code/LOOKUPSWITCH/label", new ASMContentHandler.LookupSwitchLabelRule());
      this.RULES.add("class/method/code/INVOKEDYNAMIC", new ASMContentHandler.InvokeDynamicRule());
      this.RULES.add("class/method/code/INVOKEDYNAMIC/bsmArg", new ASMContentHandler.InvokeDynamicBsmArgumentsRule());
      this.RULES.add("class/method/code/Label", new ASMContentHandler.LabelRule());
      this.RULES.add("class/method/code/TryCatch", new ASMContentHandler.TryCatchRule());
      this.RULES.add("class/method/code/LineNumber", new ASMContentHandler.LineNumberRule());
      this.RULES.add("class/method/code/LocalVar", new ASMContentHandler.LocalVarRule());
      this.RULES.add("class/method/code/Max", new ASMContentHandler.MaxRule());
      this.RULES.add("*/annotation", new ASMContentHandler.AnnotationRule());
      this.RULES.add("*/typeAnnotation", new ASMContentHandler.TypeAnnotationRule());
      this.RULES.add("*/parameterAnnotation", new ASMContentHandler.AnnotationParameterRule());
      this.RULES.add("*/insnAnnotation", new ASMContentHandler.InsnAnnotationRule());
      this.RULES.add("*/tryCatchAnnotation", new ASMContentHandler.TryCatchAnnotationRule());
      this.RULES.add("*/localVariableAnnotation", new ASMContentHandler.LocalVariableAnnotationRule());
      this.RULES.add("*/annotationValue", new ASMContentHandler.AnnotationValueRule());
      this.RULES.add("*/annotationValueAnnotation", new ASMContentHandler.AnnotationValueAnnotationRule());
      this.RULES.add("*/annotationValueEnum", new ASMContentHandler.AnnotationValueEnumRule());
      this.RULES.add("*/annotationValueArray", new ASMContentHandler.AnnotationValueArrayRule());
      this.cv = cv;
   }

   public final void startElement(String ns, String lName, String qName, Attributes list) throws SAXException {
      String name = lName != null && lName.length() != 0 ? lName : qName;
      StringBuilder sb = new StringBuilder(this.match);
      if (this.match.length() > 0) {
         sb.append('/');
      }

      sb.append(name);
      this.match = sb.toString();
      ASMContentHandler.Rule r = (ASMContentHandler.Rule)this.RULES.match(this.match);
      if (r != null) {
         r.begin(name, list);
      }

   }

   public final void endElement(String ns, String lName, String qName) throws SAXException {
      String name = lName != null && lName.length() != 0 ? lName : qName;
      ASMContentHandler.Rule r = (ASMContentHandler.Rule)this.RULES.match(this.match);
      if (r != null) {
         r.end(name);
      }

      int slash = this.match.lastIndexOf(47);
      if (slash >= 0) {
         this.match = this.match.substring(0, slash);
      } else {
         this.match = "";
      }

   }

   final Object peek() {
      int size = this.stack.size();
      return size == 0 ? null : this.stack.get(size - 1);
   }

   final Object pop() {
      int size = this.stack.size();
      return size == 0 ? null : this.stack.remove(size - 1);
   }

   final void push(Object object) {
      this.stack.add(object);
   }

   static {
      addOpcode("NOP", 0, 0);
      addOpcode("ACONST_NULL", 1, 0);
      addOpcode("ICONST_M1", 2, 0);
      addOpcode("ICONST_0", 3, 0);
      addOpcode("ICONST_1", 4, 0);
      addOpcode("ICONST_2", 5, 0);
      addOpcode("ICONST_3", 6, 0);
      addOpcode("ICONST_4", 7, 0);
      addOpcode("ICONST_5", 8, 0);
      addOpcode("LCONST_0", 9, 0);
      addOpcode("LCONST_1", 10, 0);
      addOpcode("FCONST_0", 11, 0);
      addOpcode("FCONST_1", 12, 0);
      addOpcode("FCONST_2", 13, 0);
      addOpcode("DCONST_0", 14, 0);
      addOpcode("DCONST_1", 15, 0);
      addOpcode("BIPUSH", 16, 1);
      addOpcode("SIPUSH", 17, 1);
      addOpcode("LDC", 18, 7);
      addOpcode("ILOAD", 21, 2);
      addOpcode("LLOAD", 22, 2);
      addOpcode("FLOAD", 23, 2);
      addOpcode("DLOAD", 24, 2);
      addOpcode("ALOAD", 25, 2);
      addOpcode("IALOAD", 46, 0);
      addOpcode("LALOAD", 47, 0);
      addOpcode("FALOAD", 48, 0);
      addOpcode("DALOAD", 49, 0);
      addOpcode("AALOAD", 50, 0);
      addOpcode("BALOAD", 51, 0);
      addOpcode("CALOAD", 52, 0);
      addOpcode("SALOAD", 53, 0);
      addOpcode("ISTORE", 54, 2);
      addOpcode("LSTORE", 55, 2);
      addOpcode("FSTORE", 56, 2);
      addOpcode("DSTORE", 57, 2);
      addOpcode("ASTORE", 58, 2);
      addOpcode("IASTORE", 79, 0);
      addOpcode("LASTORE", 80, 0);
      addOpcode("FASTORE", 81, 0);
      addOpcode("DASTORE", 82, 0);
      addOpcode("AASTORE", 83, 0);
      addOpcode("BASTORE", 84, 0);
      addOpcode("CASTORE", 85, 0);
      addOpcode("SASTORE", 86, 0);
      addOpcode("POP", 87, 0);
      addOpcode("POP2", 88, 0);
      addOpcode("DUP", 89, 0);
      addOpcode("DUP_X1", 90, 0);
      addOpcode("DUP_X2", 91, 0);
      addOpcode("DUP2", 92, 0);
      addOpcode("DUP2_X1", 93, 0);
      addOpcode("DUP2_X2", 94, 0);
      addOpcode("SWAP", 95, 0);
      addOpcode("IADD", 96, 0);
      addOpcode("LADD", 97, 0);
      addOpcode("FADD", 98, 0);
      addOpcode("DADD", 99, 0);
      addOpcode("ISUB", 100, 0);
      addOpcode("LSUB", 101, 0);
      addOpcode("FSUB", 102, 0);
      addOpcode("DSUB", 103, 0);
      addOpcode("IMUL", 104, 0);
      addOpcode("LMUL", 105, 0);
      addOpcode("FMUL", 106, 0);
      addOpcode("DMUL", 107, 0);
      addOpcode("IDIV", 108, 0);
      addOpcode("LDIV", 109, 0);
      addOpcode("FDIV", 110, 0);
      addOpcode("DDIV", 111, 0);
      addOpcode("IREM", 112, 0);
      addOpcode("LREM", 113, 0);
      addOpcode("FREM", 114, 0);
      addOpcode("DREM", 115, 0);
      addOpcode("INEG", 116, 0);
      addOpcode("LNEG", 117, 0);
      addOpcode("FNEG", 118, 0);
      addOpcode("DNEG", 119, 0);
      addOpcode("ISHL", 120, 0);
      addOpcode("LSHL", 121, 0);
      addOpcode("ISHR", 122, 0);
      addOpcode("LSHR", 123, 0);
      addOpcode("IUSHR", 124, 0);
      addOpcode("LUSHR", 125, 0);
      addOpcode("IAND", 126, 0);
      addOpcode("LAND", 127, 0);
      addOpcode("IOR", 128, 0);
      addOpcode("LOR", 129, 0);
      addOpcode("IXOR", 130, 0);
      addOpcode("LXOR", 131, 0);
      addOpcode("IINC", 132, 8);
      addOpcode("I2L", 133, 0);
      addOpcode("I2F", 134, 0);
      addOpcode("I2D", 135, 0);
      addOpcode("L2I", 136, 0);
      addOpcode("L2F", 137, 0);
      addOpcode("L2D", 138, 0);
      addOpcode("F2I", 139, 0);
      addOpcode("F2L", 140, 0);
      addOpcode("F2D", 141, 0);
      addOpcode("D2I", 142, 0);
      addOpcode("D2L", 143, 0);
      addOpcode("D2F", 144, 0);
      addOpcode("I2B", 145, 0);
      addOpcode("I2C", 146, 0);
      addOpcode("I2S", 147, 0);
      addOpcode("LCMP", 148, 0);
      addOpcode("FCMPL", 149, 0);
      addOpcode("FCMPG", 150, 0);
      addOpcode("DCMPL", 151, 0);
      addOpcode("DCMPG", 152, 0);
      addOpcode("IFEQ", 153, 6);
      addOpcode("IFNE", 154, 6);
      addOpcode("IFLT", 155, 6);
      addOpcode("IFGE", 156, 6);
      addOpcode("IFGT", 157, 6);
      addOpcode("IFLE", 158, 6);
      addOpcode("IF_ICMPEQ", 159, 6);
      addOpcode("IF_ICMPNE", 160, 6);
      addOpcode("IF_ICMPLT", 161, 6);
      addOpcode("IF_ICMPGE", 162, 6);
      addOpcode("IF_ICMPGT", 163, 6);
      addOpcode("IF_ICMPLE", 164, 6);
      addOpcode("IF_ACMPEQ", 165, 6);
      addOpcode("IF_ACMPNE", 166, 6);
      addOpcode("GOTO", 167, 6);
      addOpcode("JSR", 168, 6);
      addOpcode("RET", 169, 2);
      addOpcode("IRETURN", 172, 0);
      addOpcode("LRETURN", 173, 0);
      addOpcode("FRETURN", 174, 0);
      addOpcode("DRETURN", 175, 0);
      addOpcode("ARETURN", 176, 0);
      addOpcode("RETURN", 177, 0);
      addOpcode("GETSTATIC", 178, 4);
      addOpcode("PUTSTATIC", 179, 4);
      addOpcode("GETFIELD", 180, 4);
      addOpcode("PUTFIELD", 181, 4);
      addOpcode("INVOKEVIRTUAL", 182, 5);
      addOpcode("INVOKESPECIAL", 183, 5);
      addOpcode("INVOKESTATIC", 184, 5);
      addOpcode("INVOKEINTERFACE", 185, 5);
      addOpcode("NEW", 187, 3);
      addOpcode("NEWARRAY", 188, 1);
      addOpcode("ANEWARRAY", 189, 3);
      addOpcode("ARRAYLENGTH", 190, 0);
      addOpcode("ATHROW", 191, 0);
      addOpcode("CHECKCAST", 192, 3);
      addOpcode("INSTANCEOF", 193, 3);
      addOpcode("MONITORENTER", 194, 0);
      addOpcode("MONITOREXIT", 195, 0);
      addOpcode("MULTIANEWARRAY", 197, 9);
      addOpcode("IFNULL", 198, 6);
      addOpcode("IFNONNULL", 199, 6);
      TYPES = new HashMap();
      String[] types = SAXCodeAdapter.TYPES;

      for(int i = 0; i < types.length; ++i) {
         TYPES.put(types[i], i);
      }

   }

   static final class Opcode {
      public final int opcode;
      public final int type;

      Opcode(int opcode, int type) {
         this.opcode = opcode;
         this.type = type;
      }
   }

   final class AnnotationDefaultRule extends ASMContentHandler.Rule {
      AnnotationDefaultRule() {
         super();
      }

      public void begin(String nm, Attributes attrs) {
         MethodVisitor av = (MethodVisitor)ASMContentHandler.this.peek();
         ASMContentHandler.this.push(av == null ? null : av.visitAnnotationDefault());
      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class AnnotationValueArrayRule extends ASMContentHandler.Rule {
      AnnotationValueArrayRule() {
         super();
      }

      public void begin(String nm, Attributes attrs) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.peek();
         ASMContentHandler.this.push(av == null ? null : av.visitArray(attrs.getValue("name")));
      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class AnnotationValueAnnotationRule extends ASMContentHandler.Rule {
      AnnotationValueAnnotationRule() {
         super();
      }

      public void begin(String nm, Attributes attrs) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.peek();
         ASMContentHandler.this.push(av == null ? null : av.visitAnnotation(attrs.getValue("name"), attrs.getValue("desc")));
      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class AnnotationValueEnumRule extends ASMContentHandler.Rule {
      AnnotationValueEnumRule() {
         super();
      }

      public void begin(String nm, Attributes attrs) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.peek();
         if (av != null) {
            av.visitEnum(attrs.getValue("name"), attrs.getValue("desc"), attrs.getValue("value"));
         }

      }
   }

   final class AnnotationValueRule extends ASMContentHandler.Rule {
      AnnotationValueRule() {
         super();
      }

      public void begin(String nm, Attributes attrs) throws SAXException {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.peek();
         if (av != null) {
            av.visit(attrs.getValue("name"), this.getValue(attrs.getValue("desc"), attrs.getValue("value")));
         }

      }
   }

   final class LocalVariableAnnotationRule extends ASMContentHandler.Rule {
      LocalVariableAnnotationRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         String desc = attrs.getValue("desc");
         boolean visible = Boolean.valueOf(attrs.getValue("visible"));
         int typeRef = Integer.parseInt(attrs.getValue("typeRef"));
         TypePath typePath = TypePath.fromString(attrs.getValue("typePath"));
         String[] s = attrs.getValue("start").split(" ");
         Label[] start = new Label[s.length];

         for(int ixx = 0; ixx < start.length; ++ixx) {
            start[ixx] = this.getLabel(s[ixx]);
         }

         String[] e = attrs.getValue("end").split(" ");
         Label[] end = new Label[e.length];

         for(int i = 0; i < end.length; ++i) {
            end[i] = this.getLabel(e[i]);
         }

         String[] v = attrs.getValue("index").split(" ");
         int[] index = new int[v.length];

         for(int ix = 0; ix < index.length; ++ix) {
            index[ix] = Integer.parseInt(v[ix]);
         }

         ASMContentHandler.this.push(((MethodVisitor)ASMContentHandler.this.peek()).visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible));
      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class TryCatchAnnotationRule extends ASMContentHandler.Rule {
      TryCatchAnnotationRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         String desc = attrs.getValue("desc");
         boolean visible = Boolean.valueOf(attrs.getValue("visible"));
         int typeRef = Integer.parseInt(attrs.getValue("typeRef"));
         TypePath typePath = TypePath.fromString(attrs.getValue("typePath"));
         ASMContentHandler.this.push(((MethodVisitor)ASMContentHandler.this.peek()).visitTryCatchAnnotation(typeRef, typePath, desc, visible));
      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class InsnAnnotationRule extends ASMContentHandler.Rule {
      InsnAnnotationRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         String desc = attrs.getValue("desc");
         boolean visible = Boolean.valueOf(attrs.getValue("visible"));
         int typeRef = Integer.parseInt(attrs.getValue("typeRef"));
         TypePath typePath = TypePath.fromString(attrs.getValue("typePath"));
         ASMContentHandler.this.push(((MethodVisitor)ASMContentHandler.this.peek()).visitInsnAnnotation(typeRef, typePath, desc, visible));
      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class AnnotationParameterRule extends ASMContentHandler.Rule {
      AnnotationParameterRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         int parameter = Integer.parseInt(attrs.getValue("parameter"));
         String desc = attrs.getValue("desc");
         boolean visible = Boolean.valueOf(attrs.getValue("visible"));
         ASMContentHandler.this.push(((MethodVisitor)ASMContentHandler.this.peek()).visitParameterAnnotation(parameter, desc, visible));
      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class TypeAnnotationRule extends ASMContentHandler.Rule {
      TypeAnnotationRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         String desc = attrs.getValue("desc");
         boolean visible = Boolean.valueOf(attrs.getValue("visible"));
         int typeRef = Integer.parseInt(attrs.getValue("typeRef"));
         TypePath typePath = TypePath.fromString(attrs.getValue("typePath"));
         Object v = ASMContentHandler.this.peek();
         if (v instanceof ClassVisitor) {
            ASMContentHandler.this.push(((ClassVisitor)v).visitTypeAnnotation(typeRef, typePath, desc, visible));
         } else if (v instanceof FieldVisitor) {
            ASMContentHandler.this.push(((FieldVisitor)v).visitTypeAnnotation(typeRef, typePath, desc, visible));
         } else if (v instanceof MethodVisitor) {
            ASMContentHandler.this.push(((MethodVisitor)v).visitTypeAnnotation(typeRef, typePath, desc, visible));
         }

      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class AnnotationRule extends ASMContentHandler.Rule {
      AnnotationRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         String desc = attrs.getValue("desc");
         boolean visible = Boolean.valueOf(attrs.getValue("visible"));
         Object v = ASMContentHandler.this.peek();
         if (v instanceof ClassVisitor) {
            ASMContentHandler.this.push(((ClassVisitor)v).visitAnnotation(desc, visible));
         } else if (v instanceof FieldVisitor) {
            ASMContentHandler.this.push(((FieldVisitor)v).visitAnnotation(desc, visible));
         } else if (v instanceof MethodVisitor) {
            ASMContentHandler.this.push(((MethodVisitor)v).visitAnnotation(desc, visible));
         }

      }

      public void end(String name) {
         AnnotationVisitor av = (AnnotationVisitor)ASMContentHandler.this.pop();
         if (av != null) {
            av.visitEnd();
         }

      }
   }

   final class MaxRule extends ASMContentHandler.Rule {
      MaxRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) {
         int maxStack = Integer.parseInt(attrs.getValue("maxStack"));
         int maxLocals = Integer.parseInt(attrs.getValue("maxLocals"));
         this.getCodeVisitor().visitMaxs(maxStack, maxLocals);
      }
   }

   final class OpcodesRule extends ASMContentHandler.Rule {
      OpcodesRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) throws SAXException {
         ASMContentHandler.Opcode o = (ASMContentHandler.Opcode)ASMContentHandler.OPCODES.get(element);
         if (o == null) {
            throw new SAXException("Invalid element: " + element + " at " + ASMContentHandler.this.match);
         } else {
            switch(o.type) {
            case 0:
               this.getCodeVisitor().visitInsn(o.opcode);
               break;
            case 1:
               this.getCodeVisitor().visitIntInsn(o.opcode, Integer.parseInt(attrs.getValue("value")));
               break;
            case 2:
               this.getCodeVisitor().visitVarInsn(o.opcode, Integer.parseInt(attrs.getValue("var")));
               break;
            case 3:
               this.getCodeVisitor().visitTypeInsn(o.opcode, attrs.getValue("desc"));
               break;
            case 4:
               this.getCodeVisitor().visitFieldInsn(o.opcode, attrs.getValue("owner"), attrs.getValue("name"), attrs.getValue("desc"));
               break;
            case 5:
               this.getCodeVisitor().visitMethodInsn(o.opcode, attrs.getValue("owner"), attrs.getValue("name"), attrs.getValue("desc"), attrs.getValue("itf").equals("true"));
               break;
            case 6:
               this.getCodeVisitor().visitJumpInsn(o.opcode, this.getLabel(attrs.getValue("label")));
               break;
            case 7:
               this.getCodeVisitor().visitLdcInsn(this.getValue(attrs.getValue("desc"), attrs.getValue("cst")));
               break;
            case 8:
               this.getCodeVisitor().visitIincInsn(Integer.parseInt(attrs.getValue("var")), Integer.parseInt(attrs.getValue("inc")));
               break;
            case 9:
               this.getCodeVisitor().visitMultiANewArrayInsn(attrs.getValue("desc"), Integer.parseInt(attrs.getValue("dims")));
               break;
            default:
               throw new Error("Internal error");
            }

         }
      }
   }

   final class InvokeDynamicBsmArgumentsRule extends ASMContentHandler.Rule {
      InvokeDynamicBsmArgumentsRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) throws SAXException {
         ArrayList<Object> bsmArgs = (ArrayList)ASMContentHandler.this.peek();
         bsmArgs.add(this.getValue(attrs.getValue("desc"), attrs.getValue("cst")));
      }
   }

   final class InvokeDynamicRule extends ASMContentHandler.Rule {
      InvokeDynamicRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) throws SAXException {
         ASMContentHandler.this.push(attrs.getValue("name"));
         ASMContentHandler.this.push(attrs.getValue("desc"));
         ASMContentHandler.this.push(this.decodeHandle(attrs.getValue("bsm")));
         ASMContentHandler.this.push(new ArrayList());
      }

      public final void end(String element) {
         ArrayList<?> bsmArgs = (ArrayList)ASMContentHandler.this.pop();
         Handle bsm = (Handle)ASMContentHandler.this.pop();
         String desc = (String)ASMContentHandler.this.pop();
         String name = (String)ASMContentHandler.this.pop();
         this.getCodeVisitor().visitInvokeDynamicInsn(name, desc, bsm, bsmArgs.toArray());
      }
   }

   final class LocalVarRule extends ASMContentHandler.Rule {
      LocalVarRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) {
         String name = attrs.getValue("name");
         String desc = attrs.getValue("desc");
         String signature = attrs.getValue("signature");
         Label start = this.getLabel(attrs.getValue("start"));
         Label end = this.getLabel(attrs.getValue("end"));
         int var = Integer.parseInt(attrs.getValue("var"));
         this.getCodeVisitor().visitLocalVariable(name, desc, signature, start, end, var);
      }
   }

   final class LineNumberRule extends ASMContentHandler.Rule {
      LineNumberRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         int line = Integer.parseInt(attrs.getValue("line"));
         Label start = this.getLabel(attrs.getValue("start"));
         this.getCodeVisitor().visitLineNumber(line, start);
      }
   }

   final class TryCatchRule extends ASMContentHandler.Rule {
      TryCatchRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         Label start = this.getLabel(attrs.getValue("start"));
         Label end = this.getLabel(attrs.getValue("end"));
         Label handler = this.getLabel(attrs.getValue("handler"));
         String type = attrs.getValue("type");
         this.getCodeVisitor().visitTryCatchBlock(start, end, handler, type);
      }
   }

   final class LabelRule extends ASMContentHandler.Rule {
      LabelRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         this.getCodeVisitor().visitLabel(this.getLabel(attrs.getValue("name")));
      }
   }

   final class FrameTypeRule extends ASMContentHandler.Rule {
      FrameTypeRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         ArrayList<Object> types = (ArrayList)((HashMap)ASMContentHandler.this.peek()).get(name);
         String type = attrs.getValue("type");
         if ("uninitialized".equals(type)) {
            types.add(this.getLabel(attrs.getValue("label")));
         } else {
            Integer t = (Integer)ASMContentHandler.TYPES.get(type);
            if (t == null) {
               types.add(type);
            } else {
               types.add(t);
            }
         }

      }
   }

   final class FrameRule extends ASMContentHandler.Rule {
      FrameRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         HashMap<String, Object> typeLists = new HashMap();
         typeLists.put("local", new ArrayList());
         typeLists.put("stack", new ArrayList());
         ASMContentHandler.this.push(attrs.getValue("type"));
         ASMContentHandler.this.push(attrs.getValue("count") == null ? "0" : attrs.getValue("count"));
         ASMContentHandler.this.push(typeLists);
      }

      public void end(String name) {
         HashMap<?, ?> typeLists = (HashMap)ASMContentHandler.this.pop();
         ArrayList<?> locals = (ArrayList)typeLists.get("local");
         int nLocal = locals.size();
         Object[] local = locals.toArray();
         ArrayList<?> stacks = (ArrayList)typeLists.get("stack");
         int nStack = stacks.size();
         Object[] stack = stacks.toArray();
         String count = (String)ASMContentHandler.this.pop();
         String type = (String)ASMContentHandler.this.pop();
         if ("NEW".equals(type)) {
            this.getCodeVisitor().visitFrame(-1, nLocal, local, nStack, stack);
         } else if ("FULL".equals(type)) {
            this.getCodeVisitor().visitFrame(0, nLocal, local, nStack, stack);
         } else if ("APPEND".equals(type)) {
            this.getCodeVisitor().visitFrame(1, nLocal, local, 0, (Object[])null);
         } else if ("CHOP".equals(type)) {
            this.getCodeVisitor().visitFrame(2, Integer.parseInt(count), (Object[])null, 0, (Object[])null);
         } else if ("SAME".equals(type)) {
            this.getCodeVisitor().visitFrame(3, 0, (Object[])null, 0, (Object[])null);
         } else if ("SAME1".equals(type)) {
            this.getCodeVisitor().visitFrame(4, 0, (Object[])null, nStack, stack);
         }

      }
   }

   final class LookupSwitchLabelRule extends ASMContentHandler.Rule {
      LookupSwitchLabelRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         HashMap<?, ?> vals = (HashMap)ASMContentHandler.this.peek();
         ((ArrayList)vals.get("labels")).add(this.getLabel(attrs.getValue("name")));
         ((ArrayList)vals.get("keys")).add(attrs.getValue("key"));
      }
   }

   final class LookupSwitchRule extends ASMContentHandler.Rule {
      LookupSwitchRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         HashMap<String, Object> vals = new HashMap();
         vals.put("dflt", attrs.getValue("dflt"));
         vals.put("labels", new ArrayList());
         vals.put("keys", new ArrayList());
         ASMContentHandler.this.push(vals);
      }

      public final void end(String name) {
         HashMap<?, ?> vals = (HashMap)ASMContentHandler.this.pop();
         Label dflt = this.getLabel(vals.get("dflt"));
         ArrayList<String> keyList = (ArrayList)vals.get("keys");
         ArrayList<?> lbls = (ArrayList)vals.get("labels");
         Label[] labels = (Label[])lbls.toArray(new Label[lbls.size()]);
         int[] keys = new int[keyList.size()];

         for(int i = 0; i < keys.length; ++i) {
            keys[i] = Integer.parseInt((String)keyList.get(i));
         }

         this.getCodeVisitor().visitLookupSwitchInsn(dflt, keys, labels);
      }
   }

   final class TableSwitchLabelRule extends ASMContentHandler.Rule {
      TableSwitchLabelRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         ((ArrayList)((HashMap)ASMContentHandler.this.peek()).get("labels")).add(this.getLabel(attrs.getValue("name")));
      }
   }

   final class TableSwitchRule extends ASMContentHandler.Rule {
      TableSwitchRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         HashMap<String, Object> vals = new HashMap();
         vals.put("min", attrs.getValue("min"));
         vals.put("max", attrs.getValue("max"));
         vals.put("dflt", attrs.getValue("dflt"));
         vals.put("labels", new ArrayList());
         ASMContentHandler.this.push(vals);
      }

      public final void end(String name) {
         HashMap<?, ?> vals = (HashMap)ASMContentHandler.this.pop();
         int min = Integer.parseInt((String)vals.get("min"));
         int max = Integer.parseInt((String)vals.get("max"));
         Label dflt = this.getLabel(vals.get("dflt"));
         ArrayList<?> lbls = (ArrayList)vals.get("labels");
         Label[] labels = (Label[])lbls.toArray(new Label[lbls.size()]);
         this.getCodeVisitor().visitTableSwitchInsn(min, max, dflt, labels);
      }
   }

   final class MethodParameterRule extends ASMContentHandler.Rule {
      MethodParameterRule() {
         super();
      }

      public void begin(String nm, Attributes attrs) {
         String name = attrs.getValue("name");
         int access = this.getAccess(attrs.getValue("access"));
         this.getCodeVisitor().visitParameter(name, access);
      }
   }

   final class ExceptionsRule extends ASMContentHandler.Rule {
      ExceptionsRule() {
         super();
      }

      public final void end(String element) {
         HashMap<?, ?> vals = (HashMap)ASMContentHandler.this.pop();
         int access = this.getAccess((String)vals.get("access"));
         String name = (String)vals.get("name");
         String desc = (String)vals.get("desc");
         String signature = (String)vals.get("signature");
         ArrayList<?> excs = (ArrayList)vals.get("exceptions");
         String[] exceptions = (String[])excs.toArray(new String[excs.size()]);
         ASMContentHandler.this.push(ASMContentHandler.this.cv.visitMethod(access, name, desc, signature, exceptions));
      }
   }

   final class ExceptionRule extends ASMContentHandler.Rule {
      ExceptionRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         ((ArrayList)((HashMap)ASMContentHandler.this.peek()).get("exceptions")).add(attrs.getValue("name"));
      }
   }

   final class MethodRule extends ASMContentHandler.Rule {
      MethodRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         ASMContentHandler.this.labels = new HashMap();
         HashMap<String, Object> vals = new HashMap();
         vals.put("access", attrs.getValue("access"));
         vals.put("name", attrs.getValue("name"));
         vals.put("desc", attrs.getValue("desc"));
         vals.put("signature", attrs.getValue("signature"));
         vals.put("exceptions", new ArrayList());
         ASMContentHandler.this.push(vals);
      }

      public final void end(String name) {
         ((MethodVisitor)ASMContentHandler.this.pop()).visitEnd();
         ASMContentHandler.this.labels = null;
      }
   }

   final class FieldRule extends ASMContentHandler.Rule {
      FieldRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) throws SAXException {
         int access = this.getAccess(attrs.getValue("access"));
         String name = attrs.getValue("name");
         String signature = attrs.getValue("signature");
         String desc = attrs.getValue("desc");
         Object value = this.getValue(desc, attrs.getValue("value"));
         ASMContentHandler.this.push(ASMContentHandler.this.cv.visitField(access, name, desc, signature, value));
      }

      public void end(String name) {
         ((FieldVisitor)ASMContentHandler.this.pop()).visitEnd();
      }
   }

   final class InnerClassRule extends ASMContentHandler.Rule {
      InnerClassRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) {
         int access = this.getAccess(attrs.getValue("access"));
         String name = attrs.getValue("name");
         String outerName = attrs.getValue("outerName");
         String innerName = attrs.getValue("innerName");
         ASMContentHandler.this.cv.visitInnerClass(name, outerName, innerName, access);
      }
   }

   final class OuterClassRule extends ASMContentHandler.Rule {
      OuterClassRule() {
         super();
      }

      public final void begin(String element, Attributes attrs) {
         String owner = attrs.getValue("owner");
         String name = attrs.getValue("name");
         String desc = attrs.getValue("desc");
         ASMContentHandler.this.cv.visitOuterClass(owner, name, desc);
      }
   }

   final class InterfacesRule extends ASMContentHandler.Rule {
      InterfacesRule() {
         super();
      }

      public final void end(String element) {
         HashMap<?, ?> vals = (HashMap)ASMContentHandler.this.pop();
         int version = (Integer)vals.get("version");
         int access = this.getAccess((String)vals.get("access"));
         String name = (String)vals.get("name");
         String signature = (String)vals.get("signature");
         String parent = (String)vals.get("parent");
         ArrayList<?> infs = (ArrayList)vals.get("interfaces");
         String[] interfaces = (String[])infs.toArray(new String[infs.size()]);
         ASMContentHandler.this.cv.visit(version, access, name, signature, parent, interfaces);
         ASMContentHandler.this.push(ASMContentHandler.this.cv);
      }
   }

   final class InterfaceRule extends ASMContentHandler.Rule {
      InterfaceRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         ((ArrayList)((HashMap)ASMContentHandler.this.peek()).get("interfaces")).add(attrs.getValue("name"));
      }
   }

   final class SourceRule extends ASMContentHandler.Rule {
      SourceRule() {
         super();
      }

      public void begin(String name, Attributes attrs) {
         String file = attrs.getValue("file");
         String debug = attrs.getValue("debug");
         ASMContentHandler.this.cv.visitSource(file, debug);
      }
   }

   final class ClassRule extends ASMContentHandler.Rule {
      ClassRule() {
         super();
      }

      public final void begin(String name, Attributes attrs) {
         int major = Integer.parseInt(attrs.getValue("major"));
         int minor = Integer.parseInt(attrs.getValue("minor"));
         HashMap<String, Object> vals = new HashMap();
         vals.put("version", minor << 16 | major);
         vals.put("access", attrs.getValue("access"));
         vals.put("name", attrs.getValue("name"));
         vals.put("parent", attrs.getValue("parent"));
         vals.put("source", attrs.getValue("source"));
         vals.put("signature", attrs.getValue("signature"));
         vals.put("interfaces", new ArrayList());
         ASMContentHandler.this.push(vals);
      }
   }

   protected abstract class Rule {
      public void begin(String name, Attributes attrs) throws SAXException {
      }

      public void end(String name) {
      }

      protected final Object getValue(String desc, String val) throws SAXException {
         Object value = null;
         if (val != null) {
            if ("Ljava/lang/String;".equals(desc)) {
               value = this.decode(val);
            } else if (!"Ljava/lang/Integer;".equals(desc) && !"I".equals(desc) && !"S".equals(desc) && !"B".equals(desc) && !"C".equals(desc) && !"Z".equals(desc)) {
               if ("Ljava/lang/Short;".equals(desc)) {
                  value = new Short(val);
               } else if ("Ljava/lang/Byte;".equals(desc)) {
                  value = new Byte(val);
               } else if ("Ljava/lang/Character;".equals(desc)) {
                  value = new Character(this.decode(val).charAt(0));
               } else if ("Ljava/lang/Boolean;".equals(desc)) {
                  value = Boolean.valueOf(val);
               } else if (!"Ljava/lang/Long;".equals(desc) && !"J".equals(desc)) {
                  if (!"Ljava/lang/Float;".equals(desc) && !"F".equals(desc)) {
                     if (!"Ljava/lang/Double;".equals(desc) && !"D".equals(desc)) {
                        if (Type.getDescriptor(Type.class).equals(desc)) {
                           value = Type.getType(val);
                        } else {
                           if (!Type.getDescriptor(Handle.class).equals(desc)) {
                              throw new SAXException("Invalid value:" + val + " desc:" + desc + " ctx:" + this);
                           }

                           value = this.decodeHandle(val);
                        }
                     } else {
                        value = new Double(val);
                     }
                  } else {
                     value = new Float(val);
                  }
               } else {
                  value = new Long(val);
               }
            } else {
               value = new Integer(val);
            }
         }

         return value;
      }

      Handle decodeHandle(String val) throws SAXException {
         try {
            int dotIndex = val.indexOf(46);
            int descIndex = val.indexOf(40, dotIndex + 1);
            int tagIndex = val.lastIndexOf(40);
            int itfIndex = val.indexOf(32, tagIndex + 1);
            boolean itf = itfIndex != -1;
            int tag = Integer.parseInt(val.substring(tagIndex + 1, itf ? val.length() - 1 : itfIndex));
            String owner = val.substring(0, dotIndex);
            String name = val.substring(dotIndex + 1, descIndex);
            String desc = val.substring(descIndex, tagIndex - 1);
            return new Handle(tag, owner, name, desc, itf);
         } catch (RuntimeException var11) {
            throw new SAXException("Malformed handle " + val, var11);
         }
      }

      private final String decode(String val) throws SAXException {
         StringBuilder sb = new StringBuilder(val.length());

         try {
            for(int n = 0; n < val.length(); ++n) {
               char c = val.charAt(n);
               if (c == '\\') {
                  ++n;
                  c = val.charAt(n);
                  if (c == '\\') {
                     sb.append('\\');
                  } else {
                     ++n;
                     sb.append((char)Integer.parseInt(val.substring(n, n + 4), 16));
                     n += 3;
                  }
               } else {
                  sb.append(c);
               }
            }
         } catch (RuntimeException var5) {
            throw new SAXException(var5);
         }

         return sb.toString();
      }

      protected final Label getLabel(Object label) {
         Label lbl = (Label)ASMContentHandler.this.labels.get(label);
         if (lbl == null) {
            lbl = new Label();
            ASMContentHandler.this.labels.put(label, lbl);
         }

         return lbl;
      }

      protected final MethodVisitor getCodeVisitor() {
         return (MethodVisitor)ASMContentHandler.this.peek();
      }

      protected final int getAccess(String s) {
         int access = 0;
         if (s.indexOf("public") != -1) {
            access |= 1;
         }

         if (s.indexOf("private") != -1) {
            access |= 2;
         }

         if (s.indexOf("protected") != -1) {
            access |= 4;
         }

         if (s.indexOf("static") != -1) {
            access |= 8;
         }

         if (s.indexOf("final") != -1) {
            access |= 16;
         }

         if (s.indexOf("super") != -1) {
            access |= 32;
         }

         if (s.indexOf("synchronized") != -1) {
            access |= 32;
         }

         if (s.indexOf("volatile") != -1) {
            access |= 64;
         }

         if (s.indexOf("bridge") != -1) {
            access |= 64;
         }

         if (s.indexOf("varargs") != -1) {
            access |= 128;
         }

         if (s.indexOf("transient") != -1) {
            access |= 128;
         }

         if (s.indexOf("native") != -1) {
            access |= 256;
         }

         if (s.indexOf("interface") != -1) {
            access |= 512;
         }

         if (s.indexOf("abstract") != -1) {
            access |= 1024;
         }

         if (s.indexOf("strict") != -1) {
            access |= 2048;
         }

         if (s.indexOf("synthetic") != -1) {
            access |= 4096;
         }

         if (s.indexOf("annotation") != -1) {
            access |= 8192;
         }

         if (s.indexOf("enum") != -1) {
            access |= 16384;
         }

         if (s.indexOf("deprecated") != -1) {
            access |= 131072;
         }

         if (s.indexOf("mandated") != -1) {
            access |= 32768;
         }

         return access;
      }
   }

   static final class RuleSet {
      private final HashMap<String, Object> rules = new HashMap();
      private final ArrayList<String> lpatterns = new ArrayList();
      private final ArrayList<String> rpatterns = new ArrayList();

      public void add(String path, Object rule) {
         String pattern = path;
         if (path.startsWith("*/")) {
            pattern = path.substring(1);
            this.lpatterns.add(pattern);
         } else if (path.endsWith("/*")) {
            pattern = path.substring(0, path.length() - 1);
            this.rpatterns.add(pattern);
         }

         this.rules.put(pattern, rule);
      }

      public Object match(String path) {
         if (this.rules.containsKey(path)) {
            return this.rules.get(path);
         } else {
            int n = path.lastIndexOf(47);
            Iterator it = this.lpatterns.iterator();

            String pattern;
            do {
               if (!it.hasNext()) {
                  it = this.rpatterns.iterator();

                  do {
                     if (!it.hasNext()) {
                        return null;
                     }

                     pattern = (String)it.next();
                  } while(!path.startsWith(pattern));

                  return this.rules.get(pattern);
               }

               pattern = (String)it.next();
            } while(!path.substring(n).endsWith(pattern));

            return this.rules.get(pattern);
         }
      }
   }

   private interface OpcodeGroup {
      int INSN = 0;
      int INSN_INT = 1;
      int INSN_VAR = 2;
      int INSN_TYPE = 3;
      int INSN_FIELD = 4;
      int INSN_METHOD = 5;
      int INSN_JUMP = 6;
      int INSN_LDC = 7;
      int INSN_IINC = 8;
      int INSN_MULTIANEWARRAY = 9;
   }
}
