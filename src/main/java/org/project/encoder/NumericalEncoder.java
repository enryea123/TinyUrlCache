package org.project.encoder;

public class NumericalEncoder implements IEncoder {
    private static int count;

    public NumericalEncoder() {
        count = 0;
    }

    @Override
    public String encode(String url) {
        count += 1;
        return String.valueOf(count);
    }
}
