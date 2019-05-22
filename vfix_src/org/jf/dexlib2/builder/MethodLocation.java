package org.jf.dexlib2.builder;

import com.google.common.collect.ImmutableList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.builder.debug.BuilderEndLocal;
import org.jf.dexlib2.builder.debug.BuilderEpilogueBegin;
import org.jf.dexlib2.builder.debug.BuilderLineNumber;
import org.jf.dexlib2.builder.debug.BuilderPrologueEnd;
import org.jf.dexlib2.builder.debug.BuilderRestartLocal;
import org.jf.dexlib2.builder.debug.BuilderSetSourceFile;
import org.jf.dexlib2.builder.debug.BuilderStartLocal;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class MethodLocation {
   @Nullable
   BuilderInstruction instruction;
   int codeAddress;
   int index;
   @Nullable
   private List<Label> labels = null;
   @Nullable
   private List<BuilderDebugItem> debugItems = null;

   MethodLocation(@Nullable BuilderInstruction instruction, int codeAddress, int index) {
      this.instruction = instruction;
      this.codeAddress = codeAddress;
      this.index = index;
   }

   @Nullable
   public Instruction getInstruction() {
      return this.instruction;
   }

   public int getCodeAddress() {
      return this.codeAddress;
   }

   public int getIndex() {
      return this.index;
   }

   @Nonnull
   private List<Label> getLabels(boolean mutable) {
      if (this.labels == null) {
         if (mutable) {
            this.labels = new ArrayList(1);
            return this.labels;
         } else {
            return ImmutableList.of();
         }
      } else {
         return this.labels;
      }
   }

   @Nonnull
   private List<BuilderDebugItem> getDebugItems(boolean mutable) {
      if (this.debugItems == null) {
         if (mutable) {
            this.debugItems = new ArrayList(1);
            return this.debugItems;
         } else {
            return ImmutableList.of();
         }
      } else {
         return this.debugItems;
      }
   }

   void mergeInto(@Nonnull MethodLocation other) {
      List debugItems;
      Iterator var3;
      if (this.labels != null || other.labels != null) {
         debugItems = other.getLabels(true);
         var3 = this.getLabels(false).iterator();

         while(var3.hasNext()) {
            Label label = (Label)var3.next();
            label.location = other;
            debugItems.add(label);
         }

         this.labels = null;
      }

      if (this.debugItems != null || other.labels != null) {
         debugItems = this.getDebugItems(true);

         BuilderDebugItem debugItem;
         for(var3 = debugItems.iterator(); var3.hasNext(); debugItem.location = other) {
            debugItem = (BuilderDebugItem)var3.next();
         }

         debugItems.addAll(other.getDebugItems(false));
         other.debugItems = debugItems;
         this.debugItems = null;
      }

   }

   @Nonnull
   public Set<Label> getLabels() {
      return new AbstractSet<Label>() {
         @Nonnull
         public Iterator<Label> iterator() {
            final Iterator<Label> it = MethodLocation.this.getLabels(false).iterator();
            return new Iterator<Label>() {
               @Nullable
               private Label currentLabel = null;

               public boolean hasNext() {
                  return it.hasNext();
               }

               public Label next() {
                  this.currentLabel = (Label)it.next();
                  return this.currentLabel;
               }

               public void remove() {
                  if (this.currentLabel != null) {
                     this.currentLabel.location = null;
                  }

                  it.remove();
               }
            };
         }

         public int size() {
            return MethodLocation.this.getLabels(false).size();
         }

         public boolean add(@Nonnull Label label) {
            if (label.isPlaced()) {
               throw new IllegalArgumentException("Cannot add a label that is already placed. You must remove it from its current location first.");
            } else {
               label.location = MethodLocation.this;
               MethodLocation.this.getLabels(true).add(label);
               return true;
            }
         }
      };
   }

   @Nonnull
   public Label addNewLabel() {
      Label label = new Label(this);
      this.getLabels(true).add(label);
      return label;
   }

   @Nonnull
   public Set<BuilderDebugItem> getDebugItems() {
      return new AbstractSet<BuilderDebugItem>() {
         @Nonnull
         public Iterator<BuilderDebugItem> iterator() {
            final Iterator<BuilderDebugItem> it = MethodLocation.this.getDebugItems(false).iterator();
            return new Iterator<BuilderDebugItem>() {
               @Nullable
               private BuilderDebugItem currentDebugItem = null;

               public boolean hasNext() {
                  return it.hasNext();
               }

               public BuilderDebugItem next() {
                  this.currentDebugItem = (BuilderDebugItem)it.next();
                  return this.currentDebugItem;
               }

               public void remove() {
                  if (this.currentDebugItem != null) {
                     this.currentDebugItem.location = null;
                  }

                  it.remove();
               }
            };
         }

         public int size() {
            return MethodLocation.this.getDebugItems(false).size();
         }

         public boolean add(@Nonnull BuilderDebugItem debugItem) {
            if (debugItem.location != null) {
               throw new IllegalArgumentException("Cannot add a debug item that has already been added to a method. You must remove it from its current location first.");
            } else {
               debugItem.location = MethodLocation.this;
               MethodLocation.this.getDebugItems(true).add(debugItem);
               return true;
            }
         }
      };
   }

   public void addLineNumber(int lineNumber) {
      this.getDebugItems().add(new BuilderLineNumber(lineNumber));
   }

   public void addStartLocal(int registerNumber, @Nullable StringReference name, @Nullable TypeReference type, @Nullable StringReference signature) {
      this.getDebugItems().add(new BuilderStartLocal(registerNumber, name, type, signature));
   }

   public void addEndLocal(int registerNumber) {
      this.getDebugItems().add(new BuilderEndLocal(registerNumber));
   }

   public void addRestartLocal(int registerNumber) {
      this.getDebugItems().add(new BuilderRestartLocal(registerNumber));
   }

   public void addPrologue() {
      this.getDebugItems().add(new BuilderPrologueEnd());
   }

   public void addEpilogue() {
      this.getDebugItems().add(new BuilderEpilogueBegin());
   }

   public void addSetSourceFile(@Nullable StringReference sourceFile) {
      this.getDebugItems().add(new BuilderSetSourceFile(sourceFile));
   }
}
