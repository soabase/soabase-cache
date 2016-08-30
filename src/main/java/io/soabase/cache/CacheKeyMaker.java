package io.soabase.cache;

import com.google.common.base.Joiner;
import java.lang.reflect.Method;

public class CacheKeyMaker
{
    public static String makeKey(String value, Method method, Object[] args)
    {
        String name = ((value == null) || value.isEmpty()) ? method.getName() : value;
        StringBuilder key = new StringBuilder();
        key.append(method.getDeclaringClass().getName()).append("#").append(name).append("(");
        if ( args != null )
        {
            Object[] strs = new Object[args.length];
            for ( int i = 0; i < args.length; ++i )
            {
                if ( args[i] instanceof CacheKeyAccessor )
                {
                    strs[i] = ((CacheKeyAccessor)args[i]).getCacheKey();
                }
                else
                {
                    strs[i] = String.valueOf(args[i]);
                }
            }
            Joiner.on(",").appendTo(key, strs);
        }
        key.append(")");
        return key.toString();
    }

    private CacheKeyMaker()
    {
    }
}
