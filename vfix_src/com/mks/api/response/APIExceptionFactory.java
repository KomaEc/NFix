package com.mks.api.response;

import com.mks.api.IntegrationPointFactory;
import com.mks.api.util.MKSLogger;
import java.text.MessageFormat;

public class APIExceptionFactory {
   private static final String EXCEPTION_CLASS_PREFIX = "com.mks.api.response.";
   private static final String UNKNOWN_EXCEPTION_CLASS_MSG = "Unknown exception class: {0}";
   private static final String EXCEPTION_CLASS_CONSTRUCTION_ERROR_MSG = "Error creating the exception object for: {0}";

   public static APIException createAPIException(String className, Response response) {
      APIException apiException = null;
      Object obj = getAPIExceptionObject(className);
      if (obj instanceof APIError) {
         APIError err = (APIError)obj;
         throw err;
      } else {
         apiException = (APIException)obj;
         apiException.setResponse(response);
         return apiException;
      }
   }

   public static APIException createAPIException(String className, String msg) {
      Object obj = getAPIExceptionObject(className);
      if (obj instanceof APIError) {
         APIError err = (APIError)obj;
         err.setMessage(msg);
         throw err;
      } else {
         APIException apiException = (APIException)obj;
         apiException.setMessage(msg);
         return apiException;
      }
   }

   private static APIException handleException(String msgKey, String className, Exception ex) {
      String logMsg = MessageFormat.format(msgKey, className);
      MKSLogger apiLogger = IntegrationPointFactory.getLogger();
      apiLogger.message((Class)APIExceptionFactory.class, "API", 0, logMsg);
      apiLogger.exception((Class)APIExceptionFactory.class, "API", 0, ex);
      APIException apiException = new APIException();
      apiException.addField("original-exception", "com.mks.api.response." + className);
      apiException.setMessage(ex.getMessage());
      return apiException;
   }

   private static Object getAPIExceptionObject(String className) {
      Object obj = null;

      try {
         Class clazz = Class.forName("com.mks.api.response." + className);
         obj = clazz.newInstance();
         if (obj instanceof APIError) {
            APIError err = (APIError)obj;
            err.setShowStackTrace(false);
         } else {
            APIException apiException = (APIException)obj;
            apiException.setShowStackTrace(false);
         }
      } catch (ClassNotFoundException var4) {
         obj = handleException("Unknown exception class: {0}", className, var4);
      } catch (InstantiationException var5) {
         obj = handleException("Error creating the exception object for: {0}", className, var5);
      } catch (IllegalAccessException var6) {
         obj = handleException("Error creating the exception object for: {0}", className, var6);
      } catch (Exception var7) {
         obj = handleException("Unknown exception class: {0}", className, var7);
      }

      return obj;
   }
}
