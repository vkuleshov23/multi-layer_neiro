package ai;

import ai.controller.MainSceneController;
import ai.util.WriteSaveProgress;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class FxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(AiMlApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(MainSceneController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        WriteSaveProgress writeSaveProgress = applicationContext.getBean(WriteSaveProgress.class);
        writeSaveProgress.load();
    }

    @Override
    public void stop() {
        WriteSaveProgress writeSaveProgress = applicationContext.getBean(WriteSaveProgress.class);
        writeSaveProgress.save();
        this.applicationContext.close();
        Platform.exit();
    }
}
