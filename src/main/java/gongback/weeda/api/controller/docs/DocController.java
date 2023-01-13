package gongback.weeda.api.controller.docs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api-docs")
public class DocController {

    private final FileSystemResourceLoader fileSystemResourceLoader;

    private final String PROFILE_INFO = "spring.profiles.active";
    private final String LOCAL = "local";

    @Value("${apiDocs.localFilePath}")
    private String localDocsHtml;

    @Value("${apiDocs.prodFilePath}")
    private String prodDocsHtml;

    public DocController() {
        this.fileSystemResourceLoader = new FileSystemResourceLoader();
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Resource docs() {
        if (System.getProperty(PROFILE_INFO).equalsIgnoreCase(LOCAL)) {
            return fileSystemResourceLoader.getResource(localDocsHtml);
        }

        return fileSystemResourceLoader.getResource(prodDocsHtml);
    }
}
