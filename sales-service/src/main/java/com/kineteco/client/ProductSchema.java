package com.kineteco.client;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.types.java.math.BigDecimalAdapter;

@AutoProtoSchemaBuilder(includeClasses= { Product.class, BigDecimalAdapter.class },
      schemaPackageName = "kineteco")
public interface ProductSchema extends GeneratedSchema {
}
