package groovy.inspect.swingui;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AstBrowserProperties extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204927L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204927 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstBrowserProperties;

   public AstBrowserProperties() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public AstBrowserProperties(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$inspect$swingui$AstBrowserProperties(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      return var1[1].callCurrent(this, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  return var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        return var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                           // $FF: synthetic field
                           private static ClassInfo $staticClassInfo;
                           // $FF: synthetic field
                           private static SoftReference $callSiteArray;
                           // $FF: synthetic field
                           private static Class $class$java$lang$Object;
                           // $FF: synthetic field
                           private static Class $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4;

                           public {
                              CallSite[] var3 = $getCallSiteArray();
                           }

                           public Object doCall(Object it) {
                              CallSite[] var2 = $getCallSiteArray();
                              ScriptBytecodeAdapter.setGroovyObjectProperty("ClassNode - $expression.name", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "ClassNode");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("InnerClassNode - $expression.name", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "InnerClassNode");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("ConstructorNode - $expression.name", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "ConstructorNode");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("MethodNode - $expression.name", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "MethodNode");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("FieldNode - $expression.name : $expression.type", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "FieldNode");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("PropertyNode - ${expression.field?.name} : ${expression.field?.type}", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "PropertyNode");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("AnnotationNode - ${expression.classNode?.name}", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "AnnotationNode");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("Parameter - $expression.name", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "Parameter");
                              ScriptBytecodeAdapter.setGroovyObjectProperty("DynamicVariable - $expression.name", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), this, "DynamicVariable");
                              var2[0].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                                 // $FF: synthetic field
                                 private static ClassInfo $staticClassInfo;
                                 // $FF: synthetic field
                                 private static SoftReference $callSiteArray;
                                 // $FF: synthetic field
                                 private static Class $class$java$lang$Object;
                                 // $FF: synthetic field
                                 private static Class $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5;

                                 public {
                                    CallSite[] var3 = $getCallSiteArray();
                                 }

                                 public Object doCall(Object it) {
                                    CallSite[] var2 = $getCallSiteArray();
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("BlockStatement - (${expression.statements ? expression.statements.size() : 0})", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5(), this, "BlockStatement");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("ExpressionStatement - ${expression?.expression.getClass().simpleName}", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5(), this, "ExpressionStatement");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("ReturnStatement - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5(), this, "ReturnStatement");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("TryCatchStatement - ${expression.catchStatements?.size ?: 0} catch, ${expression.finallyStatement ? 1 : 0} finally", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5(), this, "TryCatchStatement");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("CatchStatement - $expression.exceptionType]", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5(), this, "CatchStatement");
                                    return "CatchStatement - $expression.exceptionType]";
                                 }

                                 public Object doCall() {
                                    CallSite[] var1 = $getCallSiteArray();
                                    return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                 }

                                 // $FF: synthetic method
                                 protected MetaClass $getStaticMetaClass() {
                                    if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5()) {
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
                                    return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5(), var0);
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
                                 private static Class $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5() {
                                    Class var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5 = class$("groovy.inspect.swingui.AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure5");
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
                              return var2[1].callCurrent(this, (Object)(new GeneratedClosure(this, this.getThisObject()) {
                                 // $FF: synthetic field
                                 private static ClassInfo $staticClassInfo;
                                 // $FF: synthetic field
                                 private static SoftReference $callSiteArray;
                                 // $FF: synthetic field
                                 private static Class $class$java$lang$Object;
                                 // $FF: synthetic field
                                 private static Class $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6;

                                 public {
                                    CallSite[] var3 = $getCallSiteArray();
                                 }

                                 public Object doCall(Object it) {
                                    CallSite[] var2 = $getCallSiteArray();
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("ConstructorCall - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "ConstructorCallExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Spread - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "SpreadExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("ArgumentList - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "ArgumentListExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("MethodCall - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "MethodCallExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("GString - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "GStringExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Attribute - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "AttributeExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Declaration - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "DeclarationExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Variable - $expression.name : $expression.type", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "VariableExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Constant - $expression.value : $expression.type", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "ConstantExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Binary - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "BinaryExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Class - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "ClassExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Boolean - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "BooleanExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Array - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "ArrayExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("List - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "ListExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Tuple - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "TupleExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Field - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "FieldExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Property - $expression.propertyAsString", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "PropertyExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Not - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "NotExpression");
                                    ScriptBytecodeAdapter.setGroovyObjectProperty("Cast - $expression.text", $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), this, "CastExpression");
                                    return "Cast - $expression.text";
                                 }

                                 public Object doCall() {
                                    CallSite[] var1 = $getCallSiteArray();
                                    return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                                 }

                                 // $FF: synthetic method
                                 protected MetaClass $getStaticMetaClass() {
                                    if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6()) {
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
                                    return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6(), var0);
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
                                 private static Class $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6() {
                                    Class var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6;
                                    if (var10000 == null) {
                                       var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6 = class$("groovy.inspect.swingui.AstBrowserProperties$_run_closure1_closure2_closure3_closure4_closure6");
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

                           public Object doCall() {
                              CallSite[] var1 = $getCallSiteArray();
                              return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                           }

                           // $FF: synthetic method
                           protected MetaClass $getStaticMetaClass() {
                              if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4()) {
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
                              var0[0] = "stmt";
                              var0[1] = "expr";
                              var0[2] = "doCall";
                           }

                           // $FF: synthetic method
                           private static CallSiteArray $createCallSiteArray() {
                              String[] var0 = new String[3];
                              $createCallSiteArray_1(var0);
                              return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4(), var0);
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
                           private static Class $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4() {
                              Class var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4;
                              if (var10000 == null) {
                                 var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3_closure4 = class$("groovy.inspect.swingui.AstBrowserProperties$_run_closure1_closure2_closure3_closure4");
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

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3()) {
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
                        var0[0] = "ast";
                        var0[1] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[2];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3() {
                        Class var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2_closure3 = class$("groovy.inspect.swingui.AstBrowserProperties$_run_closure1_closure2_closure3");
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

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2()) {
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
                  var0[0] = "groovy";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2() {
                  Class var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1_closure2 = class$("groovy.inspect.swingui.AstBrowserProperties$_run_closure1_closure2");
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

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1()) {
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
            var0[0] = "codehaus";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1() {
            Class var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstBrowserProperties$_run_closure1 = class$("groovy.inspect.swingui.AstBrowserProperties$_run_closure1");
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
      if (this.getClass() == $get$$class$groovy$inspect$swingui$AstBrowserProperties()) {
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
      Class var10000 = $get$$class$groovy$inspect$swingui$AstBrowserProperties();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$AstBrowserProperties(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$AstBrowserProperties(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$3$println() {
      super.println();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$3$print(Object var1) {
      super.print(var1);
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object[] var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public Object super$3$evaluate(String var1) {
      return super.evaluate(var1);
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
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Binding super$3$getBinding() {
      return super.getBinding();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setBinding(Binding var1) {
      super.setBinding(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$3$run(File var1, String[] var2) {
      super.run(var1, var2);
   }

   // $FF: synthetic method
   public Object super$3$evaluate(File var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$3$println(Object var1) {
      super.println(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "runScript";
      var0[1] = "org";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[2];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$AstBrowserProperties(), var0);
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
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
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
   private static Class $get$$class$groovy$inspect$swingui$AstBrowserProperties() {
      Class var10000 = $class$groovy$inspect$swingui$AstBrowserProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstBrowserProperties = class$("groovy.inspect.swingui.AstBrowserProperties");
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
