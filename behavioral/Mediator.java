/*
    This one acts as central command center for some sort of network of classes.
    You can think of it as router, but if you could you would not be reading this.
    Its good for decoupling and avoiding spaghetti code like some spider web.
    It adds bit of performance hit and might act as god object if not carefull.
    Its uses are limited but irreplacable, so just remember some example and call it a day.
 */

import java.util.HashMap;
import java.util.Map;

public class Mediator {
    public static void main(String[] args) {
        MemoryController controller = new MemoryController();

        // 1. Register two distinct processes
        Process procA = controller.RegisterNewProcess();
        Process procB = controller.RegisterNewProcess();

        System.out.println("--- Scenario 1: Allocation ---");
        procA.ReserveBlock("0x001", "Secret Data From A");
        System.out.println("\n--- Scenario 2: Cross-Process Query ---");
        procB.QueryAddress("0x001");
        System.out.println("\n--- Scenario 3: Memory Contention ---");
        procB.ReserveBlock("0x001", "Proc B's Data");
        System.out.println("\n--- Scenario 4: Proper Release and Reallocation ---");
        procA.MarkDone("0x001");
        procB.ReserveBlock("0x001", "Proc B's Data");
    }
}

interface IEventController {
    public void notify(Event e);
}

class MemoryController implements IEventController {
    public Map<String, String> memoryMap = new HashMap<>();
    public Map<String, Process> processMap = new HashMap<>();
    public Integer procCounter = 0;

    @Override
    public void notify(Event e) {
        String process = e.source;
        String requestType = "";
        String address = "";
        if (e.payload.containsKey("type") && e.payload.get("type") instanceof String) {
            requestType = (String) e.payload.get("type");
        }
        if (e.payload.containsKey("address") && e.payload.get("address") instanceof String) {
            address = (String) e.payload.get("address");
        }
        switch (requestType) {
            case "memAlloc":
                Boolean success = false;
                if (address != null && !address.isEmpty()) {
                    if (!memoryMap.containsKey(address)) {
                        memoryMap.put(address, process);
                        success = true;
                    } else {
                        String memOwner = memoryMap.get(address);
                        if (processMap.get(memOwner).ReleaseMemory(address)) {
                            memoryMap.put(address, process);
                            success = true;
                        } else {
                            System.out.println("Memory is still in use by process " + memOwner);
                        }
                    }
                    if (!success) {
                        processMap.get(process).RevertAllocation(address);
                    } else {
                        System.out.println("Memory " + address + " allocated to process " + process);
                    }
                } else {
                    System.err.println("Invalid memory address!");
                }
                break;
            case "memGet":
                if (address != null && !address.isEmpty()) {
                    if (memoryMap.containsKey(address)) {
                        String memOwner = memoryMap.get(address);
                        Object data = processMap.get(memOwner).GetMemoryData(address);
                        if (data != null) {
                            processMap.get(process).QueryResponse(data);
                        } else {
                            System.out.println("No data found");
                        }
                    } else {
                        System.out.println("Memory is unallocated");
                    }
                } else {
                    System.err.println("Invalid memory address!");
                }
                break;
            default:
                System.err.println("Invalid event!");
        }
    }

    public Process RegisterNewProcess() {
        procCounter += 1;
        Process p = new Process(this, Integer.toString(procCounter));
        processMap.put(Integer.toString(procCounter), p);
        return p;
    }
}

class Event {
    public String source;
    public Map<String, Object> payload;
}

class Process {
    private final Map<String, Object> memoryData = new HashMap<>();
    private final Map<String, Boolean> memoryLife = new HashMap<>();
    private final MemoryController controller;
    private final String processId;

    public Process(MemoryController controller, String processId) {
        this.controller = controller;
        this.processId = processId;
    }

    public void ReserveBlock(String address, Object data) {
        if (address != null && !address.isEmpty()) {
            memoryData.put(address, data);
            memoryLife.put(address, true);
            Event memAlloc = new Event();
            memAlloc.source = processId;
            memAlloc.payload = new HashMap<>();
            memAlloc.payload.put("type", "memAlloc");
            memAlloc.payload.put("address", address);
            controller.notify(memAlloc);
        } else {
            System.err.println("Invalid address!");
        }
    }

    public void RevertAllocation(String address) {
        memoryData.remove(address);
        memoryLife.remove(address);
    }

    public void QueryAddress(String address) {
        Event memGet = new Event();
        memGet.source = processId;
        memGet.payload = new HashMap<>();
        memGet.payload.put("type", "memGet");
        memGet.payload.put("address", address);
        controller.notify(memGet);
    }

    public void QueryResponse(Object data) {
        System.out.println("Returned data: " + data);
    }

    public Object GetMemoryData(String address) {
        if (memoryData.containsKey(address)) {
            return memoryData.get(address);
        } else {
            System.err.println("Data not found");
            return null;
        }
    }

    public void MarkDone(String address) {
        if (memoryLife.containsKey(address)) {
            memoryLife.put(address, false);
            System.out.println("Memory " + address + " can be released.");
        } else {
            System.err.println("Address not assigned");
        }
    }

    public Boolean ReleaseMemory(String address) {
        if (memoryLife.containsKey(address)) {
            if (!memoryLife.get(address)) {
                memoryData.remove(address);
                memoryLife.remove(address);
                return true;
            } else {
                System.out.println("Memory cant be released.");
                return false;
            }
        } else {
            System.out.println("Memory not assigned.");
            return true;
        }
    }
}