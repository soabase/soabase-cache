package io.soabase.cache.keys;

import java.util.List;
import java.util.stream.Collectors;

public class StandardKeyPartCombiner implements KeyPartCombiner
{
    public static final KeyPartCombiner instance = new StandardKeyPartCombiner();

    private static final String SEPARATOR = "-";

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
                {
                    return part.getValue().isEmpty() ? part.getElementValue() : part.getValue();
                }

                case PARAMETER:
                {
                    return part.getValue().isEmpty() ? part.getElementValue() : (part.getValue() + SEPARATOR + part.getElementValue());
                }
            }
        })
        .filter(value -> !value.isEmpty())
        .collect(Collectors.joining(SEPARATOR));
    }
}
