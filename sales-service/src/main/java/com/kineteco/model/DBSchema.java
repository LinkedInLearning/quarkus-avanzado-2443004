package com.kineteco.model;

import com.kineteco.client.Product;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.types.java.math.BigDecimalAdapter;

@AutoProtoSchemaBuilder(includeClasses= { Customer.class },
      schemaPackageName = "db.kineteco")
public interface DBSchema extends GeneratedSchema {
}
