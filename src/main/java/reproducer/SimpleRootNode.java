package reproducer;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

public class SimpleRootNode extends RootNode {
  public SimpleRootNode(TruffleLanguage<?> language) {
    super(language);
  }

  @Override
  public Object execute(VirtualFrame frame) {
    return 1;
  }
}
