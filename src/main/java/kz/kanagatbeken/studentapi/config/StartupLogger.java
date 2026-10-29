package kz.kanagatbeken.studentapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final String studentName;
    private final ObjectProvider<BuildProperties> buildProperties;

    public StartupLogger(@Value("${app.student-name}") String studentName,
                         ObjectProvider<BuildProperties> buildProperties) {
        this.studentName = studentName;
        this.buildProperties = buildProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        BuildProperties props = buildProperties.getIfAvailable();
        String version = props != null ? props.getVersion() : "dev";
        log.info("Student API by {} started, version {}", studentName, version);
    }
}
