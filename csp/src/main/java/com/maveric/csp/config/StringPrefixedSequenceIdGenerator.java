package com.maveric.csp.config;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.id.Configurable;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;

import org.hibernate.type.Type;
import org.hibernate.type.spi.TypeConfiguration;

import java.io.Serializable;
import java.util.Properties;

import static com.maveric.csp.constants.Constants.*;

public class StringPrefixedSequenceIdGenerator extends SequenceStyleGenerator implements Configurable {

    public static final String VALUE_PREFIX_PARAMETER = SEQUENCE_GENERATOR_VALUE_PREFIX;
    public static final String VALUE_PREFIX_DEFAULT = "";
    private String valuePrefix;

    public static final String NUMBER_FORMAT_PARAMETER = SEQUENCE_GENERATOR_NUMBERFORMAT;
    public static final String NUMBER_FORMAT_DEFAULT = SEQUENCE_GENERATOR_NUMBER_FORMAT_DEFAULT;
    private String numberFormat;

    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) throws HibernateException {
        return valuePrefix + String.format(numberFormat, super.generate(session, object));
    }

    @Override
    public void configure(Type type, Properties params,
                          ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(new TypeConfiguration().getBasicTypeRegistry().getRegisteredType(Long.class),params, serviceRegistry);
        valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER,
                params, VALUE_PREFIX_DEFAULT);
        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER,
                params, NUMBER_FORMAT_DEFAULT);
    }

}
