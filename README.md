# ✦ Tic-Tac-Toe in Java (AWT)

A classic two-player Tic-Tac-Toe game built with pure Java AWT (Abstract Window Toolkit) — no external libraries required.

---

## 🎮 Features

- Two-player local gameplay (X and O)
- Automatic win detection — rows, columns, and diagonals
- Draw detection when the board is full
- Winning cells highlighted after each match
- Live score tracker across multiple rounds
- "New Game" button to reset the board without restarting
- Custom-painted buttons with hover effects
- Dark-themed UI with color-coded symbols

---

## 📁 Project Structure

```
TicTacToe/
├── TicTacToe.java      # Main source file (all classes included)
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command-line terminal (or any Java IDE)

### Compile

```bash
javac TicTacToe.java
```

### Run

```bash
java TicTacToe
```

---

## 🕹️ How to Play

1. Run the program — a game window will appear.
2. Players take turns clicking empty cells.
3. **Player X** always goes first.
4. Get three of your symbol in a row (horizontal, vertical, or diagonal) to win.
5. If all 9 cells are filled with no winner, it's a **Draw**.
6. Click **↺ New Game** to play again — scores are preserved.

---

## 🛠️ Built With

- **Java** — core language
- **AWT (Abstract Window Toolkit)** — GUI framework
- **Canvas** — custom button rendering via `Graphics2D`

---

## 📌 Classes Overview

| Class | Role |
|---|---|
| `TicTacToe` | Main window (Frame), game logic, score tracking |
| `RoundButton` | Custom canvas-based button with hover and symbol drawing |

---

## 📄 License

This project is open source under the [MIT License](LICENSE).

---

## 🙋 Author

Made by **[Aditya Patel]**
GitHub:[https://github.com/AdityaPatelgit]
