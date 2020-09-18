package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.*;

import static model.Utils.*;


public class Attr {
    private static final ObservableList<String> types = FXCollections.observableArrayList("int", "String", "boolean");
    private TextField name;               // название атрибута
    private ComboBox<String> type;        // типы атрибутов (int, var,...)
    private Button del;                   // кнопка для удаления
    private Circle circle;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private HBox boxData, boxRow;
    private StackPane stackPaneCircles;
    private Table table;
    private Table link;
    private Attr attrStartLine;

    public Attr(Table table) {

        this.table = table;
        name = new TextField("row");                              // инициалицируем имя аттрибута
        boxData = new HBox();
        boxRow = new HBox();
        stackPaneCircles = new StackPane();
        circle = new Circle(10, Color.rgb(55, 113, 241));

        type = new ComboBox<>(types);                             // помещаем созданную выборку типов

        //инициализируем кнопку и устанавливаем ей картинку
        del = new Button("", new ImageView(new Image("images/button_delete_attr.png")));
        del.getStyleClass().add("btn-del-attr");                  // добавляем стиль .frame из файла css/style.css

        init();
    }

    private void init() {

        name.getStyleClass().add("name-attribute-data");          // добавляем стиль .frame из файла css/style.css
        name.setFocusTraversable(false);                          // при разлицных кликах на таблице отменяем автофокус

        type.setValue("int");                                     // устанавливаем значение INT
        type.getStyleClass().add("type-attribute-data");          // добавляем стиль .frame из файла css/style.css

        boxData.setSpacing(5);
        boxData.getStyleClass().add("attribute-data");            // добавляем стиль .frame из файла css/style.css
        boxData.setAlignment(Pos.CENTER);
        boxData.getChildren().addAll(name, type, del);

        del.getStyleClass().add("btn-del-attr");                  // добавляем стиль .frame из файла css/style.css

        circle.setVisible(false);
        stackPaneCircles.getChildren().add(circle);
        stackPaneCircles.setAlignment(Pos.CENTER);
        stackPaneCircles.toFront();

        boxRow.setAlignment(Pos.CENTER);
        boxRow.getChildren().addAll(stackPaneCircles, boxData);

        Utils.pane.getChildren().remove(Utils.line);
        Utils.pane.getChildren().add(Utils.line);

        Utils.line.setStroke(Color.RED);
        Utils.line.setStrokeWidth(2.5);

        initListeners();
    }

    private void initListeners() {

        name.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                this.getBoxRow().requestFocus();

        });

        circle.setOnMousePressed(e -> {

            Utils.isCreateLink = true;

            orgSceneX = e.getSceneX();
            orgSceneY = e.getSceneY();

            orgTranslateX = circle.getTranslateX();
            orgTranslateY = circle.getTranslateY();

            attrStartLine = this;

            Utils.line.setStartX(e.getSceneX());
            Utils.line.setStartY(e.getSceneY());

            Utils.line.setEndX(e.getSceneX());
            Utils.line.setEndY(e.getSceneY());
        });


        circle.setOnMouseDragged(t -> {

            if (!Utils.isCreateLink) return;
            Utils.line.setVisible(true);

            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            circle.setTranslateX(newTranslateX);
            circle.setTranslateY(newTranslateY);

            Utils.line.setStartX(t.getSceneX());
            Utils.line.setStartY(t.getSceneY());

        });

        circle.setOnMouseReleased(e -> {
            Utils.isCreateLink = false;
            Utils.line.setVisible(false);
            circle.setVisible(false);

            myIntersect(e.getSceneX(), e.getSceneY());

            circle.setTranslateX(0);
            circle.setTranslateY(0);

            if (e.getSource() instanceof HBox) {
                System.err.println("+++");
            }
        });


        boxRow.setOnMouseEntered(e -> {
            circle.setVisible(true);
        });

        boxRow.setOnMouseExited(e -> {
            circle.setVisible(false);
        });

        del.setOnMouseClicked(e -> {

            Links linksForDel = null;


            boolean flag = false;
            for (Links links : links) {

                if (links.getAs().equals(this) || links.getAe().equals(this)) {

                    pane.getChildren().remove(links.getLink());

                    links.getAs().getBoxCircles().getChildren().remove(links.getStart());
                    links.getAe().getBoxCircles().getChildren().remove(links.getEnd());

                    links.getAe().setLink(null);

                    linksForDel = links;

                    flag = true;
                }


                if (!flag) {
                    if (check(this, links.getAs()) && links.getTs().equals(table)) {

                        links.getLink().setStartY(links.getLink().getStartY() - 45);
                        links.setHs(links.getHs() - 45);
                        System.err.println("2");
                    }

                    if (check(this, links.getAe()) && links.getTe().equals(table)) {
                        links.getLink().setEndY(links.getLink().getEndY() - 45);
                        links.setHe(links.getHe() - 45);
                        System.err.println("3");
                    }
                }
            }

            if (linksForDel != null) {
                Utils.links.remove(linksForDel);
            } else {
                table.getvBox().getChildren().remove(getBoxRow());
                table.getAttributes().remove(this);
                table.refresh();
            }
        });
    }

    private boolean check(Attr aDel, Attr cur) {
        return table.getAttributes().indexOf(aDel) < table.getAttributes().indexOf(cur);

    }


    private void myIntersect(double x, double y) {

        for (Table t : listTables) {
            if (t.equals(table)) continue;


            for (Attr a : t.getAttributes()) {

                Rectangle r1 = new Rectangle(x, y, 2, 2);
                Rectangle r2 = new Rectangle((t.getvBox().getLayoutX() + t.getvBox().getTranslateX()), (a.getBoxRow().getLayoutY() + t.getvBox().getLayoutY() + t.getvBox().getTranslateY()), a.getBoxRow().getWidth(), a.getBoxRow().getHeight());

                if (r1.intersects(r2.getLayoutBounds())) {

                    Circle start = new Circle((table.getvBox().getLayoutX() + table.getvBox().getTranslateX() + 10), (attrStartLine.getBoxRow().getLayoutY() + table.getvBox().getLayoutY() + table.getvBox().getTranslateY() + 23), 5, Color.RED);

                    Circle end = new Circle((t.getvBox().getLayoutX() + t.getvBox().getTranslateX() + 10), (a.getBoxRow().getLayoutY() + t.getvBox().getLayoutY() + t.getvBox().getTranslateY() + 23), 2, Color.GREEN);

                    Links lks = new Links(start, end, table, a.getTable(), this.getBoxRow().getLayoutY(), a.getBoxRow().getLayoutY(), this, a);

                    links.add(lks);

                    pane.getChildren().add(lks.getLink());

                    this.getBoxCircles().getChildren().add(start);
                    a.getBoxCircles().getChildren().add(end);

                    this.getTable().HEIGHT = this.getBoxRow().getLayoutY();

                    a.getTable().HEIGHT = a.getBoxRow().getLayoutY();
                    a.setLink(table);
                    break;
                }
            }
        }
    }

    public StackPane getBoxCircles() {
        return stackPaneCircles;
    }

    public ArrayList<Links> getLinks() {
        return links;
    }

    public HBox getBoxRow() {
        return boxRow;
    }

    public TextField getName() {
        return name;
    }

    public ComboBox<String> getType() {
        return type;
    }

    public Table getTable() {
        return table;
    }

    public Table getLink() {
        return link;
    }


    public void setType(String type) {
        this.type.getItems().add(type);
        this.type.setValue(type);
    }

    public void setLink(Table link) {
        this.link = link;
    }
}
