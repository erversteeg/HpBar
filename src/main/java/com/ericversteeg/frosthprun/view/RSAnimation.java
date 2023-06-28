package com.ericversteeg.frosthprun.view;

import java.time.Instant;

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
    private boolean easeOut = false;

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

    public RSAnimation easeOut()
    {
        easeOut = true;
        return this;
    }

    public RSAnimation start()
    {
        view.addAnimation(type, this);
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
        if (easeOut)
        {
            t = easeOut(t);
        }

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
            view.removeAnimation(type);
            if (onComplete != null)
            {
                onComplete.onComplete();
            }
        }

        return value;
    }

    private float easeOut(float t)
    {
        return (float) (1 - Math.pow(1 - t, 3));
    }

    public interface OnComplete
    {
        void onComplete();
    }
}
