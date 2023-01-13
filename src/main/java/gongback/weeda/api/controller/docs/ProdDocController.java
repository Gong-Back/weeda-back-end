package gongback.weeda.api.controller.docs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Profile("prod")
@RequestMapping("/api-docs")
public class ProdDocController {

    private final FileSystemResourceLoader resourceLoader;

    @Value("${apiDocs.prodFilePath}")
    private String prodDocsHtml;

    public ProdDocController() {
        this.resourceLoader = new FileSystemResourceLoader();
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Resource docs() {
        return resourceLoader.getResource(prodDocsHtml);
    }
}
