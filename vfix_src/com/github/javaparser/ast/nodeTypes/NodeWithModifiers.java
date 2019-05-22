package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

public interface NodeWithModifiers<N extends Node> {
   EnumSet<Modifier> getModifiers();

   N setModifiers(EnumSet<Modifier> modifiers);

   default N addModifier(Modifier... modifiers) {
      EnumSet<Modifier> newModifiers = this.getModifiers().clone();
      newModifiers.addAll((Collection)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })));
      this.setModifiers(newModifiers);
      return (Node)this;
   }

   default N removeModifier(Modifier... m) {
      EnumSet<Modifier> newModifiers = this.getModifiers().clone();
      newModifiers.removeAll((Collection)Arrays.stream(m).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })));
      this.setModifiers(newModifiers);
      return (Node)this;
   }

   default N setModifier(Modifier m, boolean set) {
      return set ? this.addModifier(m) : this.removeModifier(m);
   }
}
