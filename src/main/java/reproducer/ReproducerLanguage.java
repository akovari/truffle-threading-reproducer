package reproducer;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.InteropLibrary;
import reproducer.runtime.Context;

@TruffleLanguage.Registration(id = ReproducerLanguage.ID, name = "reproducer", defaultMimeType = ReproducerLanguage.MIME_TYPE, characterMimeTypes = ReproducerLanguage.MIME_TYPE, contextPolicy = TruffleLanguage.ContextPolicy.SHARED, fileTypeDetectors = FiletypeDetector.class)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, StandardTags.ExpressionTag.class, StandardTags.ReadVariableTag.class, StandardTags.WriteVariableTag.class, DebuggerTags.AlwaysHalt.class})
public class ReproducerLanguage extends TruffleLanguage<Context> {
  public static final String ID = "reproducer";
  public static final String MIME_TYPE = "application/x-reproducer";

  public ReproducerLanguage() {
    super();
  }

  @Override
  protected Context createContext(Env env) {
    return new Context(this, env);
  }

  @Override
  protected void initializeContext(Context context) {
    context.initialize();
  }

  @Override
  protected void finalizeContext(Context context) {
    context.dispose();
  }

  @Override
  public CallTarget parse(ParsingRequest request) {
    return Truffle.getRuntime().createCallTarget(new SimpleRootNode(this));
  }

  @Override
  protected boolean isVisible(Context context, Object value) {
    return !InteropLibrary.getFactory().getUncached(value).isNull(value);

  }

  @Override
  protected boolean isThreadAccessAllowed(Thread thread, boolean singleThreaded) {
    return true;
  }
}
