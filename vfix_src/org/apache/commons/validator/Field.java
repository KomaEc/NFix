package org.apache.commons.validator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.validator.util.ValidatorUtils;

public class Field implements Cloneable, Serializable {
   private static final String DEFAULT_ARG = "org.apache.commons.validator.Field.DEFAULT";
   public static final String TOKEN_INDEXED = "[]";
   protected static final String TOKEN_START = "${";
   protected static final String TOKEN_END = "}";
   protected static final String TOKEN_VAR = "var:";
   protected String property = null;
   protected String indexedProperty = null;
   protected String indexedListProperty = null;
   protected String key = null;
   protected String depends = null;
   protected int page = 0;
   protected int fieldOrder = 0;
   private List dependencyList = Collections.synchronizedList(new ArrayList());
   /** @deprecated */
   protected FastHashMap hVars = new FastHashMap();
   /** @deprecated */
   protected FastHashMap hMsgs = new FastHashMap();
   protected Map[] args = new Map[0];

   public int getPage() {
      return this.page;
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getFieldOrder() {
      return this.fieldOrder;
   }

   public void setFieldOrder(int fieldOrder) {
      this.fieldOrder = fieldOrder;
   }

   public String getProperty() {
      return this.property;
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public String getIndexedProperty() {
      return this.indexedProperty;
   }

   public void setIndexedProperty(String indexedProperty) {
      this.indexedProperty = indexedProperty;
   }

   public String getIndexedListProperty() {
      return this.indexedListProperty;
   }

   public void setIndexedListProperty(String indexedListProperty) {
      this.indexedListProperty = indexedListProperty;
   }

   public String getDepends() {
      return this.depends;
   }

   public void setDepends(String depends) {
      this.depends = depends;
      this.dependencyList.clear();
      StringTokenizer st = new StringTokenizer(depends, ",");

      while(st.hasMoreTokens()) {
         String depend = st.nextToken().trim();
         if (depend != null && depend.length() > 0) {
            this.dependencyList.add(depend);
         }
      }

   }

   public void addMsg(Msg msg) {
      this.hMsgs.put(msg.getName(), msg);
   }

   public String getMsg(String key) {
      Msg msg = this.getMessage(key);
      return msg == null ? null : msg.getKey();
   }

   public Msg getMessage(String key) {
      return (Msg)this.hMsgs.get(key);
   }

   public Map getMessages() {
      return Collections.unmodifiableMap(this.hMsgs);
   }

   public void addArg(Arg arg) {
      if (arg != null && arg.getKey() != null && arg.getKey().length() != 0) {
         this.determineArgPosition(arg);
         this.ensureArgsCapacity(arg);
         Map argMap = this.args[arg.getPosition()];
         if (argMap == null) {
            argMap = new HashMap();
            this.args[arg.getPosition()] = (Map)argMap;
         }

         if (arg.getName() == null) {
            ((Map)argMap).put("org.apache.commons.validator.Field.DEFAULT", arg);
         } else {
            ((Map)argMap).put(arg.getName(), arg);
         }

      }
   }

   private void determineArgPosition(Arg arg) {
      int position = arg.getPosition();
      if (position < 0) {
         if (this.args != null && this.args.length != 0) {
            String key = arg.getName() == null ? "org.apache.commons.validator.Field.DEFAULT" : arg.getName();
            int lastPosition = -1;
            int lastDefault = -1;

            for(int i = 0; i < this.args.length; ++i) {
               if (this.args[i] != null && this.args[i].containsKey(key)) {
                  lastPosition = i;
               }

               if (this.args[i] != null && this.args[i].containsKey("org.apache.commons.validator.Field.DEFAULT")) {
                  lastDefault = i;
               }
            }

            if (lastPosition < 0) {
               lastPosition = lastDefault;
            }

            ++lastPosition;
            arg.setPosition(lastPosition);
         } else {
            arg.setPosition(0);
         }
      }
   }

   private void ensureArgsCapacity(Arg arg) {
      if (arg.getPosition() >= this.args.length) {
         Map[] newArgs = new Map[arg.getPosition() + 1];
         System.arraycopy(this.args, 0, newArgs, 0, this.args.length);
         this.args = newArgs;
      }

   }

   public Arg getArg(int position) {
      return this.getArg("org.apache.commons.validator.Field.DEFAULT", position);
   }

   public Arg getArg(String key, int position) {
      if (position < this.args.length && this.args[position] != null) {
         Arg arg = (Arg)this.args[position].get(key);
         if (arg == null && key.equals("org.apache.commons.validator.Field.DEFAULT")) {
            return null;
         } else {
            return arg == null ? this.getArg(position) : arg;
         }
      } else {
         return null;
      }
   }

   public Arg[] getArgs(String key) {
      Arg[] args = new Arg[this.args.length];

      for(int i = 0; i < this.args.length; ++i) {
         args[i] = this.getArg(key, i);
      }

      return args;
   }

   public void addVar(Var v) {
      this.hVars.put(v.getName(), v);
   }

   public void addVar(String name, String value, String jsType) {
      this.addVar(new Var(name, value, jsType));
   }

   public Var getVar(String mainKey) {
      return (Var)this.hVars.get(mainKey);
   }

   public String getVarValue(String mainKey) {
      String value = null;
      Object o = this.hVars.get(mainKey);
      if (o != null && o instanceof Var) {
         Var v = (Var)o;
         value = v.getValue();
      }

      return value;
   }

   public Map getVars() {
      return Collections.unmodifiableMap(this.hVars);
   }

   public String getKey() {
      if (this.key == null) {
         this.generateKey();
      }

      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public boolean isIndexed() {
      return this.indexedListProperty != null && this.indexedListProperty.length() > 0;
   }

   public void generateKey() {
      if (this.isIndexed()) {
         this.key = this.indexedListProperty + "[]" + "." + this.property;
      } else {
         this.key = this.property;
      }

   }

   void process(Map globalConstants, Map constants) {
      this.hMsgs.setFast(false);
      this.hVars.setFast(true);
      this.generateKey();
      Iterator i = constants.keySet().iterator();

      String key;
      String key;
      while(i.hasNext()) {
         String key = (String)i.next();
         key = "${" + key + "}";
         key = (String)constants.get(key);
         this.property = ValidatorUtils.replace(this.property, key, key);
         this.processVars(key, key);
         this.processMessageComponents(key, key);
      }

      Iterator i = globalConstants.keySet().iterator();

      String key2;
      while(i.hasNext()) {
         key = (String)i.next();
         key = "${" + key + "}";
         key2 = (String)globalConstants.get(key);
         this.property = ValidatorUtils.replace(this.property, key, key2);
         this.processVars(key, key2);
         this.processMessageComponents(key, key2);
      }

      Iterator i = this.hVars.keySet().iterator();

      while(i.hasNext()) {
         key = (String)i.next();
         key2 = "${var:" + key + "}";
         Var var = this.getVar(key);
         String replaceValue = var.getValue();
         this.processMessageComponents(key2, replaceValue);
      }

      this.hMsgs.setFast(true);
   }

   private void processVars(String key, String replaceValue) {
      Iterator i = this.hVars.keySet().iterator();

      while(i.hasNext()) {
         String varKey = (String)i.next();
         Var var = this.getVar(varKey);
         var.setValue(ValidatorUtils.replace(var.getValue(), key, replaceValue));
      }

   }

   private void processMessageComponents(String key, String replaceValue) {
      String varKey = "${var:";
      if (key != null && !key.startsWith(varKey)) {
         Iterator i = this.hMsgs.values().iterator();

         while(i.hasNext()) {
            Msg msg = (Msg)i.next();
            msg.setKey(ValidatorUtils.replace(msg.getKey(), key, replaceValue));
         }
      }

      this.processArg(key, replaceValue);
   }

   private void processArg(String key, String replaceValue) {
      for(int i = 0; i < this.args.length; ++i) {
         Map argMap = this.args[i];
         if (argMap != null) {
            Iterator iter = argMap.values().iterator();

            while(iter.hasNext()) {
               Arg arg = (Arg)iter.next();
               if (arg != null) {
                  arg.setKey(ValidatorUtils.replace(arg.getKey(), key, replaceValue));
               }
            }
         }
      }

   }

   public boolean isDependency(String validatorName) {
      return this.dependencyList.contains(validatorName);
   }

   public List getDependencyList() {
      return Collections.unmodifiableList(this.dependencyList);
   }

   public Object clone() {
      Field field = null;

      try {
         field = (Field)super.clone();
      } catch (CloneNotSupportedException var7) {
         throw new RuntimeException(var7.toString());
      }

      field.args = new Map[this.args.length];

      for(int i = 0; i < this.args.length; ++i) {
         if (this.args[i] != null) {
            Map argMap = new HashMap(this.args[i]);
            Iterator iter = argMap.keySet().iterator();

            while(iter.hasNext()) {
               String validatorName = (String)iter.next();
               Arg arg = (Arg)argMap.get(validatorName);
               argMap.put(validatorName, arg.clone());
            }

            field.args[i] = argMap;
         }
      }

      field.hVars = ValidatorUtils.copyFastHashMap(this.hVars);
      field.hMsgs = ValidatorUtils.copyFastHashMap(this.hMsgs);
      return field;
   }

   public String toString() {
      StringBuffer results = new StringBuffer();
      results.append("\t\tkey = " + this.key + "\n");
      results.append("\t\tproperty = " + this.property + "\n");
      results.append("\t\tindexedProperty = " + this.indexedProperty + "\n");
      results.append("\t\tindexedListProperty = " + this.indexedListProperty + "\n");
      results.append("\t\tdepends = " + this.depends + "\n");
      results.append("\t\tpage = " + this.page + "\n");
      results.append("\t\tfieldOrder = " + this.fieldOrder + "\n");
      if (this.hVars != null) {
         results.append("\t\tVars:\n");
         Iterator i = this.hVars.keySet().iterator();

         while(i.hasNext()) {
            Object key = i.next();
            results.append("\t\t\t");
            results.append(key);
            results.append("=");
            results.append(this.hVars.get(key));
            results.append("\n");
         }
      }

      return results.toString();
   }

   Object[] getIndexedProperty(Object bean) throws ValidatorException {
      Object indexedProperty = null;

      try {
         indexedProperty = PropertyUtils.getProperty(bean, this.getIndexedListProperty());
      } catch (IllegalAccessException var6) {
         throw new ValidatorException(var6.getMessage());
      } catch (InvocationTargetException var7) {
         throw new ValidatorException(var7.getMessage());
      } catch (NoSuchMethodException var8) {
         throw new ValidatorException(var8.getMessage());
      }

      if (indexedProperty instanceof Collection) {
         return ((Collection)indexedProperty).toArray();
      } else if (indexedProperty.getClass().isArray()) {
         return (Object[])indexedProperty;
      } else {
         throw new ValidatorException(this.getKey() + " is not indexed");
      }
   }

   private boolean validateForRule(ValidatorAction va, ValidatorResults results, Map actions, Map params, int pos) throws ValidatorException {
      ValidatorResult result = results.getValidatorResult(this.getKey());
      if (result != null && result.containsAction(va.getName())) {
         return result.isValid(va.getName());
      } else {
         return !this.runDependentValidators(va, results, actions, params, pos) ? false : va.executeValidationMethod(this, params, results, pos);
      }
   }

   private boolean runDependentValidators(ValidatorAction va, ValidatorResults results, Map actions, Map params, int pos) throws ValidatorException {
      List dependentValidators = va.getDependencyList();
      if (dependentValidators.isEmpty()) {
         return true;
      } else {
         Iterator iter = dependentValidators.iterator();

         ValidatorAction action;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            String depend = (String)iter.next();
            action = (ValidatorAction)actions.get(depend);
            if (action == null) {
               this.handleMissingAction(depend);
            }
         } while(this.validateForRule(action, results, actions, params, pos));

         return false;
      }
   }

   public ValidatorResults validate(Map params, Map actions) throws ValidatorException {
      if (this.getDepends() == null) {
         return new ValidatorResults();
      } else {
         ValidatorResults allResults = new ValidatorResults();
         Object bean = params.get("java.lang.Object");
         int numberOfFieldsToValidate = this.isIndexed() ? this.getIndexedProperty(bean).length : 1;

         for(int fieldNumber = 0; fieldNumber < numberOfFieldsToValidate; ++fieldNumber) {
            Iterator dependencies = this.dependencyList.iterator();

            while(dependencies.hasNext()) {
               String depend = (String)dependencies.next();
               ValidatorAction action = (ValidatorAction)actions.get(depend);
               if (action == null) {
                  this.handleMissingAction(depend);
               }

               ValidatorResults results = new ValidatorResults();
               boolean good = this.validateForRule(action, results, actions, params, fieldNumber);
               allResults.merge(results);
               if (!good) {
                  return allResults;
               }
            }
         }

         return allResults;
      }
   }

   private void handleMissingAction(String name) throws ValidatorException {
      throw new ValidatorException("No ValidatorAction named " + name + " found for field " + this.getProperty());
   }

   protected Map getMsgMap() {
      return this.hMsgs;
   }

   protected Map getVarMap() {
      return this.hVars;
   }
}
