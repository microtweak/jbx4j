package com.github.microtweak.jbx4j.serializer.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.microtweak.jbx4j.descriptor.JpaDescriptor;
import com.github.microtweak.jbx4j.descriptor.attribute.EntityAttribute;
import com.github.microtweak.jbx4j.descriptor.attribute.RelationshipEntityAttribute;
import com.github.microtweak.jbx4j.serializer.resolver.JpaEntityData;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class JacksonJpaEntityData<E> implements JpaEntityData<E> {

	private Map<String, Object> data = new LinkedHashMap<>();
	
	protected JacksonJpaEntityData(Class<E> clazz, ObjectMapper mapper, JsonNode jsonNode) throws JsonProcessingException {
		for (EntityAttribute attr : JpaDescriptor.of(clazz).getAttributes()) {
			JsonNode attrNode = jsonNode.get(attr.getName());
			
			if (attr instanceof RelationshipEntityAttribute || Objects.isNull(attrNode)) {
				continue;
			}
			
			Object value = mapper.treeToValue(attrNode, attr.getType());
			data.put(attr.getName(), value);
		}
	}

	@Override
	public Object get(String attrName) {
		return data.get(attrName);
	}

}
