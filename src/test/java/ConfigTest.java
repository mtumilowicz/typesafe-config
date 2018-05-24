import com.typesafe.config.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by mtumilowicz on 2018-05-24.
 */
class ConfigTest {
    
    private final static Config conf = ConfigFactory.load();
    
    @Test
    void missing_key() {
        assertThrows(ConfigException.Missing.class, () -> conf.getString("non-existent"));
    }

    @Test
    void null_value() {
        assertThrows(ConfigException.Null.class, () -> conf.getString("conf.null_value_key"));
    }
    
    @Test
    void project_name() {
        assertEquals("typesafe-config", conf.getString("conf.project_name"));
    }
    
    @Test
    void version() {
        assertEquals("1.0-SNAPSHOT", conf.getString("predefined.version"));
    }

    @Test
    void project_version_substitution() {
        assertEquals("1.0-SNAPSHOT", conf.getString("conf.project_version"));
    }

    @Test
    void artifact_version_substitution() {
        assertEquals("1.0-SNAPSHOT", conf.getString("conf.artifact_version"));
    }
    
    @Test
    void author() {
        ConfigObject author = conf.getObject("conf.author");
        Config asConfig = author.toConfig();
        assertEquals("michal", asConfig.getString("name"));
        assertEquals("tumilowicz", asConfig.getString("surname"));
    }
    
    @Test
    void authorBean() {
        assertEquals(new Author("michal", "tumilowicz"), 
                ConfigBeanFactory.create(conf.getObject("conf.author").toConfig(), Author.class) );
    }
    
    @Test
    void persistence() {
        ConfigObject persistence = conf.getObject("conf.persistence");
        Config asConfig = persistence.toConfig();
        assertEquals("JPA", asConfig.getString("specification"));
        assertEquals("EclipseLink", asConfig.getString("provider"));
        assertFalse(asConfig.getBoolean("cache"));
        assertEquals("Oracle", asConfig.getString("database"));
    }
    
    @Test
    void branch_east() {
        ConfigObject branchEast = conf.getObject("conf.branch_east");
        Config asConfig = branchEast.toConfig();
        assertEquals("mtumilowicz holding", asConfig.getString("name"));
        assertEquals("east", asConfig.getString("branch_name"));
    }
    
    @Test
    void login() {
        assertEquals("admin", conf.getString("conf.login"));
    }
    
    @Test
    void web_container() {
        assertEquals("GlassFish", conf.getString("conf.web_container"));
    }
    
    @Test
    void languages() {
        assertEquals(Arrays.asList("english", "polish", "french"), conf.getStringList("conf.languages"));
    }
    
    @Test
    void user_statuses() {
        assertArrayEquals(UserStatus.values(),
                conf.getEnumList(UserStatus.class, "conf.user_status").toArray(new UserStatus[0]));
    }
}
