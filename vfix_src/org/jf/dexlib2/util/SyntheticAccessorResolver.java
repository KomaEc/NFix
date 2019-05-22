package org.jf.dexlib2.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;

public class SyntheticAccessorResolver {
   public static final int METHOD = 0;
   public static final int GETTER = 1;
   public static final int SETTER = 2;
   public static final int POSTFIX_INCREMENT = 3;
   public static final int PREFIX_INCREMENT = 4;
   public static final int POSTFIX_DECREMENT = 5;
   public static final int PREFIX_DECREMENT = 6;
   public static final int ADD_ASSIGNMENT = 7;
   public static final int SUB_ASSIGNMENT = 8;
   public static final int MUL_ASSIGNMENT = 9;
   public static final int DIV_ASSIGNMENT = 10;
   public static final int REM_ASSIGNMENT = 11;
   public static final int AND_ASSIGNMENT = 12;
   public static final int OR_ASSIGNMENT = 13;
   public static final int XOR_ASSIGNMENT = 14;
   public static final int SHL_ASSIGNMENT = 15;
   public static final int SHR_ASSIGNMENT = 16;
   public static final int USHR_ASSIGNMENT = 17;
   private final SyntheticAccessorFSM syntheticAccessorFSM;
   private final Map<String, ClassDef> classDefMap;
   private final Map<String, SyntheticAccessorResolver.AccessedMember> resolvedAccessors = Maps.newConcurrentMap();

   public SyntheticAccessorResolver(@Nonnull Opcodes opcodes, @Nonnull Iterable<? extends ClassDef> classDefs) {
      this.syntheticAccessorFSM = new SyntheticAccessorFSM(opcodes);
      ImmutableMap.Builder<String, ClassDef> builder = ImmutableMap.builder();
      Iterator var4 = classDefs.iterator();

      while(var4.hasNext()) {
         ClassDef classDef = (ClassDef)var4.next();
         builder.put(classDef.getType(), classDef);
      }

      this.classDefMap = builder.build();
   }

   public static boolean looksLikeSyntheticAccessor(String methodName) {
      return methodName.startsWith("access$");
   }

   @Nullable
   public SyntheticAccessorResolver.AccessedMember getAccessedMember(@Nonnull MethodReference methodReference) {
      String methodDescriptor = ReferenceUtil.getMethodDescriptor(methodReference);
      SyntheticAccessorResolver.AccessedMember accessedMember = (SyntheticAccessorResolver.AccessedMember)this.resolvedAccessors.get(methodDescriptor);
      if (accessedMember != null) {
         return accessedMember;
      } else {
         String type = methodReference.getDefiningClass();
         ClassDef classDef = (ClassDef)this.classDefMap.get(type);
         if (classDef == null) {
            return null;
         } else {
            Method matchedMethod = null;
            MethodImplementation matchedMethodImpl = null;
            Iterator var8 = classDef.getMethods().iterator();

            while(var8.hasNext()) {
               Method method = (Method)var8.next();
               MethodImplementation methodImpl = method.getImplementation();
               if (methodImpl != null && methodReferenceEquals(method, methodReference)) {
                  matchedMethod = method;
                  matchedMethodImpl = methodImpl;
                  break;
               }
            }

            if (matchedMethod == null) {
               return null;
            } else if (!AccessFlags.SYNTHETIC.isSet(matchedMethod.getAccessFlags())) {
               return null;
            } else {
               List<Instruction> instructions = ImmutableList.copyOf(matchedMethodImpl.getInstructions());
               int accessType = this.syntheticAccessorFSM.test(instructions);
               if (accessType >= 0) {
                  SyntheticAccessorResolver.AccessedMember member = new SyntheticAccessorResolver.AccessedMember(accessType, ((ReferenceInstruction)instructions.get(0)).getReference());
                  this.resolvedAccessors.put(methodDescriptor, member);
                  return member;
               } else {
                  return null;
               }
            }
         }
      }
   }

   private static boolean methodReferenceEquals(@Nonnull MethodReference ref1, @Nonnull MethodReference ref2) {
      return ref1.getName().equals(ref2.getName()) && ref1.getReturnType().equals(ref2.getReturnType()) && ref1.getParameterTypes().equals(ref2.getParameterTypes());
   }

   public static class AccessedMember {
      public final int accessedMemberType;
      @Nonnull
      public final Reference accessedMember;

      public AccessedMember(int accessedMemberType, @Nonnull Reference accessedMember) {
         this.accessedMemberType = accessedMemberType;
         this.accessedMember = accessedMember;
      }
   }
}
