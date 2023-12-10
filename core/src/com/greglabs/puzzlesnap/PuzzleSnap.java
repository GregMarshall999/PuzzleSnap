package com.greglabs.puzzlesnap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class PuzzleSnap extends ApplicationAdapter {
	private Skin glassy;
	private SpriteBatch batch;
	private Stage stage;
	private TextureRegion backgroundImage;

	@Override
	public void create() {
		VisUI.load();

		batch = new SpriteBatch();
		backgroundImage = new TextureRegion();

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		glassy = new Skin(Gdx.files.internal("glassy/glassy-ui.json"));

		Button button = new TextButton("Open", glassy, "small");
		button.setPosition(50, 50);
		stage.addActor(button);

		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				openFileExplorer();
			}
		});
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		if(backgroundImage.getTexture() != null) {
			batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		batch.end();

		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		glassy.dispose();
		VisUI.dispose();
	}

	private void openFileExplorer() {
		switch (Gdx.app.getType()) {
			case Desktop -> {
				VisWindow fileChooserWindow = new VisWindow("File Explorer");
				fileChooserWindow.setPosition(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f);
				fileChooserWindow.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

				FileChooser fileChooser = new FileChooser("Select an image", FileChooser.Mode.OPEN);
				fileChooser.setResizable(false);
				fileChooser.setListener(new FileChooserAdapter() {
					@Override
					public void selected(Array<FileHandle> files) {
						FileHandle selectedFile = files.first();
						Texture texture = new Texture(selectedFile);
						backgroundImage.setRegion(new TextureRegion(texture));
						fileChooserWindow.remove();
					}

					@Override
					public void canceled() {
						fileChooserWindow.remove();
					}
				});

				VisTable container = new VisTable();
				container.add(fileChooser.fadeIn()).pad(4).expand().fill();

				fileChooserWindow.add(container).expand().fill();
				fileChooserWindow.pack();

				fileChooserWindow.setResizable(true);
				fileChooserWindow.setResizeBorder(8);
				fileChooserWindow.pack();

				stage.addActor(fileChooserWindow);
			}
			case Android -> {
				//TODO handle android
			}
		}
	}
}
