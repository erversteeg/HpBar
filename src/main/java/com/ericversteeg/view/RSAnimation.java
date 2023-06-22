package com.ericversteeg.view;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class RSAnimation
{
    enum Type
    {
        FADE_IN,
        FADE_OUT,
        INTERPOLATE
    }

    private RSView view;
    private Type type;
    private float duration;
    private long start;
    private float startOpacity;
    private float from;
    private float to;
    private OnComplete onComplete;

    public RSAnimation(RSView view)
    {
        this.view = view;
    }

    public RSAnimation fadeIn()
    {
        type = Type.FADE_IN;
        this.startOpacity = view.opacity;
        return this;
    }

    public RSAnimation fadeOut()
    {
        type = Type.FADE_OUT;
        this.startOpacity = view.opacity;
        return this;
    }

    public RSAnimation interpolate(float from, float to)
    {
        type = Type.INTERPOLATE;
        //System.out.println("Type set to " + type);
        this.from = from;
        this.to = to;
        return this;
    }

    public RSAnimation duration(float duration)
    {
        this.duration = duration;
        return this;
    }

    public RSAnimation start()
    {
        this.start = Instant.now().toEpochMilli();
        //System.out.println("Start called");
        return this;
    }

    public RSAnimation onComplete(OnComplete onComplete)
    {
        this.onComplete = onComplete;
        return this;
    }

    public Type getType()
    {
        return type;
    }

    public float getValue()
    {
        float t = Math.min((Instant.now().toEpochMilli() - start) / 1000f / duration, 1f);
        float value = 0f;

        //System.out.println("T is " + t);

        switch (type)
        {
            case FADE_IN:
                value = t * startOpacity;
                break;
            case FADE_OUT:
                value = (1 - t) * startOpacity;
                break;
            case INTERPOLATE:
                value = (to - from) * t + from;
                break;
        }

        if (t >= 1f)
        {
            //System.out.println("Finished animating, type is " + getType());
            view.setAnimation(null);
            if (onComplete != null)
            {
                onComplete.onComplete();
            }
        }

        return value;
    }

    public interface OnComplete
    {
        void onComplete();
    }
}
