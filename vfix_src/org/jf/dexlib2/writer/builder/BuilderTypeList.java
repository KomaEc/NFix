package org.jf.dexlib2.writer.builder;

import com.google.common.collect.ImmutableList;
import java.util.AbstractList;
import java.util.List;
import javax.annotation.Nonnull;

public class BuilderTypeList extends AbstractList<BuilderTypeReference> {
   static final BuilderTypeList EMPTY = new BuilderTypeList(ImmutableList.of());
   @Nonnull
   final List<? extends BuilderTypeReference> types;
   int offset = 0;

   public BuilderTypeList(@Nonnull List<? extends BuilderTypeReference> types) {
      this.types = types;
   }

   public BuilderTypeReference get(int index) {
      return (BuilderTypeReference)this.types.get(index);
   }

   public int size() {
      return this.types.size();
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int offset) {
      this.offset = offset;
   }
}
