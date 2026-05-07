package game.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import engine.ui.core.UiTransform.Anchor;
import engine.ui.elements.UiButton;
import engine.ui.elements.UiImage;
import engine.ui.elements.UiText;

public class PauseMenu extends UiImage 
{
	private final UiButton info = new UiButton("Info", () -> onInfoClick());
	
	public PauseMenu(int sizeX, int sizeY, Color color) 
	{
		super(sizeX, sizeY, color);
	
		getUiTransform().setAnchor(Anchor.CENTER_RIGHT);
		addChild(info);
	}
	
	private void onInfoClick()
	{
		
	}
}
