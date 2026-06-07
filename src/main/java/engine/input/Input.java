package engine.input;

import engine.math.vectors.MutableVec2d;
import engine.math.vectors.Vec2d;

import java.awt.event.*;
import java.security.InvalidParameterException;
import java.util.Arrays;

public class Input
{
    private static final boolean[] keys = new boolean[525];
    private static final boolean[] keysDown = new boolean[525];
    private static final boolean[] keysUp = new boolean[525];

    private static final boolean[] mouseButtons = new boolean[3];
    private static final boolean[] mouseButtonsDown = new boolean[3];
    private static final boolean[] mouseButtonsUp = new boolean[3];

    private static String inputString = "";
    private static final MutableVec2d mousePosition = new MutableVec2d();

    private static class InputAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int code = e.getKeyCode();
            if (code >= 0 && code < keys.length)
            {
                if (!keys[code])
                    keysDown[code] = true;
                keys[code] = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            int code = e.getKeyCode();
            if (code >= 0 && code < keys.length)
            {
                keys[code] = false;
                keysUp[code] = true;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();

            if (c == KeyEvent.VK_BACK_SPACE)
            {
                if (!inputString.isEmpty())
                    inputString = inputString.substring(0, inputString.length() - 1);
            }
            else if (c >= 32 && c <= 126) {
                inputString += c;
            }
        }
    }

    private static class MouseInput extends MouseAdapter
    {
        @Override
        public void mouseMoved(MouseEvent e) {
            mousePosition.set(e.getX(), e.getY());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mousePosition.set(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            int button = e.getButton() - 1;
            if (button >= 0 && button < 3)
            {
                if (!mouseButtons[button])
                    mouseButtonsDown[button] = true;
                mouseButtons[button] = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            int button = e.getButton() - 1;
            if (button >= 0 && button < 3)
            {
                mouseButtons[button] = false;
                mouseButtonsUp[button] = true;
            }
        }
    }

    public static final KeyListener keyListener = new InputAdapter();
    public static final MouseAdapter mouseListener = new MouseInput();

    public static void endFrame()
    {
        Arrays.fill(keysDown, false);
        Arrays.fill(keysUp, false);
        Arrays.fill(mouseButtonsDown, false);
        Arrays.fill(mouseButtonsUp, false);
    }

    public static Vec2d getMousePos() { return mousePosition; }

    public static boolean getKey(int keyCode) { return keys[keyCode]; }
    public static boolean getKeyDown(int keyCode) { return keysDown[keyCode]; }
    public static boolean getKeyUp(int keyCode) { return keysUp[keyCode]; }

    public static boolean getMouseButton(int button) { return mouseButtons[button]; }
    public static boolean getMouseButtonDown(int button) { return mouseButtonsDown[button]; }
    public static boolean getMouseButtonUp(int button) { return mouseButtonsUp[button]; }

    public static String getInputString() { return inputString; }
    public static void clearInputString() { inputString = ""; }

    public static int getAxisRaw(String axisType)
    {
        if (axisType.equals("Horizontal"))
        {
            if (getKey(KeyEvent.VK_D) || getKey(KeyEvent.VK_RIGHT))
                return 1;
            else if (getKey(KeyEvent.VK_A) || getKey(KeyEvent.VK_LEFT))
                return -1;
            return 0;
        }
        if (axisType.equals("Vertical"))
        {
            if (getKey(KeyEvent.VK_W) || getKey(KeyEvent.VK_UP))
                return -1;
            else if (getKey(KeyEvent.VK_S) || getKey(KeyEvent.VK_DOWN))
                return 1;
            return 0;
        }
        throw new InvalidParameterException("axis not valid");
    }
}