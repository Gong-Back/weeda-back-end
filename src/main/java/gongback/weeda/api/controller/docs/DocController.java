package gongback.weeda.api.controller.docs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api-docs")
public class DocController {

    private final ResourceLoader resourceLoader;

    @Value("${apiDocs.filePath}")
    private String docsHtml;

    public DocController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Resource docs() throws IOException {
        log.info(resourceLoader.getResource("classpath:" + docsHtml).getURL().toString());
        log.info(resourceLoader.getResource("classpath:").getURL().toString());
        return resourceLoader.getResource("classpath:" + docsHtml);
    }
}
