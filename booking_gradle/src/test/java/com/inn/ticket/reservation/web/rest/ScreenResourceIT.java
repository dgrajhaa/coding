package com.inn.ticket.reservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inn.ticket.reservation.IntegrationTest;
import com.inn.ticket.reservation.domain.Screen;
import com.inn.ticket.reservation.repository.ScreenRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ScreenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScreenResourceIT {

    private static final Long DEFAULT_LAYOUT_ID = 1L;
    private static final Long UPDATED_LAYOUT_ID = 2L;

    private static final String DEFAULT_SCREEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCREEN_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/screens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{screenId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScreenMockMvc;

    private Screen screen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Screen createEntity(EntityManager em) {
        Screen screen = new Screen().layoutId(DEFAULT_LAYOUT_ID).screenName(DEFAULT_SCREEN_NAME);
        return screen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Screen createUpdatedEntity(EntityManager em) {
        Screen screen = new Screen().layoutId(UPDATED_LAYOUT_ID).screenName(UPDATED_SCREEN_NAME);
        return screen;
    }

    @BeforeEach
    public void initTest() {
        screen = createEntity(em);
    }

    @Test
    @Transactional
    void createScreen() throws Exception {
        int databaseSizeBeforeCreate = screenRepository.findAll().size();
        // Create the Screen
        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screen)))
            .andExpect(status().isCreated());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeCreate + 1);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getLayoutId()).isEqualTo(DEFAULT_LAYOUT_ID);
        assertThat(testScreen.getScreenName()).isEqualTo(DEFAULT_SCREEN_NAME);
    }

    @Test
    @Transactional
    void createScreenWithExistingId() throws Exception {
        // Create the Screen with an existing ID
        screen.setScreenId(1L);

        int databaseSizeBeforeCreate = screenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screen)))
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScreens() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList
        restScreenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=screenId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].screenId").value(hasItem(screen.getScreenId().intValue())))
            .andExpect(jsonPath("$.[*].layoutId").value(hasItem(DEFAULT_LAYOUT_ID.intValue())))
            .andExpect(jsonPath("$.[*].screenName").value(hasItem(DEFAULT_SCREEN_NAME)));
    }

    @Test
    @Transactional
    void getScreen() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get the screen
        restScreenMockMvc
            .perform(get(ENTITY_API_URL_ID, screen.getScreenId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.screenId").value(screen.getScreenId().intValue()))
            .andExpect(jsonPath("$.layoutId").value(DEFAULT_LAYOUT_ID.intValue()))
            .andExpect(jsonPath("$.screenName").value(DEFAULT_SCREEN_NAME));
    }

    @Test
    @Transactional
    void getNonExistingScreen() throws Exception {
        // Get the screen
        restScreenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScreen() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeUpdate = screenRepository.findAll().size();

        // Update the screen
        Screen updatedScreen = screenRepository.findById(screen.getScreenId()).get();
        // Disconnect from session so that the updates on updatedScreen are not directly saved in db
        em.detach(updatedScreen);
        updatedScreen.layoutId(UPDATED_LAYOUT_ID).screenName(UPDATED_SCREEN_NAME);

        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScreen.getScreenId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScreen))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getLayoutId()).isEqualTo(UPDATED_LAYOUT_ID);
        assertThat(testScreen.getScreenName()).isEqualTo(UPDATED_SCREEN_NAME);
    }

    @Test
    @Transactional
    void putNonExistingScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, screen.getScreenId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(screen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(screen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScreenWithPatch() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeUpdate = screenRepository.findAll().size();

        // Update the screen using partial update
        Screen partialUpdatedScreen = new Screen();
        partialUpdatedScreen.setScreenId(screen.getScreenId());

        partialUpdatedScreen.screenName(UPDATED_SCREEN_NAME);

        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScreen.getScreenId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScreen))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getLayoutId()).isEqualTo(DEFAULT_LAYOUT_ID);
        assertThat(testScreen.getScreenName()).isEqualTo(UPDATED_SCREEN_NAME);
    }

    @Test
    @Transactional
    void fullUpdateScreenWithPatch() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeUpdate = screenRepository.findAll().size();

        // Update the screen using partial update
        Screen partialUpdatedScreen = new Screen();
        partialUpdatedScreen.setScreenId(screen.getScreenId());

        partialUpdatedScreen.layoutId(UPDATED_LAYOUT_ID).screenName(UPDATED_SCREEN_NAME);

        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScreen.getScreenId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScreen))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getLayoutId()).isEqualTo(UPDATED_LAYOUT_ID);
        assertThat(testScreen.getScreenName()).isEqualTo(UPDATED_SCREEN_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, screen.getScreenId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(screen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(screen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(screen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScreen() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeDelete = screenRepository.findAll().size();

        // Delete the screen
        restScreenMockMvc
            .perform(delete(ENTITY_API_URL_ID, screen.getScreenId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
