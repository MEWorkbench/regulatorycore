package pt.uminho.ceb.biosystems.mew.regulatorycore.integratedmodel.simulation.controlcenter;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.core.simulation.components.AbstractSimulationMethodsFactory;
import pt.uminho.ceb.biosystems.mew.solvers.lp.LPProblem;

public class IntegratedMethodsFactory extends AbstractSimulationMethodsFactory{

	public IntegratedMethodsFactory(Map<String, Class<?>> mapMethods) {
		super(mapMethods);
	}
 
	
	
	@SuppressWarnings({"unchecked"})
	private static <T extends LPProblem> Class<T> getProblemClass(final Class<?> klass){
		
		ParameterizedType parameterizedType =
			       (ParameterizedType) klass.getGenericSuperclass();
		
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	

	
	
}