package com.github.microtweak.jbx4j.descriptor.eclipselink;

import com.github.microtweak.jbx4j.descriptor.exception.JpaLoadStateException;
import com.github.microtweak.jbx4j.descriptor.spi.AttributeLoadChecker;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.indirection.ValueHolderInterface;

import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EclipseLinkAttributeLoadChecker extends AttributeLoadChecker {

    private static final Logger LOG = Logger.getLogger(EclipseLinkAttributeLoadChecker.class.getName());

    private final Map<String, Field> WEAVING_VALUE_HOLDER_CACHE = new HashMap<>();

    @Override
    public boolean isLoaded(Object entity, String attrName) {
        try {
            return Persistence.getPersistenceUtil().isLoaded(entity, attrName);
        } catch (DescriptorException e) {
            try {
                ValueHolderInterface propertyVh = (ValueHolderInterface) getValueHolderOfAttribute(entity.getClass(), attrName).get(entity);

                if (propertyVh == null) {
                    final String msg = MessageFormat.format(
                        "Could not determine load state of class / attribute \"{0}.{1}\". Make sure this entity has even been provided by EclipseLink!",
                        entity.getClass().getName(), attrName
                    );
                    throw new JpaLoadStateException(msg);
                }

                return propertyVh.isInstantiated();
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                final String msg = MessageFormat.format(
                    "Failed to access ValueHolder of class/attribute \"{0}.{1}\". This may be related to issues with EclipseLink Weaving.",
                    entity.getClass().getName(), attrName
                );
                
                LOG.log(Level.WARNING, msg, ex);

                return false;
            }
        }
    }

    private synchronized Field getValueHolderOfAttribute(Class<?> entityClass, String attrName) {
        final String CACHE_VALUE_HOLDER_IDENTIFIER = getValueHolderFieldCacheName(entityClass, attrName);
        Field valueHolderField = WEAVING_VALUE_HOLDER_CACHE.get(CACHE_VALUE_HOLDER_IDENTIFIER);

        if (valueHolderField == null) {
            try {
                valueHolderField = entityClass.getDeclaredField( getValueHolderFieldName(attrName) );
                valueHolderField.setAccessible(true);

                WEAVING_VALUE_HOLDER_CACHE.put(CACHE_VALUE_HOLDER_IDENTIFIER, valueHolderField);
            } catch (NoSuchFieldException | SecurityException e) {
                final String msg = MessageFormat.format(
                    "The ValueHolder of the \"{0}\" attribute was not found in the \"{1}\" class. Make sure EclipseLink Weaving is enabled!",
                    attrName, entityClass.getName()
                );
                throw new JpaLoadStateException(msg, e);
            }
        }

        return valueHolderField;
    }

    private String getValueHolderFieldCacheName(Class<?> entityClass, String attrName) {
        return entityClass.getName() + "#" + attrName;
    }

    private String getValueHolderFieldName(String attrName) {
        return EclipseLinkConstants.WEAVING_PREFIX + attrName + EclipseLinkConstants.WEAVING_VALUE_HOLDER_SUFFIX;
    }

}
