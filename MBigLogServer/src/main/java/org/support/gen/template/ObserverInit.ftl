package ${rootPackageName}.gen;
import org.support.observer.*;
import org.support.function.*;
import org.support.gen.GenFile;

@GenFile
public final class ${rootClassName} {
	public static void init(Observer ob) {
	<#list methodsList as map>
	<#list map.methods as m>
	<#list m.keys as key>
		ob.reg("${key}", (Function${m.paramsSize}${m.functionTypes})${map.packageName}.${map.className}::${m.name}, ${m.paramsSize});
	</#list>
	</#list>
	</#list>
	}
}

