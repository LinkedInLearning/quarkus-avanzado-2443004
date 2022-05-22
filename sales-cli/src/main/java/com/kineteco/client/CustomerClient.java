package com.kineteco.client;

import com.kineteco.model.Customer;
import io.smallrye.graphql.client.typesafe.api.ErrorOr;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

@GraphQLClientApi(configKey = "customers")
public interface CustomerClient {

   @Query(value="getCustomer")
   ErrorOr<Customer> customerDetail(String customerId);

   @Mutation(value="createOrUpdateCustomer")
   Customer updateEmail(Customer customer);
}
