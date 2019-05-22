package soot.dexpler.instructions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.reference.MethodReference;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;

public abstract class MethodInvocationInstruction extends DexlibAbstractInstruction implements DanglingInstruction {
   protected InvokeExpr invocation;
   protected AssignStmt assign = null;
   private SootMethodRef methodRef = null;

   public MethodInvocationInstruction(Instruction instruction, int codeAddress) {
      super(instruction, codeAddress);
   }

   public void finalize(DexBody body, DexlibAbstractInstruction successor) {
      if (successor instanceof MoveResultInstruction) {
         this.assign = Jimple.v().newAssignStmt(body.getStoreResultLocal(), this.invocation);
         this.setUnit(this.assign);
         this.addTags(this.assign);
         body.add(this.assign);
         this.unit = this.assign;
      } else {
         InvokeStmt invoke = Jimple.v().newInvokeStmt(this.invocation);
         this.setUnit(invoke);
         this.addTags(invoke);
         body.add(invoke);
         this.unit = invoke;
      }

   }

   public Set<Type> introducedTypes() {
      Set<Type> types = new HashSet();
      MethodReference method = (MethodReference)((MethodReference)((ReferenceInstruction)this.instruction).getReference());
      types.add(DexType.toSoot(method.getDefiningClass()));
      types.add(DexType.toSoot(method.getReturnType()));
      List<? extends CharSequence> paramTypes = method.getParameterTypes();
      if (paramTypes != null) {
         Iterator var4 = paramTypes.iterator();

         while(var4.hasNext()) {
            CharSequence type = (CharSequence)var4.next();
            types.add(DexType.toSoot(type.toString()));
         }
      }

      return types;
   }

   boolean isUsedAsFloatingPoint(DexBody body, int register) {
      return this.isUsedAsFloatingPoint(body, register, false);
   }

   protected boolean isUsedAsFloatingPoint(DexBody body, int register, boolean isStatic) {
      MethodReference item = (MethodReference)((ReferenceInstruction)this.instruction).getReference();
      List<? extends CharSequence> paramTypes = item.getParameterTypes();
      List<Integer> regs = this.getUsedRegistersNums();
      if (paramTypes == null) {
         return false;
      } else {
         int i = 0;

         for(int j = 0; i < regs.size(); ++j) {
            if (!isStatic && i == 0) {
               --j;
            } else {
               if ((Integer)regs.get(i) == register && Util.isFloatLike(DexType.toSoot(((CharSequence)paramTypes.get(j)).toString()))) {
                  return true;
               }

               if (DexType.isWide(((CharSequence)paramTypes.get(j)).toString())) {
                  ++i;
               }
            }

            ++i;
         }

         return false;
      }
   }

   protected boolean isUsedAsObject(DexBody body, int register, boolean isStatic) {
      MethodReference item = (MethodReference)((ReferenceInstruction)this.instruction).getReference();
      List<? extends CharSequence> paramTypes = item.getParameterTypes();
      List<Integer> regs = this.getUsedRegistersNums();
      if (paramTypes == null) {
         return false;
      } else if (!isStatic && (Integer)regs.get(0) == register) {
         return true;
      } else {
         int i = 0;

         for(int j = 0; i < regs.size(); ++j) {
            if (!isStatic && i == 0) {
               --j;
            } else {
               if ((Integer)regs.get(i) == register && DexType.toSoot(((CharSequence)paramTypes.get(j)).toString()) instanceof RefType) {
                  return true;
               }

               if (DexType.isWide(((CharSequence)paramTypes.get(j)).toString())) {
                  ++i;
               }
            }

            ++i;
         }

         return false;
      }
   }

   protected SootMethodRef getSootMethodRef() {
      return this.getSootMethodRef(MethodInvocationInstruction.InvocationType.Virtual);
   }

   protected SootMethodRef getStaticSootMethodRef() {
      return this.getSootMethodRef(MethodInvocationInstruction.InvocationType.Static);
   }

   private SootMethodRef getSootMethodRef(MethodInvocationInstruction.InvocationType invType) {
      if (this.methodRef != null) {
         return this.methodRef;
      } else {
         MethodReference mItem = (MethodReference)((ReferenceInstruction)this.instruction).getReference();
         String tItem = mItem.getDefiningClass();
         String className;
         if (tItem.startsWith("[")) {
            className = "java.lang.Object";
         } else {
            className = Util.dottedClassName(tItem);
         }

         SootClass sc = SootResolver.v().makeClassRef(className);
         if (invType == MethodInvocationInstruction.InvocationType.Interface && sc.isPhantom()) {
            sc.setModifiers(sc.getModifiers() | 512);
         }

         String methodName = mItem.getName();
         Type returnType = DexType.toSoot(mItem.getReturnType());
         List<Type> parameterTypes = new ArrayList();
         List<? extends CharSequence> paramTypes = mItem.getParameterTypes();
         if (paramTypes != null) {
            Iterator var10 = paramTypes.iterator();

            while(var10.hasNext()) {
               CharSequence type = (CharSequence)var10.next();
               parameterTypes.add(DexType.toSoot(type.toString()));
            }
         }

         this.methodRef = Scene.v().makeMethodRef(sc, methodName, parameterTypes, returnType, invType == MethodInvocationInstruction.InvocationType.Static);
         return this.methodRef;
      }
   }

   protected MethodReference getTargetMethodReference() {
      return (MethodReference)((ReferenceInstruction)this.instruction).getReference();
   }

   protected List<Local> buildParameters(DexBody body, boolean isStatic) {
      MethodReference item = this.getTargetMethodReference();
      List<? extends CharSequence> paramTypes = item.getParameterTypes();
      List<Local> parameters = new ArrayList();
      List<Integer> regs = this.getUsedRegistersNums();
      int i = 0;

      for(int j = 0; i < regs.size(); ++j) {
         parameters.add(body.getRegisterLocal((Integer)regs.get(i)));
         if (!isStatic && i == 0) {
            --j;
         } else if (paramTypes != null && DexType.isWide(((CharSequence)paramTypes.get(j)).toString())) {
            ++i;
         }

         ++i;
      }

      return parameters;
   }

   protected List<Integer> getUsedRegistersNums() {
      if (this.instruction instanceof Instruction35c) {
         return this.getUsedRegistersNums((Instruction35c)this.instruction);
      } else if (this.instruction instanceof Instruction3rc) {
         return this.getUsedRegistersNums((Instruction3rc)this.instruction);
      } else {
         throw new RuntimeException("Instruction is neither a InvokeInstruction nor a InvokeRangeInstruction");
      }
   }

   protected void jimplifyVirtual(DexBody body) {
      SootMethodRef ref = this.getSootMethodRef();
      if (ref.declaringClass().isInterface()) {
         this.methodRef = null;
         this.jimplifyInterface(body);
      } else {
         List<Local> parameters = this.buildParameters(body, false);
         this.invocation = Jimple.v().newVirtualInvokeExpr((Local)parameters.get(0), ref, parameters.subList(1, parameters.size()));
         body.setDanglingInstruction(this);
      }
   }

   protected void jimplifyInterface(DexBody body) {
      SootMethodRef ref = this.getSootMethodRef(MethodInvocationInstruction.InvocationType.Interface);
      if (!ref.declaringClass().isInterface()) {
         this.jimplifyVirtual(body);
      } else {
         List<Local> parameters = this.buildParameters(body, false);
         this.invocation = Jimple.v().newInterfaceInvokeExpr((Local)parameters.get(0), this.getSootMethodRef(MethodInvocationInstruction.InvocationType.Interface), parameters.subList(1, parameters.size()));
         body.setDanglingInstruction(this);
      }
   }

   protected void jimplifySpecial(DexBody body) {
      List<Local> parameters = this.buildParameters(body, false);
      this.invocation = Jimple.v().newSpecialInvokeExpr((Local)parameters.get(0), this.getSootMethodRef(), parameters.subList(1, parameters.size()));
      body.setDanglingInstruction(this);
   }

   protected void jimplifyStatic(DexBody body) {
      this.invocation = Jimple.v().newStaticInvokeExpr(this.getStaticSootMethodRef(), this.buildParameters(body, true));
      body.setDanglingInstruction(this);
   }

   private static enum InvocationType {
      Static,
      Interface,
      Virtual;
   }
}
