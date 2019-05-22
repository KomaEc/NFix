package soot;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.tagkit.CodeAttribute;
import soot.tagkit.Tag;
import soot.util.Chain;
import soot.util.EscapedWriter;
import soot.util.HashChain;
import soot.validation.BodyValidator;
import soot.validation.CheckEscapingValidator;
import soot.validation.CheckInitValidator;
import soot.validation.CheckTypesValidator;
import soot.validation.CheckVoidLocalesValidator;
import soot.validation.LocalsValidator;
import soot.validation.TrapsValidator;
import soot.validation.UnitBoxesValidator;
import soot.validation.UsesValidator;
import soot.validation.ValidationException;
import soot.validation.ValueBoxesValidator;

public abstract class Body extends AbstractHost implements Serializable {
   private static final Logger logger = LoggerFactory.getLogger(Body.class);
   protected transient SootMethod method = null;
   protected Chain<Local> localChain = new HashChain();
   protected Chain<Trap> trapChain = new HashChain();
   protected PatchingChain<Unit> unitChain = new PatchingChain(new HashChain());
   private static BodyValidator[] validators;

   public abstract Object clone();

   private static synchronized BodyValidator[] getValidators() {
      if (validators == null) {
         validators = new BodyValidator[]{LocalsValidator.v(), TrapsValidator.v(), UnitBoxesValidator.v(), UsesValidator.v(), ValueBoxesValidator.v(), CheckTypesValidator.v(), CheckVoidLocalesValidator.v(), CheckEscapingValidator.v()};
      }

      return validators;
   }

   protected Body(SootMethod m) {
      this.method = m;
   }

   protected Body() {
   }

   public SootMethod getMethod() {
      if (this.method == null) {
         throw new RuntimeException("no method associated w/ body");
      } else {
         return this.method;
      }
   }

   public void setMethod(SootMethod method) {
      this.method = method;
   }

   public int getLocalCount() {
      return this.localChain.size();
   }

   public Map<Object, Object> importBodyContentsFrom(Body b) {
      HashMap<Object, Object> bindings = new HashMap();
      Iterator var3 = b.getUnits().iterator();

      Unit newObject;
      while(var3.hasNext()) {
         Unit original = (Unit)var3.next();
         newObject = (Unit)original.clone();
         newObject.addAllTagsOf(original);
         this.unitChain.addLast(newObject);
         bindings.put(original, newObject);
      }

      var3 = b.getTraps().iterator();

      while(var3.hasNext()) {
         Trap original = (Trap)var3.next();
         Trap copy = (Trap)original.clone();
         this.trapChain.addLast(copy);
         bindings.put(original, copy);
      }

      var3 = b.getLocals().iterator();

      while(var3.hasNext()) {
         Local original = (Local)var3.next();
         Local copy = (Local)original.clone();
         this.localChain.addLast(copy);
         bindings.put(original, copy);
      }

      var3 = this.getAllUnitBoxes().iterator();

      while(var3.hasNext()) {
         UnitBox box = (UnitBox)var3.next();
         Unit oldObject = box.getUnit();
         if ((newObject = (Unit)bindings.get(oldObject)) != null) {
            box.setUnit(newObject);
         }
      }

      var3 = this.getUseBoxes().iterator();

      ValueBox vb;
      while(var3.hasNext()) {
         vb = (ValueBox)var3.next();
         if (vb.getValue() instanceof Local) {
            vb.setValue((Value)bindings.get(vb.getValue()));
         }
      }

      var3 = this.getDefBoxes().iterator();

      while(var3.hasNext()) {
         vb = (ValueBox)var3.next();
         if (vb.getValue() instanceof Local) {
            vb.setValue((Value)bindings.get(vb.getValue()));
         }
      }

      return bindings;
   }

   protected void runValidation(BodyValidator validator) {
      List<ValidationException> exceptionList = new ArrayList();
      validator.validate(this, exceptionList);
      if (!exceptionList.isEmpty()) {
         throw (ValidationException)exceptionList.get(0);
      }
   }

   public void validate() {
      List<ValidationException> exceptionList = new ArrayList();
      this.validate(exceptionList);
      if (!exceptionList.isEmpty()) {
         throw (ValidationException)exceptionList.get(0);
      }
   }

   public void validate(List<ValidationException> exceptionList) {
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

   public void validateValueBoxes() {
      this.runValidation(ValueBoxesValidator.v());
   }

   public void validateLocals() {
      this.runValidation(LocalsValidator.v());
   }

   public void validateTraps() {
      this.runValidation(TrapsValidator.v());
   }

   public void validateUnitBoxes() {
      this.runValidation(UnitBoxesValidator.v());
   }

   public void validateUses() {
      this.runValidation(UsesValidator.v());
   }

   public Chain<Local> getLocals() {
      return this.localChain;
   }

   public Chain<Trap> getTraps() {
      return this.trapChain;
   }

   public Local getThisLocal() {
      Iterator var1 = this.getUnits().iterator();

      Unit s;
      do {
         if (!var1.hasNext()) {
            throw new RuntimeException("couldn't find identityref! in " + this.getMethod());
         }

         s = (Unit)var1.next();
      } while(!(s instanceof IdentityStmt) || !(((IdentityStmt)s).getRightOp() instanceof ThisRef));

      return (Local)((Local)((IdentityStmt)s).getLeftOp());
   }

   public Local getParameterLocal(int i) {
      Iterator var2 = this.getUnits().iterator();

      while(var2.hasNext()) {
         Unit s = (Unit)var2.next();
         if (s instanceof IdentityStmt && ((IdentityStmt)s).getRightOp() instanceof ParameterRef) {
            IdentityStmt is = (IdentityStmt)s;
            ParameterRef pr = (ParameterRef)is.getRightOp();
            if (pr.getIndex() == i) {
               return (Local)is.getLeftOp();
            }
         }
      }

      throw new RuntimeException("couldn't find parameterref" + i + "! in " + this.getMethod());
   }

   public List<Local> getParameterLocals() {
      int numParams = this.getMethod().getParameterCount();
      List<Local> retVal = new ArrayList(numParams);
      Iterator var3 = this.getUnits().iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         if (u instanceof IdentityStmt) {
            IdentityStmt is = (IdentityStmt)u;
            if (is.getRightOp() instanceof ParameterRef) {
               ParameterRef pr = (ParameterRef)is.getRightOp();
               retVal.add(pr.getIndex(), (Local)is.getLeftOp());
            }
         }
      }

      if (retVal.size() != numParams) {
         throw new RuntimeException("couldn't find parameterref! in " + this.getMethod());
      } else {
         return retVal;
      }
   }

   public List<Value> getParameterRefs() {
      Value[] res = new Value[this.getMethod().getParameterCount()];
      Iterator var2 = this.getUnits().iterator();

      while(var2.hasNext()) {
         Unit s = (Unit)var2.next();
         if (s instanceof IdentityStmt) {
            Value rightOp = ((IdentityStmt)s).getRightOp();
            if (rightOp instanceof ParameterRef) {
               ParameterRef parameterRef = (ParameterRef)rightOp;
               res[parameterRef.getIndex()] = parameterRef;
            }
         }
      }

      return Arrays.asList(res);
   }

   public PatchingChain<Unit> getUnits() {
      return this.unitChain;
   }

   public List<UnitBox> getAllUnitBoxes() {
      ArrayList<UnitBox> unitBoxList = new ArrayList();
      Iterator it = this.unitChain.iterator();

      while(it.hasNext()) {
         Unit item = (Unit)it.next();
         unitBoxList.addAll(item.getUnitBoxes());
      }

      it = this.trapChain.iterator();

      while(it.hasNext()) {
         Trap item = (Trap)it.next();
         unitBoxList.addAll(item.getUnitBoxes());
      }

      it = this.getTags().iterator();

      while(it.hasNext()) {
         Tag t = (Tag)it.next();
         if (t instanceof CodeAttribute) {
            unitBoxList.addAll(((CodeAttribute)t).getUnitBoxes());
         }
      }

      return unitBoxList;
   }

   public List<UnitBox> getUnitBoxes(boolean branchTarget) {
      ArrayList<UnitBox> unitBoxList = new ArrayList();
      Iterator it = this.unitChain.iterator();

      while(it.hasNext()) {
         Unit item = (Unit)it.next();
         if (branchTarget) {
            if (item.branches()) {
               unitBoxList.addAll(item.getUnitBoxes());
            }
         } else if (!item.branches()) {
            unitBoxList.addAll(item.getUnitBoxes());
         }
      }

      it = this.trapChain.iterator();

      while(it.hasNext()) {
         Trap item = (Trap)it.next();
         unitBoxList.addAll(item.getUnitBoxes());
      }

      it = this.getTags().iterator();

      while(it.hasNext()) {
         Tag t = (Tag)it.next();
         if (t instanceof CodeAttribute) {
            unitBoxList.addAll(((CodeAttribute)t).getUnitBoxes());
         }
      }

      return unitBoxList;
   }

   public List<ValueBox> getUseBoxes() {
      ArrayList<ValueBox> useBoxList = new ArrayList();
      Iterator it = this.unitChain.iterator();

      while(it.hasNext()) {
         Unit item = (Unit)it.next();
         useBoxList.addAll(item.getUseBoxes());
      }

      return useBoxList;
   }

   public List<ValueBox> getDefBoxes() {
      ArrayList<ValueBox> defBoxList = new ArrayList();
      Iterator it = this.unitChain.iterator();

      while(it.hasNext()) {
         Unit item = (Unit)it.next();
         defBoxList.addAll(item.getDefBoxes());
      }

      return defBoxList;
   }

   public List<ValueBox> getUseAndDefBoxes() {
      ArrayList<ValueBox> useAndDefBoxList = new ArrayList();
      Iterator it = this.unitChain.iterator();

      while(it.hasNext()) {
         Unit item = (Unit)it.next();
         useAndDefBoxList.addAll(item.getUseBoxes());
         useAndDefBoxList.addAll(item.getDefBoxes());
      }

      return useAndDefBoxList;
   }

   public void checkInit() {
      this.runValidation(CheckInitValidator.v());
   }

   public String toString() {
      ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
      PrintWriter writerOut = new PrintWriter(new EscapedWriter(new OutputStreamWriter(streamOut)));

      try {
         Printer.v().printTo(this, writerOut);
      } catch (RuntimeException var4) {
         logger.error((String)var4.getMessage(), (Throwable)var4);
      }

      writerOut.flush();
      writerOut.close();
      return streamOut.toString();
   }

   public long getModificationCount() {
      return this.localChain.getModificationCount() + this.unitChain.getModificationCount() + this.trapChain.getModificationCount();
   }
}
