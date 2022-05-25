package com.kineteco.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MetricsService {

   @Inject
   MeterRegistry registry;

   public void countStocks(int stock) {
      if (stock == 1 ) {
         registry
               .counter("kineteco.stock.number", "type", "one")
               .increment();
      }

      if (stock > 100 ) {
         registry
               .counter("kineteco.stock.number", "type", "greater100")
               .increment();
      }
   }
}
