package com.gzoltar.shaded.org.pitest.mutationtest.verify;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.help.Help;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import java.util.Collection;

public class DefaultBuildVerifier implements BuildVerifier {
   public void verify(CodeSource code) {
      Collection<ClassInfo> codeClasses = code.getCode();
      this.checkAtLeastOneClassHasLineNumbers(codeClasses);
      FCollection.forEach(codeClasses, this.throwErrorIfHasNoSourceFile());
   }

   private void checkAtLeastOneClassHasLineNumbers(Collection<ClassInfo> codeClasses) {
      if (!FCollection.contains(codeClasses, aClassWithLineNumbers()) && !codeClasses.isEmpty()) {
         throw new PitHelpError(Help.NO_LINE_NUMBERS, new Object[0]);
      }
   }

   private static F<ClassInfo, Boolean> aClassWithLineNumbers() {
      return new F<ClassInfo, Boolean>() {
         public Boolean apply(ClassInfo a) {
            return a.getNumberOfCodeLines() != 0;
         }
      };
   }

   private SideEffect1<ClassInfo> throwErrorIfHasNoSourceFile() {
      return new SideEffect1<ClassInfo>() {
         public void apply(ClassInfo a) {
            if (a.getSourceFileName() == null) {
               throw new PitHelpError(Help.NO_SOURCE_FILE, new Object[]{a.getName().asJavaName()});
            }
         }
      };
   }
}
