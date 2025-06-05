# Blackjack Game (Java)

A simple text-based Blackjack game implemented in Java.

---

## Description

This is a console-based Blackjack game where the player competes against the dealer (computer). The game follows basic Blackjack rules:

- Both player and dealer start with two cards.
- One of the dealer's cards is hidden until the player's turn ends.
- The player can choose to "hit" (draw a card) or "stay" (end their turn).
- If the player's hand exceeds 21, the player busts and loses immediately.
- After the player stays, the dealer reveals the hidden card and draws cards until their hand value reaches at least 17.
- If the dealer busts (goes over 21), the player wins.
- Otherwise, the hand with the higher value wins. A tie is possible.

Card values:

- Number cards (2-10): Face value.
- Face cards (Jack, Queen, King): 10 points.
- Ace: 1 or 11 points, whichever benefits the hand more.

---

## How to Play

1. Run the program from the terminal or your Java IDE.
2. Press Enter to start the game.
3. You will see your initial two cards and the dealer's first card (the second is hidden).
4. Choose to "Hit" (H) to draw another card or "Stay" (S) to end your turn.
5. If you exceed 21 points, you bust and lose.
6. If you stay, the dealer will reveal their hidden card and draw additional cards if needed.
7. The winner is determined based on who has the highest valid hand.

---

## Requirements

- Java Development Kit (JDK) 8 or higher
- Any Java IDE or command line environment to compile and run

---

## How to Run

### Using Command Line

1. Compile the Java file:
   ```bash
   javac Blackjack.java
