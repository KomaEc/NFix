package soot.dexpler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.jf.dexlib2.AnnotationVisibility;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.EnumEncodedValue;
import org.jf.dexlib2.iface.value.FieldEncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import soot.ArrayType;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.javaToJimple.IInitialResolver;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.DeprecatedTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.Host;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.toDex.SootToDexUtils;

public class DexAnnotation {
   private final Type ARRAY_TYPE = RefType.v("Array");
   private final SootClass clazz;
   private final IInitialResolver.Dependencies deps;

   DexAnnotation(SootClass clazz, IInitialResolver.Dependencies deps) {
      this.clazz = clazz;
      this.deps = deps;
   }

   public void handleClassAnnotation(ClassDef classDef) {
      Set<? extends Annotation> aSet = classDef.getAnnotations();
      if (aSet != null && !aSet.isEmpty()) {
         List<Tag> tags = this.handleAnnotation(aSet, classDef.getType());
         if (tags != null) {
            InnerClassAttribute ica = null;
            Iterator var5 = tags.iterator();

            while(true) {
               label114:
               while(true) {
                  Tag t;
                  do {
                     if (!var5.hasNext()) {
                        return;
                     }

                     t = (Tag)var5.next();
                  } while(t == null);

                  if (t instanceof InnerClassTag) {
                     if (ica == null) {
                        ica = (InnerClassAttribute)this.clazz.getTag("InnerClassAttribute");
                        if (ica == null) {
                           ica = new InnerClassAttribute();
                           this.clazz.addTag(ica);
                        }
                     }

                     ica.add((InnerClassTag)t);
                  } else if (!(t instanceof VisibilityAnnotationTag)) {
                     this.clazz.addTag(t);
                  } else {
                     VisibilityAnnotationTag vt = (VisibilityAnnotationTag)t;
                     Iterator var8 = vt.getAnnotations().iterator();

                     label112:
                     while(true) {
                        AnnotationTag a;
                        do {
                           if (!var8.hasNext()) {
                              if (vt.getVisibility() != 1) {
                                 this.clazz.addTag(vt);
                              }
                              continue label114;
                           }

                           a = (AnnotationTag)var8.next();
                        } while(!a.getType().equals("Ldalvik/annotation/AnnotationDefault;"));

                        Iterator var10 = a.getElems().iterator();

                        while(true) {
                           AnnotationElem ae;
                           do {
                              if (!var10.hasNext()) {
                                 continue label112;
                              }

                              ae = (AnnotationElem)var10.next();
                           } while(!(ae instanceof AnnotationAnnotationElem));

                           AnnotationAnnotationElem aae = (AnnotationAnnotationElem)ae;
                           AnnotationTag at = aae.getValue();
                           Map<String, AnnotationElem> defaults = new HashMap();
                           Iterator var15 = at.getElems().iterator();

                           while(var15.hasNext()) {
                              AnnotationElem aelem = (AnnotationElem)var15.next();
                              defaults.put(aelem.getName(), aelem);
                           }

                           var15 = this.clazz.getMethods().iterator();

                           AnnotationElem e;
                           while(var15.hasNext()) {
                              SootMethod sm = (SootMethod)var15.next();
                              String methodName = sm.getName();
                              if (defaults.containsKey(methodName)) {
                                 e = (AnnotationElem)defaults.get(methodName);
                                 Type annotationType = this.getSootType(e);
                                 boolean isCorrectType = false;
                                 if (annotationType == null) {
                                    isCorrectType = true;
                                 } else if (annotationType.equals(sm.getReturnType())) {
                                    isCorrectType = true;
                                 } else if (annotationType.equals(this.ARRAY_TYPE) && sm.getReturnType() instanceof ArrayType) {
                                    isCorrectType = true;
                                 }

                                 if (isCorrectType && sm.getParameterCount() == 0) {
                                    e.setName("default");
                                    AnnotationDefaultTag d = new AnnotationDefaultTag(e);
                                    sm.addTag(d);
                                    defaults.remove(sm.getName());
                                 }
                              }
                           }

                           var15 = defaults.entrySet().iterator();

                           while(var15.hasNext()) {
                              Entry<String, AnnotationElem> leftOverEntry = (Entry)var15.next();
                              SootMethod found = this.clazz.getMethodByNameUnsafe((String)leftOverEntry.getKey());
                              e = (AnnotationElem)leftOverEntry.getValue();
                              if (found != null) {
                                 e.setName("default");
                                 AnnotationDefaultTag d = new AnnotationDefaultTag(e);
                                 found.addTag(d);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private Type getSootType(AnnotationElem e) {
      Object annotationType;
      switch(e.getKind()) {
      case 'B':
      case 'C':
      case 'D':
      case 'F':
      case 'I':
      case 'J':
      case 'L':
      case 'S':
      case 'V':
      case 'Z':
         annotationType = Util.getType(String.valueOf(e.getKind()));
         break;
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'W':
      case 'X':
      case 'Y':
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      case 'a':
      case 'b':
      case 'd':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      default:
         annotationType = null;
         break;
      case '[':
         annotationType = this.ARRAY_TYPE;
         AnnotationArrayElem array = (AnnotationArrayElem)e;
         if (array.getNumValues() > 0) {
            AnnotationElem firstElement = array.getValueAt(0);
            Type type = this.getSootType(firstElement);
            if (type == null) {
               return null;
            }

            if (type.equals(this.ARRAY_TYPE)) {
               return this.ARRAY_TYPE;
            }

            return ArrayType.v(type, 1);
         }
         break;
      case 'c':
         annotationType = RefType.v("java.lang.Class");
         break;
      case 'e':
         AnnotationEnumElem enumElem = (AnnotationEnumElem)e;
         annotationType = Util.getType(enumElem.getTypeName());
         break;
      case 's':
         annotationType = RefType.v("java.lang.String");
      }

      return (Type)annotationType;
   }

   public void handleFieldAnnotation(Host h, Field f) {
      Set<? extends Annotation> aSet = f.getAnnotations();
      if (aSet != null && !aSet.isEmpty()) {
         List<Tag> tags = this.handleAnnotation(aSet, (String)null);
         if (tags != null) {
            Iterator var5 = tags.iterator();

            while(var5.hasNext()) {
               Tag t = (Tag)var5.next();
               if (t != null) {
                  h.addTag(t);
               }
            }
         }
      }

   }

   public void handleMethodAnnotation(Host h, Method method) {
      Set<? extends Annotation> aSet = method.getAnnotations();
      if (aSet != null && !aSet.isEmpty()) {
         List<Tag> tags = this.handleAnnotation(aSet, (String)null);
         if (tags != null) {
            Iterator var5 = tags.iterator();

            while(var5.hasNext()) {
               Tag t = (Tag)var5.next();
               if (t != null) {
                  h.addTag(t);
               }
            }
         }
      }

      String[] parameterNames = null;
      int i = 0;

      for(Iterator var22 = method.getParameters().iterator(); var22.hasNext(); ++i) {
         MethodParameter p = (MethodParameter)var22.next();
         String name = p.getName();
         if (name != null) {
            parameterNames = new String[method.getParameters().size()];
            parameterNames[i] = name;
         }
      }

      if (parameterNames != null) {
         h.addTag(new ParamNamesTag(parameterNames));
      }

      boolean doParam = false;
      List<? extends MethodParameter> parameters = method.getParameters();
      Iterator var25 = parameters.iterator();

      while(var25.hasNext()) {
         MethodParameter p = (MethodParameter)var25.next();
         if (p.getAnnotations().size() > 0) {
            doParam = true;
            break;
         }
      }

      if (doParam) {
         VisibilityParameterAnnotationTag tag = new VisibilityParameterAnnotationTag(parameters.size(), 0);
         Iterator var27 = parameters.iterator();

         while(true) {
            label82:
            while(var27.hasNext()) {
               MethodParameter p = (MethodParameter)var27.next();
               List<Tag> tags = this.handleAnnotation(p.getAnnotations(), (String)null);
               if (tags == null) {
                  tag.addVisibilityAnnotation((VisibilityAnnotationTag)null);
               } else {
                  VisibilityAnnotationTag paramVat = new VisibilityAnnotationTag(0);
                  tag.addVisibilityAnnotation(paramVat);
                  Iterator var13 = tags.iterator();

                  while(true) {
                     Tag t;
                     do {
                        if (!var13.hasNext()) {
                           continue label82;
                        }

                        t = (Tag)var13.next();
                     } while(t == null);

                     AnnotationTag vat = null;
                     if (t instanceof VisibilityAnnotationTag) {
                        vat = (AnnotationTag)((VisibilityAnnotationTag)t).getAnnotations().get(0);
                     } else if (t instanceof DeprecatedTag) {
                        vat = new AnnotationTag("Ljava/lang/Deprecated;");
                     } else {
                        if (!(t instanceof SignatureTag)) {
                           throw new RuntimeException("error: unhandled tag for parameter annotation in method " + h + " (" + t + ").");
                        }

                        SignatureTag sig = (SignatureTag)t;
                        ArrayList<AnnotationElem> sigElements = new ArrayList();
                        Iterator var18 = SootToDexUtils.splitSignature(sig.getSignature()).iterator();

                        while(var18.hasNext()) {
                           String s = (String)var18.next();
                           sigElements.add(new AnnotationStringElem(s, 's', "value"));
                        }

                        AnnotationElem elem = new AnnotationArrayElem(sigElements, '[', "value");
                        vat = new AnnotationTag("Ldalvik/annotation/Signature;", Collections.singleton(elem));
                     }

                     paramVat.addAnnotation(vat);
                  }
               }
            }

            if (tag.getVisibilityAnnotations().size() > 0) {
               h.addTag(tag);
            }
            break;
         }
      }

   }

   private List<Tag> handleAnnotation(Set<? extends Annotation> annotations, String classType) {
      if (annotations != null && annotations.size() != 0) {
         List<Tag> tags = new ArrayList();
         VisibilityAnnotationTag[] vatg = new VisibilityAnnotationTag[3];
         Iterator var5 = annotations.iterator();

         while(true) {
            int v;
            label200:
            while(var5.hasNext()) {
               Annotation a = (Annotation)var5.next();
               v = this.getVisibility(a.getVisibility());
               Tag t = null;
               Type atype = DexType.toSoot(a.getType());
               String atypes = atype.toString();
               int eSize = a.getElements().size();
               if (atypes.equals("dalvik.annotation.AnnotationDefault")) {
                  if (eSize != 1) {
                     throw new RuntimeException("error: expected 1 element for annotation Default. Got " + eSize + " instead.");
                  }

                  AnnotationElem e = (AnnotationElem)this.getElements(a.getElements()).get(0);
                  AnnotationTag adt = new AnnotationTag(a.getType());
                  adt.addElem(e);
                  if (vatg[v] == null) {
                     vatg[v] = new VisibilityAnnotationTag(v);
                  }

                  vatg[v].addAnnotation(adt);
               } else {
                  String outerClass;
                  if (atypes.equals("dalvik.annotation.EnclosingClass")) {
                     if (eSize != 1) {
                        throw new RuntimeException("error: expected 1 element for annotation EnclosingClass. Got " + eSize + " instead.");
                     }

                     Iterator var32 = a.getElements().iterator();

                     do {
                        if (!var32.hasNext()) {
                           continue label200;
                        }

                        AnnotationElement elem = (AnnotationElement)var32.next();
                        outerClass = ((TypeEncodedValue)elem.getValue()).getValue();
                        outerClass = Util.dottedClassName(outerClass);
                        if (outerClass.equals(this.clazz.getName())) {
                           if (outerClass.contains("$-")) {
                              outerClass = outerClass.substring(0, outerClass.indexOf("$-"));
                           } else if (outerClass.contains("$")) {
                              outerClass = outerClass.substring(0, outerClass.lastIndexOf("$"));
                           }
                        }

                        this.deps.typesToSignature.add(RefType.v(outerClass));
                        this.clazz.setOuterClass(SootResolver.v().makeClassRef(outerClass));
                     } while($assertionsDisabled || this.clazz.getOuterClass() != this.clazz);

                     throw new AssertionError();
                  }

                  String innerClass;
                  String outerClass;
                  String name;
                  if (atypes.equals("dalvik.annotation.EnclosingMethod")) {
                     if (eSize == 0) {
                        continue;
                     }

                     if (eSize != 1) {
                        throw new RuntimeException("error: expected 1 element for annotation EnclosingMethod. Got " + eSize + " instead.");
                     }

                     AnnotationStringElem e = (AnnotationStringElem)this.getElements(a.getElements()).get(0);
                     String[] split1 = e.getValue().split("\\ \\|");
                     outerClass = split1[0];
                     String methodString = split1[1];
                     innerClass = split1[2];
                     outerClass = split1[3];
                     name = "(" + innerClass + ")" + outerClass;
                     t = new EnclosingMethodTag(outerClass, methodString, name);
                     String outerClass = outerClass.replace("/", ".");
                     this.deps.typesToSignature.add(RefType.v(outerClass));
                     this.clazz.setOuterClass(SootResolver.v().makeClassRef(outerClass));

                     assert this.clazz.getOuterClass() != this.clazz;
                  } else {
                     String sig;
                     Iterator var30;
                     AnnotationElem ae;
                     if (atypes.equals("dalvik.annotation.InnerClass")) {
                        int accessFlags = -1;
                        sig = null;
                        var30 = this.getElements(a.getElements()).iterator();

                        while(true) {
                           while(var30.hasNext()) {
                              ae = (AnnotationElem)var30.next();
                              if (ae instanceof AnnotationIntElem && ae.getName().equals("accessFlags")) {
                                 accessFlags = ((AnnotationIntElem)ae).getValue();
                              } else {
                                 if (!(ae instanceof AnnotationStringElem) || !ae.getName().equals("name")) {
                                    throw new RuntimeException("Unexpected inner class annotation element");
                                 }

                                 sig = ((AnnotationStringElem)ae).getValue();
                              }
                           }

                           if (this.clazz.hasOuterClass()) {
                              outerClass = this.clazz.getOuterClass().getName();
                           } else if (classType.contains("$-")) {
                              outerClass = classType.substring(0, classType.indexOf("$-"));
                              if (Util.isByteCodeClassName(classType)) {
                                 outerClass = outerClass + ";";
                              }
                           } else if (classType.contains("$")) {
                              outerClass = classType.substring(0, classType.lastIndexOf("$")) + ";";
                              if (Util.isByteCodeClassName(classType)) {
                                 outerClass = outerClass + ";";
                              }
                           } else {
                              outerClass = null;
                           }

                           Tag innerTag = new InnerClassTag(DexType.toSootICAT(classType), outerClass == null ? null : DexType.toSootICAT(outerClass), sig, accessFlags);
                           tags.add(innerTag);
                           if (outerClass != null && !this.clazz.hasOuterClass()) {
                              innerClass = Util.dottedClassName(outerClass);
                              this.deps.typesToSignature.add(RefType.v(innerClass));
                              this.clazz.setOuterClass(SootResolver.v().makeClassRef(innerClass));

                              assert this.clazz.getOuterClass() != this.clazz;
                           }
                           continue label200;
                        }
                     }

                     AnnotationArrayElem e;
                     Iterator var31;
                     AnnotationElem e;
                     if (atypes.equals("dalvik.annotation.MemberClasses")) {
                        e = (AnnotationArrayElem)this.getElements(a.getElements()).get(0);
                        var31 = e.getValues().iterator();

                        while(true) {
                           if (!var31.hasNext()) {
                              continue label200;
                           }

                           e = (AnnotationElem)var31.next();
                           AnnotationClassElem c = (AnnotationClassElem)e;
                           innerClass = c.getDesc();
                           int i;
                           if (innerClass.contains("$-")) {
                              i = innerClass.indexOf("$-");
                              outerClass = innerClass.substring(0, i);
                              name = innerClass.substring(i + 2).replaceAll(";$", "");
                           } else if (innerClass.contains("$")) {
                              i = innerClass.lastIndexOf("$");
                              outerClass = innerClass.substring(0, i);
                              name = innerClass.substring(i + 1).replaceAll(";$", "");
                           } else {
                              outerClass = null;
                              name = null;
                           }

                           if (name != null && name.matches("^\\d*$")) {
                              name = null;
                           }

                           int accessFlags = 0;
                           Tag innerTag = new InnerClassTag(DexType.toSootICAT(innerClass), outerClass == null ? null : DexType.toSootICAT(outerClass), name, accessFlags);
                           tags.add(innerTag);
                        }
                     }

                     if (atypes.equals("dalvik.annotation.Signature")) {
                        if (eSize != 1) {
                           throw new RuntimeException("error: expected 1 element for annotation Signature. Got " + eSize + " instead.");
                        }

                        e = (AnnotationArrayElem)this.getElements(a.getElements()).get(0);
                        sig = "";

                        AnnotationStringElem s;
                        for(var30 = e.getValues().iterator(); var30.hasNext(); sig = sig + s.getValue()) {
                           ae = (AnnotationElem)var30.next();
                           s = (AnnotationStringElem)ae;
                        }

                        t = new SignatureTag(sig);
                     } else {
                        if (atypes.equals("dalvik.annotation.Throws")) {
                           continue;
                        }

                        AnnotationTag adt;
                        if (atypes.equals("java.lang.Deprecated")) {
                           if (eSize != 0) {
                              throw new RuntimeException("error: expected 1 element for annotation Deprecated. Got " + eSize + " instead.");
                           }

                           t = new DeprecatedTag();
                           adt = new AnnotationTag("Ljava/lang/Deprecated;");
                           if (vatg[v] == null) {
                              vatg[v] = new VisibilityAnnotationTag(v);
                           }

                           vatg[v].addAnnotation(adt);
                        } else {
                           if (vatg[v] == null) {
                              vatg[v] = new VisibilityAnnotationTag(v);
                           }

                           adt = new AnnotationTag(a.getType());
                           var31 = this.getElements(a.getElements()).iterator();

                           while(var31.hasNext()) {
                              e = (AnnotationElem)var31.next();
                              adt.addElem(e);
                           }

                           vatg[v].addAnnotation(adt);
                        }
                     }
                  }
               }

               tags.add(t);
            }

            VisibilityAnnotationTag[] var21 = vatg;
            int var22 = vatg.length;

            for(v = 0; v < var22; ++v) {
               VisibilityAnnotationTag vat = var21[v];
               if (vat != null) {
                  tags.add(vat);
               }
            }

            return tags;
         }
      } else {
         return null;
      }
   }

   private ArrayList<AnnotationElem> getElements(Set<? extends AnnotationElement> set) {
      ArrayList<AnnotationElem> aelemList = new ArrayList();
      Iterator var3 = set.iterator();

      while(var3.hasNext()) {
         AnnotationElement ae = (AnnotationElement)var3.next();
         List<AnnotationElem> eList = this.handleAnnotationElement(ae, Collections.singletonList(ae.getValue()));
         if (eList != null) {
            aelemList.addAll(eList);
         }
      }

      return aelemList;
   }

   private ArrayList<AnnotationElem> handleAnnotationElement(AnnotationElement ae, List<? extends EncodedValue> evList) {
      ArrayList<AnnotationElem> aelemList = new ArrayList();
      Iterator var4 = evList.iterator();

      while(var4.hasNext()) {
         Object elem;
         EncodedValue ev = (EncodedValue)var4.next();
         int type = ev.getValueType();
         elem = null;
         Iterator var14;
         FieldReference fr;
         String className;
         label64:
         switch(type) {
         case 0:
            ByteEncodedValue v = (ByteEncodedValue)ev;
            elem = new AnnotationIntElem(v.getValue(), 'B', ae.getName());
            break;
         case 1:
         case 5:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         default:
            throw new RuntimeException("Unknown annotation element 0x" + Integer.toHexString(type));
         case 2:
            ShortEncodedValue v = (ShortEncodedValue)ev;
            elem = new AnnotationIntElem(v.getValue(), 'S', ae.getName());
            break;
         case 3:
            CharEncodedValue v = (CharEncodedValue)ev;
            elem = new AnnotationIntElem(v.getValue(), 'C', ae.getName());
            break;
         case 4:
            IntEncodedValue v = (IntEncodedValue)ev;
            elem = new AnnotationIntElem(v.getValue(), 'I', ae.getName());
            break;
         case 6:
            LongEncodedValue v = (LongEncodedValue)ev;
            elem = new AnnotationLongElem(v.getValue(), 'J', ae.getName());
            break;
         case 16:
            FloatEncodedValue v = (FloatEncodedValue)ev;
            elem = new AnnotationFloatElem(v.getValue(), 'F', ae.getName());
            break;
         case 17:
            DoubleEncodedValue v = (DoubleEncodedValue)ev;
            elem = new AnnotationDoubleElem(v.getValue(), 'D', ae.getName());
            break;
         case 23:
            StringEncodedValue v = (StringEncodedValue)ev;
            elem = new AnnotationStringElem(v.getValue(), 's', ae.getName());
            break;
         case 24:
            TypeEncodedValue v = (TypeEncodedValue)ev;
            elem = new AnnotationClassElem(v.getValue(), 'c', ae.getName());
            break;
         case 25:
            FieldEncodedValue v = (FieldEncodedValue)ev;
            fr = v.getValue();
            className = "";
            className = className + DexType.toSootAT(fr.getDefiningClass()) + ": ";
            className = className + DexType.toSootAT(fr.getType()) + " ";
            className = className + fr.getName();
            elem = new AnnotationStringElem(className, 'f', ae.getName());
            break;
         case 26:
            MethodEncodedValue v = (MethodEncodedValue)ev;
            MethodReference mr = v.getValue();
            className = DexType.toSootICAT(mr.getDefiningClass());
            String returnType = DexType.toSootAT(mr.getReturnType());
            String methodName = mr.getName();
            String parameters = "";

            CharSequence p;
            for(var14 = mr.getParameterTypes().iterator(); var14.hasNext(); parameters = parameters + DexType.toSootAT(p.toString())) {
               p = (CharSequence)var14.next();
            }

            String mSig = className + " |" + methodName + " |" + parameters + " |" + returnType;
            elem = new AnnotationStringElem(mSig, 'M', ae.getName());
            break;
         case 27:
            EnumEncodedValue v = (EnumEncodedValue)ev;
            fr = v.getValue();
            elem = new AnnotationEnumElem(DexType.toSootAT(fr.getType()).toString(), fr.getName(), 'e', ae.getName());
            break;
         case 28:
            ArrayEncodedValue v = (ArrayEncodedValue)ev;
            ArrayList<AnnotationElem> l = this.handleAnnotationElement(ae, v.getValue());
            if (l != null) {
               elem = new AnnotationArrayElem(l, '[', ae.getName());
            }
            break;
         case 29:
            AnnotationEncodedValue v = (AnnotationEncodedValue)ev;
            AnnotationTag t = new AnnotationTag(DexType.toSootAT(v.getType()).toString());
            Iterator var10 = v.getElements().iterator();

            while(true) {
               ArrayList aList;
               do {
                  if (!var10.hasNext()) {
                     elem = new AnnotationAnnotationElem(t, '@', ae.getName());
                     break label64;
                  }

                  AnnotationElement newElem = (AnnotationElement)var10.next();
                  List<EncodedValue> l = new ArrayList();
                  l.add(newElem.getValue());
                  aList = this.handleAnnotationElement(newElem, l);
               } while(aList == null);

               var14 = aList.iterator();

               while(var14.hasNext()) {
                  AnnotationElem e = (AnnotationElem)var14.next();
                  t.addElem(e);
               }
            }
         case 30:
            elem = new AnnotationStringElem((String)null, 'N', ae.getName());
            break;
         case 31:
            BooleanEncodedValue v = (BooleanEncodedValue)ev;
            elem = new AnnotationBooleanElem(v.getValue(), 'Z', ae.getName());
         }

         if (elem != null) {
            aelemList.add(elem);
         }
      }

      return aelemList;
   }

   private int getVisibility(int visibility) {
      if ("runtime".equals(AnnotationVisibility.getVisibility(visibility))) {
         return 0;
      } else if ("system".equals(AnnotationVisibility.getVisibility(visibility))) {
         return 1;
      } else if ("build".equals(AnnotationVisibility.getVisibility(visibility))) {
         return 2;
      } else {
         throw new RuntimeException("error: unknown annotation visibility: '" + visibility + "'");
      }
   }

   class MyAnnotations {
      List<AnnotationTag> annotationList = new ArrayList();
      List<Integer> visibilityList = new ArrayList();

      public void add(AnnotationTag a, int visibility) {
         this.annotationList.add(a);
         this.visibilityList.add(new Integer(visibility));
      }

      public List<AnnotationTag> getAnnotations() {
         return this.annotationList;
      }

      public List<Integer> getVisibilityList() {
         return this.visibilityList;
      }
   }
}
