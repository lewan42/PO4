package model;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static model.Utils.*;

public class Table {

    private Rectangle rectangle;
    private Rectangle rectangle2;
    private Group group;
    private VBox vBox;
    public static ArrayList<VBox> attributesList = new ArrayList<>();
    private TextField name;                   // название сущности
    private Button btn_add_attr;                   // название сущности
    private double x;
    private double y;
    private double orgSceneX, orgSceneY;
    public double HEIGHT;
    public ArrayList<Attr> attributes = new ArrayList<>();

    /*
    Конструктор для создания таблицы
     */

    public Table() {

    }

    public Table(Pane root, double x, double y) {

        this.x = x;
        this.y = y;

        btn_add_attr = new Button("", new ImageView(new Image("images/button_add_attr.png")));
        group = new Group();

        Utils.listGroup.add(group);

        rectangle = new Rectangle(x, y, WIDTH_TABLE, 45);

        rectangle2 = new Rectangle(x, y + 50, WIDTH_TABLE, HEIGHT_ATTR * START_COUNT_ATTR);

        vBox = new VBox();
        vBox.setLayoutX(x - 12);
        vBox.setLayoutY(y + 50);

        attributesList.add(vBox);
        listTables.add(this);

        name = new TextField("Table");
        name.setLayoutX(x + 55);
        name.setLayoutY(y + 10);

        btn_add_attr.setLayoutX(x + 190);
        btn_add_attr.setLayoutY(y + 10);

        init(root);
    }

    private void init(Pane root) {
        rectangle.getStyleClass().add("frame-title");                           // добавляем стиль .frame из файла css/style.css
        rectangle2.getStyleClass().add("frame-body");                           // добавляем стиль .frame из файла css/style.css
        name.getStyleClass().add("name-table");                                 // добавляем стиль .frame из файла css/style.css
        name.setFocusTraversable(false);                                        // при разлицных кликах на таблице отменяем автофокус
        btn_add_attr.getStyleClass().add("btn-add-attr");                       // добавляем стиль .frame из файла css/style.css


        btn_add_attr.setOnMouseClicked(e -> {
            createAttr();
            refresh();
        });

        name.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                root.requestFocus();
        });


        rectangle.setOnMouseReleased(e -> {
            if (iv.contains(e.getSceneX(), e.getSceneY())) {
                listTables.remove(this);
                root.getChildren().remove(group);
            }
        });


        /*
        событие клика мыши
        необходимо для определения координат клика мыши
         */


        group.setOnMousePressed((t) -> {

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });

         /*

        событие перемешения объектов

         */
        group.setOnMouseDragged((t) -> {
            if (isCreateLink) return;

            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            group.getChildren().stream()
                    .filter(node -> node instanceof Rectangle || node instanceof TextField || node instanceof VBox || node instanceof Button)
                    .forEach(node -> {
                        node.setTranslateX(offsetX + node.getTranslateX());
                        node.setTranslateY(offsetY + node.getTranslateY());

                    });

            Table table = this;
            links.forEach(e -> {
                if (e.getTs().equals(table)) {

                    e.getLink().setStartX(table.getvBox().getTranslateX() + table.getvBox().getLayoutX() + 11);
                    e.getLink().setStartY(table.getvBox().getTranslateY() + table.getvBox().getLayoutY() + e.getHs() + 23);
                }

                if (e.getTe().equals(table)) {

                    e.getLink().setEndX(table.getvBox().getTranslateX() + table.getvBox().getLayoutX() + 11);
                    e.getLink().setEndY(table.getvBox().getTranslateY() + table.getvBox().getLayoutY() + e.getHe() + 23);
                }
            });


            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });

        root.getChildren().add(group);


        for (int i = 0; i < START_COUNT_ATTR; i++) {
            createAttr();
        }

        group.getChildren().addAll(rectangle, rectangle2, name, vBox, btn_add_attr);
    }

    public void refresh() {
        rectangle2.setHeight(HEIGHT_ATTR * countAttributes());
    }


    private int countAttributes() {
        return vBox.getChildren().size();
    }

    private void createAttr() {
        Attr attribute = new Attr(this);
        vBox.getChildren().add(attribute.getBoxRow());
        attributes.add(attribute);
    }

    public ArrayList<Attr> getAttributes() {
        return attributes;
    }

    public TextField getName() {
        return name;
    }

    public VBox getvBox() {
        return vBox;
    }

    public Group getGroup() {
        return group;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
