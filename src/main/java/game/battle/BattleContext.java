package game.battle;
public class BattleContext{
    private Integer turnNumber;
    private Trainer player,opponent;
    public BattleContext(Integer turnNumber,Trainer player,Trainer opponent){
        this.turnNumber=turnNumber;
        this.player=player;
        this.opponent=opponent;
    }
    public Integer getTurnNumber(){return turnNumber;}
    public Trainer getPlayer(){return player;}
    public Trainer getOpponent(){return opponent;}
    public void setTurnNumber(Integer turnNumber){this.turnNumber=turnNumber;}
    public void setPlayer(Trainer player){this.player=player;}
    public void setOpponent(Trainer opponent){this.opponent=opponent;}

    /*
    public Boolean canCapture(Trainer actor,Pokemon target){

    }
    public Boolean isValidAction(Trainer actor,CombatAction action){

    }
    */
}