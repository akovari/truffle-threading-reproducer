package reproducer;

import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.RepeatedTest;

public class ReproducerTest {
  @RepeatedTest(10)
  public void simpleTest() {
    Context context = Context.newBuilder().allowAllAccess(true).build();
    context.eval(ReproducerLanguage.ID, "");
    context.close();
  }
}
