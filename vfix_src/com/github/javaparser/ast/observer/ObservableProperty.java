package com.github.javaparser.ast.observer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.utils.Utils;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public enum ObservableProperty {
   ANNOTATIONS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   ANONYMOUS_CLASS_BODY(ObservableProperty.Type.MULTIPLE_REFERENCE),
   ARGUMENTS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   ASTERISK(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   BODY(ObservableProperty.Type.SINGLE_REFERENCE),
   CATCH_CLAUSES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   CHECK(ObservableProperty.Type.SINGLE_REFERENCE),
   CLASS_BODY(ObservableProperty.Type.MULTIPLE_REFERENCE),
   CLASS_DECLARATION(ObservableProperty.Type.SINGLE_REFERENCE),
   CLASS_EXPR(ObservableProperty.Type.SINGLE_REFERENCE),
   COMMENT(ObservableProperty.Type.SINGLE_REFERENCE),
   COMPARE(ObservableProperty.Type.SINGLE_REFERENCE),
   COMPONENT_TYPE(ObservableProperty.Type.SINGLE_REFERENCE),
   CONDITION(ObservableProperty.Type.SINGLE_REFERENCE),
   CONTENT(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   DEFAULT_VALUE(ObservableProperty.Type.SINGLE_REFERENCE),
   DIMENSION(ObservableProperty.Type.SINGLE_REFERENCE),
   ELEMENTS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   ELEMENT_TYPE(ObservableProperty.Type.SINGLE_REFERENCE),
   ELSE_EXPR(ObservableProperty.Type.SINGLE_REFERENCE),
   ELSE_STMT(ObservableProperty.Type.SINGLE_REFERENCE),
   ENCLOSING_PARAMETERS(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   ENTRIES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   EXPRESSION(ObservableProperty.Type.SINGLE_REFERENCE),
   EXTENDED_TYPE(ObservableProperty.Type.SINGLE_REFERENCE),
   EXTENDED_TYPES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   FINALLY_BLOCK(ObservableProperty.Type.SINGLE_REFERENCE),
   IDENTIFIER(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   IMPLEMENTED_TYPES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   IMPORTS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   INDEX(ObservableProperty.Type.SINGLE_REFERENCE),
   INITIALIZATION(ObservableProperty.Type.MULTIPLE_REFERENCE),
   INITIALIZER(ObservableProperty.Type.SINGLE_REFERENCE),
   INNER(ObservableProperty.Type.SINGLE_REFERENCE),
   INTERFACE(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   ITERABLE(ObservableProperty.Type.SINGLE_REFERENCE),
   LABEL(ObservableProperty.Type.SINGLE_REFERENCE),
   LEFT(ObservableProperty.Type.SINGLE_REFERENCE),
   LEVELS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   MEMBERS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   MEMBER_VALUE(ObservableProperty.Type.SINGLE_REFERENCE),
   MESSAGE(ObservableProperty.Type.SINGLE_REFERENCE),
   MODIFIERS(ObservableProperty.Type.MULTIPLE_ATTRIBUTE),
   MODULE(ObservableProperty.Type.SINGLE_REFERENCE),
   MODULE_NAMES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   MODULE_STMTS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   NAME(ObservableProperty.Type.SINGLE_REFERENCE),
   OPEN(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   OPERATOR(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   ORIGIN(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   PACKAGE_DECLARATION(ObservableProperty.Type.SINGLE_REFERENCE),
   PAIRS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   PARAMETER(ObservableProperty.Type.SINGLE_REFERENCE),
   PARAMETERS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   QUALIFIER(ObservableProperty.Type.SINGLE_REFERENCE),
   RECEIVER_PARAMETER(ObservableProperty.Type.SINGLE_REFERENCE),
   RESOURCES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   RIGHT(ObservableProperty.Type.SINGLE_REFERENCE),
   SCOPE(ObservableProperty.Type.SINGLE_REFERENCE),
   SELECTOR(ObservableProperty.Type.SINGLE_REFERENCE),
   STATEMENT(ObservableProperty.Type.SINGLE_REFERENCE),
   STATEMENTS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   STATIC(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   SUPER_TYPE(ObservableProperty.Type.SINGLE_REFERENCE),
   TARGET(ObservableProperty.Type.SINGLE_REFERENCE),
   THEN_EXPR(ObservableProperty.Type.SINGLE_REFERENCE),
   THEN_STMT(ObservableProperty.Type.SINGLE_REFERENCE),
   THIS(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   THROWN_EXCEPTIONS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   TRY_BLOCK(ObservableProperty.Type.SINGLE_REFERENCE),
   TYPE(ObservableProperty.Type.SINGLE_REFERENCE),
   TYPES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   TYPE_ARGUMENTS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   TYPE_BOUND(ObservableProperty.Type.MULTIPLE_REFERENCE),
   TYPE_PARAMETERS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   UPDATE(ObservableProperty.Type.MULTIPLE_REFERENCE),
   VALUE(ObservableProperty.Type.SINGLE_REFERENCE),
   VALUES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   VARIABLE(ObservableProperty.Type.SINGLE_REFERENCE),
   VARIABLES(ObservableProperty.Type.MULTIPLE_REFERENCE),
   VAR_ARGS(ObservableProperty.Type.SINGLE_ATTRIBUTE),
   VAR_ARGS_ANNOTATIONS(ObservableProperty.Type.MULTIPLE_REFERENCE),
   WITH(ObservableProperty.Type.MULTIPLE_REFERENCE),
   CASCADING_IF_STMT(ObservableProperty.Type.SINGLE_ATTRIBUTE, true),
   ELSE_BLOCK(ObservableProperty.Type.SINGLE_ATTRIBUTE, true),
   ELSE_BRANCH(ObservableProperty.Type.SINGLE_ATTRIBUTE, true),
   EXPRESSION_BODY(ObservableProperty.Type.SINGLE_REFERENCE, true),
   MAXIMUM_COMMON_TYPE(ObservableProperty.Type.SINGLE_REFERENCE, true),
   POSTFIX(ObservableProperty.Type.SINGLE_ATTRIBUTE, true),
   PREFIX(ObservableProperty.Type.SINGLE_ATTRIBUTE, true),
   THEN_BLOCK(ObservableProperty.Type.SINGLE_ATTRIBUTE, true),
   USING_DIAMOND_OPERATOR(ObservableProperty.Type.SINGLE_ATTRIBUTE, true),
   RANGE,
   COMMENTED_NODE;

   private ObservableProperty.Type type;
   private boolean derived;

   public static ObservableProperty fromCamelCaseName(String camelCaseName) {
      Optional<ObservableProperty> observableProperty = Arrays.stream(values()).filter((v) -> {
         return v.camelCaseName().equals(camelCaseName);
      }).findFirst();
      if (observableProperty.isPresent()) {
         return (ObservableProperty)observableProperty.get();
      } else {
         throw new IllegalArgumentException("No property found with the given camel case name: " + camelCaseName);
      }
   }

   private ObservableProperty(ObservableProperty.Type type) {
      this.type = type;
      this.derived = false;
   }

   private ObservableProperty(ObservableProperty.Type type, boolean derived) {
      this.type = type;
      this.derived = derived;
   }

   private ObservableProperty() {
      this(ObservableProperty.Type.SINGLE_REFERENCE, false);
   }

   public boolean isDerived() {
      return this.derived;
   }

   public boolean isAboutNodes() {
      return this.type.node;
   }

   public boolean isAboutValues() {
      return !this.isAboutNodes();
   }

   public boolean isMultiple() {
      return this.type.multiple;
   }

   public boolean isSingle() {
      return !this.isMultiple();
   }

   public String camelCaseName() {
      return Utils.screamingToCamelCase(this.name());
   }

   public Node getValueAsSingleReference(Node node) {
      Object rawValue = this.getRawValue(node);

      try {
         if (rawValue instanceof Node) {
            return (Node)rawValue;
         } else if (rawValue instanceof Optional) {
            Optional<Node> opt = (Optional)rawValue;
            return opt.isPresent() ? (Node)opt.get() : null;
         } else {
            throw new RuntimeException(String.format("Property %s returned %s (%s)", this.name(), rawValue.toString(), rawValue.getClass().getCanonicalName()));
         }
      } catch (ClassCastException var4) {
         throw new RuntimeException(var4);
      }
   }

   private boolean hasMethod(Node node, String name) {
      try {
         node.getClass().getMethod(name);
         return true;
      } catch (NoSuchMethodException var4) {
         return false;
      }
   }

   public NodeList<? extends Node> getValueAsMultipleReference(Node node) {
      Object rawValue = this.getRawValue(node);

      try {
         if (rawValue == null) {
            return null;
         } else if (rawValue instanceof NodeList) {
            return (NodeList)rawValue;
         } else {
            Optional<NodeList> opt = (Optional)rawValue;
            return opt.isPresent() ? (NodeList)opt.get() : null;
         }
      } catch (ClassCastException var4) {
         throw new RuntimeException("Unable to get list value for " + this.name() + " from " + node + " (class: " + node.getClass().getSimpleName() + ")", var4);
      }
   }

   public Collection<?> getValueAsCollection(Node node) {
      Object rawValue = this.getRawValue(node);

      try {
         return (Collection)rawValue;
      } catch (ClassCastException var4) {
         throw new RuntimeException("Unable to get list value for " + this.name() + " from " + node + " (class: " + node.getClass().getSimpleName() + ")", var4);
      }
   }

   public String getValueAsStringAttribute(Node node) {
      return (String)this.getRawValue(node);
   }

   public Boolean getValueAsBooleanAttribute(Node node) {
      return (Boolean)this.getRawValue(node);
   }

   public Object getRawValue(Node node) {
      String getterName = "get" + Utils.capitalize(this.camelCaseName());
      if (!this.hasMethod(node, getterName)) {
         getterName = "is" + Utils.capitalize(this.camelCaseName());
         if (!this.hasMethod(node, getterName)) {
            getterName = "has" + Utils.capitalize(this.camelCaseName());
         }
      }

      try {
         return node.getClass().getMethod(getterName).invoke(node);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var4) {
         throw new RuntimeException("Unable to get value for " + this.name() + " from " + node + " (" + node.getClass().getSimpleName() + ")", var4);
      }
   }

   public boolean isNull(Node node) {
      return null == this.getRawValue(node);
   }

   public boolean isNullOrNotPresent(Node node) {
      Object result = this.getRawValue(node);
      if (result == null) {
         return true;
      } else if (result instanceof Optional) {
         return !((Optional)result).isPresent();
      } else {
         return false;
      }
   }

   public boolean isNullOrEmpty(Node node) {
      return Utils.valueIsNullOrEmpty(this.getRawValue(node));
   }

   static enum Type {
      SINGLE_ATTRIBUTE(false, false),
      SINGLE_REFERENCE(false, true),
      MULTIPLE_ATTRIBUTE(true, false),
      MULTIPLE_REFERENCE(true, true);

      private boolean multiple;
      private boolean node;

      private Type(boolean multiple, boolean node) {
         this.multiple = multiple;
         this.node = node;
      }
   }
}
