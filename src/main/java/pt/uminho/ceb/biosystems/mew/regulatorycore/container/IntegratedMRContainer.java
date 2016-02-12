package pt.uminho.ceb.biosystems.mew.regulatorycore.container;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.CompartmentCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionConstraintCI;
import pt.uminho.ceb.biosystems.mew.regulatorycore.container.interfaces.IContainerIntegratedMRBuilder;

public class IntegratedMRContainer extends Container{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected RegulatoryContainer regcontainer;
	
	
	public IntegratedMRContainer(){
		
		reactions = new HashMap<String, ReactionCI>();
		metabolites = new HashMap<String, MetaboliteCI>();
		compartments = new HashMap<String, CompartmentCI>();
		genes = new HashMap<String, GeneCI>();
		defaultEC = new HashMap<String, ReactionConstraintCI>();
		metabolitesExtraInfo = new HashMap<String, Map<String,String>>();
		reactionsExtraInfo = new HashMap<String, Map<String,String>>();
		regcontainer = new RegulatoryContainer();
		
	}
	
	
	public IntegratedMRContainer(IContainerIntegratedMRBuilder builder){
		
		populateContainer(builder);
		regcontainer=builder.getRegulatoryContainer();
		
	}
	
	public IntegratedMRContainer (Container metcont){
		super(metcont.clone());
		
	}
	
	


	public RegulatoryContainer getRegcontainer() {
		return regcontainer;
	}


	public void setRegcontainer(RegulatoryContainer regcontainer) {
		this.regcontainer = regcontainer;
	}

	
	
	
	

}
