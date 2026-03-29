package com.cybertetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Game panel with cyberpunk neon visuals
 */
public class GamePanel extends JPanel implements ActionListener {
    private static final int CELL_SIZE = 30;
    private static final int PREVIEW_CELL = 18;
    
    private Board board;
    private Timer timer;
    private boolean paused = false;
    private boolean gameOver = false;
    
    // Particle effects for line clears
    private List<Particle> particles = new ArrayList<>();
    
    // Cyberpunk colors
    private static final Color BACKGROUND = new Color(10, 10, 20);
    private static final Color GRID_COLOR = new Color(0, 100, 100, 80);
    private static final Color GLOW_CYAN = new Color(0, 255, 255);
    private static final Color GLOW_MAGENTA = new Color(255, 0, 255);
    private static final Color TEXT_COLOR = new Color(0, 255, 255);
    
    public GamePanel() {
        setPreferredSize(new Dimension(
            Board.WIDTH * CELL_SIZE + 160,
            Board.HEIGHT * CELL_SIZE + 40
        ));
        setBackground(BACKGROUND);
        setFocusable(true);
        
        board = new Board();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        restartGame();
                    }
                    return;
                }
                
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    paused = !paused;
                    if (paused) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                    return;
                }
                
                if (paused) return;
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> board.moveLeft();
                    case KeyEvent.VK_RIGHT -> board.moveRight();
                    case KeyEvent.VK_DOWN -> board.moveDown();
                    case KeyEvent.VK_UP -> board.rotate();
                    case KeyEvent.VK_SPACE -> board.hardDrop();
                }
                repaint();
            }
        });
        
        timer = new Timer(board.getScoreManager().getDropDelay(), this);
        timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused && !gameOver) {
            if (!board.moveDown()) {
                if (board.isGameOver()) {
                    gameOver = true;
                    timer.stop();
                }
            }
            
            // Update timer speed based on level
            timer.setDelay(board.getScoreManager().getDropDelay());
            
            // Spawn particles on line clear
            spawnParticles();
            
            repaint();
        }
    }
    
    private void spawnParticles() {
        // Add particles effect when lines are cleared
        // (simplified version)
    }
    
    private void restartGame() {
        board = new Board();
        gameOver = false;
        paused = false;
        timer.start();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background with subtle gradient
        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(10, 10, 30),
            0, getHeight(), new Color(5, 5, 15)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw main game area
        drawGameArea(g2d);
        
        // Draw side panel
        drawSidePanel(g2d);
        
        // Draw paused overlay
        if (paused) {
            drawPausedOverlay(g2d);
        }
        
        // Draw game over overlay
        if (gameOver) {
            drawGameOverOverlay(g2d);
        }
        
        // Draw and update particles
        updateAndDrawParticles(g2d);
    }
    
    private void drawGameArea(Graphics2D g2d) {
        int offsetX = 20;
        int offsetY = 20;
        
        // Draw glowing border
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(GLOW_CYAN);
        g2d.drawRect(offsetX - 2, offsetY - 2, 
            Board.WIDTH * CELL_SIZE + 4, Board.HEIGHT * CELL_SIZE + 4);
        
        // Draw grid background
        g2d.setColor(new Color(0, 20, 30));
        g2d.fillRect(offsetX, offsetY, Board.WIDTH * CELL_SIZE, Board.HEIGHT * CELL_SIZE);
        
        // Draw grid lines
        g2d.setColor(GRID_COLOR);
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i <= Board.WIDTH; i++) {
            g2d.drawLine(offsetX + i * CELL_SIZE, offsetY, 
                        offsetX + i * CELL_SIZE, offsetY + Board.HEIGHT * CELL_SIZE);
        }
        for (int i = 0; i <= Board.HEIGHT; i++) {
            g2d.drawLine(offsetX, offsetY + i * CELL_SIZE,
                        offsetX + Board.WIDTH * CELL_SIZE, offsetY + i * CELL_SIZE);
        }
        
        // Draw locked pieces with glow
        Color[][] grid = board.getGrid();
        for (int y = 0; y < Board.HEIGHT; y++) {
            for (int x = 0; x < Board.WIDTH; x++) {
                if (grid[y][x] != null) {
                    drawGlowBlock(g2d, offsetX + x * CELL_SIZE, offsetY + y * CELL_SIZE, 
                                 grid[y][x], CELL_SIZE - 1);
                }
            }
        }
        
        // Draw current piece
        if (!gameOver && !paused) {
            int[][] shape = board.getCurrentShape();
            Color color = board.getCurrentColor();
            int px = board.getCurrentX();
            int py = board.getCurrentY();
            
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        int blockX = offsetX + (px + j) * CELL_SIZE;
                        int blockY = offsetY + (py + i) * CELL_SIZE;
                        drawGlowBlock(g2d, blockX, blockY, color, CELL_SIZE - 1);
                    }
                }
            }
        }
    }
    
    private void drawGlowBlock(Graphics2D g2d, int x, int y, Color color, int size) {
        // Outer glow
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
        g2d.fillRect(x - 2, y - 2, size + 4, size + 4);
        
        // Main block with gradient
        GradientPaint blockGradient = new GradientPaint(
            x, y, color,
            x, y + size, color.darker()
        );
        g2d.setPaint(blockGradient);
        g2d.fillRect(x, y, size, size);
        
        // Inner highlight
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillRect(x + 2, y + 2, size / 3, size / 3);
    }
    
    private void drawSidePanel(Graphics2D g2d) {
        int panelX = Board.WIDTH * CELL_SIZE + 50;
        
        // Next piece label with glow
        g2d.setColor(GLOW_CYAN);
        g2d.setFont(new Font("Consolas", Font.BOLD, 14));
        String nextLabel = "NEXT";
        g2d.drawString(nextLabel, panelX, 35);
        
        // Next piece preview box
        g2d.setColor(new Color(0, 40, 50));
        g2d.fillRect(panelX, 45, 90, 80);
        g2d.setColor(GLOW_CYAN);
        g2d.drawRect(panelX, 45, 90, 80);
        
        // Draw next piece
        Tetromino next = board.getNextPiece();
        int[][] shape = next.getShape();
        int startX = panelX + 45 / 2 - (shape[0].length * PREVIEW_CELL) / 2;
        int startY = 55;
        
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    drawGlowBlock(g2d, startX + j * PREVIEW_CELL, 
                                 startY + i * PREVIEW_CELL, 
                                 next.getColor(), PREVIEW_CELL - 1);
                }
            }
        }
        
        // Score section
        g2d.setColor(GLOW_MAGENTA);
        g2d.drawString("SCORE", panelX, 155);
        
        // Score value with LED style
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Consolas", Font.BOLD, 18));
        String scoreStr = String.format("%06d", board.getScoreManager().getScore());
        g2d.drawString(scoreStr, panelX, 175);
        
        // Level
        g2d.setColor(GLOW_MAGENTA);
        g2d.setFont(new Font("Consolas", Font.BOLD, 14));
        g2d.drawString("LEVEL", panelX, 205);
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Consolas", Font.BOLD, 18));
        g2d.drawString(String.valueOf(board.getScoreManager().getLevel()), panelX, 225);
        
        // Lines
        g2d.setColor(GLOW_MAGENTA);
        g2d.setFont(new Font("Consolas", Font.BOLD, 14));
        g2d.drawString("LINES", panelX, 255);
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Consolas", Font.BOLD, 18));
        g2d.drawString(String.valueOf(board.getScoreManager().getLinesCleared()), panelX, 275);
        
        // Controls help
        g2d.setColor(new Color(100, 100, 120));
        g2d.setFont(new Font("Consolas", Font.PLAIN, 10));
        g2d.drawString("CONTROLS:", panelX, 320);
        g2d.drawString("← → : Move", panelX, 335);
        g2d.drawString("↑ : Rotate", panelX, 350);
        g2d.drawString("↓ : Soft Drop", panelX, 365);
        g2d.drawString("SPACE: Hard Drop", panelX, 380);
        g2d.drawString("P : Pause", panelX, 395);
    }
    
    private void drawPausedOverlay(Graphics2D g2d) {
        // Dark overlay
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // PAUSED text with glow
        g2d.setColor(GLOW_CYAN);
        g2d.setFont(new Font("Consolas", Font.BOLD, 48));
        String pausedText = "PAUSED";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(pausedText)) / 2;
        g2d.drawString(pausedText, textX, getHeight() / 2);
    }
    
    private void drawGameOverOverlay(Graphics2D g2d) {
        // Dark overlay
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // GAME OVER text with red glow
        g2d.setColor(new Color(255, 50, 50));
        g2d.setFont(new Font("Consolas", Font.BOLD, 36));
        String gameOverText = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(gameOverText)) / 2;
        g2d.drawString(gameOverText, textX, getHeight() / 2 - 30);
        
        // Final score
        g2d.setColor(GLOW_CYAN);
        g2d.setFont(new Font("Consolas", Font.BOLD, 24));
        String finalScore = "SCORE: " + board.getScoreManager().getScore();
        textX = (getWidth() - fm.stringWidth(finalScore)) / 2;
        g2d.drawString(finalScore, textX, getHeight() / 2 + 20);
        
        // Restart hint
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Consolas", Font.PLAIN, 16));
        String restartHint = "Press R to Restart";
        fm = g2d.getFontMetrics();
        textX = (getWidth() - fm.stringWidth(restartHint)) / 2;
        g2d.drawString(restartHint, textX, getHeight() / 2 + 60);
    }
    
    private void updateAndDrawParticles(Graphics2D g2d) {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            p.draw(g2d);
            if (p.isDead()) {
                particles.remove(i);
            }
        }
    }
    
    public void startGame() {
        timer.start();
    }
    
    // Inner Particle class for effects
    private static class Particle {
        private double x, y;
        private double vx, vy;
        private Color color;
        private int life;
        
        public Particle(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.vx = (Math.random() - 0.5) * 8;
            this.vy = (Math.random() - 0.5) * 8;
            this.life = 30;
        }
        
        public void update() {
            x += vx;
            y += vy;
            life--;
        }
        
        public void draw(Graphics2D g2d) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), life * 5));
            g2d.fillRect((int)x, (int)y, 3, 3);
        }
        
        public boolean isDead() {
            return life <= 0;
        }
    }
}
