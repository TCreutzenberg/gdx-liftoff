package gdx.liftoff.ui.dialogs;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Scaling;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.ScaleContainer;
import gdx.liftoff.Main;
import gdx.liftoff.ui.panels.CompleteButtonsFullscreenPanel;
import gdx.liftoff.ui.panels.CompletePanel;
import gdx.liftoff.ui.panels.GeneratingPanel;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static gdx.liftoff.Main.*;

/**
 * Dialog shown when in fullscreen layout mode and the user clicks the generate button. The layout scales up if the
 * available space is larger than 1920x1080
 */
public class FullscreenCompleteDialog extends PopTable {
    private static final float SPACING = 30;

    public FullscreenCompleteDialog() {
        super(skin.get("fullscreen", WindowStyle.class));
        setFillParent(true);
        pad(20);

        Table contentTable = new Table();
        ScaleContainer scaleContainer = new ScaleContainer(Scaling.fit, contentTable);
        scaleContainer.setPrefSize(1920, 1080);
        add(scaleContainer).grow();

        contentTable.defaults().space(SPACING);
        Button button = new Button(skin, "restore");
        contentTable.add(button).expandX().right();
        addHandListener(button);
        onChange(button, () -> {
            hide();
            root.fadeInTable();
            Main.restoreWindow();
        });

        contentTable.row();
        GeneratingPanel generatingPanel = new GeneratingPanel();

        Table table = new Table();
        contentTable.stack(generatingPanel, table);

        table.defaults().space(10);
        CompletePanel completePanel = new CompletePanel();
        table.add(completePanel);

        table.row();
        CompleteButtonsFullscreenPanel completeButtonsPanel = new CompleteButtonsFullscreenPanel(this);
        table.add(completeButtonsPanel);

        //initial setup
        table.setColor(CLEAR_WHITE);
        table.setTouchable(Touchable.disabled);
        generatingPanel.setColor(CLEAR_WHITE);

        //animation
        addAction(sequence(
            targeting(generatingPanel, fadeIn(.5f)),
            delay(1f),
            targeting(generatingPanel, fadeOut(.3f)),
            targeting(table, fadeIn(.3f)),
            targeting(table, touchable(Touchable.enabled))
        ));
    }

    public static void show() {
        FullscreenCompleteDialog fullscreenDialog = new FullscreenCompleteDialog();
        fullscreenDialog.show(stage);
    }
}
