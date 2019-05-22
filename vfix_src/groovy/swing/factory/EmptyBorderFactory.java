package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class EmptyBorderFactory extends SwingBorderFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)4;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204980L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204980 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$javax$swing$BorderFactory;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$EmptyBorderFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public EmptyBorderFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      CallSite[] var5 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty(var5[0].call(attributes, (Object)"parent"), $get$$class$groovy$swing$factory$EmptyBorderFactory(), var5[1].callGroovyObjectGetProperty(builder), "applyBorderToParent");
      if (!DefaultTypeTransformation.booleanUnbox(attributes)) {
         if (value instanceof Integer) {
            return (Object)ScriptBytecodeAdapter.castToType(var5[2].call($get$$class$javax$swing$BorderFactory(), value, value, value, value), $get$$class$java$lang$Object());
         } else {
            if (DefaultTypeTransformation.booleanUnbox(value instanceof List && ScriptBytecodeAdapter.compareEqual(var5[3].call(value), $const$0) ? Boolean.TRUE : Boolean.FALSE)) {
               Boolean ints = new Reference(Boolean.TRUE);
               var5[4].call(value, (Object)(new GeneratedClosure(this, this, ints) {
                  private Reference<T> ints;
                  // $FF: synthetic field
                  private static ClassInfo $staticClassInfo;
                  // $FF: synthetic field
                  private static SoftReference $callSiteArray;
                  // $FF: synthetic field
                  private static Class $class$java$lang$Object;
                  // $FF: synthetic field
                  private static Class $class$java$lang$Boolean;
                  // $FF: synthetic field
                  private static Class $class$groovy$swing$factory$EmptyBorderFactory$_newInstance_closure1;

                  public {
                     CallSite[] var4 = $getCallSiteArray();
                     this.ints = (Reference)ints;
                  }

                  public Object doCall(Object it) {
                     Object itx = new Reference(it);
                     CallSite[] var3 = $getCallSiteArray();
                     Boolean var10000 = (Boolean)ScriptBytecodeAdapter.castToType(var3[0].call(this.ints.get(), (Object)(itx.get() instanceof Integer ? Boolean.TRUE : Boolean.FALSE)), $get$$class$java$lang$Boolean());
                     this.ints.set(var10000);
                     return var10000;
                  }

                  public Boolean getInts() {
                     CallSite[] var1 = $getCallSiteArray();
                     return (Boolean)ScriptBytecodeAdapter.castToType(this.ints.get(), $get$$class$java$lang$Boolean());
                  }

                  public Object doCall() {
                     CallSite[] var1 = $getCallSiteArray();
                     return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                  }

                  // $FF: synthetic method
                  protected MetaClass $getStaticMetaClass() {
                     if (this.getClass() == $get$$class$groovy$swing$factory$EmptyBorderFactory$_newInstance_closure1()) {
                        return ScriptBytecodeAdapter.initMetaClass(this);
                     } else {
                        ClassInfo var1 = $staticClassInfo;
                        if (var1 == null) {
                           $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
                        }

                        return var1.getMetaClass();
                     }
                  }

                  // $FF: synthetic method
                  private static void $createCallSiteArray_1(String[] var0) {
                     var0[0] = "and";
                     var0[1] = "doCall";
                  }

                  // $FF: synthetic method
                  private static CallSiteArray $createCallSiteArray() {
                     String[] var0 = new String[2];
                     $createCallSiteArray_1(var0);
                     return new CallSiteArray($get$$class$groovy$swing$factory$EmptyBorderFactory$_newInstance_closure1(), var0);
                  }

                  // $FF: synthetic method
                  private static CallSite[] $getCallSiteArray() {
                     CallSiteArray var0;
                     if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
                        var0 = $createCallSiteArray();
                        $callSiteArray = new SoftReference(var0);
                     }

                     return var0.array;
                  }

                  // $FF: synthetic method
                  private static Class $get$$class$java$lang$Object() {
                     Class var10000 = $class$java$lang$Object;
                     if (var10000 == null) {
                        var10000 = $class$java$lang$Object = class$("java.lang.Object");
                     }

                     return var10000;
                  }

                  // $FF: synthetic method
                  private static Class $get$$class$java$lang$Boolean() {
                     Class var10000 = $class$java$lang$Boolean;
                     if (var10000 == null) {
                        var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
                     }

                     return var10000;
                  }

                  // $FF: synthetic method
                  private static Class $get$$class$groovy$swing$factory$EmptyBorderFactory$_newInstance_closure1() {
                     Class var10000 = $class$groovy$swing$factory$EmptyBorderFactory$_newInstance_closure1;
                     if (var10000 == null) {
                        var10000 = $class$groovy$swing$factory$EmptyBorderFactory$_newInstance_closure1 = class$("groovy.swing.factory.EmptyBorderFactory$_newInstance_closure1");
                     }

                     return var10000;
                  }

                  // $FF: synthetic method
                  static Class class$(String var0) {
                     try {
                        return Class.forName(var0);
                     } catch (ClassNotFoundException var2) {
                        throw new NoClassDefFoundError(var2.getMessage());
                     }
                  }
               }));
               if (DefaultTypeTransformation.booleanUnbox(ints.get())) {
                  CallSite var10000 = var5[5];
                  Class var10001 = $get$$class$javax$swing$BorderFactory();
                  Object[] var10002 = new Object[0];
                  Object[] var10003 = new Object[]{value};
                  int[] var11 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
                  return (Object)ScriptBytecodeAdapter.castToType(var10000.call(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var11)), $get$$class$java$lang$Object());
               }
            }

            throw (Throwable)var5[6].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " only accepts a single integer or an array of four integers as a value argument"})));
         }
      } else if (!ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
         throw (Throwable)var5[13].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " cannot be called with both an argulent value and attributes"})));
      } else {
         Integer top = (Integer)ScriptBytecodeAdapter.castToType(var5[7].call(attributes, (Object)"top"), $get$$class$java$lang$Integer());
         Integer left = (Integer)ScriptBytecodeAdapter.castToType(var5[8].call(attributes, (Object)"left"), $get$$class$java$lang$Integer());
         Integer bottom = (Integer)ScriptBytecodeAdapter.castToType(var5[9].call(attributes, (Object)"bottom"), $get$$class$java$lang$Integer());
         Integer right = (Integer)ScriptBytecodeAdapter.castToType(var5[10].call(attributes, (Object)"right"), $get$$class$java$lang$Integer());
         if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(top, (Object)null) && !ScriptBytecodeAdapter.compareEqual(top, (Object)null) ? Boolean.FALSE : Boolean.TRUE) && !ScriptBytecodeAdapter.compareEqual(top, (Object)null) ? Boolean.FALSE : Boolean.TRUE) && !ScriptBytecodeAdapter.compareEqual(top, (Object)null) ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(attributes) ? Boolean.FALSE : Boolean.TRUE)) {
            throw (Throwable)var5[11].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"When ", " is called it must be call with top:, left:, bottom:, right:, and no other attributes"})));
         } else {
            return (Object)ScriptBytecodeAdapter.castToType(var5[12].call($get$$class$javax$swing$BorderFactory(), top, left, bottom, right), $get$$class$java$lang$Object());
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$EmptyBorderFactory()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$4(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$factory$EmptyBorderFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$EmptyBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$EmptyBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$this$dist$get$3(String var1) {
      return super.this$dist$get$3(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public boolean super$3$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setChild(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setChild(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$3$this$dist$set$3(String var1, Object var2) {
      super.this$dist$set$3(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$3$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$3$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public boolean super$2$onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3) {
      return super.onNodeChildren(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$3$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$3$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "remove";
      var0[1] = "context";
      var0[2] = "createEmptyBorder";
      var0[3] = "size";
      var0[4] = "each";
      var0[5] = "createEmptyBorder";
      var0[6] = "<$constructor$>";
      var0[7] = "remove";
      var0[8] = "remove";
      var0[9] = "remove";
      var0[10] = "remove";
      var0[11] = "<$constructor$>";
      var0[12] = "createEmptyBorder";
      var0[13] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[14];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$EmptyBorderFactory(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Integer() {
      Class var10000 = $class$java$lang$Integer;
      if (var10000 == null) {
         var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$BorderFactory() {
      Class var10000 = $class$javax$swing$BorderFactory;
      if (var10000 == null) {
         var10000 = $class$javax$swing$BorderFactory = class$("javax.swing.BorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$RuntimeException() {
      Class var10000 = $class$java$lang$RuntimeException;
      if (var10000 == null) {
         var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$EmptyBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$EmptyBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$EmptyBorderFactory = class$("groovy.swing.factory.EmptyBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
