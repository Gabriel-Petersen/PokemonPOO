package engine.events;

import engine.animation.Animator;

public class AnimationEvent extends GameEvent
{
    private final Animator animator;
    private final String originalAnim;
    private final String animName;
    private boolean isOver = false;

    public AnimationEvent(
            EventScheduler fatherScheduler, Animator animator, String animName
    ) {
        super(fatherScheduler);
        this.animator = animator;
        originalAnim = animator.getCurrentAnim().getName();
        this.animName = animName;
    }

    @Override
    public boolean isFinished() {
        return isOver;
    }

    @Override
    public void update(double dt)
    {
        animator.update();
        if (!isOver && isFinished()) {
            isOver = true;
            scheduler.enqueue(new LambdaEvent(() -> animator.play(originalAnim)));
        }
    }

    @Override
    public void init() {
        animator.play(animName);
    }
}
