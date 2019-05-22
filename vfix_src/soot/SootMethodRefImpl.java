package soot;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.util.NumberedString;

public class SootMethodRefImpl implements SootMethodRef {
   private static final Logger logger = LoggerFactory.getLogger(SootMethodRefImpl.class);
   private final SootClass declaringClass;
   private final String name;
   protected List<Type> parameterTypes;
   private final Type returnType;
   private final boolean isStatic;
   private NumberedString subsig;

   public SootMethodRefImpl(SootClass declaringClass, String name, List<Type> parameterTypes, Type returnType, boolean isStatic) {
      this.declaringClass = declaringClass;
      this.name = name;
      if (parameterTypes == null) {
         this.parameterTypes = null;
      } else {
         List<Type> l = new ArrayList();
         l.addAll(parameterTypes);
         this.parameterTypes = Collections.unmodifiableList(l);
      }

      this.returnType = returnType;
      this.isStatic = isStatic;
      if (declaringClass == null) {
         throw new RuntimeException("Attempt to create SootMethodRef with null class");
      } else if (name == null) {
         throw new RuntimeException("Attempt to create SootMethodRef with null name");
      } else if (returnType == null) {
         throw new RuntimeException("Attempt to create SootMethodRef with null returnType");
      }
   }

   public SootClass declaringClass() {
      return this.declaringClass;
   }

   public String name() {
      return this.name;
   }

   public List<Type> parameterTypes() {
      return this.parameterTypes == null ? Collections.emptyList() : this.parameterTypes;
   }

   public Type returnType() {
      return this.returnType;
   }

   public boolean isStatic() {
      return this.isStatic;
   }

   public NumberedString getSubSignature() {
      if (this.subsig == null) {
         this.subsig = Scene.v().getSubSigNumberer().findOrAdd(SootMethod.getSubSignature(this.name, this.parameterTypes, this.returnType));
      }

      return this.subsig;
   }

   public String getSignature() {
      return SootMethod.getSignature(this.declaringClass, this.name, this.parameterTypes, this.returnType);
   }

   public Type parameterType(int i) {
      return (Type)this.parameterTypes.get(i);
   }

   public SootMethod resolve() {
      return this.resolve((StringBuffer)null);
   }

   public SootMethod tryResolve() {
      return this.tryResolve((StringBuffer)null);
   }

   private SootMethod checkStatic(SootMethod ret) {
      if (ret.isStatic() != this.isStatic() && Options.v().wrong_staticness() != 2 && Options.v().wrong_staticness() != 4) {
         throw new ResolutionFailedException("Resolved " + this + " to " + ret + " which has wrong static-ness");
      } else {
         return ret;
      }
   }

   private SootMethod tryResolve(StringBuffer trace) {
      if (this.declaringClass.getName().equals("java.dyn.InvokeDynamic")) {
         throw new IllegalStateException("Cannot resolve invokedynamic method references at compile time!");
      } else {
         SootClass cl = this.declaringClass;

         while(true) {
            if (trace != null) {
               trace.append("Looking in " + cl + " which has methods " + cl.getMethods() + "\n");
            }

            SootMethod sm = cl.getMethodUnsafe(this.getSubSignature());
            if (sm != null) {
               return this.checkStatic(sm);
            }

            if (Scene.v().allowsPhantomRefs() && (cl.isPhantom() || Options.v().ignore_resolution_errors())) {
               SootMethod m = Scene.v().makeSootMethod(this.name, this.parameterTypes, this.returnType, this.isStatic() ? 8 : 0);
               m.setPhantom(true);
               m = cl.getOrAddMethod(m);
               return this.checkStatic(m);
            }

            if (!cl.hasSuperclass()) {
               cl = this.declaringClass;

               while(true) {
                  ArrayDeque<SootClass> queue = new ArrayDeque();
                  queue.addAll(cl.getInterfaces());

                  while(true) {
                     SootClass iface = (SootClass)queue.poll();
                     if (iface == null) {
                        if (!cl.hasSuperclass()) {
                           return null;
                        }

                        cl = cl.getSuperclass();
                        break;
                     }

                     if (trace != null) {
                        trace.append("Looking in " + iface + " which has methods " + iface.getMethods() + "\n");
                     }

                     SootMethod sm = iface.getMethodUnsafe(this.getSubSignature());
                     if (sm != null) {
                        return this.checkStatic(sm);
                     }

                     queue.addAll(iface.getInterfaces());
                  }
               }
            }

            cl = cl.getSuperclass();
         }
      }
   }

   private SootMethod resolve(StringBuffer trace) {
      SootMethod resolved = this.tryResolve(trace);
      if (resolved != null) {
         return resolved;
      } else if (Options.v().allow_phantom_refs()) {
         return this.createUnresolvedErrorMethod(this.declaringClass);
      } else {
         if (trace == null) {
            SootMethodRefImpl.ClassResolutionFailedException e = new SootMethodRefImpl.ClassResolutionFailedException();
            if (!Options.v().ignore_resolution_errors()) {
               throw e;
            }

            logger.debug("" + e.getMessage());
         }

         return null;
      }
   }

   private SootMethod createUnresolvedErrorMethod(SootClass declaringClass) {
      SootMethod m = Scene.v().makeSootMethod(this.name, this.parameterTypes, this.returnType, this.isStatic() ? 8 : 0);
      int modifiers = 1;
      if (this.isStatic()) {
         modifiers |= 8;
      }

      m.setModifiers(modifiers);
      JimpleBody body = Jimple.v().newBody(m);
      m.setActiveBody(body);
      LocalGenerator lg = new LocalGenerator(body);
      body.insertIdentityStmts(declaringClass);
      RefType runtimeExceptionType = RefType.v("java.lang.Error");
      NewExpr newExpr = Jimple.v().newNewExpr(runtimeExceptionType);
      Local exceptionLocal = lg.generateLocal(runtimeExceptionType);
      AssignStmt assignStmt = Jimple.v().newAssignStmt(exceptionLocal, newExpr);
      body.getUnits().add((Unit)assignStmt);
      SootMethodRef cref = Scene.v().makeConstructorRef(runtimeExceptionType.getSootClass(), Collections.singletonList(RefType.v("java.lang.String")));
      SpecialInvokeExpr constructorInvokeExpr = Jimple.v().newSpecialInvokeExpr(exceptionLocal, cref, (Value)StringConstant.v("Unresolved compilation error: Method " + this.getSignature() + " does not exist!"));
      InvokeStmt initStmt = Jimple.v().newInvokeStmt(constructorInvokeExpr);
      body.getUnits().insertAfter((Unit)initStmt, (Unit)assignStmt);
      body.getUnits().insertAfter((Unit)Jimple.v().newThrowStmt(exceptionLocal), (Unit)initStmt);
      return declaringClass.getOrAddMethod(m);
   }

   public String toString() {
      return this.getSignature();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.declaringClass == null ? 0 : this.declaringClass.hashCode());
      result = 31 * result + (this.isStatic ? 1231 : 1237);
      result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      result = 31 * result + (this.parameterTypes == null ? 0 : this.parameterTypes.hashCode());
      result = 31 * result + (this.returnType == null ? 0 : this.returnType.hashCode());
      result = 31 * result + (this.subsig == null ? 0 : this.subsig.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         SootMethodRefImpl other = (SootMethodRefImpl)obj;
         if (this.declaringClass == null) {
            if (other.declaringClass != null) {
               return false;
            }
         } else if (!this.declaringClass.equals(other.declaringClass)) {
            return false;
         }

         if (this.isStatic != other.isStatic) {
            return false;
         } else {
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            if (this.parameterTypes == null) {
               if (other.parameterTypes != null) {
                  return false;
               }
            } else if (!this.parameterTypes.equals(other.parameterTypes)) {
               return false;
            }

            if (this.returnType == null) {
               if (other.returnType != null) {
                  return false;
               }
            } else if (!this.returnType.equals(other.returnType)) {
               return false;
            }

            if (this.subsig == null) {
               if (other.subsig != null) {
                  return false;
               }
            } else if (!this.subsig.equals(other.subsig)) {
               return false;
            }

            return true;
         }
      }
   }

   public class ClassResolutionFailedException extends ResolutionFailedException {
      private static final long serialVersionUID = 5430199603403917938L;

      public ClassResolutionFailedException() {
         super("Class " + SootMethodRefImpl.this.declaringClass + " doesn't have method " + SootMethodRefImpl.this.name + "(" + (SootMethodRefImpl.this.parameterTypes == null ? "" : SootMethodRefImpl.this.parameterTypes) + ") : " + SootMethodRefImpl.this.returnType + "; failed to resolve in superclasses and interfaces");
      }

      public String toString() {
         StringBuffer ret = new StringBuffer();
         ret.append(super.toString());
         SootMethodRefImpl.this.resolve(ret);
         return ret.toString();
      }
   }
}
