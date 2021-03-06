/* 
 * Copyright 2012 Phil Pratt-Szeliga and other contributors
 * http://chirrup.org/
 * 
 * See the file LICENSE for copying permission.
 */

package edu.syr.pcpratts.rootbeer.compiler4;

import java.util.List;
import soot.G;
import soot.PackManager;
import soot.Transform;

public class RootbeerCompiler4 {

  public void compile(String in_jar, String out_jar){
    
    FindGpuMethodTransform finder = new FindGpuMethodTransform();
    Transform transform1 = new Transform("jtp.FindGpuMethodTransform", finder);
    PackManager.v().getPack("jtp").add(transform1);
    
    int size = 4;
    String[] soot_args = new String[size];
    soot_args[0] = "-process-dir";
    soot_args[1] = in_jar;
    soot_args[2] = "-pp";
    soot_args[3] = "-allow-phantom-refs";
    soot.Main.main(soot_args);
    
    G.reset();
    
    List<String> kernel_classes = finder.getKernelClasses();
    for(String kernel : kernel_classes){
      System.out.println(kernel);
    }
    RootbeerTransform rootbeer_transform = new RootbeerTransform(kernel_classes);
    Transform transform2 = new Transform("wjtp.RootbeerTransform", rootbeer_transform);
    PackManager.v().getPack("wjtp").add(transform2);
    
    size = 6;
    soot_args = new String[size];
    soot_args[0] = "-process-dir";
    soot_args[1] = in_jar;
    soot_args[2] = "-pp";
    soot_args[3] = "-allow-phantom-refs";
    soot_args[4] = "-include-all";
    soot_args[5] = "-whole-program";
    soot.Main.main(soot_args);
  }      
}
