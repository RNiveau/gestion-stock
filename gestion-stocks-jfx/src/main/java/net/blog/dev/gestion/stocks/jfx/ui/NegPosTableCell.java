package net.blog.dev.gestion.stocks.jfx.ui;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import net.blog.dev.gestion.stocks.jfx.utils.FrontUtils;

/**
 * Created by Xebia on 28/07/2014.
 */
public class NegPosTableCell<T, S> extends TableCell<T, S> {


    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            this.setTextFill(Color.web(item.toString().contains("-") ? FrontUtils.RED : FrontUtils.GREEN));
            setText(item.toString());
        }
    }


}
