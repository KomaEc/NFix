package groovy.xml.streamingmarkupsupport;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AbstractStreamingBuilder implements GroovyObject {
   private Object badTagClosure;
   private Object namespaceSetupClosure;
   private Object aliasSetupClosure;
   private Object getNamespaceClosure;
   private Object specialTags;
   private Object builder;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203685L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203685 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public AbstractStreamingBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.badTagClosure = new AbstractStreamingBuilder._closure1(this, this);
      this.namespaceSetupClosure = new AbstractStreamingBuilder._closure2(this, this);
      this.aliasSetupClosure = new AbstractStreamingBuilder._closure3(this, this);
      this.getNamespaceClosure = new AbstractStreamingBuilder._closure4(this, this);
      this.specialTags = ScriptBytecodeAdapter.createMap(new Object[]{"declareNamespace", this.namespaceSetupClosure, "declareAlias", this.aliasSetupClosure, "getNamespaces", this.getNamespaceClosure});
      this.builder = null;
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder()) {
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
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public Object getBadTagClosure() {
      return this.badTagClosure;
   }

   public void setBadTagClosure(Object var1) {
      this.badTagClosure = var1;
   }

   public Object getNamespaceSetupClosure() {
      return this.namespaceSetupClosure;
   }

   public void setNamespaceSetupClosure(Object var1) {
      this.namespaceSetupClosure = var1;
   }

   public Object getAliasSetupClosure() {
      return this.aliasSetupClosure;
   }

   public void setAliasSetupClosure(Object var1) {
      this.aliasSetupClosure = var1;
   }

   public Object getGetNamespaceClosure() {
      return this.getNamespaceClosure;
   }

   public void setGetNamespaceClosure(Object var1) {
      this.getNamespaceClosure = var1;
   }

   public Object getSpecialTags() {
      return this.specialTags;
   }

   public void setSpecialTags(Object var1) {
      this.specialTags = var1;
   }

   public Object getBuilder() {
      return this.builder;
   }

   public void setBuilder(Object var1) {
      this.builder = var1;
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[0];
      return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder(), var0);
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
   private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder() {
      Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder");
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

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure1;
      // $FF: synthetic field
      private static Class $class$groovy$lang$GroovyRuntimeException;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object tag, Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefixx, Object... rest) {
         Object tagx = new Reference(tag);
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespacesx = new Reference(namespaces);
         Object prefix = new Reference(prefixx);
         CallSite[] var12 = $getCallSiteArray();
         Object uri = new Reference(var12[0].call(pendingNamespacesx.get(), prefix.get()));
         if (ScriptBytecodeAdapter.compareEqual(uri.get(), (Object)null)) {
            uri.set(var12[1].call(namespacesx.get(), prefix.get()));
         }

         throw (Throwable)var12[2].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{tagx.get(), uri.get()}, new String[]{"Tag ", " is not allowed in namespace ", ""})));
      }

      public Object call(Object tag, Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefixx, Object... rest) {
         Object tagx = new Reference(tag);
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespacesx = new Reference(namespaces);
         Object prefix = new Reference(prefixx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[3].callCurrent(this, (Object[])ArrayUtil.createArray(tagx.get(), doc, pendingNamespacesx.get(), namespacesx.get(), namespaceSpecificTags, prefix.get(), rest));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure1()) {
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
         var0[0] = "getAt";
         var0[1] = "getAt";
         var0[2] = "<$constructor$>";
         var0[3] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[4];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure1(), var0);
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
      private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure1() {
         Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure1 = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder$_closure1");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$groovy$lang$GroovyRuntimeException() {
         Class var10000 = $class$groovy$lang$GroovyRuntimeException;
         if (var10000 == null) {
            var10000 = $class$groovy$lang$GroovyRuntimeException = class$("groovy.lang.GroovyRuntimeException");
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

   class _closure2 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrsx, Object... rest) {
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespacesx = new Reference(namespaces);
         Object namespaceSpecificTagsx = new Reference(namespaceSpecificTags);
         Object attrs = new Reference(attrsx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[0].call(attrs.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), pendingNamespacesx, namespaceSpecificTagsx, namespacesx) {
            private Reference<T> pendingNamespaces;
            private Reference<T> namespaceSpecificTags;
            private Reference<T> namespaces;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static final Integer $const$1 = (Integer)1;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2_closure5;

            public {
               CallSite[] var6 = $getCallSiteArray();
               this.pendingNamespaces = (Reference)pendingNamespaces;
               this.namespaceSpecificTags = (Reference)namespaceSpecificTags;
               this.namespaces = (Reference)namespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               if (ScriptBytecodeAdapter.compareEqual(keyx.get(), "")) {
                  keyx.set(":");
               }

               valuex.set(var5[0].call(valuex.get()));
               CallSite var10000;
               Object var10001;
               Object var10002;
               Object baseEntry;
               if (ScriptBytecodeAdapter.compareNotEqual(var5[1].call(this.namespaces.get(), keyx.get()), valuex.get())) {
                  var10000 = var5[2];
                  var10001 = this.pendingNamespaces.get();
                  var10002 = keyx.get();
                  baseEntry = valuex.get();
                  var10000.call(var10001, var10002, baseEntry);
               }

               if (!DefaultTypeTransformation.booleanUnbox(var5[3].call(this.namespaceSpecificTags.get(), valuex.get()))) {
                  baseEntry = var5[4].call(this.namespaceSpecificTags.get(), (Object)":");
                  var10000 = var5[5];
                  var10001 = this.namespaceSpecificTags.get();
                  var10002 = valuex.get();
                  Object var7 = var5[6].call(ScriptBytecodeAdapter.createList(new Object[]{var5[7].call(baseEntry, (Object)$const$0), var5[8].call(baseEntry, (Object)$const$1), ScriptBytecodeAdapter.createMap(new Object[0])}));
                  var10000.call(var10001, var10002, var7);
                  return var7;
               } else {
                  return null;
               }
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[9].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getPendingNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.pendingNamespaces.get();
            }

            public Object getNamespaceSpecificTags() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaceSpecificTags.get();
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2_closure5()) {
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
               var0[0] = "toString";
               var0[1] = "getAt";
               var0[2] = "putAt";
               var0[3] = "containsKey";
               var0[4] = "getAt";
               var0[5] = "putAt";
               var0[6] = "toArray";
               var0[7] = "getAt";
               var0[8] = "getAt";
               var0[9] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[10];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2_closure5(), var0);
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
            private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2_closure5() {
               Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2_closure5;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2_closure5 = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder$_closure2_closure5");
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
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrsx, Object... rest) {
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespacesx = new Reference(namespaces);
         Object namespaceSpecificTagsx = new Reference(namespaceSpecificTags);
         Object attrs = new Reference(attrsx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[1].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespacesx.get(), namespacesx.get(), namespaceSpecificTagsx.get(), prefix, attrs.get(), rest));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2()) {
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
         var0[0] = "each";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2(), var0);
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
      private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2() {
         Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure2 = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder$_closure2");
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

   class _closure3 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrsx, Object... rest) {
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespacesx = new Reference(namespaces);
         Object namespaceSpecificTagsx = new Reference(namespaceSpecificTags);
         Object attrs = new Reference(attrsx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[0].call(attrs.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), pendingNamespacesx, namespaceSpecificTagsx, namespacesx) {
            private Reference<T> pendingNamespaces;
            private Reference<T> namespaceSpecificTags;
            private Reference<T> namespaces;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)2;
            // $FF: synthetic field
            private static final Integer $const$1 = (Integer)1;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$lang$GroovyRuntimeException;
            // $FF: synthetic field
            private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6;

            public {
               CallSite[] var6 = $getCallSiteArray();
               this.pendingNamespaces = (Reference)pendingNamespaces;
               this.namespaceSpecificTags = (Reference)namespaceSpecificTags;
               this.namespaces = (Reference)namespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               if (valuex.get() instanceof Map) {
                  Object infox = new Reference((Object)null);
                  if (DefaultTypeTransformation.booleanUnbox(var5[0].call(this.namespaces.get(), keyx.get()))) {
                     infox.set(var5[1].call(this.namespaceSpecificTags.get(), var5[2].call(this.namespaces.get(), keyx.get())));
                  } else {
                     if (!DefaultTypeTransformation.booleanUnbox(var5[3].call(this.pendingNamespaces.get(), keyx.get()))) {
                        throw (Throwable)var5[6].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{keyx.get()}, new String[]{"namespace prefix ", " has not been declared"})));
                     }

                     infox.set(var5[4].call(this.namespaceSpecificTags.get(), var5[5].call(this.pendingNamespaces.get(), keyx.get())));
                  }

                  return var5[7].call(valuex.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), infox) {
                     private Reference<T> info;
                     // $FF: synthetic field
                     private static final Integer $const$0 = (Integer)2;
                     // $FF: synthetic field
                     private static final Integer $const$1 = (Integer)1;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6_closure7;

                     public {
                        CallSite[] var4 = $getCallSiteArray();
                        this.info = (Reference)info;
                     }

                     public Object doCall(Object to, Object from) {
                        Object tox = new Reference(to);
                        Object fromx = new Reference(from);
                        CallSite[] var5 = $getCallSiteArray();
                        CallSite var10000 = var5[0];
                        Object var10001 = var5[1].call(this.info.get(), (Object)$const$0);
                        Object var10002 = tox.get();
                        Object var6 = var5[2].call(var5[3].call(this.info.get(), (Object)$const$1), fromx.get());
                        var10000.call(var10001, var10002, var6);
                        return var6;
                     }

                     public Object call(Object to, Object from) {
                        Object tox = new Reference(to);
                        Object fromx = new Reference(from);
                        CallSite[] var5 = $getCallSiteArray();
                        return var5[4].callCurrent(this, tox.get(), fromx.get());
                     }

                     public Object getInfo() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.info.get();
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6_closure7()) {
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
                        var0[0] = "putAt";
                        var0[1] = "getAt";
                        var0[2] = "curry";
                        var0[3] = "getAt";
                        var0[4] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[5];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6_closure7(), var0);
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
                     private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6_closure7() {
                        Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6_closure7;
                        if (var10000 == null) {
                           var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6_closure7 = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder$_closure3_closure6_closure7");
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
               } else {
                  Object info = var5[8].call(this.namespaceSpecificTags.get(), (Object)":");
                  CallSite var10000 = var5[9];
                  Object var10001 = var5[10].call(info, (Object)$const$0);
                  Object var10002 = keyx.get();
                  Object var7 = var5[11].call(var5[12].call(info, (Object)$const$1), valuex.get());
                  var10000.call(var10001, var10002, var7);
                  return var7;
               }
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[13].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getPendingNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.pendingNamespaces.get();
            }

            public Object getNamespaceSpecificTags() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaceSpecificTags.get();
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6()) {
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
               var0[0] = "containsKey";
               var0[1] = "getAt";
               var0[2] = "getAt";
               var0[3] = "containsKey";
               var0[4] = "getAt";
               var0[5] = "getAt";
               var0[6] = "<$constructor$>";
               var0[7] = "each";
               var0[8] = "getAt";
               var0[9] = "putAt";
               var0[10] = "getAt";
               var0[11] = "curry";
               var0[12] = "getAt";
               var0[13] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[14];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6(), var0);
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
            private static Class $get$$class$groovy$lang$GroovyRuntimeException() {
               Class var10000 = $class$groovy$lang$GroovyRuntimeException;
               if (var10000 == null) {
                  var10000 = $class$groovy$lang$GroovyRuntimeException = class$("groovy.lang.GroovyRuntimeException");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6() {
               Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3_closure6 = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder$_closure3_closure6");
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
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrsx, Object... rest) {
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespacesx = new Reference(namespaces);
         Object namespaceSpecificTagsx = new Reference(namespaceSpecificTags);
         Object attrs = new Reference(attrsx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[1].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespacesx.get(), namespacesx.get(), namespaceSpecificTagsx.get(), prefix, attrs.get(), rest));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3()) {
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
         var0[0] = "each";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3(), var0);
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
      private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3() {
         Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure3 = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder$_closure3");
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

   class _closure4 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure4;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespacesx, Object... rest) {
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespaces = new Reference(namespacesx);
         CallSite[] var7 = $getCallSiteArray();
         return ScriptBytecodeAdapter.createList(new Object[]{namespaces.get(), pendingNamespacesx.get()});
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespacesx, Object... rest) {
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespaces = new Reference(namespacesx);
         CallSite[] var7 = $getCallSiteArray();
         return var7[0].callCurrent(this, doc, pendingNamespacesx.get(), namespaces.get(), rest);
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure4()) {
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
         var0[0] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[1];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure4(), var0);
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
      private static Class $get$$class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure4() {
         Class var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure4;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$streamingmarkupsupport$AbstractStreamingBuilder$_closure4 = class$("groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder$_closure4");
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
}
