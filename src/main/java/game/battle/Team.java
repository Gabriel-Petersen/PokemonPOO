package game.battle;
import game.creature.Pokemon;
import java.util.ArrayList;
import java.util.List;
public class Team{
    private final List<Pokemon>members=new ArrayList<>();
    private Integer activeIndex = 0;
    public Integer getActiveIndex(){return activeIndex;}
    public void setActiveIndex(Integer activeIndex){this.activeIndex=activeIndex;}
    public List<Pokemon> getMembers(){return members;}
    public void addMember(Pokemon pokemon){if(members.size()<6)members.add(pokemon);}
    public void removeMember(Pokemon pokemon){if(!members.isEmpty())members.remove(pokemon);}
    public Pokemon getActiveMember(){return members.get(activeIndex);}
    public Boolean hasAvailableMember(){
        for(Pokemon member:members)if(member.isAlive())return true;
        return false;
    }
    public Boolean switchActive(Integer index){
        if(index>=0&&index<members.size()){
            activeIndex=index;
            return true;
        }
        return false;
    }
    public boolean hasIndex(Integer index){return index>=0&&index<members.size();}
}
