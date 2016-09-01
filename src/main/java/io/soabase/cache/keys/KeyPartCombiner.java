package io.soabase.cache.keys;

import java.util.List;

public interface KeyPartCombiner
{
    String toKey(List<KeyPart> keyParts);
}
