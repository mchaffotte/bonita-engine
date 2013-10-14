/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.engine.profile.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bonitasoft.engine.events.EventActionType;
import org.bonitasoft.engine.events.EventService;
import org.bonitasoft.engine.events.model.SInsertEvent;
import org.bonitasoft.engine.events.model.SUpdateEvent;
import org.bonitasoft.engine.log.technical.TechnicalLoggerService;
import org.bonitasoft.engine.persistence.QueryOptions;
import org.bonitasoft.engine.persistence.ReadPersistenceService;
import org.bonitasoft.engine.persistence.SBonitaReadException;
import org.bonitasoft.engine.persistence.SBonitaSearchException;
import org.bonitasoft.engine.persistence.SelectByIdDescriptor;
import org.bonitasoft.engine.persistence.SelectListDescriptor;
import org.bonitasoft.engine.persistence.SelectOneDescriptor;
import org.bonitasoft.engine.profile.SProfileCreationException;
import org.bonitasoft.engine.profile.SProfileNotFoundException;
import org.bonitasoft.engine.profile.SProfileUpdateException;
import org.bonitasoft.engine.profile.builder.SProfileUpdateBuilder;
import org.bonitasoft.engine.profile.builder.impl.SProfileUpdateBuilderImpl;
import org.bonitasoft.engine.profile.model.SProfile;
import org.bonitasoft.engine.queriablelogger.model.SQueriableLogSeverity;
import org.bonitasoft.engine.recorder.Recorder;
import org.bonitasoft.engine.recorder.SRecorderException;
import org.bonitasoft.engine.recorder.model.InsertRecord;
import org.bonitasoft.engine.recorder.model.UpdateRecord;
import org.bonitasoft.engine.services.QueriableLoggerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Celine Souchet
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceImplForProfileTest {

    @Mock
    private EventService eventService;

    @Mock
    private TechnicalLoggerService logger;

    @Mock
    private ReadPersistenceService persistenceService;

    @Mock
    private QueriableLoggerService queriableLoggerService;

    @Mock
    private Recorder recorder;

    @InjectMocks
    private ProfileServiceImpl profileServiceImpl;

    /**
     * Test method for {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#getNumberOfProfiles(org.bonitasoft.engine.persistence.QueryOptions)}.
     */
    @Test
    public void getNumberOfProfilesWithOptions() throws Exception {
        final QueryOptions options = new QueryOptions(0, 10);
        when(persistenceService.getNumberOfEntities(SProfile.class, options, Collections.<String, Object> emptyMap())).thenReturn(1L);

        assertEquals(1L, profileServiceImpl.getNumberOfProfiles(options));
    }

    @Test(expected = SBonitaSearchException.class)
    public void getNumberOfProfilesWithOptionsThrowException() throws Exception {
        final QueryOptions options = new QueryOptions(0, 10);
        when(persistenceService.getNumberOfEntities(SProfile.class, options, Collections.<String, Object> emptyMap())).thenThrow(new SBonitaReadException(""));

        profileServiceImpl.getNumberOfProfiles(options);
    }

    /**
     * Test method for {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#searchProfiles(org.bonitasoft.engine.persistence.QueryOptions)}.
     */
    @Test
    public void searchProfilesWithOptions() throws Exception {
        final QueryOptions options = new QueryOptions(0, 10);
        when(persistenceService.searchEntity(SProfile.class, options, Collections.<String, Object> emptyMap())).thenReturn(new ArrayList<SProfile>());

        assertNotNull(profileServiceImpl.searchProfiles(options));
    }

    @Test(expected = SBonitaSearchException.class)
    public void searchProfilesWithOptionsThrowException() throws Exception {
        final QueryOptions options = new QueryOptions(0, 10);
        when(persistenceService.searchEntity(SProfile.class, options, Collections.<String, Object> emptyMap())).thenThrow(new SBonitaReadException(""));

        profileServiceImpl.searchProfiles(options);
    }

    /**
     * Test method for {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#getProfile(long)}.
     */
    @Test
    public void getProfile() throws Exception {
        final SProfile sProfile = mock(SProfile.class);

        doReturn(sProfile).when(persistenceService).selectById(Matchers.<SelectByIdDescriptor<SProfile>> any());

        assertEquals(sProfile, profileServiceImpl.getProfile(1));
    }

    @Test(expected = SProfileNotFoundException.class)
    public void getNoProfile() throws Exception {
        when(persistenceService.selectById(Matchers.<SelectByIdDescriptor<SProfile>> any())).thenReturn(null);

        profileServiceImpl.getProfile(1);
    }

    @Test(expected = SProfileNotFoundException.class)
    public void getProfileThrowException() throws Exception {
        when(persistenceService.selectById(Matchers.<SelectByIdDescriptor<SProfile>> any())).thenThrow(new SBonitaReadException(""));

        profileServiceImpl.getProfile(1);
    }

    /**
     * Test method for {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#getProfileByName(java.lang.String)}.
     */
    @Test
    public void getProfileByName() throws Exception {
        final String profileName = "plop";
        final SProfile sProfile = mock(SProfile.class);

        doReturn(sProfile).when(persistenceService).selectOne(Matchers.<SelectOneDescriptor<SProfile>> any());

        assertEquals(sProfile, profileServiceImpl.getProfileByName(profileName));
    }

    @Test(expected = SProfileNotFoundException.class)
    public void getNoProfileByName() throws Exception {
        when(persistenceService.selectOne(Matchers.<SelectOneDescriptor<SProfile>> any())).thenReturn(null);

        profileServiceImpl.getProfileByName("plop");
    }

    @Test(expected = SProfileNotFoundException.class)
    public void getProfileByNameThrowException() throws Exception {
        when(persistenceService.selectOne(Matchers.<SelectOneDescriptor<SProfile>> any())).thenThrow(new SBonitaReadException(""));

        profileServiceImpl.getProfileByName("plop");
    }

    /**
     * Test method for {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#getProfiles(java.util.List)}.
     * 
     * @throws SProfileNotFoundException
     * @throws SBonitaReadException
     */
    @Test
    public final void getProfiles() throws SProfileNotFoundException, SBonitaReadException {
        final List<Long> profileIds = new ArrayList<Long>();
        profileIds.add(1L);
        profileIds.add(2L);

        final List<SProfile> sProfiles = new ArrayList<SProfile>();
        final SProfile sProfile = mock(SProfile.class);
        sProfiles.add(sProfile);
        sProfiles.add(sProfile);

        doReturn(sProfile).when(persistenceService).selectById(Matchers.<SelectByIdDescriptor<SProfile>> any());

        assertEquals(sProfiles, profileServiceImpl.getProfiles(profileIds));
    }

    @Test(expected = SProfileNotFoundException.class)
    public final void getNoProfiles() throws SProfileNotFoundException, SBonitaReadException {
        final List<Long> profileIds = new ArrayList<Long>();
        profileIds.add(1L);

        doReturn(null).when(persistenceService).selectById(Matchers.<SelectByIdDescriptor<SProfile>> any());

        profileServiceImpl.getProfiles(profileIds);
    }

    @Test
    public final void getProfilesWithEmptyList() throws SProfileNotFoundException {
        final List<Long> profileIds = new ArrayList<Long>();

        assertTrue(profileServiceImpl.getProfiles(profileIds).isEmpty());
    }

    @Test
    public final void getProfilesWithNullList() throws SProfileNotFoundException {
        assertTrue(profileServiceImpl.getProfiles(null).isEmpty());
    }

    /**
     * Test method for {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#getProfilesOfUser(long)}.
     * 
     * @throws SBonitaReadException
     * @throws SProfileNotFoundException
     */
    @Test
    public final void getProfilesOfUser() throws SBonitaReadException, SProfileNotFoundException {
        final List<SProfile> sProfiles = new ArrayList<SProfile>();
        final SProfile sProfile = mock(SProfile.class);
        sProfiles.add(sProfile);

        doReturn(sProfiles).when(persistenceService).selectList(Matchers.<SelectListDescriptor<SProfile>> any());

        assertEquals(sProfiles, profileServiceImpl.getProfilesOfUser(1));
    }

    @Test
    public final void getNoProfilesOfUser() throws SBonitaReadException, SProfileNotFoundException {
        final List<SProfile> sProfiles = new ArrayList<SProfile>();

        doReturn(sProfiles).when(persistenceService).selectList(Matchers.<SelectListDescriptor<SProfile>> any());

        assertEquals(sProfiles, profileServiceImpl.getProfilesOfUser(1));
    }

    @Test(expected = SProfileNotFoundException.class)
    public final void getProfilesOfUserThrowException() throws SBonitaReadException, SProfileNotFoundException {
        doThrow(new SBonitaReadException("plop")).when(persistenceService).selectList(Matchers.<SelectListDescriptor<SProfile>> any());

        profileServiceImpl.getProfilesOfUser(1);
    }

    /**
     * Test method for {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#createProfile(org.bonitasoft.engine.profile.model.SProfile)}.
     * 
     * @throws SProfileCreationException
     * @throws SRecorderException
     */
    @Test
    public final void createProfile() throws SProfileCreationException, SRecorderException {
        final SProfile sProfile = mock(SProfile.class);
        doReturn(1L).when(sProfile).getId();

        doReturn(false).when(eventService).hasHandlers(anyString(), any(EventActionType.class));
        doNothing().when(recorder).recordInsert(any(InsertRecord.class), any(SInsertEvent.class));
        doReturn(false).when(queriableLoggerService).isLoggable(anyString(), any(SQueriableLogSeverity.class));

        final SProfile result = profileServiceImpl.createProfile(sProfile);
        assertNotNull(result);
        assertEquals(sProfile, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void createNullProfile() throws Exception {
        profileServiceImpl.createProfile(null);
    }

    /**
     * Test method for
     * {@link org.bonitasoft.engine.profile.impl.ProfileServiceImpl#updateProfile(org.bonitasoft.engine.profile.model.SProfile, org.bonitasoft.engine.recorder.model.EntityUpdateDescriptor)}
     * 
     * @throws SBonitaReadException
     * @throws SRecorderException
     * @throws SProfileUpdateException
     */
    @Test
    public final void updateProfile() throws SProfileUpdateException {
        final SProfile sProfile = mock(SProfile.class);
        doReturn(3L).when(sProfile).getId();
        final SProfileUpdateBuilder SProfileUpdateBuilder = new SProfileUpdateBuilderImpl();
        SProfileUpdateBuilder.setDescription("newDescription").setName("newName");

        doReturn(false).when(eventService).hasHandlers(anyString(), any(EventActionType.class));
        doReturn(false).when(queriableLoggerService).isLoggable(anyString(), any(SQueriableLogSeverity.class));

        final SProfile result = profileServiceImpl.updateProfile(sProfile, SProfileUpdateBuilder.done());
        assertNotNull(result);
        assertEquals(sProfile, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void updateNullProfile() throws SProfileUpdateException {
        final SProfileUpdateBuilder SProfileUpdateBuilder = new SProfileUpdateBuilderImpl();

        profileServiceImpl.updateProfile(null, SProfileUpdateBuilder.done());
    }

    @Test(expected = IllegalArgumentException.class)
    public final void updateProfileWithNullDescriptor() throws SProfileUpdateException {
        final SProfile sProfile = mock(SProfile.class);
        doReturn(3L).when(sProfile).getId();

        profileServiceImpl.updateProfile(sProfile, null);
    }

    @Test(expected = SProfileUpdateException.class)
    public final void updateActorThrowException() throws SRecorderException, SProfileUpdateException {
        final SProfile sProfile = mock(SProfile.class);
        doReturn(3L).when(sProfile).getId();
        final SProfileUpdateBuilder SProfileUpdateBuilder = new SProfileUpdateBuilderImpl();
        SProfileUpdateBuilder.setDescription("newDescription").setName("newName");

        doThrow(new SRecorderException("plop")).when(recorder).recordUpdate(any(UpdateRecord.class), any(SUpdateEvent.class));

        profileServiceImpl.updateProfile(sProfile, SProfileUpdateBuilder.done());
    }
}
