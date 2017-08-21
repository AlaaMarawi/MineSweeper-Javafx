/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFx_mayinTarlasi;

import com.sun.net.httpserver.Filter;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import jdk.nashorn.internal.ir.BreakNode;

/**
 *
 * @author alaa_ma
 */
public class GameController implements Initializable {

    @FXML
    Pane pane;

    @FXML
    TilePane tilePane;
    @FXML
    Label lbl_mayinNum;
    @FXML
    DialogPane dialogPane;
    @FXML
    Label lbl_game;

    public int y_button = 9;
    public int x_button = 9;

    public double buttonSize = 40;

    public int mineNum = 10;
    public int mineCount = 0;

    int flagNum;

//    ImageView img = new ImageView("mine2.jpg");
    private class ImageView_Mine extends ImageView {

        public ImageView_Mine(String url) {
            super(url);
            setFitHeight(buttonSize - 2);
            setFitWidth(buttonSize - 2);

        }
    }

    public class ToggleButton_Mine extends ToggleButton {

        public int x;
        public int y;
        public boolean hasBomb;
        public boolean hasFlag = false;

        public ToggleButton_Mine(int x, int y, boolean hasBomb, boolean hasFlag) {
            super();
            this.x = x;
            this.y = y;
            this.hasBomb = hasBomb;
            this.hasFlag = hasFlag;

            if (hasFlag) {
                setCacheShape(false);
            }

            this.setOnMouseClicked(new EventHandler1(this));

//             this.selectedProperty().addListener(new ChangeListener<Boolean>() {
//                 
//                    @Override
//                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                        if (!(this.isSelected())) {
//                            button.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
//                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
//                                    true, true, true, true, true, true, null));
//                        }
//
//                        System.out.println("listener");
//
//                    }
//                });


//                        to not to fire event when keyboard is pressed
            this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    event.consume();
                }
            });
        }
    }

    ToggleButton_Mine tb[][];

    public void select(ActionEvent ae) {
        dialogPane.setVisible(false);
        tilePane.setDisable(false);
        switch (((Button) ae.getSource()).getText()) {

            case "Easy":
                y_button = 9;
                x_button = 9;
                mineNum = 10;
                buttonSize = 40;
                break;
            case "Medium":
                y_button = 16;
                x_button = 16;
                mineNum = 40;
                buttonSize = 28;
                break;
            case "Hard":
                y_button = 23;
                x_button = 23;
                mineNum = 70;
                buttonSize = 25;
                break;

        }
        tilePane.getChildren().clear();
        newGame(x_button, y_button, mineNum, buttonSize);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newGame(x_button, y_button, mineNum, buttonSize);

    }

    public class EventHandler1 implements EventHandler<MouseEvent> {

        ToggleButton_Mine button;
        MouseEvent mevent;

        public EventHandler1(ToggleButton_Mine button) {
            this.button = button;
        }

        @Override
        public void handle(MouseEvent event) {

//            if (!(event.getSource() instanceof MouseButton)) {
//                return;
//            }
            System.out.println(event.getSource().getClass().toString());
            System.out.println(event.getButton());
            if (event.getButton().name().equals("SECONDARY")) {

                if (button.hasFlag) {
                    button.hasFlag = false;
                    button.setGraphic(null);
                    flagNum--;
                } else {
                    button.setSelected(false);
                    button.setGraphic(new ImageView_Mine("flag2.png"));
                    button.hasFlag = true;
                    flagNum++;
//                  button.setText("M");
//                  button.setTextFill(Color.DEEPPINK);
                }
                lbl_mayinNum.setText(mineNum - flagNum + "");
//                           
            } else {

                System.out.println("primary basıldı");

                if (button.hasFlag) {
//                                button.setCacheShape(false);
                    System.out.println("has flag!");
                    button.setSelected(false);
                    return;
                }

                if (button.hasBomb) {

                    button.setGraphic(new ImageView_Mine("mine2.jpg"));
                    button.setPrefSize(buttonSize, buttonSize);
                    button.setDisable(true);
                    if (button.isSelected()) {
                        Game_lose();
                    }

                } else {
                    button.setSelected(true);
                    mineCount = 0;
                    mineCount = mineCount(button.x, button.y);
//                    button.setText(mineCount + "");
                    button.setDisable(true);
                }
            }
            if (flagNum == mineNum) {
                int selectedButtonCount = 0;
                for (int i = 0; i < y_button; i++) {
                    for (int j = 0; j < x_button; j++) {
                        if (!tb[j][i].hasFlag && !tb[j][i].isSelected()) {
                            return;
                        }
                        if (tb[j][i].isSelected()) {
                            selectedButtonCount++;
                        }
                    }
                }
                if (selectedButtonCount == (x_button * y_button) - mineNum) {
                    Game_win();
                }

            }
        }

    }

    public void newGame(int xbutton, int ybutton, int mayinAdet, double buttonSize) {

        this.x_button = xbutton;
        this.y_button = ybutton;
        this.mineNum = mayinAdet;
        this.buttonSize = buttonSize;
        flagNum = 0;
        lbl_mayinNum.setText(this.mineNum + "");
        tb = new ToggleButton_Mine[x_button][y_button];

//        img.setFitHeight(buttonSize);
//        img.setFitWidth(buttonSize);
        tilePane.setPrefSize(x_button * buttonSize, y_button * buttonSize);
        tilePane.setPrefColumns(y_button);
        tilePane.setPrefRows(x_button);
//        tilePane.setLayoutX(pane.getPrefWidth()-tilePane.getPrefWidth()-20);
//         tilePane.setLayoutY(20);

        tilePane.setScaleX(1);
        tilePane.setScaleY(1);
        tilePane.setTranslateX(+180 - tilePane.getPrefWidth() / 2);
        tilePane.setTranslateY(-tilePane.getPrefHeight() / 2);
//        pane.setPrefSize(600, 600);
        pane.setMinSize(tilePane.getPrefWidth(), tilePane.getPrefHeight() + 100);

        for (int y = 0; y < y_button; y++) {
            for (int x = 0; x < x_button; x++) {

                ToggleButton_Mine button = new ToggleButton_Mine(x, y, false, false);
                tb[x][y] = button;
                button.setPrefSize(buttonSize, buttonSize);
                button.setMinSize(buttonSize, buttonSize);
                button.setBorder(new Border(new BorderStroke(Color.DARKORCHID, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT, new Insets(0))));
                button.setAlignment(Pos.CENTER);
//                button.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

//                button.setOnMouseClicked(new EventHandler1(button)); //moved to ToggleButton class
                button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (!button.isSelected()) {
                            button.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        }

                        System.out.println("listener");

                    }
                });

                tilePane.getChildren().add(button);
            }
        }

        Random rX = new Random();
        Random rY = new Random();

        for (int i = 0; i < mayinAdet; i++) {
            int x = rX.nextInt(x_button);
            int y = rY.nextInt(y_button);

            if (!tb[x][y].hasBomb) {
                tb[x][y].hasBomb = true;
            } else {
                i--;
            }
        }

    }

    public int mineCount(int x, int y) {
        mineCount = 0;
        System.out.println("basıldı  X=" + x + " - Y=" + y);
        for (int i = y - 1; i < y + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {

                if (i == y && j == x) {
                    continue;
                }
                try {
                    if (tb[j][i].hasBomb) {
                        mineCount++;
                    }
                } catch (Exception ex) {
                    continue;
                }
            }
        }
        if (mineCount != 0) {
            tb[x][y].setText(mineCount + "");
        }

        if (mineCount == 0) {
            for (int i = y - 1; i < y + 2; i++) {
                for (int j = x - 1; j < x + 2; j++) {
                    if (i < 0 || j < 0) {
                        continue;
                    }
                    try {
                        if (!tb[j][i].isSelected() && !tb[j][i].hasFlag) {
                            tb[j][i].fire();
                            tb[j][i].setSelected(false);

                            System.out.println("x= " + tb[j][i].x + " - y= " + tb[j][i].y);
                        }
                    } catch (Exception ex) {
                        continue;
                    }
                }
            }
        }

        return mineCount;
    }

    public void Game_lose() {

        for (int i = 0; i < y_button; i++) {
            for (int j = 0; j < x_button; j++) {
                if (tb[j][i].hasBomb) {
                    tb[j][i].fire();
                    tb[j][i].setSelected(false);
                }
            }
        }

        dialogPane.setVisible(true);
        lbl_game.setText("YOU LOSE !");

//                                adding a button or an event to existing button!!
//        Button cancleButton = new Button("new game");
//        cancleButton.setCancelButton(true);
//        cancleButton.setAlignment(Pos.BOTTOM_RIGHT);
//        cancleButton.setLayoutX(100);
//        cancleButton.setLayoutY(100);
//        dialogPane.getChildren().add(0, cancleButton);
        // ButtonType [text=Close, buttonData=CANCEL_CLOSE]
//        System.out.println("aaaaa" + dialogPane.getButtonTypes().get(0));
////       dialogPane.visibleProperty().addListener(new ChangeListener<Boolean>() {
////            @Override
////            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
////
////
////            }
////        });
        tilePane.setDisable(true);

//        tilePane.setEffect(new GaussianBlur());
//        dialogPane.getContent().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//dialogPane.setVisible(false);
//            }
//        });
//        dialogPane.getButtonTypes().addListener(new ListChangeListener<ButtonType>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends ButtonType> c) {
//                dialogPane.setVisible(false);
//            }
//        });
//        dialogPane.getContent().fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
//                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
//                true, true, true, true, true, true, null));
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                
//            }
//        }
    }

    public void Game_win() {
        dialogPane.setVisible(true);
        lbl_game.setText("YOU WIN !");
        tilePane.setDisable(true);
    }

}
