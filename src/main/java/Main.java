
import grails.Project;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Attr;
import model.Table;
import model.Utils;


public class Main extends Application {

    ContextMenu contextMenu = new ContextMenu();

    private double x;
    private double y;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Utils.pane = new Pane();

        Scene scene = new Scene(Utils.pane);
        stage.setScene(scene);
        stage.setTitle("First Application");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
        scene.getStylesheets().add("css/style.css");

        Utils.iv.setX(5);
        Utils.iv.setY(5);

        Utils.iv.setImage(new Image("images/trash.png"));

        Utils.pane.getChildren().add(Utils.iv);



        MenuItem create_table = new MenuItem("Создать таблицу");

        create_table.setOnAction(event -> {
            new Table(Utils.pane, x, y);
            contextMenu.hide();
        });

        MenuItem start = new MenuItem("Генерация");
        start.setOnAction(event -> {


            for (Table t : Utils.listTables) {
                System.err.println("table=" + t.getName().getText());
                for (Attr attr : t.getAttributes()) {
                    Table link = attr.getLink();
                    if (link != null) {
                        attr.setType(attr.getLink().getName().getText());
                        System.err.println("attr=" + attr.getName().getText() + " link=" + link.getName().getText() + " " + attr.getType().getSelectionModel().getSelectedItem());

                    } else

                        System.err.println("attr=" + attr.getName().getText() + " link=null " + attr.getType().getSelectionModel().getSelectedItem());

                }
                System.err.println();
            }
            System.err.println("/////////////////////");


            Project p = new Project();
            p.create();


            Utils.listTables.forEach(e -> {
                String content = p.createContentFile(e);

                System.err.println(content);

                p.replaceFile(e.getName().getText(), content);
            });


            p.generateAll();
        });

        MenuItem close = new MenuItem("Закрыть");
        close.setOnAction(event -> {


        });

        MenuItem deleteLink = new MenuItem("Создать таблицу");

        deleteLink.setOnAction(event -> {
            new Table(Utils.pane, x, y);
            contextMenu.hide();
        });

        contextMenu.getItems().addAll(create_table, start, close);

        Utils.pane.setOnContextMenuRequested(event -> {

            x = event.getSceneX();
            y = event.getSceneY();
            contextMenu.show(Utils.pane, event.getScreenX(), event.getScreenY());
        });

    }

}