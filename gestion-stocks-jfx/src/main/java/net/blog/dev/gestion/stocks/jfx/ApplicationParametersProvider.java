package net.blog.dev.gestion.stocks.jfx;

import javafx.application.Application.Parameters;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class ApplicationParametersProvider {
	private Parameters parameters;

	void setParameters(Parameters p) {
		this.parameters = p;
	}

	public @Produces
	Parameters getParameters() {
		return parameters;
	}
}
