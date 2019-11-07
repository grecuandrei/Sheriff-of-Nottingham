package com.tema1.main;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();

        if (gameInput.isValidInput()) {
            Game game = new Game(gameInput.getPlayerNames().size(), gameInput.getRounds(),
                    gameInput.getPlayerNames(), gameInput.getAssetIds());
            game.play();
        }
    }
}
