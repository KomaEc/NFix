package groovy.xml;

import groovy.lang.Buildable;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.xml.streamingmarkupsupport.AbstractStreamingBuilder;
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

public class StreamingMarkupBuilder extends AbstractStreamingBuilder implements GroovyObject {
   private boolean useDoubleQuotes;
   private Object pendingStack;
   private Object commentClosure;
   private Object piClosure;
   private Object declarationClosure;
   private Object noopClosure;
   private Object unescapedClosure;
   private Object tagClosure;
   private Object builder;
   private Object encoding;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204641L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204641 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$xml$StreamingMarkupBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public StreamingMarkupBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.useDoubleQuotes = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.pendingStack = ScriptBytecodeAdapter.createList(new Object[0]);
      this.commentClosure = new StreamingMarkupBuilder._closure1(this, this);
      this.piClosure = new StreamingMarkupBuilder._closure2(this, this);
      this.declarationClosure = new StreamingMarkupBuilder._closure3(this, this);
      this.noopClosure = new StreamingMarkupBuilder._closure4(this, this);
      this.unescapedClosure = new StreamingMarkupBuilder._closure5(this, this);
      this.tagClosure = new StreamingMarkupBuilder._closure6(this, this);
      this.builder = null;
      this.encoding = null;
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      var1[0].call(var1[1].callGroovyObjectGetProperty(this), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"yield", this.noopClosure, "yieldUnescaped", this.unescapedClosure, "xmlDeclaration", this.declarationClosure, "comment", this.commentClosure, "pi", this.piClosure}));
      Object nsSpecificTags = ScriptBytecodeAdapter.createMap(new Object[]{":", ScriptBytecodeAdapter.createList(new Object[]{this.tagClosure, this.tagClosure, ScriptBytecodeAdapter.createMap(new Object[0])}), "http://www.w3.org/XML/1998/namespace", ScriptBytecodeAdapter.createList(new Object[]{this.tagClosure, this.tagClosure, ScriptBytecodeAdapter.createMap(new Object[0])}), "http://www.codehaus.org/Groovy/markup/keywords", ScriptBytecodeAdapter.createList(new Object[]{var1[2].callGroovyObjectGetProperty(this), this.tagClosure, var1[3].callGroovyObjectGetProperty(this)})});
      this.builder = var1[4].callConstructor($get$$class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder(), (Object)nsSpecificTags);
   }

   public Object getQt() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.useDoubleQuotes)) ? "\"" : "'";
   }

   public Object bind(Object closure) {
      CallSite[] var2 = $getCallSiteArray();
      Object boundClosure = new Reference(var2[5].call(this.builder, closure));
      Object enc = new Reference(this.encoding);
      return var2[6].call(new GeneratedClosure(this, this, boundClosure, enc) {
         private Reference<T> boundClosure;
         private Reference<T> enc;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$xml$StreamingMarkupBuilder$_bind_closure7;
         // $FF: synthetic field
         private static Class $class$groovy$xml$streamingmarkupsupport$StreamingMarkupWriter;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.boundClosure = (Reference)boundClosure;
            this.enc = (Reference)enc;
         }

         public Object doCall(Object out) {
            Object outx = new Reference(out);
            CallSite[] var3 = $getCallSiteArray();
            outx.set(var3[0].callConstructor($get$$class$groovy$xml$streamingmarkupsupport$StreamingMarkupWriter(), outx.get(), this.enc.get()));
            ScriptBytecodeAdapter.setProperty(outx.get(), $get$$class$groovy$xml$StreamingMarkupBuilder$_bind_closure7(), this.boundClosure.get(), "trigger");
            return var3[1].call(outx.get());
         }

         public Object getBoundClosure() {
            CallSite[] var1 = $getCallSiteArray();
            return this.boundClosure.get();
         }

         public Object getEnc() {
            CallSite[] var1 = $getCallSiteArray();
            return this.enc.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_bind_closure7()) {
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
            var0[1] = "flush";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_bind_closure7(), var0);
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
         private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_bind_closure7() {
            Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_bind_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$xml$StreamingMarkupBuilder$_bind_closure7 = class$("groovy.xml.StreamingMarkupBuilder$_bind_closure7");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$xml$streamingmarkupsupport$StreamingMarkupWriter() {
            Class var10000 = $class$groovy$xml$streamingmarkupsupport$StreamingMarkupWriter;
            if (var10000 == null) {
               var10000 = $class$groovy$xml$streamingmarkupsupport$StreamingMarkupWriter = class$("groovy.xml.streamingmarkupsupport.StreamingMarkupWriter");
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
      });
   }

   public Object bindNode(Object node) {
      Object node = new Reference(node);
      CallSite[] var3 = $getCallSiteArray();
      return var3[7].callCurrent(this, (Object)(new GeneratedClosure(this, this, node) {
         private Reference<T> node;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$xml$StreamingMarkupBuilder$_bindNode_closure8;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.node = (Reference)node;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].call(var2[1].callGroovyObjectGetProperty(this), this.node.get());
         }

         public Object getNode() {
            CallSite[] var1 = $getCallSiteArray();
            return this.node.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_bindNode_closure8()) {
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
            var0[0] = "leftShift";
            var0[1] = "out";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_bindNode_closure8(), var0);
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
         private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_bindNode_closure8() {
            Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_bindNode_closure8;
            if (var10000 == null) {
               var10000 = $class$groovy$xml$StreamingMarkupBuilder$_bindNode_closure8 = class$("groovy.xml.StreamingMarkupBuilder$_bindNode_closure8");
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

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder()) {
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$xml$StreamingMarkupBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$xml$StreamingMarkupBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$xml$StreamingMarkupBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public boolean getUseDoubleQuotes() {
      return this.useDoubleQuotes;
   }

   public boolean isUseDoubleQuotes() {
      return this.useDoubleQuotes;
   }

   public void setUseDoubleQuotes(boolean var1) {
      this.useDoubleQuotes = var1;
   }

   public Object getPendingStack() {
      return this.pendingStack;
   }

   public void setPendingStack(Object var1) {
      this.pendingStack = var1;
   }

   public Object getCommentClosure() {
      return this.commentClosure;
   }

   public void setCommentClosure(Object var1) {
      this.commentClosure = var1;
   }

   public Object getPiClosure() {
      return this.piClosure;
   }

   public void setPiClosure(Object var1) {
      this.piClosure = var1;
   }

   public Object getDeclarationClosure() {
      return this.declarationClosure;
   }

   public void setDeclarationClosure(Object var1) {
      this.declarationClosure = var1;
   }

   public Object getNoopClosure() {
      return this.noopClosure;
   }

   public void setNoopClosure(Object var1) {
      this.noopClosure = var1;
   }

   public Object getUnescapedClosure() {
      return this.unescapedClosure;
   }

   public void setUnescapedClosure(Object var1) {
      this.unescapedClosure = var1;
   }

   public Object getTagClosure() {
      return this.tagClosure;
   }

   public void setTagClosure(Object var1) {
      this.tagClosure = var1;
   }

   public Object getBuilder() {
      return this.builder;
   }

   public void setBuilder(Object var1) {
      this.builder = var1;
   }

   public Object getEncoding() {
      return this.encoding;
   }

   public void setEncoding(Object var1) {
      this.encoding = var1;
   }

   // $FF: synthetic method
   public void super$2$setAliasSetupClosure(Object var1) {
      super.setAliasSetupClosure(var1);
   }

   // $FF: synthetic method
   public Object super$2$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public Object super$2$getNamespaceSetupClosure() {
      return super.getNamespaceSetupClosure();
   }

   // $FF: synthetic method
   public void super$2$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setBuilder(Object var1) {
      super.setBuilder(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public Object super$2$getBadTagClosure() {
      return super.getBadTagClosure();
   }

   // $FF: synthetic method
   public void super$2$setGetNamespaceClosure(Object var1) {
      super.setGetNamespaceClosure(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$2$setNamespaceSetupClosure(Object var1) {
      super.setNamespaceSetupClosure(var1);
   }

   // $FF: synthetic method
   public Object super$2$this$dist$get$2(String var1) {
      return super.this$dist$get$2(var1);
   }

   // $FF: synthetic method
   public void super$2$setBadTagClosure(Object var1) {
      super.setBadTagClosure(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Object super$2$getAliasSetupClosure() {
      return super.getAliasSetupClosure();
   }

   // $FF: synthetic method
   public void super$2$this$dist$set$2(String var1, Object var2) {
      super.this$dist$set$2(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$2$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   public Object super$2$getSpecialTags() {
      return super.getSpecialTags();
   }

   // $FF: synthetic method
   public void super$2$setSpecialTags(Object var1) {
      super.setSpecialTags(var1);
   }

   // $FF: synthetic method
   public Object super$2$getBuilder() {
      return super.getBuilder();
   }

   // $FF: synthetic method
   public Object super$2$getGetNamespaceClosure() {
      return super.getGetNamespaceClosure();
   }

   // $FF: synthetic method
   public Object super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   public Object super$2$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "putAll";
      var0[1] = "specialTags";
      var0[2] = "badTagClosure";
      var0[3] = "specialTags";
      var0[4] = "<$constructor$>";
      var0[5] = "bind";
      var0[6] = "asWritable";
      var0[7] = "bind";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[8];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder(), var0);
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
   private static Class $get$$class$groovy$xml$StreamingMarkupBuilder() {
      Class var10000 = $class$groovy$xml$StreamingMarkupBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$StreamingMarkupBuilder = class$("groovy.xml.StreamingMarkupBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder() {
      Class var10000 = $class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder = class$("groovy.xml.streamingmarkupsupport.BaseMarkupBuilder");
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
      private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object bodyx = new Reference(body);
         Object outx = new Reference(out);
         CallSite[] var11 = $getCallSiteArray();
         var11[0].call(var11[1].call(outx.get()), (Object)"<!--");
         var11[2].call(var11[3].call(outx.get()), bodyx.get());
         return var11[4].call(var11[5].call(outx.get()), (Object)"-->");
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object bodyx = new Reference(body);
         Object outx = new Reference(out);
         CallSite[] var11 = $getCallSiteArray();
         return var11[6].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, bodyx.get(), outx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure1()) {
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
         var0[0] = "leftShift";
         var0[1] = "unescaped";
         var0[2] = "leftShift";
         var0[3] = "escaped";
         var0[4] = "leftShift";
         var0[5] = "unescaped";
         var0[6] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[7];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure1(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure1() {
         Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure1 = class$("groovy.xml.StreamingMarkupBuilder$_closure1");
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
      private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure2;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object attrsx = new Reference(attrs);
         Object outx = new Reference(out);
         CallSite[] var11 = $getCallSiteArray();
         return var11[0].call(attrsx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), outx) {
            private Reference<T> out;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.out = (Reference)out;
            }

            public Object doCall(Object target, Object instruction) {
               Object targetx = new Reference(target);
               Object instructionx = new Reference(instruction);
               CallSite[] var5 = $getCallSiteArray();
               var5[0].call(var5[1].call(this.out.get()), (Object)"<?");
               if (instructionx.get() instanceof Map) {
                  var5[2].call(var5[3].call(this.out.get()), targetx.get());
                  CallSite var10000 = var5[4];
                  Object var10001 = instructionx.get();
                  Object var10005 = this.getThisObject();
                  Reference out = this.out;
                  var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, out) {
                     private Reference<T> out;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9_closure10;

                     public {
                        Reference outx = new Reference(out);
                        CallSite[] var5 = $getCallSiteArray();
                        this.out = (Reference)((Reference)outx.get());
                     }

                     public Object doCall(Object name, Object value) {
                        Object namex = new Reference(name);
                        Object valuex = new Reference(value);
                        CallSite[] var5 = $getCallSiteArray();
                        return DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var5[0].call(var5[1].call(valuex.get()), (Object)"'")) && !DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var5[2].callGroovyObjectGetProperty(this)) && !DefaultTypeTransformation.booleanUnbox(var5[3].call(var5[4].call(valuex.get()), (Object)"\"")) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.FALSE : Boolean.TRUE) ? var5[5].call(var5[6].call(this.out.get()), (Object)(new GStringImpl(new Object[]{namex.get(), valuex.get()}, new String[]{" ", "=\"", "\""}))) : var5[7].call(var5[8].call(this.out.get()), (Object)(new GStringImpl(new Object[]{namex.get(), valuex.get()}, new String[]{" ", "='", "'"})));
                     }

                     public Object call(Object name, Object value) {
                        Object namex = new Reference(name);
                        Object valuex = new Reference(value);
                        CallSite[] var5 = $getCallSiteArray();
                        return var5[9].callCurrent(this, namex.get(), valuex.get());
                     }

                     public Object getOut() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.out.get();
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9_closure10()) {
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
                        var0[0] = "contains";
                        var0[1] = "toString";
                        var0[2] = "useDoubleQuotes";
                        var0[3] = "contains";
                        var0[4] = "toString";
                        var0[5] = "leftShift";
                        var0[6] = "unescaped";
                        var0[7] = "leftShift";
                        var0[8] = "unescaped";
                        var0[9] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[10];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9_closure10(), var0);
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
                     private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9_closure10() {
                        Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9_closure10;
                        if (var10000 == null) {
                           var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9_closure10 = class$("groovy.xml.StreamingMarkupBuilder$_closure2_closure9_closure10");
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
                  var5[5].call(var5[6].call(this.out.get()), (Object)(new GStringImpl(new Object[]{targetx.get(), instructionx.get()}, new String[]{"", " ", ""})));
               }

               return var5[7].call(var5[8].call(this.out.get()), (Object)"?>");
            }

            public Object call(Object target, Object instruction) {
               Object targetx = new Reference(target);
               Object instructionx = new Reference(instruction);
               CallSite[] var5 = $getCallSiteArray();
               return var5[9].callCurrent(this, targetx.get(), instructionx.get());
            }

            public Object getOut() {
               CallSite[] var1 = $getCallSiteArray();
               return this.out.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9()) {
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
               var0[0] = "leftShift";
               var0[1] = "unescaped";
               var0[2] = "leftShift";
               var0[3] = "unescaped";
               var0[4] = "each";
               var0[5] = "leftShift";
               var0[6] = "unescaped";
               var0[7] = "leftShift";
               var0[8] = "unescaped";
               var0[9] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[10];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9() {
               Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure2_closure9 = class$("groovy.xml.StreamingMarkupBuilder$_closure2_closure9");
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

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object attrsx = new Reference(attrs);
         Object outx = new Reference(out);
         CallSite[] var11 = $getCallSiteArray();
         return var11[1].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrsx.get(), body, outx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure2()) {
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
         return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure2(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure2() {
         Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure2;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure2 = class$("groovy.xml.StreamingMarkupBuilder$_closure2");
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
      private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure3;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object outx = new Reference(out);
         CallSite[] var10 = $getCallSiteArray();
         var10[0].call(var10[1].call(outx.get()), var10[2].call(var10[3].call(var10[4].call("<?xml version=", (Object)var10[5].callGroovyObjectGetProperty(this)), (Object)"1.0"), var10[6].callGroovyObjectGetProperty(this)));
         if (DefaultTypeTransformation.booleanUnbox(var10[7].callGetProperty(outx.get()))) {
            var10[8].call(var10[9].call(outx.get()), var10[10].call(var10[11].call(var10[12].call(" encoding=", (Object)var10[13].callGroovyObjectGetProperty(this)), var10[14].callGetProperty(outx.get())), var10[15].callGroovyObjectGetProperty(this)));
         }

         return var10[16].call(var10[17].call(outx.get()), (Object)"?>\n");
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object outx = new Reference(out);
         CallSite[] var10 = $getCallSiteArray();
         return var10[18].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, body, outx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure3()) {
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
         var0[0] = "leftShift";
         var0[1] = "unescaped";
         var0[2] = "plus";
         var0[3] = "plus";
         var0[4] = "plus";
         var0[5] = "qt";
         var0[6] = "qt";
         var0[7] = "encodingKnown";
         var0[8] = "leftShift";
         var0[9] = "escaped";
         var0[10] = "plus";
         var0[11] = "plus";
         var0[12] = "plus";
         var0[13] = "qt";
         var0[14] = "encoding";
         var0[15] = "qt";
         var0[16] = "leftShift";
         var0[17] = "unescaped";
         var0[18] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[19];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure3(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure3() {
         Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure3;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure3 = class$("groovy.xml.StreamingMarkupBuilder$_closure3");
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
      private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure4;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object outx) {
         Object docx = new Reference(doc);
         Object bodyx = new Reference(body);
         Object out = new Reference(outx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[0].call(bodyx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), docx, out) {
            private Reference<T> doc;
            private Reference<T> out;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure4_closure11;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.doc = (Reference)doc;
               this.out = (Reference)out;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               if (itx.get() instanceof Closure) {
                  Object body1 = var3[0].call(itx.get());
                  ScriptBytecodeAdapter.setProperty(this.doc.get(), $get$$class$groovy$xml$StreamingMarkupBuilder$_closure4_closure11(), body1, "delegate");
                  return var3[1].call(body1, this.doc.get());
               } else {
                  return itx.get() instanceof Buildable ? var3[2].call(itx.get(), this.doc.get()) : var3[3].call(var3[4].call(this.out.get()), itx.get());
               }
            }

            public Object getDoc() {
               CallSite[] var1 = $getCallSiteArray();
               return this.doc.get();
            }

            public Object getOut() {
               CallSite[] var1 = $getCallSiteArray();
               return this.out.get();
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure4_closure11()) {
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
               var0[0] = "clone";
               var0[1] = "call";
               var0[2] = "build";
               var0[3] = "leftShift";
               var0[4] = "escaped";
               var0[5] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[6];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure4_closure11(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure4_closure11() {
               Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure4_closure11;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure4_closure11 = class$("groovy.xml.StreamingMarkupBuilder$_closure4_closure11");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object outx) {
         Object docx = new Reference(doc);
         Object bodyx = new Reference(body);
         Object out = new Reference(outx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[1].callCurrent(this, (Object[])ArrayUtil.createArray(docx.get(), pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, bodyx.get(), out.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure4()) {
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
         return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure4(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure4() {
         Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure4;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure4 = class$("groovy.xml.StreamingMarkupBuilder$_closure4");
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

   class _closure5 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure5;

      public _closure5(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object bodyx = new Reference(body);
         Object outx = new Reference(out);
         CallSite[] var11 = $getCallSiteArray();
         return var11[0].call(var11[1].call(outx.get()), bodyx.get());
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object out) {
         Object bodyx = new Reference(body);
         Object outx = new Reference(out);
         CallSite[] var11 = $getCallSiteArray();
         return var11[2].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, bodyx.get(), outx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure5()) {
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
         var0[0] = "leftShift";
         var0[1] = "unescaped";
         var0[2] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[3];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure5(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure5() {
         Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure5;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure5 = class$("groovy.xml.StreamingMarkupBuilder$_closure5");
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

   class _closure6 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure6;
      // $FF: synthetic field
      private static Class $class$groovy$lang$GroovyRuntimeException;

      public _closure6(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object tag, Object docx, Object pendingNamespacesx, Object namespacesx, Object namespaceSpecificTags, Object prefixx, Object attrsx, Object bodyx, Object outx) {
         Object tagx = new Reference(tag);
         Object doc = new Reference(docx);
         Object pendingNamespaces = new Reference(pendingNamespacesx);
         Object namespaces = new Reference(namespacesx);
         Object prefix = new Reference(prefixx);
         Object attrs = new Reference(attrsx);
         Object body = new Reference(bodyx);
         Object out = new Reference(outx);
         CallSite[] var18 = $getCallSiteArray();
         if (ScriptBytecodeAdapter.compareNotEqual(prefix.get(), "")) {
            if (!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var18[0].call(namespaces.get(), prefix.get())) && !DefaultTypeTransformation.booleanUnbox(var18[1].call(pendingNamespaces.get(), prefix.get())) ? Boolean.FALSE : Boolean.TRUE)) {
               throw (Throwable)var18[2].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{prefix.get()}, new String[]{"Namespace prefix: ", " is not bound to a URI"})));
            }

            if (ScriptBytecodeAdapter.compareNotEqual(prefix.get(), ":")) {
               tagx.set(var18[3].call(var18[4].call(prefix.get(), (Object)":"), tagx.get()));
            }
         }

         out.set(var18[5].call(var18[6].call(out.get()), (Object)(new GStringImpl(new Object[]{tagx.get()}, new String[]{"<", ""}))));
         var18[7].call(attrs.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), pendingNamespaces, doc, namespaces, out) {
            private Reference<T> pendingNamespaces;
            private Reference<T> doc;
            private Reference<T> namespaces;
            private Reference<T> out;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static final Integer $const$1 = (Integer)1;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12;
            // $FF: synthetic field
            private static Class $class$groovy$lang$GroovyRuntimeException;

            public {
               CallSite[] var7 = $getCallSiteArray();
               this.pendingNamespaces = (Reference)pendingNamespaces;
               this.doc = (Reference)doc;
               this.namespaces = (Reference)namespaces;
               this.out = (Reference)out;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               if (DefaultTypeTransformation.booleanUnbox(var5[0].call(keyx.get(), (Object)"$"))) {
                  Object parts = new Reference(var5[1].call(keyx.get(), (Object)"$"));
                  if (!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var5[2].call(this.namespaces.get(), var5[3].call(parts.get(), (Object)$const$0))) && !DefaultTypeTransformation.booleanUnbox(var5[4].call(this.pendingNamespaces.get(), var5[5].call(parts.get(), (Object)$const$0))) ? Boolean.FALSE : Boolean.TRUE)) {
                     throw (Throwable)var5[10].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{var5[11].call(parts.get(), (Object)$const$0), keyx.get()}, new String[]{"bad attribute namespace tag: ", " in ", ""})));
                  }

                  keyx.set(var5[6].call(var5[7].call(var5[8].call(parts.get(), (Object)$const$0), (Object)":"), var5[9].call(parts.get(), (Object)$const$1)));
               }

               var5[12].call(this.out.get(), var5[13].call(new GStringImpl(new Object[]{keyx.get()}, new String[]{" ", "="}), (Object)var5[14].callGroovyObjectGetProperty(this)));
               ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12(), this.out.get(), "writingAttribute");
               var5[15].call(new GStringImpl(new Object[]{valuex.get()}, new String[]{"", ""}), (Object)this.doc.get());
               ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12(), this.out.get(), "writingAttribute");
               return var5[16].call(this.out.get(), var5[17].callGroovyObjectGetProperty(this));
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[18].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getPendingNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.pendingNamespaces.get();
            }

            public Object getDoc() {
               CallSite[] var1 = $getCallSiteArray();
               return this.doc.get();
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            public Object getOut() {
               CallSite[] var1 = $getCallSiteArray();
               return this.out.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12()) {
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
               var0[0] = "contains";
               var0[1] = "tokenize";
               var0[2] = "containsKey";
               var0[3] = "getAt";
               var0[4] = "containsKey";
               var0[5] = "getAt";
               var0[6] = "plus";
               var0[7] = "plus";
               var0[8] = "getAt";
               var0[9] = "getAt";
               var0[10] = "<$constructor$>";
               var0[11] = "getAt";
               var0[12] = "leftShift";
               var0[13] = "plus";
               var0[14] = "qt";
               var0[15] = "build";
               var0[16] = "leftShift";
               var0[17] = "qt";
               var0[18] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[19];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12() {
               Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure12 = class$("groovy.xml.StreamingMarkupBuilder$_closure6_closure12");
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
         }));
         Object hiddenNamespaces = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
         var18[8].call(pendingNamespaces.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), doc, namespaces, hiddenNamespaces, out) {
            private Reference<T> doc;
            private Reference<T> namespaces;
            private Reference<T> hiddenNamespaces;
            private Reference<T> out;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13;

            public {
               CallSite[] var7 = $getCallSiteArray();
               this.doc = (Reference)doc;
               this.namespaces = (Reference)namespaces;
               this.hiddenNamespaces = (Reference)hiddenNamespaces;
               this.out = (Reference)out;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               CallSite var10000 = var5[0];
               Object var10001 = this.hiddenNamespaces.get();
               Object var10002 = keyx.get();
               Object var6 = var5[1].call(this.namespaces.get(), keyx.get());
               var10000.call(var10001, var10002, var6);
               var10000 = var5[2];
               var10001 = this.namespaces.get();
               var10002 = keyx.get();
               var6 = valuex.get();
               var10000.call(var10001, var10002, var6);
               var5[3].call(this.out.get(), ScriptBytecodeAdapter.compareEqual(keyx.get(), ":") ? var5[4].call(" xmlns=", (Object)var5[5].callGroovyObjectGetProperty(this)) : var5[6].call(new GStringImpl(new Object[]{keyx.get()}, new String[]{" xmlns:", "="}), (Object)var5[7].callGroovyObjectGetProperty(this)));
               ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13(), this.out.get(), "writingAttribute");
               var5[8].call(new GStringImpl(new Object[]{valuex.get()}, new String[]{"", ""}), (Object)this.doc.get());
               ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13(), this.out.get(), "writingAttribute");
               return var5[9].call(this.out.get(), var5[10].callGroovyObjectGetProperty(this));
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[11].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getDoc() {
               CallSite[] var1 = $getCallSiteArray();
               return this.doc.get();
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            public Object getHiddenNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.hiddenNamespaces.get();
            }

            public Object getOut() {
               CallSite[] var1 = $getCallSiteArray();
               return this.out.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13()) {
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
               var0[2] = "putAt";
               var0[3] = "leftShift";
               var0[4] = "plus";
               var0[5] = "qt";
               var0[6] = "plus";
               var0[7] = "qt";
               var0[8] = "build";
               var0[9] = "leftShift";
               var0[10] = "qt";
               var0[11] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[12];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13() {
               Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure13 = class$("groovy.xml.StreamingMarkupBuilder$_closure6_closure13");
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
         if (ScriptBytecodeAdapter.compareEqual(body.get(), (Object)null)) {
            var18[9].call(out.get(), (Object)"/>");
         } else {
            var18[10].call(out.get(), (Object)">");
            var18[11].call(var18[12].callGroovyObjectGetProperty(this), var18[13].call(pendingNamespaces.get()));
            var18[14].call(pendingNamespaces.get());
            var18[15].call(body.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), doc, out) {
               private Reference<T> doc;
               private Reference<T> out;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure14;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.doc = (Reference)doc;
                  this.out = (Reference)out;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  if (itx.get() instanceof Closure) {
                     Object body1 = var3[0].call(itx.get());
                     ScriptBytecodeAdapter.setProperty(this.doc.get(), $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure14(), body1, "delegate");
                     return var3[1].call(body1, this.doc.get());
                  } else {
                     return itx.get() instanceof Buildable ? var3[2].call(itx.get(), this.doc.get()) : var3[3].call(var3[4].call(this.out.get()), itx.get());
                  }
               }

               public Object getDoc() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.doc.get();
               }

               public Object getOut() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.out.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure14()) {
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
                  var0[0] = "clone";
                  var0[1] = "call";
                  var0[2] = "build";
                  var0[3] = "leftShift";
                  var0[4] = "escaped";
                  var0[5] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[6];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure14(), var0);
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
               private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure14() {
                  Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure14;
                  if (var10000 == null) {
                     var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure14 = class$("groovy.xml.StreamingMarkupBuilder$_closure6_closure14");
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
            var18[16].call(pendingNamespaces.get());
            var18[17].call(pendingNamespaces.get(), var18[18].call(var18[19].callGroovyObjectGetProperty(this)));
            var18[20].call(out.get(), (Object)(new GStringImpl(new Object[]{tagx.get()}, new String[]{"</", ">"})));
         }

         return var18[21].call(hiddenNamespaces.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), namespaces) {
            private Reference<T> namespaces;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure15;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.namespaces = (Reference)namespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               if (ScriptBytecodeAdapter.compareEqual(valuex.get(), (Object)null)) {
                  return var5[0].call(this.namespaces.get(), keyx.get());
               } else {
                  CallSite var10000 = var5[1];
                  Object var10001 = this.namespaces.get();
                  Object var10002 = keyx.get();
                  Object var6 = valuex.get();
                  var10000.call(var10001, var10002, var6);
                  return var6;
               }
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[2].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure15()) {
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
               var0[0] = "remove";
               var0[1] = "putAt";
               var0[2] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure15(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6_closure15() {
               Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure15;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6_closure15 = class$("groovy.xml.StreamingMarkupBuilder$_closure6_closure15");
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

      public Object call(Object tag, Object docx, Object pendingNamespacesx, Object namespacesx, Object namespaceSpecificTags, Object prefixx, Object attrsx, Object bodyx, Object outx) {
         Object tagx = new Reference(tag);
         Object doc = new Reference(docx);
         Object pendingNamespaces = new Reference(pendingNamespacesx);
         Object namespaces = new Reference(namespacesx);
         Object prefix = new Reference(prefixx);
         Object attrs = new Reference(attrsx);
         Object body = new Reference(bodyx);
         Object out = new Reference(outx);
         CallSite[] var18 = $getCallSiteArray();
         return var18[22].callCurrent(this, (Object[])ArrayUtil.createArray(tagx.get(), doc.get(), pendingNamespaces.get(), namespaces.get(), namespaceSpecificTags, prefix.get(), attrs.get(), body.get(), out.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6()) {
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
         var0[1] = "containsKey";
         var0[2] = "<$constructor$>";
         var0[3] = "plus";
         var0[4] = "plus";
         var0[5] = "leftShift";
         var0[6] = "unescaped";
         var0[7] = "each";
         var0[8] = "each";
         var0[9] = "leftShift";
         var0[10] = "leftShift";
         var0[11] = "add";
         var0[12] = "pendingStack";
         var0[13] = "clone";
         var0[14] = "clear";
         var0[15] = "each";
         var0[16] = "clear";
         var0[17] = "putAll";
         var0[18] = "pop";
         var0[19] = "pendingStack";
         var0[20] = "leftShift";
         var0[21] = "each";
         var0[22] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[23];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingMarkupBuilder$_closure6(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingMarkupBuilder$_closure6() {
         Class var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingMarkupBuilder$_closure6 = class$("groovy.xml.StreamingMarkupBuilder$_closure6");
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
}
