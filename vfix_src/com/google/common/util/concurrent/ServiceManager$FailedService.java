package com.google.common.util.concurrent;

final class ServiceManager$FailedService extends Throwable {
   ServiceManager$FailedService(Service service) {
      super(service.toString(), service.failureCause(), false, false);
   }
}
