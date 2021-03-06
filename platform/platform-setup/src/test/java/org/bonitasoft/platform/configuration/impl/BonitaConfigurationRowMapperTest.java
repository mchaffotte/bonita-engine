package org.bonitasoft.platform.configuration.impl;

import static org.bonitasoft.platform.configuration.impl.ConfigurationFields.*;
import static org.mockito.Mockito.doReturn;

import java.sql.ResultSet;

import org.bonitasoft.platform.configuration.model.BonitaConfiguration;
import org.bonitasoft.platform.configuration.model.BonitaConfigurationAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Laurent Leseigneur
 */
@RunWith(MockitoJUnitRunner.class)
public class BonitaConfigurationRowMapperTest {

    @Mock
    private ResultSet rs;

    @Before
    public void setup() throws Exception {
        doReturn(1L).when(rs).getLong(TENANT_ID);
        doReturn("my resource").when(rs).getString(RESOURCE_NAME);
        doReturn("PLATFORM_PORTAL").when(rs).getString(CONTENT_TYPE);
        doReturn("my content".getBytes()).when(rs).getBytes(RESOURCE_CONTENT);
    }

    @Test
    public void testMapRow() throws Exception {
        //given
        BonitaConfigurationRowMapper bonitaConfigurationRowMapper = new BonitaConfigurationRowMapper();

        //when
        final BonitaConfiguration bonitaConfiguration = bonitaConfigurationRowMapper.mapRow(rs, 5);

        //then
        BonitaConfigurationAssert.assertThat(bonitaConfiguration)
                .hasResourceName("my resource")
                .hasResourceContent("my content".getBytes());
    }
}
