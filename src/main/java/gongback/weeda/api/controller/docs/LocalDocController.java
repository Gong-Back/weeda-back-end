package gongback.weeda.api.controller.docs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Profile("local")
@RequestMapping("/api-docs")
public class LocalDocController {

    private final ResourceLoader resourceLoader;

    @Value("${apiDocs.localFilePath}")
    private String docsHtml;

    public LocalDocController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Resource docs() {
        return resourceLoader.getResource(docsHtml);
    }
}
