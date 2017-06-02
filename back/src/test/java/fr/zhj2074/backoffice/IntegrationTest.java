package fr.zhj2074.backoffice;

import datability.Databases;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
@Transactional
@Rollback
@org.springframework.boot.test.IntegrationTest()
public abstract class IntegrationTest {

    protected MockMvc mockMvc;
    @Autowired
    protected JdbcTemplate jdbc;
    @Autowired
    protected NamedParameterJdbcTemplate namedJdbc;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setupDeps() throws Exception {
        String[] tables = {"users"};
        Databases.postgresql(DataSourceUtils.getConnection(jdbc.getDataSource())).dropAll(tables);
        JdbcTestUtils.deleteFromTables(jdbc, tables);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(new CharacterEncodingFilter("UTF8", true)).build();
    }
}
