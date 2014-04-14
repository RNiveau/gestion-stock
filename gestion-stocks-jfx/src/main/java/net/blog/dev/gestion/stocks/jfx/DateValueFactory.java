package net.blog.dev.gestion.stocks.jfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValueFactory<S, T> implements
		Callback<CellDataFeatures<S, T>, ObservableValue<T>> {

    static private final Logger logger = LoggerFactory.getLogger(DateValueFactory.class);

	private String property;

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	public ObservableValue<T> call(CellDataFeatures<S, T> cell) {
		try {

            Field field = findField(cell.getValue().getClass());
            field.setAccessible(true);

            Date date = (Date)field.get(cell.getValue());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

			return (ObservableValue<T>) new SimpleStringProperty(simpleDateFormat.format(date));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			logger.error(e.getMessage());
        }
        return null;
    }

    private Field findField(Class<?> clazz) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(property);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            if (clazz.getGenericSuperclass() != null && clazz.getGenericSuperclass() != clazz)
                return findField((Class<?>) clazz.getGenericSuperclass());
            throw e;
        }
    }
}
