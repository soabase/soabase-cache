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
import java.util.List;
import java.util.stream.Collectors;

public class StandardKeyPartCombiner implements KeyPartCombiner
{
    public static final KeyPartCombiner instance = new StandardKeyPartCombiner();

    private static final String DEFAULT_SEPARATOR = "-";
    private final String separator;

    public StandardKeyPartCombiner()
    {
        this(DEFAULT_SEPARATOR);
    }

    public StandardKeyPartCombiner(String separator)
    {
        this.separator = Preconditions.checkNotNull(separator, "separator cannot be null");
    }

    @Override
    public String toKey(List<KeyPart> keyParts)
    {
        return keyParts.stream().map(part -> {
            switch ( part.getElementType() )
            {
                default:
                {
                    return part.getValue();
                }

                case TYPE:
                case METHOD:
                {
                    return part.getValue().isEmpty() ? part.getElementValue() : part.getValue();
                }

                case PARAMETER:
                {
                    return part.getValue().isEmpty() ? part.getElementValue() : (part.getValue() + separator + part.getElementValue());
                }
            }
        })
        .filter(value -> !value.isEmpty())
        .collect(Collectors.joining(separator));
    }
}
