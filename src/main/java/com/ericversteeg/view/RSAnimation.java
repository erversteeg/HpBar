package com.ericversteeg.view;

import java.time.Instant;

public class RSAnimation
{
    enum Type
    {
        FADE_IN,
        FADE_OUT
    }

    private RSView view;
    private Type type;
    private float duration;
    private long start;
    private OnComplete onComplete;

    public RSAnimation(RSView view)
    {
        this.view = view;
    }

    public RSAnimation fadeIn()
    {
        type = Type.FADE_IN;
        return this;
    }

    public RSAnimation fadeOut()
    {
        type = Type.FADE_OUT;
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
        this.view.setAnimating(true);
        return this;
    }

    public RSAnimation onComplete(OnComplete onComplete)
    {
        this.onComplete = onComplete;
        return this;
    }

    public void render()
    {
        float t = Math.min((Instant.now().toEpochMilli() - start) / 1000f / duration, 1f);
        switch (type)
        {
            case FADE_IN:
                view.setOpacity(t);
                break;
            case FADE_OUT:
                view.setOpacity(1 - t);
        }

        if (t >= 1f)
        {
            view.setAnimating(false);
            if (onComplete != null)
            {
                onComplete.onComplete();
            }
        }
    }

    public interface OnComplete
    {
        void onComplete();
    }
}
