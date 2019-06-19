package util;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class UiUpdaer {
    private TextArea textarea_log;
    private Label label;

    public UiUpdaer(TextArea textarea_log) {
        super();
        this.textarea_log = textarea_log;
    }

    public UiUpdaer(Label label) {
        this.label = label;
    }

    public void update(String content) {
        Platform.runLater(() -> {
            String formal = textarea_log.getText();
            textarea_log.setText(formal + "\r\n" + content);
        });
    }

    public void resetAndUpdate(String content) {

        Platform.runLater(() -> {
            label.setText(content);
        });
    }


}
