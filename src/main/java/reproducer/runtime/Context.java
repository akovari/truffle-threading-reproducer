package reproducer.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLogger;
import reproducer.ReproducerLanguage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Context {
  private final TruffleLanguage.Env env;
  private final BufferedReader input;
  private final PrintWriter output;
  private final ReproducerLanguage language;
  private ExecutorService ioExecutor;

  public Context(final ReproducerLanguage language, final TruffleLanguage.Env env) {
    this.env = env;
    this.input = new BufferedReader(new InputStreamReader(env.in()));
    this.output = new PrintWriter(env.out(), true);
    this.language = language;
  }

  public void initialize() {
    System.out.println("Reproducer Context initializing");
    this.ioExecutor = Executors.newCachedThreadPool(runnable -> env.createThread(runnable, null, new ThreadGroup("reproducer-io")));
    this.ioExecutor.submit(() -> {
      return;
    });
    System.out.println("Reproducer Context initialized");
  }

  @CompilerDirectives.TruffleBoundary
  public void dispose() {
    System.out.println("Threading shutting down");
    ioExecutor.shutdown();
    assert ioExecutor.shutdownNow().isEmpty();
    assert ioExecutor.isShutdown();
    while (!ioExecutor.isTerminated()) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("Threading shut down");
  }
}
