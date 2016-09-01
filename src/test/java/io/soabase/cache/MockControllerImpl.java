package io.soabase.cache;

import java.util.Random;

public class MockControllerImpl implements MockController
{
    private final Random random = new Random();

    @Override
    public long getValue()
    {
        return random.nextLong();
    }

    @Override
    public long makeValue(int plus)
    {
        return random.nextLong() + plus;
    }

    @Override
    public void clear()
    {
        // NOP
    }
}
