package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

//USE WS buttons and DOWN-UP buttons to play. Run this application as Desktop.
//This is a multiplayer Pong-game, with a Super-mario theme.
public class MyGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture pipe1;
	private Texture pipe2;
	private Texture ball;
	private Texture background;

	//Coordinates for pipes
	private float pipe1X;
	private float pipe1Y;
	private float pipe2X;
	private float pipe2Y;

	//Speed and coordinates for the fireball
	private float moveSpeed;
	private float ballX;
	private float ballY;
	private float ballSpeedX;
	private float ballSpeedY;

	private float screenWidth;
	private float screenHeight;

	private int point1;
	private int point2;
	private boolean gameOver = false;
	private BitmapFont font;
	private BitmapFont gameFont;
	private BitmapFont gameFont2;


	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		gameFont = new BitmapFont();
		gameFont2 = new BitmapFont();

		point1 = 0;
		point2 = 0;

		font.getData().scale(2);
		gameFont.getData().scale(3);
		gameFont2.getData().scale(2);

		//Scaling down the pipe images
		Pixmap pixmap200 = new Pixmap(Gdx.files.internal("transparentPipe.png"));
		Pixmap pixmap100 = new Pixmap(100, 100, pixmap200.getFormat());
		pixmap100.drawPixmap(pixmap200,
				0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
				0, 0, pixmap100.getWidth(), pixmap100.getHeight()
		);
		pipe1 = new Texture(pixmap100);
		pipe2 = new Texture(pixmap100);
		pixmap200.dispose();
		pixmap100.dispose();

		//Scaling down the fireball image
		Pixmap pixmap300 = new Pixmap(Gdx.files.internal("flower2.png"));
		Pixmap pixmap50 = new Pixmap(50, 50, pixmap300.getFormat());
		pixmap50.drawPixmap(pixmap300,
				0, 0, pixmap300.getWidth(), pixmap300.getHeight(),
				0, 0, pixmap50.getWidth(), pixmap50.getHeight()
		);
		ball = new Texture(pixmap50);
		pixmap300.dispose();
		pixmap50.dispose();

		//Scaling up the background image
		Pixmap prevBackground = new Pixmap(Gdx.files.internal("background.jpg"));
		Pixmap scaled = new Pixmap(800, 700, prevBackground.getFormat());
		scaled.drawPixmap(prevBackground,
				0, 0, prevBackground.getWidth(), prevBackground.getHeight(),
				0, 0, scaled.getWidth(), scaled.getHeight()
		);
		background = new Texture(scaled);
		prevBackground.dispose();
		scaled.dispose();


		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();


		pipe1X = 10;
		pipe1Y = 100;

		pipe2X = screenWidth - pipe1.getWidth();
		pipe2Y = 100;

		//Ball coordinates and speed
		ballSpeedX = 2;
		ballSpeedY = 1;

		moveSpeed = 2;

		ballX = screenWidth/2 - ball.getWidth()/2;
		ballY = screenHeight/2 - ball.getHeight()/2;


	}

	public void resetGame() {
		ballX = Gdx.graphics.getWidth()/2 - ball.getWidth()/2;
		ballY = Gdx.graphics.getHeight()/2 - ball.getHeight()/2;
		pipe1X = 10;
		pipe1Y = 100;

		ballSpeedX = 2;
		ballSpeedY = 1;
		moveSpeed = 2;

		pipe2X = screenWidth - pipe1.getWidth();
		pipe2Y = 100;

		if(point1 >= 3 || point2 >=21) {
			gameOver = true;
			point1 = 0;
			point2 = 0;

		}
	}

	public void checkWin() {
		if(ballX < 0 ) {
			point2++;
			resetGame();
		}

		if(ballX > screenWidth - ball.getWidth()) {
			point1++;
			resetGame();
		}
	}
	public int getPoint1() {
		return point1;
	}
	public int getPoint2() {
		return point2;
	}


	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ballX += ballSpeedX;
		ballY += ballSpeedY;

		Rectangle rect1 = new Rectangle(pipe1X,pipe1Y,pipe1.getWidth(),pipe1.getHeight());
		Rectangle rect2 = new Rectangle(pipe2X,pipe2Y,pipe1.getWidth(),pipe1.getHeight());
		Rectangle rect3 = new Rectangle(ballX,ballY,ball.getWidth(),ball.getHeight());

		//Increase speed every time a pipe is hit
		if(rect1.overlaps(rect3)) {
			ballSpeedX *=-1 - 0.2;
			ballSpeedY *=-1 - 0.2;
			moveSpeed += 0.1;

		}
		if(rect2.overlaps(rect3)) {
			ballSpeedX *=-1 - 0.2;
			ballSpeedY *=-1 - 0.2;
			moveSpeed += 0.1;

		}

		if(Gdx.input.isKeyPressed(Input.Keys.W) && pipe1Y < Gdx.graphics.getHeight() - pipe1.getHeight()) {
			pipe1Y += moveSpeed;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.S) && pipe1Y > 0) {
			pipe1Y -= moveSpeed;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.UP) && pipe2Y < Gdx.graphics.getHeight() - pipe2.getHeight() ) {
			pipe2Y += moveSpeed;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && pipe2Y > 0) {
			pipe2Y -= moveSpeed;

		}
		if(ballY < 0 || ballY > screenHeight) {
			ballSpeedY *= -1;
		}

		//Restart game if one player reaches 21 points
		if(gameOver && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			gameOver=false;
			resetGame();
		}


		checkWin();

		batch.begin();
		batch.draw(background,0,0);
		batch.draw(pipe1, pipe1X,pipe1Y);
		batch.draw(pipe2,pipe2X,pipe2Y);
		font.draw(batch, String.valueOf(getPoint1()), 30,480);
		font.draw(batch, String.valueOf(getPoint2()), screenWidth - pipe1.getWidth(),480);

		if(!gameOver) {
			batch.draw(ball,ballX,ballY);
		}
		if(gameOver) {
			gameFont.draw(batch, "Game over",  180,300);
			gameFont2.draw(batch, "Press R to restart", 180, 200);
		}
		batch.end();



	}
	
	@Override
	public void dispose () {
		batch.dispose();
		pipe1.dispose();

	}
}
