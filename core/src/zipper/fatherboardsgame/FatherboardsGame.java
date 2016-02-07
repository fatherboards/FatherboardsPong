package zipper.fatherboardsgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;


public class FatherboardsGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture paddles;
    Texture background;
    Rectangle paddleLeft;
    Rectangle paddleRight;
    Rectangle backgroundLogo;
    Rectangle ballRect;
    Texture ball;
    Sound[] hitSound;
    BitmapFont font;
    int wincounter = 0;
    int curx = 50;
    int cury = 100;
    int speed = 4;
    boolean bottom = false;
    boolean left = false;
    boolean playing = true;
    int size = 50;
    int winShow = 0;
    OrthographicCamera camera;

    @Override
    public void create() {
        hitSound = new Sound[]{Gdx.audio.newSound(Gdx.files.internal("Cena.mp3")), Gdx.audio.newSound(Gdx.files.internal("Gelato.mp3")), Gdx.audio.newSound(Gdx.files.internal("Khaled.mp3")), Gdx.audio.newSound(Gdx.files.internal("Patrick.mp3")), Gdx.audio.newSound(Gdx.files.internal("Robotics.mp3")), Gdx.audio.newSound(Gdx.files.internal("Spanking.mp3")), Gdx.audio.newSound(Gdx.files.internal("Yeah.mp3")), Gdx.audio.newSound(Gdx.files.internal("What.mp3"))};
        batch = new SpriteBatch();
        paddles = new Texture("purple.jpg");
        background = new Texture("fatherboardslogo.png");
        paddleLeft = new Rectangle(25, 100, 25, 75);
        paddleRight = new Rectangle(750, 100, 25, 75);
        backgroundLogo = new Rectangle(500,300,400,400);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890:";

        font = generator.generateFont(parameter);
        generator.dispose();
        font.setColor(Color.CYAN);
    }

    @Override
    public void render() {
        if(playing) {
            playing = playing();
        }
        else {
            lost();
        }

    }
    public boolean playing() {
        camera.update();
        int prevwincounter = wincounter;

        Gdx.gl.glClearColor(0,1,1,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ballRect = new Rectangle(curx, cury, size, size);
        if (Gdx.input.isTouched()) {
            Vector3 touch = new Vector3();
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (touch.x < 400) paddleLeft.y = touch.y;
            else paddleRight.y = touch.y;
        }
        if (curx < 800 - size && !left) curx += speed;
        else left = true;
        if (curx > 0 && left) curx -= speed;
        else left = false;
        if (cury < 500 - size && !bottom) cury += speed;
        else bottom = true;
        if (cury > 0 && bottom) cury -= speed;
        else bottom = false;
        if (ballRect.overlaps(paddleRight)) {
            curx=700;
            left = true;
            wincounter++;
        }
        if (ballRect.overlaps(paddleLeft)) {
            curx=75;
            left = false;
            wincounter++;
        }

            switch (wincounter % 12) {
                case 0:
                    ball = new Texture("berger.PNG");
                    break;
                case 1:
                    ball = new Texture("andre.PNG");
                    break;
                case 2:
                    ball = new Texture("billini.PNG");
                    break;
                case 3:
                    ball = new Texture("brett.png");
                    break;
                case 4:
                    ball = new Texture("brody.PNG");
                    break;
                case 5:
                    ball = new Texture("carsen.PNG");
                    break;
                case 6:
                    ball = new Texture("christian.PNG");
                    break;
                case 7:
                    ball = new Texture("gabriel.PNG");
                    break;
                case 8:
                    ball = new Texture("jackson.PNG");
                    break;
                case 9:
                    ball = new Texture("revilla.PNG");
                    break;
                case 10:
                    ball = new Texture("vega.PNG");
                    break;
                case 11:
                    ball = new Texture("zipper.PNG");
                    break;
                default:
                    ball = new Texture("fatherboardslogo.png");
                    break;
            }

        if (ballRect.overlaps(paddleRight)) {
            hitSound[(int)(Math.random()*8)].play();
            speed++;
        }
        if (ballRect.overlaps(paddleLeft)) {
            hitSound[(int)(Math.random()*8)].play();
            speed++;
        }


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.setColor(Color.RED);
        font.draw(batch, "SCORE: " + wincounter, 125, 450, 0, Align.center, true);
        font.setColor(Color.CYAN);
        if(prevwincounter!=wincounter) {
            winShow++;
        }
        if(winShow >0 && winShow < 7) {
            winShow++;
            batch.draw(background, 200, 25, 400, 400);
        }
        else if(winShow>=7) {
            winShow=0;
        }
        batch.draw(paddles, paddleLeft.x, paddleLeft.y, 25, 75);
        batch.draw(paddles, paddleRight.x, paddleRight.y, 25, 75);
        batch.draw(ball, curx, cury, size, size);
        batch.end();
        if(ballRect.x < 25) {
            curx = 50;
            speed = 4;
            left = false;
            return false;
        }
        else if(ballRect.x > 725) {
            curx = 700;
            speed = 4;
            left = true;
            return false;
        }
        else {
            return true;
        }
    }
    public void lost() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 200, 25, 400, 400);
        font.draw(batch, "CLICK THE LOGO TO PLAY AGAIN",410, 440, 0, Align.center, true);
        font.draw(batch, "SCORE: " + wincounter, 125, 75, 0, Align.center, true);
        batch.end();
        if (Gdx.input.isTouched()) {
            if(backgroundLogo.overlaps(new Rectangle(Gdx.input.getX()-500,Gdx.input.getY()-150,400,400))) {
                wincounter=0;
                playing = playing();
            }
        }
    }
}
