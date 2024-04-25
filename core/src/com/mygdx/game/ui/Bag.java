package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.CoreGame;

public class Bag extends Table {

    private final CoreGame game;
    private final Image itemImage[];

    private final Table bagTable;
    public int item;
    public int maxItem;
    private TextureRegionDrawable textureRegionDrawableBg;

    public Bag(Skin skin, CoreGame game) {
        super(skin);
        this.game =game;
        item = 0;
        maxItem = 4;
        bagTable = new Table();
//        bagTable.setColor(new Color(0f, 0f, 0f, 1f));

        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.BLACK);
        bgPixmap.fill();

        textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        bagTable.setBackground(textureRegionDrawableBg);
        bagTable.top().left();


//        Color bgColor = new Color(0f, 0f, 0f, 0.5f); // Độ trong suốt alpha = 0.5
//        bagTable.setBackground((Drawable) bgColor);

        setFillParent(true);
        bagTable.setDebug(true);
        itemImage = new Image[maxItem];

        for (int i = 0; i < maxItem; i++) {
            Texture texture = new Texture(Gdx.files.internal("assets/Items/Treasure/Coin2.png"));
            itemImage[i] = new Image(texture);

            bagTable.add(itemImage[i]).expand().center().pad(5);
            bagTable.scaleBy(0.5f);
        }

        this.add(bagTable); // Add bagTable to Bag
        System.out.println("Bag created");
        updateBag();

    }

    public void updateBag() {
        System.out.println("Bag updated");

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    // private Texture setItemImage() {
    //     Texture texture = new Texture(Gdx.files.internal("badlogic.png"));
    //     return texture;
    // }
//    public void setInputProcessor() {
//        Gdx.input.setInputProcessor(new InputAdapter() {
//            @Override
//            public boolean keyDown(int keycode) {
//                if (keycode == Input.Keys.U) {
//                    setVisible(!isVisible());
//                }
//                return true;
//            }
//        });
//    }
}
