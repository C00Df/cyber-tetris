package com.cybertetris;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Game board logic
 */
public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    
    private Color[][] grid;
    private int currentX, currentY;
    private int[][] currentShape;
    private Color currentColor;
    private Tetromino nextPiece;
    
    private final ScoreManager scoreManager;
    private boolean gameOver = false;
    
    public Board() {
        grid = new Color[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            grid[i] = new Color[WIDTH];
        }
        scoreManager = new ScoreManager();
        nextPiece = Tetromino.randomPiece();
        spawnPiece();
    }
    
    public void spawnPiece() {
        Tetromino piece = nextPiece;
        nextPiece = Tetromino.randomPiece();
        
        currentShape = deepCopy(piece.getShape());
        currentColor = piece.getColor();
        currentX = WIDTH / 2 - currentShape[0].length / 2;
        currentY = 0;
        
        if (!isValidPosition(currentX, currentY, currentShape)) {
            gameOver = true;
        }
    }
    
    public boolean moveLeft() {
        if (isValidPosition(currentX - 1, currentY, currentShape)) {
            currentX--;
            return true;
        }
        return false;
    }
    
    public boolean moveRight() {
        if (isValidPosition(currentX + 1, currentY, currentShape)) {
            currentX++;
            return true;
        }
        return false;
    }
    
    public boolean moveDown() {
        if (isValidPosition(currentX, currentY + 1, currentShape)) {
            currentY++;
            return true;
        }
        lockPiece();
        return false;
    }
    
    public void hardDrop() {
        while (moveDown()) {
            scoreManager.addSoftDrop();
        }
    }
    
    public boolean rotate() {
        int[][] rotated = rotateMatrix(currentShape);
        if (isValidPosition(currentX, currentY, rotated)) {
            currentShape = rotated;
            return true;
        }
        // Wall kick attempts
        if (isValidPosition(currentX - 1, currentY, rotated)) {
            currentX--;
            currentShape = rotated;
            return true;
        }
        if (isValidPosition(currentX + 1, currentY, rotated)) {
            currentX++;
            currentShape = rotated;
            return true;
        }
        return false;
    }
    
    private int[][] rotateMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = matrix[i][j];
            }
        }
        return rotated;
    }
    
    private boolean isValidPosition(int x, int y, int[][] shape) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= WIDTH || newY >= HEIGHT) {
                        return false;
                    }
                    if (newY >= 0 && grid[newY][newX] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void lockPiece() {
        for (int i = 0; i < currentShape.length; i++) {
            for (int j = 0; j < currentShape[i].length; j++) {
                if (currentShape[i][j] == 1) {
                    int y = currentY + i;
                    int x = currentX + j;
                    if (y >= 0 && y < HEIGHT && x >= 0 && x < WIDTH) {
                        grid[y][x] = currentColor;
                    }
                }
            }
        }
        clearLines();
        spawnPiece();
    }
    
    private void clearLines() {
        int linesCleared = 0;
        List<Integer> clearedRows = new ArrayList<>();
        
        for (int i = HEIGHT - 1; i >= 0; i--) {
            boolean full = true;
            for (int j = 0; j < WIDTH; j++) {
                if (grid[i][j] == null) {
                    full = false;
                    break;
                }
            }
            if (full) {
                clearedRows.add(i);
                linesCleared++;
            }
        }
        
        // Remove cleared lines
        for (int row : clearedRows) {
            for (int i = row; i > 0; i--) {
                grid[i] = grid[i - 1].clone();
            }
            grid[0] = new Color[WIDTH];
        }
        
        if (linesCleared > 0) {
            scoreManager.addLinesCleared(linesCleared);
        }
    }
    
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
    
    // Getters
    public Color[][] getGrid() { return grid; }
    public int getCurrentX() { return currentX; }
    public int getCurrentY() { return currentY; }
    public int[][] getCurrentShape() { return currentShape; }
    public Color getCurrentColor() { return currentColor; }
    public Tetromino getNextPiece() { return nextPiece; }
    public ScoreManager getScoreManager() { return scoreManager; }
    public boolean isGameOver() { return gameOver; }
}
