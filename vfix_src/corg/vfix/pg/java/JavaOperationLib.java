package corg.vfix.pg.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.utils.Pair;
import corg.vfix.pg.java.element.TmpExprMaps;
import corg.vfix.pg.java.visitor.GetBlockByRangeVisitor;
import corg.vfix.pg.java.visitor.JavaBox;
import corg.vfix.pg.java.visitor.MethodCallExprVisitor;
import corg.vfix.pg.java.visitor.ReplaceNewVisitor;
import corg.vfix.sa.vfg.VFGNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.ShortType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;

public class JavaOperationLib {
   private static String path = "/home/xuezheng/workspace-kepler/vfix/dataset/chart-14/source";

   public static void init(String srcPath) {
      path = srcPath;
   }

   public static void insertNullCheckInit(BlockStmt body, Statement stmt, Expression var, String typeName, Type type) {
      if (var instanceof MethodCallExpr) {
         putMtdCallAhead(body, stmt, (MethodCallExpr)var, type);
         Expression tmp = new NameExpr("vfix_Tmp");
         String newStmtStr = stmt.toString().replace(((Expression)var).toString(), tmp.toString());
         Statement newStmt = JavaParser.parseStatement(newStmtStr);
         replace(body, stmt, newStmt);
         stmt = newStmt;
         var = tmp;
      }

      ExpressionStmt initStmt = objectCreationStmt((Expression)var, typeName);
      IfStmt ifStmt = nullCheckEqThen((Expression)var, initStmt);
      insertBefore(body, stmt, ifStmt);
   }

   public static void insertNullCheckReplace(BlockStmt body, Statement stmt, Expression var, Type type, String newClsName, Stmt jimpleStmt) throws Exception {
      if (isVarDec(stmt)) {
         stmt = putVarDecAhead(body, (ExpressionStmt)stmt);
      }

      Expression value = getDefaultValue(type, newClsName);
      Statement newStmt = replaceNullDerefWithNew(stmt, var, value, jimpleStmt);
      IfStmt ifStmt = nullCheckEqThenElse(var, newStmt, stmt);
      replace(body, (Statement)stmt, (Statement)ifStmt);
   }

   public static void insertNullCheckReturn(BlockStmt body, Statement stmt, Expression var, Type type, String newClsName) {
      Expression value = getDefaultValue(type, newClsName);
      ReturnStmt ret = new ReturnStmt(value);
      IfStmt ifStmt = nullCheckEqThen(var, ret);
      insertBefore(body, stmt, ifStmt);
   }

   public static void insertNullCheckSkip(BlockStmt body, Statement stmt, Expression var, Type type) {
      if (isVarDec(stmt)) {
         stmt = putVarDecAhead(body, (ExpressionStmt)stmt);
      }

      if (var instanceof MethodCallExpr) {
         putMtdCallAhead(body, stmt, (MethodCallExpr)var, type);
         Expression tmp = new NameExpr("vfix_Tmp");
         String newStmtStr = stmt.toString().replace(((Expression)var).toString(), tmp.toString());
         Statement newStmt = JavaParser.parseStatement(newStmtStr);
         replace(body, stmt, newStmt);
         stmt = newStmt;
         var = tmp;
      }

      IfStmt ifStmt = nullCheckNeThen((Expression)var, stmt);
      replace(body, (Statement)stmt, (Statement)ifStmt);
   }

   public static void insertNullCheckSkip(BlockStmt body, Statement stmt, Pair<Integer, Integer> invalidRange, Expression var) throws Exception {
      NodeList<Statement> stmts = new NodeList();
      NodeList<Statement> bodyStmts = body.getStatements();
      int stmtStart = ((Range)stmt.getRange().get()).begin.line;
      int stmtEnd = ((Range)stmt.getRange().get()).end.line;
      int begin = Math.min((Integer)invalidRange.a, stmtStart);
      int end = Math.max((Integer)invalidRange.b, stmtEnd);
      if (end >= begin) {
         Iterator var11 = bodyStmts.iterator();

         while(var11.hasNext()) {
            Statement s = (Statement)var11.next();
            Range range = (Range)s.getRange().get();
            if (range.begin.line >= begin && range.end.line <= end) {
               stmts.add((Node)s);
            }
         }

         if (stmts.isEmpty()) {
            System.out.println("Cannot generate Java File in this case. Skip...");
         } else {
            IfStmt ifStmt = nullCheckNeThen(var, new BlockStmt(stmts));
            replace(body, stmts, ifStmt);
         }
      }
   }

   private static void replace(BlockStmt body, NodeList<Statement> stmts, IfStmt ifStmt) {
      NodeList<Statement> bodyStmts = body.getStatements();

      for(int i = 1; i < stmts.size(); ++i) {
         bodyStmts.remove(stmts.get(i));
      }

      bodyStmts.replace((Statement)stmts.get(0), ifStmt);
   }

   public static void replaceNullWithNew(BlockStmt body, Statement stmt, Type type, String newClsName) {
      Expression value = getDefaultValue(type, newClsName);
      String stmtStr = stmt.toString();
      Statement newStmt = JavaParser.parseStatement(stmtStr.replace("null", value.toString()));
      System.out.println("NewStmt: " + newStmt);
      replace(body, stmt, newStmt);
   }

   private static void putMtdCallAhead(BlockStmt body, Statement stmt, MethodCallExpr var, Type type) {
      com.github.javaparser.ast.type.Type javaType = getJavaTypeFromSoot(type);
      VariableDeclarator vd = new VariableDeclarator(javaType, "vfix_Tmp", var);
      VariableDeclarationExpr vdExpr = new VariableDeclarationExpr(vd);
      ExpressionStmt vdStmt = new ExpressionStmt(vdExpr);
      insertBefore(body, stmt, vdStmt);
   }

   private static com.github.javaparser.ast.type.Type getJavaTypeFromSoot(Type type) {
      if (type instanceof PrimType) {
         return getJavaPrimitiveTypeFromSoot((PrimType)type);
      } else if (type instanceof ArrayType) {
         return getJavaArrayTypeFromSoot((ArrayType)type);
      } else if (type instanceof RefType) {
         return getJavaRefTypeFromSoot((RefType)type);
      } else {
         System.out.println("Cannot transfer soot type " + type + " to Java type");
         return null;
      }
   }

   private static ClassOrInterfaceType getJavaRefTypeFromSoot(RefType type) {
      ClassOrInterfaceType cit = new ClassOrInterfaceType();
      cit.setName((String)type.getClassName());
      return cit;
   }

   private static com.github.javaparser.ast.type.ArrayType getJavaArrayTypeFromSoot(ArrayType type) {
      Type elementType = type.getElementType();
      com.github.javaparser.ast.type.Type javaElementType = null;
      if (elementType instanceof PrimType) {
         javaElementType = getJavaPrimitiveTypeFromSoot((PrimType)elementType);
      } else if (elementType instanceof RefType) {
         javaElementType = getJavaRefTypeFromSoot((RefType)elementType);
      } else if (elementType instanceof ArrayType) {
         System.out.println("Cannot handle Multidimensional Arrays");
      }

      return new com.github.javaparser.ast.type.ArrayType((com.github.javaparser.ast.type.Type)javaElementType, com.github.javaparser.ast.type.ArrayType.Origin.TYPE, new NodeList());
   }

   private static PrimitiveType getJavaPrimitiveTypeFromSoot(PrimType pt) {
      PrimitiveType javaPt = new PrimitiveType();
      if (pt instanceof BooleanType) {
         javaPt.setType(PrimitiveType.Primitive.BOOLEAN);
      } else if (pt instanceof ByteType) {
         javaPt.setType(PrimitiveType.Primitive.BYTE);
      } else if (pt instanceof CharType) {
         javaPt.setType(PrimitiveType.Primitive.CHAR);
      } else if (pt instanceof DoubleType) {
         javaPt.setType(PrimitiveType.Primitive.DOUBLE);
      } else if (pt instanceof FloatType) {
         javaPt.setType(PrimitiveType.Primitive.FLOAT);
      } else if (pt instanceof IntType) {
         javaPt.setType(PrimitiveType.Primitive.INT);
      } else if (pt instanceof LongType) {
         javaPt.setType(PrimitiveType.Primitive.LONG);
      } else if (pt instanceof ShortType) {
         javaPt.setType(PrimitiveType.Primitive.SHORT);
      } else {
         System.out.println("Cannot transfer soot type " + pt + " to Java type");
      }

      return javaPt;
   }

   private static Statement putVarDecAhead(BlockStmt body, ExpressionStmt stmt) {
      Expression expr = stmt.getExpression();
      VariableDeclarationExpr vdExpr = (VariableDeclarationExpr)expr;
      VariableDeclarationExpr newVDExpr = vdExpr.clone();
      NodeList<Statement> newStmts = new NodeList();
      NodeList<Statement> newAssignStmts = new NodeList();
      Iterator var8 = newVDExpr.getVariables().iterator();

      while(var8.hasNext()) {
         VariableDeclarator vd = (VariableDeclarator)var8.next();
         if (vd.getInitializer().isPresent()) {
            ExpressionStmt assignStmt = new ExpressionStmt(new AssignExpr(new NameExpr(vd.getName()), (Expression)vd.getInitializer().get(), AssignExpr.Operator.ASSIGN));
            newAssignStmts.add((Node)assignStmt);
            vd.removeInitializer();
         }
      }

      ExpressionStmt vdStmt = new ExpressionStmt(newVDExpr);
      newStmts.add(0, (Node)vdStmt);
      Statement output = null;
      if (newAssignStmts.size() == 1) {
         output = (Statement)newAssignStmts.get(0);
         newStmts.add(1, (Node)output);
      } else if (newAssignStmts.size() > 1) {
         output = new BlockStmt(newAssignStmts);
         newStmts.add(1, (Node)output);
      }

      replace(body, (Statement)stmt, (NodeList)newStmts);
      return (Statement)output;
   }

   private static void replace(BlockStmt body, Statement oldStmt, NodeList<Statement> newStmts) {
      NodeList<Statement> stmts = body.getStatements();
      if (oldStmt != null && newStmts != null && stmts.contains((Node)oldStmt) && !newStmts.isEmpty()) {
         Statement firstStmt = (Statement)newStmts.get(0);
         stmts.replace(oldStmt, firstStmt);
         int index = stmts.indexOf(firstStmt);

         for(int i = 1; i < newStmts.size(); ++i) {
            Statement afterStmt = (Statement)newStmts.get(i);
            stmts.add(index + i, (Node)afterStmt);
         }

      }
   }

   private static NodeList<Expression> parseArgExprs(List<Value> argList) {
      NodeList<Expression> argExprs = new NodeList();
      Iterator var3 = argList.iterator();

      while(var3.hasNext()) {
         Value arg = (Value)var3.next();
         argExprs.add((Node)(new NameExpr(arg.toString())));
      }

      return argExprs;
   }

   public static Expression value2Expr(SootMethod mtd, Value value, Statement stmt) {
      if (value == null) {
         return new NullLiteralExpr();
      } else if (TmpExprMaps.isTmpLocal(value.toString())) {
         Local local = (Local)value;
         Expression expr = TmpExprMaps.getTmpName(mtd, local);
         if (expr instanceof MethodCallExpr) {
            MethodCallExpr mtdExpr = (MethodCallExpr)expr;
            System.out.println("MethodCallExpr: " + expr);
            return verifyMethodCallExpr(mtdExpr, stmt);
         } else {
            return expr;
         }
      } else if (value instanceof Local) {
         return new NameExpr(new SimpleName(value.toString()));
      } else if (value instanceof InstanceFieldRef) {
         InstanceFieldRef ifr = (InstanceFieldRef)value;
         return new FieldAccessExpr(new NameExpr(ifr.getBase().toString()), ifr.getField().toString());
      } else {
         MethodCallExpr mtdExpr;
         if (value instanceof StaticInvokeExpr) {
            StaticInvokeExpr sie = (StaticInvokeExpr)value;
            mtdExpr = new MethodCallExpr();
            mtdExpr.setScope(new NameExpr(sie.getMethod().getDeclaringClass().getPackageName().toString()));
            mtdExpr.setName((String)sie.getMethodRef().name());
            mtdExpr.setArguments(parseArgExprs(sie.getArgs()));
            return mtdExpr;
         } else if (value instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr)value;
            mtdExpr = new MethodCallExpr();
            mtdExpr.setScope(new NameExpr(iie.getBase().toString()));
            mtdExpr.setName((String)iie.getMethodRef().name());
            mtdExpr.setArguments(parseArgExprs(iie.getArgs()));
            return mtdExpr;
         } else if (value instanceof StaticFieldRef) {
            StaticFieldRef sfr = (StaticFieldRef)value;
            return new FieldAccessExpr(new NameExpr(sfr.getFieldRef().declaringClass().toString()), sfr.getField().toString());
         } else {
            return null;
         }
      }
   }

   private static Expression verifyMethodCallExpr(MethodCallExpr mtdExpr, Statement stmt) {
      JavaBox javaBox = new JavaBox();
      javaBox.line = ((Range)stmt.getRange().get()).begin.line;
      javaBox.methodCallExpr = mtdExpr;
      NodeList<Statement> block = new NodeList();
      block.add((Node)stmt);
      BlockStmt blockStmt = new BlockStmt(block);
      MethodCallExprVisitor mceVisitor = new MethodCallExprVisitor();
      mceVisitor.visit(blockStmt, javaBox);
      return javaBox.methodCallExpr;
   }

   private static void replace(BlockStmt body, Statement oldStmt, Statement newStmt) {
      NodeList<Statement> stmts = body.getStatements();
      if (oldStmt != null && newStmt != null && stmts.contains((Node)oldStmt)) {
         stmts.replace(oldStmt, newStmt);
      }
   }

   private static void insertBefore(BlockStmt body, Statement beforeThisStmt, Statement addedStmt) {
      NodeList<Statement> stmts = body.getStatements();
      if (beforeThisStmt != null && addedStmt != null && stmts.contains((Node)beforeThisStmt)) {
         stmts.addBefore(addedStmt, beforeThisStmt);
      }
   }

   public static Expression getPrimDefaultValue(Type type) {
      if (type instanceof BooleanType) {
         return new BooleanLiteralExpr(false);
      } else if (type instanceof CharType) {
         return new CharLiteralExpr("");
      } else if (!(type instanceof DoubleType) && !(type instanceof FloatType)) {
         if (!(type instanceof IntType) && !(type instanceof ShortType)) {
            return (Expression)(type instanceof LongType ? new LongLiteralExpr(0L) : new NullLiteralExpr());
         } else {
            return new IntegerLiteralExpr(0);
         }
      } else {
         return new DoubleLiteralExpr(0.0D);
      }
   }

   private static Expression getDefaultValue(Type type, String newClsName) {
      if (type instanceof PrimType) {
         return getPrimDefaultValue(type);
      } else if (type instanceof RefType) {
         return (Expression)(newClsName == null ? new NullLiteralExpr() : objectCreationExpr(newClsName));
      } else if (type instanceof ArrayType) {
         return arrayCreationExpr((ArrayType)type);
      } else {
         System.out.println("init value not support in java");
         return null;
      }
   }

   private static boolean isVarDec(Statement stmt) {
      if (stmt.isExpressionStmt()) {
         ExpressionStmt exprStmt = (ExpressionStmt)stmt;
         Expression expr = exprStmt.getExpression();
         if (expr.isVariableDeclarationExpr()) {
            return true;
         }
      }

      return false;
   }

   private static ExpressionStmt objectCreationStmt(Expression var, String typeName) {
      ObjectCreationExpr ocExpr = objectCreationExpr(typeName);
      AssignExpr assignExpr = new AssignExpr(var, ocExpr, AssignExpr.Operator.ASSIGN);
      return new ExpressionStmt(assignExpr);
   }

   private static ObjectCreationExpr objectCreationExpr(String typeName) {
      ObjectCreationExpr ocExpr = new ObjectCreationExpr();
      ClassOrInterfaceType type = new ClassOrInterfaceType();
      type.setName((String)typeName);
      ocExpr.setType(type);
      return ocExpr;
   }

   private static Expression arrayCreationExpr(ArrayType arrayType) {
      ArrayCreationExpr acExpr = new ArrayCreationExpr();
      acExpr.setElementType(arrayType.toQuotedString());
      return acExpr;
   }

   private static Statement replaceNullDerefWithNew(Statement stmt, Expression var, Expression value, Stmt jimpleStmt) throws Exception {
      JavaBox javaBox = new JavaBox();
      javaBox.var = var.toString();
      javaBox.expr = value.toString();
      javaBox.stmt = stmt;
      javaBox.line = jimpleStmt.getJavaSourceStartLineNumber();
      NodeList<Statement> stmts = new NodeList();
      stmts.add((Node)stmt);
      BlockStmt blockStmt = new BlockStmt(stmts);
      ReplaceNewVisitor replaceVisitor = new ReplaceNewVisitor();
      replaceVisitor.visit((BlockStmt)blockStmt, (Object)javaBox);
      if (javaBox.outputStmt != null) {
         return javaBox.outputStmt;
      } else {
         System.out.println("cannot replace " + var + " in " + javaBox.stmt.toString() + " with " + value);
         throw new Exception();
      }
   }

   private static IfStmt nullCheckEqThenElse(Expression var, Statement thenStmt, Statement elseStmt) {
      IfStmt ifStmt = nullCheckEqThen(var, thenStmt);
      ifStmt.setElseStmt(elseStmt);
      return ifStmt;
   }

   private static IfStmt nullCheckNeThen(Expression var, Statement thenStmt) {
      IfStmt ifStmt = nullCheckNe(var);
      ifStmt.setThenStmt(thenStmt);
      return ifStmt;
   }

   private static IfStmt nullCheckEqThen(Expression var, Statement thenStmt) {
      IfStmt ifStmt = nullCheckEq(var);
      ifStmt.setThenStmt(thenStmt);
      return ifStmt;
   }

   private static IfStmt nullCheckNe(Expression var) {
      return nullCheck(var, BinaryExpr.Operator.NOT_EQUALS);
   }

   private static IfStmt nullCheckEq(Expression var) {
      return nullCheck(var, BinaryExpr.Operator.EQUALS);
   }

   private static IfStmt nullCheck(Expression var, BinaryExpr.Operator op) {
      BinaryExpr condition = new BinaryExpr(var, new NullLiteralExpr(), op);
      IfStmt ifStmt = new IfStmt();
      ifStmt.setCondition(condition);
      return ifStmt;
   }

   public static CompilationUnit getCUByVFGNode(VFGNode node) throws FileNotFoundException {
      String filename = path + "/" + name2Path(node.getClsName());
      return JavaParser.parse(new File(filename));
   }

   public static BlockStmt getBlockStmtByStmt(JavaNode node, Statement stmt) {
      JavaBox box = new JavaBox();
      box.stmt = stmt;
      box.begin = ((Position)stmt.getBegin().get()).line;
      box.end = ((Position)stmt.getEnd().get()).line;
      GetBlockByRangeVisitor visitor = new GetBlockByRangeVisitor();
      visitor.visit((CompilationUnit)node.getCompilationUnit(), (Object)box);
      return box.blockStmt;
   }

   public static String name2Path(String name) {
      String path = "";
      if (!name.contains("$")) {
         path = name.replace(".", "/") + ".java";
      } else {
         int index = name.indexOf(36);
         String sub = name.substring(index);
         path = name.replace(sub, "");
         path = path.replace(".", "/") + ".java";
      }

      return path;
   }

   public static boolean isInLoopOrNot(BlockStmt body, Statement stmt) {
      return false;
   }

   public static void insertNullCheckContinue(BlockStmt body, Statement stmt, Expression var) {
      IfStmt ifStmt = nullCheckEqThen(var, new ContinueStmt());
      insertBefore(body, stmt, ifStmt);
   }
}
