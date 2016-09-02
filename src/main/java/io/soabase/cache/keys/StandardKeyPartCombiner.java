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
