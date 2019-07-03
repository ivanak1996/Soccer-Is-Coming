package rs.ac.bg.etf.ki150362.socceriscoming.activities;

public class ResumeGameInitializerStrategy extends InitializerStrategy {

    private GameState gameState;

    public ResumeGameInitializerStrategy(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void init(Game game) {

        if (gameState != null) {

            game.gameMode = gameState.gameMode;

            game.homePlayerDrawableId = gameState.homePlayerDrawableId;
            game.guestPlayerDrawableId = gameState.guestPlayerDrawableId;

            super.init(game);

            game.elapsedInTotal = gameState.elapsedInTotal;
            game.homePlayerName = gameState.homePlayerName;
            game.guestPlayerName = gameState.guestPlayerName;

            game.homeTeam.setScore(gameState.homePlayerScore);
            game.guestTeam.setScore(gameState.guestPlayerScore);

            for (int i = 0; i < Team.NUMBER_OF_PLAYERS; i++) {

                game.homeTeam.getPlayers().get(i).setX(gameState.homePlayersCoordinates[i][0]);
                game.homeTeam.getPlayers().get(i).setY(gameState.homePlayersCoordinates[i][1]);

                game.homeTeam.getPlayers().get(i).setVxVector(gameState.homePlayersVelocities[i][0]);
                game.homeTeam.getPlayers().get(i).setVyVector(gameState.homePlayersVelocities[i][1]);

                game.guestTeam.getPlayers().get(i).setX(gameState.guestPlayersCoordinates[i][0]);
                game.guestTeam.getPlayers().get(i).setY(gameState.guestPlayersCoordinates[i][1]);

                game.guestTeam.getPlayers().get(i).setVxVector(gameState.guestPlayersVelocities[i][0]);
                game.guestTeam.getPlayers().get(i).setVyVector(gameState.guestPlayersVelocities[i][1]);

            }

            game.turn = gameState.homePlayersTurn ? game.homeTeam : game.guestTeam;

            game.ball.setX(gameState.ballX);
            game.ball.setY(gameState.ballY);

            game.ball.setVxVector(gameState.ballVx);
            game.ball.setVyVector(gameState.ballVy);

        }

    }

}
