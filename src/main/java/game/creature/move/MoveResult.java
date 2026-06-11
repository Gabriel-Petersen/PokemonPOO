package game.creature.move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveResult {
    private final List<String> resultMessage = new ArrayList<>();
    private Boolean hit;
    private Integer damageApplied;
    private Boolean statusApplied;

    public MoveResult(String resultMessage, Boolean hit, Integer damageApplied, Boolean statusApplied) {
        this.resultMessage.add(resultMessage);
        this.hit = hit;
        this.damageApplied = damageApplied;
        this.statusApplied = statusApplied;
    }
    
    public MoveResult(String resultMessage, Boolean hit) {
        this.resultMessage.add(resultMessage);
        this.hit = hit;
        damageApplied = 0;
        statusApplied = false;
    }

    public MoveResult(Boolean hit, Integer damageApplied, Boolean statusApplied) {
        this.hit = hit;
        this.damageApplied = damageApplied;
        this.statusApplied = statusApplied;
    }
    
    public MoveResult(Boolean hit) {
        this.hit = hit;
        damageApplied = 0;
        statusApplied = false;
    }

    public void addMessage(String message) { resultMessage.add(message); }

    public Boolean getHit() {
        return hit;
    }
    public void setHit(Boolean hit) {
        this.hit = hit;
    }
    public Integer getDamageApplied() {
        return damageApplied;
    }
    public void setDamageApplied(Integer damageApplied) {
        this.damageApplied = damageApplied;
    }
    public Boolean getStatusApplied() {
        return statusApplied;
    }
    public void setStatusApplied(Boolean statusApplied) {
        this.statusApplied = statusApplied;
    }
    
    public Collection<String> getResultMessages() { return resultMessage; }
}