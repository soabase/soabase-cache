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
