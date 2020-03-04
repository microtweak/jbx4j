package com.github.microtweak.jbx4j.serializer.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.github.microtweak.jbx4j.descriptor.spi.ReflectionHelper;
import com.github.microtweak.jbx4j.serializer.resolver.EntityResolverManager;

/**
 * @author Marcos Koch Salvador
 * @since 1.0.0
 */
public class Jbx4jJacksonModule extends SimpleModule {

    private EntityResolverManager resolverManager = new EntityResolverManager();

    @Override
    public String getModuleName() {
        return "jbx4j-jackson-module";
    }

    @Override
    public void setupModule(SetupContext context) {
        ObjectMapper mapper = context.getOwner();

        setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                Class<?> rootType = ReflectionHelper.getInstance().getJpaClass(beanDesc.getBeanClass());

                if (ReflectionHelper.getInstance().isEntity(rootType) || ReflectionHelper.getInstance().isEmbeddable(rootType)) {
                    return new EntitySerializer();
                }

                return serializer;
            }
        });

        setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                Class<?> rootType = ReflectionHelper.getInstance().getJpaClass(beanDesc.getBeanClass());

                if (ReflectionHelper.getInstance().isEntity(rootType) || ReflectionHelper.getInstance().isEmbeddable(rootType)) {
                    return new EntityDeserializer(Jbx4jJacksonModule.this, mapper, rootType);
                }

                return deserializer;
            }
        });

        super.setupModule(context);
    }

    public EntityResolverManager getResolverManager() {
        return resolverManager;
    }

}
