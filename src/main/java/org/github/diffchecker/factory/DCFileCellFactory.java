package org.github.diffchecker.factory;

import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import org.github.diffchecker.model.DCFile;

public class DCFileCellFactory extends MFXListCell<DCFile> {

   private final MFXFontIcon userIcon;

    public DCFileCellFactory(MFXListView<DCFile> listView, DCFile file) {
        super(listView, file);
        userIcon = new MFXFontIcon("fas-user", 10);
        userIcon.getStyleClass().add("user-icon");
        render(file);
    }


    @Override
    protected void render(DCFile file) {
        super.render(file);
        if (userIcon != null) getChildren().add(0, userIcon);
    }
}
