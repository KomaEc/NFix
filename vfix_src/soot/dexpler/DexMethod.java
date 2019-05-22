package soot.dexpler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodSource;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.toolkits.typing.TypeAssigner;
import soot.options.Options;

public class DexMethod {
   private static final Logger logger = LoggerFactory.getLogger(DexMethod.class);
   protected final DexFile dexFile;
   protected final SootClass declaringClass;

   public DexMethod(DexFile dexFile, SootClass declaringClass) {
      this.dexFile = dexFile;
      this.declaringClass = declaringClass;
   }

   public SootMethod makeSootMethod(Method method) {
      int accessFlags = method.getAccessFlags();
      String name = method.getName();
      List<SootClass> thrownExceptions = this.getThrownExceptions(method);
      List<Type> parameterTypes = this.getParameterTypes(method);
      Type returnType = DexType.toSoot(method.getReturnType());
      SootMethod sm = this.declaringClass.getMethodUnsafe(name, parameterTypes, returnType);
      if (sm == null) {
         sm = Scene.v().makeSootMethod(name, parameterTypes, returnType, accessFlags, thrownExceptions);
      }

      int flags = method.getAccessFlags();
      if (!Modifier.isAbstract(flags) && !Modifier.isNative(flags)) {
         if (Options.v().oaat() && this.declaringClass.resolvingLevel() <= 2) {
            return sm;
         } else {
            sm.setSource(this.createMethodSource(method));
            return sm;
         }
      } else {
         return sm;
      }
   }

   protected MethodSource createMethodSource(final Method method) {
      return new MethodSource() {
         public Body getBody(SootMethod m, String phaseName) {
            JimpleBody b = Jimple.v().newBody(m);

            try {
               DexBody dexBody = new DexBody(DexMethod.this.dexFile, method, DexMethod.this.declaringClass.getType());
               dexBody.jimplify(b, m);
            } catch (InvalidDalvikBytecodeException var6) {
               String msg = "Warning: Invalid bytecode in method " + m + ": " + var6;
               DexMethod.logger.debug("" + msg);
               Util.emptyBody(b);
               Util.addExceptionAfterUnit(b, "java.lang.RuntimeException", b.getUnits().getLast(), "Soot has detected that this method contains invalid Dalvik bytecode which would have throw an exception at runtime. [" + msg + "]");
               TypeAssigner.v().transform(b);
            }

            m.setActiveBody(b);
            return m.getActiveBody();
         }
      };
   }

   protected List<Type> getParameterTypes(Method method) {
      List<Type> parameterTypes = new ArrayList();
      if (method.getParameters() != null) {
         List<? extends CharSequence> parameters = method.getParameterTypes();
         Iterator var4 = parameters.iterator();

         while(var4.hasNext()) {
            CharSequence t = (CharSequence)var4.next();
            Type type = DexType.toSoot(t.toString());
            parameterTypes.add(type);
         }
      }

      return parameterTypes;
   }

   protected List<SootClass> getThrownExceptions(Method method) {
      List<SootClass> thrownExceptions = new ArrayList();
      Iterator var3 = method.getAnnotations().iterator();

      label40:
      while(true) {
         Annotation a;
         String atypes;
         do {
            if (!var3.hasNext()) {
               return thrownExceptions;
            }

            a = (Annotation)var3.next();
            Type atype = DexType.toSoot(a.getType());
            atypes = atype.toString();
         } while(!atypes.equals("dalvik.annotation.Throws"));

         Iterator var7 = a.getElements().iterator();

         while(true) {
            EncodedValue ev;
            do {
               if (!var7.hasNext()) {
                  continue label40;
               }

               AnnotationElement ae = (AnnotationElement)var7.next();
               ev = ae.getValue();
            } while(!(ev instanceof ArrayEncodedValue));

            Iterator var10 = ((ArrayEncodedValue)ev).getValue().iterator();

            while(var10.hasNext()) {
               EncodedValue evSub = (EncodedValue)var10.next();
               if (evSub instanceof TypeEncodedValue) {
                  TypeEncodedValue valueType = (TypeEncodedValue)evSub;
                  String exceptionName = valueType.getValue();
                  String dottedName = Util.dottedClassName(exceptionName);
                  thrownExceptions.add(SootResolver.v().makeClassRef(dottedName));
               }
            }
         }
      }
   }
}
