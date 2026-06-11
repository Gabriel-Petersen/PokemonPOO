package game.creature.move;

public class MoveResult {
    private String resultMessage;
    private Boolean hit;
    private Integer damageApplied;
    private Boolean statusApplied;

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
    public MoveResult(String resultMessage, Boolean hit, Integer damageApplied, Boolean statusApplied) {
        this.resultMessage = resultMessage;
        this.hit = hit;
        this.damageApplied = damageApplied;
        this.statusApplied = statusApplied;
    }
    
    public MoveResult(boolean b, String string) {
        this.hit=b;
        this.resultMessage=string;
    }
    public String getResultMessage() { return resultMessage; }
    
    public void setResultMessage(String resultMessage) { this.resultMessage = resultMessage; }
}