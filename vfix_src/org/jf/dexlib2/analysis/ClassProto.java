package org.jf.dexlib2.analysis;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.analysis.util.TypeProtoUtils;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.util.AlignmentUtils;
import org.jf.util.ExceptionWithContext;
import org.jf.util.SparseArray;

public class ClassProto implements TypeProto {
   private static final byte REFERENCE = 0;
   private static final byte WIDE = 1;
   private static final byte OTHER = 2;
   @Nonnull
   protected final ClassPath classPath;
   @Nonnull
   protected final String type;
   protected boolean vtableFullyResolved = true;
   protected boolean interfacesFullyResolved = true;
   protected Set<String> unresolvedInterfaces = null;
   @Nonnull
   private final Supplier<ClassDef> classDefSupplier = Suppliers.memoize(new Supplier<ClassDef>() {
      public ClassDef get() {
         return ClassProto.this.classPath.getClassDef(ClassProto.this.type);
      }
   });
   @Nonnull
   private final Supplier<LinkedHashMap<String, ClassDef>> preDefaultMethodInterfaceSupplier = Suppliers.memoize(new Supplier<LinkedHashMap<String, ClassDef>>() {
      public LinkedHashMap<String, ClassDef> get() {
         Set<String> unresolvedInterfaces = new HashSet(0);
         LinkedHashMap interfaces = Maps.newLinkedHashMap();

         try {
            Iterator var3 = ClassProto.this.getClassDef().getInterfaces().iterator();

            label76:
            while(true) {
               String interfaceType;
               do {
                  if (!var3.hasNext()) {
                     break label76;
                  }

                  interfaceType = (String)var3.next();
               } while(interfaces.containsKey(interfaceType));

               try {
                  ClassDef interfaceDef = ClassProto.this.classPath.getClassDef(interfaceType);
                  interfaces.put(interfaceType, interfaceDef);
               } catch (UnresolvedClassException var9) {
                  interfaces.put(interfaceType, (Object)null);
                  unresolvedInterfaces.add(interfaceType);
                  ClassProto.this.interfacesFullyResolved = false;
               }

               ClassProto interfaceProto = (ClassProto)ClassProto.this.classPath.getClass(interfaceType);
               Iterator var7 = interfaceProto.getInterfaces().keySet().iterator();

               while(var7.hasNext()) {
                  String superInterface = (String)var7.next();
                  if (!interfaces.containsKey(superInterface)) {
                     interfaces.put(superInterface, interfaceProto.getInterfaces().get(superInterface));
                  }
               }

               if (!interfaceProto.interfacesFullyResolved) {
                  unresolvedInterfaces.addAll(interfaceProto.getUnresolvedInterfaces());
                  ClassProto.this.interfacesFullyResolved = false;
               }
            }
         } catch (UnresolvedClassException var11) {
            interfaces.put(ClassProto.this.type, (Object)null);
            unresolvedInterfaces.add(ClassProto.this.type);
            ClassProto.this.interfacesFullyResolved = false;
         }

         if (ClassProto.this.isInterface() && !interfaces.containsKey(ClassProto.this.getType())) {
            interfaces.put(ClassProto.this.getType(), (Object)null);
         }

         String superclass = ClassProto.this.getSuperclass();

         try {
            if (superclass != null) {
               ClassProto superclassProto = (ClassProto)ClassProto.this.classPath.getClass(superclass);
               Iterator var14 = superclassProto.getInterfaces().keySet().iterator();

               while(var14.hasNext()) {
                  String superclassInterface = (String)var14.next();
                  if (!interfaces.containsKey(superclassInterface)) {
                     interfaces.put(superclassInterface, (Object)null);
                  }
               }

               if (!superclassProto.interfacesFullyResolved) {
                  unresolvedInterfaces.addAll(superclassProto.getUnresolvedInterfaces());
                  ClassProto.this.interfacesFullyResolved = false;
               }
            }
         } catch (UnresolvedClassException var10) {
            unresolvedInterfaces.add(superclass);
            ClassProto.this.interfacesFullyResolved = false;
         }

         if (unresolvedInterfaces.size() > 0) {
            ClassProto.this.unresolvedInterfaces = unresolvedInterfaces;
         }

         return interfaces;
      }
   });
   @Nonnull
   private final Supplier<LinkedHashMap<String, ClassDef>> postDefaultMethodInterfaceSupplier = Suppliers.memoize(new Supplier<LinkedHashMap<String, ClassDef>>() {
      public LinkedHashMap<String, ClassDef> get() {
         Set<String> unresolvedInterfaces = new HashSet(0);
         LinkedHashMap<String, ClassDef> interfaces = Maps.newLinkedHashMap();
         String superclass = ClassProto.this.getSuperclass();
         if (superclass != null) {
            ClassProto superclassProto = (ClassProto)ClassProto.this.classPath.getClass(superclass);
            Iterator var5 = superclassProto.getInterfaces().keySet().iterator();

            while(var5.hasNext()) {
               String superclassInterface = (String)var5.next();
               interfaces.put(superclassInterface, (Object)null);
            }

            if (!superclassProto.interfacesFullyResolved) {
               unresolvedInterfaces.addAll(superclassProto.getUnresolvedInterfaces());
               ClassProto.this.interfacesFullyResolved = false;
            }
         }

         try {
            Iterator var12 = ClassProto.this.getClassDef().getInterfaces().iterator();

            label58:
            while(true) {
               String interfaceType;
               do {
                  if (!var12.hasNext()) {
                     break label58;
                  }

                  interfaceType = (String)var12.next();
               } while(interfaces.containsKey(interfaceType));

               ClassProto interfaceProto = (ClassProto)ClassProto.this.classPath.getClass(interfaceType);

               try {
                  Iterator var7 = interfaceProto.getInterfaces().entrySet().iterator();

                  while(var7.hasNext()) {
                     Entry<String, ClassDef> entry = (Entry)var7.next();
                     if (!interfaces.containsKey(entry.getKey())) {
                        interfaces.put(entry.getKey(), entry.getValue());
                     }
                  }
               } catch (UnresolvedClassException var10) {
                  interfaces.put(interfaceType, (Object)null);
                  unresolvedInterfaces.add(interfaceType);
                  ClassProto.this.interfacesFullyResolved = false;
               }

               if (!interfaceProto.interfacesFullyResolved) {
                  unresolvedInterfaces.addAll(interfaceProto.getUnresolvedInterfaces());
                  ClassProto.this.interfacesFullyResolved = false;
               }

               try {
                  ClassDef interfaceDef = ClassProto.this.classPath.getClassDef(interfaceType);
                  interfaces.put(interfaceType, interfaceDef);
               } catch (UnresolvedClassException var9) {
                  interfaces.put(interfaceType, (Object)null);
                  unresolvedInterfaces.add(interfaceType);
                  ClassProto.this.interfacesFullyResolved = false;
               }
            }
         } catch (UnresolvedClassException var11) {
            interfaces.put(ClassProto.this.type, (Object)null);
            unresolvedInterfaces.add(ClassProto.this.type);
            ClassProto.this.interfacesFullyResolved = false;
         }

         if (unresolvedInterfaces.size() > 0) {
            ClassProto.this.unresolvedInterfaces = unresolvedInterfaces;
         }

         return interfaces;
      }
   });
   @Nonnull
   private final Supplier<SparseArray<FieldReference>> dalvikInstanceFieldsSupplier = Suppliers.memoize(new Supplier<SparseArray<FieldReference>>() {
      public SparseArray<FieldReference> get() {
         ArrayList<Field> fields = this.getSortedInstanceFields(ClassProto.this.getClassDef());
         int fieldCount = fields.size();
         byte[] fieldTypes = new byte[fields.size()];

         int back;
         for(back = 0; back < fieldCount; ++back) {
            fieldTypes[back] = ClassProto.getFieldType((FieldReference)fields.get(back));
         }

         back = fields.size() - 1;

         int front;
         for(front = 0; front < fieldCount; ++front) {
            if (fieldTypes[front] != 0) {
               while(back > front) {
                  if (fieldTypes[back] == 0) {
                     this.swap(fieldTypes, fields, front, back--);
                     break;
                  }

                  --back;
               }
            }

            if (fieldTypes[front] != 0) {
               break;
            }
         }

         int startFieldOffset = 8;
         String superclassType = ClassProto.this.getSuperclass();
         ClassProto superclass = null;
         if (superclassType != null) {
            superclass = (ClassProto)ClassProto.this.classPath.getClass(superclassType);
            startFieldOffset = superclass.getNextFieldOffset();
         }

         byte fieldIndexMod;
         if (startFieldOffset % 8 == 0) {
            fieldIndexMod = 0;
         } else {
            fieldIndexMod = 1;
         }

         if (front < fieldCount && front % 2 != fieldIndexMod) {
            if (fieldTypes[front] == 1) {
               for(back = fieldCount - 1; back > front; --back) {
                  if (fieldTypes[back] == 2) {
                     this.swap(fieldTypes, fields, front++, back);
                     break;
                  }
               }
            } else {
               ++front;
            }
         }

         for(back = fieldCount - 1; front < fieldCount; ++front) {
            if (fieldTypes[front] != 1) {
               while(back > front) {
                  if (fieldTypes[back] == 1) {
                     this.swap(fieldTypes, fields, front, back--);
                     break;
                  }

                  --back;
               }
            }

            if (fieldTypes[front] != 1) {
               break;
            }
         }

         SparseArray superFields;
         if (superclass != null) {
            superFields = superclass.getInstanceFields();
         } else {
            superFields = new SparseArray();
         }

         int superFieldCount = superFields.size();
         int totalFieldCount = superFieldCount + fieldCount;
         SparseArray<FieldReference> instanceFields = new SparseArray(totalFieldCount);
         int fieldOffset;
         if (superclass != null && superFieldCount > 0) {
            for(int i = 0; i < superFieldCount; ++i) {
               instanceFields.append(superFields.keyAt(i), superFields.valueAt(i));
            }

            fieldOffset = instanceFields.keyAt(superFieldCount - 1);
            FieldReference lastSuperField = (FieldReference)superFields.valueAt(superFieldCount - 1);
            char fieldType = lastSuperField.getType().charAt(0);
            if (fieldType != 'J' && fieldType != 'D') {
               fieldOffset += 4;
            } else {
               fieldOffset += 8;
            }
         } else {
            fieldOffset = 8;
         }

         boolean gotDouble = false;

         for(int ix = 0; ix < fieldCount; ++ix) {
            FieldReference field = (FieldReference)fields.get(ix);
            if (fieldTypes[ix] == 1 && !gotDouble) {
               if (fieldOffset % 8 != 0) {
                  assert fieldOffset % 8 == 4;

                  fieldOffset += 4;
               }

               gotDouble = true;
            }

            instanceFields.append(fieldOffset, field);
            if (fieldTypes[ix] == 1) {
               fieldOffset += 8;
            } else {
               fieldOffset += 4;
            }
         }

         return instanceFields;
      }

      @Nonnull
      private ArrayList<Field> getSortedInstanceFields(@Nonnull ClassDef classDef) {
         ArrayList<Field> fields = Lists.newArrayList(classDef.getInstanceFields());
         Collections.sort(fields);
         return fields;
      }

      private void swap(byte[] fieldTypes, List<Field> fields, int position1, int position2) {
         byte tempType = fieldTypes[position1];
         fieldTypes[position1] = fieldTypes[position2];
         fieldTypes[position2] = tempType;
         Field tempField = (Field)fields.set(position1, fields.get(position2));
         fields.set(position2, tempField);
      }
   });
   @Nonnull
   private final Supplier<SparseArray<FieldReference>> artInstanceFieldsSupplier = Suppliers.memoize(new Supplier<SparseArray<FieldReference>>() {
      public SparseArray<FieldReference> get() {
         PriorityQueue<ClassProto.FieldGap> gaps = new PriorityQueue();
         SparseArray<FieldReference> linkedFields = new SparseArray();
         ArrayList<Field> fields = this.getSortedInstanceFields(ClassProto.this.getClassDef());
         int fieldOffset = 0;
         String superclassType = ClassProto.this.getSuperclass();
         int lastOffset;
         if (superclassType != null) {
            ClassProto superclass = (ClassProto)ClassProto.this.classPath.getClass(superclassType);
            SparseArray<FieldReference> superFields = superclass.getInstanceFields();
            FieldReference fieldx = null;
            lastOffset = 0;

            for(int i = 0; i < superFields.size(); ++i) {
               int offset = superFields.keyAt(i);
               fieldx = (FieldReference)superFields.valueAt(i);
               linkedFields.put(offset, fieldx);
               lastOffset = offset;
            }

            if (fieldx != null) {
               fieldOffset = lastOffset + this.getFieldSize(fieldx);
            }
         }

         Iterator var12 = fields.iterator();

         while(true) {
            while(var12.hasNext()) {
               Field field = (Field)var12.next();
               int fieldSize = this.getFieldSize(field);
               if (!AlignmentUtils.isAligned(fieldOffset, fieldSize)) {
                  lastOffset = fieldOffset;
                  fieldOffset = AlignmentUtils.alignOffset(fieldOffset, fieldSize);
                  this.addFieldGap(lastOffset, fieldOffset, gaps);
               }

               ClassProto.FieldGap gap = (ClassProto.FieldGap)gaps.peek();
               if (gap != null && gap.size >= fieldSize) {
                  gaps.poll();
                  linkedFields.put(gap.offset, field);
                  if (gap.size > fieldSize) {
                     this.addFieldGap(gap.offset + fieldSize, gap.offset + gap.size, gaps);
                  }
               } else {
                  linkedFields.append(fieldOffset, field);
                  fieldOffset += fieldSize;
               }
            }

            return linkedFields;
         }
      }

      private void addFieldGap(int gapStart, int gapEnd, @Nonnull PriorityQueue<ClassProto.FieldGap> gaps) {
         int offset = gapStart;

         while(true) {
            while(offset < gapEnd) {
               int remaining = gapEnd - offset;
               if (remaining >= 4 && offset % 4 == 0) {
                  gaps.add(ClassProto.FieldGap.newFieldGap(offset, 4, ClassProto.this.classPath.oatVersion));
                  offset += 4;
               } else if (remaining >= 2 && offset % 2 == 0) {
                  gaps.add(ClassProto.FieldGap.newFieldGap(offset, 2, ClassProto.this.classPath.oatVersion));
                  offset += 2;
               } else {
                  gaps.add(ClassProto.FieldGap.newFieldGap(offset, 1, ClassProto.this.classPath.oatVersion));
                  ++offset;
               }
            }

            return;
         }
      }

      @Nonnull
      private ArrayList<Field> getSortedInstanceFields(@Nonnull ClassDef classDef) {
         ArrayList<Field> fields = Lists.newArrayList(classDef.getInstanceFields());
         Collections.sort(fields, new Comparator<Field>() {
            public int compare(Field field1, Field field2) {
               int result = Ints.compare(getFieldSortOrder(field1), getFieldSortOrder(field2));
               if (result != 0) {
                  return result;
               } else {
                  result = field1.getName().compareTo(field2.getName());
                  return result != 0 ? result : field1.getType().compareTo(field2.getType());
               }
            }
         });
         return fields;
      }

      private int getFieldSortOrder(@Nonnull FieldReference field) {
         switch(field.getType().charAt(0)) {
         case 'B':
            return 8;
         case 'C':
            return 5;
         case 'D':
            return 2;
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
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         default:
            throw new ExceptionWithContext("Invalid field type: %s", new Object[]{field.getType()});
         case 'F':
            return 4;
         case 'I':
            return 3;
         case 'J':
            return 1;
         case 'L':
         case '[':
            return 0;
         case 'S':
            return 6;
         case 'Z':
            return 7;
         }
      }

      private int getFieldSize(@Nonnull FieldReference field) {
         return ClassProto.getTypeSize(field.getType().charAt(0));
      }
   });
   @Nonnull
   private final Supplier<List<Method>> preDefaultMethodVtableSupplier = Suppliers.memoize(new Supplier<List<Method>>() {
      public List<Method> get() {
         ArrayList vtable = Lists.newArrayList();

         String superclassType;
         try {
            superclassType = ClassProto.this.getSuperclass();
         } catch (UnresolvedClassException var9) {
            vtable.addAll(((ClassProto)ClassProto.this.classPath.getClass("Ljava/lang/Object;")).getVtable());
            ClassProto.this.vtableFullyResolved = false;
            return vtable;
         }

         if (superclassType != null) {
            ClassProto superclass = (ClassProto)ClassProto.this.classPath.getClass(superclassType);
            vtable.addAll(superclass.getVtable());
            if (!superclass.vtableFullyResolved) {
               ClassProto.this.vtableFullyResolved = false;
               return vtable;
            }
         }

         if (!ClassProto.this.isInterface()) {
            ClassProto.this.addToVtable(ClassProto.this.getClassDef().getVirtualMethods(), vtable, true, true);
            Iterable<ClassDef> interfaces = ClassProto.this.getDirectInterfaces();
            Iterator var4 = interfaces.iterator();

            while(var4.hasNext()) {
               ClassDef interfaceDef = (ClassDef)var4.next();
               List<Method> interfaceMethods = Lists.newArrayList();
               Iterator var7 = interfaceDef.getVirtualMethods().iterator();

               while(var7.hasNext()) {
                  Method interfaceMethod = (Method)var7.next();
                  interfaceMethods.add(new ClassProto.ReparentedMethod(interfaceMethod, ClassProto.this.type));
               }

               ClassProto.this.addToVtable(interfaceMethods, vtable, false, true);
            }
         }

         return vtable;
      }
   });
   @Nonnull
   private final Supplier<List<Method>> buggyPostDefaultMethodVtableSupplier = Suppliers.memoize(new Supplier<List<Method>>() {
      public List<Method> get() {
         ArrayList vtable = Lists.newArrayList();

         String superclassType;
         try {
            superclassType = ClassProto.this.getSuperclass();
         } catch (UnresolvedClassException var21) {
            vtable.addAll(((ClassProto)ClassProto.this.classPath.getClass("Ljava/lang/Object;")).getVtable());
            ClassProto.this.vtableFullyResolved = false;
            return vtable;
         }

         if (superclassType != null) {
            ClassProto superclass = (ClassProto)ClassProto.this.classPath.getClass(superclassType);
            vtable.addAll(superclass.getVtable());
            if (!superclass.vtableFullyResolved) {
               ClassProto.this.vtableFullyResolved = false;
               return vtable;
            }
         }

         if (!ClassProto.this.isInterface()) {
            ClassProto.this.addToVtable(ClassProto.this.getClassDef().getVirtualMethods(), vtable, true, true);
            List<String> interfaces = Lists.newArrayList((Iterable)ClassProto.this.getInterfaces().keySet());
            List<Method> defaultMethods = Lists.newArrayList();
            List<Method> defaultConflictMethods = Lists.newArrayList();
            List<Method> mirandaMethods = Lists.newArrayList();
            final HashMap<MethodReference, Integer> methodOrder = Maps.newHashMap();

            label109:
            for(int i = interfaces.size() - 1; i >= 0; --i) {
               String interfaceType = (String)interfaces.get(i);
               ClassDef interfaceDef = ClassProto.this.classPath.getClassDef(interfaceType);
               Iterator var11 = interfaceDef.getVirtualMethods().iterator();

               while(true) {
                  while(true) {
                     Method interfaceMethod;
                     int vtableIndex;
                     Method oldVtableMethod;
                     int defaultMethodIndex;
                     do {
                        if (!var11.hasNext()) {
                           continue label109;
                        }

                        interfaceMethod = (Method)var11.next();
                        vtableIndex = ClassProto.this.findMethodIndexInVtableReverse(vtable, interfaceMethod);
                        oldVtableMethod = null;
                        if (vtableIndex >= 0) {
                           oldVtableMethod = (Method)vtable.get(vtableIndex);
                        }

                        for(defaultMethodIndex = 0; defaultMethodIndex < vtable.size(); ++defaultMethodIndex) {
                           Method candidate = (Method)vtable.get(defaultMethodIndex);
                           if (MethodUtil.methodSignaturesMatch(candidate, interfaceMethod) && (!ClassProto.this.classPath.shouldCheckPackagePrivateAccess() || AnalyzedMethodUtil.canAccess(ClassProto.this, candidate, true, false, false)) && ClassProto.this.interfaceMethodOverrides(interfaceMethod, candidate)) {
                              vtable.set(defaultMethodIndex, interfaceMethod);
                           }
                        }
                     } while(vtableIndex >= 0 && !ClassProto.this.isOverridableByDefaultMethod((Method)vtable.get(vtableIndex)));

                     defaultMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultMethods, interfaceMethod);
                     if (defaultMethodIndex >= 0) {
                        if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                           ClassProto existingInterface = (ClassProto)ClassProto.this.classPath.getClass(((Method)defaultMethods.get(defaultMethodIndex)).getDefiningClass());
                           if (!existingInterface.implementsInterface(interfaceMethod.getDefiningClass())) {
                              Method removedMethod = (Method)defaultMethods.remove(defaultMethodIndex);
                              defaultConflictMethods.add(removedMethod);
                           }
                        }
                     } else {
                        int defaultConflictMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultConflictMethods, interfaceMethod);
                        if (defaultConflictMethodIndex < 0) {
                           int mirandaMethodIndex = ClassProto.this.findMethodIndexInVtable(mirandaMethods, interfaceMethod);
                           if (mirandaMethodIndex >= 0) {
                              if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                                 ClassProto existingInterfacex = (ClassProto)ClassProto.this.classPath.getClass(((Method)mirandaMethods.get(mirandaMethodIndex)).getDefiningClass());
                                 if (!existingInterfacex.implementsInterface(interfaceMethod.getDefiningClass())) {
                                    Method oldMethod = (Method)mirandaMethods.remove(mirandaMethodIndex);
                                    int methodOrderValue = (Integer)methodOrder.get(oldMethod);
                                    methodOrder.put(interfaceMethod, methodOrderValue);
                                    defaultMethods.add(interfaceMethod);
                                 }
                              }
                           } else if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                              if (oldVtableMethod == null || ClassProto.this.interfaceMethodOverrides(interfaceMethod, oldVtableMethod)) {
                                 defaultMethods.add(interfaceMethod);
                                 methodOrder.put(interfaceMethod, methodOrder.size());
                              }
                           } else if (oldVtableMethod == null) {
                              mirandaMethods.add(interfaceMethod);
                              methodOrder.put(interfaceMethod, methodOrder.size());
                           }
                        }
                     }
                  }
               }
            }

            Comparator<MethodReference> comparator = new Comparator<MethodReference>() {
               public int compare(MethodReference o1, MethodReference o2) {
                  return Ints.compare((Integer)methodOrder.get(o1), (Integer)methodOrder.get(o2));
               }
            };
            Collections.sort(mirandaMethods, comparator);
            Collections.sort(defaultMethods, comparator);
            Collections.sort(defaultConflictMethods, comparator);
            vtable.addAll(mirandaMethods);
            vtable.addAll(defaultMethods);
            vtable.addAll(defaultConflictMethods);
         }

         return vtable;
      }
   });
   @Nonnull
   private final Supplier<List<Method>> postDefaultMethodVtableSupplier = Suppliers.memoize(new Supplier<List<Method>>() {
      public List<Method> get() {
         ArrayList vtable = Lists.newArrayList();

         String superclassType;
         try {
            superclassType = ClassProto.this.getSuperclass();
         } catch (UnresolvedClassException var19) {
            vtable.addAll(((ClassProto)ClassProto.this.classPath.getClass("Ljava/lang/Object;")).getVtable());
            ClassProto.this.vtableFullyResolved = false;
            return vtable;
         }

         if (superclassType != null) {
            ClassProto superclass = (ClassProto)ClassProto.this.classPath.getClass(superclassType);
            vtable.addAll(superclass.getVtable());
            if (!superclass.vtableFullyResolved) {
               ClassProto.this.vtableFullyResolved = false;
               return vtable;
            }
         }

         if (!ClassProto.this.isInterface()) {
            ClassProto.this.addToVtable(ClassProto.this.getClassDef().getVirtualMethods(), vtable, true, true);
            Iterable<ClassDef> interfaces = Lists.reverse(Lists.newArrayList(ClassProto.this.getDirectInterfaces()));
            List<Method> defaultMethods = Lists.newArrayList();
            List<Method> defaultConflictMethods = Lists.newArrayList();
            List<Method> mirandaMethods = Lists.newArrayList();
            final HashMap<MethodReference, Integer> methodOrder = Maps.newHashMap();
            Iterator var8 = interfaces.iterator();

            while(var8.hasNext()) {
               ClassDef interfaceDef = (ClassDef)var8.next();
               Iterator var10 = interfaceDef.getVirtualMethods().iterator();

               while(var10.hasNext()) {
                  Method interfaceMethod = (Method)var10.next();
                  int vtableIndex = ClassProto.this.findMethodIndexInVtable(vtable, interfaceMethod);
                  if (vtableIndex >= 0) {
                     if (ClassProto.this.interfaceMethodOverrides(interfaceMethod, (Method)vtable.get(vtableIndex))) {
                        vtable.set(vtableIndex, interfaceMethod);
                     }
                  } else {
                     int defaultMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultMethods, interfaceMethod);
                     if (defaultMethodIndex >= 0) {
                        if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                           ClassProto existingInterface = (ClassProto)ClassProto.this.classPath.getClass(((Method)defaultMethods.get(defaultMethodIndex)).getDefiningClass());
                           if (!existingInterface.implementsInterface(interfaceMethod.getDefiningClass())) {
                              Method removedMethod = (Method)defaultMethods.remove(defaultMethodIndex);
                              defaultConflictMethods.add(removedMethod);
                           }
                        }
                     } else {
                        int defaultConflictMethodIndex = ClassProto.this.findMethodIndexInVtable(defaultConflictMethods, interfaceMethod);
                        if (defaultConflictMethodIndex < 0) {
                           int mirandaMethodIndex = ClassProto.this.findMethodIndexInVtable(mirandaMethods, interfaceMethod);
                           if (mirandaMethodIndex >= 0) {
                              if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                                 ClassProto existingInterfacex = (ClassProto)ClassProto.this.classPath.getClass(((Method)mirandaMethods.get(mirandaMethodIndex)).getDefiningClass());
                                 if (!existingInterfacex.implementsInterface(interfaceMethod.getDefiningClass())) {
                                    Method oldMethod = (Method)mirandaMethods.remove(mirandaMethodIndex);
                                    int methodOrderValue = (Integer)methodOrder.get(oldMethod);
                                    methodOrder.put(interfaceMethod, methodOrderValue);
                                    defaultMethods.add(interfaceMethod);
                                 }
                              }
                           } else if (!AccessFlags.ABSTRACT.isSet(interfaceMethod.getAccessFlags())) {
                              defaultMethods.add(interfaceMethod);
                              methodOrder.put(interfaceMethod, methodOrder.size());
                           } else {
                              mirandaMethods.add(interfaceMethod);
                              methodOrder.put(interfaceMethod, methodOrder.size());
                           }
                        }
                     }
                  }
               }
            }

            Comparator<MethodReference> comparator = new Comparator<MethodReference>() {
               public int compare(MethodReference o1, MethodReference o2) {
                  return Ints.compare((Integer)methodOrder.get(o1), (Integer)methodOrder.get(o2));
               }
            };
            Collections.sort(defaultMethods, comparator);
            Collections.sort(defaultConflictMethods, comparator);
            Collections.sort(mirandaMethods, comparator);
            ClassProto.this.addToVtable(defaultMethods, vtable, false, false);
            ClassProto.this.addToVtable(defaultConflictMethods, vtable, false, false);
            ClassProto.this.addToVtable(mirandaMethods, vtable, false, false);
         }

         return vtable;
      }
   });

   public ClassProto(@Nonnull ClassPath classPath, @Nonnull String type) {
      if (type.charAt(0) != 'L') {
         throw new ExceptionWithContext("Cannot construct ClassProto for non reference type: %s", new Object[]{type});
      } else {
         this.classPath = classPath;
         this.type = type;
      }
   }

   public String toString() {
      return this.type;
   }

   @Nonnull
   public ClassPath getClassPath() {
      return this.classPath;
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   @Nonnull
   public ClassDef getClassDef() {
      return (ClassDef)this.classDefSupplier.get();
   }

   public boolean isInterface() {
      ClassDef classDef = this.getClassDef();
      return (classDef.getAccessFlags() & AccessFlags.INTERFACE.getValue()) != 0;
   }

   @Nonnull
   protected LinkedHashMap<String, ClassDef> getInterfaces() {
      return this.classPath.isArt() && this.classPath.oatVersion >= 72 ? (LinkedHashMap)this.postDefaultMethodInterfaceSupplier.get() : (LinkedHashMap)this.preDefaultMethodInterfaceSupplier.get();
   }

   @Nonnull
   protected Set<String> getUnresolvedInterfaces() {
      return (Set)(this.unresolvedInterfaces == null ? ImmutableSet.of() : this.unresolvedInterfaces);
   }

   @Nonnull
   protected Iterable<ClassDef> getDirectInterfaces() {
      Iterable<ClassDef> directInterfaces = FluentIterable.from((Iterable)this.getInterfaces().values()).filter(Predicates.notNull());
      if (!this.interfacesFullyResolved) {
         throw new UnresolvedClassException("Interfaces for class %s not fully resolved: %s", new Object[]{this.getType(), Joiner.on(',').join((Iterable)this.getUnresolvedInterfaces())});
      } else {
         return directInterfaces;
      }
   }

   public boolean implementsInterface(@Nonnull String iface) {
      if (this.getInterfaces().containsKey(iface)) {
         return true;
      } else if (!this.interfacesFullyResolved) {
         throw new UnresolvedClassException("Interfaces for class %s not fully resolved", new Object[]{this.getType()});
      } else {
         return false;
      }
   }

   @Nullable
   public String getSuperclass() {
      return this.getClassDef().getSuperclass();
   }

   private boolean checkInterface(@Nonnull ClassProto other) {
      boolean isResolved = true;
      boolean isInterface = true;

      try {
         isInterface = this.isInterface();
      } catch (UnresolvedClassException var5) {
         isResolved = false;
      }

      if (isInterface) {
         try {
            if (other.implementsInterface(this.getType())) {
               return true;
            }
         } catch (UnresolvedClassException var6) {
            if (isResolved) {
               throw var6;
            }
         }
      }

      return false;
   }

   @Nonnull
   public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
      if (!(other instanceof ClassProto)) {
         return other.getCommonSuperclass(this);
      } else if (this != other && !this.getType().equals(other.getType())) {
         if (this.getType().equals("Ljava/lang/Object;")) {
            return this;
         } else if (other.getType().equals("Ljava/lang/Object;")) {
            return other;
         } else {
            boolean gotException = false;

            try {
               if (this.checkInterface((ClassProto)other)) {
                  return this;
               }
            } catch (UnresolvedClassException var8) {
               gotException = true;
            }

            try {
               if (((ClassProto)other).checkInterface(this)) {
                  return other;
               }
            } catch (UnresolvedClassException var7) {
               gotException = true;
            }

            if (gotException) {
               return this.classPath.getUnknownClass();
            } else {
               List<TypeProto> thisChain = Lists.newArrayList((Object[])(this));
               Iterables.addAll(thisChain, TypeProtoUtils.getSuperclassChain(this));
               List<TypeProto> otherChain = Lists.newArrayList((Object[])(other));
               Iterables.addAll(otherChain, TypeProtoUtils.getSuperclassChain(other));
               List<TypeProto> thisChain = Lists.reverse(thisChain);
               List<TypeProto> otherChain = Lists.reverse(otherChain);

               for(int i = Math.min(thisChain.size(), otherChain.size()) - 1; i >= 0; --i) {
                  TypeProto typeProto = (TypeProto)thisChain.get(i);
                  if (typeProto.getType().equals(((TypeProto)otherChain.get(i)).getType())) {
                     return typeProto;
                  }
               }

               return this.classPath.getUnknownClass();
            }
         }
      } else {
         return this;
      }
   }

   @Nullable
   public FieldReference getFieldByOffset(int fieldOffset) {
      return this.getInstanceFields().size() == 0 ? null : (FieldReference)this.getInstanceFields().get(fieldOffset);
   }

   @Nullable
   public Method getMethodByVtableIndex(int vtableIndex) {
      List<Method> vtable = this.getVtable();
      return vtableIndex >= 0 && vtableIndex < vtable.size() ? (Method)vtable.get(vtableIndex) : null;
   }

   public int findMethodIndexInVtable(@Nonnull MethodReference method) {
      return this.findMethodIndexInVtable(this.getVtable(), method);
   }

   private int findMethodIndexInVtable(@Nonnull List<Method> vtable, MethodReference method) {
      for(int i = 0; i < vtable.size(); ++i) {
         Method candidate = (Method)vtable.get(i);
         if (MethodUtil.methodSignaturesMatch(candidate, method) && (!this.classPath.shouldCheckPackagePrivateAccess() || AnalyzedMethodUtil.canAccess(this, candidate, true, false, false))) {
            return i;
         }
      }

      return -1;
   }

   private int findMethodIndexInVtableReverse(@Nonnull List<Method> vtable, MethodReference method) {
      for(int i = vtable.size() - 1; i >= 0; --i) {
         Method candidate = (Method)vtable.get(i);
         if (MethodUtil.methodSignaturesMatch(candidate, method) && (!this.classPath.shouldCheckPackagePrivateAccess() || AnalyzedMethodUtil.canAccess(this, candidate, true, false, false))) {
            return i;
         }
      }

      return -1;
   }

   @Nonnull
   public SparseArray<FieldReference> getInstanceFields() {
      return this.classPath.isArt() ? (SparseArray)this.artInstanceFieldsSupplier.get() : (SparseArray)this.dalvikInstanceFieldsSupplier.get();
   }

   private int getNextFieldOffset() {
      SparseArray<FieldReference> instanceFields = this.getInstanceFields();
      if (instanceFields.size() == 0) {
         return this.classPath.isArt() ? 0 : 8;
      } else {
         int lastItemIndex = instanceFields.size() - 1;
         int fieldOffset = instanceFields.keyAt(lastItemIndex);
         FieldReference lastField = (FieldReference)instanceFields.valueAt(lastItemIndex);
         if (this.classPath.isArt()) {
            return fieldOffset + getTypeSize(lastField.getType().charAt(0));
         } else {
            switch(lastField.getType().charAt(0)) {
            case 'D':
            case 'J':
               return fieldOffset + 8;
            default:
               return fieldOffset + 4;
            }
         }
      }
   }

   private static int getTypeSize(char type) {
      switch(type) {
      case 'B':
      case 'Z':
         return 1;
      case 'C':
      case 'S':
         return 2;
      case 'D':
      case 'J':
         return 8;
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
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      default:
         throw new ExceptionWithContext("Invalid type: %s", new Object[]{type});
      case 'F':
      case 'I':
      case 'L':
      case '[':
         return 4;
      }
   }

   @Nonnull
   public List<Method> getVtable() {
      if (this.classPath.isArt() && this.classPath.oatVersion >= 72) {
         return this.classPath.oatVersion < 87 ? (List)this.buggyPostDefaultMethodVtableSupplier.get() : (List)this.postDefaultMethodVtableSupplier.get();
      } else {
         return (List)this.preDefaultMethodVtableSupplier.get();
      }
   }

   private void addToVtable(@Nonnull Iterable<? extends Method> localMethods, @Nonnull List<Method> vtable, boolean replaceExisting, boolean sort) {
      if (sort) {
         ArrayList<Method> methods = Lists.newArrayList((Iterable)localMethods);
         Collections.sort(methods);
         localMethods = methods;
      }

      Iterator var8 = ((Iterable)localMethods).iterator();

      while(var8.hasNext()) {
         Method virtualMethod = (Method)var8.next();
         int vtableIndex = this.findMethodIndexInVtable(vtable, virtualMethod);
         if (vtableIndex >= 0) {
            if (replaceExisting) {
               vtable.set(vtableIndex, virtualMethod);
            }
         } else {
            vtable.add(virtualMethod);
         }
      }

   }

   private static byte getFieldType(@Nonnull FieldReference field) {
      switch(field.getType().charAt(0)) {
      case 'D':
      case 'J':
         return 1;
      case 'L':
      case '[':
         return 0;
      default:
         return 2;
      }
   }

   private boolean isOverridableByDefaultMethod(@Nonnull Method method) {
      ClassProto classProto = (ClassProto)this.classPath.getClass(method.getDefiningClass());
      return classProto.isInterface();
   }

   private boolean interfaceMethodOverrides(@Nonnull Method method, @Nonnull Method method2) {
      ClassProto classProto = (ClassProto)this.classPath.getClass(method2.getDefiningClass());
      if (classProto.isInterface()) {
         ClassProto targetClassProto = (ClassProto)this.classPath.getClass(method.getDefiningClass());
         return targetClassProto.implementsInterface(method2.getDefiningClass());
      } else {
         return false;
      }
   }

   static class ReparentedMethod extends BaseMethodReference implements Method {
      private final Method method;
      private final String definingClass;

      public ReparentedMethod(Method method, String definingClass) {
         this.method = method;
         this.definingClass = definingClass;
      }

      @Nonnull
      public String getDefiningClass() {
         return this.definingClass;
      }

      @Nonnull
      public String getName() {
         return this.method.getName();
      }

      @Nonnull
      public List<? extends CharSequence> getParameterTypes() {
         return this.method.getParameterTypes();
      }

      @Nonnull
      public String getReturnType() {
         return this.method.getReturnType();
      }

      @Nonnull
      public List<? extends MethodParameter> getParameters() {
         return this.method.getParameters();
      }

      public int getAccessFlags() {
         return this.method.getAccessFlags();
      }

      @Nonnull
      public Set<? extends Annotation> getAnnotations() {
         return this.method.getAnnotations();
      }

      @Nullable
      public MethodImplementation getImplementation() {
         return this.method.getImplementation();
      }
   }

   private abstract static class FieldGap implements Comparable<ClassProto.FieldGap> {
      public final int offset;
      public final int size;

      public static ClassProto.FieldGap newFieldGap(int offset, int size, int oatVersion) {
         return oatVersion >= 67 ? new ClassProto.FieldGap(offset, size) {
            public int compareTo(@Nonnull ClassProto.FieldGap o) {
               int result = Ints.compare(o.size, this.size);
               return result != 0 ? result : Ints.compare(this.offset, o.offset);
            }
         } : new ClassProto.FieldGap(offset, size) {
            public int compareTo(@Nonnull ClassProto.FieldGap o) {
               int result = Ints.compare(this.size, o.size);
               return result != 0 ? result : Ints.compare(o.offset, this.offset);
            }
         };
      }

      private FieldGap(int offset, int size) {
         this.offset = offset;
         this.size = size;
      }

      // $FF: synthetic method
      FieldGap(int x0, int x1, Object x2) {
         this(x0, x1);
      }
   }
}
