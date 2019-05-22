package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.AbstractUnit;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.dava.DavaBody;
import soot.dava.DavaUnitPrinter;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.grimp.Grimp;
import soot.jimple.ArrayRef;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.util.IterableSet;

public class DVariableDeclarationStmt extends AbstractUnit implements Stmt {
   Type declarationType = null;
   List declarations = null;
   DavaBody davaBody = null;

   public DVariableDeclarationStmt(Type decType, DavaBody davaBody) {
      if (this.declarationType != null) {
         throw new RuntimeException("creating a VariableDeclaration which has already been created");
      } else {
         this.declarationType = decType;
         this.declarations = new ArrayList();
         this.davaBody = davaBody;
      }
   }

   public List getDeclarations() {
      return this.declarations;
   }

   public void addLocal(Local add) {
      this.declarations.add(add);
   }

   public void removeLocal(Local remove) {
      for(int i = 0; i < this.declarations.size(); ++i) {
         Local temp = (Local)this.declarations.get(i);
         if (temp.getName().compareTo(remove.getName()) == 0) {
            this.declarations.remove(i);
            return;
         }
      }

   }

   public Type getType() {
      return this.declarationType;
   }

   public boolean isOfType(Type type) {
      return type.toString().compareTo(this.declarationType.toString()) == 0;
   }

   public Object clone() {
      DVariableDeclarationStmt temp = new DVariableDeclarationStmt(this.declarationType, this.davaBody);
      Iterator it = this.declarations.iterator();

      while(it.hasNext()) {
         Local obj = (Local)it.next();
         Value temp1 = Grimp.cloneIfNecessary(obj);
         if (temp1 instanceof Local) {
            temp.addLocal((Local)temp1);
         }
      }

      return temp;
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      if (this.declarations.size() == 0) {
         return b.toString();
      } else {
         String type = this.declarationType.toString();
         if (type.equals("null_type")) {
            b.append("Object");
         } else {
            b.append(type);
         }

         b.append(" ");
         Iterator decIt = this.declarations.iterator();

         while(decIt.hasNext()) {
            Local tempDec = (Local)decIt.next();
            b.append(tempDec.getName());
            if (decIt.hasNext()) {
               b.append(", ");
            }
         }

         return b.toString();
      }
   }

   public void toString(UnitPrinter up) {
      if (this.declarations.size() != 0) {
         if (!(up instanceof DavaUnitPrinter)) {
            throw new RuntimeException("DavaBody should always be printed using the DavaUnitPrinter");
         } else {
            DavaUnitPrinter dup = (DavaUnitPrinter)up;
            String type = this.declarationType.toString();
            if (type.equals("null_type")) {
               dup.printString("Object");
            } else {
               IterableSet importSet = this.davaBody.getImportList();
               if (!importSet.contains(type)) {
                  this.davaBody.addToImportList(type);
               }

               type = RemoveFullyQualifiedName.getReducedName(this.davaBody.getImportList(), type, this.declarationType);
               dup.printString(type);
            }

            dup.printString(" ");
            Iterator decIt = this.declarations.iterator();

            while(decIt.hasNext()) {
               Local tempDec = (Local)decIt.next();
               dup.printString(tempDec.getName());
               if (decIt.hasNext()) {
                  dup.printString(", ");
               }
            }

         }
      }
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }

   public boolean containsInvokeExpr() {
      return false;
   }

   public InvokeExpr getInvokeExpr() {
      throw new RuntimeException("getInvokeExpr() called with no invokeExpr present!");
   }

   public ValueBox getInvokeExprBox() {
      throw new RuntimeException("getInvokeExprBox() called with no invokeExpr present!");
   }

   public boolean containsArrayRef() {
      return false;
   }

   public ArrayRef getArrayRef() {
      throw new RuntimeException("getArrayRef() called with no ArrayRef present!");
   }

   public ValueBox getArrayRefBox() {
      throw new RuntimeException("getArrayRefBox() called with no ArrayRef present!");
   }

   public boolean containsFieldRef() {
      return false;
   }

   public FieldRef getFieldRef() {
      throw new RuntimeException("getFieldRef() called with no FieldRef present!");
   }

   public ValueBox getFieldRefBox() {
      throw new RuntimeException("getFieldRefBox() called with no FieldRef present!");
   }
}
