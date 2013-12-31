/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codefollower.lealone.atomicdb.db.marshal;

import java.nio.ByteBuffer;

import com.codefollower.lealone.atomicdb.cql.CQL3Type;
import com.codefollower.lealone.atomicdb.serializers.LongSerializer;
import com.codefollower.lealone.atomicdb.serializers.MarshalException;
import com.codefollower.lealone.atomicdb.serializers.TypeSerializer;
import com.codefollower.lealone.atomicdb.utils.ByteBufferUtil;

public class LongType extends AbstractType<Long>
{
    public static final LongType instance = new LongType();

    LongType() {} // singleton

    public int compare(ByteBuffer o1, ByteBuffer o2)
    {
        return compareLongs(o1, o2);
    }

    public static int compareLongs(ByteBuffer o1, ByteBuffer o2)
    {
        if (o1.remaining() == 0)
        {
            return o2.remaining() == 0 ? 0 : -1;
        }
        if (o2.remaining() == 0)
        {
            return 1;
        }

        int diff = o1.get(o1.position() + o1.arrayOffset()) - o2.get(o2.position() + o2.arrayOffset());
        if (diff != 0)
            return diff;

        return ByteBufferUtil.compareUnsigned(o1, o2);
    }

    public ByteBuffer fromString(String source) throws MarshalException
    {
        // Return an empty ByteBuffer for an empty string.
        if (source.isEmpty())
            return ByteBufferUtil.EMPTY_BYTE_BUFFER;

        long longType;

        try
        {
            longType = Long.parseLong(source);
        }
        catch (Exception e)
        {
            throw new MarshalException(String.format("unable to make long from '%s'", source), e);
        }

        return decompose(longType);
    }

    public CQL3Type asCQL3Type()
    {
        return CQL3Type.Native.BIGINT;
    }

    public TypeSerializer<Long> getSerializer()
    {
        return LongSerializer.instance;
    }
}