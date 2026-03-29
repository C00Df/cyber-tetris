package com.cybertetris;

import javax.swing.*;
import java.awt.*;

/**
 * Cyber Tetris - A cyberpunk-style Tetris game with neon glow effects
 * 
 * Controls:
 * - Arrow Left/Right: Move piece
 * - Arrow Up: Rotate
 * - Arrow Down: Soft drop
 * - Space: Hard drop
 * - P: Pause/Resume
 * - R: Restart (when game over)
 */
public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cyber Tetris");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            GamePanel gamePanel = new GamePanel();
            frame.add(gamePanel);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            gamePanel.startGame();
        });
    }
}
