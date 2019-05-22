package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;

public class ImmutableTryBlock extends BaseTryBlock<ImmutableExceptionHandler> {
   protected final int startCodeAddress;
   protected final int codeUnitCount;
   @Nonnull
   protected final ImmutableList<? extends ImmutableExceptionHandler> exceptionHandlers;
   private static final ImmutableConverter<ImmutableTryBlock, TryBlock<? extends ExceptionHandler>> CONVERTER = new ImmutableConverter<ImmutableTryBlock, TryBlock<? extends ExceptionHandler>>() {
      protected boolean isImmutable(@Nonnull TryBlock item) {
         return item instanceof ImmutableTryBlock;
      }

      @Nonnull
      protected ImmutableTryBlock makeImmutable(@Nonnull TryBlock<? extends ExceptionHandler> item) {
         return ImmutableTryBlock.of(item);
      }
   };

   public ImmutableTryBlock(int startCodeAddress, int codeUnitCount, @Nullable List<? extends ExceptionHandler> exceptionHandlers) {
      this.startCodeAddress = startCodeAddress;
      this.codeUnitCount = codeUnitCount;
      this.exceptionHandlers = ImmutableExceptionHandler.immutableListOf(exceptionHandlers);
   }

   public ImmutableTryBlock(int startCodeAddress, int codeUnitCount, @Nullable ImmutableList<? extends ImmutableExceptionHandler> exceptionHandlers) {
      this.startCodeAddress = startCodeAddress;
      this.codeUnitCount = codeUnitCount;
      this.exceptionHandlers = ImmutableUtils.nullToEmptyList(exceptionHandlers);
   }

   public static ImmutableTryBlock of(TryBlock<? extends ExceptionHandler> tryBlock) {
      return tryBlock instanceof ImmutableTryBlock ? (ImmutableTryBlock)tryBlock : new ImmutableTryBlock(tryBlock.getStartCodeAddress(), tryBlock.getCodeUnitCount(), tryBlock.getExceptionHandlers());
   }

   public int getStartCodeAddress() {
      return this.startCodeAddress;
   }

   public int getCodeUnitCount() {
      return this.codeUnitCount;
   }

   @Nonnull
   public ImmutableList<? extends ImmutableExceptionHandler> getExceptionHandlers() {
      return this.exceptionHandlers;
   }

   @Nonnull
   public static ImmutableList<ImmutableTryBlock> immutableListOf(@Nullable List<? extends TryBlock<? extends ExceptionHandler>> list) {
      return CONVERTER.toList(list);
   }
}
