package soot.toDex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;

public class RegisterAllocator {
   private int nextRegNum;
   private Map<Local, Integer> localToLastRegNum = new HashMap();
   private int paramRegCount;
   private List<Register> classConstantReg = new ArrayList();
   private List<Register> nullConstantReg = new ArrayList();
   private List<Register> floatConstantReg = new ArrayList();
   private List<Register> intConstantReg = new ArrayList();
   private List<Register> longConstantReg = new ArrayList();
   private List<Register> doubleConstantReg = new ArrayList();
   private List<Register> stringConstantReg = new ArrayList();
   private AtomicInteger classI = new AtomicInteger(0);
   private AtomicInteger nullI = new AtomicInteger(0);
   private AtomicInteger floatI = new AtomicInteger(0);
   private AtomicInteger intI = new AtomicInteger(0);
   private AtomicInteger longI = new AtomicInteger(0);
   private AtomicInteger doubleI = new AtomicInteger(0);
   private AtomicInteger stringI = new AtomicInteger(0);
   private Set<Register> lockedRegisters = new HashSet();
   private int lastReg;
   private Register currentLocalRegister;

   private Register asConstant(Constant c, ConstantVisitor constantV) {
      Register constantRegister = null;
      List<Register> rArray = null;
      AtomicInteger iI = null;
      if (c instanceof ClassConstant) {
         rArray = this.classConstantReg;
         iI = this.classI;
      } else if (c instanceof NullConstant) {
         rArray = this.nullConstantReg;
         iI = this.nullI;
      } else if (c instanceof FloatConstant) {
         rArray = this.floatConstantReg;
         iI = this.floatI;
      } else if (c instanceof IntConstant) {
         rArray = this.intConstantReg;
         iI = this.intI;
      } else if (c instanceof LongConstant) {
         rArray = this.longConstantReg;
         iI = this.longI;
      } else if (c instanceof DoubleConstant) {
         rArray = this.doubleConstantReg;
         iI = this.doubleI;
      } else {
         if (!(c instanceof StringConstant)) {
            throw new RuntimeException("Error. Unknown constant type: '" + c.getType() + "'");
         }

         rArray = this.stringConstantReg;
         iI = this.stringI;
      }

      for(boolean inConflict = true; inConflict; inConflict = this.lockedRegisters.contains(constantRegister)) {
         if (rArray.size() == 0 || iI.intValue() >= rArray.size()) {
            rArray.add(new Register(c.getType(), this.nextRegNum));
            this.nextRegNum += SootToDexUtils.getDexWords(c.getType());
         }

         constantRegister = ((Register)rArray.get(iI.getAndIncrement())).clone();
      }

      constantV.setDestination(constantRegister);
      c.apply(constantV);
      return constantRegister.clone();
   }

   public void resetImmediateConstantsPool() {
      this.classI = new AtomicInteger(0);
      this.nullI = new AtomicInteger(0);
      this.floatI = new AtomicInteger(0);
      this.intI = new AtomicInteger(0);
      this.longI = new AtomicInteger(0);
      this.doubleI = new AtomicInteger(0);
      this.stringI = new AtomicInteger(0);
   }

   public Map<Local, Integer> getLocalToRegisterMapping() {
      return this.localToLastRegNum;
   }

   public Register asLocal(Local local) {
      Integer oldRegNum = (Integer)this.localToLastRegNum.get(local);
      Register localRegister;
      if (oldRegNum != null) {
         localRegister = new Register(local.getType(), oldRegNum);
      } else {
         localRegister = new Register(local.getType(), this.nextRegNum);
         this.localToLastRegNum.put(local, this.nextRegNum);
         this.nextRegNum += SootToDexUtils.getDexWords(local.getType());
      }

      return localRegister;
   }

   public void asParameter(SootMethod sm, Local l) {
      if (!this.localToLastRegNum.containsKey(l)) {
         int paramRegNum = 0;
         boolean found = false;
         if (!sm.isStatic()) {
            try {
               if (sm.getActiveBody().getThisLocal() == l) {
                  paramRegNum = 0;
                  found = true;
               }
            } catch (RuntimeException var7) {
            }
         }

         int i;
         if (!found) {
            for(i = 0; i < sm.getParameterCount(); ++i) {
               if (sm.getActiveBody().getParameterLocal(i) == l) {
                  if (!sm.isStatic()) {
                     ++paramRegNum;
                  }

                  found = true;
                  break;
               }

               Type paramType = sm.getParameterType(i);
               paramRegNum += SootToDexUtils.getDexWords(paramType);
            }
         }

         if (!found) {
            throw new RuntimeException("Parameter local not found");
         } else {
            this.localToLastRegNum.put(l, paramRegNum);
            i = SootToDexUtils.getDexWords(l.getType());
            this.nextRegNum = Math.max(this.nextRegNum + i, paramRegNum + i);
            this.paramRegCount += i;
         }
      }
   }

   public Register asImmediate(Value v, ConstantVisitor constantV) {
      if (v instanceof Constant) {
         return this.asConstant((Constant)v, constantV);
      } else if (v instanceof Local) {
         return this.asLocal((Local)v);
      } else {
         throw new RuntimeException("expected Immediate (Constant or Local), but was: " + v.getClass());
      }
   }

   public Register asTmpReg(Type regType) {
      int newRegCount = this.getRegCount();
      if (this.lastReg == newRegCount) {
         return this.currentLocalRegister;
      } else {
         this.currentLocalRegister = this.asLocal(new TemporaryRegisterLocal(regType));
         this.lastReg = newRegCount;
         return this.currentLocalRegister;
      }
   }

   public void increaseRegCount(int amount) {
      this.nextRegNum += amount;
   }

   public int getParamRegCount() {
      return this.paramRegCount;
   }

   public int getRegCount() {
      return this.nextRegNum;
   }

   public void lockRegister(Register reg) {
      this.lockedRegisters.add(reg);
   }
}
