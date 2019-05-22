package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStrictfpModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.CallableDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class CallableDeclaration<T extends CallableDeclaration<?>> extends BodyDeclaration<T> implements NodeWithAccessModifiers<T>, NodeWithDeclaration, NodeWithSimpleName<T>, NodeWithParameters<T>, NodeWithThrownExceptions<T>, NodeWithTypeParameters<T>, NodeWithJavadoc<T>, NodeWithAbstractModifier<T>, NodeWithStaticModifier<T>, NodeWithFinalModifier<T>, NodeWithStrictfpModifier<T> {
   private EnumSet<Modifier> modifiers;
   private NodeList<TypeParameter> typeParameters;
   private SimpleName name;
   private NodeList<Parameter> parameters;
   private NodeList<ReferenceType> thrownExceptions;
   @OptionalProperty
   private ReceiverParameter receiverParameter;

   @AllFieldsConstructor
   CallableDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<TypeParameter> typeParameters, SimpleName name, NodeList<Parameter> parameters, NodeList<ReferenceType> thrownExceptions, ReceiverParameter receiverParameter) {
      this((TokenRange)null, modifiers, annotations, typeParameters, name, parameters, thrownExceptions, receiverParameter);
   }

   public CallableDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<TypeParameter> typeParameters, SimpleName name, NodeList<Parameter> parameters, NodeList<ReferenceType> thrownExceptions, ReceiverParameter receiverParameter) {
      super(tokenRange, annotations);
      this.setModifiers(modifiers);
      this.setTypeParameters(typeParameters);
      this.setName(name);
      this.setParameters(parameters);
      this.setThrownExceptions(thrownExceptions);
      this.setReceiverParameter(receiverParameter);
      this.customInitialization();
   }

   public EnumSet<Modifier> getModifiers() {
      return this.modifiers;
   }

   public T setModifiers(final EnumSet<Modifier> modifiers) {
      Utils.assertNotNull(modifiers);
      if (modifiers == this.modifiers) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODIFIERS, this.modifiers, modifiers);
         this.modifiers = modifiers;
         return this;
      }
   }

   public SimpleName getName() {
      return this.name;
   }

   public T setName(final SimpleName name) {
      Utils.assertNotNull(name);
      if (name == this.name) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.NAME, this.name, name);
         if (this.name != null) {
            this.name.setParentNode((Node)null);
         }

         this.name = name;
         this.setAsParentNodeOf(name);
         return this;
      }
   }

   public NodeList<Parameter> getParameters() {
      return this.parameters;
   }

   public T setParameters(final NodeList<Parameter> parameters) {
      Utils.assertNotNull(parameters);
      if (parameters == this.parameters) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.PARAMETERS, this.parameters, parameters);
         if (this.parameters != null) {
            this.parameters.setParentNode((Node)null);
         }

         this.parameters = parameters;
         this.setAsParentNodeOf(parameters);
         return this;
      }
   }

   public NodeList<ReferenceType> getThrownExceptions() {
      return this.thrownExceptions;
   }

   public T setThrownExceptions(final NodeList<ReferenceType> thrownExceptions) {
      Utils.assertNotNull(thrownExceptions);
      if (thrownExceptions == this.thrownExceptions) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.THROWN_EXCEPTIONS, this.thrownExceptions, thrownExceptions);
         if (this.thrownExceptions != null) {
            this.thrownExceptions.setParentNode((Node)null);
         }

         this.thrownExceptions = thrownExceptions;
         this.setAsParentNodeOf(thrownExceptions);
         return this;
      }
   }

   public NodeList<TypeParameter> getTypeParameters() {
      return this.typeParameters;
   }

   public T setTypeParameters(final NodeList<TypeParameter> typeParameters) {
      Utils.assertNotNull(typeParameters);
      if (typeParameters == this.typeParameters) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE_PARAMETERS, this.typeParameters, typeParameters);
         if (this.typeParameters != null) {
            this.typeParameters.setParentNode((Node)null);
         }

         this.typeParameters = typeParameters;
         this.setAsParentNodeOf(typeParameters);
         return this;
      }
   }

   public String getDeclarationAsString(boolean includingModifiers, boolean includingThrows) {
      return this.getDeclarationAsString(includingModifiers, includingThrows, true);
   }

   public String getDeclarationAsString() {
      return this.getDeclarationAsString(true, true, true);
   }

   public abstract String getDeclarationAsString(boolean includingModifiers, boolean includingThrows, boolean includingParameterName);

   protected String appendThrowsIfRequested(boolean includingThrows) {
      StringBuilder sb = new StringBuilder();
      if (includingThrows) {
         boolean firstThrow = true;

         ReferenceType thr;
         for(Iterator var4 = this.getThrownExceptions().iterator(); var4.hasNext(); sb.append(thr.toString(prettyPrinterNoCommentsConfiguration))) {
            thr = (ReferenceType)var4.next();
            if (firstThrow) {
               firstThrow = false;
               sb.append(" throws ");
            } else {
               sb.append(", ");
            }
         }
      }

      return sb.toString();
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.parameters.size(); ++i) {
            if (this.parameters.get(i) == node) {
               this.parameters.remove(i);
               return true;
            }
         }

         if (this.receiverParameter != null && node == this.receiverParameter) {
            this.removeReceiverParameter();
            return true;
         } else {
            for(i = 0; i < this.thrownExceptions.size(); ++i) {
               if (this.thrownExceptions.get(i) == node) {
                  this.thrownExceptions.remove(i);
                  return true;
               }
            }

            for(i = 0; i < this.typeParameters.size(); ++i) {
               if (this.typeParameters.get(i) == node) {
                  this.typeParameters.remove(i);
                  return true;
               }
            }

            return super.remove(node);
         }
      }
   }

   public CallableDeclaration.Signature getSignature() {
      return new CallableDeclaration.Signature(this.getName().getIdentifier(), (List)this.getParameters().stream().map(this::getTypeWithVarargsAsArray).map(this::stripGenerics).map(this::stripAnnotations).collect(Collectors.toList()));
   }

   private Type stripAnnotations(Type type) {
      if (type instanceof NodeWithAnnotations) {
         ((NodeWithAnnotations)type).setAnnotations(new NodeList());
      }

      return type;
   }

   private Type stripGenerics(Type type) {
      if (type instanceof NodeWithTypeArguments) {
         ((NodeWithTypeArguments)type).setTypeArguments((NodeList)null);
      }

      return type;
   }

   private Type getTypeWithVarargsAsArray(Parameter p) {
      Type t = p.getType().clone();
      if (p.isVarArgs()) {
         t = new ArrayType((Type)t, new AnnotationExpr[0]);
      }

      return (Type)t;
   }

   public CallableDeclaration<?> clone() {
      return (CallableDeclaration)this.accept(new CloneVisitor(), (Object)null);
   }

   public CallableDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.callableDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
      } else {
         int i;
         for(i = 0; i < this.parameters.size(); ++i) {
            if (this.parameters.get(i) == node) {
               this.parameters.set(i, (Node)((Parameter)replacementNode));
               return true;
            }
         }

         if (this.receiverParameter != null && node == this.receiverParameter) {
            this.setReceiverParameter((ReceiverParameter)replacementNode);
            return true;
         } else {
            for(i = 0; i < this.thrownExceptions.size(); ++i) {
               if (this.thrownExceptions.get(i) == node) {
                  this.thrownExceptions.set(i, (Node)((ReferenceType)replacementNode));
                  return true;
               }
            }

            for(i = 0; i < this.typeParameters.size(); ++i) {
               if (this.typeParameters.get(i) == node) {
                  this.typeParameters.set(i, (Node)((TypeParameter)replacementNode));
                  return true;
               }
            }

            return super.replace(node, replacementNode);
         }
      }
   }

   public boolean isCallableDeclaration() {
      return true;
   }

   public CallableDeclaration asCallableDeclaration() {
      return this;
   }

   public void ifCallableDeclaration(Consumer<CallableDeclaration> action) {
      action.accept(this);
   }

   public Optional<ReceiverParameter> getReceiverParameter() {
      return Optional.ofNullable(this.receiverParameter);
   }

   public T setReceiverParameter(final ReceiverParameter receiverParameter) {
      if (receiverParameter == this.receiverParameter) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.RECEIVER_PARAMETER, this.receiverParameter, receiverParameter);
         if (this.receiverParameter != null) {
            this.receiverParameter.setParentNode((Node)null);
         }

         this.receiverParameter = receiverParameter;
         this.setAsParentNodeOf(receiverParameter);
         return this;
      }
   }

   public CallableDeclaration removeReceiverParameter() {
      return this.setReceiverParameter((ReceiverParameter)null);
   }

   public Optional<CallableDeclaration> toCallableDeclaration() {
      return Optional.of(this);
   }

   public static class Signature {
      private final String name;
      private final List<Type> parameterTypes;

      private Signature(String name, List<Type> parameterTypes) {
         this.name = name;
         this.parameterTypes = parameterTypes;
      }

      public String getName() {
         return this.name;
      }

      public List<Type> getParameterTypes() {
         return this.parameterTypes;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            CallableDeclaration.Signature signature = (CallableDeclaration.Signature)o;
            if (!this.name.equals(signature.name)) {
               return false;
            } else {
               return this.parameterTypes.equals(signature.parameterTypes);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.name.hashCode();
         result = 31 * result + this.parameterTypes.hashCode();
         return result;
      }

      public String asString() {
         return (String)this.parameterTypes.stream().map(Type::asString).collect(Collectors.joining(", ", this.name + "(", ")"));
      }

      public String toString() {
         return this.asString();
      }

      // $FF: synthetic method
      Signature(String x0, List x1, Object x2) {
         this(x0, x1);
      }
   }
}
