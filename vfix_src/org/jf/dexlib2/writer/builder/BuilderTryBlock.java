package org.jf.dexlib2.writer.builder;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseTryBlock;

public class BuilderTryBlock extends BaseTryBlock<BuilderExceptionHandler> {
   private final int startCodeAddress;
   private final int codeUnitCount;
   @Nonnull
   private final List<? extends BuilderExceptionHandler> exceptionHandlers;

   public BuilderTryBlock(int startCodeAddress, int codeUnitCount, @Nonnull List<? extends BuilderExceptionHandler> exceptionHandlers) {
      this.startCodeAddress = startCodeAddress;
      this.codeUnitCount = codeUnitCount;
      this.exceptionHandlers = exceptionHandlers;
   }

   public int getStartCodeAddress() {
      return this.startCodeAddress;
   }

   public int getCodeUnitCount() {
      return this.codeUnitCount;
   }

   @Nonnull
   public List<? extends BuilderExceptionHandler> getExceptionHandlers() {
      return this.exceptionHandlers;
   }
}
