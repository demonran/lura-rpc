package org.lura.rpc.serializer;

import java.io.IOException;

public interface Serializer {

    byte[] serialize(Object obj) throws IOException;


    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
