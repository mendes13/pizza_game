package com.unisul.domain;

import com.unisul.utils.CircularLinkedList;

public class Game {
    public static void main(String[] args) {
        CircularLinkedList<Player> list = new CircularLinkedList<Player>();
        list.add(new Player(new Pizza(Pizza.Flavor.CALABRESA)));
        list.add(new Player(new Pizza(Pizza.Flavor.MUCARELA)));
        new Game(list, new DiceImpl()).play();
    }

    private CircularLinkedList<Player> players;
    private Dice dice;

    public Game(CircularLinkedList<Player> players, Dice dice) {
        this.players = players;
        this.dice = dice;

        Board board = new Board();

        for (int i = 0; i < players.size(); i++) {
            players.get(i).position = board.positions().newIterator();
        }
    }

    public void play() {
        while (true) {
            Player currentPlayer = players.iterator().next();

            int diceResult = dice.roll();
            System.out.println("Dice result: " + diceResult);

            // moves currentPlayer position n times
            // n = dice result
            for (int i = 0; i < diceResult; i++) {
                currentPlayer.position.next();
            }

            // if currentPlayerPosition is an ingredient:
            // else if currentPlayerPosition is SORTE_OU_AZAR
            // else if currentPlayerPosition is PERDE_TUDO
            Position position = currentPlayer.position.current();
            switch (position.getType()) {
                case INGREDIENT: {
                    Pizza.Ingredient ingredient = position.getIngredient();

                    // checks if the currentPlayer already has the
                    // ingredient he has found
                    // if the doesn't have (and needs), then add that
                    // ingredient to the pizza
                    int pizzaIngredientIndex = currentPlayer.pizza.ingredients.indexOf(ingredient);
                    if (pizzaIngredientIndex >= 0) {
                        System.out.print("Pizza do jogador: " + currentPlayer.pizza.getFlavor() + " -> ");
                        System.out.println("Ingrediente encontrado:" + position.getIngredient());
                        currentPlayer.pizza.ingredients.remove(pizzaIngredientIndex);
                    }

                    break;
                }
            }

            // checks if the current player finished the pizza
            if (currentPlayer.pizza.ingredients.size() == 0) {
                System.out.println(currentPlayer.position.current());
                break;
            }
        }
    }
}
