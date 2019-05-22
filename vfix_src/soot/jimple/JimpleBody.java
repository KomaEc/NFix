package soot.jimple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.validation.FieldRefValidator;
import soot.jimple.validation.IdentityStatementsValidator;
import soot.jimple.validation.IdentityValidator;
import soot.jimple.validation.InvokeArgumentValidator;
import soot.jimple.validation.JimpleTrapValidator;
import soot.jimple.validation.MethodValidator;
import soot.jimple.validation.NewValidator;
import soot.jimple.validation.ReturnStatementsValidator;
import soot.jimple.validation.TypesValidator;
import soot.options.Options;
import soot.util.Chain;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public class JimpleBody extends StmtBody {
   private static BodyValidator[] validators;

   private static synchronized BodyValidator[] getValidators() {
      if (validators == null) {
         validators = new BodyValidator[]{IdentityStatementsValidator.v(), TypesValidator.v(), ReturnStatementsValidator.v(), InvokeArgumentValidator.v(), FieldRefValidator.v(), NewValidator.v(), JimpleTrapValidator.v(), IdentityValidator.v(), MethodValidator.v()};
      }

      return validators;
   }

   public JimpleBody(SootMethod m) {
      super(m);
   }

   public JimpleBody() {
   }

   public Object clone() {
      Body b = new JimpleBody(this.getMethod());
      b.importBodyContentsFrom(this);
      return b;
   }

   public void validate() {
      List<ValidationException> exceptionList = new ArrayList();
      this.validate(exceptionList);
      if (!exceptionList.isEmpty()) {
         throw (ValidationException)exceptionList.get(0);
      }
   }

   public void validate(List<ValidationException> exceptionList) {
      super.validate(exceptionList);
      boolean runAllValidators = Options.v().debug() || Options.v().validate();
      BodyValidator[] var3 = getValidators();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BodyValidator validator = var3[var5];
         if (validator.isBasicValidator() || runAllValidators) {
            validator.validate(this, exceptionList);
         }
      }

   }

   public void validateIdentityStatements() {
      this.runValidation(IdentityStatementsValidator.v());
   }

   public void insertIdentityStmts() {
      this.insertIdentityStmts(this.getMethod().getDeclaringClass());
   }

   public void insertIdentityStmts(SootClass declaringClass) {
      Jimple jimple = Jimple.v();
      PatchingChain<Unit> unitChain = this.getUnits();
      Chain<Local> localChain = this.getLocals();
      Unit lastUnit = null;
      if (!this.getMethod().isStatic()) {
         if (declaringClass == null) {
            throw new IllegalArgumentException(String.format("No declaring class given for method %s", this.method.getSubSignature()));
         }

         Local l = jimple.newLocal("this", RefType.v(declaringClass));
         Stmt s = jimple.newIdentityStmt(l, jimple.newThisRef((RefType)l.getType()));
         localChain.add(l);
         unitChain.addFirst((Unit)s);
         lastUnit = s;
      }

      int i = 0;

      for(Iterator var12 = this.getMethod().getParameterTypes().iterator(); var12.hasNext(); ++i) {
         Type t = (Type)var12.next();
         Local l = jimple.newLocal("parameter" + i, t);
         Stmt s = jimple.newIdentityStmt(l, jimple.newParameterRef(l.getType(), i));
         localChain.add(l);
         if (lastUnit == null) {
            unitChain.addFirst((Unit)s);
         } else {
            unitChain.insertAfter((Unit)s, (Unit)lastUnit);
         }

         lastUnit = s;
      }

   }

   public Stmt getFirstNonIdentityStmt() {
      Iterator<Unit> it = this.getUnits().iterator();
      Object o = null;

      while(it.hasNext() && (o = it.next()) instanceof IdentityStmt) {
      }

      if (o == null) {
         throw new RuntimeException("no non-id statements!");
      } else {
         return (Stmt)o;
      }
   }
}
