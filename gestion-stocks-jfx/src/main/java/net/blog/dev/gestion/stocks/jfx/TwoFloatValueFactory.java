package net.blog.dev.gestion.stocks.jfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanFloatProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanFloatPropertyBuilder;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class TwoFloatValueFactory<S, T> implements
		Callback<CellDataFeatures<S, T>, ObservableValue<T>> {

	private String property;

	private String property2;

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
		ReadOnlyJavaBeanFloatPropertyBuilder builder = ReadOnlyJavaBeanFloatPropertyBuilder
				.create();
		builder.bean(cell.getValue());
		builder.name(getProperty());
		try {
			ReadOnlyJavaBeanFloatProperty build = builder.build();
			final String float1 = FrontUtils.formatPricePercentage(build.get());

			builder = ReadOnlyJavaBeanFloatPropertyBuilder.create();
			builder.bean(cell.getValue());
			builder.name(getProperty2());
			build = builder.build();
			final String float2 = FrontUtils.formatPricePercentage(build.get());
			return (ObservableValue<T>) new SimpleStringProperty(float1 + " ("
					+ float2 + " %)");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the property2
	 */
	public String getProperty2() {
		return property2;
	}

	/**
	 * @param property2
	 *            the property2 to set
	 */
	public void setProperty2(String property2) {
		this.property2 = property2;
	}
}
