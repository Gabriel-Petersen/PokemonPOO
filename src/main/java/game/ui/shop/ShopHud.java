package game.ui.shop;

import engine.core.GamePanel;
import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;
import game.battle.ActionResult;
import game.commerce.Seller;
import game.commerce.ShopService;
import game.itemsystem.Inventory;
import game.itemsystem.Item;
import game.player.Player;
import java.awt.Color;
import java.awt.Font;
import java.util.Map;

public class ShopHud extends UiImage {

    private static final ShopHud INSTANCE = new ShopHud(940, 560, new Color(40, 44, 52));
    private static boolean isAdded = false;

    private final ShopService shopService;
    private final UiImage sellerPanel;
    private final UiImage playerPanel;
    private final UiImage sellerContainer;
    private final UiImage playerContainer;
    private final UiText descriptionTxt;
    private final UiText moneyTxt;
    private final UiText infoTxt;
    private final UiButton buyBtn;
    private final UiButton sellBtn;
    private final UiButton closeBtn;

    private Player player;
    private Seller seller;
    private Item selectedItem;
    private boolean selectedFromSeller;

    public static ShopHud getInstance() {
        if (!isAdded) {
            GamePanel.getInstance().addElement(INSTANCE);
            isAdded = true;
        }
        return INSTANCE;
    }

    private ShopHud(int sizeX, int sizeY, Color color) {
        super(sizeX, sizeY, color);
        shopService = new ShopService();

        getUiTransform().setAnchor(Anchor.CENTER);
        getUiTransform().setPosition(0, 0);
        setVisible(false);

        UiText title = new UiText("Loja");
        title.setFont("Arial", Font.BOLD, 18);
        title.setColor(Color.WHITE);
        title.getUiTransform().setAnchor(Anchor.CENTER_TOP);
        title.getUiTransform().setPosition(0, 12);
        addChild(title);

        moneyTxt = new UiText("Dinheiro: 0$ ");
        moneyTxt.setFont("Arial", Font.PLAIN, 14);
        moneyTxt.setColor(new Color(173, 218, 107));
        moneyTxt.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        moneyTxt.getUiTransform().setPosition(-20, 12);
        addChild(moneyTxt);

        sellerPanel = new UiImage((sizeX - 60) / 2, sizeY - 180, new Color(27, 32, 42));
        sellerPanel.getUiTransform().setAnchor(Anchor.TOP_LEFT);
        sellerPanel.getUiTransform().setPosition(20, 40);
        addChild(sellerPanel);

        playerPanel = new UiImage((sizeX - 60) / 2, sizeY - 180, new Color(27, 32, 42));
        playerPanel.getUiTransform().setAnchor(Anchor.TOP_RIGHT);
        playerPanel.getUiTransform().setPosition(-20, 40);
        addChild(playerPanel);

        UiText sellerTitle = new UiText("Itens à venda");
        sellerTitle.setFont("Arial", Font.BOLD, 14);
        sellerTitle.setColor(Color.WHITE);
        sellerTitle.getUiTransform().setAnchor(Anchor.CENTER_TOP);
        sellerTitle.getUiTransform().setPosition(0, 10);
        sellerPanel.addChild(sellerTitle);

        UiText playerTitle = new UiText("Seu inventário");
        playerTitle.setFont("Arial", Font.BOLD, 14);
        playerTitle.setColor(Color.WHITE);
        playerTitle.getUiTransform().setAnchor(Anchor.CENTER_TOP);
        playerTitle.getUiTransform().setPosition(0, 10);
        playerPanel.addChild(playerTitle);

        sellerContainer = new UiImage((int) sellerPanel.getTransform().getScale().x() - 30, (int) sellerPanel.getTransform().getScale().y() - 65, new Color(18, 22, 30));
        sellerContainer.getUiTransform().setAnchor(Anchor.CENTER_TOP);
        sellerContainer.getUiTransform().setPosition(0, 35);
        sellerPanel.addChild(sellerContainer);

        playerContainer = new UiImage((int) playerPanel.getTransform().getScale().x() - 30, (int) playerPanel.getTransform().getScale().y() - 65, new Color(18, 22, 30));
        playerContainer.getUiTransform().setAnchor(Anchor.CENTER_TOP);
        playerContainer.getUiTransform().setPosition(0, 35);
        playerPanel.addChild(playerContainer);

        UiImage bottomBox = new UiImage(sizeX - 40, 100, new Color(23, 28, 36));
        bottomBox.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        bottomBox.getUiTransform().setPosition(0, -20);
        addChild(bottomBox);

        descriptionTxt = new UiText("Selecione um item para visualizar preço e descrição.");
        descriptionTxt.setFont("Arial", Font.PLAIN, 14);
        descriptionTxt.setColor(Color.LIGHT_GRAY);
        descriptionTxt.getUiTransform().setAnchor(Anchor.CENTER_LEFT);
        descriptionTxt.getUiTransform().setPosition(15, 0);
        bottomBox.addChild(descriptionTxt);

        infoTxt = new UiText("Use o painel esquerdo para comprar ou o direito para vender.");
        infoTxt.setFont("Arial", Font.PLAIN, 14);
        infoTxt.setColor(Color.WHITE);
        infoTxt.getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
        infoTxt.getUiTransform().setPosition(-15, 0);
        bottomBox.addChild(infoTxt);

        buyBtn = new UiButton("COMPRAR", this::onBuy);
        sellBtn = new UiButton("VENDER", this::onSell);
        closeBtn = new UiButton("FECHAR", this::closeShop);

        configureButton(buyBtn, new Color(60, 110, 180), Color.WHITE);
        configureButton(sellBtn, new Color(163, 94, 60), Color.WHITE);
        configureButton(closeBtn, new Color(65, 65, 65), Color.WHITE);

        int btnW = 120;
        int btnH = 36;

        buyBtn.getTransform().setScale(btnW, btnH);
        sellBtn.getTransform().setScale(btnW, btnH);
        closeBtn.getTransform().setScale(btnW, btnH);

        buyBtn.getUiTransform().setAnchor(Anchor.BOTTOM_LEFT);
        buyBtn.getUiTransform().setPosition(30, -20);
        sellBtn.getUiTransform().setAnchor(Anchor.CENTER_BOTTOM);
        sellBtn.getUiTransform().setPosition(0, -20);
        closeBtn.getUiTransform().setAnchor(Anchor.BOTTOM_RIGHT);
        closeBtn.getUiTransform().setPosition(-30, -20);

        addChild(buyBtn);
        addChild(sellBtn);
        addChild(closeBtn);

        updateButtons();
    }

    public void openShop(Player player, Seller seller) {
        this.player = player;
        this.seller = seller;
        this.selectedItem = null;
        this.selectedFromSeller = false;

        this.setVisible(true);
        this.player.setTalking(true);

        updateMoneyText();
        refreshSellerItems();
        refreshPlayerItems();
        descriptionTxt.setText("Selecione um item para visualizar preço e descrição.");
        infoTxt.setText("Escolha um item à esquerda para comprar ou à direita para vender.");
        updateButtons();
    }

    public void closeShop() {
        setVisible(false);
        if (player != null) {
            player.setTalking(false);
            player = null;
        }
        seller = null;
        selectedItem = null;
        selectedFromSeller = false;
    }

    public void setupShop(Player player, Seller seller) {
        openShop(player, seller);
    }

    private void refreshSellerItems() {
        sellerContainer.getAllChildrenSet().clear();

        Inventory inventory = seller.getInventory();
        int index = 0;
        int startY = 10;
        int rowHeight = 40;

        for (Map.Entry<Item, Integer> entry : inventory.getItems().entrySet()) {
            Item item = entry.getKey();
            int amount = entry.getValue();
            int price = (int) (item.getBasePrice() * 1.10);

            ShopItemButton itemBtn = new ShopItemButton(
                item,
                amount,
                descriptionTxt,
                () -> onSellerItemSelected(item, price)
            );

            itemBtn.getTransform().setScale(sellerContainer.getTransform().getScale().x() - 30, 34);
            itemBtn.getUiTransform().setAnchor(Anchor.CENTER_TOP);
            itemBtn.getUiTransform().setPosition(0, startY + (index * rowHeight));
            itemBtn.setForegroundColor(new Color(40, 46, 58));
            itemBtn.setBackgroundColor(new Color(63, 80, 103));
            sellerContainer.addChild(itemBtn);
            index++;
        }

        if (index == 0) {
            descriptionTxt.setText("A loja não tem itens no momento.");
        }
    }

    private void refreshPlayerItems() {
        playerContainer.getAllChildrenSet().clear();

        Inventory inventory = player.getInventory();
        int index = 0;
        int startY = 10;
        int rowHeight = 40;

        for (Map.Entry<Item, Integer> entry : inventory.getItems().entrySet()) {
            Item item = entry.getKey();
            int amount = entry.getValue();
            int sellPrice = (int) (item.getBasePrice() * 0.90);

            ShopItemButton itemBtn = new ShopItemButton(
                item,
                amount,
                descriptionTxt,
                () -> onPlayerItemSelected(item, sellPrice)
            );

            itemBtn.getTransform().setScale(playerContainer.getTransform().getScale().x() - 30, 34);
            itemBtn.getUiTransform().setAnchor(Anchor.CENTER_TOP);
            itemBtn.getUiTransform().setPosition(0, startY + (index * rowHeight));
            itemBtn.setForegroundColor(new Color(40, 46, 58));
            itemBtn.setBackgroundColor(new Color(63, 80, 103));
            playerContainer.addChild(itemBtn);
            index++;
        }

        if (index == 0 && player != null) {
            descriptionTxt.setText("Seu inventário está vazio. Venda algo quando tiver itens disponíveis.");
        }
    }

    private void onSellerItemSelected(Item item, int price) {
        selectedItem = item;
        selectedFromSeller = true;
        descriptionTxt.setText(item.getDescription() + "\nPreço de compra: " + price + "$ | Estoque: " + seller.getInventory().getStack(item));
        infoTxt.setText("Clique em COMPRAR para adquirir este item.");
        updateButtons();
    }

    private void onPlayerItemSelected(Item item, int sellPrice) {
        selectedItem = item;
        selectedFromSeller = false;
        descriptionTxt.setText(item.getDescription() + "\nPreço de venda: " + sellPrice + "$ | Quantidade: " + player.getInventory().getStack(item));
        infoTxt.setText("Clique em VENDER para vender este item.");
        updateButtons();
    }

    private void onBuy() {
        if (player == null || seller == null || selectedItem == null || !selectedFromSeller) {
            infoTxt.setText("Selecione um item válido da loja para comprar.");
            return;
        }

        ActionResult result = shopService.buyItem(player, seller, selectedItem, 1);
        if (result == ActionResult.SUCCESS) {
            updateMoneyText();
            refreshSellerItems();
            refreshPlayerItems();
            infoTxt.setText("Compra realizada! " + selectedItem.getName() + " adicionado ao seu inventário.");
        } else {
            infoTxt.setText("Compra falhou: saldo insuficiente ou estoque insuficiente.");
        }
    }

    private void onSell() {
        if (player == null || seller == null || selectedItem == null || selectedFromSeller) {
            infoTxt.setText("Selecione um item válido do seu inventário para vender.");
            return;
        }

        ActionResult result = shopService.sellItem(player, seller, selectedItem, 1);
        if (result == ActionResult.SUCCESS) {
            updateMoneyText();
            refreshSellerItems();
            refreshPlayerItems();
            infoTxt.setText("Venda realizada! " + selectedItem.getName() + " vendido com sucesso.");
        } else {
            infoTxt.setText("Venda falhou: não há itens suficientes para vender.");
        }
    }

    private void updateMoneyText() {
        if (player != null) {
            moneyTxt.setText("Dinheiro: " + player.getMetadata().getMoney() + "$ ");
        }
    }

    private void updateButtons() {
        buyBtn.setVisible(selectedItem != null && selectedFromSeller);
        sellBtn.setVisible(selectedItem != null && !selectedFromSeller);
    }

    private void configureButton(UiButton button, Color bg, Color fg) {
        button.setBackgroundColor(bg);
        button.setForegroundColor(fg);

        for (var txt : button.getAllChildrenFromType(UiText.class)) {
            txt.setFont("Arial", Font.BOLD, 12);
        }
    }

    private static final class ShopItemButton extends UiButton {
        private final Item item;
        private final UiText sharedDescriptionTxt;

        public ShopItemButton(Item item, int amount, UiText sharedDescription, Runnable onClick) {
            super(item.getName() + " x" + amount, onClick);
            this.item = item;
            this.sharedDescriptionTxt = sharedDescription;

            if (getTxtChild() != null) {
                getTxtChild().setColor(Color.WHITE);
            }
        }

        @Override
        public void onPointerEnter() {
            super.onPointerEnter();
            if (item.getDescription() != null)
                sharedDescriptionTxt.setText(item.getDescription());
        }

        @Override
        public void onPointerExit() {
            super.onPointerExit();
            sharedDescriptionTxt.setText("Selecione um item para visualizar preço e descrição.");
        }
    }
}
