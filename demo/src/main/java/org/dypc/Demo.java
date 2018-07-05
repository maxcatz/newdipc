package org.dypc;

import java.io.IOException;

public class Demo {

    public static void main(String[] args) throws IOException {
        PropertyConductor conductor = new PropertyConductor("./config/application.yml");

        conductor.setProperty("Machine.cpu","\"o&oo*o&&o&&&&&&&&&&&&&&&&&&&&&&&&&&oo&o&o&o&\"         III          III             ##\"");
        conductor.setProperty("Machine.DiskSpace","\"oo&    oo&&&&&&&&&&&&&&&&&&&&&&&&&&&&oo    &oo*\"       III          III             ##\"");
        conductor.setProperty("Machine.Memory","\"oo&    oo&&&&&&&&&&&&&&&&&&&&&&&&&&&&oo    &oo*\"        III          III             ##\"");
        conductor.setProperty("Machine.freeDisk","\"o&&   8@  oo&o&&&&&&&&&&&&&&&&&&&&&o&o  8@   o&&*\"      III          III    iii      ##\"");
        conductor.setProperty("Machine.freeMem","\"&oo   @@@@:  oo&oo&&&&&&&&&&&&&&&o&o&:  @@@@   &oo\"      III          III    iii      ##\"");
        conductor.setProperty("Machine.newProcesses","\"&&&  8@@@@@   *o&o&&&&&&&&&&&&&&&&&&   .@@@@@  *oo\"      IIIIIIIIIIIIIIII             ##\"");
        conductor.setProperty("Machine.LastConnecTime","\"&o&  8@@@@   @  o&&&&&&&&&&&&&&&o&  8o  #@@@@  *oo\"      IIIIIIIIIIIIIIII    iii      ##\"");
        conductor.setProperty("Machine.Process","\"ooo   #@@@@@@@@& *oo&&&&&&&&&&o  @@@@@@@@@   &o&\"       III          III    iii      ##\"");
        conductor.setProperty("Machine.ProcIncoming","\"ooo*    o@@@@@@#  &oo&&&&&&&: #@@@@@@@     o&o\"        III          III    iii      ##\"");
        conductor.setProperty("Machine.ProcInternal","\"oo&&             &oo&&&&o*            ooo&\"          III          III    ii\"");
        conductor.setProperty("Machine.ProcOutgoing","\"&o&&&ooooo&*   oo&&&&/  .ooooooo&o&&.\"            III          III    iii      ##\"");
        conductor.setProperty("Machine.ProcConnectionCount","\"oooo&&&&&o&&&&&oo&o&&&oo&o.\"                 III          III    iii      ##\"");
        conductor.flash();
    }
}
