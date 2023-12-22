package com.inn.ticket.reservation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inn.ticket.reservation.IntegrationTest;
import com.inn.ticket.reservation.domain.Layout;
import com.inn.ticket.reservation.repository.LayoutRepository;
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
 * Integration tests for the {@link LayoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LayoutResourceIT {

    private static final Integer DEFAULT_TOTAL_ROWS = 1;
    private static final Integer UPDATED_TOTAL_ROWS = 2;

    private static final Integer DEFAULT_TOTAL_COLUMN = 1;
    private static final Integer UPDATED_TOTAL_COLUMN = 2;

    private static final String ENTITY_API_URL = "/api/layouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{layoutId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLayoutMockMvc;

    private Layout layout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Layout createEntity(EntityManager em) {
        Layout layout = new Layout().totalRows(DEFAULT_TOTAL_ROWS).totalColumn(DEFAULT_TOTAL_COLUMN);
        return layout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Layout createUpdatedEntity(EntityManager em) {
        Layout layout = new Layout().totalRows(UPDATED_TOTAL_ROWS).totalColumn(UPDATED_TOTAL_COLUMN);
        return layout;
    }

    @BeforeEach
    public void initTest() {
        layout = createEntity(em);
    }

    @Test
    @Transactional
    void createLayout() throws Exception {
        int databaseSizeBeforeCreate = layoutRepository.findAll().size();
        // Create the Layout
        restLayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layout)))
            .andExpect(status().isCreated());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeCreate + 1);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getTotalRows()).isEqualTo(DEFAULT_TOTAL_ROWS);
        assertThat(testLayout.getTotalColumn()).isEqualTo(DEFAULT_TOTAL_COLUMN);
    }

    @Test
    @Transactional
    void createLayoutWithExistingId() throws Exception {
        // Create the Layout with an existing ID
        layout.setLayoutId(1L);

        int databaseSizeBeforeCreate = layoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layout)))
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLayouts() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList
        restLayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=layoutId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].layoutId").value(hasItem(layout.getLayoutId().intValue())))
            .andExpect(jsonPath("$.[*].totalRows").value(hasItem(DEFAULT_TOTAL_ROWS)))
            .andExpect(jsonPath("$.[*].totalColumn").value(hasItem(DEFAULT_TOTAL_COLUMN)));
    }

    @Test
    @Transactional
    void getLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get the layout
        restLayoutMockMvc
            .perform(get(ENTITY_API_URL_ID, layout.getLayoutId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.layoutId").value(layout.getLayoutId().intValue()))
            .andExpect(jsonPath("$.totalRows").value(DEFAULT_TOTAL_ROWS))
            .andExpect(jsonPath("$.totalColumn").value(DEFAULT_TOTAL_COLUMN));
    }

    @Test
    @Transactional
    void getNonExistingLayout() throws Exception {
        // Get the layout
        restLayoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Update the layout
        Layout updatedLayout = layoutRepository.findById(layout.getLayoutId()).get();
        // Disconnect from session so that the updates on updatedLayout are not directly saved in db
        em.detach(updatedLayout);
        updatedLayout.totalRows(UPDATED_TOTAL_ROWS).totalColumn(UPDATED_TOTAL_COLUMN);

        restLayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLayout.getLayoutId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLayout))
            )
            .andExpect(status().isOk());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getTotalRows()).isEqualTo(UPDATED_TOTAL_ROWS);
        assertThat(testLayout.getTotalColumn()).isEqualTo(UPDATED_TOTAL_COLUMN);
    }

    @Test
    @Transactional
    void putNonExistingLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setLayoutId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, layout.getLayoutId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setLayoutId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(layout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setLayoutId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(layout)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLayoutWithPatch() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Update the layout using partial update
        Layout partialUpdatedLayout = new Layout();
        partialUpdatedLayout.setLayoutId(layout.getLayoutId());

        partialUpdatedLayout.totalColumn(UPDATED_TOTAL_COLUMN);

        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayout.getLayoutId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayout))
            )
            .andExpect(status().isOk());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getTotalRows()).isEqualTo(DEFAULT_TOTAL_ROWS);
        assertThat(testLayout.getTotalColumn()).isEqualTo(UPDATED_TOTAL_COLUMN);
    }

    @Test
    @Transactional
    void fullUpdateLayoutWithPatch() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Update the layout using partial update
        Layout partialUpdatedLayout = new Layout();
        partialUpdatedLayout.setLayoutId(layout.getLayoutId());

        partialUpdatedLayout.totalRows(UPDATED_TOTAL_ROWS).totalColumn(UPDATED_TOTAL_COLUMN);

        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLayout.getLayoutId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLayout))
            )
            .andExpect(status().isOk());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getTotalRows()).isEqualTo(UPDATED_TOTAL_ROWS);
        assertThat(testLayout.getTotalColumn()).isEqualTo(UPDATED_TOTAL_COLUMN);
    }

    @Test
    @Transactional
    void patchNonExistingLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setLayoutId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, layout.getLayoutId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setLayoutId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(layout))
            )
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();
        layout.setLayoutId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLayoutMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(layout)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeDelete = layoutRepository.findAll().size();

        // Delete the layout
        restLayoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, layout.getLayoutId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
