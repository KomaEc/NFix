package com.github.javaparser.metamodel;

import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.utils.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseNodeMetaModel {
   private final Optional<BaseNodeMetaModel> superNodeMetaModel;
   private final List<PropertyMetaModel> declaredPropertyMetaModels = new ArrayList();
   private final List<PropertyMetaModel> derivedPropertyMetaModels = new ArrayList();
   private final List<PropertyMetaModel> constructorParameters = new ArrayList();
   private final Class<? extends Node> type;
   private final String name;
   private final String packageName;
   private final boolean isAbstract;
   private final boolean hasWildcard;

   public BaseNodeMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      this.superNodeMetaModel = superNodeMetaModel;
      this.type = type;
      this.name = name;
      this.packageName = packageName;
      this.isAbstract = isAbstract;
      this.hasWildcard = hasWildcard;
   }

   public boolean is(Class<? extends Node> c) {
      return this.type.equals(c);
   }

   public String getQualifiedClassName() {
      return this.packageName + "." + this.name;
   }

   public Optional<BaseNodeMetaModel> getSuperNodeMetaModel() {
      return this.superNodeMetaModel;
   }

   public List<PropertyMetaModel> getDeclaredPropertyMetaModels() {
      return this.declaredPropertyMetaModels;
   }

   public List<PropertyMetaModel> getDerivedPropertyMetaModels() {
      return this.derivedPropertyMetaModels;
   }

   public List<PropertyMetaModel> getConstructorParameters() {
      return this.constructorParameters;
   }

   public List<PropertyMetaModel> getAllPropertyMetaModels() {
      List<PropertyMetaModel> allPropertyMetaModels = new ArrayList(this.getDeclaredPropertyMetaModels());
      BaseNodeMetaModel walkNode = this;

      while(walkNode.getSuperNodeMetaModel().isPresent()) {
         walkNode = (BaseNodeMetaModel)walkNode.getSuperNodeMetaModel().get();
         allPropertyMetaModels.addAll(walkNode.getDeclaredPropertyMetaModels());
      }

      return allPropertyMetaModels;
   }

   public boolean isInstanceOfMetaModel(BaseNodeMetaModel baseMetaModel) {
      if (this == baseMetaModel) {
         return true;
      } else {
         return this.isRootNode() ? false : ((BaseNodeMetaModel)this.getSuperNodeMetaModel().get()).isInstanceOfMetaModel(baseMetaModel);
      }
   }

   public Class<? extends Node> getType() {
      return this.type;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public boolean isAbstract() {
      return this.isAbstract;
   }

   public boolean hasWildcard() {
      return this.hasWildcard;
   }

   public boolean isRootNode() {
      return !this.superNodeMetaModel.isPresent();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BaseNodeMetaModel classMetaModel = (BaseNodeMetaModel)o;
         return this.type.equals(classMetaModel.type);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.type.hashCode();
   }

   public String toString() {
      return this.name;
   }

   public String getTypeNameGenerified() {
      return this.hasWildcard ? this.getTypeName() + "<?>" : this.getTypeName();
   }

   public String getTypeName() {
      return this.type.getSimpleName();
   }

   public String getMetaModelFieldName() {
      return Utils.decapitalize(this.getClass().getSimpleName());
   }

   public Node construct(Map<String, Object> parameters) {
      Constructor[] var2 = this.getType().getConstructors();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Constructor<?> constructor = var2[var4];
         if (constructor.getAnnotation(AllFieldsConstructor.class) != null) {
            try {
               Object[] paramArray = new Object[constructor.getParameterCount()];
               int i = 0;

               for(Iterator var8 = this.getConstructorParameters().iterator(); var8.hasNext(); ++i) {
                  PropertyMetaModel constructorParameter = (PropertyMetaModel)var8.next();
                  paramArray[i] = parameters.get(constructorParameter.getName());
                  if (paramArray[i] == null && constructorParameter.isRequired()) {
                     if (constructorParameter.isNodeList()) {
                        paramArray[i] = new NodeList();
                     } else if (constructorParameter.isEnumSet()) {
                        paramArray[i] = EnumSet.noneOf(Modifier.class);
                     }
                  }
               }

               return (Node)constructor.newInstance(paramArray);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException var10) {
               throw new RuntimeException(var10);
            }
         }
      }

      throw new IllegalStateException();
   }
}
