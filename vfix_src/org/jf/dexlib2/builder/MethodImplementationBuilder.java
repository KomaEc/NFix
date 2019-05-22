package org.jf.dexlib2.builder;

import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class MethodImplementationBuilder {
   private final HashMap<String, Label> labels = new HashMap();
   @Nonnull
   private final MutableMethodImplementation impl;
   private MethodLocation currentLocation;

   public MethodImplementationBuilder(int registerCount) {
      this.impl = new MutableMethodImplementation(registerCount);
      this.currentLocation = (MethodLocation)this.impl.instructionList.get(0);
   }

   public MethodImplementation getMethodImplementation() {
      return this.impl;
   }

   @Nonnull
   public Label addLabel(@Nonnull String name) {
      Label label = (Label)this.labels.get(name);
      if (label != null) {
         if (label.isPlaced()) {
            throw new IllegalArgumentException("There is already a label with that name.");
         }

         this.currentLocation.getLabels().add(label);
      } else {
         label = this.currentLocation.addNewLabel();
         this.labels.put(name, label);
      }

      return label;
   }

   @Nonnull
   public Label getLabel(@Nonnull String name) {
      Label label = (Label)this.labels.get(name);
      if (label == null) {
         label = new Label();
         this.labels.put(name, label);
      }

      return label;
   }

   public void addCatch(@Nullable TypeReference type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
      this.impl.addCatch(type, from, to, handler);
   }

   public void addCatch(@Nullable String type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
      this.impl.addCatch(type, from, to, handler);
   }

   public void addCatch(@Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
      this.impl.addCatch(from, to, handler);
   }

   public void addLineNumber(int lineNumber) {
      this.currentLocation.addLineNumber(lineNumber);
   }

   public void addStartLocal(int registerNumber, @Nullable StringReference name, @Nullable TypeReference type, @Nullable StringReference signature) {
      this.currentLocation.addStartLocal(registerNumber, name, type, signature);
   }

   public void addEndLocal(int registerNumber) {
      this.currentLocation.addEndLocal(registerNumber);
   }

   public void addRestartLocal(int registerNumber) {
      this.currentLocation.addRestartLocal(registerNumber);
   }

   public void addPrologue() {
      this.currentLocation.addPrologue();
   }

   public void addEpilogue() {
      this.currentLocation.addEpilogue();
   }

   public void addSetSourceFile(@Nullable StringReference sourceFile) {
      this.currentLocation.addSetSourceFile(sourceFile);
   }

   public void addInstruction(@Nullable BuilderInstruction instruction) {
      this.impl.addInstruction(instruction);
      this.currentLocation = (MethodLocation)this.impl.instructionList.get(this.impl.instructionList.size() - 1);
   }
}
