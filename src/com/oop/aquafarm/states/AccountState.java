package com.oop.aquafarm.states;

import com.oop.aquafarm.GamePanel;
import com.oop.aquafarm.graphics.CFont;
import com.oop.aquafarm.util.KeyHandler;
import com.oop.aquafarm.util.MouseHandler;

import java.awt.*;

public class AccountState extends GameState {
    public AccountState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update(double time) {
        System.out.println("login/signup");
    }

    @Override
    public void input(MouseHandler mouseIn, KeyHandler keyh) {

    }

    @Override
    public void render(Graphics2D g) {

    }
}
