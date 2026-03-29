# 🎮 Cyber Tetris

A cyberpunk-style Tetris game with neon glow effects, built with Java 21 and Swing.

![Cyberpunk](https://img.shields.io/badge/Style-Cyberpunk-00ffff?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apache-maven)

## ✨ Features

- 🎨 **Cyberpunk Visual Style** — Dark backgrounds with neon glow effects
- 🌈 **7 Classic Tetromino Shapes** — Each with unique neon colors
- 💯 **Score & Level System** — Tracks score, level, and lines cleared
- ⚡ **Progressive Difficulty** — Speed increases as you level up
- 🔮 **Particle Effects** — Visual feedback on line clears
- ⏸️ **Pause/Resume** — Press P to pause the game
- 🔄 **Restart** — Press R after game over to play again

## 🎮 Controls

| Key | Action |
|-----|--------|
| ← → | Move left/right |
| ↑ | Rotate piece |
| ↓ | Soft drop |
| Space | Hard drop |
| P | Pause/Resume |
| R | Restart (after game over) |

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/C00Df/cyber-tetris.git
cd cyber-tetris

# Build the project
mvn clean package

# Run the game
mvn exec:java
```

### Alternative Run Methods

```bash
# Run with java command
java -jar target/cyber-tetris-1.0.0.jar

# Or compile and run manually
javac -d target/classes src/main/java/com/cybertetris/*.java
java -cp target/classes com.cybertetris.Game
```

## 📊 Scoring

| Lines Cleared | Points |
|---------------|--------|
| 1 Line | 100 × Level |
| 2 Lines | 300 × Level |
| 3 Lines | 500 × Level |
| 4 Lines (Tetris!) | 800 × Level |
| Soft Drop | 1 point per cell |

## 🎨 Color Palette

| Piece | Color |
|-------|-------|
| I-Shape | Cyan `#00FFFF` |
| O-Shape | Magenta `#FF00FF` |
| T-Shape | Purple `#9400D3` |
| S-Shape | Neon Green `#00FF7F` |
| Z-Shape | Orange Red `#FF4500` |
| J-Shape | Deep Sky Blue `#00BFFF` |
| L-Shape | Gold `#FFD700` |

## 📁 Project Structure

```
cyber-tetris/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── cybertetris/
                    ├── Game.java          # Main entry point
                    ├── GamePanel.java      # Game UI with cyberpunk visuals
                    ├── Board.java          # Game board logic
                    ├── Tetromino.java      # Tetromino definitions
                    └── ScoreManager.java   # Score and level tracking
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the MIT License.

---

Made with 💜 and Java
