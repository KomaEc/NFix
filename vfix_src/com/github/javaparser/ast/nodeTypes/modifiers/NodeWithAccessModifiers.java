package com.github.javaparser.ast.nodeTypes.modifiers;

import com.github.javaparser.ast.Node;

public interface NodeWithAccessModifiers<N extends Node> extends NodeWithPublicModifier<N>, NodeWithPrivateModifier<N>, NodeWithProtectedModifier<N> {
}
