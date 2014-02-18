/**
 * 
 */
package net.blog.dev.gestion.stocks.back.json;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * @author Kiva
 * 
 */
public class CleanFloatDeserializer extends JsonDeserializer<Float> {

	@Override
	public Float deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		if (node.asText() != null)
			return Float.parseFloat(node.asText().replaceAll("[^0-9.]*", ""));
		return null;
	}
}
