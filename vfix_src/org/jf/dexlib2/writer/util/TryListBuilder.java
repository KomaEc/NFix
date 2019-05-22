package org.jf.dexlib2.writer.util;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.util.ExceptionWithContext;

public class TryListBuilder<EH extends ExceptionHandler> {
   private final TryListBuilder.MutableTryBlock<EH> listStart = new TryListBuilder.MutableTryBlock(0, 0);
   private final TryListBuilder.MutableTryBlock<EH> listEnd = new TryListBuilder.MutableTryBlock(0, 0);

   public TryListBuilder() {
      this.listStart.next = this.listEnd;
      this.listEnd.prev = this.listStart;
   }

   public static <EH extends ExceptionHandler> List<TryBlock<EH>> massageTryBlocks(List<? extends TryBlock<? extends EH>> tryBlocks) {
      TryListBuilder<EH> tlb = new TryListBuilder();
      Iterator var2 = tryBlocks.iterator();

      while(var2.hasNext()) {
         TryBlock<? extends EH> tryBlock = (TryBlock)var2.next();
         int startAddress = tryBlock.getStartCodeAddress();
         int endAddress = startAddress + tryBlock.getCodeUnitCount();
         Iterator var6 = tryBlock.getExceptionHandlers().iterator();

         while(var6.hasNext()) {
            EH exceptionHandler = (ExceptionHandler)var6.next();
            tlb.addHandler(startAddress, endAddress, exceptionHandler);
         }
      }

      return tlb.getTryBlocks();
   }

   private TryListBuilder.TryBounds<EH> getBoundingRanges(int startAddress, int endAddress) {
      TryListBuilder.MutableTryBlock<EH> startBlock = null;

      TryListBuilder.MutableTryBlock tryBlock;
      int currentStartAddress;
      int currentEndAddress;
      for(tryBlock = this.listStart.next; tryBlock != this.listEnd; tryBlock = tryBlock.next) {
         currentStartAddress = tryBlock.startCodeAddress;
         currentEndAddress = tryBlock.endCodeAddress;
         if (startAddress == currentStartAddress) {
            startBlock = tryBlock;
            break;
         }

         if (startAddress > currentStartAddress && startAddress < currentEndAddress) {
            startBlock = tryBlock.split(startAddress);
            break;
         }

         if (startAddress < currentStartAddress) {
            if (endAddress <= currentStartAddress) {
               startBlock = new TryListBuilder.MutableTryBlock(startAddress, endAddress);
               tryBlock.prepend(startBlock);
               return new TryListBuilder.TryBounds(startBlock, startBlock);
            }

            startBlock = new TryListBuilder.MutableTryBlock(startAddress, currentStartAddress);
            tryBlock.prepend(startBlock);
            break;
         }
      }

      if (startBlock == null) {
         startBlock = new TryListBuilder.MutableTryBlock(startAddress, endAddress);
         this.listEnd.prepend(startBlock);
         return new TryListBuilder.TryBounds(startBlock, startBlock);
      } else {
         for(tryBlock = startBlock; tryBlock != this.listEnd; tryBlock = tryBlock.next) {
            currentStartAddress = tryBlock.startCodeAddress;
            currentEndAddress = tryBlock.endCodeAddress;
            if (endAddress == currentEndAddress) {
               return new TryListBuilder.TryBounds(startBlock, tryBlock);
            }

            if (endAddress > currentStartAddress && endAddress < currentEndAddress) {
               tryBlock.split(endAddress);
               return new TryListBuilder.TryBounds(startBlock, tryBlock);
            }

            if (endAddress <= currentStartAddress) {
               TryListBuilder.MutableTryBlock<EH> endBlock = new TryListBuilder.MutableTryBlock(tryBlock.prev.endCodeAddress, endAddress);
               tryBlock.prepend(endBlock);
               return new TryListBuilder.TryBounds(startBlock, endBlock);
            }
         }

         TryListBuilder.MutableTryBlock<EH> endBlock = new TryListBuilder.MutableTryBlock(this.listEnd.prev.endCodeAddress, endAddress);
         this.listEnd.prepend(endBlock);
         return new TryListBuilder.TryBounds(startBlock, endBlock);
      }
   }

   public void addHandler(int startAddress, int endAddress, EH handler) {
      TryListBuilder.TryBounds<EH> bounds = this.getBoundingRanges(startAddress, endAddress);
      TryListBuilder.MutableTryBlock<EH> startBlock = bounds.start;
      TryListBuilder.MutableTryBlock<EH> endBlock = bounds.end;
      int previousEnd = startAddress;
      TryListBuilder.MutableTryBlock tryBlock = startBlock;

      do {
         if (tryBlock.startCodeAddress > previousEnd) {
            TryListBuilder.MutableTryBlock<EH> newBlock = new TryListBuilder.MutableTryBlock(previousEnd, tryBlock.startCodeAddress);
            tryBlock.prepend(newBlock);
            tryBlock = newBlock;
         }

         tryBlock.addHandler(handler);
         previousEnd = tryBlock.endCodeAddress;
         tryBlock = tryBlock.next;
      } while(tryBlock.prev != endBlock);

   }

   public List<TryBlock<EH>> getTryBlocks() {
      return Lists.newArrayList(new Iterator<TryBlock<EH>>() {
         @Nullable
         private TryListBuilder.MutableTryBlock<EH> next;

         {
            this.next = TryListBuilder.this.listStart;
            this.next = this.readNextItem();
         }

         @Nullable
         protected TryListBuilder.MutableTryBlock<EH> readNextItem() {
            TryListBuilder.MutableTryBlock<EH> ret = this.next.next;
            if (ret == TryListBuilder.this.listEnd) {
               return null;
            } else {
               while(ret.next != TryListBuilder.this.listEnd && ret.endCodeAddress == ret.next.startCodeAddress && ret.getExceptionHandlers().equals(ret.next.getExceptionHandlers())) {
                  ret.mergeNext();
               }

               return ret;
            }
         }

         public boolean hasNext() {
            return this.next != null;
         }

         @Nonnull
         public TryBlock<EH> next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               TryBlock<EH> ret = this.next;
               this.next = this.readNextItem();
               return ret;
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      });
   }

   private static class MutableTryBlock<EH extends ExceptionHandler> extends BaseTryBlock<EH> {
      public TryListBuilder.MutableTryBlock<EH> prev = null;
      public TryListBuilder.MutableTryBlock<EH> next = null;
      public int startCodeAddress;
      public int endCodeAddress;
      @Nonnull
      public List<EH> exceptionHandlers = Lists.newArrayList();

      public MutableTryBlock(int startCodeAddress, int endCodeAddress) {
         this.startCodeAddress = startCodeAddress;
         this.endCodeAddress = endCodeAddress;
      }

      public MutableTryBlock(int startCodeAddress, int endCodeAddress, @Nonnull List<EH> exceptionHandlers) {
         this.startCodeAddress = startCodeAddress;
         this.endCodeAddress = endCodeAddress;
         this.exceptionHandlers = Lists.newArrayList((Iterable)exceptionHandlers);
      }

      public int getStartCodeAddress() {
         return this.startCodeAddress;
      }

      public int getCodeUnitCount() {
         return this.endCodeAddress - this.startCodeAddress;
      }

      @Nonnull
      public List<EH> getExceptionHandlers() {
         return this.exceptionHandlers;
      }

      @Nonnull
      public TryListBuilder.MutableTryBlock<EH> split(int splitAddress) {
         TryListBuilder.MutableTryBlock<EH> newTryBlock = new TryListBuilder.MutableTryBlock(splitAddress, this.endCodeAddress, this.exceptionHandlers);
         this.endCodeAddress = splitAddress;
         this.append(newTryBlock);
         return newTryBlock;
      }

      public void delete() {
         this.next.prev = this.prev;
         this.prev.next = this.next;
      }

      public void mergeNext() {
         this.endCodeAddress = this.next.endCodeAddress;
         this.next.delete();
      }

      public void append(@Nonnull TryListBuilder.MutableTryBlock<EH> tryBlock) {
         this.next.prev = tryBlock;
         tryBlock.next = this.next;
         tryBlock.prev = this;
         this.next = tryBlock;
      }

      public void prepend(@Nonnull TryListBuilder.MutableTryBlock<EH> tryBlock) {
         this.prev.next = tryBlock;
         tryBlock.prev = this.prev;
         tryBlock.next = this;
         this.prev = tryBlock;
      }

      public void addHandler(@Nonnull EH handler) {
         Iterator var2 = this.exceptionHandlers.iterator();

         while(var2.hasNext()) {
            ExceptionHandler existingHandler = (ExceptionHandler)var2.next();
            String existingType = existingHandler.getExceptionType();
            String newType = handler.getExceptionType();
            if (existingType == null) {
               if (newType == null) {
                  if (existingHandler.getHandlerCodeAddress() != handler.getHandlerCodeAddress()) {
                     throw new TryListBuilder.InvalidTryException("Multiple overlapping catch all handlers with different handlers", new Object[0]);
                  }

                  return;
               }
            } else if (existingType.equals(newType)) {
               return;
            }
         }

         this.exceptionHandlers.add(handler);
      }
   }

   public static class InvalidTryException extends ExceptionWithContext {
      public InvalidTryException(Throwable cause) {
         super(cause);
      }

      public InvalidTryException(Throwable cause, String message, Object... formatArgs) {
         super(cause, message, formatArgs);
      }

      public InvalidTryException(String message, Object... formatArgs) {
         super(message, formatArgs);
      }
   }

   private static class TryBounds<EH extends ExceptionHandler> {
      @Nonnull
      public final TryListBuilder.MutableTryBlock<EH> start;
      @Nonnull
      public final TryListBuilder.MutableTryBlock<EH> end;

      public TryBounds(@Nonnull TryListBuilder.MutableTryBlock<EH> start, @Nonnull TryListBuilder.MutableTryBlock<EH> end) {
         this.start = start;
         this.end = end;
      }
   }
}
