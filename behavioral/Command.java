/*
    If you got time to waste, this pattern is perfect for you.
    Command pattern is essentially an abstraction that supposedly hides complexity of execution.
    As you know design patterns dont work over the network so client has all the code.
    This is like showing cables in back if pc case and calling it cable management.
    I would recommend you know existence of this one but dont implement it unless its perfect for the scenario.
    Only scenario where you are advised to apply this pattern is where object and methods must be decoupled dynamically.
 */

import java.util.HashMap;
import java.util.Map;

public class Command {
    public static void main(String[] args) {
        RemoteController remote = new RemoteController();
        remote.registerButton("on");
        remote.setButtonPress("on", new PowerOnCommand());
        remote.registerButton("off");
        remote.setButtonPress("off", new PowerOffCommand());
        remote.pressButton("on");
        remote.pressButton("off");
    }
}

interface ICommand {
    public void execute();
}

class PowerOffCommand implements ICommand {
    @Override
    public void execute() {
        System.out.println("Power Off...");
    }
}

class PowerOnCommand implements ICommand {
    @Override
    public void execute() {
        System.out.println("Power On...");
    }
}

class Button {
    private ICommand command;

    public void setCommand(ICommand command) {
        this.command = command;
    }

    public void press() {
        if (command != null) {
            command.execute();
        } else {
            System.err.println("Unmapped button");
        }  
    }
}

class RemoteController {
    private final Map<String, Button> buttonMap = new HashMap<>();

    public void registerButton(String slot) {
        Button newButton = new Button();
        buttonMap.put(slot, newButton);
    }

    public void setButtonPress(String slot, ICommand command) {
        if (buttonMap.containsKey(slot) && buttonMap.get(slot) != null) {
            Button btn = buttonMap.get(slot);
            btn.setCommand(command);
        } else {
            System.out.println("Empty Slot");
        }
    }

    public void pressButton(String slot) {
        if (buttonMap.containsKey(slot) && buttonMap.get(slot) != null) {
            Button btn = buttonMap.get(slot);
            btn.press();
        } else {
            System.out.println("Empty Slot");
        }
    }
}