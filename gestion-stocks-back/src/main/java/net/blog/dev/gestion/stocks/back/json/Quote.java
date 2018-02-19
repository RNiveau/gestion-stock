/**
 * 
 */
package net.blog.dev.gestion.stocks.back.json;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * @author Kiva
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2096753063711776090L;
	private String symbol;

	@JsonProperty
	private String Name;

	@JsonProperty
	@JsonDeserialize(using = CleanFloatDeserializer.class)
	private Float Open;

	@JsonProperty
	private Long Volume;
	@JsonProperty
	private Float LastTradePriceOnly;

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Float getOpen() {
		return Open;
	}

	public void setOpen(Float open) {
		Open = open;
	}

	public Long getVolume() {
		return Volume;
	}

	public void setVolume(Long volume) {
		Volume = volume;
	}

	public Float getLastTradePriceOnly() {
		return LastTradePriceOnly;
	}

	public void setLastTradePriceOnly(Float lastTradePriceOnly) {
		LastTradePriceOnly = lastTradePriceOnly;
	}
}
