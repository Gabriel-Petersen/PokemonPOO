package game.battle;

import game.ui.battle.BattleHud;

public class BattleContext{
    private Integer turnNumber;
    private Trainer player,opponent;
    private final BattleHud hud;
    public BattleContext(BattleHud hud, Trainer player,Trainer opponent){
        this.turnNumber=0;
        this.player=player;
        this.opponent=opponent;
        this.hud = hud;
    }
    public Integer getTurnNumber(){return turnNumber;}
    public Trainer getPlayer(){return player;}
    public Trainer getOpponent(){return opponent;}
    public BattleHud getHud(){return hud;}
    public void setTurnNumber(Integer turnNumber){this.turnNumber=turnNumber;}
    public void setPlayer(Trainer player){this.player=player;}
    public void setOpponent(Trainer opponent){this.opponent=opponent;}
}