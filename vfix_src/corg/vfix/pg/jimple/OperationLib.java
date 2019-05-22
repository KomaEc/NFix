package corg.vfix.pg.jimple;

import com.github.javaparser.ast.CompilationUnit;
import corg.vfix.pg.FileOperationLib;
import corg.vfix.sa.vfg.VFGNode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JEqExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JNeExpr;
import soot.util.Chain;
import soot.util.JasminOutputStream;

public class OperationLib {
   private static String outputPath;
   private static int patchID = 0;

   public static int getPatchID() {
      return patchID;
   }

   public static void clearPatchID() {
      patchID = 0;
   }

   public static void init(String op) {
      outputPath = op;
   }

   public static void helloWorld() throws FileNotFoundException, IOException {
      Scene.v().loadClassAndSupport("java.lang.Object");
      Scene.v().loadClassAndSupport("java.lang.System");
      SootClass sClass = new SootClass("HelloWorld", 1);
      sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
      Scene.v().addClass(sClass);
      SootMethod method = new SootMethod("main", Arrays.asList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v(), 9);
      sClass.addMethod(method);
      JimpleBody body = Jimple.v().newBody(method);
      method.setActiveBody(body);
      Chain<Unit> units = body.getUnits();
      Local arg = Jimple.v().newLocal("l0", ArrayType.v(RefType.v("java.lang.String"), 1));
      body.getLocals().add(arg);
      Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
      body.getLocals().add(tmpRef);
      units.add(Jimple.v().newIdentityStmt(arg, Jimple.v().newParameterRef(ArrayType.v(RefType.v("java.lang.String"), 1), 0)));
      units.add(Jimple.v().newAssignStmt(tmpRef, Jimple.v().newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));
      SootMethod toCall = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
      units.add(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), (Value)StringConstant.v("Hello world!"))));
      units.add(Jimple.v().newReturnVoidStmt());
      System.out.println(units);
      outputClassFile(sClass);
   }

   public static void insertNullCheck(Chain<Unit> units, Value nullObject, Stmt nullStmt, Stmt actionStmt) {
      units.insertBefore((Object)actionStmt, nullStmt);
      JNeExpr condition = new JNeExpr(nullObject, NullConstant.v());
      IfStmt ifStmt = Jimple.v().newIfStmt(condition, (Unit)nullStmt);
      units.insertBefore((Object)ifStmt, actionStmt);
   }

   public static void insertReplaceBefore(Chain<Unit> units, Value variable, Value nullObject, Stmt targetStmt, Value value) {
      ArrayList<Stmt> actionStmts = new ArrayList();
      AssignStmt assignStmt = Jimple.v().newAssignStmt(variable, value);
      actionStmts.add(assignStmt);
      GotoStmt gotoStmt = Jimple.v().newGotoStmt((Unit)units.getSuccOf(targetStmt));
      actionStmts.add(gotoStmt);
      insertNullCheck(units, nullObject, targetStmt, actionStmts);
   }

   public static void insertReplaceBefore(Chain<Unit> units, Value variable, Value nullObject, Stmt targetStmt, ArrayType type) {
      ArrayList<Stmt> actionStmts = new ArrayList();
      NewArrayExpr newArrayExpr = Jimple.v().newNewArrayExpr(type, IntConstant.v(0));
      AssignStmt assignStmt = Jimple.v().newAssignStmt(variable, newArrayExpr);
      actionStmts.add(assignStmt);
      GotoStmt gotoStmt = Jimple.v().newGotoStmt((Unit)units.getSuccOf(targetStmt));
      actionStmts.add(gotoStmt);
      insertNullCheck(units, nullObject, targetStmt, actionStmts);
   }

   public static void insertReplaceBefore(Chain<Unit> units, Value variable, Value nullObject, Stmt targetStmt, SootMethod constructMethod) {
      ArrayList<Stmt> actionStmts = new ArrayList();
      Type type = constructMethod.getDeclaringClass().getType();
      NewExpr newExpr = Jimple.v().newNewExpr((RefType)type);
      AssignStmt assignStmt = Jimple.v().newAssignStmt(variable, newExpr);
      actionStmts.add(assignStmt);
      SpecialInvokeExpr sInvokeExpr = Jimple.v().newSpecialInvokeExpr((Local)variable, constructMethod.makeRef());
      JInvokeStmt sInvokeStmt = (JInvokeStmt)Jimple.v().newInvokeStmt(sInvokeExpr);
      actionStmts.add(sInvokeStmt);
      GotoStmt gotoStmt = Jimple.v().newGotoStmt((Unit)units.getSuccOf(targetStmt));
      actionStmts.add(gotoStmt);
      insertNullCheck(units, nullObject, targetStmt, actionStmts);
   }

   public static void insertInitBefore(Chain<Unit> units, Value variable, Stmt targetStmt, SootMethod constructMethod) {
      ArrayList<Stmt> actionStmts = new ArrayList();
      Type type = constructMethod.getDeclaringClass().getType();
      NewExpr newExpr = Jimple.v().newNewExpr((RefType)type);
      AssignStmt assignStmt = Jimple.v().newAssignStmt(variable, newExpr);
      actionStmts.add(assignStmt);
      SpecialInvokeExpr sInvokeExpr = Jimple.v().newSpecialInvokeExpr((Local)variable, constructMethod.makeRef());
      JInvokeStmt sInvokeStmt = (JInvokeStmt)Jimple.v().newInvokeStmt(sInvokeExpr);
      actionStmts.add(sInvokeStmt);
      insertNullCheck(units, variable, targetStmt, actionStmts);
   }

   public static void insertArrayInitBefore(Chain<Unit> units, Value variable, Stmt targetStmt, ArrayType arrayType) {
      ArrayList<Stmt> actionStmts = new ArrayList();
      Type type = arrayType.getElementType();
      NewArrayExpr newArrayExpr = Jimple.v().newNewArrayExpr(type, IntConstant.v(0));
      AssignStmt assignStmt = Jimple.v().newAssignStmt(variable, newArrayExpr);
      actionStmts.add(assignStmt);
      insertNullCheck(units, variable, targetStmt, actionStmts);
   }

   public static void insertNullCheck(Chain<Unit> units, Value nullObject, Stmt nullStmt, ArrayList<Stmt> actionStmts) {
      Stmt firstActionStmt = (Stmt)actionStmts.get(0);
      Iterator var6 = actionStmts.iterator();

      while(var6.hasNext()) {
         Stmt stmt = (Stmt)var6.next();
         units.insertBefore((Object)stmt, nullStmt);
      }

      JNeExpr condition = new JNeExpr(nullObject, NullConstant.v());
      IfStmt ifStmt = Jimple.v().newIfStmt(condition, (Unit)nullStmt);
      units.insertBefore((Object)ifStmt, firstActionStmt);
   }

   public static void insertNullCheckSkipTo(Chain<Unit> units, Value nullObject, Stmt nullStmt, Stmt targetStmt) {
      JEqExpr condition = new JEqExpr(nullObject, NullConstant.v());
      IfStmt ifStmt = Jimple.v().newIfStmt(condition, (Unit)targetStmt);
      units.insertBefore((Object)ifStmt, nullStmt);
   }

   public static ArrayList<Stmt> makeNewRetStmts(Body body, Type type, VFGNode node) {
      ArrayList<Stmt> newRetStmts = new ArrayList();
      ReturnStmt newRetStmt;
      if (type instanceof RefType) {
         SootMethod constructor = getImplementedConstructor(type);
         if (constructor == null) {
            newRetStmt = Jimple.v().newReturnStmt(NullConstant.v());
            newRetStmts.add(newRetStmt);
            node.setNewClsName((SootMethod)null);
         } else {
            node.setNewClsName(constructor);
            Local arg = Jimple.v().newLocal("ret0", type);
            body.getLocals().add(arg);
            NewExpr newExpr = Jimple.v().newNewExpr((RefType)type);
            AssignStmt assignStmt = Jimple.v().newAssignStmt(arg, newExpr);
            newRetStmts.add(assignStmt);
            SpecialInvokeExpr sInvokeExpr = Jimple.v().newSpecialInvokeExpr(arg, constructor.makeRef());
            JInvokeStmt sInvokeStmt = (JInvokeStmt)Jimple.v().newInvokeStmt(sInvokeExpr);
            newRetStmts.add(sInvokeStmt);
            ReturnStmt newRetStmt = Jimple.v().newReturnStmt(arg);
            newRetStmts.add(newRetStmt);
         }
      } else if (type instanceof PrimType) {
         Value returnValue = getDefaultValue(type);
         newRetStmt = Jimple.v().newReturnStmt(returnValue);
         newRetStmts.add(newRetStmt);
      } else if (type instanceof VoidType) {
         ReturnVoidStmt newRetStmt = Jimple.v().newReturnVoidStmt();
         newRetStmts.add(newRetStmt);
      } else if (type instanceof ArrayType) {
         ReturnStmt newRetStmt = Jimple.v().newReturnStmt(NullConstant.v());
         newRetStmts.add(newRetStmt);
      }

      return newRetStmts;
   }

   public static void replaceNullAssignWithNew(Body body, Stmt stmt, SootMethod constructMethod) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         if (assign.getRightOp() instanceof NullConstant) {
            Type type = constructMethod.getDeclaringClass().getType();
            Local temp = Jimple.v().newLocal("$n0", type);
            body.getLocals().add(temp);
            NewExpr newExpr = Jimple.v().newNewExpr((RefType)type);
            AssignStmt assignStmt = Jimple.v().newAssignStmt(temp, newExpr);
            SpecialInvokeExpr sInvokeExpr = Jimple.v().newSpecialInvokeExpr(temp, constructMethod.makeRef());
            JInvokeStmt sInvokeStmt = (JInvokeStmt)Jimple.v().newInvokeStmt(sInvokeExpr);
            Chain<Unit> units = body.getUnits();
            List<Unit> newUnits = new ArrayList();
            newUnits.add(assignStmt);
            newUnits.add(sInvokeStmt);
            units.insertBefore((List)newUnits, stmt);
            assign.setRightOp(temp);
         }
      }
   }

   public static void replaceNullAssignWithNewArray(Body body, Stmt stmt, Type type) {
      if (stmt instanceof AssignStmt) {
         AssignStmt assign = (AssignStmt)stmt;
         if (assign.getRightOp() instanceof NullConstant) {
            if (type instanceof ArrayType) {
               Type elementType = ((ArrayType)type).getElementType();
               Local temp = Jimple.v().newLocal("$n0", type);
               body.getLocals().add(temp);
               NewArrayExpr newArrayExpr = Jimple.v().newNewArrayExpr(elementType, IntConstant.v(0));
               AssignStmt assignStmt = Jimple.v().newAssignStmt(temp, newArrayExpr);
               Chain<Unit> units = body.getUnits();
               units.insertBefore((Object)assignStmt, stmt);
               assign.setRightOp(temp);
            }
         }
      }
   }

   public static void replaceNullRetWithNew(Body body, Stmt stmt, SootMethod constructMethod) {
      if (stmt instanceof ReturnStmt) {
         ReturnStmt ret = (ReturnStmt)stmt;
         Type type = constructMethod.getDeclaringClass().getType();
         Local temp = Jimple.v().newLocal("$n1", type);
         body.getLocals().add(temp);
         NewExpr newExpr = Jimple.v().newNewExpr((RefType)type);
         AssignStmt assignStmt = Jimple.v().newAssignStmt(temp, newExpr);
         SpecialInvokeExpr sInvokeExpr = Jimple.v().newSpecialInvokeExpr(temp, constructMethod.makeRef());
         JInvokeStmt sInvokeStmt = (JInvokeStmt)Jimple.v().newInvokeStmt(sInvokeExpr);
         Chain<Unit> units = body.getUnits();
         List<Unit> newUnits = new ArrayList();
         newUnits.add(assignStmt);
         newUnits.add(sInvokeStmt);
         units.insertBefore((List)newUnits, stmt);
         ret.setOp(temp);
      }
   }

   public static void replaceNullRetWithNewArray(Body body, Stmt stmt, Type type) {
      if (stmt instanceof ReturnStmt) {
         if (type instanceof ArrayType) {
            Type elementType = ((ArrayType)type).getElementType();
            ReturnStmt ret = (ReturnStmt)stmt;
            Local temp = Jimple.v().newLocal("$n1", type);
            body.getLocals().add(temp);
            NewArrayExpr newArrayExpr = Jimple.v().newNewArrayExpr(elementType, IntConstant.v(0));
            AssignStmt assignStmt = Jimple.v().newAssignStmt(temp, newArrayExpr);
            Chain<Unit> units = body.getUnits();
            units.insertBefore((Object)assignStmt, stmt);
            ret.setOp(temp);
         }
      }
   }

   public static void repalce(Chain<Unit> units, Stmt oldStmt, List<Unit> newStmts) {
      units.insertBefore((List)newStmts, oldStmt);
      units.remove(oldStmt);
   }

   public static void replace(Chain<Unit> units, Stmt oldStmt, Stmt newStmt) {
      units.insertBefore((Object)newStmt, oldStmt);
      units.remove(oldStmt);
   }

   public static void insertStmtBefore(Chain<Stmt> units, Stmt stmt, Stmt targetStmt) {
      units.insertBefore((Object)stmt, targetStmt);
   }

   public static void insertNullCheckReturn(Chain<Stmt> units, Value nullObject, Stmt targetStmt, Value returnValue) {
      JNeExpr condition = new JNeExpr(nullObject, NullConstant.v());
      ReturnStmt returnStmt = Jimple.v().newReturnStmt(returnValue);
      IfStmt ifStmt = Jimple.v().newIfStmt(condition, (Unit)((Unit)units.getSuccOf(targetStmt)));
      units.insertBefore((Object)ifStmt, targetStmt);
      units.insertBefore((Object)returnStmt, targetStmt);
   }

   private static void outputClassFile(SootMethod sMethod) throws IOException {
      SootClass sClass = sMethod.getDeclaringClass();
      outputClassFile(sClass);
   }

   public static void outputClassFile(SootClass sClass) throws IOException {
      String filename = getClassFileName(sClass);
      FileOperationLib.createDir(filename);
      OutputStream streamOut = new JasminOutputStream(new FileOutputStream(filename));
      PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
      JasminClass jasminClass = new JasminClass(sClass);
      jasminClass.print(writerOut);
      writerOut.flush();
      streamOut.close();
      System.out.println("Class File: " + filename);
   }

   private static String getClassFileName(SootClass sClass) {
      String filename = SourceLocator.v().getFileNameFor(sClass, 14);
      filename = outputPath + "/" + patchID + "/classes" + filename.replace("sootOutput", "");
      return filename;
   }

   private static void outputJimpleFile(Body body, String name) {
      String filename = outputPath + "/" + patchID + "/jimple/" + name + ".jimple";
      FileOperationLib.createDir(filename);

      try {
         File writename = new File(filename);
         writename.createNewFile();
         BufferedWriter out = new BufferedWriter(new FileWriter(writename));
         out.write(body.toString());
         out.flush();
         out.close();
         System.out.println("Jimple File: " + filename);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void outputJavaFile(CompilationUnit cu, String name) {
      if (cu != null) {
         String filename = outputPath + "/" + patchID + "/java/" + name;
         FileOperationLib.createDir(filename);

         try {
            File writename = new File(filename);
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(cu.toString());
            out.flush();
            out.close();
            System.out.println("Java File: " + filename);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }

   public static SootMethod getImplementedConstructor(Type type) {
      SootClass sClass = Scene.v().getSootClass(type.toString());
      if (sClass == null) {
         System.out.println("Class " + type.toString() + " not found");
         return null;
      } else {
         SootMethod constructMtd = findConstructor(sClass);
         if (constructMtd != null) {
            return constructMtd;
         } else {
            FastHierarchy classHierarchy = Scene.v().getFastHierarchy();
            Collection<SootClass> subClassList = new ArrayList();
            subClassList.addAll(classHierarchy.getSubclassesOf(sClass));
            subClassList.addAll(classHierarchy.getAllImplementersOfInterface(sClass));
            Iterator var6 = subClassList.iterator();

            while(var6.hasNext()) {
               SootClass cls = (SootClass)var6.next();
               constructMtd = findConstructor(cls);
               if (constructMtd != null) {
                  return constructMtd;
               }
            }

            return null;
         }
      }
   }

   public static SootMethod findConstructor(SootClass sClass) {
      if (sClass == null) {
         System.out.println("Class not found");
         return null;
      } else if (Modifier.isAbstract(sClass.getModifiers())) {
         return null;
      } else {
         Iterator var2 = sClass.getMethods().iterator();

         SootMethod method;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            method = (SootMethod)var2.next();
         } while(!method.isConstructor() || method.getParameterCount() != 0 || !Modifier.isPublic(method.getModifiers()));

         return method;
      }
   }

   public static Stmt locateStmt(Body body, Stmt stmt) {
      if (stmt == null) {
         return null;
      } else {
         Iterator var3 = body.getUnits().iterator();

         Unit unit;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            unit = (Unit)var3.next();
         } while(unit.getJavaSourceStartColumnNumber() != stmt.getJavaSourceStartColumnNumber() || unit.getJavaSourceStartLineNumber() != stmt.getJavaSourceStartLineNumber() || !unit.toString().equals(stmt.toString()));

         return (Stmt)unit;
      }
   }

   public static Value locateValue(Stmt stmt, Value value) {
      Iterator var3 = stmt.getUseAndDefBoxes().iterator();

      while(var3.hasNext()) {
         ValueBox valuebox = (ValueBox)var3.next();
         Value v = valuebox.getValue();
         if (v.toString().equals(value.toString())) {
            return v;
         }
      }

      return null;
   }

   public static Value getDefaultValue(Type type) {
      if (type instanceof BooleanType) {
         return IntConstant.v(0);
      } else if (type instanceof ByteType) {
         return StringConstant.v("");
      } else if (type instanceof CharType) {
         return StringConstant.v("");
      } else if (type instanceof DoubleType) {
         return DoubleConstant.v(0.0D);
      } else if (type instanceof FloatType) {
         return FloatConstant.v(0.0F);
      } else if (type instanceof IntType) {
         return IntConstant.v(0);
      } else if (type instanceof LongType) {
         return LongConstant.v(0L);
      } else {
         return (Value)(type instanceof ShortType ? IntConstant.v(0) : NullConstant.v());
      }
   }

   public static void output(VFGNode node, Body body, String op) throws IOException {
      ++patchID;
      Body oldBody = node.getMethod().getActiveBody();
      node.getMethod().setActiveBody(body);
      outputJimpleFile(body, op);
      outputJimpleFile(oldBody, op + "-ORIGIN");
      outputClassFile(node.getMethod());
      node.getMethod().setActiveBody(oldBody);
   }

   public static void main(String[] args) throws FileNotFoundException, IOException {
      helloWorld();
   }
}
