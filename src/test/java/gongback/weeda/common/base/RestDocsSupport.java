package gongback.weeda.common.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

import static org.springframework.restdocs.snippet.Attributes.Attribute;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
public class RestDocsSupport {

    @Autowired
    protected WebTestClient webTestClient;

    protected final Attribute field(
            final String key,
            final String value
    ) {
        return new Attribute(key, value);
    }

    @BeforeEach
    void setUp(ApplicationContext applicationContext, RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext).configureClient()
                .filter(documentationConfiguration(restDocumentation))
                .build();
    }

    protected Consumer<EntityExchangeResult<byte[]>> getDocument(RequestParametersSnippet requestParametersSnippet) {
        return WebTestClientRestDocumentation.document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestParametersSnippet
        );
    }

    protected Consumer<EntityExchangeResult<byte[]>> getDocument(RequestParametersSnippet requestParametersSnippet,
                                                                 ResponseFieldsSnippet responseFieldsSnippet) {
        return WebTestClientRestDocumentation.document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet
        );
    }

    protected Consumer<EntityExchangeResult<byte[]>> getDocument(RequestFieldsSnippet requestFieldsSnippet,
                                                                 ResponseFieldsSnippet responseFieldsSnippet) {
        return WebTestClientRestDocumentation.document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                requestFieldsSnippet,
                responseFieldsSnippet
        );
    }
}
