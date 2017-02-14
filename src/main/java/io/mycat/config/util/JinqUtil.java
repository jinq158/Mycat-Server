package io.mycat.config.util;

public class JinqUtil {
	public static void printClassMethod(int showParentlevel  ,Object... methodParam){
		int stackSize=Thread.currentThread().getStackTrace().length;
		StringBuffer sb=new StringBuffer();
		System.out.println("*********************");
		if(showParentlevel==-1){
			showParentlevel=stackSize-3;
		}
		for(int i=showParentlevel;i>=0;i--){
			if(i==0){
				StackTraceElement ele=Thread.currentThread().getStackTrace()[2];
				System.err.print(sb.toString()+ele);
				for(Object o:methodParam){
					System.err.print("  "+o);
				}
				System.err.println("\n");
				return;
			}
			if(stackSize>showParentlevel+2){
				StackTraceElement ele=Thread.currentThread().getStackTrace()[2+i];
				System.out.println(sb.toString()+ele);
//				System.out.println(sb.toString()+ele.getClassName()+"."+ele.getMethodName()+" [line:"+ele.getLineNumber()+"]");
				sb.append("  ");
			}
		}
		
	}
}
