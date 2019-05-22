package org.apache.maven.scm;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandParameters implements Serializable {
   private static final long serialVersionUID = -7346070735958137283L;
   private Map<String, Object> parameters = new HashMap();

   public String getString(CommandParameter parameter) throws ScmException {
      Object object = this.getObject(String.class, parameter);
      if (object == null) {
         throw new ScmException("Missing parameter: '" + parameter.getName() + "'.");
      } else {
         return object.toString();
      }
   }

   public String getString(CommandParameter parameter, String defaultValue) throws ScmException {
      Object object = this.getObject(String.class, parameter, (Object)null);
      return object == null ? defaultValue : object.toString();
   }

   public void setString(CommandParameter parameter, String value) throws ScmException {
      this.setObject(parameter, value);
   }

   public int getInt(CommandParameter parameter) throws ScmException {
      return (Integer)this.getObject(Integer.class, parameter);
   }

   public int getInt(CommandParameter parameter, int defaultValue) throws ScmException {
      Integer value = (Integer)this.getObject(Integer.class, parameter, (Object)null);
      return value == null ? defaultValue : value;
   }

   public void setInt(CommandParameter parameter, int value) throws ScmException {
      this.setObject(parameter, value);
   }

   public Date getDate(CommandParameter parameter) throws ScmException {
      return (Date)this.getObject(Date.class, parameter);
   }

   public Date getDate(CommandParameter parameter, Date defaultValue) throws ScmException {
      return (Date)this.getObject(Date.class, parameter, defaultValue);
   }

   public void setDate(CommandParameter parameter, Date date) throws ScmException {
      this.setObject(parameter, date);
   }

   public boolean getBoolean(CommandParameter parameter) throws ScmException {
      return Boolean.valueOf(this.getString(parameter));
   }

   public boolean getBoolean(CommandParameter parameter, boolean defaultValue) throws ScmException {
      return Boolean.valueOf(this.getString(parameter, Boolean.toString(defaultValue)));
   }

   public ScmVersion getScmVersion(CommandParameter parameter) throws ScmException {
      return (ScmVersion)this.getObject(ScmVersion.class, parameter);
   }

   public ScmVersion getScmVersion(CommandParameter parameter, ScmVersion defaultValue) throws ScmException {
      return (ScmVersion)this.getObject(ScmVersion.class, parameter, defaultValue);
   }

   public void setScmVersion(CommandParameter parameter, ScmVersion scmVersion) throws ScmException {
      this.setObject(parameter, scmVersion);
   }

   public File[] getFileArray(CommandParameter parameter) throws ScmException {
      return (File[])((File[])this.getObject(File[].class, parameter));
   }

   public File[] getFileArray(CommandParameter parameter, File[] defaultValue) throws ScmException {
      return (File[])((File[])this.getObject(File[].class, parameter, defaultValue));
   }

   public ScmTagParameters getScmTagParameters(CommandParameter parameter) throws ScmException {
      return (ScmTagParameters)this.getObject(ScmTagParameters.class, parameter, new ScmTagParameters());
   }

   public void setScmTagParameters(CommandParameter parameter, ScmTagParameters scmTagParameters) throws ScmException {
      this.setObject(parameter, scmTagParameters);
   }

   public void setScmBranchParameters(CommandParameter parameter, ScmBranchParameters scmBranchParameters) throws ScmException {
      this.setObject(parameter, scmBranchParameters);
   }

   public ScmBranchParameters getScmBranchParameters(CommandParameter parameter) throws ScmException {
      return (ScmBranchParameters)this.getObject(ScmBranchParameters.class, parameter, new ScmBranchParameters());
   }

   private Object getObject(Class<?> clazz, CommandParameter parameter) throws ScmException {
      Object object = this.getObject(clazz, parameter, (Object)null);
      if (object == null) {
         throw new ScmException("Missing parameter: '" + parameter.getName() + "'.");
      } else {
         return object;
      }
   }

   private Object getObject(Class<?> clazz, CommandParameter parameter, Object defaultValue) throws ScmException {
      Object object = this.parameters.get(parameter.getName());
      if (object == null) {
         return defaultValue;
      } else if (clazz != null && !clazz.isAssignableFrom(object.getClass())) {
         throw new ScmException("Wrong parameter type for '" + parameter.getName() + ". " + "Expected: " + clazz.getName() + ", got: " + object.getClass().getName());
      } else {
         return object;
      }
   }

   private void setObject(CommandParameter parameter, Object value) throws ScmException {
      Object object = this.getObject((Class)null, parameter, (Object)null);
      if (object != null) {
         throw new ScmException("The parameter is already set: " + parameter.getName());
      } else {
         this.parameters.put(parameter.getName(), value);
      }
   }

   public void remove(CommandParameter parameter) {
      this.parameters.remove(parameter);
   }
}
