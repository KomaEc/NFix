package soot.sootify;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Local;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleValueSwitch;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MethodHandle;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

public class ValueTemplatePrinter implements JimpleValueSwitch {
   private final TemplatePrinter p;
   private final TypeTemplatePrinter ttp;
   private String varName;
   private Set<String> varnamesAlreadyUsed = new HashSet();

   public ValueTemplatePrinter(TemplatePrinter p) {
      this.p = p;
      this.ttp = new TypeTemplatePrinter(p);
      this.varnamesAlreadyUsed.add("b");
      this.varnamesAlreadyUsed.add("m");
      this.varnamesAlreadyUsed.add("units");
   }

   public String printValueAssignment(Value value, String varName) {
      this.suggestVariableName(varName);
      value.apply(this);
      return this.getLastAssignedVarName();
   }

   private void printConstant(Value v, String... ops) {
      String stmtClassName = v.getClass().getSimpleName();
      this.p.print("Value " + this.varName + " = ");
      this.p.printNoIndent(stmtClassName);
      this.p.printNoIndent(".v(");
      int i = 1;
      String[] var5 = ops;
      int var6 = ops.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String op = var5[var7];
         this.p.printNoIndent(op);
         if (i < ops.length) {
            this.p.printNoIndent(",");
         }

         ++i;
      }

      this.p.printNoIndent(")");
      this.p.printlnNoIndent(";");
   }

   private void printExpr(Value v, String... ops) {
      String stmtClassName = v.getClass().getSimpleName();
      if (stmtClassName.charAt(0) == 'J') {
         stmtClassName = stmtClassName.substring(1);
      }

      this.p.print("Value " + this.varName + " = ");
      this.printFactoryMethodCall(stmtClassName, ops);
      this.p.printlnNoIndent(";");
   }

   private void printFactoryMethodCall(String stmtClassName, String... ops) {
      this.p.printNoIndent("Jimple.v().new");
      this.p.printNoIndent(stmtClassName);
      this.p.printNoIndent("(");
      int i = 1;
      String[] var4 = ops;
      int var5 = ops.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String op = var4[var6];
         this.p.printNoIndent(op);
         if (i < ops.length) {
            this.p.printNoIndent(",");
         }

         ++i;
      }

      this.p.printNoIndent(")");
   }

   public void suggestVariableName(String name) {
      int i = 0;

      String actualName;
      do {
         actualName = name + i;
         ++i;
      } while(this.varnamesAlreadyUsed.contains(actualName));

      this.varName = actualName;
      this.varnamesAlreadyUsed.add(actualName);
   }

   public String getLastAssignedVarName() {
      return this.varName;
   }

   public void caseDoubleConstant(DoubleConstant v) {
      this.printConstant(v, Double.toString(v.value));
   }

   public void caseFloatConstant(FloatConstant v) {
      this.printConstant(v, Float.toString(v.value));
   }

   public void caseIntConstant(IntConstant v) {
      this.printConstant(v, Integer.toString(v.value));
   }

   public void caseLongConstant(LongConstant v) {
      this.printConstant(v, Long.toString(v.value));
   }

   public void caseNullConstant(NullConstant v) {
      this.printConstant(v);
   }

   public void caseStringConstant(StringConstant v) {
      this.printConstant(v, "\"" + v.value + "\"");
   }

   public void caseClassConstant(ClassConstant v) {
      this.printConstant(v, "\"" + v.value + "\"");
   }

   public void caseAddExpr(AddExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseMethodHandle(MethodHandle handle) {
      throw new UnsupportedOperationException("we have not yet determined how to print Java 7 method handles");
   }

   private void printBinaryExpr(BinopExpr v) {
      String className = v.getClass().getSimpleName();
      if (className.charAt(0) == 'J') {
         className = className.substring(1);
      }

      String oldName = this.varName;
      Value left = v.getOp1();
      String v1 = this.printValueAssignment(left, "left");
      Value right = v.getOp2();
      String v2 = this.printValueAssignment(right, "right");
      this.p.println("Value " + oldName + " = Jimple.v().new" + className + "(" + v1 + "," + v2 + ");");
      this.varName = oldName;
   }

   public void caseAndExpr(AndExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseCmpExpr(CmpExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseCmpgExpr(CmpgExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseCmplExpr(CmplExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseDivExpr(DivExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseEqExpr(EqExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseNeExpr(NeExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseGeExpr(GeExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseGtExpr(GtExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseLeExpr(LeExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseLtExpr(LtExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseMulExpr(MulExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseOrExpr(OrExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseRemExpr(RemExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseShlExpr(ShlExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseShrExpr(ShrExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseUshrExpr(UshrExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseSubExpr(SubExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseXorExpr(XorExpr v) {
      this.printBinaryExpr(v);
   }

   public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {
      this.printInvokeExpr(v);
   }

   private void printInvokeExpr(InvokeExpr v) {
      this.p.openBlock();
      String oldName = this.varName;
      SootMethodRef method = v.getMethodRef();
      SootMethod m = method.resolve();
      if (!m.isStatic()) {
         Local base = (Local)((InstanceInvokeExpr)v).getBase();
         this.p.println("Local base = localByName(b,\"" + base.getName() + "\");");
      }

      this.p.println("List<Type> parameterTypes = new LinkedList<Type>();");
      int i = 0;

      for(Iterator var6 = m.getParameterTypes().iterator(); var6.hasNext(); ++i) {
         Type t = (Type)var6.next();
         this.ttp.setVariableName("type" + i);
         t.apply(this.ttp);
         this.p.println("parameterTypes.add(type" + i + ");");
      }

      this.ttp.setVariableName("returnType");
      m.getReturnType().apply(this.ttp);
      this.p.print("SootMethodRef methodRef = ");
      this.p.printNoIndent("Scene.v().makeMethodRef(");
      String className = m.getDeclaringClass().getName();
      this.p.printNoIndent("Scene.v().getSootClass(\"" + className + "\"),");
      this.p.printNoIndent("\"" + m.getName() + "\",");
      this.p.printNoIndent("parameterTypes,");
      this.p.printNoIndent("returnType,");
      this.p.printlnNoIndent(m.isStatic() + ");");
      this.printExpr(v, "base", "methodRef");
      this.varName = oldName;
      this.p.closeBlock();
   }

   public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
      this.printInvokeExpr(v);
   }

   public void caseStaticInvokeExpr(StaticInvokeExpr v) {
      this.printInvokeExpr(v);
   }

   public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {
      this.printInvokeExpr(v);
   }

   public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
      this.printInvokeExpr(v);
   }

   public void caseCastExpr(CastExpr v) {
      String oldName = this.varName;
      this.suggestVariableName("type");
      String lhsName = this.varName;
      this.ttp.setVariableName(this.varName);
      v.getType().apply(this.ttp);
      String rhsName = this.printValueAssignment(v.getOp(), "op");
      this.p.println("Value " + oldName + " = Jimple.v().newCastExpr(" + lhsName + "," + rhsName + ");");
      this.varName = oldName;
   }

   public void caseInstanceOfExpr(InstanceOfExpr v) {
      String oldName = this.varName;
      this.suggestVariableName("type");
      String lhsName = this.varName;
      this.ttp.setVariableName(this.varName);
      v.getType().apply(this.ttp);
      String rhsName = this.printValueAssignment(v.getOp(), "op");
      this.p.println("Value " + oldName + " = Jimple.v().newInstanceOfExpr(" + lhsName + "," + rhsName + ");");
      this.varName = oldName;
   }

   public void caseNewArrayExpr(NewArrayExpr v) {
      String oldName = this.varName;
      Value size = v.getSize();
      this.suggestVariableName("size");
      String sizeName = this.varName;
      size.apply(this);
      this.suggestVariableName("type");
      String lhsName = this.varName;
      this.ttp.setVariableName(this.varName);
      v.getType().apply(this.ttp);
      this.p.println("Value " + oldName + " = Jimple.v().newNewArrayExpr(" + lhsName + ", " + sizeName + ");");
      this.varName = oldName;
   }

   public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {
      this.p.openBlock();
      String oldName = this.varName;
      this.ttp.setVariableName("arrayType");
      v.getType().apply(this.ttp);
      this.p.println("List<IntConstant> sizes = new LinkedList<IntConstant>();");
      int i = 0;
      Iterator var4 = v.getSizes().iterator();

      while(var4.hasNext()) {
         Value s = (Value)var4.next();
         this.suggestVariableName("size" + i);
         s.apply(this);
         ++i;
         this.p.println("sizes.add(sizes" + i + ");");
      }

      this.p.println("Value " + oldName + " = Jimple.v().newNewMultiArrayExpr(arrayType, sizes);");
      this.varName = oldName;
      this.p.closeBlock();
   }

   public void caseNewExpr(NewExpr v) {
      String oldName = this.varName;
      this.suggestVariableName("type");
      String typeName = this.varName;
      this.ttp.setVariableName(this.varName);
      v.getType().apply(this.ttp);
      this.p.println("Value " + oldName + " = Jimple.v().newNewExpr(" + typeName + ");");
      this.varName = oldName;
   }

   public void caseLengthExpr(LengthExpr v) {
      String oldName = this.varName;
      Value op = v.getOp();
      this.suggestVariableName("op");
      String opName = this.varName;
      op.apply(this);
      this.p.println("Value " + oldName + " = Jimple.v().newLengthExpr(" + opName + ");");
      this.varName = oldName;
   }

   public void caseNegExpr(NegExpr v) {
      String oldName = this.varName;
      Value op = v.getOp();
      this.suggestVariableName("op");
      String opName = this.varName;
      op.apply(this);
      this.p.println("Value " + oldName + " = Jimple.v().newNegExpr(" + opName + ");");
      this.varName = oldName;
   }

   public void caseArrayRef(ArrayRef v) {
      String oldName = this.varName;
      Value base = v.getBase();
      this.suggestVariableName("base");
      String baseName = this.varName;
      base.apply(this);
      Value index = v.getIndex();
      this.suggestVariableName("index");
      String indexName = this.varName;
      index.apply(this);
      this.p.println("Value " + oldName + " = Jimple.v().newArrayRef(" + baseName + ", " + indexName + ");");
      this.varName = oldName;
   }

   public void caseStaticFieldRef(StaticFieldRef v) {
      this.printFieldRef(v);
   }

   private void printFieldRef(FieldRef v) {
      String refTypeName = v.getClass().getSimpleName();
      this.p.openBlock();
      String oldName = this.varName;
      SootField f = v.getField();
      this.ttp.setVariableName("type");
      f.getType().apply(this.ttp);
      this.p.print("SootFieldRef fieldRef = ");
      this.p.printNoIndent("Scene.v().makeFieldRef(");
      String className = f.getDeclaringClass().getName();
      this.p.printNoIndent("Scene.v().getSootClass(\"" + className + "\"),");
      this.p.printNoIndent("\"" + f.getName() + "\",");
      this.p.printNoIndent("type,");
      this.p.printNoIndent(f.isStatic() + ");");
      this.p.println("Value " + oldName + " = Jimple.v().new" + refTypeName + "(fieldRef);");
      this.varName = oldName;
      this.p.closeBlock();
   }

   public void caseInstanceFieldRef(InstanceFieldRef v) {
      this.printFieldRef(v);
   }

   public void caseParameterRef(ParameterRef v) {
      String oldName = this.varName;
      Type paramType = v.getType();
      this.suggestVariableName("paramType");
      String paramTypeName = this.varName;
      this.ttp.setVariableName(paramTypeName);
      paramType.apply(this.ttp);
      int number = v.getIndex();
      this.suggestVariableName("number");
      this.p.println("int " + this.varName + "=" + number + ";");
      this.p.println("Value " + oldName + " = Jimple.v().newParameterRef(" + paramTypeName + ", " + this.varName + ");");
      this.varName = oldName;
   }

   public void caseCaughtExceptionRef(CaughtExceptionRef v) {
      this.p.println("Value " + this.varName + " = Jimple.v().newCaughtExceptionRef();");
   }

   public void caseThisRef(ThisRef v) {
      String oldName = this.varName;
      Type paramType = v.getType();
      this.suggestVariableName("type");
      String typeName = this.varName;
      this.ttp.setVariableName(typeName);
      paramType.apply(this.ttp);
      this.p.println("Value " + oldName + " = Jimple.v().newThisRef(" + typeName + ");");
      this.varName = oldName;
   }

   public void caseLocal(Local l) {
      String oldName = this.varName;
      this.p.println("Local " + this.varName + " = localByName(b,\"" + l.getName() + "\");");
      this.varName = oldName;
   }

   public void defaultCase(Object object) {
      throw new InternalError();
   }
}
