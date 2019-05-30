package rs.ac.bg.etf.ki150362.socceriscoming.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import rs.ac.bg.etf.ki150362.socceriscoming.R;

public abstract class InitializerStrategy {

    public void init(Game game) {

        Bitmap ballImage = BitmapFactory.decodeResource(game.resources, R.drawable.ball_dragon);
        ballImage = Bitmap.createScaledBitmap(ballImage, 100, 100, false);
        game.ballImage = ballImage;

        Bitmap homePlayerImage = BitmapFactory.decodeResource(game.resources, game.homePlayerDrawableId);
        homePlayerImage = Bitmap.createScaledBitmap(homePlayerImage, 200, 200, false);
        game.homeTeamImage = homePlayerImage;

        Bitmap opponentPlayerImage = BitmapFactory.decodeResource(game.resources, game.guestPlayerDrawableId);
        opponentPlayerImage = Bitmap.createScaledBitmap(opponentPlayerImage, 200, 200, false);
        game.guestTeamImage = opponentPlayerImage;

        game.ball.init(ballImage);
        game.homeTeam.init(homePlayerImage);
        game.guestTeam.init(opponentPlayerImage);

    }
}
