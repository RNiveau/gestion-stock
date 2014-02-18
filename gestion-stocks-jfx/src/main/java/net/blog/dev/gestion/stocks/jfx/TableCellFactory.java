package net.blog.dev.gestion.stocks.jfx;

import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TableCellFactory<S, T> implements
		Callback<TableColumn<S, T>, TableCell<S, T>> {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TableCell<S, T> call(TableColumn<S, T> p) {
		TableCell<S, T> cell = new TableCell() {
			@Override
			public void updateItem(Object item, boolean empty) {
				if (item == getItem())
					return;

				super.updateItem(item, empty);

				if (item == null) {
					super.setText(null);
					super.setGraphic(null);
				} else if (item instanceof Node) {
					super.setText(null);
					super.setGraphic((Node) item);
				} else {
					super.setText(item.toString());
					super.setGraphic(null);
				}
			}
		};
		return cell;
	}
}
