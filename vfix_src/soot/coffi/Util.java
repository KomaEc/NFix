package soot.coffi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.UnknownType;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
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
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.GenericAttribute;
import soot.tagkit.Host;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.SyntheticTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;

public class Util {
   private static final Logger logger = LoggerFactory.getLogger(Util.class);
   private cp_info[] activeConstantPool = null;
   private LocalVariableTable_attribute activeVariableTable;
   private Map<String, Map<Integer, Local>> nameToIndexToLocal;
   private boolean useFaithfulNaming = false;
   private final ArrayList<Type> conversionTypes = new ArrayList();
   private final Map<String, Type[]> cache = new HashMap();
   int nextEasyNameIndex;

   public Util(Singletons.Global g) {
   }

   public static Util v() {
      return G.v().soot_coffi_Util();
   }

   public void bodySetup(LocalVariableTable_attribute la, LocalVariableTypeTable_attribute lt, cp_info[] ca) {
      this.activeVariableTable = la;
      this.activeConstantPool = ca;
      this.nameToIndexToLocal = null;
   }

   public void setFaithfulNaming(boolean v) {
      this.useFaithfulNaming = v;
   }

   public boolean isUsingFaithfulNaming() {
      return this.useFaithfulNaming;
   }

   public void resolveFromClassFile(SootClass aClass, InputStream is, String filePath, Collection<Type> references) {
      SootClass bclass = aClass;
      String className = aClass.getName();
      ClassFile coffiClass = new ClassFile(className);
      boolean success = coffiClass.loadClassFile(is);
      if (!success) {
         if (!Scene.v().allowsPhantomRefs()) {
            throw new RuntimeException("Could not load classfile: " + aClass.getName());
         } else {
            logger.warn("" + className + " is a phantom class!");
            aClass.setPhantomClass();
         }
      } else {
         CONSTANT_Class_info c = (CONSTANT_Class_info)coffiClass.constant_pool[coffiClass.this_class];
         String name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[c.name_index])).convert();
         name = name.replace('/', '.');
         if (!name.equals(aClass.getName())) {
            throw new RuntimeException("Error: class " + name + " read in from a classfile in which " + aClass.getName() + " was expected.");
         } else {
            aClass.setModifiers(coffiClass.access_flags & -33);
            String generic_sig;
            if (coffiClass.super_class != 0) {
               CONSTANT_Class_info c = (CONSTANT_Class_info)coffiClass.constant_pool[coffiClass.super_class];
               generic_sig = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[c.name_index])).convert();
               generic_sig = generic_sig.replace('/', '.');
               references.add(RefType.v(generic_sig));
               aClass.setSuperclass(SootResolver.v().makeClassRef(generic_sig));
            }

            int i;
            for(i = 0; i < coffiClass.interfaces_count; ++i) {
               c = (CONSTANT_Class_info)coffiClass.constant_pool[coffiClass.interfaces[i]];
               name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[c.name_index])).convert();
               name = name.replace('/', '.');
               references.add(RefType.v(name));
               SootClass interfaceClass = SootResolver.v().makeClassRef(name);
               interfaceClass.setModifiers(interfaceClass.getModifiers() | 512);
               bclass.addInterface(interfaceClass);
            }

            Type returnType;
            int j;
            String generic_sig;
            String methodDescriptor;
            for(i = 0; i < coffiClass.fields_count; ++i) {
               field_info fieldInfo = coffiClass.fields[i];
               name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[fieldInfo.name_index])).convert();
               methodDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[fieldInfo.descriptor_index])).convert();
               int modifiers = fieldInfo.access_flags;
               returnType = this.jimpleTypeOfFieldDescriptor(methodDescriptor);
               SootField field = Scene.v().makeSootField(name, returnType, modifiers);
               bclass.addField(field);
               references.add(returnType);

               for(j = 0; j < fieldInfo.attributes_count; ++j) {
                  if (fieldInfo.attributes[j] instanceof ConstantValue_attribute) {
                     ConstantValue_attribute attr = (ConstantValue_attribute)fieldInfo.attributes[j];
                     cp_info cval = coffiClass.constant_pool[attr.constantvalue_index];
                     Object tag;
                     switch(cval.tag) {
                     case 3:
                        tag = new IntegerConstantValueTag((int)((CONSTANT_Integer_info)cval).bytes);
                        break;
                     case 4:
                        tag = new FloatConstantValueTag(((CONSTANT_Float_info)cval).convert());
                        break;
                     case 5:
                        CONSTANT_Long_info lcval = (CONSTANT_Long_info)cval;
                        tag = new LongConstantValueTag((lcval.high << 32) + lcval.low);
                        break;
                     case 6:
                        CONSTANT_Double_info dcval = (CONSTANT_Double_info)cval;
                        tag = new DoubleConstantValueTag(dcval.convert());
                        break;
                     case 7:
                     default:
                        throw new RuntimeException("unexpected ConstantValue: " + cval);
                     case 8:
                        CONSTANT_String_info scval = (CONSTANT_String_info)cval;
                        CONSTANT_Utf8_info ucval = (CONSTANT_Utf8_info)coffiClass.constant_pool[scval.string_index];
                        tag = new StringConstantValueTag(ucval.convert());
                     }

                     field.addTag((Tag)tag);
                  } else if (fieldInfo.attributes[j] instanceof Synthetic_attribute) {
                     field.addTag(new SyntheticTag());
                  } else if (fieldInfo.attributes[j] instanceof Deprecated_attribute) {
                     field.addTag(new DeprecatedTag());
                  } else if (fieldInfo.attributes[j] instanceof Signature_attribute) {
                     String generic_sig = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[((Signature_attribute)fieldInfo.attributes[j]).signature_index])).convert();
                     field.addTag(new SignatureTag(generic_sig));
                  } else if (!(fieldInfo.attributes[j] instanceof RuntimeVisibleAnnotations_attribute) && !(fieldInfo.attributes[j] instanceof RuntimeInvisibleAnnotations_attribute)) {
                     if (fieldInfo.attributes[j] instanceof Generic_attribute) {
                        Generic_attribute attr = (Generic_attribute)fieldInfo.attributes[j];
                        generic_sig = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[attr.attribute_name])).convert();
                        field.addTag(new GenericAttribute(generic_sig, attr.info));
                     }
                  } else {
                     this.addAnnotationVisibilityAttribute(field, fieldInfo.attributes[j], coffiClass, references);
                  }
               }
            }

            method_info methodInfo;
            for(i = 0; i < coffiClass.methods_count; ++i) {
               methodInfo = coffiClass.methods[i];
               if (coffiClass.constant_pool[methodInfo.name_index] == null) {
                  logger.debug("method index: " + methodInfo.toName(coffiClass.constant_pool));
                  throw new RuntimeException("method has no name");
               }

               name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[methodInfo.name_index])).convert();
               methodDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[methodInfo.descriptor_index])).convert();
               Type[] types = this.jimpleTypesOfFieldOrMethodDescriptor(methodDescriptor);
               List<Type> parameterTypes = new ArrayList();

               for(j = 0; j < types.length - 1; ++j) {
                  references.add(types[j]);
                  parameterTypes.add(types[j]);
               }

               returnType = types[types.length - 1];
               references.add(returnType);
               int modifiers = methodInfo.access_flags;
               SootMethod method = Scene.v().makeSootMethod(name, parameterTypes, returnType, modifiers);
               bclass.addMethod(method);
               methodInfo.jmethod = method;

               int j;
               String name;
               for(j = 0; j < methodInfo.attributes_count; ++j) {
                  if (methodInfo.attributes[j] instanceof Exception_attribute) {
                     Exception_attribute exceptions = (Exception_attribute)methodInfo.attributes[j];

                     for(int k = 0; k < exceptions.number_of_exceptions; ++k) {
                        CONSTANT_Class_info c = (CONSTANT_Class_info)coffiClass.constant_pool[exceptions.exception_index_table[k]];
                        String exceptionName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[c.name_index])).convert();
                        exceptionName = exceptionName.replace('/', '.');
                        references.add(RefType.v(exceptionName));
                        method.addExceptionIfAbsent(SootResolver.v().makeClassRef(exceptionName));
                     }
                  } else if (methodInfo.attributes[j] instanceof Synthetic_attribute) {
                     method.addTag(new SyntheticTag());
                  } else if (methodInfo.attributes[j] instanceof Deprecated_attribute) {
                     method.addTag(new DeprecatedTag());
                  } else if (methodInfo.attributes[j] instanceof Signature_attribute) {
                     generic_sig = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[((Signature_attribute)methodInfo.attributes[j]).signature_index])).convert();
                     method.addTag(new SignatureTag(generic_sig));
                  } else if (!(methodInfo.attributes[j] instanceof RuntimeVisibleAnnotations_attribute) && !(methodInfo.attributes[j] instanceof RuntimeInvisibleAnnotations_attribute)) {
                     if (!(methodInfo.attributes[j] instanceof RuntimeVisibleParameterAnnotations_attribute) && !(methodInfo.attributes[j] instanceof RuntimeInvisibleParameterAnnotations_attribute)) {
                        if (methodInfo.attributes[j] instanceof AnnotationDefault_attribute) {
                           AnnotationDefault_attribute attr = (AnnotationDefault_attribute)methodInfo.attributes[j];
                           element_value[] input = new element_value[]{attr.default_value};
                           ArrayList<AnnotationElem> list = this.createElementTags(1, coffiClass, input);
                           method.addTag(new AnnotationDefaultTag((AnnotationElem)list.get(0)));
                        } else if (methodInfo.attributes[j] instanceof Generic_attribute) {
                           Generic_attribute attr = (Generic_attribute)methodInfo.attributes[j];
                           name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[attr.attribute_name])).convert();
                           method.addTag(new GenericAttribute(name, attr.info));
                        }
                     } else {
                        this.addAnnotationVisibilityParameterAttribute(method, methodInfo.attributes[j], coffiClass, references);
                     }
                  } else {
                     this.addAnnotationVisibilityAttribute(method, methodInfo.attributes[j], coffiClass, references);
                  }
               }

               for(j = 0; j < coffiClass.constant_pool_count; ++j) {
                  if (coffiClass.constant_pool[j] instanceof CONSTANT_Class_info) {
                     CONSTANT_Class_info c = (CONSTANT_Class_info)coffiClass.constant_pool[j];
                     name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[c.name_index])).convert();
                     String name = name.replace('/', '.');
                     if (name.startsWith("[")) {
                        references.add(this.jimpleTypeOfFieldDescriptor(name));
                     } else {
                        references.add(RefType.v(name));
                     }
                  }

                  if (coffiClass.constant_pool[j] instanceof CONSTANT_Fieldref_info || coffiClass.constant_pool[j] instanceof CONSTANT_Methodref_info || coffiClass.constant_pool[j] instanceof CONSTANT_InterfaceMethodref_info) {
                     Type[] types = this.jimpleTypesOfFieldOrMethodDescriptor(cp_info.getTypeDescr(coffiClass.constant_pool, j));
                     Type[] var62 = types;
                     int var60 = types.length;

                     for(int var61 = 0; var61 < var60; ++var61) {
                        Type element = var62[var61];
                        references.add(element);
                     }
                  }
               }
            }

            for(i = 0; i < coffiClass.methods_count; ++i) {
               methodInfo = coffiClass.methods[i];
               methodInfo.jmethod.setSource(new CoffiMethodSource(coffiClass, methodInfo));
            }

            for(i = 0; i < coffiClass.attributes_count; ++i) {
               if (coffiClass.attributes[i] instanceof SourceFile_attribute) {
                  SourceFile_attribute attr = (SourceFile_attribute)coffiClass.attributes[i];
                  name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[attr.sourcefile_index])).convert();
                  if (name.indexOf(32) >= 0) {
                     logger.debug("Warning: Class " + className + " has invalid SourceFile attribute (will be ignored).");
                  } else {
                     bclass.addTag(new SourceFileTag(name, filePath));
                  }
               } else {
                  String method_name;
                  String method_sig;
                  if (coffiClass.attributes[i] instanceof InnerClasses_attribute) {
                     InnerClasses_attribute attr = (InnerClasses_attribute)coffiClass.attributes[i];

                     for(int j = 0; j < attr.inner_classes_length; ++j) {
                        inner_class_entry e = attr.inner_classes[j];
                        method_name = null;
                        method_sig = null;
                        String name = null;
                        if (e.inner_class_index != 0) {
                           method_name = ((CONSTANT_Utf8_info)coffiClass.constant_pool[((CONSTANT_Class_info)coffiClass.constant_pool[e.inner_class_index]).name_index]).convert();
                        }

                        if (e.outer_class_index != 0) {
                           method_sig = ((CONSTANT_Utf8_info)coffiClass.constant_pool[((CONSTANT_Class_info)coffiClass.constant_pool[e.outer_class_index]).name_index]).convert();
                        }

                        if (e.name_index != 0) {
                           name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[e.name_index])).convert();
                        }

                        bclass.addTag(new InnerClassTag(method_name, method_sig, name, e.access_flags));
                     }
                  } else if (coffiClass.attributes[i] instanceof Synthetic_attribute) {
                     bclass.addTag(new SyntheticTag());
                  } else if (coffiClass.attributes[i] instanceof Deprecated_attribute) {
                     bclass.addTag(new DeprecatedTag());
                  } else if (coffiClass.attributes[i] instanceof Signature_attribute) {
                     generic_sig = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[((Signature_attribute)coffiClass.attributes[i]).signature_index])).convert();
                     bclass.addTag(new SignatureTag(generic_sig));
                  } else if (coffiClass.attributes[i] instanceof EnclosingMethod_attribute) {
                     EnclosingMethod_attribute attr = (EnclosingMethod_attribute)coffiClass.attributes[i];
                     name = ((CONSTANT_Utf8_info)coffiClass.constant_pool[((CONSTANT_Class_info)coffiClass.constant_pool[attr.class_index]).name_index]).convert();
                     CONSTANT_NameAndType_info info = (CONSTANT_NameAndType_info)coffiClass.constant_pool[attr.method_index];
                     method_name = "";
                     method_sig = "";
                     if (info != null) {
                        method_name = ((CONSTANT_Utf8_info)coffiClass.constant_pool[info.name_index]).convert();
                        method_sig = ((CONSTANT_Utf8_info)coffiClass.constant_pool[info.descriptor_index]).convert();
                     }

                     bclass.addTag(new EnclosingMethodTag(name, method_name, method_sig));
                  } else if (!(coffiClass.attributes[i] instanceof RuntimeVisibleAnnotations_attribute) && !(coffiClass.attributes[i] instanceof RuntimeInvisibleAnnotations_attribute)) {
                     if (coffiClass.attributes[i] instanceof Generic_attribute) {
                        Generic_attribute attr = (Generic_attribute)coffiClass.attributes[i];
                        name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)coffiClass.constant_pool[attr.attribute_name])).convert();
                        bclass.addTag(new GenericAttribute(name, attr.info));
                     }
                  } else {
                     this.addAnnotationVisibilityAttribute(bclass, coffiClass.attributes[i], coffiClass, references);
                  }
               }
            }

         }
      }
   }

   Type jimpleReturnTypeOfMethodDescriptor(String descriptor) {
      Type[] types = this.jimpleTypesOfFieldOrMethodDescriptor(descriptor);
      return types[types.length - 1];
   }

   public Type[] jimpleTypesOfFieldOrMethodDescriptor(String descriptor) {
      Type[] ret = null;
      synchronized(this.cache) {
         ret = (Type[])this.cache.get(descriptor);
      }

      if (ret != null) {
         return ret;
      } else {
         char[] d = descriptor.toCharArray();
         int p = 0;
         ArrayList conversionTypes = new ArrayList();

         label85:
         while(p < d.length) {
            boolean isArray = false;
            int numDimensions = 0;
            Object baseType = null;

            while(true) {
               if (p < d.length) {
                  switch(d[p]) {
                  case '(':
                  case ')':
                     ++p;
                     continue label85;
                  case '*':
                  case '+':
                  case ',':
                  case '-':
                  case '.':
                  case '/':
                  case '0':
                  case '1':
                  case '2':
                  case '3':
                  case '4':
                  case '5':
                  case '6':
                  case '7':
                  case '8':
                  case '9':
                  case ':':
                  case ';':
                  case '<':
                  case '=':
                  case '>':
                  case '?':
                  case '@':
                  case 'A':
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
                  default:
                     throw new RuntimeException("Unknown field type!");
                  case 'B':
                     baseType = ByteType.v();
                     ++p;
                     break;
                  case 'C':
                     baseType = CharType.v();
                     ++p;
                     break;
                  case 'D':
                     baseType = DoubleType.v();
                     ++p;
                     break;
                  case 'F':
                     baseType = FloatType.v();
                     ++p;
                     break;
                  case 'I':
                     baseType = IntType.v();
                     ++p;
                     break;
                  case 'J':
                     baseType = LongType.v();
                     ++p;
                     break;
                  case 'L':
                     int index;
                     for(index = p + 1; index < d.length && d[index] != ';'; ++index) {
                        if (d[index] == '/') {
                           d[index] = '.';
                        }
                     }

                     if (index >= d.length) {
                        throw new RuntimeException("Class reference has no ending ;");
                     }

                     String className = new String(d, p + 1, index - p - 1);
                     baseType = RefType.v(className);
                     p = index + 1;
                     break;
                  case 'S':
                     baseType = ShortType.v();
                     ++p;
                     break;
                  case 'V':
                     baseType = VoidType.v();
                     ++p;
                     break;
                  case 'Z':
                     baseType = BooleanType.v();
                     ++p;
                     break;
                  case '[':
                     isArray = true;
                     ++numDimensions;
                     ++p;
                     continue;
                  }
               }

               if (baseType != null) {
                  Object t;
                  if (isArray) {
                     t = ArrayType.v((Type)baseType, numDimensions);
                  } else {
                     t = baseType;
                  }

                  conversionTypes.add(t);
               }
               break;
            }
         }

         ret = (Type[])conversionTypes.toArray(new Type[0]);
         synchronized(this.cache) {
            this.cache.put(descriptor, ret);
            return ret;
         }
      }
   }

   public Type jimpleTypeOfFieldDescriptor(String descriptor) {
      boolean isArray = false;

      int numDimensions;
      for(numDimensions = 0; descriptor.startsWith("["); descriptor = descriptor.substring(1)) {
         isArray = true;
         ++numDimensions;
      }

      Object baseType;
      if (descriptor.equals("B")) {
         baseType = ByteType.v();
      } else if (descriptor.equals("C")) {
         baseType = CharType.v();
      } else if (descriptor.equals("D")) {
         baseType = DoubleType.v();
      } else if (descriptor.equals("F")) {
         baseType = FloatType.v();
      } else if (descriptor.equals("I")) {
         baseType = IntType.v();
      } else if (descriptor.equals("J")) {
         baseType = LongType.v();
      } else if (descriptor.equals("V")) {
         baseType = VoidType.v();
      } else if (descriptor.startsWith("L")) {
         if (!descriptor.endsWith(";")) {
            throw new RuntimeException("Class reference does not end with ;");
         }

         String className = descriptor.substring(1, descriptor.length() - 1);
         baseType = RefType.v(className.replace('/', '.'));
      } else if (descriptor.equals("S")) {
         baseType = ShortType.v();
      } else {
         if (!descriptor.equals("Z")) {
            throw new RuntimeException("Unknown field type: " + descriptor);
         }

         baseType = BooleanType.v();
      }

      return (Type)(isArray ? ArrayType.v((Type)baseType, numDimensions) : baseType);
   }

   void resetEasyNames() {
      this.nextEasyNameIndex = 0;
   }

   String getNextEasyName() {
      String[] easyNames = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
      int justifiedIndex = this.nextEasyNameIndex++;
      return justifiedIndex >= easyNames.length ? "local" + (justifiedIndex - easyNames.length) : easyNames[justifiedIndex];
   }

   Local getLocalForStackOp(JimpleBody listBody, TypeStack typeStack, int index) {
      if (typeStack.get(index).equals(Double2ndHalfType.v()) || typeStack.get(index).equals(Long2ndHalfType.v())) {
         --index;
      }

      return this.getLocalCreatingIfNecessary(listBody, "$stack" + index, UnknownType.v());
   }

   String getAbbreviationOfClassName(String className) {
      StringBuffer buffer = new StringBuffer((new Character(className.charAt(0))).toString());
      int periodIndex = 0;

      while(true) {
         periodIndex = className.indexOf(46, periodIndex + 1);
         if (periodIndex == -1) {
            return buffer.toString();
         }

         buffer.append(Character.toLowerCase(className.charAt(periodIndex + 1)));
      }
   }

   String getNormalizedClassName(String className) {
      className = className.replace('/', '.');
      if (className.endsWith(";")) {
         className = className.substring(0, className.length() - 1);
      }

      int numDimensions;
      for(numDimensions = 0; className.startsWith("["); className = className + "[]") {
         ++numDimensions;
         className = className.substring(1, className.length());
      }

      if (numDimensions != 0) {
         if (!className.startsWith("L")) {
            throw new RuntimeException("For some reason an array reference does not start with L");
         }

         className = className.substring(1, className.length());
      }

      return className;
   }

   private Local getLocalUnsafe(Body b, String name) {
      Iterator var3 = b.getLocals().iterator();

      Local local;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         local = (Local)var3.next();
      } while(!local.getName().equals(name));

      return local;
   }

   Local getLocalCreatingIfNecessary(JimpleBody listBody, String name, Type type) {
      Local l = this.getLocalUnsafe(listBody, name);
      if (l != null) {
         if (!l.getType().equals(type)) {
            throw new RuntimeException("The body already declares this local name with a different type.");
         }
      } else {
         l = Jimple.v().newLocal(name, type);
         listBody.getLocals().add(l);
      }

      return l;
   }

   Local getLocalForParameter(JimpleBody listBody, int index) {
      return this.getLocalForIndex(listBody, index, 0, 0, false);
   }

   Local getLocalForIndex(JimpleBody listBody, int index, Instruction context) {
      return this.getLocalForIndex(listBody, index, context.originalIndex, context.nextOffset(context.originalIndex), ByteCode.isLocalStore(context.code));
   }

   private Local getLocalForIndex(JimpleBody listBody, int index, int bcIndex, int nextBcIndex, boolean isLocalStore) {
      String name = null;
      if (this.useFaithfulNaming && this.activeVariableTable != null && bcIndex != -1) {
         int lookupBcIndex = bcIndex;
         if (isLocalStore) {
            lookupBcIndex = nextBcIndex;
         }

         name = this.activeVariableTable.getLocalVariableName(this.activeConstantPool, index, lookupBcIndex);
      }

      if (name == null) {
         name = "l" + index;
      }

      if (this.nameToIndexToLocal == null) {
         this.nameToIndexToLocal = new HashMap();
      }

      Object indexToLocal;
      if (!this.nameToIndexToLocal.containsKey(name)) {
         indexToLocal = new HashMap();
         this.nameToIndexToLocal.put(name, indexToLocal);
      } else {
         indexToLocal = (Map)this.nameToIndexToLocal.get(name);
      }

      Local local;
      if (((Map)indexToLocal).containsKey(index)) {
         local = (Local)((Map)indexToLocal).get(index);
      } else {
         local = Jimple.v().newLocal(name, UnknownType.v());
         listBody.getLocals().add(local);
         ((Map)indexToLocal).put(index, local);
      }

      return local;
   }

   boolean isValidJimpleName(String prospectiveName) {
      if (prospectiveName == null) {
         return false;
      } else {
         for(int i = 0; i < prospectiveName.length(); ++i) {
            char c = prospectiveName.charAt(i);
            if (i == 0 && c >= '0' && c <= '9') {
               return false;
            }

            if ((c < '0' || c > '9') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_' && c != '$') {
               return false;
            }
         }

         return true;
      }
   }

   private void addAnnotationVisibilityAttribute(Host host, attribute_info attribute, ClassFile coffiClass, Collection<Type> references) {
      VisibilityAnnotationTag tag;
      if (attribute instanceof RuntimeVisibleAnnotations_attribute) {
         tag = new VisibilityAnnotationTag(0);
         RuntimeVisibleAnnotations_attribute attr = (RuntimeVisibleAnnotations_attribute)attribute;
         this.addAnnotations(attr.number_of_annotations, attr.annotations, coffiClass, tag, references);
      } else {
         tag = new VisibilityAnnotationTag(1);
         RuntimeInvisibleAnnotations_attribute attr = (RuntimeInvisibleAnnotations_attribute)attribute;
         this.addAnnotations(attr.number_of_annotations, attr.annotations, coffiClass, tag, references);
      }

      host.addTag(tag);
   }

   private void addAnnotationVisibilityParameterAttribute(Host host, attribute_info attribute, ClassFile coffiClass, Collection<Type> references) {
      VisibilityParameterAnnotationTag tag;
      int i;
      parameter_annotation pAnnot;
      VisibilityAnnotationTag vTag;
      if (attribute instanceof RuntimeVisibleParameterAnnotations_attribute) {
         RuntimeVisibleParameterAnnotations_attribute attr = (RuntimeVisibleParameterAnnotations_attribute)attribute;
         tag = new VisibilityParameterAnnotationTag(attr.num_parameters, 0);

         for(i = 0; i < attr.num_parameters; ++i) {
            pAnnot = attr.parameter_annotations[i];
            vTag = new VisibilityAnnotationTag(0);
            this.addAnnotations(pAnnot.num_annotations, pAnnot.annotations, coffiClass, vTag, references);
            tag.addVisibilityAnnotation(vTag);
         }
      } else {
         RuntimeInvisibleParameterAnnotations_attribute attr = (RuntimeInvisibleParameterAnnotations_attribute)attribute;
         tag = new VisibilityParameterAnnotationTag(attr.num_parameters, 1);

         for(i = 0; i < attr.num_parameters; ++i) {
            pAnnot = attr.parameter_annotations[i];
            vTag = new VisibilityAnnotationTag(1);
            this.addAnnotations(pAnnot.num_annotations, pAnnot.annotations, coffiClass, vTag, references);
            tag.addVisibilityAnnotation(vTag);
         }
      }

      host.addTag(tag);
   }

   private void addAnnotations(int numAnnots, annotation[] annotations, ClassFile coffiClass, VisibilityAnnotationTag tag, Collection<Type> references) {
      for(int i = 0; i < numAnnots; ++i) {
         annotation annot = annotations[i];
         String annotType = ((CONSTANT_Utf8_info)coffiClass.constant_pool[annot.type_index]).convert();
         String ref = annotType.substring(1, annotType.length() - 1);
         ref = ref.replace('/', '.');
         references.add(RefType.v(ref));
         AnnotationTag annotTag = new AnnotationTag(annotType, this.createElementTags(annot.num_element_value_pairs, coffiClass, annot.element_value_pairs));
         tag.addAnnotation(annotTag);
      }

   }

   private ArrayList<AnnotationElem> createElementTags(int count, ClassFile coffiClass, element_value[] elems) {
      ArrayList<AnnotationElem> list = new ArrayList();

      for(int j = 0; j < count; ++j) {
         element_value ev = elems[j];
         char kind = ev.tag;
         String elemName = "default";
         if (ev.name_index != 0) {
            elemName = ((CONSTANT_Utf8_info)coffiClass.constant_pool[ev.name_index]).convert();
         }

         cp_info cval;
         String constant_val;
         if (kind != 'B' && kind != 'C' && kind != 'I' && kind != 'S' && kind != 'Z' && kind != 'D' && kind != 'F' && kind != 'J' && kind != 's') {
            if (kind == 'e') {
               enum_constant_element_value ecev = (enum_constant_element_value)ev;
               cval = coffiClass.constant_pool[ecev.type_name_index];
               constant_val = ((CONSTANT_Utf8_info)cval).convert();
               cp_info name_val = coffiClass.constant_pool[ecev.constant_name_index];
               String constant_name = ((CONSTANT_Utf8_info)name_val).convert();
               AnnotationEnumElem elem = new AnnotationEnumElem(constant_val, constant_name, kind, elemName);
               list.add(elem);
            } else if (kind == 'c') {
               class_element_value cev = (class_element_value)ev;
               cval = coffiClass.constant_pool[cev.class_info_index];
               CONSTANT_Utf8_info sval = (CONSTANT_Utf8_info)cval;
               String desc = sval.convert();
               AnnotationClassElem elem = new AnnotationClassElem(desc, kind, elemName);
               list.add(elem);
            } else if (kind == '[') {
               array_element_value aev = (array_element_value)ev;
               int num_vals = aev.num_values;
               ArrayList<AnnotationElem> elemVals = this.createElementTags(num_vals, coffiClass, aev.values);
               AnnotationArrayElem elem = new AnnotationArrayElem(elemVals, kind, elemName);
               list.add(elem);
            } else if (kind == '@') {
               annotation_element_value aev = (annotation_element_value)ev;
               annotation annot = aev.annotation_value;
               constant_val = ((CONSTANT_Utf8_info)coffiClass.constant_pool[annot.type_index]).convert();
               AnnotationTag annotTag = new AnnotationTag(constant_val, this.createElementTags(annot.num_element_value_pairs, coffiClass, annot.element_value_pairs));
               AnnotationAnnotationElem elem = new AnnotationAnnotationElem(annotTag, kind, elemName);
               list.add(elem);
            }
         } else {
            constant_element_value cev = (constant_element_value)ev;
            if (kind != 'B' && kind != 'C' && kind != 'I' && kind != 'S' && kind != 'Z') {
               if (kind == 'D') {
                  cval = coffiClass.constant_pool[cev.constant_value_index];
                  double constant_val = ((CONSTANT_Double_info)cval).convert();
                  AnnotationDoubleElem elem = new AnnotationDoubleElem(constant_val, kind, elemName);
                  list.add(elem);
               } else if (kind == 'F') {
                  cval = coffiClass.constant_pool[cev.constant_value_index];
                  float constant_val = ((CONSTANT_Float_info)cval).convert();
                  AnnotationFloatElem elem = new AnnotationFloatElem(constant_val, kind, elemName);
                  list.add(elem);
               } else if (kind == 'J') {
                  cval = coffiClass.constant_pool[cev.constant_value_index];
                  CONSTANT_Long_info lcval = (CONSTANT_Long_info)cval;
                  long constant_val = (lcval.high << 32) + lcval.low;
                  AnnotationLongElem elem = new AnnotationLongElem(constant_val, kind, elemName);
                  list.add(elem);
               } else if (kind == 's') {
                  cval = coffiClass.constant_pool[cev.constant_value_index];
                  constant_val = ((CONSTANT_Utf8_info)cval).convert();
                  AnnotationStringElem elem = new AnnotationStringElem(constant_val, kind, elemName);
                  list.add(elem);
               }
            } else {
               cval = coffiClass.constant_pool[cev.constant_value_index];
               int constant_val = (int)((CONSTANT_Integer_info)cval).bytes;
               AnnotationIntElem elem = new AnnotationIntElem(constant_val, kind, elemName);
               list.add(elem);
            }
         }
      }

      return list;
   }
}
