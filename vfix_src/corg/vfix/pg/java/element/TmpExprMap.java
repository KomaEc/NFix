package corg.vfix.pg.java.element;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.util.Chain;

public class TmpExprMap {
   private HashMap<Local, ExprBox> tmpNameMap = new HashMap();
   private ArrayList<Local> tmpLocals = new ArrayList();

   public Expression getTmpName(Local local) {
      return (Expression)(!this.tmpNameMap.containsKey(local) ? new NameExpr(local.toString()) : ((ExprBox)this.tmpNameMap.get(local)).getExpr());
   }

   TmpExprMap(SootMethod method) {
      Body body = method.getActiveBody();
      Chain<Local> locals = body.getLocals();
      this.setTmpLocals(locals);
      this.initTmpNameMap(body);

      while(!this.iterateMap()) {
         this.printMap();
      }

      this.printMap();
   }

   public void printMap() {
   }

   private boolean iterateMap() {
      HashMap<Local, Expression> finishedMap = new HashMap();
      HashMap<Local, Expression> unfinishedMap = new HashMap();
      Iterator var4 = this.tmpNameMap.keySet().iterator();

      Local key;
      ExprBox exprBox;
      while(var4.hasNext()) {
         key = (Local)var4.next();
         exprBox = (ExprBox)this.tmpNameMap.get(key);
         Expression expr = exprBox.getExpr();
         if (!exprBox.hasTmpLocals(this.tmpLocals)) {
            finishedMap.put(key, expr);
         } else {
            unfinishedMap.put(key, expr);
         }
      }

      if (unfinishedMap.isEmpty()) {
         return true;
      } else {
         var4 = unfinishedMap.keySet().iterator();

         while(var4.hasNext()) {
            key = (Local)var4.next();
            exprBox = (ExprBox)this.tmpNameMap.get(key);
            Iterator var7 = finishedMap.keySet().iterator();

            while(var7.hasNext()) {
               Local tmpKey = (Local)var7.next();
               if (exprBox.hasTmpLocal(tmpKey)) {
                  exprBox.replace(tmpKey, (Expression)finishedMap.get(tmpKey));
                  this.tmpNameMap.replace(key, exprBox);
               }
            }
         }

         return false;
      }
   }

   private Unit getFirstDef(Local local, Body body) {
      Iterator var4 = body.getUnits().iterator();

      while(var4.hasNext()) {
         Unit unit = (Unit)var4.next();
         List<ValueBox> valueBoxes = unit.getDefBoxes();
         Iterator var7 = valueBoxes.iterator();

         while(var7.hasNext()) {
            ValueBox valueBox = (ValueBox)var7.next();
            if (valueBox.getValue().equals(local)) {
               return unit;
            }
         }
      }

      return null;
   }

   private void initTmpNameMap(Body body) {
      Iterator var3 = this.tmpLocals.iterator();

      while(var3.hasNext()) {
         Local local = (Local)var3.next();
         Unit unit = this.getFirstDef(local, body);
         if (unit instanceof AssignStmt) {
            this.tmpNameMap.put(local, new ExprBox(((AssignStmt)unit).getRightOp()));
         }
      }

   }

   private void setTmpLocals(Chain<Local> locals) {
      Iterator var3 = locals.iterator();

      while(var3.hasNext()) {
         Local local = (Local)var3.next();
         if (local instanceof Local && this.isTmpLocal(local)) {
            this.tmpLocals.add(local);
         }
      }

   }

   private boolean isTmpLocal(Local local) {
      return this.isTmpLocal(local.getName());
   }

   private boolean isTmpLocal(String name) {
      return name.charAt(0) == '$';
   }
}
