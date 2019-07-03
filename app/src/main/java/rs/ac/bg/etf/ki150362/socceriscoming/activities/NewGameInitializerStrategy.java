package rs.ac.bg.etf.ki150362.socceriscoming.activities;

public class NewGameInitializerStrategy extends InitializerStrategy {

    private int gameMode;

    private String homePlayerName;
    private String guestPlayerName;

    private int homePlayerDrawableId;
    private int guestPlayerDrawableId;

    @Override
    public void init(Game game) {

        game.gameMode = gameMode;

        game.homePlayerName = homePlayerName;
        game.homePlayerDrawableId = homePlayerDrawableId;

        game.guestPlayerName = guestPlayerName;
        game.guestPlayerDrawableId = guestPlayerDrawableId;

        super.init(game);
    }

    public NewGameInitializerStrategy(int gameMode, String homePlayerName, String guestPlayerName, int homePlayerDrawableId, int guestPlayerDrawableId) {

        this.gameMode = gameMode;

        this.homePlayerName = homePlayerName;
        this.guestPlayerName = guestPlayerName;

        this.homePlayerDrawableId = homePlayerDrawableId;
        this.guestPlayerDrawableId = guestPlayerDrawableId;

    }

}
