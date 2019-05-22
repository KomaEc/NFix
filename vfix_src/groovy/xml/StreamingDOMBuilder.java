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
import org.w3c.dom.Node;

public class StreamingDOMBuilder extends AbstractStreamingBuilder implements GroovyObject {
   private Object pendingStack;
   private Object defaultNamespaceStack;
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
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204623L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204623 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$xml$StreamingDOMBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public StreamingDOMBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.pendingStack = ScriptBytecodeAdapter.createList(new Object[0]);
      this.defaultNamespaceStack = ScriptBytecodeAdapter.createList(new Object[]{""});
      this.commentClosure = new StreamingDOMBuilder._closure1(this, this);
      this.piClosure = new StreamingDOMBuilder._closure2(this, this);
      this.noopClosure = new StreamingDOMBuilder._closure3(this, this);
      this.tagClosure = new StreamingDOMBuilder._closure4(this, this);
      this.builder = null;
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      var1[0].call(var1[1].callGroovyObjectGetProperty(this), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"yield", this.noopClosure, "yieldUnescaped", this.noopClosure, "comment", this.commentClosure, "pi", this.piClosure}));
      Object nsSpecificTags = ScriptBytecodeAdapter.createMap(new Object[]{":", ScriptBytecodeAdapter.createList(new Object[]{this.tagClosure, this.tagClosure, ScriptBytecodeAdapter.createMap(new Object[0])}), "http://www.w3.org/2000/xmlns/", ScriptBytecodeAdapter.createList(new Object[]{this.tagClosure, this.tagClosure, ScriptBytecodeAdapter.createMap(new Object[0])}), "http://www.codehaus.org/Groovy/markup/keywords", ScriptBytecodeAdapter.createList(new Object[]{var1[2].callGroovyObjectGetProperty(this), this.tagClosure, var1[3].callGroovyObjectGetProperty(this)})});
      this.builder = var1[4].callConstructor($get$$class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder(), (Object)nsSpecificTags);
   }

   public Object bind(Object closure) {
      CallSite[] var2 = $getCallSiteArray();
      Object boundClosure = new Reference(var2[5].call(this.builder, closure));
      return new GeneratedClosure(this, this, boundClosure) {
         private Reference<T> boundClosure;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$javax$xml$parsers$DocumentBuilderFactory;
         // $FF: synthetic field
         private static Class $class$groovy$xml$StreamingDOMBuilder$_bind_closure5;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.boundClosure = (Reference)boundClosure;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            Object dBuilder;
            if (itx.get() instanceof Node) {
               dBuilder = var3[0].call(itx.get());
               ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.createMap(new Object[]{"document", dBuilder, "element", itx.get()}), $get$$class$groovy$xml$StreamingDOMBuilder$_bind_closure5(), this.boundClosure.get(), "trigger");
               return dBuilder;
            } else {
               dBuilder = var3[1].call($get$$class$javax$xml$parsers$DocumentBuilderFactory());
               ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$xml$StreamingDOMBuilder$_bind_closure5(), dBuilder, "namespaceAware");
               Object newDocument = var3[2].call(var3[3].call(dBuilder));
               ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.createMap(new Object[]{"document", newDocument, "element", newDocument}), $get$$class$groovy$xml$StreamingDOMBuilder$_bind_closure5(), this.boundClosure.get(), "trigger");
               return newDocument;
            }
         }

         public Object getBoundClosure() {
            CallSite[] var1 = $getCallSiteArray();
            return this.boundClosure.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_bind_closure5()) {
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
            var0[0] = "getOwnerDocument";
            var0[1] = "newInstance";
            var0[2] = "newDocument";
            var0[3] = "newDocumentBuilder";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_bind_closure5(), var0);
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
         private static Class $get$$class$javax$xml$parsers$DocumentBuilderFactory() {
            Class var10000 = $class$javax$xml$parsers$DocumentBuilderFactory;
            if (var10000 == null) {
               var10000 = $class$javax$xml$parsers$DocumentBuilderFactory = class$("javax.xml.parsers.DocumentBuilderFactory");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_bind_closure5() {
            Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_bind_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$xml$StreamingDOMBuilder$_bind_closure5 = class$("groovy.xml.StreamingDOMBuilder$_bind_closure5");
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
      if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder()) {
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
      Class var10000 = $get$$class$groovy$xml$StreamingDOMBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$xml$StreamingDOMBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$xml$StreamingDOMBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object getPendingStack() {
      return this.pendingStack;
   }

   public void setPendingStack(Object var1) {
      this.pendingStack = var1;
   }

   public Object getDefaultNamespaceStack() {
      return this.defaultNamespaceStack;
   }

   public void setDefaultNamespaceStack(Object var1) {
      this.defaultNamespaceStack = var1;
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
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[6];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder(), var0);
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
   private static Class $get$$class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder() {
      Class var10000 = $class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$streamingmarkupsupport$BaseMarkupBuilder = class$("groovy.xml.streamingmarkupsupport.BaseMarkupBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$xml$StreamingDOMBuilder() {
      Class var10000 = $class$groovy$xml$StreamingDOMBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$xml$StreamingDOMBuilder = class$("groovy.xml.StreamingDOMBuilder");
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
      private static Class $class$groovy$xml$StreamingDOMBuilder$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object dom) {
         Object bodyx = new Reference(body);
         Object domx = new Reference(dom);
         CallSite[] var11 = $getCallSiteArray();
         Object comment = new Reference(var11[0].call(var11[1].callGetProperty(domx.get()), bodyx.get()));
         return ScriptBytecodeAdapter.compareNotEqual(comment.get(), (Object)null) ? var11[2].call(var11[3].callGetProperty(domx.get()), comment.get()) : null;
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object dom) {
         Object bodyx = new Reference(body);
         Object domx = new Reference(dom);
         CallSite[] var11 = $getCallSiteArray();
         return var11[4].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, bodyx.get(), domx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure1()) {
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
         var0[0] = "createComment";
         var0[1] = "document";
         var0[2] = "appendChild";
         var0[3] = "element";
         var0[4] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[5];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure1(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure1() {
         Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure1 = class$("groovy.xml.StreamingDOMBuilder$_closure1");
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
      private static Class $class$groovy$xml$StreamingDOMBuilder$_closure2;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object dom) {
         Object attrsx = new Reference(attrs);
         Object domx = new Reference(dom);
         CallSite[] var11 = $getCallSiteArray();
         return var11[0].call(attrsx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), domx) {
            private Reference<T> dom;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingDOMBuilder$_closure2_closure6;
            // $FF: synthetic field
            private static Class $class$java$lang$StringBuffer;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.dom = (Reference)dom;
            }

            public Object doCall(Object target, Object instruction) {
               Object targetx = new Reference(target);
               Object instructionx = new Reference(instruction);
               CallSite[] var5 = $getCallSiteArray();
               Object pi = new Reference((Object)null);
               if (instructionx.get() instanceof Map) {
                  Object buf = new Reference(var5[0].callConstructor($get$$class$java$lang$StringBuffer()));
                  var5[1].call(instructionx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), buf) {
                     private Reference<T> buf;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$xml$StreamingDOMBuilder$_closure2_closure6_closure7;

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
                        if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure2_closure6_closure7()) {
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
                        return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure2_closure6_closure7(), var0);
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
                     private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure2_closure6_closure7() {
                        Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure2_closure6_closure7;
                        if (var10000 == null) {
                           var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure2_closure6_closure7 = class$("groovy.xml.StreamingDOMBuilder$_closure2_closure6_closure7");
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
                  pi.set(var5[2].call(var5[3].callGetProperty(this.dom.get()), targetx.get(), var5[4].call(buf.get())));
               } else {
                  pi.set(var5[5].call(var5[6].callGetProperty(this.dom.get()), targetx.get(), instructionx.get()));
               }

               return ScriptBytecodeAdapter.compareNotEqual(pi.get(), (Object)null) ? var5[7].call(var5[8].callGetProperty(this.dom.get()), pi.get()) : null;
            }

            public Object call(Object target, Object instruction) {
               Object targetx = new Reference(target);
               Object instructionx = new Reference(instruction);
               CallSite[] var5 = $getCallSiteArray();
               return var5[9].callCurrent(this, targetx.get(), instructionx.get());
            }

            public Object getDom() {
               CallSite[] var1 = $getCallSiteArray();
               return this.dom.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure2_closure6()) {
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
               var0[2] = "createProcessingInstruction";
               var0[3] = "document";
               var0[4] = "toString";
               var0[5] = "createProcessingInstruction";
               var0[6] = "document";
               var0[7] = "appendChild";
               var0[8] = "element";
               var0[9] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[10];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure2_closure6(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure2_closure6() {
               Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure2_closure6;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure2_closure6 = class$("groovy.xml.StreamingDOMBuilder$_closure2_closure6");
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

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object dom) {
         Object attrsx = new Reference(attrs);
         Object domx = new Reference(dom);
         CallSite[] var11 = $getCallSiteArray();
         return var11[1].callCurrent(this, (Object[])ArrayUtil.createArray(doc, pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrsx.get(), body, domx.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure2()) {
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
         return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure2(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure2() {
         Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure2;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure2 = class$("groovy.xml.StreamingDOMBuilder$_closure2");
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
      private static Class $class$groovy$xml$StreamingDOMBuilder$_closure3;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object domx) {
         Object docx = new Reference(doc);
         Object bodyx = new Reference(body);
         Object dom = new Reference(domx);
         CallSite[] var12 = $getCallSiteArray();
         if (bodyx.get() instanceof Closure) {
            Object body1 = var12[0].call(bodyx.get());
            ScriptBytecodeAdapter.setProperty(docx.get(), $get$$class$groovy$xml$StreamingDOMBuilder$_closure3(), body1, "delegate");
            return var12[1].call(body1, docx.get());
         } else if (bodyx.get() instanceof Buildable) {
            return var12[2].call(bodyx.get(), docx.get());
         } else {
            return ScriptBytecodeAdapter.compareNotEqual(bodyx.get(), (Object)null) ? var12[3].call(bodyx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), dom, docx) {
               private Reference<T> dom;
               private Reference<T> doc;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$xml$StreamingDOMBuilder$_closure3_closure8;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.dom = (Reference)dom;
                  this.doc = (Reference)doc;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  if (itx.get() instanceof Closure) {
                     Object body1 = var3[0].call(itx.get());
                     ScriptBytecodeAdapter.setProperty(this.doc.get(), $get$$class$groovy$xml$StreamingDOMBuilder$_closure3_closure8(), body1, "delegate");
                     return var3[1].call(body1, this.doc.get());
                  } else {
                     return itx.get() instanceof Buildable ? var3[2].call(itx.get(), this.doc.get()) : var3[3].call(var3[4].callGetProperty(this.dom.get()), var3[5].call(var3[6].callGetProperty(this.dom.get()), itx.get()));
                  }
               }

               public Object getDom() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.dom.get();
               }

               public Object getDoc() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.doc.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure3_closure8()) {
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
                  var0[3] = "appendChild";
                  var0[4] = "element";
                  var0[5] = "createTextNode";
                  var0[6] = "document";
                  var0[7] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[8];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure3_closure8(), var0);
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
               private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure3_closure8() {
                  Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure3_closure8;
                  if (var10000 == null) {
                     var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure3_closure8 = class$("groovy.xml.StreamingDOMBuilder$_closure3_closure8");
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
            })) : null;
         }
      }

      public Object call(Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object domx) {
         Object docx = new Reference(doc);
         Object bodyx = new Reference(body);
         Object dom = new Reference(domx);
         CallSite[] var12 = $getCallSiteArray();
         return var12[4].callCurrent(this, (Object[])ArrayUtil.createArray(docx.get(), pendingNamespaces, namespaces, namespaceSpecificTags, prefix, attrs, bodyx.get(), dom.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure3()) {
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
         var0[3] = "each";
         var0[4] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[5];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure3(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure3() {
         Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure3;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure3 = class$("groovy.xml.StreamingDOMBuilder$_closure3");
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
      private static Class $class$groovy$xml$StreamingDOMBuilder$_closure4;
      // $FF: synthetic field
      private static Class $class$groovy$lang$GroovyRuntimeException;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object tag, Object doc, Object pendingNamespaces, Object namespaces, Object namespaceSpecificTags, Object prefix, Object attrs, Object body, Object dom) {
         Object tagx = new Reference(tag);
         Object docx = new Reference(doc);
         Object pendingNamespacesx = new Reference(pendingNamespaces);
         Object namespacesx = new Reference(namespaces);
         Object prefixx = new Reference(prefix);
         Object attrsx = new Reference(attrs);
         Object bodyx = new Reference(body);
         Object domx = new Reference(dom);
         CallSite[] var18 = $getCallSiteArray();
         Object attributes = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         Object nsAttributes = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         Object defaultNamespace = new Reference(var18[0].call(var18[1].callGroovyObjectGetProperty(this)));
         var18[2].call(attrsx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), nsAttributes, attributes, namespacesx) {
            private Reference<T> nsAttributes;
            private Reference<T> attributes;
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
            private static Class $class$groovy$xml$StreamingDOMBuilder$_closure4_closure9;
            // $FF: synthetic field
            private static Class $class$groovy$lang$GroovyRuntimeException;

            public {
               CallSite[] var6 = $getCallSiteArray();
               this.nsAttributes = (Reference)nsAttributes;
               this.attributes = (Reference)attributes;
               this.namespaces = (Reference)namespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               if (DefaultTypeTransformation.booleanUnbox(var5[0].call(keyx.get(), (Object)"$"))) {
                  Object parts = new Reference(var5[1].call(keyx.get(), (Object)"$"));
                  Object namespaceUri = new Reference((Object)null);
                  if (DefaultTypeTransformation.booleanUnbox(var5[2].call(this.namespaces.get(), var5[3].call(parts.get(), (Object)$const$0)))) {
                     namespaceUri.set(var5[4].call(this.namespaces.get(), var5[5].call(parts.get(), (Object)$const$0)));
                     return var5[6].call(this.nsAttributes.get(), (Object)ScriptBytecodeAdapter.createList(new Object[]{namespaceUri.get(), new GStringImpl(new Object[]{var5[7].call(parts.get(), (Object)$const$0), var5[8].call(parts.get(), (Object)$const$1)}, new String[]{"", ":", ""}), new GStringImpl(new Object[]{valuex.get()}, new String[]{"", ""})}));
                  } else {
                     throw (Throwable)var5[9].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{keyx.get()}, new String[]{"bad attribute namespace tag in ", ""})));
                  }
               } else {
                  return var5[10].call(this.attributes.get(), (Object)ScriptBytecodeAdapter.createList(new Object[]{keyx.get(), valuex.get()}));
               }
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[11].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getNsAttributes() {
               CallSite[] var1 = $getCallSiteArray();
               return this.nsAttributes.get();
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
               if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure9()) {
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
               var0[4] = "getAt";
               var0[5] = "getAt";
               var0[6] = "add";
               var0[7] = "getAt";
               var0[8] = "getAt";
               var0[9] = "<$constructor$>";
               var0[10] = "add";
               var0[11] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[12];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure9(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure9() {
               Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure9;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure9 = class$("groovy.xml.StreamingDOMBuilder$_closure4_closure9");
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
         var18[3].call(pendingNamespacesx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), nsAttributes, defaultNamespace, namespacesx, hiddenNamespaces) {
            private Reference<T> nsAttributes;
            private Reference<T> defaultNamespace;
            private Reference<T> namespaces;
            private Reference<T> hiddenNamespaces;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingDOMBuilder$_closure4_closure10;

            public {
               CallSite[] var7 = $getCallSiteArray();
               this.nsAttributes = (Reference)nsAttributes;
               this.defaultNamespace = (Reference)defaultNamespace;
               this.namespaces = (Reference)namespaces;
               this.hiddenNamespaces = (Reference)hiddenNamespaces;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               if (ScriptBytecodeAdapter.compareEqual(keyx.get(), ":")) {
                  this.defaultNamespace.set(new GStringImpl(new Object[]{valuex.get()}, new String[]{"", ""}));
                  return var5[0].call(this.nsAttributes.get(), (Object)ScriptBytecodeAdapter.createList(new Object[]{"http://www.w3.org/2000/xmlns/", "xmlns", this.defaultNamespace.get()}));
               } else {
                  CallSite var10000 = var5[1];
                  Object var10001 = this.hiddenNamespaces.get();
                  Object var10002 = keyx.get();
                  Object var6 = var5[2].call(this.namespaces.get(), keyx.get());
                  var10000.call(var10001, var10002, var6);
                  var10000 = var5[3];
                  var10001 = this.namespaces.get();
                  var10002 = keyx.get();
                  var6 = valuex.get();
                  var10000.call(var10001, var10002, var6);
                  return var5[4].call(this.nsAttributes.get(), (Object)ScriptBytecodeAdapter.createList(new Object[]{"http://www.w3.org/2000/xmlns/", new GStringImpl(new Object[]{keyx.get()}, new String[]{"xmlns:", ""}), new GStringImpl(new Object[]{valuex.get()}, new String[]{"", ""})}));
               }
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[5].callCurrent(this, keyx.get(), valuex.get());
            }

            public Object getNsAttributes() {
               CallSite[] var1 = $getCallSiteArray();
               return this.nsAttributes.get();
            }

            public Object getDefaultNamespace() {
               CallSite[] var1 = $getCallSiteArray();
               return this.defaultNamespace.get();
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
               if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure10()) {
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
               var0[0] = "add";
               var0[1] = "putAt";
               var0[2] = "getAt";
               var0[3] = "putAt";
               var0[4] = "add";
               var0[5] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[6];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure10(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure10() {
               Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure10;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure10 = class$("groovy.xml.StreamingDOMBuilder$_closure4_closure10");
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
         Object uri = new Reference(defaultNamespace.get());
         Object qualifiedName = new Reference(tagx.get());
         if (ScriptBytecodeAdapter.compareNotEqual(prefixx.get(), "")) {
            if (DefaultTypeTransformation.booleanUnbox(var18[4].call(namespacesx.get(), prefixx.get()))) {
               uri.set(var18[5].call(namespacesx.get(), prefixx.get()));
            } else {
               if (!DefaultTypeTransformation.booleanUnbox(var18[6].call(pendingNamespacesx.get(), prefixx.get()))) {
                  throw (Throwable)var18[8].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), (Object)(new GStringImpl(new Object[]{prefixx.get()}, new String[]{"Namespace prefix: ", " is not bound to a URI"})));
               }

               uri.set(var18[7].call(pendingNamespacesx.get(), prefixx.get()));
            }

            if (ScriptBytecodeAdapter.compareNotEqual(prefixx.get(), ":")) {
               qualifiedName.set(var18[9].call(var18[10].call(prefixx.get(), (Object)":"), tagx.get()));
            }
         }

         Object element = new Reference(var18[11].call(var18[12].callGetProperty(domx.get()), uri.get(), qualifiedName.get()));
         var18[13].call(nsAttributes.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), element) {
            private Reference<T> element;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static final Integer $const$1 = (Integer)1;
            // $FF: synthetic field
            private static final Integer $const$2 = (Integer)2;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingDOMBuilder$_closure4_closure11;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.element = (Reference)element;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].call(this.element.get(), var3[1].call(itx.get(), (Object)$const$0), var3[2].call(itx.get(), (Object)$const$1), var3[3].call(itx.get(), (Object)$const$2));
            }

            public Object getElement() {
               CallSite[] var1 = $getCallSiteArray();
               return this.element.get();
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure11()) {
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
               var0[0] = "setAttributeNS";
               var0[1] = "getAt";
               var0[2] = "getAt";
               var0[3] = "getAt";
               var0[4] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure11(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure11() {
               Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure11;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure11 = class$("groovy.xml.StreamingDOMBuilder$_closure4_closure11");
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
         var18[14].call(attributes.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), element) {
            private Reference<T> element;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static final Integer $const$1 = (Integer)1;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingDOMBuilder$_closure4_closure12;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.element = (Reference)element;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].call(this.element.get(), var3[1].call(itx.get(), (Object)$const$0), var3[2].call(itx.get(), (Object)$const$1));
            }

            public Object getElement() {
               CallSite[] var1 = $getCallSiteArray();
               return this.element.get();
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure12()) {
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
               var0[0] = "setAttribute";
               var0[1] = "getAt";
               var0[2] = "getAt";
               var0[3] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[4];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure12(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure12() {
               Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure12;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure12 = class$("groovy.xml.StreamingDOMBuilder$_closure4_closure12");
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
         var18[15].call(var18[16].callGetProperty(domx.get()), element.get());
         ScriptBytecodeAdapter.setProperty(element.get(), $get$$class$groovy$xml$StreamingDOMBuilder$_closure4(), domx.get(), "element");
         if (ScriptBytecodeAdapter.compareNotEqual(bodyx.get(), (Object)null)) {
            var18[17].call(var18[18].callGroovyObjectGetProperty(this), defaultNamespace.get());
            var18[19].call(var18[20].callGroovyObjectGetProperty(this), var18[21].call(pendingNamespacesx.get()));
            var18[22].call(pendingNamespacesx.get());
            if (bodyx.get() instanceof Closure) {
               Object body1 = var18[23].call(bodyx.get());
               ScriptBytecodeAdapter.setProperty(docx.get(), $get$$class$groovy$xml$StreamingDOMBuilder$_closure4(), body1, "delegate");
               var18[24].call(body1, docx.get());
            } else if (bodyx.get() instanceof Buildable) {
               var18[25].call(bodyx.get(), docx.get());
            } else {
               var18[26].call(bodyx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), domx, docx) {
                  private Reference<T> dom;
                  private Reference<T> doc;
                  // $FF: synthetic field
                  private static ClassInfo $staticClassInfo;
                  // $FF: synthetic field
                  private static SoftReference $callSiteArray;
                  // $FF: synthetic field
                  private static Class $class$java$lang$Object;
                  // $FF: synthetic field
                  private static Class $class$groovy$xml$StreamingDOMBuilder$_closure4_closure13;

                  public {
                     CallSite[] var5 = $getCallSiteArray();
                     this.dom = (Reference)dom;
                     this.doc = (Reference)doc;
                  }

                  public Object doCall(Object it) {
                     Object itx = new Reference(it);
                     CallSite[] var3 = $getCallSiteArray();
                     if (itx.get() instanceof Closure) {
                        Object body1 = var3[0].call(itx.get());
                        ScriptBytecodeAdapter.setProperty(this.doc.get(), $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure13(), body1, "delegate");
                        return var3[1].call(body1, this.doc.get());
                     } else {
                        return itx.get() instanceof Buildable ? var3[2].call(itx.get(), this.doc.get()) : var3[3].call(var3[4].callGetProperty(this.dom.get()), var3[5].call(var3[6].callGetProperty(this.dom.get()), itx.get()));
                     }
                  }

                  public Object getDom() {
                     CallSite[] var1 = $getCallSiteArray();
                     return this.dom.get();
                  }

                  public Object getDoc() {
                     CallSite[] var1 = $getCallSiteArray();
                     return this.doc.get();
                  }

                  public Object doCall() {
                     CallSite[] var1 = $getCallSiteArray();
                     return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                  }

                  // $FF: synthetic method
                  protected MetaClass $getStaticMetaClass() {
                     if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure13()) {
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
                     var0[3] = "appendChild";
                     var0[4] = "element";
                     var0[5] = "createTextNode";
                     var0[6] = "document";
                     var0[7] = "doCall";
                  }

                  // $FF: synthetic method
                  private static CallSiteArray $createCallSiteArray() {
                     String[] var0 = new String[8];
                     $createCallSiteArray_1(var0);
                     return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure13(), var0);
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
                  private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure13() {
                     Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure13;
                     if (var10000 == null) {
                        var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure13 = class$("groovy.xml.StreamingDOMBuilder$_closure4_closure13");
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

            var18[27].call(pendingNamespacesx.get());
            var18[28].call(pendingNamespacesx.get(), var18[29].call(var18[30].callGroovyObjectGetProperty(this)));
            var18[31].call(var18[32].callGroovyObjectGetProperty(this));
         }

         ScriptBytecodeAdapter.setProperty(var18[33].call(var18[34].callGetProperty(domx.get())), $get$$class$groovy$xml$StreamingDOMBuilder$_closure4(), domx.get(), "element");
         return var18[35].call(hiddenNamespaces.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), namespacesx) {
            private Reference<T> namespaces;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$xml$StreamingDOMBuilder$_closure4_closure14;

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
               if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure14()) {
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
               return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure14(), var0);
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
            private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure4_closure14() {
               Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure14;
               if (var10000 == null) {
                  var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4_closure14 = class$("groovy.xml.StreamingDOMBuilder$_closure4_closure14");
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

      public Object call(Object tag, Object docx, Object pendingNamespacesx, Object namespacesx, Object namespaceSpecificTags, Object prefixx, Object attrsx, Object bodyx, Object domx) {
         Object tagx = new Reference(tag);
         Object doc = new Reference(docx);
         Object pendingNamespaces = new Reference(pendingNamespacesx);
         Object namespaces = new Reference(namespacesx);
         Object prefix = new Reference(prefixx);
         Object attrs = new Reference(attrsx);
         Object body = new Reference(bodyx);
         Object dom = new Reference(domx);
         CallSite[] var18 = $getCallSiteArray();
         return var18[36].callCurrent(this, (Object[])ArrayUtil.createArray(tagx.get(), doc.get(), pendingNamespaces.get(), namespaces.get(), namespaceSpecificTags, prefix.get(), attrs.get(), body.get(), dom.get()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$xml$StreamingDOMBuilder$_closure4()) {
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
         var0[0] = "last";
         var0[1] = "defaultNamespaceStack";
         var0[2] = "each";
         var0[3] = "each";
         var0[4] = "containsKey";
         var0[5] = "getAt";
         var0[6] = "containsKey";
         var0[7] = "getAt";
         var0[8] = "<$constructor$>";
         var0[9] = "plus";
         var0[10] = "plus";
         var0[11] = "createElementNS";
         var0[12] = "document";
         var0[13] = "each";
         var0[14] = "each";
         var0[15] = "appendChild";
         var0[16] = "element";
         var0[17] = "push";
         var0[18] = "defaultNamespaceStack";
         var0[19] = "add";
         var0[20] = "pendingStack";
         var0[21] = "clone";
         var0[22] = "clear";
         var0[23] = "clone";
         var0[24] = "call";
         var0[25] = "build";
         var0[26] = "each";
         var0[27] = "clear";
         var0[28] = "putAll";
         var0[29] = "pop";
         var0[30] = "pendingStack";
         var0[31] = "pop";
         var0[32] = "defaultNamespaceStack";
         var0[33] = "getParentNode";
         var0[34] = "element";
         var0[35] = "each";
         var0[36] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[37];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$xml$StreamingDOMBuilder$_closure4(), var0);
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
      private static Class $get$$class$groovy$xml$StreamingDOMBuilder$_closure4() {
         Class var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4;
         if (var10000 == null) {
            var10000 = $class$groovy$xml$StreamingDOMBuilder$_closure4 = class$("groovy.xml.StreamingDOMBuilder$_closure4");
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
