package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import javax.swing.border.Border;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class CompoundBorderFactory extends SwingBorderFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)3;
   // $FF: synthetic field
   private static final Integer $const$4 = (Integer)-1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204972L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204972 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$javax$swing$border$CompoundBorder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$javax$swing$border$Border;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$CompoundBorderFactory;

   public CompoundBorderFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      CallSite[] var5 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty(var5[0].call(attributes, (Object)"parent"), $get$$class$groovy$swing$factory$CompoundBorderFactory(), var5[1].callGroovyObjectGetProperty(builder), "applyBorderToParent");
      Border border = (Border)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$border$Border());
      Object inner;
      if (value instanceof List) {
         inner = var5[2].call(value);
         if (ScriptBytecodeAdapter.isCase(inner, $const$0)) {
            throw (Throwable)var5[3].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " does not accept an empty array as an value argument"})));
         }

         if (ScriptBytecodeAdapter.isCase(inner, $const$1)) {
            border = (Border)ScriptBytecodeAdapter.castToType(var5[4].call(value, (Object)$const$0), $get$$class$javax$swing$border$Border());
         } else if (ScriptBytecodeAdapter.isCase(inner, $const$2)) {
            border = var5[5].callConstructor($get$$class$javax$swing$border$CompoundBorder(), var5[6].call(value, (Object)$const$0), var5[7].call(value, (Object)$const$1));
         } else {
            if (ScriptBytecodeAdapter.isCase(inner, $const$3)) {
            }

            border = var5[8].callConstructor($get$$class$javax$swing$border$CompoundBorder(), var5[9].call(value, (Object)$const$0), var5[10].call(value, (Object)$const$1));
            border = (Border)ScriptBytecodeAdapter.castToType(var5[11].call(var5[12].call(value, (Object)ScriptBytecodeAdapter.createRange($const$2, $const$4, true)), border, new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$swing$factory$CompoundBorderFactory$_newInstance_closure1;
               // $FF: synthetic field
               private static Class $class$javax$swing$border$CompoundBorder;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object that, Object it) {
                  Object thatx = new Reference(that);
                  Object itx = new Reference(it);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[0].callConstructor($get$$class$javax$swing$border$CompoundBorder(), thatx.get(), itx.get());
               }

               public Object call(Object that, Object it) {
                  Object thatx = new Reference(that);
                  Object itx = new Reference(it);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[1].callCurrent(this, thatx.get(), itx.get());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$swing$factory$CompoundBorderFactory$_newInstance_closure1()) {
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
                  var0[0] = "<$constructor$>";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$swing$factory$CompoundBorderFactory$_newInstance_closure1(), var0);
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
               private static Class $get$$class$groovy$swing$factory$CompoundBorderFactory$_newInstance_closure1() {
                  Class var10000 = $class$groovy$swing$factory$CompoundBorderFactory$_newInstance_closure1;
                  if (var10000 == null) {
                     var10000 = $class$groovy$swing$factory$CompoundBorderFactory$_newInstance_closure1 = class$("groovy.swing.factory.CompoundBorderFactory$_newInstance_closure1");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$javax$swing$border$CompoundBorder() {
                  Class var10000 = $class$javax$swing$border$CompoundBorder;
                  if (var10000 == null) {
                     var10000 = $class$javax$swing$border$CompoundBorder = class$("javax.swing.border.CompoundBorder");
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
            }), $get$$class$javax$swing$border$Border());
         }
      }

      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(border) && DefaultTypeTransformation.booleanUnbox(attributes) ? Boolean.TRUE : Boolean.FALSE)) {
         if (DefaultTypeTransformation.booleanUnbox(value)) {
            throw (Throwable)var5[13].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " only accepts an array of borders as a value argument"})));
         }

         inner = var5[14].call(attributes, (Object)"inner");
         Object outer = var5[15].call(attributes, (Object)"outer");
         if (DefaultTypeTransformation.booleanUnbox(inner instanceof Border && outer instanceof Border ? Boolean.TRUE : Boolean.FALSE)) {
            border = var5[16].callConstructor($get$$class$javax$swing$border$CompoundBorder(), outer, inner);
         }
      }

      if (!DefaultTypeTransformation.booleanUnbox(border)) {
         throw (Throwable)var5[17].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " only accepts an array of javax.swing.border.Border or an inner: and outer: attribute"})));
      } else {
         return (Object)ScriptBytecodeAdapter.castToType(border, $get$$class$java$lang$Object());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$CompoundBorderFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$CompoundBorderFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$CompoundBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$CompoundBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[2] = "size";
      var0[3] = "<$constructor$>";
      var0[4] = "getAt";
      var0[5] = "<$constructor$>";
      var0[6] = "getAt";
      var0[7] = "getAt";
      var0[8] = "<$constructor$>";
      var0[9] = "getAt";
      var0[10] = "getAt";
      var0[11] = "inject";
      var0[12] = "getAt";
      var0[13] = "<$constructor$>";
      var0[14] = "remove";
      var0[15] = "remove";
      var0[16] = "<$constructor$>";
      var0[17] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[18];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$CompoundBorderFactory(), var0);
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
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
   private static Class $get$$class$java$lang$RuntimeException() {
      Class var10000 = $class$java$lang$RuntimeException;
      if (var10000 == null) {
         var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$border$CompoundBorder() {
      Class var10000 = $class$javax$swing$border$CompoundBorder;
      if (var10000 == null) {
         var10000 = $class$javax$swing$border$CompoundBorder = class$("javax.swing.border.CompoundBorder");
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
   private static Class $get$$class$javax$swing$border$Border() {
      Class var10000 = $class$javax$swing$border$Border;
      if (var10000 == null) {
         var10000 = $class$javax$swing$border$Border = class$("javax.swing.border.Border");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$CompoundBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$CompoundBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$CompoundBorderFactory = class$("groovy.swing.factory.CompoundBorderFactory");
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
