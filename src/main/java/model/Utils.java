package model;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Utils {

    public static Pane pane;
    public static final int START_COUNT_ATTR = 5;   // количество стартовых атрибутов
    public static final int HEIGHT_ATTR = 45;       // высота атрибута
    public static final int WIDTH_TABLE = 235;      // высота атрибута
    public static boolean isCreateLink = false;
    public static Line line = new Line();
    public static ArrayList<HBox> rows = new ArrayList<>();

    public static ArrayList<Links> links = new ArrayList<>();

    public static ArrayList<Group> listGroup = new ArrayList<>();

    public static ArrayList<Table> listTables = new ArrayList<>();

    public static ImageView iv = new ImageView();
}

