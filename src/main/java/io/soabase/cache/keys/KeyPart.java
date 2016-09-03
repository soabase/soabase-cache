/**
 * Copyright 2016 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.cache.keys;

import com.google.common.base.Preconditions;
import java.lang.annotation.ElementType;

public class KeyPart
{
    private final String value;
    private final String elementValue;
    private final ElementType elementType;

    public KeyPart(String value, String elementValue, ElementType elementType)
    {
        this.value = Preconditions.checkNotNull(value, "value; cannot be null");
        this.elementValue = Preconditions.checkNotNull(elementValue, "elementValue cannot be null");
        this.elementType = Preconditions.checkNotNull(elementType, "elementType cannot be null");
    }

    public String getValue()
    {
        return value;
    }

    public ElementType getElementType()
    {
        return elementType;
    }

    public String getElementValue()
    {
        return elementValue;
    }

    @Override
    public boolean equals(Object o)
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        KeyPart keyPart = (KeyPart)o;

        if ( !value.equals(keyPart.value) )
        {
            return false;
        }
        //noinspection SimplifiableIfStatement
        if ( !elementValue.equals(keyPart.elementValue) )
        {
            return false;
        }
        return elementType == keyPart.elementType;

    }

    @Override
    public int hashCode()
    {
        int result = value.hashCode();
        result = 31 * result + elementValue.hashCode();
        result = 31 * result + elementType.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "KeyPart{" + "value='" + value + '\'' + ", elementValue='" + elementValue + '\'' + ", elementType=" + elementType + '}';
    }
}
