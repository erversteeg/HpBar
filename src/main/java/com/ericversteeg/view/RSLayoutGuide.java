package com.ericversteeg.view;

import java.util.HashMap;
import java.util.Map;

public enum RSLayoutGuide {
    MAX_WIDTH,
    MAX_HEIGHT,
    START,
    TOP,
    END,
    BOTTOM;

    static class Builder
    {
        private Map<RSLayoutGuide, Integer> guides = new HashMap<>();

        public Builder maxWidth(int w)
        {
            guides.put(MAX_WIDTH, w);

            return this;
        }

        public Builder maxHeight(int h)
        {
            guides.put(MAX_HEIGHT, h);

            return this;
        }

        public Builder start(int s)
        {
            guides.put(START, s);

            return this;
        }

        public Builder top(int t)
        {
            guides.put(TOP, t);

            return this;
        }

        public Builder end(int e)
        {
            guides.put(END, e);

            return this;
        }

        public Builder bottom(int b)
        {
            guides.put(BOTTOM, b);

            return this;
        }

        public Map<RSLayoutGuide, Integer> build()
        {
            return guides;
        }
    }
}
