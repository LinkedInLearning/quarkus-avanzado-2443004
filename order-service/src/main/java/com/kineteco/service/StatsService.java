package com.kineteco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.model.OrderStat;
import io.vertx.core.impl.ConcurrentHashSet;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;


@ApplicationScoped
public class StatsService {
   private static final Logger LOGGER = Logger.getLogger(StatsService.class);

   @Inject
   ObjectMapper mapper;

   private final Collection<OrderStat> stats = new ConcurrentHashSet<>();

}
