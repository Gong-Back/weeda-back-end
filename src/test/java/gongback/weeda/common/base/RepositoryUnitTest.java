package gongback.weeda.common.base;

import gongback.weeda.common.TransactionStepVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(TransactionStepVerifier.class)
public class RepositoryUnitTest {

    @Autowired
    protected TransactionStepVerifier transactionStepVerifier;
}
