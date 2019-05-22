package org.jf.dexlib2.analysis;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.InlineIndexInstruction;
import org.jf.dexlib2.immutable.ImmutableMethod;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.immutable.util.ParamUtil;

public class CustomInlineMethodResolver extends InlineMethodResolver {
   @Nonnull
   private final ClassPath classPath;
   @Nonnull
   private final Method[] inlineMethods;
   private static final Pattern longMethodPattern = Pattern.compile("(L[^;]+;)->([^(]+)\\(([^)]*)\\)(.+)");

   public CustomInlineMethodResolver(@Nonnull ClassPath classPath, @Nonnull String inlineTable) {
      this.classPath = classPath;
      StringReader reader = new StringReader(inlineTable);
      List<String> lines = new ArrayList();
      BufferedReader br = new BufferedReader(reader);

      try {
         for(String line = br.readLine(); line != null; line = br.readLine()) {
            if (line.length() > 0) {
               lines.add(line);
            }
         }
      } catch (IOException var7) {
         throw new RuntimeException("Error while parsing inline table", var7);
      }

      this.inlineMethods = new Method[lines.size()];

      for(int i = 0; i < this.inlineMethods.length; ++i) {
         this.inlineMethods[i] = this.parseAndResolveInlineMethod((String)lines.get(i));
      }

   }

   public CustomInlineMethodResolver(@Nonnull ClassPath classPath, @Nonnull File inlineTable) throws IOException {
      this(classPath, Files.toString(inlineTable, Charset.forName("UTF-8")));
   }

   @Nonnull
   public Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
      InlineIndexInstruction instruction = (InlineIndexInstruction)analyzedInstruction.instruction;
      int methodIndex = instruction.getInlineIndex();
      if (methodIndex >= 0 && methodIndex < this.inlineMethods.length) {
         return this.inlineMethods[methodIndex];
      } else {
         throw new RuntimeException("Invalid method index: " + methodIndex);
      }
   }

   @Nonnull
   private Method parseAndResolveInlineMethod(@Nonnull String inlineMethod) {
      Matcher m = longMethodPattern.matcher(inlineMethod);
      if (!m.matches()) {
         assert false;

         throw new RuntimeException("Invalid method descriptor: " + inlineMethod);
      } else {
         String className = m.group(1);
         String methodName = m.group(2);
         Iterable<ImmutableMethodParameter> methodParams = ParamUtil.parseParamString(m.group(3));
         String methodRet = m.group(4);
         ImmutableMethodReference methodRef = new ImmutableMethodReference(className, methodName, methodParams, methodRet);
         int accessFlags = 0;
         boolean resolved = false;
         TypeProto typeProto = this.classPath.getClass(className);
         if (typeProto instanceof ClassProto) {
            ClassDef classDef = ((ClassProto)typeProto).getClassDef();
            Iterator var12 = classDef.getMethods().iterator();

            while(var12.hasNext()) {
               Method method = (Method)var12.next();
               if (method.equals(methodRef)) {
                  resolved = true;
                  accessFlags = method.getAccessFlags();
                  break;
               }
            }
         }

         if (!resolved) {
            throw new RuntimeException("Cannot resolve inline method: " + inlineMethod);
         } else {
            return new ImmutableMethod(className, methodName, methodParams, methodRet, accessFlags, (Set)null, (MethodImplementation)null);
         }
      }
   }
}
