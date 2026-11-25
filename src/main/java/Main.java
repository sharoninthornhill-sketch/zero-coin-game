import javafx.scene.image.Image;
import zero.Color;
import zero.GameApp;
import zero.Sound;
import zero.ui.Button;
import zero.ui.Column;
import zero.ui.Label;
import zero.ui.Row;

public class Main extends GameApp {
    private Image playerImage;
    private Sound chime;
    private Player player;
    private PlayerInput input;
    private Coin[] coins;
    private boolean won;
    private int points;
    private ScoreDisplay score;
    private Label message;

    @Override
    public void settings() {
        padding(20);
        size(640, 400);
    }

    @Override
    public void setup() {
        playerImage = loadImage("/media-demo/player.png");
        chime = loadSound("/media-demo/chime.wav");
        if (chime != null) {
            chime.setVolume(1);
        }
        input = new PlayerInput(this);
        score = new ScoreDisplay();
        score.setCaption("Score");
        message = new Label("");
        var badge = new CreditsBadge("Coin game");
        var restartButton = new Button("Restart");
        restartButton.onClick(() -> restart());
        controls(new Column(8,
                new Row(12, score.view(), message, restartButton), badge.view()));
        restart();
    }

    public void restart() {
        player = new Player(40, 180);
        player.setSprite(playerImage);
        coins = new Coin[] {
                new Coin(220, 180), new Coin(420, 80), new Coin(540, 300)
        };
        won = false;
        message.setText("Collect all " + coins.length + " coins.");
        points = 0;
        score.setScore(points);
        showGame();
    }

    @Override
    public void update(double seconds) {
        if (won)
            return;
        int horizontal = input.horizontal();
        int vertical = input.vertical();
        player.update(horizontal, vertical, seconds, width(), height());
        for (Coin each : coins) {
            if (!each.isCollected() && each.touches(player)) {
                each.collect();
                points++;
                score.setScore(points);
                if (chime != null) {
                    chime.play();
                }
            }
        }
        if (points == coins.length) {
            won = true;
            message.setText("You win! Choose Restart.");
        }
    }

    @Override
    public void draw() {
        background(Color.rgb(240, 248, 255));
        fill(Color.rgb(47, 79, 79));
        textSize(20);
        text("Click the canvas, then hold arrows or WASD.", 20, 30);
        for (Coin each : coins)
            each.draw(this);
        player.draw(this);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
