import org.apache.felix.gogo.runtime.threadio.ThreadIOImpl;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.gogo.runtime.CommandProcessorImpl;
import org.apache.felix.gogo.runtime.CommandSessionImpl;

public class Context extends CommandProcessorImpl
{
    public static final String EMPTY = "";
    
    private static final ThreadIOImpl threadio;
    private final CommandSession session;

    static
    {
        threadio = new ThreadIOImpl();
        threadio.start();
    }

    public Context()
    {
        super(threadio, null);
        session = (CommandSessionImpl) createSession(System.in, System.out, System.err);
    }

    public Object execute(CharSequence source) throws Exception
    {
        Object result = new Exception();
        try
        {
            return result = session.execute(source);
        }
        finally
        {
            System.err.println("execute<" + source + "> = ("
                + (null == result ? "Null" : result.getClass().getSimpleName()) + ")("
                + result + ")\n");
        }
    }

    public void addCommand(String function, Object target)
    {
        addCommand("test", target, function);
    }

    public void set(String name, Object value)
    {
        session.put(name, value);
    }

}