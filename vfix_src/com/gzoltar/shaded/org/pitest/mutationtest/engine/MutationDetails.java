package com.gzoltar.shaded.org.pitest.mutationtest.engine;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.coverage.ClassLine;
import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.util.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MutationDetails {
   private final MutationIdentifier id;
   private final String filename;
   private final int block;
   private final int lineNumber;
   private final String description;
   private final ArrayList<TestInfo> testsInOrder;
   private final boolean isInFinallyBlock;
   private final boolean poison;

   public MutationDetails(MutationIdentifier id, String filename, String description, int lineNumber, int block) {
      this(id, filename, description, lineNumber, block, false, false);
   }

   public MutationDetails(MutationIdentifier id, String filename, String description, int lineNumber, int block, boolean isInFinallyBlock, boolean poison) {
      this.testsInOrder = new ArrayList();
      this.id = id;
      this.description = description;
      this.filename = filename;
      this.lineNumber = lineNumber;
      this.block = block;
      this.isInFinallyBlock = isInFinallyBlock;
      this.poison = poison;
   }

   public String toString() {
      return "MutationDetails [id=" + this.id + ", filename=" + this.filename + ", block=" + this.block + ", lineNumber=" + this.lineNumber + ", description=" + this.description + ", testsInOrder=" + this.testsInOrder + "]";
   }

   public String getDescription() {
      return this.description;
   }

   /** @deprecated */
   @Deprecated
   public String getHtmlSafeDescription() {
      return StringUtil.escapeBasicHtmlChars(this.description);
   }

   /** @deprecated */
   @Deprecated
   public String getLocation() {
      return this.id.getLocation().describe();
   }

   public ClassName getClassName() {
      return this.id.getClassName();
   }

   public MethodName getMethod() {
      return this.id.getLocation().getMethodName();
   }

   public String getFilename() {
      return this.filename;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public ClassLine getClassLine() {
      return new ClassLine(this.id.getClassName(), this.lineNumber);
   }

   public MutationIdentifier getId() {
      return this.id;
   }

   public List<TestInfo> getTestsInOrder() {
      return this.testsInOrder;
   }

   public void addTestsInOrder(Collection<TestInfo> testNames) {
      this.testsInOrder.addAll(testNames);
      this.testsInOrder.trimToSize();
   }

   public boolean mayPoisonJVM() {
      return this.poison || this.isInStaticInitializer();
   }

   public boolean isInStaticInitializer() {
      return this.getMethod().name().trim().startsWith("<clinit>");
   }

   public int getBlock() {
      return this.block;
   }

   public Boolean matchesId(MutationIdentifier id) {
      return this.id.matches(id);
   }

   public String getMutator() {
      return this.id.getMutator();
   }

   public int getFirstIndex() {
      return this.id.getFirstIndex();
   }

   public boolean isInFinallyBlock() {
      return this.isInFinallyBlock;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         MutationDetails other = (MutationDetails)obj;
         if (this.id == null) {
            if (other.id != null) {
               return false;
            }
         } else if (!this.id.equals(other.id)) {
            return false;
         }

         return true;
      }
   }
}
