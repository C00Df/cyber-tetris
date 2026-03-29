package com.cybertetris;

/**
 * Score and level management
 */
public class ScoreManager {
    private int score;
    private int level;
    private int linesCleared;
    
    private static final int[] POINTS = {0, 100, 300, 500, 800};
    private static final int LINES_PER_LEVEL = 10;
    
    public ScoreManager() {
        score = 0;
        level = 1;
        linesCleared = 0;
    }
    
    public void addLinesCleared(int lines) {
        linesCleared += lines;
        score += POINTS[lines] * level;
        
        int newLevel = 1 + linesCleared / LINES_PER_LEVEL;
        if (newLevel > level) {
            level = newLevel;
        }
    }
    
    public void addSoftDrop() {
        score += 1;
    }
    
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLinesCleared() { return linesCleared; }
    
    public int getDropDelay() {
        // Speed increases with level
        return Math.max(100, 1000 - (level - 1) * 80);
    }
}
