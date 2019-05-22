package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedMethod;
import org.jf.dexlib2.dexbacked.DexBackedMethodImplementation;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LocalInfo;
import org.jf.dexlib2.immutable.debug.ImmutableEndLocal;
import org.jf.dexlib2.immutable.debug.ImmutableEpilogueBegin;
import org.jf.dexlib2.immutable.debug.ImmutableLineNumber;
import org.jf.dexlib2.immutable.debug.ImmutablePrologueEnd;
import org.jf.dexlib2.immutable.debug.ImmutableRestartLocal;
import org.jf.dexlib2.immutable.debug.ImmutableSetSourceFile;
import org.jf.dexlib2.immutable.debug.ImmutableStartLocal;

public abstract class DebugInfo implements Iterable<DebugItem> {
   @Nonnull
   public abstract Iterator<String> getParameterNames(@Nullable DexReader var1);

   public abstract int getSize();

   public static DebugInfo newOrEmpty(@Nonnull DexBackedDexFile dexFile, int debugInfoOffset, @Nonnull DexBackedMethodImplementation methodImpl) {
      return (DebugInfo)(debugInfoOffset == 0 ? DebugInfo.EmptyDebugInfo.INSTANCE : new DebugInfo.DebugInfoImpl(dexFile, debugInfoOffset, methodImpl));
   }

   private static class DebugInfoImpl extends DebugInfo {
      @Nonnull
      public final DexBackedDexFile dexFile;
      private final int debugInfoOffset;
      @Nonnull
      private final DexBackedMethodImplementation methodImpl;
      private static final LocalInfo EMPTY_LOCAL_INFO = new LocalInfo() {
         @Nullable
         public String getName() {
            return null;
         }

         @Nullable
         public String getType() {
            return null;
         }

         @Nullable
         public String getSignature() {
            return null;
         }
      };

      public DebugInfoImpl(@Nonnull DexBackedDexFile dexFile, int debugInfoOffset, @Nonnull DexBackedMethodImplementation methodImpl) {
         this.dexFile = dexFile;
         this.debugInfoOffset = debugInfoOffset;
         this.methodImpl = methodImpl;
      }

      @Nonnull
      public Iterator<DebugItem> iterator() {
         DexReader reader = this.dexFile.readerAt(this.debugInfoOffset);
         final int lineNumberStart = reader.readBigUleb128();
         int registerCount = this.methodImpl.getRegisterCount();
         final LocalInfo[] locals = new LocalInfo[registerCount];
         Arrays.fill(locals, EMPTY_LOCAL_INFO);
         DexBackedMethod method = this.methodImpl.method;
         Iterator<? extends MethodParameter> parameterIterator = new ParameterIterator(method.getParameterTypes(), method.getParameterAnnotations(), this.getParameterNames(reader));
         int parameterIndex = 0;
         if (!AccessFlags.STATIC.isSet(this.methodImpl.method.getAccessFlags())) {
            locals[parameterIndex++] = new LocalInfo() {
               public String getName() {
                  return "this";
               }

               public String getType() {
                  return DebugInfoImpl.this.methodImpl.method.getDefiningClass();
               }

               public String getSignature() {
                  return null;
               }
            };
         }

         while(parameterIterator.hasNext()) {
            locals[parameterIndex++] = (LocalInfo)parameterIterator.next();
         }

         if (parameterIndex < registerCount) {
            int localIndex = registerCount - 1;

            while(true) {
               --parameterIndex;
               if (parameterIndex <= -1) {
                  break;
               }

               LocalInfo currentLocal = locals[parameterIndex];
               String type = currentLocal.getType();
               if (type != null && (type.equals("J") || type.equals("D"))) {
                  --localIndex;
                  if (localIndex == parameterIndex) {
                     break;
                  }
               }

               locals[localIndex] = currentLocal;
               locals[parameterIndex] = EMPTY_LOCAL_INFO;
               --localIndex;
            }
         }

         return new VariableSizeLookaheadIterator<DebugItem>(this.dexFile, reader.getOffset()) {
            private int codeAddress = 0;
            private int lineNumber = lineNumberStart;

            @Nullable
            protected DebugItem readNextItem(@Nonnull DexReader reader) {
               while(true) {
                  int next = reader.readUbyte();
                  int register;
                  String name;
                  String type;
                  switch(next) {
                  case 0:
                     return (DebugItem)this.endOfData();
                  case 1:
                     register = reader.readSmallUleb128();
                     this.codeAddress += register;
                     break;
                  case 2:
                     register = reader.readSleb128();
                     this.lineNumber += register;
                     break;
                  case 3:
                     register = reader.readSmallUleb128();
                     name = DebugInfoImpl.this.dexFile.getOptionalString(reader.readSmallUleb128() - 1);
                     type = DebugInfoImpl.this.dexFile.getOptionalType(reader.readSmallUleb128() - 1);
                     ImmutableStartLocal startLocal = new ImmutableStartLocal(this.codeAddress, register, name, type, (String)null);
                     if (register >= 0 && register < locals.length) {
                        locals[register] = startLocal;
                     }

                     return startLocal;
                  case 4:
                     register = reader.readSmallUleb128();
                     name = DebugInfoImpl.this.dexFile.getOptionalString(reader.readSmallUleb128() - 1);
                     type = DebugInfoImpl.this.dexFile.getOptionalType(reader.readSmallUleb128() - 1);
                     String signature = DebugInfoImpl.this.dexFile.getOptionalString(reader.readSmallUleb128() - 1);
                     ImmutableStartLocal startLocalx = new ImmutableStartLocal(this.codeAddress, register, name, type, signature);
                     if (register >= 0 && register < locals.length) {
                        locals[register] = startLocalx;
                     }

                     return startLocalx;
                  case 5:
                     register = reader.readSmallUleb128();
                     boolean replaceLocalInTable = true;
                     LocalInfo localInfox;
                     if (register >= 0 && register < locals.length) {
                        localInfox = locals[register];
                     } else {
                        localInfox = DebugInfo.DebugInfoImpl.EMPTY_LOCAL_INFO;
                        replaceLocalInTable = false;
                     }

                     if (localInfox instanceof EndLocal) {
                        localInfox = DebugInfo.DebugInfoImpl.EMPTY_LOCAL_INFO;
                        replaceLocalInTable = false;
                     }

                     ImmutableEndLocal endLocal = new ImmutableEndLocal(this.codeAddress, register, localInfox.getName(), localInfox.getType(), localInfox.getSignature());
                     if (replaceLocalInTable) {
                        locals[register] = endLocal;
                     }

                     return endLocal;
                  case 6:
                     register = reader.readSmallUleb128();
                     LocalInfo localInfo;
                     if (register >= 0 && register < locals.length) {
                        localInfo = locals[register];
                     } else {
                        localInfo = DebugInfo.DebugInfoImpl.EMPTY_LOCAL_INFO;
                     }

                     ImmutableRestartLocal restartLocal = new ImmutableRestartLocal(this.codeAddress, register, localInfo.getName(), localInfo.getType(), localInfo.getSignature());
                     if (register >= 0 && register < locals.length) {
                        locals[register] = restartLocal;
                     }

                     return restartLocal;
                  case 7:
                     return new ImmutablePrologueEnd(this.codeAddress);
                  case 8:
                     return new ImmutableEpilogueBegin(this.codeAddress);
                  case 9:
                     String sourceFile = DebugInfoImpl.this.dexFile.getOptionalString(reader.readSmallUleb128() - 1);
                     return new ImmutableSetSourceFile(this.codeAddress, sourceFile);
                  default:
                     register = next - 10;
                     this.codeAddress += register / 15;
                     this.lineNumber += register % 15 - 4;
                     return new ImmutableLineNumber(this.codeAddress, this.lineNumber);
                  }
               }
            }
         };
      }

      @Nonnull
      public VariableSizeIterator<String> getParameterNames(@Nullable DexReader reader) {
         if (reader == null) {
            reader = this.dexFile.readerAt(this.debugInfoOffset);
            reader.skipUleb128();
         }

         int parameterNameCount = reader.readSmallUleb128();
         return new VariableSizeIterator<String>(reader, parameterNameCount) {
            protected String readNextItem(@Nonnull DexReader reader, int index) {
               return DebugInfoImpl.this.dexFile.getOptionalString(reader.readSmallUleb128() - 1);
            }
         };
      }

      public int getSize() {
         Iterator iter = this.iterator();

         while(iter.hasNext()) {
            iter.next();
         }

         return ((VariableSizeLookaheadIterator)iter).getReaderOffset() - this.debugInfoOffset;
      }
   }

   private static class EmptyDebugInfo extends DebugInfo {
      public static final DebugInfo.EmptyDebugInfo INSTANCE = new DebugInfo.EmptyDebugInfo();

      @Nonnull
      public Iterator<DebugItem> iterator() {
         return ImmutableSet.of().iterator();
      }

      @Nonnull
      public Iterator<String> getParameterNames(@Nullable DexReader reader) {
         return ImmutableSet.of().iterator();
      }

      public int getSize() {
         return 0;
      }
   }
}
