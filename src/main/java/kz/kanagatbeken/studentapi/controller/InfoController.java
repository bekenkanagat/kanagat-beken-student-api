package kz.kanagatbeken.studentapi.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final String studentName;
    private final ObjectProvider<BuildProperties> buildProperties;

    public InfoController(@Value("${app.student-name}") String studentName,
                          ObjectProvider<BuildProperties> buildProperties) {
        this.studentName = studentName;
        this.buildProperties = buildProperties;
    }

    @GetMapping
    public Map<String, String> info() {
        BuildProperties props = buildProperties.getIfAvailable();
        Map<String, String> info = new LinkedHashMap<>();
        info.put("application", "kanagat-beken-student-api");
        info.put("author", studentName);
        info.put("version", props != null ? props.getVersion() : "dev");
        return info;
    }
}
