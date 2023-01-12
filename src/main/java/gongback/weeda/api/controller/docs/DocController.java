package gongback.weeda.api.controller.docs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-docs")
public class DocController {

    @Value("classpath:/static/docs/api-docs.html")
    Resource html;

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Resource docs() {
        return html;
    }
}
