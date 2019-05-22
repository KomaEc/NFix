package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LocalInfo;
import org.jf.dexlib2.iface.debug.RestartLocal;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class DebugItemRewriter implements Rewriter<DebugItem> {
   @Nonnull
   protected final Rewriters rewriters;

   public DebugItemRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public DebugItem rewrite(@Nonnull DebugItem value) {
      switch(value.getDebugItemType()) {
      case 3:
         return new DebugItemRewriter.RewrittenStartLocal((StartLocal)value);
      case 4:
      default:
         return value;
      case 5:
         return new DebugItemRewriter.RewrittenEndLocal((EndLocal)value);
      case 6:
         return new DebugItemRewriter.RewrittenRestartLocal((RestartLocal)value);
      }
   }

   protected class RewrittenRestartLocal extends DebugItemRewriter.BaseRewrittenLocalInfoDebugItem<RestartLocal> implements RestartLocal {
      public RewrittenRestartLocal(@Nonnull RestartLocal instruction) {
         super(instruction);
      }

      public int getRegister() {
         return ((RestartLocal)this.debugItem).getRegister();
      }
   }

   protected class RewrittenEndLocal extends DebugItemRewriter.BaseRewrittenLocalInfoDebugItem<EndLocal> implements EndLocal {
      public RewrittenEndLocal(@Nonnull EndLocal instruction) {
         super(instruction);
      }

      public int getRegister() {
         return ((EndLocal)this.debugItem).getRegister();
      }
   }

   protected class RewrittenStartLocal extends DebugItemRewriter.BaseRewrittenLocalInfoDebugItem<StartLocal> implements StartLocal {
      public RewrittenStartLocal(@Nonnull StartLocal debugItem) {
         super(debugItem);
      }

      public int getRegister() {
         return ((StartLocal)this.debugItem).getRegister();
      }

      @Nullable
      public StringReference getNameReference() {
         return ((StartLocal)this.debugItem).getNameReference();
      }

      @Nullable
      public TypeReference getTypeReference() {
         TypeReference typeReference = ((StartLocal)this.debugItem).getTypeReference();
         return typeReference == null ? null : RewriterUtils.rewriteTypeReference(DebugItemRewriter.this.rewriters.getTypeRewriter(), typeReference);
      }

      @Nullable
      public StringReference getSignatureReference() {
         return ((StartLocal)this.debugItem).getSignatureReference();
      }
   }

   protected class BaseRewrittenLocalInfoDebugItem<T extends DebugItem & LocalInfo> implements DebugItem, LocalInfo {
      @Nonnull
      protected T debugItem;

      public BaseRewrittenLocalInfoDebugItem(@Nonnull T debugItem) {
         this.debugItem = debugItem;
      }

      public int getDebugItemType() {
         return this.debugItem.getDebugItemType();
      }

      public int getCodeAddress() {
         return this.debugItem.getCodeAddress();
      }

      @Nullable
      public String getName() {
         return ((LocalInfo)this.debugItem).getName();
      }

      @Nullable
      public String getType() {
         return (String)RewriterUtils.rewriteNullable(DebugItemRewriter.this.rewriters.getTypeRewriter(), ((LocalInfo)this.debugItem).getType());
      }

      @Nullable
      public String getSignature() {
         return ((LocalInfo)this.debugItem).getSignature();
      }
   }
}
