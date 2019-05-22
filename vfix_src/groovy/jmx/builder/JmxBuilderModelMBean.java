package groovy.jmx.builder;

import groovy.lang.Closure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.AttributeChangeNotification;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

public class JmxBuilderModelMBean extends RequiredModelMBean implements NotificationListener {
   private List<String> methodListeners = new ArrayList(0);
   private Object managedObject;

   public JmxBuilderModelMBean(Object objectRef) throws MBeanException, RuntimeOperationsException, InstanceNotFoundException, InvalidTargetObjectTypeException {
      super.setManagedResource(objectRef, "ObjectReference");
   }

   public JmxBuilderModelMBean() throws MBeanException, RuntimeOperationsException {
   }

   public JmxBuilderModelMBean(ModelMBeanInfo mbi) throws MBeanException, RuntimeOperationsException {
      super(mbi);
   }

   public synchronized void setManagedResource(Object obj) {
      this.managedObject = obj;

      try {
         super.setManagedResource(obj, "ObjectReference");
      } catch (Exception var3) {
         throw new JmxBuilderException(var3);
      }
   }

   public void addOperationCallListeners(Map<String, Map> descriptor) {
      if (descriptor != null) {
         Iterator i$ = descriptor.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, Map> item = (Entry)i$.next();
            if (((Map)item.getValue()).containsKey("methodListener")) {
               Map listener = (Map)((Map)item.getValue()).get("methodListener");
               String target = (String)listener.get("target");
               this.methodListeners.add(target);
               String listenerType = (String)listener.get("type");
               listener.put("managedObject", this.managedObject);
               if (listenerType.equals("attributeChangeListener")) {
                  try {
                     this.addAttributeChangeNotificationListener(JmxBuilderModelMBean.AttributeChangedListener.getListener(), (String)listener.get("attribute"), listener);
                  } catch (MBeanException var9) {
                     throw new JmxBuilderException(var9);
                  }
               }

               if (listenerType.equals("operationCallListener")) {
                  String eventType = "jmx.operation.call." + target;
                  NotificationFilterSupport filter = new NotificationFilterSupport();
                  filter.enableType(eventType);
                  this.addNotificationListener(JmxEventListener.getListner(), filter, listener);
               }
            }
         }

      }
   }

   public void addEventListeners(MBeanServer server, Map<String, Map> descriptor) {
      Iterator i$ = descriptor.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, Map> item = (Entry)i$.next();
         Map<String, Object> listener = (Map)item.getValue();
         ObjectName broadcaster = (ObjectName)listener.get("from");

         try {
            String eventType = (String)listener.get("event");
            if (eventType != null) {
               NotificationFilterSupport filter = new NotificationFilterSupport();
               filter.enableType(eventType);
               server.addNotificationListener(broadcaster, JmxEventListener.getListner(), filter, listener);
            } else {
               server.addNotificationListener(broadcaster, JmxEventListener.getListner(), (NotificationFilter)null, listener);
            }
         } catch (InstanceNotFoundException var9) {
            throw new JmxBuilderException(var9);
         }
      }

   }

   public Object invoke(String opName, Object[] opArgs, String[] signature) throws MBeanException, ReflectionException {
      Object result = super.invoke(opName, opArgs, signature);
      if (this.methodListeners.contains(opName)) {
         this.sendNotification(this.buildCallListenerNotification(opName));
      }

      return result;
   }

   public void handleNotification(Notification note, Object handback) {
   }

   private Notification buildCallListenerNotification(String target) {
      return new Notification("jmx.operation.call." + target, this, JmxBuilderModelMBean.NumberSequencer.getNextSequence(), System.currentTimeMillis());
   }

   private static final class AttributeChangedListener implements NotificationListener {
      private static JmxBuilderModelMBean.AttributeChangedListener listener;

      public static synchronized JmxBuilderModelMBean.AttributeChangedListener getListener() {
         if (listener == null) {
            listener = new JmxBuilderModelMBean.AttributeChangedListener();
         }

         return listener;
      }

      public void handleNotification(Notification notification, Object handback) {
         AttributeChangeNotification note = (AttributeChangeNotification)notification;
         Map event = (Map)handback;
         if (event != null) {
            Object del = event.get("managedObject");
            Object callback = event.get("callback");
            if (callback != null && callback instanceof Closure) {
               Closure closure = (Closure)callback;
               closure.setDelegate(del);
               if (closure.getMaximumNumberOfParameters() == 1) {
                  closure.call((Object)buildAttributeNotificationPacket(note));
               } else {
                  closure.call();
               }
            }
         }

      }

      private static Map buildAttributeNotificationPacket(AttributeChangeNotification note) {
         Map<String, Object> result = new HashMap();
         result.put("oldValue", note.getOldValue());
         result.put("newValue", note.getNewValue());
         result.put("attribute", note.getAttributeName());
         result.put("attributeType", note.getAttributeType());
         result.put("sequenceNumber", note.getSequenceNumber());
         result.put("timeStamp", note.getTimeStamp());
         return result;
      }
   }

   private static class NumberSequencer {
      private static AtomicLong num = new AtomicLong(0L);

      public static long getNextSequence() {
         return num.incrementAndGet();
      }
   }
}
