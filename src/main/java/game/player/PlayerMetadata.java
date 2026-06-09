package game.player;

import game.battle.ActionResult;
import java.util.Date;

public class PlayerMetadata {
    public static final double WK_SPEED = 3;
    public static final double RUN_SPEED = 7;

    private Integer money;
    private String name;
    private Integer enemiesWinned;
    private Date gameStartTime;

    public PlayerMetadata() {
        this.money = 0;
        this.enemiesWinned = 0;
        this.gameStartTime = new Date(System.currentTimeMillis());
    }

    public Integer getMoney() { return money; }
    public String getName() { return name; }
    public Integer getEnemiesWinned() { return enemiesWinned; }
    public Date getGameStartTime() { return gameStartTime; }
    
    public void setMoney(Integer money) { this.money = money; }
    public void setName(String name) { this.name = name; }
    public void setGameStartTime(Date startTime) { gameStartTime = startTime; }
    
    public void addNewWin() { ++enemiesWinned; }

    public ActionResult addMoney(Integer amount){
        if(amount <= 0)
            return ActionResult.FAILED;
        money += amount;
        return ActionResult.SUCCESS;
    }

    public ActionResult removeMoney(Integer amount){
        if(amount <= 0)
            return ActionResult.FAILED;
        if(money < amount)
            return ActionResult.FAILED;
        money -= amount;
        return ActionResult.SUCCESS;
    }
}
