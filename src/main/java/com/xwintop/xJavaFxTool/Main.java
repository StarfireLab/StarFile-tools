package com.xwintop.xJavaFxTool;

import com.xwintop.xJavaFxTool.fxmlView.IndexView;
import com.xwintop.xJavaFxTool.utils.StageUtils;
import com.xwintop.xJavaFxTool.utils.XJavaFxSystemUtil;
import com.xwintop.xcore.util.javafx.AlertUtil;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.GUIState;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @ClassName: Main
 * @Description: 启动类
 * @author: xufeng
 * @date: 2017年11月10日 下午4:34:11
 */
@SpringBootApplication
@Slf4j
public class Main extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        XJavaFxSystemUtil.initSystemLocal();//初始化本地语言
        XJavaFxSystemUtil.addJarByLibs();//添加外部jar包

        SplashScreen splashScreen = new SplashScreen() {
            @Override
            public String getImagePath() {
                return "/images/javafx.png";
            }
            @Override
            public boolean visible() {
                return false;
            }
        };
        launch(Main.class, IndexView.class, splashScreen, args);
//		launchApp(Main.class, IndexView.class, args);
    }

    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        super.beforeInitialView(stage, ctx);
        stage.initStyle(StageStyle.UNDECORATED);
        BorderPane root = new BorderPane();



        Scene scene = new Scene(new AnchorPane(),1400,800);
//        Scene scene = JavaFxViewUtil.getJFXDecoratorScene(stage, "", null, new AnchorPane());
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            /*
             * 鼠标按下时，记下相对于 root左上角(0,0) 的 x, y坐标, 也就是x偏移量 = x - 0, y偏移量 = y - 0
             */
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            /*
             * 根据偏移量设置primaryStage新的位置
             */
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });


        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (AlertUtil.showConfirmAlert("确定要退出吗？")) {
                    System.exit(0);
                } else {
                    event.consume();
                }
            }
        });
        GUIState.setScene(scene);
        Platform.runLater(() -> {
            StageUtils.updateStageStyle(GUIState.getStage());
        });
    }
}
