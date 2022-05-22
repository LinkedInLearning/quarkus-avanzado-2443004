package com.kineteco.graphql;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("customer-not-found")
public class CustomerNotFoundException extends RuntimeException {
}
