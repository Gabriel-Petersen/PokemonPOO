package game.itemsystem;

@FunctionalInterface
public interface ItemSelectionCallback {
    boolean onItemChosen(Item item);
}