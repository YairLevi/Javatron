import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class LogFormatter extends Formatter {
    // ANSI escape code
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Here you can configure the format of the output and
    // its color by using the ANSI escape codes defined above.

    // format is called for every console log message
    private String getColor(Level lvl) {
        if (lvl.equals(Level.INFO)) {
            return ANSI_BLUE;
        } else if (lvl.equals(Level.FINE)) {
            return ANSI_GREEN;
        } else if (lvl.equals(Level.WARNING)) {
            return ANSI_YELLOW;
        } else if (lvl.equals(Level.SEVERE)) {
            return ANSI_RED;
        } else if (lvl.equals(Level.CONFIG)) {
            return ANSI_WHITE;
        }
        return ANSI_WHITE;
    }

    @Override
    public String format(LogRecord record) {
        // This example will print date/time, class, and log level in yellow,
        // followed by the log message and it's parameters in white .
        StringBuilder builder = new StringBuilder();
        builder.append(getColor(record.getLevel()));

        builder.append("[");
        builder.append(calcDate(record.getMillis()));
        builder.append("]");

        builder.append(" [");
        builder.append(record.getSourceClassName());
        builder.append("]");

        builder.append(" [");
        builder.append(record.getLevel().getName());
        builder.append("]");

        builder.append(ANSI_WHITE);
        builder.append(" - ");
        builder.append(record.getMessage());

        Object[] params = record.getParameters();

        if (params != null) {
            builder.append("\t");
            for (int i = 0; i < params.length; i++) {
                builder.append(params[i]);
                if (i < params.length - 1)
                    builder.append(", ");
            }
        }

        builder.append(ANSI_RESET);
        builder.append("\n");
        return builder.toString();
    }

    private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }
}

class Log {
    private static final Logger log = Logger.getAnonymousLogger();
    static {
        ConsoleHandler h = new ConsoleHandler();
        h.setFormatter(new LogFormatter());
        log.setUseParentHandlers(false);
        log.addHandler(h);
    }
    static void INFO(String message, Object... args) {
        log.log(Level.INFO, String.format(message, args));
    }

    static void CONFIG(String message, Object... args) {
        log.log(Level.CONFIG, String.format(message, args));
    }

    static void WARNING(String message, Object... args) {
        log.log(Level.WARNING, String.format(message, args));
    }

    static void SEVERE(String message, Object... args) {
        log.log(Level.SEVERE, String.format(message, args));
    }
}
