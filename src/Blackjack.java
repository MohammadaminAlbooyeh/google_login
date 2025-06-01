import java.util.Scanner;


/**
 * A simple text-based Blackjack game.
 *
 * Rules:
 * - The player and dealer each get two cards. One of the dealer's cards is hidden.
 * - The player can choose to "hit" (draw a card) or "stay" (end their turn).
 * - If the player's hand exceeds 21, the player busts and loses.
 * - After the player stays, the dealer reveals the hidden card and draws cards until reaching at least 17.
 * - If the dealer's hand exceeds 21, the dealer busts and the player wins.
 * - If neither busts, the hand with the higher value wins. A tie is possible.
 *
 * Card Values:
 * - Number cards: face value (2-10)
 * - Face cards (J, Q, K): 10
 * - Ace: 1 or 11, whichever is more favorable for the hand
 *
 * To play: Run the program and follow the prompts in the terminal.
 */


public class Blackjack {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Blackjack!");
        System.out.println("Press Enter to start...");
        scanner.nextLine();

        // Player's hand
        int[] playerHand = {drawRandomCard(), drawRandomCard()};
        System.out.println("Your first card is:\n" + cardString(playerHand[0]));
        System.out.println("Your second card is:\n" + cardString(playerHand[1]));
        System.out.println("Your total is: " + calculateHandValue(playerHand));

        // Dealer's hand
        int[] dealerHand = {drawRandomCard(), drawRandomCard()};
        System.out.println("The dealer's first card is:\n" + cardString(dealerHand[0]));
        System.out.println("The dealer's second card is:\n" + faceDown());
        System.out.println("The dealer's second card is hidden.");

        // Player's turn
        while (true) {
            String option = hitOrStay();
            if (option.equals("H")) {
                int newCard = drawRandomCard();
                System.out.println("You drew:\n" + cardString(newCard));
                playerHand = addCardToHand(playerHand, newCard);
                int total = calculateHandValue(playerHand);
                System.out.println("Your new total is: " + total);

                if (total > 21) {
                    System.out.println("You busted! Your total is over 21. You lose.");
                    return;
                }
            } else {
                break;
            }
        }

        int playerTotal = calculateHandValue(playerHand);
        System.out.println("You chose to stay. Your final total is: " + playerTotal);

        // Dealer's turn
        System.out.println("The dealer's second card is:\n" + cardString(dealerHand[1]));
        int dealerTotal = calculateHandValue(dealerHand);
        System.out.println("Dealer's total is: " + dealerTotal);

        while (dealerTotal < 17) {
            int newCard = drawRandomCard();
            System.out.println("Dealer draws:\n" + cardString(newCard));
            dealerHand = addCardToHand(dealerHand, newCard);
            dealerTotal = calculateHandValue(dealerHand);
            System.out.println("Dealer's new total is: " + dealerTotal);
        }

        // Determine winner
        if (dealerTotal > 21) {
            System.out.println("Dealer busted! You win!");
        } else if (dealerTotal > playerTotal) {
            System.out.println("Dealer wins!");
        } else if (dealerTotal < playerTotal) {
            System.out.println("You win!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    public static int drawRandomCard() {
        // Generate a random card value between 1 and 13
        return (int) (Math.random() * 13) + 1;
    }

    public static int[] addCardToHand(int[] hand, int card) {
        int[] newHand = new int[hand.length + 1];
        System.arraycopy(hand, 0, newHand, 0, hand.length);
        newHand[hand.length] = card;
        return newHand;
    }

    public static int calculateHandValue(int[] hand) {
        int value = 0;
        int aces = 0;
        for (int card : hand) {
            if (card == 1) { // Ace
                aces++;
                value += 11;
            } else if (card > 10) { // Face cards
                value += 10;
            } else {
                value += card;
            }
        }
        // Adjust for Aces if the total exceeds 21
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
        return value;
    }

    public static String hitOrStay() {
        System.out.println("Do you want to hit (H) or stay (S)?");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        while (!response.equalsIgnoreCase("H") && !response.equalsIgnoreCase("S")) {
            System.out.println("Invalid input. Please enter 'H' to hit or 'S' to stay.");
            response = scanner.nextLine();
        }
        return response.toUpperCase();
    }

    public static String cardString(int cardNumber) {
        switch (cardNumber) {
            case 1:
                return  "   _____\n" +
                        "  |A _  |\n" +
                        "  | ( ) |\n" +
                        "  |(_'_)|\n" +
                        "  |  |  |\n" +
                        "  |____V|\n";
            case 2:
                return  "   _____\n" +
                        "  |2    |\n" +
                        "  |  o  |\n" +
                        "  |     |\n" +
                        "  |  o  |\n" +
                        "  |____Z|\n";
            case 3:
                return  "   _____\n" +
                        "  |3    |\n" +
                        "  | o o |\n" +
                        "  |     |\n" +
                        "  |  o  |\n" +
                        "  |____E|\n";
            case 4:
                return  "   _____\n" +
                        "  |4    |\n" +
                        "  | o o |\n" +
                        "  |     |\n" +
                        "  | o o |\n" +
                        "  |____h|\n";
            case 5:
                return  "   _____\n" +
                        "  |5    |\n" +
                        "  | o o |\n" +
                        "  |  o  |\n" +
                        "  | o o |\n" +
                        "  |____S|\n";
            case 6:
                return  "   _____\n" +
                        "  |6    |\n" +
                        "  | o o |\n" +
                        "  | o o |\n" +
                        "  | o o |\n" +
                        "  |____6|\n";
            case 7:
                return  "   _____\n" +
                        "  |7    |\n" +
                        "  | o o |\n" +
                        "  |o o o|\n" +
                        "  | o o |\n" +
                        "  |____7|\n";
            case 8:
                return  "   _____\n" +
                        "  |8    |\n" +
                        "  |o o o|\n" +
                        "  | o o |\n" +
                        "  |o o o|\n" +
                        "  |____8|\n";
            case 9:
                return  "   _____\n" +
                        "  |9    |\n" +
                        "  |o o o|\n" +
                        "  |o o o|\n" +
                        "  |o o o|\n" +
                        "  |____9|\n";
            case 10:
                return  "   _____\n" +
                        "  |10   |\n" +
                        "  |o o o|\n" +
                        "  |o o o|\n" +
                        "  |o o o|\n" +
                        "  |___10|\n";
            case 11:
                return  "   _____\n" +
                        "  |J  ww|\n" +
                        "  | o {)|\n" +
                        "  |o o% |\n" +
                        "  | | % |\n" +
                        "  |__%%[|\n";
            case 12:
                return  "   _____\n" +
                        "  |Q  ww|\n" +
                        "  | o {(|\n" +
                        "  |o o%%|\n" +
                        "  | |%%%|\n" +
                        "  |_%%%O|\n";
            case 13:
                return  "   _____\n" +
                        "  |K  WW|\n" +
                        "  | o {)|\n" +
                        "  |o o%%|\n" +
                        "  | |%%%|\n" +
                        "  |_%%%>|\n";
            default:
                return "Invalid Card";
        }
    }

    public static String faceDown() {
        return "   _____\n" +
               "  |     |\n" +
               "  |  J  |\n" +
               "  | JJJ |\n" +
               "  |  J  |\n" +
               "  |_____|\n";
    }
}
