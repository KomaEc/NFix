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
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

public class StreamingSAXBuilder extends AbstractStreamingBuilder implements GroovyObject {
   private Object pendingStack;
   private Object commentClosure;
   private Object piClosure;
   private Object noopClosure;
   private Object tagClosure;
   private Object builder;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204668L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204668 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$xml$StreamingSAXBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyRuntimeException;

   public StreamingSAXBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.pendingStack = ScriptBytecodeAdapter.createList(new Object[0]);
      this.commentClosure = new StreamingSAXBuilder._closure1(this, this);
      this.piClosure = new StreamingSAXBuilder._closure2(this, this);
      this.noopClosure = new StreamingSAXBuilder._closure3(this, this);
      this.tagClosure = new StreamingSAXBuilder._closure4(this, this);
      this.builder = null;
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      var1[0].call(var1[1].callGroovyObjectGetProperty(this), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"yield", this.noopClosure, "yieldUnescaped", this.noopClosure, "comment", this.commentClosure, "pi", this.piClosure}));
      Object nsSpecificTags = ScriptBytecodeAdapter.createMap(new Object[]{":", ScriptBytecodeAdapter.createList(new Object[]{this.tagClosure, this.tagClosure, ScriptBytecodeAdapter.createMap(new Object[0])}), "http://www.w3.org/XML/1998/namespace", ScriptBytecodeAdapter.createList(new Object[]{this.tagClosure, this.tagClosure, ScriptBytecodeAdapter.createMap(new Object[0])}), "http://www.codehaus.org/Groovy/markup/keywords", ScriptBytecodeAdapter.createList(new Object[]{var1[2].callGroovyObjectGetProperty(this), this.tagClosure, var1[3].callGroovyObjectGetProperty(this)})});
      this.builder = var1[4].callConstructor($get$$class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder(), (Object)nsSpecificTags);
   }

   private Object addAttributes(AttributesImpl attributes, Object key, Object value, Object namespaces) {
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var5[5].call(key, (Object)"$"))) {
         Object parts = var5[6].call(key, (Object)"$");
         if (DefaultTypeTransformation.booleanUnbox(var5[7].call(namespaces, var5[8].call(parts, (Object)$const$0)))) {
            Object namespaceUri = var5[9].call(namespaces, var5[10].call(parts, (Object)$const$0));
            return var5[11].call(attributes, (Object[])ArrayUtil.createArray(namespaceUri, var5[12].call(parts, (Object)$const$1), new GStringImpl(new Object[]{var5[13].call(parts, (Object)$const$0), var5[14].call(parts, (Object)$const$1)}, new String[]{"", ":", ""}), "CDATA", new GStringImpl(new Object[]{value}, new String[]{"", ""})));
         } else {
            throw (Throwable)var5[15].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{key}, new String[]{"bad attribute namespace tag in ", ""})));
         }
      } else {
         return var5[16].call(attributes, (Object[])ArrayUtil.createArray("", key, key, "CDATA", new GStringImpl(new Object[]{value}, new String[]{"", ""})));
      }
   }

   private Object processBody(Object body, Object doc, Object contentHandler) {
      Object doc = new Reference(doc);
      Object contentHandler = new Reference(contentHandler);
      CallSite[] var6 = $getCallSiteArray();
      if (body instanceof Closure) {
         Object body1 = var6[17].call(body);
         ScriptBytecodeAdapter.setProperty(doc.get(), $get$$class$groovy$xml$StreamingSAXBuilder(), body1, "delegate");
         return var6[18].call(body1, doc.get());
      } else {
         return body instanceof Buildable ? var6[19].call(body, doc.get()) : var6[20].call(body, (Object)(new GeneratedClosure(this, this, contentHandler, doc) {
            private Reference<T> contentHandler;
            private Reference<T> doc;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingSAXBuilder$_processBody_closure5;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.contentHandler = (Reference)contentHandler;
               this.doc = (Reference)doc;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].callCurrent(this, itx.get(), this.doc.get(), this.contentHandler.get());
            }

            public Object getContentHandler() {
               CallSite[] var1 = $getCallSiteArray();
               return this.contentHandler.get();
            }

            public Object getDoc() {
               CallSite[] var1 = $getCallSiteArray();
               return this.doc.get();
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_processBody_closure5()) {
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
               var0[0] = "processBodyPart";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_processBody_closure5(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_processBody_closure5() {
               Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_processBody_closure5;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingSAXBuilder$_processBody_closure5 = class$("groovy.xml.StreamingSAXBuilder$_processBody_closure5");
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
   }

   private Object processBodyPart(Object part, Object doc, Object contentHandler) {
      CallSite[] var4 = $getCallSiteArray();
      Object chars;
      if (part instanceof Closure) {
         chars = var4[21].call(part);
         ScriptBytecodeAdapter.setProperty(doc, $get$$class$groovy$xml$StreamingSAXBuilder(), chars, "delegate");
         return var4[22].call(chars, doc);
      } else if (part instanceof Buildable) {
         return var4[23].call(part, doc);
      } else {
         chars = var4[24].call(part);
         return var4[25].call(contentHandler, chars, $const$0, var4[26].call(chars));
      }
   }

   public Object bind(Object closure) {
      CallSite[] var2 = $getCallSiteArray();
      Object boundClosure = new Reference(var2[27].call(this.builder, closure));
      return new GeneratedClosure(this, this, boundClosure) {
         private Reference<T> boundClosure;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$xml$StreamingSAXBuilder$_bind_closure6;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.boundClosure = (Reference)boundClosure;
         }

         public Object doCall(Object contentHandler) {
            Object contentHandlerx = new Reference(contentHandler);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].call(contentHandlerx.get());
            ScriptBytecodeAdapter.setProperty(contentHandlerx.get(), $get$$class$groovy$xml$StreamingSAXBuilder$_bind_closure6(), this.boundClosure.get(), "trigger");
            return var3[1].call(contentHandlerx.get());
         }

         public Object getBoundClosure() {
            CallSite[] var1 = $getCallSiteArray();
            return this.boundClosure.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_bind_closure6()) {
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
            var0[0] = "startDocument";
            var0[1] = "endDocument";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_bind_closure6(), var0);
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
         private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_bind_closure6() {
            Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_bind_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$xml$StreamingSAXBuilder$_bind_closure6 = class$("groovy.xml.StreamingSAXBuilder$_bind_closure6");
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
      };
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder()) {
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
      Class var10000 = $get$$class$groovy$xml$StreamingSAXBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$xml$StreamingSAXBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$xml$StreamingSAXBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public Object getNoopClosure() {
      return this.noopClosure;
   }

   public void setNoopClosure(Object var1) {
      this.noopClosure = var1;
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

   // $FF: synthetic method
   public Object this$3$addAttributes(AttributesImpl var1, Object var2, Object var3, Object var4) {
      return this.addAttributes(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public Object this$3$processBody(Object var1, Object var2, Object var3) {
      return this.processBody(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object this$3$processBodyPart(Object var1, Object var2, Object var3) {
      return this.processBodyPart(var1, var2, var3);
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
      var0[5] = "contains";
      var0[6] = "tokenize";
      var0[7] = "containsKey";
      var0[8] = "getAt";
      var0[9] = "getAt";
      var0[10] = "getAt";
      var0[11] = "addAttribute";
      var0[12] = "getAt";
      var0[13] = "getAt";
      var0[14] = "getAt";
      var0[15] = "<$constructor$>";
      var0[16] = "addAttribute";
      var0[17] = "clone";
      var0[18] = "call";
      var0[19] = "build";
      var0[20] = "each";
      var0[21] = "clone";
      var0[22] = "call";
      var0[23] = "build";
      var0[24] = "toCharArray";
      var0[25] = "characters";
      var0[26] = "size";
      var0[27] = "bind";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[28];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder(), var0);
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
   private static Class $get$$class$groovy$xml$StreamingSAXBuilder() {
      Class var10000 = $class$groovy$xml$StreamingSAXBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$StreamingSAXBuilder = class$("groovy.xml.StreamingSAXBuilder");
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

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static final Integer $const$0 = (Integer)0;
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$xml$StreamingSAXBuilder$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object contentHandler) {
         Object bodyx = new Reference(body);
         Object contentHandlerx = new Reference(contentHandler);
         CallSite[] var11 = $getCallSiteArray();
         return contentHandlerx.get() instanceof LexicalHandler ? var11[0].call(contentHandlerx.get(), var11[1].call(bodyx.get()), $const$0, var11[2].call(bodyx.get())) : null;
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object contentHandler) {
         Object bodyx = new Reference(body);
         Object contentHandlerx = new Reference(contentHandler);
         CallSite[] var11 = $getCallSiteArray();
         return var11[3].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, bodyx.get(), contentHandlerx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure1()) {
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
         var0[0] = "comment";
         var0[1] = "toCharArray";
         var0[2] = "size";
         var0[3] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[4];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure1(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure1() {
         Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure1 = class$("groovy.xml.StreamingSAXBuilder$_closure1");
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
      private static Class $class$groovy$xml$StreamingSAXBuilder$_closure2;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object contentHandler) {
         Object attrsx = new Reference(attrs);
         Object contentHandlerx = new Reference(contentHandler);
         CallSite[] var11 = $getCallSiteArray();
         return var11[0].call(attrsx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), contentHandlerx) {
            private Reference<T> contentHandler;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingSAXBuilder$_closure2_closure7;
            // $FF: synthetic field
            private static Class $class$java$lang$StringBuffer;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.contentHandler = (Reference)contentHandler;
            }

            public Object doCall(Object target, Object instruction) {
               Object targetx = new Reference(target);
               Object instructionx = new Reference(instruction);
               CallSite[] var5 = $getCallSiteArray();
               if (instructionx.get() instanceof Map) {
                  Object buf = new Reference(var5[0].callConstructor($get$$class$java$lang$StringBuffer()));
                  var5[1].call(instructionx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), buf) {
                     private Reference<T> buf;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$xml$StreamingSAXBuilder$_closure2_closure7_closure8;

                     public {
                        CallSite[] var4 = $getCallSiteArray();
                        this.buf = (Reference)buf;
                     }

                     public Object doCall(Object name, Object value) {
                        Object namex = new Reference(name);
                        Object valuex = new Reference(value);
                        CallSite[] var5 = $getCallSiteArray();
                        return DefaultTypeTransformation.booleanUnbox(var5[0].call(var5[1].call(valuex.get()), (Object)"\"")) ? var5[2].call(this.buf.get(), (Object)(new GStringImpl(new Object[]{namex.get(), valuex.get()}, new String[]{" ", "='", "'"}))) : var5[3].call(this.buf.get(), (Object)(new GStringImpl(new Object[]{namex.get(), valuex.get()}, new String[]{" ", "=\"", "\""})));
                     }

                     public Object call(Object name, Object value) {
                        Object namex = new Reference(name);
                        Object valuex = new Reference(value);
                        CallSite[] var5 = $getCallSiteArray();
                        return var5[4].callCurrent(this, namex.get(), valuex.get());
                     }

                     public Object getBuf() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.buf.get();
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure2_closure7_closure8()) {
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
                        var0[2] = "append";
                        var0[3] = "append";
                        var0[4] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[5];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure2_closure7_closure8(), var0);
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
                     private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure2_closure7_closure8() {
                        Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure2_closure7_closure8;
                        if (var10000 == null) {
                           var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure2_closure7_closure8 = class$("groovy.xml.StreamingSAXBuilder$_closure2_closure7_closure8");
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
                  return var5[2].call(this.contentHandler.get(), targetx.get(), var5[3].call(buf.get()));
               } else {
                  return var5[4].call(this.contentHandler.get(), targetx.get(), instructionx.get());
               }
            }

            public Object call(Object target, Object instruction) {
               Object targetx = new Reference(target);
               Object instructionx = new Reference(instruction);
               CallSite[] var5 = $getCallSiteArray();
               return var5[5].callCurrent(this, targetx.get(), instructionx.get());
            }

            public Object getContentHandler() {
               CallSite[] var1 = $getCallSiteArray();
               return this.contentHandler.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure2_closure7()) {
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
               var0[1] = "each";
               var0[2] = "processingInstruction";
               var0[3] = "toString";
               var0[4] = "processingInstruction";
               var0[5] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[6];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure2_closure7(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure2_closure7() {
               Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure2_closure7;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure2_closure7 = class$("groovy.xml.StreamingSAXBuilder$_closure2_closure7");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$lang$StringBuffer() {
               Class var10000 = $class$java$lang$StringBuffer;
               if (var10000 == null) {
                  var10000 = $class$java$lang$StringBuffer = class$("java.lang.StringBuffer");
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

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object contentHandler) {
         Object attrsx = new Reference(attrs);
         Object contentHandlerx = new Reference(contentHandler);
         CallSite[] var11 = $getCallSiteArray();
         return var11[1].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrsx.get(), body, contentHandlerx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure2()) {
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
         return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure2(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure2() {
         Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure2;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure2 = class$("groovy.xml.StreamingSAXBuilder$_closure2");
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
      private static Class $class$groovy$xml$StreamingSAXBuilder$_closure3;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object contentHandlerx) {
         Object docx = new Reference(doc);
         Object bodyx = new Reference(body);
         Object contentHandler = new Reference(contentHandlerx);
         CallSite[] var12 = $getCallSiteArray();
         return ScriptBytecodeAdapter.compareNotEqual(bodyx.get(), (Object)null) ? var12[0].callCurrent(this, bodyx.get(), docx.get(), contentHandler.get()) : null;
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object contentHandlerx) {
         Object docx = new Reference(doc);
         Object bodyx = new Reference(body);
         Object contentHandler = new Reference(contentHandlerx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[1].callCurrent(this, (Object[])ArrayUtil.createArray(docx.get(), pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, bodyx.get(), contentHandler.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure3()) {
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
         var0[0] = "processBody";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure3(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure3() {
         Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure3;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure3 = class$("groovy.xml.StreamingSAXBuilder$_closure3");
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
      private static Class $class$groovy$xml$StreamingSAXBuilder$_closure4;
      // $FF: synthetic field
      private static Class $class$org$xml$sax$helpers$AttributesImpl;
      // $FF: synthetic field
      private static Class $class$groovy$lang$GroovyRuntimeException;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object tag, Object docx, Object pendingNamespacesx, Object namespacesx, Object namespaceSpecificTags, Object prefixx, Object attrsx, Object bodyx, Object contentHandlerx) {
         Object tagx = new Reference(tag);
         Object doc = new Reference(docx);
         Object pendingNamespaces = new Reference(pendingNamespacesx);
         Object namespaces = new Reference(namespacesx);
         Object prefix = new Reference(prefixx);
         Object attrs = new Reference(attrsx);
         Object body = new Reference(bodyx);
         Object contentHandler = new Reference(contentHandlerx);
         CallSite[] var18 = $getCallSiteArray();
         Object attributes = new Reference(var18[0].callConstructor($get$$class$org$xml$sax$helpers$AttributesImpl()));
         var18[1].call(attrs.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), attributes, namespaces) {
            private Reference<T> attributes;
            private Reference<T> namespaces;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingSAXBuilder$_closure4_closure9;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.attributes = (Reference)attributes;
               this.namespaces = (Reference)namespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[0].callCurrent(this, this.attributes.get(), keyx.get(), valuex.get(), this.namespaces.get());
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[1].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getAttributes() {
               CallSite[] var1 = $getCallSiteArray();
               return this.attributes.get();
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure9()) {
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
               var0[0] = "addAttributes";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure9(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure9() {
               Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4_closure9;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4_closure9 = class$("groovy.xml.StreamingSAXBuilder$_closure4_closure9");
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
         var18[2].call(pendingNamespaces.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), contentHandler, attributes, namespaces, hiddenNamespaces) {
            private Reference<T> contentHandler;
            private Reference<T> attributes;
            private Reference<T> namespaces;
            private Reference<T> hiddenNamespaces;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingSAXBuilder$_closure4_closure10;

            public {
               CallSite[] var7 = $getCallSiteArray();
               this.contentHandler = (Reference)contentHandler;
               this.attributes = (Reference)attributes;
               this.namespaces = (Reference)namespaces;
               this.hiddenNamespaces = (Reference)hiddenNamespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               Object k = ScriptBytecodeAdapter.compareEqual(keyx.get(), ":") ? "" : keyx.get();
               CallSite var10000 = var5[0];
               Object var10001 = this.hiddenNamespaces.get();
               Object var7 = var5[1].call(this.namespaces.get(), keyx.get());
               var10000.call(var10001, k, var7);
               var10000 = var5[2];
               var10001 = this.namespaces.get();
               var7 = valuex.get();
               var10000.call(var10001, k, var7);
               var5[3].call(this.attributes.get(), ArrayUtil.createArray("http://www.w3.org/2000/xmlns/", k, new GStringImpl(new Object[]{ScriptBytecodeAdapter.compareEqual(k, "") ? "" : new GStringImpl(new Object[]{k}, new String[]{":", ""})}, new String[]{"xmlns", ""}), "CDATA", new GStringImpl(new Object[]{valuex.get()}, new String[]{"", ""})));
               return var5[4].call(this.contentHandler.get(), k, valuex.get());
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[5].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getContentHandler() {
               CallSite[] var1 = $getCallSiteArray();
               return this.contentHandler.get();
            }

            public Object getAttributes() {
               CallSite[] var1 = $getCallSiteArray();
               return this.attributes.get();
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            public Object getHiddenNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.hiddenNamespaces.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure10()) {
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
               var0[3] = "addAttribute";
               var0[4] = "startPrefixMapping";
               var0[5] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[6];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure10(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure10() {
               Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4_closure10;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4_closure10 = class$("groovy.xml.StreamingSAXBuilder$_closure4_closure10");
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
         Object uri = new Reference("");
         Object qualifiedName = new Reference(tagx.get());
         if (ScriptBytecodeAdapter.compareNotEqual(prefix.get(), "")) {
            if (DefaultTypeTransformation.booleanUnbox(var18[3].call(namespaces.get(), prefix.get()))) {
               uri.set(var18[4].call(namespaces.get(), prefix.get()));
            } else {
               if (!DefaultTypeTransformation.booleanUnbox(var18[5].call(pendingNamespaces.get(), prefix.get()))) {
                  throw (Throwable)var18[7].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{prefix.get()}, new String[]{"Namespace prefix: ", " is not bound to a URI"})));
               }

               uri.set(var18[6].call(pendingNamespaces.get(), prefix.get()));
            }

            if (ScriptBytecodeAdapter.compareNotEqual(prefix.get(), ":")) {
               qualifiedName.set(var18[8].call(var18[9].call(prefix.get(), (Object)":"), tagx.get()));
            }
         }

         var18[10].call(contentHandler.get(), uri.get(), tagx.get(), qualifiedName.get(), attributes.get());
         if (ScriptBytecodeAdapter.compareNotEqual(body.get(), (Object)null)) {
            var18[11].call(var18[12].callGroovyObjectGetProperty(this), var18[13].call(pendingNamespaces.get()));
            var18[14].call(pendingNamespaces.get());
            var18[15].callCurrent(this, body.get(), doc.get(), contentHandler.get());
            var18[16].call(pendingNamespaces.get());
            var18[17].call(pendingNamespaces.get(), var18[18].call(var18[19].callGroovyObjectGetProperty(this)));
         }

         var18[20].call(contentHandler.get(), uri.get(), tagx.get(), qualifiedName.get());
         return var18[21].call(hiddenNamespaces.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), contentHandler, namespaces) {
            private Reference<T> contentHandler;
            private Reference<T> namespaces;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingSAXBuilder$_closure4_closure11;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.contentHandler = (Reference)contentHandler;
               this.namespaces = (Reference)namespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               var5[0].call(this.contentHandler.get(), keyx.get());
               if (ScriptBytecodeAdapter.compareEqual(valuex.get(), (Object)null)) {
                  return var5[1].call(this.namespaces.get(), keyx.get());
               } else {
                  CallSite var10000 = var5[2];
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
               return var5[3].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getContentHandler() {
               CallSite[] var1 = $getCallSiteArray();
               return this.contentHandler.get();
            }

            public Object getNamespaces() {
               CallSite[] var1 = $getCallSiteArray();
               return this.namespaces.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure11()) {
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
               var0[0] = "endPrefixMapping";
               var0[1] = "remove";
               var0[2] = "putAt";
               var0[3] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[4];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure11(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure4_closure11() {
               Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4_closure11;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4_closure11 = class$("groovy.xml.StreamingSAXBuilder$_closure4_closure11");
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

      public Object call(Object tag, Object docx, Object pendingNamespacesx, Object namespacesx, Object namespaceSpecificTags, Object prefixx, Object attrsx, Object bodyx, Object contentHandlerx) {
         Object tagx = new Reference(tag);
         Object doc = new Reference(docx);
         Object pendingNamespaces = new Reference(pendingNamespacesx);
         Object namespaces = new Reference(namespacesx);
         Object prefix = new Reference(prefixx);
         Object attrs = new Reference(attrsx);
         Object body = new Reference(bodyx);
         Object contentHandler = new Reference(contentHandlerx);
         CallSite[] var18 = $getCallSiteArray();
         return var18[22].callCurrent(this, (Object[])ArrayUtil.createArray(tagx.get(), doc.get(), pendingNamespaces.get(), namespaces.get(), namespaceSpecificTags, prefix.get(), attrs.get(), body.get(), contentHandler.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingSAXBuilder$_closure4()) {
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
         var0[1] = "each";
         var0[2] = "each";
         var0[3] = "containsKey";
         var0[4] = "getAt";
         var0[5] = "containsKey";
         var0[6] = "getAt";
         var0[7] = "<$constructor$>";
         var0[8] = "plus";
         var0[9] = "plus";
         var0[10] = "startElement";
         var0[11] = "add";
         var0[12] = "pendingStack";
         var0[13] = "clone";
         var0[14] = "clear";
         var0[15] = "processBody";
         var0[16] = "clear";
         var0[17] = "putAll";
         var0[18] = "pop";
         var0[19] = "pendingStack";
         var0[20] = "endElement";
         var0[21] = "each";
         var0[22] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[23];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingSAXBuilder$_closure4(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingSAXBuilder$_closure4() {
         Class var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingSAXBuilder$_closure4 = class$("groovy.xml.StreamingSAXBuilder$_closure4");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$org$xml$sax$helpers$AttributesImpl() {
         Class var10000 = $class$org$xml$sax$helpers$AttributesImpl;
         if (var10000 == null) {
            var10000 = $class$org$xml$sax$helpers$AttributesImpl = class$("org.xml.sax.helpers.AttributesImpl");
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
