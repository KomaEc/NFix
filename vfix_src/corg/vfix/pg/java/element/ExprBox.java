package corg.vfix.pg.java.element;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.DivExpr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.MulExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;

public class ExprBox {
   private Expression expr;
   private Expression base;
   private NodeList<Expression> args = new NodeList();
   private Expression op1;
   private Expression op2;

   ExprBox(Value value) {
      if (value instanceof InvokeExpr) {
         this.parseInvokeExpr((InvokeExpr)value);
      } else if (value instanceof Ref) {
         this.parseRef((Ref)value);
      } else if (value instanceof LengthExpr) {
         this.parseLengthExpr((LengthExpr)value);
      } else if (value instanceof AnyNewExpr) {
         this.parseAnyNewExpr((AnyNewExpr)value);
      } else if (value instanceof BinopExpr) {
         this.parseBinopExpr((BinopExpr)value);
      } else if (value instanceof CastExpr) {
         this.parseCastExpr((CastExpr)value);
      } else {
         System.out.println("cannot handle expr: " + value);
      }

   }

   public Expression getExpr() {
      return this.expr;
   }

   public boolean hasTmpLocals(ArrayList<Local> locals) {
      Iterator var3 = locals.iterator();

      while(var3.hasNext()) {
         Local local = (Local)var3.next();
         if (this.hasTmpLocal(local)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasTmpLocal(Local value) {
      return this.hasTmpLocalInBase(value) || this.hasTmpLocalInArgs(value);
   }

   private boolean hasTmpLocalInBase(Local value) {
      return this.base == null ? false : value.toString().equals(this.base.toString());
   }

   private boolean hasTmpLocalInArgs(Local value) {
      Iterator var3 = this.args.iterator();

      while(var3.hasNext()) {
         Expression arg = (Expression)var3.next();
         if (this.hasTmpLocalInArg(value, arg)) {
            return true;
         }
      }

      return false;
   }

   private boolean hasTmpLocalInArg(Value value, Expression arg) {
      return arg == null ? false : arg.toString().equals(value.toString());
   }

   private boolean hasTmpLocalInOp(Value value, Expression op) {
      return op == null ? false : op.toString().equals(value.toString());
   }

   public void replace(Local value, Expression newExpr) {
      if (this.hasTmpLocalInBase(value)) {
         this.base = newExpr;
      }

      Iterator var4 = this.args.iterator();

      while(var4.hasNext()) {
         Expression arg = (Expression)var4.next();
         if (this.hasTmpLocalInArg(value, arg)) {
            this.args.replace(arg, newExpr);
         }
      }

      if (this.hasTmpLocalInOp(value, this.op1)) {
         this.op1 = newExpr;
      }

      if (this.hasTmpLocalInOp(value, this.op2)) {
         this.op2 = newExpr;
      }

      this.updateExpr();
   }

   private void updateExpr() {
      if (this.expr instanceof FieldAccessExpr) {
         ((FieldAccessExpr)this.expr).setScope(this.base);
      } else if (this.expr instanceof MethodCallExpr) {
         ((MethodCallExpr)this.expr).setScope(this.base);
         ((MethodCallExpr)this.expr).setArguments(this.args);
      } else if (this.expr instanceof ArrayAccessExpr) {
         ((ArrayAccessExpr)this.expr).setName(this.base);
         ((ArrayAccessExpr)this.expr).setIndex((Expression)this.args.get(0));
      } else if (this.expr instanceof BinaryExpr) {
         ((BinaryExpr)this.expr).setLeft(this.op1);
         ((BinaryExpr)this.expr).setRight(this.op2);
      } else if (this.expr instanceof CastExpr) {
         this.expr = this.base;
      }

   }

   private void parseRef(Ref value) {
      if (value instanceof InstanceFieldRef) {
         this.parseInstanceFieldRef((InstanceFieldRef)value);
      } else if (value instanceof StaticFieldRef) {
         this.parseStaticFieldRef((StaticFieldRef)value);
      } else if (value instanceof ArrayRef) {
         this.parseArrayRef((ArrayRef)value);
      }

   }

   private void parseAnyNewExpr(AnyNewExpr value) {
      if (value instanceof NewExpr) {
         this.parseNewExpr((NewExpr)value);
      } else if (value instanceof NewArrayExpr) {
         this.parseNewArrayExpr((NewArrayExpr)value);
      }

   }

   private void parseNewArrayExpr(NewArrayExpr value) {
   }

   private void parseBinopExpr(BinopExpr value) {
      this.op1 = new NameExpr(value.getOp1().toString());
      this.op2 = new NameExpr(value.getOp2().toString());
      this.expr = new BinaryExpr();
      ((BinaryExpr)this.expr).setLeft(this.op1);
      ((BinaryExpr)this.expr).setRight(this.op2);
      if (value instanceof AddExpr) {
         ((BinaryExpr)this.expr).setOperator(BinaryExpr.Operator.PLUS);
      } else if (value instanceof AndExpr) {
         ((BinaryExpr)this.expr).setOperator(BinaryExpr.Operator.AND);
      } else if (value instanceof DivExpr) {
         ((BinaryExpr)this.expr).setOperator(BinaryExpr.Operator.DIVIDE);
      } else if (value instanceof MulExpr) {
         ((BinaryExpr)this.expr).setOperator(BinaryExpr.Operator.MULTIPLY);
      } else if (value instanceof SubExpr) {
         ((BinaryExpr)this.expr).setOperator(BinaryExpr.Operator.MINUS);
      }

   }

   private void parseNewExpr(NewExpr value) {
      this.expr = new ObjectCreationExpr();
      ClassOrInterfaceType type = new ClassOrInterfaceType();
      type.setName((String)value.getBaseType().toString());
      ((ObjectCreationExpr)this.expr).setType(type);
   }

   private void parseCastExpr(CastExpr value) {
      Value v = value.getOp();
      this.expr = new NameExpr(v.toString());
      this.base = new NameExpr(v.toString());
   }

   private void parseArrayRef(ArrayRef value) {
      this.base = new NameExpr(value.getBase().toString());
      this.args.add((Node)(new NameExpr(value.getIndex().toString())));
      this.expr = new ArrayAccessExpr();
      ((ArrayAccessExpr)this.expr).setName(this.base);
      ((ArrayAccessExpr)this.expr).setIndex((Expression)this.args.get(0));
   }

   private void parseLengthExpr(LengthExpr value) {
      String baseStr = value.getOp().toString();
      String fr = "length";
      this.expr = new FieldAccessExpr();
      if (baseStr.equals("this")) {
         this.base = new ThisExpr();
         ((FieldAccessExpr)this.expr).setScope(this.base);
      } else {
         this.base = new NameExpr(baseStr);
         ((FieldAccessExpr)this.expr).setScope(this.base);
      }

      ((FieldAccessExpr)this.expr).setName(new SimpleName(fr));
   }

   private void parseInstanceFieldRef(InstanceFieldRef value) {
      String baseStr = value.getBase().toString();
      String fr = value.getFieldRef().name();
      this.expr = new FieldAccessExpr();
      if (baseStr.equals("this")) {
         this.base = new ThisExpr();
         ((FieldAccessExpr)this.expr).setScope(this.base);
      } else {
         this.base = new NameExpr(baseStr);
         ((FieldAccessExpr)this.expr).setScope(this.base);
      }

      ((FieldAccessExpr)this.expr).setName(new SimpleName(fr));
   }

   private void parseStaticFieldRef(StaticFieldRef value) {
      String baseStr = value.getFieldRef().declaringClass().toString();
      String fr = value.getFieldRef().name();
      this.expr = new FieldAccessExpr();
      if (baseStr.equals("this")) {
         this.base = new ThisExpr();
         ((FieldAccessExpr)this.expr).setScope(this.base);
      } else {
         this.base = new NameExpr(baseStr);
         ((FieldAccessExpr)this.expr).setScope(this.base);
      }

      ((FieldAccessExpr)this.expr).setName(new SimpleName(fr));
   }

   private void parseInvokeExpr(InvokeExpr value) {
      if (value instanceof InstanceInvokeExpr) {
         this.parseInstanceInvokeExpr((InstanceInvokeExpr)value);
      } else if (value instanceof StaticInvokeExpr) {
         this.parseStaticInvokeExpr((StaticInvokeExpr)value);
      }

   }

   private void parseStaticInvokeExpr(StaticInvokeExpr value) {
      String baseStr = value.getMethod().getDeclaringClass().getName().toString();
      String smr = value.getMethodRef().name();
      this.expr = new MethodCallExpr();
      this.parseArgExprs(value.getArgs());
      ((MethodCallExpr)this.expr).setArguments(this.args);
      if (baseStr.equals("this")) {
         this.base = new ThisExpr();
         ((MethodCallExpr)this.expr).setScope(this.base);
      } else {
         this.base = new NameExpr(baseStr);
         ((MethodCallExpr)this.expr).setScope(this.base);
      }

      ((MethodCallExpr)this.expr).setName((String)smr);
   }

   private void parseInstanceInvokeExpr(InstanceInvokeExpr value) {
      String baseStr = value.getBase().toString();
      String smr = value.getMethodRef().name();
      this.expr = new MethodCallExpr();
      this.parseArgExprs(value.getArgs());
      ((MethodCallExpr)this.expr).setArguments(this.args);
      if (baseStr.equals("this")) {
         this.base = new ThisExpr();
         ((MethodCallExpr)this.expr).setScope(this.base);
      } else {
         this.base = new NameExpr(baseStr);
         ((MethodCallExpr)this.expr).setScope(this.base);
      }

      ((MethodCallExpr)this.expr).setName((String)smr);
   }

   private void parseArgExprs(List<Value> argList) {
      NodeList<Expression> argExprs = new NodeList();

      String argStr;
      for(Iterator var4 = argList.iterator(); var4.hasNext(); argExprs.add((Node)(new NameExpr(argStr)))) {
         Value arg = (Value)var4.next();
         argStr = arg.toString();
         if (argStr.contains("class \"")) {
            argStr = this.transClass(argStr);
         }
      }

      this.args = argExprs;
   }

   private String transClass(String argStr) {
      System.out.println(argStr);
      argStr = argStr.replace("\"", "");
      argStr = argStr.replace(";", "");
      String[] tokens = argStr.split("/");
      String className = tokens[tokens.length - 1] + ".class";
      return className;
   }
}
