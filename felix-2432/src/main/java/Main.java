


public class Main {

    public void testCoercion() throws Exception
    {
        Context c = new Context();
        c.addCommand("echo", this);

        // FELIX-2432
        boolean b = "null x".equals(c.execute("echo $expandsToNull x"));
        System.out.println(b);
    }

    public CharSequence echo(Object args[])
    {
        if (args == null)
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Object arg : args)
        {
            if (arg != null)
            {
                if (sb.length() > 0)
                    sb.append(' ');
                sb.append(arg);
            }
        }
        return sb.toString();
    }
    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.testCoercion();
    }
}