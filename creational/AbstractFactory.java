/*
    Extension of factory pattern, This one is so confusing that i cant even think of any quip on it
    If I were to compare this to regular factory, difference is this one is interface of object, not method
    You can view this as the format a factory must follow in order to become multipurpose and not just wrapper
    This pattern is the peak implementation of abstraction, there is nothing concrete in abstract factory.
 */

public class AbstractFactory {
    public static void main(String[] args) {
        //This Acts as Client code and suppose this var is given to client via ILogFactory, which we are not doing here for sake of simplicity
        FileLoggerFactory flFactory = new FileLoggerFactory();
        ILogger fl = flFactory.createLogger();
        fl.log("Hello World");
        ILogConnection flc = flFactory.createLogConnection();
        flc.open();
        flc.open();
        flc.close();
        ConsoleLoggerFactory clFactory = new ConsoleLoggerFactory();
        ILogger cl = clFactory.createLogger();
        cl.log("Hmm");
        ILogConnection clc = clFactory.createLogConnection();
        clc.close();
        clc.open();
        clc.close();
    }
}

interface ILogFactory {
    ILogger createLogger();
    ILogConnection createLogConnection();
}

interface ILogger {
    void log(String message);
}

class FileLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println("Data logged in File");
    }
}

class ConsoleLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println("Data printed on Console");
    }
}

class FileLogConnection implements ILogConnection {
    private int connection;

    @Override
    public void open() {
        if (connection == 0) {
            connection = 1;
            System.out.println("Connection Opened for file logger");
        } else {
            System.out.println("Connection is already made for file logger");
        }
    }

    @Override
    public void close() {
        if (connection == 0) {
            System.out.println("No connection present for file logger");
        } else {
            connection = 0;
            System.out.println("Connection Closed for file logger");
        }
    }
}

class ConsoleLogConnection implements ILogConnection {
    private int connection;

    @Override
    public void open() {
        if (connection == 0) {
            connection = 1;
            System.out.println("Connection Opened for console logger");
        } else {
            System.out.println("Connection is already made for console logger");
        }
    }

    @Override
    public void close() {
        if (connection == 0) {
            System.out.println("No connection present for console logger");
        } else {
            connection = 0;
            System.out.println("Connection Closed for console logger");
        }
    }
}

interface ILogConnection {
    void open();
    void close();
}

class FileLoggerFactory implements ILogFactory {
    @Override
    public ILogger createLogger() {
        return new FileLogger();
    }

    @Override
    public ILogConnection createLogConnection() {
        return new FileLogConnection();
    }
}

class ConsoleLoggerFactory implements ILogFactory {
    @Override
    public ILogger createLogger() {
        return new ConsoleLogger();
    }

    @Override
    public ILogConnection createLogConnection() {
        return new ConsoleLogConnection();
    }
}