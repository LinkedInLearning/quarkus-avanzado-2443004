package com.kineteco;

import com.kineteco.service.CustomerService;

import javax.inject.Inject;

public class ViewCustomerCommand {
   Long customerId;

   @Inject
   CustomerService customerService;
}

