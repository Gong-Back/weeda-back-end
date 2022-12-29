package gongback.weeda.util.base;

import gongback.weeda.util.TransactionStepVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.ComponentScan;

@DataR2dbcTest
@ComponentScan(basePackages = {"gongback.weeda.util"})
public class RepositoryUnitTest {

    @Autowired
    protected TransactionStepVerifier transactionStepVerifier;
}
