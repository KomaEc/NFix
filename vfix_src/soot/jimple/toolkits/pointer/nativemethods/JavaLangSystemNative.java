package soot.jimple.toolkits.pointer.nativemethods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.toolkits.pointer.representations.Environment;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;

public class JavaLangSystemNative extends NativeMethodClass {
   private static final Logger logger = LoggerFactory.getLogger(JavaLangSystemNative.class);

   public JavaLangSystemNative(NativeHelper helper) {
      super(helper);
   }

   public void simulateMethod(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      String subSignature = method.getSubSignature();
      if (subSignature.equals("void arraycopy(java.lang.Object,int,java.lang.Object,int,int)")) {
         this.java_lang_System_arraycopy(method, thisVar, returnVar, params);
      } else if (subSignature.equals("void setIn0(java.io.InputStream)")) {
         this.java_lang_System_setIn0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("void setOut0(java.io.PrintStream)")) {
         this.java_lang_System_setOut0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("void setErr0(java.io.PrintStream)")) {
         this.java_lang_System_setErr0(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.util.Properties initProperties(java.util.Properties)")) {
         this.java_lang_System_initProperties(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.String mapLibraryName(java.lang.String)")) {
         this.java_lang_System_mapLibraryName(method, thisVar, returnVar, params);
      } else if (subSignature.equals("java.lang.Class getCallerClass()")) {
         this.java_lang_System_getCallerClass(method, thisVar, returnVar, params);
      } else {
         defaultMethod(method, thisVar, returnVar, params);
      }
   }

   public void java_lang_System_arraycopy(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable srcElm = this.helper.arrayElementOf(params[0]);
      ReferenceVariable dstElm = this.helper.arrayElementOf(params[2]);
      ReferenceVariable tmpVar = this.helper.tempLocalVariable(method);
      this.helper.assign(tmpVar, srcElm);
      this.helper.assign(dstElm, tmpVar);
   }

   public void java_lang_System_setIn0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable sysIn = this.helper.staticField("java.lang.System", "in");
      this.helper.assign(sysIn, params[0]);
   }

   public void java_lang_System_setOut0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable sysOut = this.helper.staticField("java.lang.System", "out");
      this.helper.assign(sysOut, params[0]);
   }

   public void java_lang_System_setErr0(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable sysErr = this.helper.staticField("java.lang.System", "err");
      this.helper.assign(sysErr, params[0]);
   }

   public void java_lang_System_initProperties(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      ReferenceVariable sysProps = this.helper.staticField("java.lang.System", "props");
      this.helper.assign(returnVar, sysProps);
      this.helper.assign(sysProps, params[0]);
   }

   public void java_lang_System_mapLibraryName(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getStringObject());
   }

   public void java_lang_System_getCallerClass(SootMethod method, ReferenceVariable thisVar, ReferenceVariable returnVar, ReferenceVariable[] params) {
      this.helper.assignObjectTo(returnVar, Environment.v().getClassObject());
   }
}
