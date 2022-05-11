package com.kineteco.service;

import com.kineteco.model.Customer;
import io.quarkus.runtime.configuration.ProfileManager;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

@ApplicationScoped
public class Scheduler {
   @Scheduled(every="{kineteco.task}", skipExecutionIf = SkipIfNotDev.class)
   void increment() {
      System.out.println(Customer.count());
   }
}
@Singleton
class SkipIfNotDev implements Scheduled.SkipPredicate {
   public boolean test(ScheduledExecution execution) {
      return !"dev".equals(ProfileManager.getActiveProfile());
   }
}
