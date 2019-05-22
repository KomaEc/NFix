package soot;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.baf.DoubleWordType;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Attribute;
import soot.tagkit.Base64;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.toolkits.graph.Block;
import soot.util.StringTools;

public abstract class AbstractJasminClass {
   private static final Logger logger = LoggerFactory.getLogger(AbstractJasminClass.class);
   protected Map<Unit, String> unitToLabel;
   protected Map<Local, Integer> localToSlot;
   protected Map<Unit, Integer> subroutineToReturnAddressSlot;
   protected List<String> code;
   protected boolean isEmittingMethodCode;
   protected int labelCount;
   protected boolean isNextGotoAJsr;
   protected int returnAddressSlot;
   protected int currentStackHeight = 0;
   protected int maxStackHeight = 0;
   protected Map<Local, Object> localToGroup;
   protected Map<Object, Integer> groupToColorCount;
   protected Map<Local, Integer> localToColor;
   protected Map<Block, Integer> blockToStackHeight = new HashMap();
   protected Map<Block, Integer> blockToLogicalStackHeight = new HashMap();
   private static Map<Integer, VisibilityAnnotationTag> safeVats = new HashMap();

   public static String slashify(String s) {
      return s.replace('.', '/');
   }

   public static int sizeOfType(Type t) {
      if (!(t instanceof DoubleWordType) && !(t instanceof LongType) && !(t instanceof DoubleType)) {
         return t instanceof VoidType ? 0 : 1;
      } else {
         return 2;
      }
   }

   public static int argCountOf(SootMethodRef m) {
      int argCount = 0;

      Type t;
      for(Iterator typeIt = m.parameterTypes().iterator(); typeIt.hasNext(); argCount += sizeOfType(t)) {
         t = (Type)typeIt.next();
      }

      return argCount;
   }

   public static String jasminDescriptorOf(Type type) {
      TypeSwitch sw;
      type.apply(sw = new TypeSwitch() {
         public void caseBooleanType(BooleanType t) {
            this.setResult("Z");
         }

         public void caseByteType(ByteType t) {
            this.setResult("B");
         }

         public void caseCharType(CharType t) {
            this.setResult("C");
         }

         public void caseDoubleType(DoubleType t) {
            this.setResult("D");
         }

         public void caseFloatType(FloatType t) {
            this.setResult("F");
         }

         public void caseIntType(IntType t) {
            this.setResult("I");
         }

         public void caseLongType(LongType t) {
            this.setResult("J");
         }

         public void caseShortType(ShortType t) {
            this.setResult("S");
         }

         public void defaultCase(Type t) {
            throw new RuntimeException("Invalid type: " + t);
         }

         public void caseArrayType(ArrayType t) {
            StringBuffer buffer = new StringBuffer();

            for(int i = 0; i < t.numDimensions; ++i) {
               buffer.append("[");
            }

            this.setResult(buffer.toString() + AbstractJasminClass.jasminDescriptorOf(t.baseType));
         }

         public void caseRefType(RefType t) {
            this.setResult("L" + t.getClassName().replace('.', '/') + ";");
         }

         public void caseVoidType(VoidType t) {
            this.setResult("V");
         }
      });
      return (String)sw.getResult();
   }

   public static String jasminDescriptorOf(SootMethodRef m) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("(");
      Iterator typeIt = m.parameterTypes().iterator();

      while(typeIt.hasNext()) {
         Type t = (Type)typeIt.next();
         buffer.append(jasminDescriptorOf(t));
      }

      buffer.append(")");
      buffer.append(jasminDescriptorOf(m.returnType()));
      return buffer.toString();
   }

   protected void emit(String s) {
      this.okayEmit(s);
   }

   protected void okayEmit(String s) {
      if (this.isEmittingMethodCode && !s.endsWith(":")) {
         this.code.add("    " + s);
      } else {
         this.code.add(s);
      }

   }

   private String getVisibilityAnnotationAttr(VisibilityAnnotationTag tag) {
      StringBuffer sb = new StringBuffer();
      if (tag == null) {
         return "";
      } else {
         if (tag.getVisibility() == 0) {
            sb.append(".runtime_visible_annotation\n");
         } else {
            if (tag.getVisibility() != 1) {
               return "";
            }

            sb.append(".runtime_invisible_annotation\n");
         }

         if (tag.hasAnnotations()) {
            Iterator it = tag.getAnnotations().iterator();

            while(it.hasNext()) {
               AnnotationTag annot = (AnnotationTag)it.next();
               sb.append(".annotation ");
               sb.append(StringTools.getQuotedStringOf(annot.getType()) + "\n");
               Iterator var5 = annot.getElems().iterator();

               while(var5.hasNext()) {
                  AnnotationElem ae = (AnnotationElem)var5.next();
                  sb.append(this.getElemAttr(ae));
               }

               sb.append(".end .annotation\n");
            }
         }

         sb.append(".end .annotation_attr\n");
         return sb.toString();
      }
   }

   private String getVisibilityParameterAnnotationAttr(VisibilityParameterAnnotationTag tag) {
      StringBuffer sb = new StringBuffer();
      sb.append(".param ");
      if (tag.getKind() == 0) {
         sb.append(".runtime_visible_annotation\n");
      } else {
         sb.append(".runtime_invisible_annotation\n");
      }

      ArrayList<VisibilityAnnotationTag> vis_list = tag.getVisibilityAnnotations();
      if (vis_list != null) {
         Iterator var4 = vis_list.iterator();

         while(var4.hasNext()) {
            VisibilityAnnotationTag vat = (VisibilityAnnotationTag)var4.next();
            VisibilityAnnotationTag safeVat = vat == null ? this.getSafeVisibilityAnnotationTag(tag.getKind()) : vat;
            sb.append(this.getVisibilityAnnotationAttr(safeVat));
         }
      }

      sb.append(".end .param\n");
      return sb.toString();
   }

   private VisibilityAnnotationTag getSafeVisibilityAnnotationTag(int kind) {
      VisibilityAnnotationTag safeVat = (VisibilityAnnotationTag)safeVats.get(kind);
      if (safeVat == null) {
         safeVats.put(kind, safeVat = new VisibilityAnnotationTag(kind));
      }

      return safeVat;
   }

   private String getElemAttr(AnnotationElem elem) {
      StringBuffer result = new StringBuffer(".elem ");
      switch(elem.getKind()) {
      case '@':
         result.append(".ann_kind ");
         result.append("\"" + elem.getName() + "\"\n");
         AnnotationTag annot = ((AnnotationAnnotationElem)elem).getValue();
         result.append(".annotation ");
         result.append(StringTools.getQuotedStringOf(annot.getType()) + "\n");
         Iterator var7 = annot.getElems().iterator();

         while(var7.hasNext()) {
            AnnotationElem ae = (AnnotationElem)var7.next();
            result.append(this.getElemAttr(ae));
         }

         result.append(".end .annotation\n");
         result.append(".end .annot_elem\n");
         break;
      case 'A':
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      case 'a':
      case 'b':
      case 'd':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      default:
         throw new RuntimeException("Unknown Elem Attr Kind: " + elem.getKind());
      case 'B':
         result.append(".byte_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(((AnnotationIntElem)elem).getValue());
         result.append("\n");
         break;
      case 'C':
         result.append(".char_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(((AnnotationIntElem)elem).getValue());
         result.append("\n");
         break;
      case 'D':
         result.append(".doub_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(((AnnotationDoubleElem)elem).getValue());
         result.append("\n");
         break;
      case 'F':
         result.append(".float_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(((AnnotationFloatElem)elem).getValue());
         result.append("\n");
         break;
      case 'I':
         result.append(".int_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(((AnnotationIntElem)elem).getValue());
         result.append("\n");
         break;
      case 'J':
         result.append(".long_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(((AnnotationLongElem)elem).getValue());
         result.append("\n");
         break;
      case 'S':
         result.append(".short_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(((AnnotationIntElem)elem).getValue());
         result.append("\n");
         break;
      case 'Z':
         result.append(".bool_kind ");
         result.append("\"" + elem.getName() + "\" ");
         if (elem instanceof AnnotationIntElem) {
            result.append(((AnnotationIntElem)elem).getValue());
         } else if (((AnnotationBooleanElem)elem).getValue()) {
            result.append(1);
         } else {
            result.append(0);
         }

         result.append("\n");
         break;
      case '[':
         result.append(".arr_kind ");
         result.append("\"" + elem.getName() + "\" ");
         AnnotationArrayElem arrayElem = (AnnotationArrayElem)elem;
         result.append("\n");

         for(int i = 0; i < arrayElem.getNumValues(); ++i) {
            result.append(this.getElemAttr(arrayElem.getValueAt(i)));
         }

         result.append(".end .arr_elem\n");
         break;
      case 'c':
         result.append(".cls_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(StringTools.getQuotedStringOf(((AnnotationClassElem)elem).getDesc()));
         result.append("\n");
         break;
      case 'e':
         result.append(".enum_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(StringTools.getQuotedStringOf(((AnnotationEnumElem)elem).getTypeName()));
         result.append(" ");
         result.append(StringTools.getQuotedStringOf(((AnnotationEnumElem)elem).getConstantName()));
         result.append("\n");
         break;
      case 's':
         result.append(".str_kind ");
         result.append("\"" + elem.getName() + "\" ");
         result.append(StringTools.getQuotedStringOf(((AnnotationStringElem)elem).getValue()));
         result.append("\n");
      }

      return result.toString();
   }

   public AbstractJasminClass(SootClass sootClass) {
      if (Options.v().time()) {
         Timers.v().buildJasminTimer.start();
      }

      if (Options.v().verbose()) {
         logger.debug("[" + sootClass.getName() + "] Constructing baf.JasminClass...");
      }

      this.code = new LinkedList();
      int modifiers = sootClass.getModifiers();
      if (sootClass.getTag("SourceFileTag") != null && !Options.v().no_output_source_file_attribute()) {
         String srcName = ((SourceFileTag)sootClass.getTag("SourceFileTag")).getSourceFile();
         if (File.separatorChar == '\\') {
            srcName = srcName.replace('\\', '/');
         }

         srcName = StringTools.getEscapedStringOf(srcName);
         if (!Options.v().android_jars().isEmpty() && !srcName.isEmpty() && Character.isDigit(srcName.charAt(0))) {
            srcName = "n_" + srcName;
         }

         srcName = srcName.replace(" ", "-");
         srcName = srcName.replace("\"", "");
         if (!srcName.isEmpty()) {
            this.emit(".source " + srcName);
         }
      }

      if (Modifier.isInterface(modifiers)) {
         modifiers -= 512;
         this.emit(".interface " + Modifier.toString(modifiers) + " " + slashify(sootClass.getName()));
      } else {
         this.emit(".class " + Modifier.toString(modifiers) + " " + slashify(sootClass.getName()));
      }

      if (sootClass.hasSuperclass()) {
         this.emit(".super " + slashify(sootClass.getSuperclass().getName()));
      } else {
         this.emit(".no_super");
      }

      this.emit("");
      Iterator it = sootClass.getInterfaces().iterator();

      while(it.hasNext()) {
         SootClass inter = (SootClass)it.next();
         this.emit(".implements " + slashify(inter.getName()));
      }

      it = sootClass.getTags().iterator();

      while(it.hasNext()) {
         Tag tag = (Tag)it.next();
         if (tag instanceof Attribute) {
            this.emit(".class_attribute " + tag.getName() + " \"" + new String(Base64.encode(((Attribute)tag).getValue())) + "\"");
         }
      }

      if (sootClass.hasTag("SyntheticTag") || Modifier.isSynthetic(sootClass.getModifiers())) {
         this.emit(".synthetic\n");
      }

      InnerClassAttribute ica = (InnerClassAttribute)sootClass.getTag("InnerClassAttribute");
      Iterator vit;
      if (ica != null && ica.getSpecs().size() > 0 && !Options.v().no_output_inner_classes_attribute()) {
         this.emit(".inner_class_attr ");
         vit = ((InnerClassAttribute)sootClass.getTag("InnerClassAttribute")).getSpecs().iterator();

         while(vit.hasNext()) {
            InnerClassTag ict = (InnerClassTag)vit.next();
            this.emit(".inner_class_spec_attr \"" + ict.getInnerClass() + "\" \"" + ict.getOuterClass() + "\" \"" + ict.getShortName() + "\" " + Modifier.toString(ict.getAccessFlags()) + " .end .inner_class_spec_attr");
         }

         this.emit(".end .inner_class_attr\n");
      }

      String sigAttr;
      if (sootClass.hasTag("EnclosingMethodTag")) {
         sigAttr = ".enclosing_method_attr ";
         EnclosingMethodTag eMethTag = (EnclosingMethodTag)sootClass.getTag("EnclosingMethodTag");
         sigAttr = sigAttr + "\"" + eMethTag.getEnclosingClass() + "\" ";
         sigAttr = sigAttr + "\"" + eMethTag.getEnclosingMethod() + "\" ";
         sigAttr = sigAttr + "\"" + eMethTag.getEnclosingMethodSig() + "\"\n";
         this.emit(sigAttr);
      }

      if (sootClass.hasTag("DeprecatedTag")) {
         this.emit(".deprecated\n");
      }

      if (sootClass.hasTag("SignatureTag")) {
         sigAttr = ".signature_attr ";
         SignatureTag sigTag = (SignatureTag)sootClass.getTag("SignatureTag");
         sigAttr = sigAttr + "\"" + sigTag.getSignature() + "\"\n";
         this.emit(sigAttr);
      }

      vit = sootClass.getTags().iterator();

      while(vit.hasNext()) {
         Tag t = (Tag)vit.next();
         if (t.getName().equals("VisibilityAnnotationTag")) {
            this.emit(this.getVisibilityAnnotationAttr((VisibilityAnnotationTag)t));
         }
      }

      Iterator methodIt = sootClass.getFields().iterator();

      while(methodIt.hasNext()) {
         SootField field = (SootField)methodIt.next();
         String fieldString = ".field " + Modifier.toString(field.getModifiers()) + " \"" + field.getName() + "\" " + jasminDescriptorOf(field.getType());
         if (field.hasTag("StringConstantValueTag")) {
            fieldString = fieldString + " = ";
            fieldString = fieldString + StringTools.getQuotedStringOf(((StringConstantValueTag)field.getTag("StringConstantValueTag")).getStringValue());
         } else if (field.hasTag("IntegerConstantValueTag")) {
            fieldString = fieldString + " = ";
            fieldString = fieldString + ((IntegerConstantValueTag)field.getTag("IntegerConstantValueTag")).getIntValue();
         } else if (field.hasTag("LongConstantValueTag")) {
            fieldString = fieldString + " = ";
            fieldString = fieldString + ((LongConstantValueTag)field.getTag("LongConstantValueTag")).getLongValue();
         } else if (field.hasTag("FloatConstantValueTag")) {
            fieldString = fieldString + " = ";
            FloatConstantValueTag val = (FloatConstantValueTag)field.getTag("FloatConstantValueTag");
            fieldString = fieldString + this.floatToString(val.getFloatValue());
         } else if (field.hasTag("DoubleConstantValueTag")) {
            fieldString = fieldString + " = ";
            DoubleConstantValueTag val = (DoubleConstantValueTag)field.getTag("DoubleConstantValueTag");
            fieldString = fieldString + this.doubleToString(val.getDoubleValue());
         }

         if (field.hasTag("SyntheticTag") || Modifier.isSynthetic(field.getModifiers())) {
            fieldString = fieldString + " .synthetic";
         }

         fieldString = fieldString + "\n";
         if (field.hasTag("DeprecatedTag")) {
            fieldString = fieldString + ".deprecated\n";
         }

         if (field.hasTag("SignatureTag")) {
            fieldString = fieldString + ".signature_attr ";
            SignatureTag sigTag = (SignatureTag)field.getTag("SignatureTag");
            fieldString = fieldString + "\"" + sigTag.getSignature() + "\"\n";
         }

         Iterator vfit = field.getTags().iterator();

         while(vfit.hasNext()) {
            Tag t = (Tag)vfit.next();
            if (t.getName().equals("VisibilityAnnotationTag")) {
               fieldString = fieldString + this.getVisibilityAnnotationAttr((VisibilityAnnotationTag)t);
            }
         }

         this.emit(fieldString);
         Iterator attributeIt = field.getTags().iterator();

         while(attributeIt.hasNext()) {
            Tag tag = (Tag)attributeIt.next();
            if (tag instanceof Attribute) {
               this.emit(".field_attribute " + tag.getName() + " \"" + new String(Base64.encode(((Attribute)tag).getValue())) + "\"");
            }
         }
      }

      if (sootClass.getFieldCount() != 0) {
         this.emit("");
      }

      methodIt = sootClass.methodIterator();

      while(methodIt.hasNext()) {
         this.emitMethod((SootMethod)methodIt.next());
         this.emit("");
      }

      if (Options.v().time()) {
         Timers.v().buildJasminTimer.end();
      }

   }

   protected void assignColorsToLocals(Body body) {
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Assigning colors to locals...");
      }

      if (Options.v().time()) {
         Timers.v().packTimer.start();
      }

      this.localToGroup = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
      this.groupToColorCount = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
      this.localToColor = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
      Iterator codeIt = body.getLocals().iterator();

      while(codeIt.hasNext()) {
         Local l = (Local)codeIt.next();
         Object g;
         if (sizeOfType(l.getType()) == 1) {
            g = IntType.v();
         } else {
            g = LongType.v();
         }

         this.localToGroup.put(l, g);
         if (!this.groupToColorCount.containsKey(g)) {
            this.groupToColorCount.put(g, new Integer(0));
         }
      }

      codeIt = body.getUnits().iterator();

      while(codeIt.hasNext()) {
         Stmt s = (Stmt)codeIt.next();
         if (s instanceof IdentityStmt && ((IdentityStmt)s).getLeftOp() instanceof Local) {
            Local l = (Local)((IdentityStmt)s).getLeftOp();
            Object group = this.localToGroup.get(l);
            int count = (Integer)this.groupToColorCount.get(group);
            this.localToColor.put(l, new Integer(count));
            ++count;
            this.groupToColorCount.put(group, new Integer(count));
         }
      }

   }

   protected void emitMethod(SootMethod method) {
      if (!method.isPhantom()) {
         this.emit(".method " + Modifier.toString(method.getModifiers()) + " " + method.getName() + jasminDescriptorOf(method.makeRef()));
         Iterator throwsIt = method.getExceptions().iterator();

         while(throwsIt.hasNext()) {
            SootClass exceptClass = (SootClass)throwsIt.next();
            this.emit(".throws " + exceptClass.getName());
         }

         if (method.hasTag("SyntheticTag") || Modifier.isSynthetic(method.getModifiers())) {
            this.emit(".synthetic");
         }

         if (method.hasTag("DeprecatedTag")) {
            this.emit(".deprecated");
         }

         String annotDefAttr;
         if (method.hasTag("SignatureTag")) {
            annotDefAttr = ".signature_attr ";
            SignatureTag sigTag = (SignatureTag)method.getTag("SignatureTag");
            annotDefAttr = annotDefAttr + "\"" + sigTag.getSignature() + "\"";
            this.emit(annotDefAttr);
         }

         if (method.hasTag("AnnotationDefaultTag")) {
            annotDefAttr = ".annotation_default ";
            AnnotationDefaultTag annotDefTag = (AnnotationDefaultTag)method.getTag("AnnotationDefaultTag");
            annotDefAttr = annotDefAttr + this.getElemAttr(annotDefTag.getDefaultVal());
            annotDefAttr = annotDefAttr + ".end .annotation_default";
            this.emit(annotDefAttr);
         }

         Iterator vit = method.getTags().iterator();

         while(vit.hasNext()) {
            Tag t = (Tag)vit.next();
            if (t.getName().equals("VisibilityAnnotationTag")) {
               this.emit(this.getVisibilityAnnotationAttr((VisibilityAnnotationTag)t));
            }

            if (t.getName().equals("VisibilityParameterAnnotationTag")) {
               this.emit(this.getVisibilityParameterAnnotationAttr((VisibilityParameterAnnotationTag)t));
            }
         }

         if (method.isConcrete()) {
            if (!method.hasActiveBody()) {
               throw new RuntimeException("method: " + method.getName() + " has no active body!");
            }

            this.emitMethodBody(method);
         }

         this.emit(".end method");
         Iterator it = method.getTags().iterator();

         while(it.hasNext()) {
            Tag tag = (Tag)it.next();
            if (tag instanceof Attribute) {
               this.emit(".method_attribute " + tag.getName() + " \"" + new String(Base64.encode(tag.getValue())) + "\"");
            }
         }

      }
   }

   protected abstract void emitMethodBody(SootMethod var1);

   public void print(PrintWriter out) {
      Iterator var2 = this.code.iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         out.println(s);
      }

   }

   protected String doubleToString(DoubleConstant v) {
      String s = v.toString();
      if (s.equals("#Infinity")) {
         s = "+DoubleInfinity";
      } else if (s.equals("#-Infinity")) {
         s = "-DoubleInfinity";
      } else if (s.equals("#NaN")) {
         s = "+DoubleNaN";
      }

      return s;
   }

   protected String doubleToString(double d) {
      String doubleString = (new Double(d)).toString();
      if (doubleString.equals("NaN")) {
         return "+DoubleNaN";
      } else if (doubleString.equals("Infinity")) {
         return "+DoubleInfinity";
      } else {
         return doubleString.equals("-Infinity") ? "-DoubleInfinity" : doubleString;
      }
   }

   protected String floatToString(FloatConstant v) {
      String s = v.toString();
      if (s.equals("#InfinityF")) {
         s = "+FloatInfinity";
      } else if (s.equals("#-InfinityF")) {
         s = "-FloatInfinity";
      } else if (s.equals("#NaNF")) {
         s = "+FloatNaN";
      }

      return s;
   }

   protected String floatToString(float d) {
      String floatString = (new Float(d)).toString();
      if (floatString.equals("NaN")) {
         return "+FloatNaN";
      } else if (floatString.equals("Infinity")) {
         return "+FloatInfinity";
      } else {
         return floatString.equals("-Infinity") ? "-FloatInfinity" : floatString;
      }
   }
}
